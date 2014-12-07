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
package net.sf.maltcms.chromaui.charts.events;

import cross.event.EventSource;
import cross.event.IEvent;
import cross.event.IListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author nilshoffmann
 */
public class DomainMarkerKeyListener implements KeyListener, XYItemEntityEventSource {

    private XYPlot xyp;

    private final EventSource<XYItemEntity> esource = new EventSource<>(1);

    /**
     *
     * @param xyp
     */
    public DomainMarkerKeyListener(XYPlot xyp) {
        this.xyp = xyp;
    }

    /**
     *
     * @param plot
     */
    public void setPlot(XYPlot plot) {
        this.xyp = plot;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        double oldPos = xyp.getDomainCrosshairValue();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            xyp.setDomainCrosshairValue(oldPos++);
//            xyp.getDataset().get
//            final IEvent<XYItemEntity> v = new XYItemEntityClickedEvent();
//            fireEvent(v);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            xyp.setDomainCrosshairValue(oldPos--);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     *
     * @param il
     */
    @Override
    public void addListener(IListener<IEvent<XYItemEntity>> il) {
        esource.addListener(il);
    }

    /**
     *
     * @param ievent
     */
    @Override
    public void fireEvent(IEvent<XYItemEntity> ievent) {
        esource.fireEvent(ievent);
    }

    /**
     *
     * @param il
     */
    @Override
    public void removeListener(IListener<IEvent<XYItemEntity>> il) {
        esource.removeListener(il);
    }

}
