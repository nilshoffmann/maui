/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.charts.events;

import cross.event.AEvent;
import cross.event.IEventSource;
import org.jfree.chart.entity.XYItemEntity;

/**
 *
 * @author nilshoffmann
 */
public class XYItemEntityAddedEvent  extends AEvent<XYItemEntity> {

        /**
         * @param v
         * @param ies
         */
        public XYItemEntityAddedEvent(XYItemEntity v,
                IEventSource<XYItemEntity> ies) {
            super(v, ies, "XYITEMENTITY_ADDED");
        }
    }
