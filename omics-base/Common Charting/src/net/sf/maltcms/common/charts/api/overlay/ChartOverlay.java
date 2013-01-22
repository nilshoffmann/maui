/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.api.overlay;

import org.jfree.chart.panel.Overlay;

/**
 *
 * @author Nils Hoffmann
 */
public interface ChartOverlay extends Overlay {
    public void setVisible(boolean b);
    public boolean isVisible();
    public boolean isVisibilityChangeable();
    public int getLayerPosition();
    public void setLayerPosition(int pos);
    
    public String getName();
    public String getDisplayName();
    public String getShortDescription();
    
    public final int LAYER_LOWEST = Integer.MIN_VALUE;
    public final int LAYER_HIGHEST = Integer.MAX_VALUE;
    public final String PROP_VISIBLE = "visible";
    public final String PROP_LAYER_POSITION = "layerPosition";
    public final String PROP_NAME = "name";
    public final String PROP_DISPLAY_NAME = "displayName";
    public final String PROP_SHORT_DESCRIPTION = "shortDescription";
}
