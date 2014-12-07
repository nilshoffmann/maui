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
package org.jzy3d.demos.histogram.barchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.jzy3d.plot3d.primitives.HistogramBar;
import org.jzy3d.plot3d.primitives.pickable.Pickable;

/**
 *
 * @author ao
 */
public class SimpleBarChartBar<ITEM> extends HistogramBar implements Pickable {

    // HACK! -> the view class in API does not expose GLU object!
    private final String info;
    private ITEM item;
    private int pickingId = -1;

    public SimpleBarChartBar(String info, ITEM item, int pickingId) {
        super();
        this.info = info;
        this.item = item;
        this.pickingId = pickingId;
    }

    public SimpleBarChartBar(ITEM item, int pickingId) {
        this(item.toString(), item, pickingId);
    }

    public ITEM getItem() {
        return item;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public void setPickingId(int i) {
        this.pickingId = i;
    }

    @Override
    public int getPickingId() {
        return this.pickingId;
    }
}
