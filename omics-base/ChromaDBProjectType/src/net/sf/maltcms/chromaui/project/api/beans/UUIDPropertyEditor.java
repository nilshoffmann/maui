/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.beans;

import java.beans.*;
import java.util.UUID;

/**
 *
 * @author nilshoffmann
 */
public class UUIDPropertyEditor extends PropertyEditorSupport {
    
    public UUIDPropertyEditor() {
    }

    @Override
    public String getAsText() {
        UUID id = (UUID)getValue();
        return id.toString();
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        throw new IllegalArgumentException("Editing of UUIDs is not supported!");
//        setValue(UUID.fromString(string));
    }
}
