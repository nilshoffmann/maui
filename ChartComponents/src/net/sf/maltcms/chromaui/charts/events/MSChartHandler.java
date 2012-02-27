/**
 * 
 */
package net.sf.maltcms.chromaui.charts.events;

import org.jfree.chart.entity.XYItemEntity;

import cross.event.IEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.charts.dataset.Dataset1D;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class MSChartHandler<T extends IScan> implements
        XYItemEntityEventListener, Lookup.Provider {

    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    private IChromatogram lastChromatogram = null;
    private T scan = null;
    private final Dataset1D<IChromatogram, T> ds;
    private ExecutorService es = Executors.newSingleThreadExecutor();

    public MSChartHandler(Dataset1D<IChromatogram, T> ds) {
        this.ds = ds;
    }

    /* (non-Javadoc)
     * @see cross.event.IListener#listen(cross.event.IEvent)
     */
    @Override
    public void listen(final IEvent<XYItemEntity> v) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                System.out.println("MSChartHandler received XYItemEntity!");
                XYItemEntity e = v.get();
                if (scan != null) {
                    System.out.println("Removing old scan");
                    ic.remove(scan);
                }
                if (lastChromatogram != null) {
                    System.out.println("Removing old chromatogram");
                    ic.remove(lastChromatogram);
                }
                System.out.println("Removed old entities!");
                scan = ds.getTarget(e.getSeriesIndex(), e.getItem());
                System.out.println("Retrieved scan");
                lastChromatogram = ds.getSource(e.getSeriesIndex());
                System.out.println("Retrieved chromatogram");
                System.out.println("Adding scan and chromatogram to instance content");
                ic.add(scan);
                ic.add(lastChromatogram);
            }
        };
        es.submit(r);
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
