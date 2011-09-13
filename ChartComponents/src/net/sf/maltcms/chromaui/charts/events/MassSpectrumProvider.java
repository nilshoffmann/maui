/**
 * 
 */
package net.sf.maltcms.chromaui.charts.events;

import ucar.ma2.Array;
import cross.datastructures.tuple.Tuple2D;
import maltcms.datastructures.ms.IScan;

public interface MassSpectrumProvider<T extends IScan> {
	public Tuple2D<Array,Array> getMS(int index);
        
        public T getScan(int index);
	
	public double getRT(int index);
	
	public int getIndex(double rt);
}
