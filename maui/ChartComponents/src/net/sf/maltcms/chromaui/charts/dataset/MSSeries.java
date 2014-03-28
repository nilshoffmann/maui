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
package net.sf.maltcms.chromaui.charts.dataset;

import java.util.TreeMap;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.Scan1D;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;

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
	
	public MSSeries differenceTo(MSSeries other) {
		MSSeries newSeries = new MSSeries("DIFFERENCE",getAutoSort(),getAllowDuplicateXValues());
//		XYSeriesCollection xyds = new XYSeriesCollection();
//		xyds.addSeries(this);
//		xyds.addSeries(other);
		TreeMap<Number,Number> domainToRange = new TreeMap<Number,Number>();
		for(int i = 0;i < getItemCount(); i++) {
			Number x = getX(i);
			Number y = getY(i);
			domainToRange.put(x, Math.abs(y.doubleValue()));
		}
		for(int i = 0;i < other.getItemCount(); i++) {
			Number x = other.getX(i);
			Number y = other.getY(i);
			if(domainToRange.containsKey(x)) {
				Number myY = domainToRange.get(x);
				domainToRange.put(x,myY.doubleValue()-Math.abs(y.doubleValue()));
			}else{
				domainToRange.put(x,-Math.abs(y.doubleValue()));
			}
		}
		for(Number x:domainToRange.keySet()) {
			newSeries.add(x, domainToRange.get(x));
		}
		return newSeries;
	}
	
	public IScan asScan() {
		Array masses = new ArrayDouble.D1(getItemCount());
		Array intensities = new ArrayInt.D1(getItemCount());
		for(int i = 0;i < getItemCount(); i++) {
			Number x = getX(i);
			Number y = getY(i);
			masses.setDouble(i, x.doubleValue());
			intensities.setInt(i, y.intValue());
		}
		return new Scan1D(masses, intensities, -1, Double.NaN);
	}
    
    public boolean isNormalize() {
        return normalize;
    }
    
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
        if(this.normalize) {
            this.norm = getNormalizationValue();
//            System.out.println("Normalizing series "+getKey()+": "+normalize);
            //normalize
            for(int i = 0;i<getItemCount();i++) {
                updateByIndex(i, Double.valueOf(getY(i).doubleValue()/this.norm));
            }
        }else{
//            System.out.println("Normalizing series "+getKey()+": "+normalize);
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
