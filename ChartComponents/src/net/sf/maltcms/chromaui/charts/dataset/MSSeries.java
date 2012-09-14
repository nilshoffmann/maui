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
package net.sf.maltcms.chromaui.charts.dataset;

import org.jfree.data.xy.XYSeries;

/**
 *
 * @author nilshoffmann
 */
public class MSSeries extends XYSeries{

    private boolean normalize = false;
    private double norm = 1.0d;

    public MSSeries(Comparable key, boolean autoSort, boolean allowDuplicateXValues) {
        super(key,autoSort,allowDuplicateXValues);
    }

    public MSSeries(Comparable key, boolean autoSort) {
        super(key,autoSort);
    }

    public MSSeries(Comparable key) {
        super(key);
    }
    
    public boolean isNormalize() {
        return normalize;
    }
    
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
        if(this.normalize) {
            this.norm = getNormalizationValue();
            System.out.println("Normalizing series "+getKey()+": "+normalize);
            //normalize
            for(int i = 0;i<getItemCount();i++) {
                updateByIndex(i, Double.valueOf(getY(i).doubleValue()/this.norm));
            }
        }else{
            System.out.println("Normalizing series "+getKey()+": "+normalize);
            //denormalize
            for(int i = 0;i<getItemCount();i++) {
                updateByIndex(i, Double.valueOf(getY(i).doubleValue()*this.norm));
            }
            this.norm = 1.0d;
        }
        fireSeriesChanged();
    }
    
    public double getNormalizationValue() {
        return getMaxY()-getMinY();
    }
    
}
