/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes.propertySupport;

import de.bielefeld.maltcms.nodes.Command;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author mw
 */
public class CommandPropertySupport<T> extends PropertySupport.ReadWrite<T> {

    private String category;
    private String key;
    private Command c;
    private Class<T> ct;
    private PropertyChangeListener l;

    public CommandPropertySupport(Command c, String category, String key, Class<T> t) {
        super(key, t, parse(key), key);

        this.category = category;
        this.key = key;
        this.c = c;
        this.ct = t;
    }

    private static String parse(String k) {
        String[] s = k.split("\\.");
        return s[s.length - 1];
    }

    @Override
    public T getValue() throws IllegalAccessException, InvocationTargetException {
        if (this.ct == Boolean.class) {
            return (T) (Boolean) Boolean.parseBoolean(this.c.getValueForCategory(this.category, this.key).toLowerCase());
        }
        if (this.ct == Integer.class) {
            return (T) (Integer) Integer.parseInt(this.c.getValueForCategory(this.category, this.key));
        }
        return (T) this.c.getValueForCategory(this.category, this.key);
    }

    @Override
    public void setValue(T t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (l != null) {
            l.propertyChange(new PropertyChangeEvent(this, this.key, getValue().toString(), t.toString()));
        }
        this.c.setValueForCategory(this.category, this.key, t.toString());
    }

    public void setPropertyChangeListener(PropertyChangeListener l) {
        this.l = l;
    }
}
