/**
 * 
 */
package maltcms.ui.events;

import ucar.ma2.Array;
import cross.datastructures.tuple.Tuple2D;

public interface MassSpectrumProvider {
	public Tuple2D<Array,Array> getMS(int index);
	
	public double getRT(int index);
	
	public int getIndex(double rt);
}
