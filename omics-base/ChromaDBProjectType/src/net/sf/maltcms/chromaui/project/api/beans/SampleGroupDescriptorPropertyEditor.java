/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.beans;

import java.beans.PropertyEditorSupport;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public class SampleGroupDescriptorPropertyEditor extends PropertyEditorSupport {
    
    public SampleGroupDescriptorPropertyEditor() {
    }

    @Override
    public String getAsText() {
        ISampleGroupDescriptor id = (ISampleGroupDescriptor)getValue();
        return id==null?"<NA>":id.getDisplayName();
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        throw new IllegalArgumentException("Editing of SampleGroupDescriptor is not supported!");
    }
}
