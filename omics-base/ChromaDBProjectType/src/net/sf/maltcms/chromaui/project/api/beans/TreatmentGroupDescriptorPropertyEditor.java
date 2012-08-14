/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.beans;

import java.beans.PropertyEditorSupport;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupDescriptorPropertyEditor extends PropertyEditorSupport {
    
    public TreatmentGroupDescriptorPropertyEditor() {
    }

    @Override
    public String getAsText() {
        ITreatmentGroupDescriptor id = (ITreatmentGroupDescriptor)getValue();
        return id.getDisplayName();
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        throw new IllegalArgumentException("Editing of TreatmentGroupDescriptor is not supported!");
    }
}
