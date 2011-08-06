/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes.propertySupport.editor;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import java.awt.Component;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author mw
 */
public class APropertyEditorService extends PropertyEditorSupport {

    private String className;

    public APropertyEditorService(String classname) {
        this.className = classname;

//        System.out.println("Classes: ");
//        Collection<? extends Object> r = Lookup.getDefault().lookupAll(Object.class);
//        for (Object o : r) {
//            System.out.println(o.getClass().getName());
//        }
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }

    @Override
    public void setAsText(String s) {
        setValue(s);
    }

    @Override
    public Component getCustomEditor() {
        return new ServiceLoaderPropertyPanel(this.className, getAsText(), this);
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }
}
