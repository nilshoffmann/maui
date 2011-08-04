/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import groovy.lang.GroovyClassLoader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.codehaus.groovy.control.CompilationFailedException;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.loaders.DataObject;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

@ActionID(category = "Groovy",
id = "net.sf.maltcms.chromaui.groovy.GenericGroovyRawDataAction")
@ActionRegistration(displayName = "#CTL_GenericGroovyRawDataAction")
@ActionReferences({
    @ActionReference(path = "Loaders/application/x-cdf/Actions", position = -400)
})
@Messages("CTL_GenericGroovyRawDataAction=Run Groovy Action")
public final class GenericGroovyRawDataAction implements ActionListener {

    private final List<DataObject> context;

    public GenericGroovyRawDataAction(List<DataObject> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IChromAUIProject icap = Utilities.actionsGlobalContext().lookup(
                IChromAUIProject.class);
        if (icap != null) {
            GroovyClassLoader gcl = new GroovyClassLoader();
            File fo = FileUtil.toFile(icap.getLocation());
            FileObject groovyDir = null;
            if (!new File(fo, "groovy").isDirectory()) {
                try {
                    groovyDir = icap.getLocation().createFolder("groovy");
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                groovyDir = icap.getLocation().getFileObject("groovy/");
            }


            List<RawDataGroovyScript> groovyScripts = new LinkedList<RawDataGroovyScript>();
            if (groovyDir.isValid()) {
                Enumeration<? extends FileObject> enumeration = groovyDir.
                        getChildren(true);
                while (enumeration.hasMoreElements()) {
                    FileObject child = enumeration.nextElement();
                    if (child.hasExt("groovy")) {
                        Class clazz;
                        try {
                            clazz = gcl.parseClass(FileUtil.toFile(child));
                            Class<RawDataGroovyScript> irdgs = clazz.asSubclass(
                                    RawDataGroovyScript.class);
                            RawDataGroovyScript script = irdgs.newInstance();
                            groovyScripts.add(script);
                        } catch (InstantiationException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (IllegalAccessException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (CompilationFailedException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }

                    }
                }
            } else {
            }

            GroovyScriptSelectionForm gssf = new GroovyScriptSelectionForm();
            gssf.setModel(groovyScripts);
            DialogDescriptor dd = new DialogDescriptor(gssf,
                    "Please select a script for execution");
            Object ret = DialogDisplayer.getDefault().notify(dd);
            if (DialogDescriptor.OK_OPTION.equals(ret)) {
                RawDataGroovyScript selectedScript = gssf.getSelectedScript(
                        RawDataGroovyScript.class);
                if (selectedScript != null) {
                    selectedScript.setProject(icap);
                    selectedScript.setDataObjects(this.context.toArray(new CDFDataObject[this.context.
                            size()]));
                    RequestProcessor.getDefault().post(selectedScript);
                }
            } else {
            }




        }
    }
}
