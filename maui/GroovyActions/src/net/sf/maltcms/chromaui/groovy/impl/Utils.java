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
package net.sf.maltcms.chromaui.groovy.impl;

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
import org.openide.util.NbPreferences;

/**
 *
 * @author Nils Hoffmann
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
//                Exceptions.printStackTrace(ex);
            }
        } else {
            groovyDir = icap.getLocation().getFileObject("groovy/");
        }
        FileObject scriptsGroovyDir = null;
        if (!new File(fo, "scripts/groovy").isDirectory()) {
            try {
                scriptsGroovyDir = icap.getLocation().createFolder("scripts/groovy");
            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
            }
        } else {
            scriptsGroovyDir = icap.getLocation().getFileObject("scripts/groovy/");
        }
        String[] scriptLocations = NbPreferences.forModule(
                GroovyScriptLocationsPanel.class).get("scriptLocations", "").
                split(",");
        List<FileObject> scriptDirectories = new LinkedList<FileObject>();
        scriptDirectories.add(groovyDir);
        scriptDirectories.add(scriptsGroovyDir);
        for (String str : scriptLocations) {
            if (!str.isEmpty()) {
                scriptDirectories.add(FileUtil.toFileObject(new File(str)));
            }
        }
        List<FileObject> scriptFiles = Utils.getGroovyScripts(scriptDirectories);
        return scriptFiles;
    }
}
