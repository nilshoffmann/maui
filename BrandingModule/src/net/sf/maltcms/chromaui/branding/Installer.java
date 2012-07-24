/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.branding;

import java.io.File;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        File projectsDir = new File(new File(System.getProperty("user.home")), "MauiProjects");
        if (!projectsDir.exists()) {
            projectsDir.mkdirs();
        }
        System.setProperty("netbeans.projects.dir", projectsDir.getAbsolutePath());
    }
}
