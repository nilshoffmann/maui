/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project;

import java.beans.PropertyEditorManager;
import java.util.UUID;
import net.sf.maltcms.chromaui.project.api.beans.*;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        PropertyEditorManager.registerEditor(UUID.class, UUIDPropertyEditor.class);
        PropertyEditorManager.registerEditor(IDetectorType.class, DetectorTypePropertyEditor.class);
        PropertyEditorManager.registerEditor(ISeparationType.class, SeparationTypePropertyEditor.class);
        PropertyEditorManager.registerEditor(ITreatmentGroupDescriptor.class, TreatmentGroupDescriptorPropertyEditor.class);
        PropertyEditorManager.registerEditor(ISampleGroupDescriptor.class, SampleGroupDescriptorPropertyEditor.class);
    }
}
