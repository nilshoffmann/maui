/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes.propertySupport;

import de.bielefeld.maltcms.nodes.Command;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author mw
 */
public class DynamicPropertySet extends StaticPropertySet implements PropertyChangeListener {

    public DynamicPropertySet(String name, String category, Command command) {
        this(name, name, name, category, command);
    }

    public DynamicPropertySet(String name, String displayName, String shortDescription, String category, Command command) {
        super(name, displayName, shortDescription, category, command);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getCommand().clearPropertiesForCategory(evt.getOldValue().toString());
        getCommand().addPropertiesForCategory(evt.getNewValue().toString());
        setCategory(evt.getNewValue().toString());
        initPropertyArray();
    }
}
