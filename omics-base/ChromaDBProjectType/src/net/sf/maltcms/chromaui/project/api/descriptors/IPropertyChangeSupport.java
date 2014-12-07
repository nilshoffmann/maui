/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Nils Hoffmann
 */
public interface IPropertyChangeSupport {

    /**
     *
     * @param string
     * @param pl
     */
    void addPropertyChangeListener(String string, PropertyChangeListener pl);

    /**
     *
     * @param pl
     */
    void addPropertyChangeListener(PropertyChangeListener pl);

    /**
     *
     * @param string
     * @param i
     * @param bln
     * @param bln1
     */
    void fireIndexedPropertyChange(String string, int i, boolean bln, boolean bln1);

    /**
     *
     * @param string
     * @param i
     * @param i1
     * @param i2
     */
    void fireIndexedPropertyChange(String string, int i, int i1, int i2);

    /**
     *
     * @param string
     * @param i
     * @param o
     * @param o1
     */
    void fireIndexedPropertyChange(String string, int i, Object o, Object o1);

    /**
     *
     * @param pce
     */
    void firePropertyChange(PropertyChangeEvent pce);

    /**
     *
     * @param string
     * @param bln
     * @param bln1
     */
    void firePropertyChange(String string, boolean bln, boolean bln1);

    /**
     *
     * @param string
     * @param i
     * @param i1
     */
    void firePropertyChange(String string, int i, int i1);

    /**
     *
     * @param string
     * @param o
     * @param o1
     */
    void firePropertyChange(String string, Object o, Object o1);

    /**
     *
     * @param string
     * @return
     */
    PropertyChangeListener[] getPropertyChangeListeners(String string);

    /**
     *
     * @return
     */
    PropertyChangeListener[] getPropertyChangeListeners();

    /**
     *
     * @param string
     * @return
     */
    boolean hasListeners(String string);

    /**
     *
     * @param string
     * @param pl
     */
    void removePropertyChangeListener(String string, PropertyChangeListener pl);

    /**
     *
     * @param pl
     */
    void removePropertyChangeListener(PropertyChangeListener pl);

}
