/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.beans;

import java.beans.PropertyEditorSupport;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;

/**
 *
 * @author hoffmann
 */
public class SeparationTypePropertyEditor extends PropertyEditorSupport {
    
    public SeparationTypePropertyEditor() {
    }

    @Override
    public String getAsText() {
        ISeparationType id = (ISeparationType)getValue();
        return id.getSeparationType();
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        throw new IllegalArgumentException("Editing of SeparationType is not supported!");
    }
}
