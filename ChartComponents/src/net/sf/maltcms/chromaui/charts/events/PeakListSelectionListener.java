/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.charts.events;

import cross.event.AEvent;
import cross.event.IEvent;
import cross.event.IListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import org.jfree.chart.annotations.XYAnnotation;

import cross.event.EventSource;
import javax.swing.DefaultListModel;

public class PeakListSelectionListener implements XYAnnotationEventSource, ListSelectionListener {

	
	private final DefaultListModel xyalm;

        private final EventSource<XYAnnotation> es = new EventSource<XYAnnotation>(1);
	
	public PeakListSelectionListener(DefaultListModel xyalm) {
		this.xyalm = xyalm;
	}
	/* (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int index = e.getFirstIndex();
        XYAnnotation xya = (XYAnnotation)this.xyalm.get(index);
        fireEvent(new AEvent<XYAnnotation>(xya, this, "XYANNOTATION_SELECT"));
    }

    public void addListener(IListener<IEvent<XYAnnotation>> il) {
        this.es.addListener(il);
    }

    public void fireEvent(IEvent<XYAnnotation> ievent) {
        this.es.fireEvent(ievent);
    }

    public void removeListener(IListener<IEvent<XYAnnotation>> il) {
        this.es.removeListener(il);
    }
	
}
