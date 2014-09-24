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
package net.sf.maltcms.chromaui.charts.events;

import java.awt.Color;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

import ucar.ma2.Array;
import ucar.ma2.Index;
import cross.event.IEvent;
import java.util.logging.Logger;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 *
 * @author Nils Hoffmann
 */
public class TICChartHandler implements XYItemEntityEventListener {

    private WeakHashMap<Integer, XYDataset> ticCache = new WeakHashMap<>();
    private int topK = 10;

    /**
     *
     * @return
     */
    public int getTopK() {
        return topK;
    }

    /**
     *
     * @param topK
     */
    public void setTopK(int topK) {
        this.topK = topK;
    }

    /**
     *
     * @param min
     * @param max
     */
    public void setValueAxisRange(double min, double max) {
        this.valueAxisMin = min;
        this.valueAxisMax = max;
        setValueAxisFixed(true);
    }

    /**
     *
     * @param b
     */
    public void setValueAxisFixed(boolean b) {
        this.valueAxisFixed = b;
    }

    /**
     *
     * @return
     */
    public boolean isValueAxisFixed() {
        return this.valueAxisFixed;
    }
    private boolean valueAxisFixed = false;
    private double valueAxisMin = 0;
    private double valueAxisMax = 100;
    private final XYPlot xypl;
    private final Array data;
    private final int scanlines;
    private final int scansPerMod;
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private XYItemRenderer xyr = new XYLineAndShapeRenderer(true, false);

    /**
     *
     * @param xyp
     * @param data
     * @param scanlines
     * @param scansPerMod
     */
    public TICChartHandler(XYPlot xyp, Array data, int scanlines, int scansPerMod) {
        this.xypl = xyp;
        this.xypl.setRenderer(this.xyr);
        this.data = data;
        this.scanlines = scanlines;
        this.scansPerMod = scansPerMod;
    }

    /* (non-Javadoc)
     * @see cross.event.IListener#listen(cross.event.IEvent)
     */

    /**
     *
     * @param v
     */
    
    @Override
    public void listen(final IEvent<XYItemEntity> v) {
        if (!(v.get().getDataset() instanceof XYZDataset)) {
            Logger.getLogger(getClass().getName()).warning("Can not handle instances other than XYZDataset!");
            return;
        }
        final ValueAxis domainAxis = xypl.getDomainAxis();
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
//        	
        Runnable s = new Runnable() {

            @Override
            public void run() {
                long start = System.currentTimeMillis();
                //System.out.println("Running update of TICChartHandler");
                XYItemEntity e = v.get();
                if (e.getDataset() instanceof XYZDataset) {
                    final XYZDataset xyz = (XYZDataset) e.getDataset();
                    final int seriesIndex = e.getSeriesIndex();
                    final int y = (int) xyz.getYValue(seriesIndex, e.getItem());
                    final int x = (int) xyz.getXValue(seriesIndex, e.getItem());
                    XYDataset xydss;
                    if (ticCache.containsKey(y)) {
                        xydss = ticCache.get(y);
                    } else {
                        final DefaultXYDataset xyds = new DefaultXYDataset();
                        double[][] d = new double[2][scanlines];
                        Index idx = data.getIndex();
                        //columns, y
                        for (int j = 0; j < scanlines; j++) {
                            idx.set(scansPerMod * j + y);
                            d[0][j] = j;
                            d[1][j] = data.getDouble(idx);
                        }
                        xyds.addSeries("TIC@[" + y + "]", d);
                        xydss = xyds;
                        ticCache.put(y, xyds);
                    }
                    final XYDataset fds = xydss;
                    Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            xypl.setDataset(fds);
                            xypl.getRenderer().setBaseItemLabelsVisible(true);
                            xypl.getRenderer().setBasePaint(Color.GRAY);
                            xypl.getRenderer().setBaseOutlinePaint(Color.DARK_GRAY);
                            xypl.setDomainCrosshairLockedOnData(true);
                            xypl.setDomainCrosshairValue(x, true);
                            if (valueAxisFixed) {
                                xypl.getRangeAxis().setAutoRange(false);
                                xypl.getRangeAxis().setLowerBound(valueAxisMin);
                                xypl.getRangeAxis().setUpperBound(valueAxisMax);
                            } else {
                                xypl.getRangeAxis().setAutoRange(true);
                            }

                        }
                    };
                    SwingUtilities.invokeLater(runnable);
//					}else{
//						System.err.println("Can only handle XYZDatasets!");
//					}
                    //System.out.println("Set TIC data in "+(System.currentTimeMillis()-start));
                }
            }
        };
        es.submit(s);
    }
}
