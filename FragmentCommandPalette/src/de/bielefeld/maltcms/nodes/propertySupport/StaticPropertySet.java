/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes.propertySupport;

import de.bielefeld.maltcms.nodes.Command;
import java.util.Set;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;

/**
 *
 * @author mw
 */
public class StaticPropertySet extends Node.PropertySet {

    private String category;
    private Command command;
    private Property[] propertyArray;

    public StaticPropertySet(String name, String category, Command command) {
        this(name, name, name, category, command);
    }

    public StaticPropertySet(String name, String displayName, String shortDescription, String category, Command command) {
        super(name, displayName, shortDescription);
        this.category = category;
        this.command = command;

        this.command.addPropertiesForCategory(this.category);
    }

    protected void initPropertyArray() {
        Set<String> keys = this.command.getKeySetForCategory(this.category);
        this.propertyArray = new Property[keys.size()];
        int i = 0;
        for (String k : keys) {
            CommandPropertySupport t = CommandPropertySupportFactory.getSupportFor(this.command, this.category, k);
            this.propertyArray[i++] = t;
        }
    }

    @Override
    public Property<?>[] getProperties() {
        if (this.propertyArray == null) {
            initPropertyArray();
        }
        return this.propertyArray;
    }

    protected Command getCommand() {
        return this.command;
    }

    protected void setCategory(String category) {
        this.category = category;
    }
}
