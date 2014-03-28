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

import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.common.charts.api.overlay.AbstractChartOverlay;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import static net.sf.maltcms.common.charts.api.overlay.ChartOverlay.PROP_VISIBLE;
import net.sf.maltcms.common.charts.api.selection.SelectionChangeEvent;
import org.jfree.chart.ChartPanel;
import org.openide.nodes.Node;

/**
 * Virtual overlay to controll the visibility of all overlays associated to one 
 * chromatotogram.
 * 
 * @author Nils Hoffmann
 */
public class ChromatogramDescriptorOverlay extends AbstractChartOverlay implements ChartOverlay, PropertyChangeListener {

	private final List<ChartOverlay> overlays;
	private final IChromatogramDescriptor descriptor;

	public ChromatogramDescriptorOverlay(IChromatogramDescriptor descriptor, String name, String displayName, String shortDescription, boolean visibilityChangeable, List<ChartOverlay> children) {
		super(name, displayName, shortDescription, visibilityChangeable);
		this.descriptor = descriptor;
		this.overlays = children;
		super.content.add(descriptor);
	}

	@Override
	public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
		
	}

	@Override
	public void setVisible(boolean b) {
		boolean old = super.isVisible();
		boolean newValue = b;
		for(ChartOverlay overlay:overlays) {
			overlay.setVisible(newValue);
		}
		super.setVisible(newValue);
		firePropertyChange(PROP_VISIBLE, old, newValue);
		fireOverlayChanged();
	}
	
	@Override
	public void selectionStateChanged(SelectionChangeEvent ce) {
		//TODO implement peak descriptor selection
//        XYSelection selection = ce.getSelection();
//
//        if (selection == null) {
//            if (mouseHoverSelection != null) {
//                mouseHoverSelection.removePropertyChangeListener(XYSelection.PROP_VISIBLE, this);
//                firePropertyChange(PROP_HOVER_SELECTION, mouseHoverSelection, null);
//            }
//            mouseHoverSelection = null;
//        } else {
//            if (ce.getSelection().getType() == XYSelection.Type.CLICK) {
//                if (mouseClickSelection.contains(selection)) {
//                    mouseClickSelection.remove(selection);
//                    selection.removePropertyChangeListener(XYSelection.PROP_VISIBLE, this);
//                } else {
//                    mouseClickSelection.add(selection);
//                    selection.addPropertyChangeListener(XYSelection.PROP_VISIBLE, WeakListeners.propertyChange(this, selection));
//                }
//                firePropertyChange(PROP_SELECTION, null, mouseClickSelection);
//            } else if (ce.getSelection().getType() == XYSelection.Type.HOVER) {
//                if (mouseHoverSelection != null) {
//                    mouseHoverSelection.removePropertyChangeListener(XYSelection.PROP_VISIBLE, this);
//                }
//                mouseHoverSelection = selection;
//                mouseHoverSelection.addPropertyChangeListener(XYSelection.PROP_VISIBLE, WeakListeners.propertyChange(this, mouseHoverSelection));
//                firePropertyChange(PROP_HOVER_SELECTION, null, mouseHoverSelection);
//            }
//        }
		fireOverlayChanged();
	}

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		fireOverlayChanged();
	}

	public List<ChartOverlay> getOverlays() {
		return overlays;
	}

	public IChromatogramDescriptor getDescriptor() {
		return descriptor;
	}

}
