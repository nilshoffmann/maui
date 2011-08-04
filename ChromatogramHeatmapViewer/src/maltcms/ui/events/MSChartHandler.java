/**
 * 
 */
package maltcms.ui.events;

import net.sf.maltcms.chromaui.charts.TopKItemsLabelGenerator;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import cross.datastructures.tuple.Tuple2D;
import cross.event.IEvent;
import java.util.WeakHashMap;
import java.util.HashSet;
import maltcms.datastructures.ms.IScan;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class MSChartHandler<T extends IScan> implements
        XYItemEntityEventListener, Lookup.Provider {
    
    private ExecutorService es = Executors.newSingleThreadExecutor();
//    private int topK = 10;
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    private T scan = null;
    
//    public int getTopK() {
//        return topK;
//    }
//    
//    public void setTopK(int topK) {
//        this.topK = topK;
//    }
//    
//    public void setValueAxisRange(double min, double max) {
//        this.valueAxisMin = min;
//        this.valueAxisMax = max;
//        setValueAxisFixed(true);
//    }
//    
//    public void setValueAxisFixed(boolean b) {
//        this.valueAxisFixed = b;
//    }
//    
//    public boolean isValueAxisFixed() {
//        return this.valueAxisFixed;
//    }
//    private boolean valueAxisFixed = false;
//    private double valueAxisMin = 0;
//    private double valueAxisMax = 100;
    private final MassSpectrumProvider<T> isll;
//    private final XYPlot xypl;
//    private final WeakHashMap<Double, DefaultXYDataset> cache;
//    private final WeakHashMap<Double, SortedMap<Double, Double>> valueMap;
//    private final HashSet<T> scans = new HashSet<T>();
    
    public MSChartHandler(MassSpectrumProvider<T> isl, XYPlot xyp) {
        isll = isl;
//        xypl = xyp;
//        this.cache = new WeakHashMap<Double, DefaultXYDataset>();
//        this.valueMap = new WeakHashMap<Double, SortedMap<Double, Double>>();
    }

    /* (non-Javadoc)
     * @see cross.event.IListener#listen(cross.event.IEvent)
     */
    @Override
    public void listen(final IEvent<XYItemEntity> v) {
        System.out.println("Received event");
        //System.out.println("Received event");
//        	Runnable r = new Runnable() {
//				
//				@Override
//				public void run() {
//					xypl.setNoDataMessage("Loading...");
//					xypl.setDataset(null);
//				}
//        	};
//        	
//        	SwingUtilities.invokeLater(r);

        Runnable s = new Runnable() {
            
            @Override
            public void run() {
//                for (IScan scan : scans) {
//                    ic.remove(scan);
//                }
                System.out.println("MS Chart Handler: Within runnable");
                long m = System.currentTimeMillis();
                
                XYItemEntity e = v.get();
//                Shape shape = e.getArea();
//                Point2D p = new Point2D.Double(shape.getBounds2D().getCenterX(),
//                        shape.getBounds2D().getCenterY());
                double x = e.getDataset().getXValue(e.getSeriesIndex(), e.
                        getItem());
//                DefaultXYDataset xyds = null;
//                xypl.setDomainCrosshairValue(x);
//                if (cache.containsKey(x)) {
//                    xyds = cache.get(x);
//                } else {
//                    xyds = new DefaultXYDataset();
//                    cache.put(x, xyds);
//                    double y = e.getDataset().getYValue(e.getSeriesIndex(), e.
//                            getItem());
//                    System.out.println("Click at " + x + " and " + y);
//                    long l = System.currentTimeMillis();
//                    Tuple2D<Array, Array> t;
//                    T scan = null;
//                    if (isll instanceof Chromatogram2DMSProvider) {
//                        System.out.println("Chromatogram2DMSProvider");
//                        int x2 = (int) p.getX();//e.getDataset().getXValue(e.getSeriesIndex(), e.getItem());
//                        int y2 = (int) p.getY();//e.getDataset().getYValue(e.getSeriesIndex(),e.getItem());
//                        t = ((Chromatogram2DMSProvider) isll).getMS(x2, y2);
//                    } else {
//                        System.out.println("Chromatogram1DMSProvider");
//                        t = isll.getMS(isll.getIndex(x));
//                    }
                    if(scan!=null) {
                        ic.remove(scan);
                    }
                    scan = isll.getScan(isll.getIndex(x));
//                    System.out.println("Retrieved spectrum in " + (m - l));
//                    
//                    Array masses = t.getFirst();
//                    IndexIterator mi = masses.getIndexIterator();
//                    Array intensities = t.getSecond();
//                    IndexIterator ii = intensities.getIndexIterator();
//                    double[][] d = new double[2][t.getFirst().getShape()[0]];
//                    int i = 0;
//                    while (mi.hasNext() && ii.hasNext()) {
//                        d[0][i] = mi.getDoubleNext();
//                        d[1][i] = ii.getDoubleNext();
//                        i++;
//                    }
//                    xyds.addSeries("ms@[" + x + "," + y + "]", d);
                    ic.add(scan);
//                }
                
//                final SortedMap<Double, Double> tm = new TreeMap<Double, Double>();
//                for (int i = 0; i < xyds.getItemCount(0); i++) {
//                    double mass = xyds.getXValue(0, i);
//                    double intens = xyds.getYValue(0, i);
//                    tm.put(intens, mass);
//                }
//                
//                System.out.println("Adding series");
//                
//                m = System.currentTimeMillis();
//				        System.out.println("Created data series in "+(System.currentTimeMillis()-m));
//                Runnable s2 = new Runnable() {
//
//                    @Override
//                    public void run() {
//                System.out.println("Setting data set");
//                //long s = System.currentTimeMillis();
//                //System.out.println("Created dataset in "+(m-l));
//                xypl.setDataset(new XYBarDataset(xyds, 1.0d));
//                xypl.setDomainCrosshairLockedOnData(true);
//                xypl.setDomainCrosshairVisible(true);
//                ((XYBarRenderer) xypl.getRenderer()).setShadowVisible(false);
//                ((XYBarRenderer) xypl.getRenderer()).setDrawBarOutline(false);
//                ((XYBarRenderer) xypl.getRenderer()).setBaseFillPaint(Color.RED);
//                ((XYBarRenderer) xypl.getRenderer()).setBarPainter(
//                        new StandardXYBarPainter());
//                xypl.getRenderer().setBaseItemLabelsVisible(true);
//                xypl.getRenderer().setBaseItemLabelGenerator(new TopKItemsLabelGenerator(
//                        tm, topK));
//                        if (valueAxisFixed) {
//                            xypl.getRangeAxis().setAutoRange(false);
//                            xypl.getRangeAxis().setLowerBound(valueAxisMin);
//                            xypl.getRangeAxis().setUpperBound(valueAxisMax);
//                        } else {
//                            xypl.getRangeAxis().setAutoRange(true);
//                        }
//								System.out.println("Set data series in "+(System.currentTimeMillis()-s));
//                    }
//                };
//                SwingUtilities.invokeLater(s2);
            }
        };
        es.submit(s);
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
