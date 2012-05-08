/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project;

import java.beans.PropertyEditorManager;
import java.util.UUID;
import net.sf.maltcms.chromaui.project.spi.beans.UUIDPropertyEditor;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        PropertyEditorManager.registerEditor(UUID.class, UUIDPropertyEditor.class);
    }
}
