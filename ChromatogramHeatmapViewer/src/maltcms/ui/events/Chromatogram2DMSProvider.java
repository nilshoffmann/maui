/**
 * 
 */
package maltcms.ui.events;

import cross.datastructures.fragments.IFileFragment;
import ucar.ma2.Array;
import cross.datastructures.tuple.Tuple2D;
import cross.exception.NotImplementedException;
import maltcms.datastructures.caches.IScanLine;
import maltcms.datastructures.caches.ScanLineCacheFactory;

public class Chromatogram2DMSProvider implements MassSpectrumProvider {

    private final int sl;
    private final int spm;
    private final IFileFragment iff;
    private final IScanLine isl;

    public Chromatogram2DMSProvider(IFileFragment f, int sl, int spm) {
            this.sl = sl;
            this.spm = spm;
            this.iff = f;
            System.out.println("Initing scanlinecache");
            this.isl = ScanLineCacheFactory.getScanLineCache(f);
            System.out.println("done initing scanlinecache");
    }
	
	/* (non-Javadoc)
     * @see maltcms.ui.events.MSChartHandler.MassSpectrumProvider#getMS(int)
     */
    @Override
    public Tuple2D<Array, Array> getMS(int index) {
        return this.isl.getSparseMassSpectra(index/sl, index%sl);
        //return MaltcmsTools.getMS(this.iff,index);
    }
    
    public Tuple2D<Array,Array> getMS(int x, int y) {
        int index = (x*spm) + y;
    	return this.isl.getSparseMassSpectra(x, y);
        //return MaltcmsTools.getMS(this.iff,index);
    }

	/* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getIndex(double)
     */
    @Override
    public int getIndex(double rt) {
	    throw new NotImplementedException();
    }

	/* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getRT(int)
     */
    @Override
    public double getRT(int index) {
    	throw new NotImplementedException();
    }
	
}