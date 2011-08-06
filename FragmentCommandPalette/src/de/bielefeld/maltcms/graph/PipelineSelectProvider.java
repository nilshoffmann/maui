/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.graph;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author mw
 */
public class PipelineSelectProvider implements SelectProvider {

    private Widget lastSelected;
    private PipelineGraphScene scene;

    public PipelineSelectProvider(PipelineGraphScene s) {
        this.scene = s;
    }

    @Override
    public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    @Override
    public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    @Override
    public void select(Widget widget, Point point, boolean bln) {
        if (lastSelected != null) {
            lastSelected.setState(lastSelected.getState().deriveSelected(false));
        }
        widget.setState(widget.getState().deriveSelected(true));
        lastSelected = widget;

        this.scene.setSelectedWidget(lastSelected);
    }
}
