/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
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
 * @author hoffmann
 */
public class MaltcmsClassPathProvider implements ClassPathProvider, PreferenceChangeListener {

    private Set<ClassPath> classPaths = new LinkedHashSet<ClassPath>();

    public MaltcmsClassPathProvider() {
        NbPreferences.forModule(IMaltcmsClassPathProvider.class).addPreferenceChangeListener(this);
        ClassPath bootClassPath = ClassPathSupport.createClassPath(System.getProperty("java.class.path"));
        classPaths.add(bootClassPath);
    }

    @Override
    public ClassPath findClassPath(FileObject fo, String string) {
        System.out.println("Trying to find classpath of type "+string+" for "+fo);
        return ClassPathSupport.createProxyClassPath(classPaths.toArray(new ClassPath[classPaths.size()]));
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent pce) {
        if (pce.getKey().equals("extClassPathRoot")) {
            System.out.println("Adding classPathRoot: " + pce.getNewValue());
            classPaths.clear();
            classPaths.add(ClassPathSupport.createClassPath(FileUtil.toFileObject(new File(pce.getNewValue()))));
        }
    }
}
