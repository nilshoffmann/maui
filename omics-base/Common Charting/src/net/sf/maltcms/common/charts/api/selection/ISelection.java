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
package net.sf.maltcms.common.charts.api.selection;

import java.awt.Shape;
import java.beans.PropertyChangeListener;
import org.jfree.data.general.Dataset;

/**
 *
 * @author Nils Hoffmann
 */
public interface ISelection {

    /**
     *
     */
    public enum Type {

        /**
         *
         */
        CLE,
        CLEAR,

        /**
         *
         */
        KEYBOARD,

        /**
         *
         */
        HOVER,

        /**
         *
         */
        CLICK
    };

    /**
     *
     */
    public final String PROP_NAME = "name";

    /**
     *
     */
    public final String PROP_DISPLAY_NAME = "displayName";

    /**
     *
     */
    public final String PROP_SHORT_DESCRIPTION = "shortDescription";

    /**
     *
     */
    public final String PROP_VISIBLE = "visible";

    /**
     *
     * @return
     */
    Object getSource();

    /**
     *
     * @return
     */
    Object getTarget();

    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @param name
     */
    void setName(String name);

    /**
     *
     * @return
     */
    Type getType();

    /**
     *
     * @return
     */
    String getDisplayName();

    /**
     *
     * @param name
     */
    void setDisplayName(String name);

    /**
     *
     * @return
     */
    String getShortDescription();

    /**
     *
     * @param name
     */
    void setShortDescription(String name);

    /**
     *
     * @return
     */
    boolean isVisible();

    /**
     *
     * @param b
     */
    void setVisible(boolean b);

    /**
     *
     * @return
     */
    Shape getSelectionShape();

    /**
     *
     * @return
     */
    Dataset getDataset();

    /**
     *
     * @param dataset
     */
    void setDataset(Dataset dataset);

    /**
     *
     * @return
     */
    int getSeriesIndex();

    /**
     *
     * @return
     */
    int getItemIndex();

    /**
     *
     * @param listener
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     *
     * @param property
     * @param listener
     */
    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    /**
     *
     * @param listener
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     *
     * @param property
     * @param listener
     */
    void removePropertyChangeListener(String property, PropertyChangeListener listener);
}
