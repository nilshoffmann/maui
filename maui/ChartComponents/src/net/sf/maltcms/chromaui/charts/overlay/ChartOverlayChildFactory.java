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
package net.sf.maltcms.chromaui.charts.overlay;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.maltcms.common.charts.api.Charts;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import org.jfree.chart.event.OverlayChangeEvent;
import org.jfree.chart.event.OverlayChangeListener;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;

/**
 *
 * @author Nils Hoffmann
 */
public class ChartOverlayChildFactory extends ChildFactory<ChartOverlay> implements OverlayChangeListener, PropertyChangeListener {

    private List<ChartOverlay> overlays;

    public ChartOverlayChildFactory(List<ChartOverlay> overlays) {
        this.overlays = overlays;
        for (ChartOverlay overlay : overlays) {
            overlay.addChangeListener(this);
        }
    }

    @Override
    protected boolean createKeys(List<ChartOverlay> list) {
        for (ChartOverlay overlay : overlays) {
            list.add(overlay);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ChartOverlay key) {
        Node n = key.createNodeDelegate();
        n.addPropertyChangeListener(WeakListeners.propertyChange(this, n));
        return n;
    }

    @Override
    public void overlayChanged(OverlayChangeEvent oce) {
        refresh(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh(true);
    }
}
