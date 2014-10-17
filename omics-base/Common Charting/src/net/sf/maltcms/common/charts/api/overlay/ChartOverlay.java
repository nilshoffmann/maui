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
package net.sf.maltcms.common.charts.api.overlay;

import org.jfree.chart.panel.Overlay;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public interface ChartOverlay extends Overlay, Lookup.Provider {

    /**
     *
     * @param b
     */
    public void setVisible(boolean b);

    /**
     *
     * @return
     */
    public boolean isVisible();

    /**
     *
     * @return
     */
    public boolean isVisibilityChangeable();

    /**
     *
     * @return
     */
    public int getLayerPosition();

    /**
     *
     * @param pos
     */
    public void setLayerPosition(int pos);

    /**
     *
     * @return
     */
    public String getName();

    /**
     *
     * @return
     */
    public String getDisplayName();

    /**
     *
     * @return
     */
    public String getShortDescription();

    /**
     *
     * @return
     */
    public Node createNodeDelegate();

    /**
     *
     */
    public final int LAYER_LOWEST = Integer.MIN_VALUE;

    /**
     *
     */
    public final int LAYER_HIGHEST = Integer.MAX_VALUE;

    /**
     *
     */
    public final String PROP_VISIBLE = "visible";

    /**
     *
     */
    public final String PROP_LAYER_POSITION = "layerPosition";

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
}
