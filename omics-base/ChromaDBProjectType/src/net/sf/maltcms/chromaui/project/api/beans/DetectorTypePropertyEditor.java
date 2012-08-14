/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.beans;

import java.beans.PropertyEditorSupport;
import java.util.UUID;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;

/**
 *
 * @author hoffmann
 */
public class DetectorTypePropertyEditor extends PropertyEditorSupport {
    
    public DetectorTypePropertyEditor() {
    }

    @Override
    public String getAsText() {
        IDetectorType id = (IDetectorType)getValue();
        return id.getDetectorType();
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        throw new IllegalArgumentException("Editing of DetectorType is not supported!");
    }
}
