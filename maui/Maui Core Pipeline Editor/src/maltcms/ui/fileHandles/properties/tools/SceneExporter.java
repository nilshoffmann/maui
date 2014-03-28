/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package maltcms.ui.fileHandles.properties.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGeneralConfigWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.visual.widget.Widget;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Mathias Wilhelm
 */
public class SceneExporter {

    private FileObject file;
    private boolean oneFile;
    private PipelineGraphScene scene;
    private String name;

    public SceneExporter(String directory, String name, PipelineGraphScene scene) {
        this(directory, name, false, scene);
    }

    public SceneExporter(String directory, String name,
            boolean exportToOneFile, PipelineGraphScene scene) {
        try {
            this.file = FileUtil.createFolder(new File(directory));
            if (name.endsWith(".mpl")) {
                this.name = name.substring(0, name.lastIndexOf(".mpl"));
            } else {
                this.name = name;
            }
            this.oneFile = exportToOneFile;
            this.scene = scene;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public boolean export() {
        Widget pipelinesLayer = SceneParser.getPipelinesLayer(this.scene), connectionLayer = SceneParser.getConnectionLayer(this.scene);
        if (connectionLayer != null && pipelinesLayer != null) {
            createConfigFiles(SceneParser.getPipeline(this.scene), SceneParser.getGeneralConfig(pipelinesLayer));
        }

        return true;
    }

    /**
     * Layout:
     * NAME/
     *  NAME.properties
     *  NAME-general.properties (optional)
     *  fragmentCommands/
     *      00_CLASSNAME/
     *          CLASSNAME.properties
     *      01_CLASSNAME/
     *          CLASSNAME.properties
     *
     * @param pipeline
     * @param general
     */
    private void createConfigFiles(List<PipelineElementWidget> pipeline, PipelineGeneralConfigWidget general) {
        try {
            //create a stack for FileLock objects to ease bookkeeping
//            Stack<FileLock> fls = new Stack<FileLock>();
            //create base config
            FileObject baseConfigFo = this.file.getFileObject(this.name + ".mpl");
            File f = FileUtil.toFile(baseConfigFo);
//            lock(baseConfigFo, fls);
            PropertiesConfiguration baseConfig = new PropertiesConfiguration();

            //create subdir "fragmentCommands"
            File subDir = new File(f.getParent(),"xml");
            FileUtil.createFolder(subDir);
//            subDir.mkdirs();
//            lock(subDir, fls);
            //retrieve general configuration
            Configuration generalConfig = general.getProperties();
            //only create and link, if non-empty
            if (!generalConfig.isEmpty()) {
                FileObject generalConfigFo = this.file.createData(this.name + "-general.properties");
//                lock(generalConfigFo, fls);
                //create outputstream for general config
                PropertiesConfiguration pc = new PropertiesConfiguration();
                ConfigurationUtils.copy(generalConfig, pc);
                pc.save(new PrintStream(generalConfigFo.getOutputStream()));
//                PrintStream baseConfigPrintStream = new PrintStream(generalConfigFo.getOutputStream());
//                //loop over config keys
//                ConfigurationUtils.dump(generalConfig, baseConfigPrintStream);
//                unlock(fls);
                //add to base configuration
                baseConfig.addProperty("include", this.name + "-general.properties");
            }

            //number scheme for subdirectories
            int cnt = 0;
            final int digits = (int) Math.ceil(Math.log10(pipeline.size())) + 1;

            //String list for pipeline elements
            List<String> pipelineElements = new LinkedList<String>();
            //String list for pipeline elements configuration locations
            List<String> pipelinePropertiesElements = new LinkedList<String>();
            for (PipelineElementWidget pw : pipeline) {
                //add full class name to pipeline elements
                pipelineElements.add(pw.getClassName());
                //add subdirs and specific configuration
                //get class name suffix
                String[] tmp = pw.getClassName().split("\\.");
                String cname = tmp[tmp.length - 1];
                //create numbered subdir with name (gives a better hint to contents,
                //than just the number
                File pipeElementSubDir = new File(subDir,String.format("%0" + digits + "d", cnt++)
                        + "_" + cname);
                FileObject pipeDirFo = FileUtil.createFolder(pipeElementSubDir);
//                pipeElementSubDir.mkdirs();
//                lock(pipeElementSubDir, fls);
                //create file object for pipeline element specific configuration
                File cfgf = new File(pipeElementSubDir,cname + ".properties");
                FileObject cfgFo = FileUtil.createData(cfgf);
                //write configuration to that file
                PropertiesConfiguration pc = new PropertiesConfiguration();
                ConfigurationUtils.copy(pw.getProperties(), pc);
                pc.save(new PrintStream(cfgFo.getOutputStream()));
//                unlock(fls);
                //append file path to other pipeline properties elements
                pipelinePropertiesElements.add(cfgf.getPath());
            }
            //set pipeline property
            baseConfig.setProperty("pipeline", pipelineElements);
            //set pipeline.properties property
            baseConfig.setProperty("pipeline.properties", pipelinePropertiesElements);
            FileObject fo = FileUtil.toFileObject(f);
            try {
                baseConfig.save(new PrintStream(fo.getOutputStream()));
                //release remaining file locks
                //            unlockAll(fls);
            } catch (ConfigurationException ex) {
                Exceptions.printStackTrace(ex);
            }
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void unlock(Stack<FileLock> s) {
        FileLock fl = s.pop();
        fl.releaseLock();
    }

    private void lock(FileObject fo, Stack<FileLock> s) {
        if (fo.isLocked()) {
            return;
        }
        FileLock fl = FileLock.NONE;
        try {
            fl = fo.lock();
        } catch (Throwable ex) {
//            Exceptions.printStackTrace(ex);
        }
        if (fl.equals(FileLock.NONE)) {
            return;
        }
        s.push(fl);
    }

    private void unlockAll(Stack<FileLock> s) {
        while (!s.isEmpty()) {
            s.pop().releaseLock();
        }
    }

    public static void showSaveDialog(PipelineGraphScene scene) {
        if (scene.getBaseFile() == null) {

            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "property files", "properties");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showSaveDialog(scene.getView());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                saveScene(scene, f);
            }

        } else {
            try {

                saveScene(scene, new File(scene.getBaseFile().getURL().toURI()));
            } catch (FileStateInvalidException ex) {
                Exceptions.printStackTrace(ex);
            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public static void saveScene(PipelineGraphScene scene, File configuration) {
        final SceneExporter exporter = new SceneExporter(configuration.getParent() + System.getProperty("file.separator"), configuration.getName(), scene);
        if (exporter.export()) {
            JOptionPane.showMessageDialog(scene.getView(), "Configuration saved!",
                    "Confirmation", 1);
        } else {
            JOptionPane.showMessageDialog(scene.getView(), "Configuration not saved! An Error Occured.",
                    "Saving Failed", 1);
        }
    }
}
