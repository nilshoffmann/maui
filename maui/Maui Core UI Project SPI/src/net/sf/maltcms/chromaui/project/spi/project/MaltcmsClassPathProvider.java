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
package net.sf.maltcms.chromaui.project.spi.project;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import net.sf.maltcms.chromaui.project.api.IMaltcmsClassPathProvider;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbPreferences;

/**
 *
 * @author Nils Hoffmann
 */
public class MaltcmsClassPathProvider implements ClassPathProvider, PreferenceChangeListener {

    private Set<ClassPath> classPaths = new LinkedHashSet<>();

    public MaltcmsClassPathProvider() {
        NbPreferences.forModule(IMaltcmsClassPathProvider.class).addPreferenceChangeListener(this);
        ClassPath bootClassPath = ClassPathSupport.createClassPath(System.getProperty("java.class.path"));
        classPaths.add(bootClassPath);
    }

    @Override
    public ClassPath findClassPath(FileObject fo, String string) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Trying to find classpath of type {0} for {1}", new Object[]{string, fo});
        return ClassPathSupport.createProxyClassPath(classPaths.toArray(new ClassPath[classPaths.size()]));
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent pce) {
        if (pce.getKey().equals("extClassPathRoot")) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding classPathRoot: {0}", pce.getNewValue());
            classPaths.clear();
            classPaths.add(ClassPathSupport.createClassPath(FileUtil.toFileObject(new File(pce.getNewValue()))));
        }
    }
}
