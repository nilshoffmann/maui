/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import net.sf.maltcms.chromaui.groovy.options.GroovyScriptLocationsPanel;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 *
 * @author nilshoffmann
 */
public class Utils {

    public static List<FileObject> getGroovyScripts(FileObject... groovyDirs) {
        return getGroovyScripts(Arrays.asList(groovyDirs));
    }

    public static List<FileObject> getGroovyScripts(List<FileObject> groovyDirs) {
        List<FileObject> groovyScripts = new ArrayList<FileObject>();
        for (FileObject groovyDir : groovyDirs) {
            if (groovyDir.isValid()) {
                Enumeration<? extends FileObject> enumeration = groovyDir.
                        getChildren(true);
                while (enumeration.hasMoreElements()) {
                    FileObject child = enumeration.nextElement();
                    if (child.hasExt("groovy")) {
                        groovyScripts.add(child);

                    }
                }
            } else {
            }
        }
        return groovyScripts;
    }

    public static List<FileObject> getScriptLocations(IChromAUIProject icap) {
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
        String[] scriptLocations = NbPreferences.forModule(
                GroovyScriptLocationsPanel.class).get("scriptLocations", "").
                split(",");
        List<FileObject> scriptDirectories = new LinkedList<FileObject>();
        scriptDirectories.add(groovyDir);
        for (String str : scriptLocations) {
            if (!str.isEmpty()) {
                scriptDirectories.add(FileUtil.toFileObject(new File(str)));
            }
        }
        List<FileObject> scriptFiles = Utils.getGroovyScripts(scriptDirectories);
        return scriptFiles;
    }
}
