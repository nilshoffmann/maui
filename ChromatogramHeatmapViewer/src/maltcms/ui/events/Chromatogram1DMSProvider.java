/**
 * 
 */
package maltcms.ui.events;

import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.Scan1D;
import ucar.ma2.Array;
import ucar.ma2.Index;
import cross.datastructures.tuple.Tuple2D;

public class Chromatogram1DMSProvider implements MassSpectrumProvider {

    private final IChromatogram1D c;

    public Chromatogram1DMSProvider(IChromatogram1D c) {
        this.c = c;
    }

    /* (non-Javadoc)
     * @see maltcms.ui.events.MSChartHandler.MassSpectrumProvider#getMS(int)
     */
    @Override
    public Tuple2D<Array, Array> getMS(int index) {
        Scan1D s = this.c.getScan(index);
        return new Tuple2D<Array, Array>(s.getMasses(), s.getIntensities());
    }

    /* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getIndex(double)
     */
    @Override
    public int getIndex(double rt) {
        int idx = this.c.getIndexFor(rt);
        System.out.println("Index " + idx + " for rt " + rt);
        return idx;
    }

    /* (non-Javadoc)
     * @see maltcms.ui.events.MassSpectrumProvider#getRT(int)
     */
    @Override
    public double getRT(int index) {
        Array a = this.c.getScanAcquisitionTime();
        Index idx = a.getIndex();
        return a.getDouble(idx.set(index));
    }
}
