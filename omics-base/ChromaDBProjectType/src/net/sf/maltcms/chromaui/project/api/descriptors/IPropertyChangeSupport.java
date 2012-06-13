/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author nilshoffmann
 */
public interface IPropertyChangeSupport {

    void addPropertyChangeListener(String string, PropertyChangeListener pl);

    void addPropertyChangeListener(PropertyChangeListener pl);

    void fireIndexedPropertyChange(String string, int i, boolean bln, boolean bln1);

    void fireIndexedPropertyChange(String string, int i, int i1, int i2);

    void fireIndexedPropertyChange(String string, int i, Object o, Object o1);

    void firePropertyChange(PropertyChangeEvent pce);

    void firePropertyChange(String string, boolean bln, boolean bln1);

    void firePropertyChange(String string, int i, int i1);

    void firePropertyChange(String string, Object o, Object o1);

    PropertyChangeListener[] getPropertyChangeListeners(String string);

    PropertyChangeListener[] getPropertyChangeListeners();

    boolean hasListeners(String string);

    void removePropertyChangeListener(String string, PropertyChangeListener pl);

    void removePropertyChangeListener(PropertyChangeListener pl);
    
}
