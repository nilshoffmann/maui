/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nilshoffmann
 */
public class Utils {

    public static List<FileObject> getGroovyScripts(FileObject... groovyDirs) {
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
}
