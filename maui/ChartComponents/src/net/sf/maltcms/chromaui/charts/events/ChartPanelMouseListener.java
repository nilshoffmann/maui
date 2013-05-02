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
package net.sf.maltcms.chromaui.charts.events;

import cross.event.IEvent;
import cross.event.IListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYAnnotationEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

import cross.event.EventSource;
import javax.swing.JPopupMenu;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class ChartPanelMouseListener<SOURCE, TARGET> implements XYItemEntityEventSource,
        ChartMouseListener, Lookup.Provider {

    private ChartPanel cp;
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private final EventSource<XYItemEntity> esource = new EventSource<XYItemEntity>(
            1);
    private InstanceContent ic = new InstanceContent();
    private Lookup lkp = new AbstractLookup(ic);
    private ADataset1D<SOURCE, TARGET> dataset;
    private TARGET lastTarget = null;

    public ChartPanelMouseListener(ADataset1D<SOURCE, TARGET> ds) {
        this.dataset = ds;
    }
    
    public void setChartPanel(ChartPanel cp) {
        this.cp = cp;
    }

    @Override
    public void addListener(IListener<IEvent<XYItemEntity>> il) {
        this.esource.addListener(il);
    }

    @Override
    public void fireEvent(IEvent<XYItemEntity> ievent) {
        this.esource.fireEvent(ievent);
    }

    @Override
    public void removeListener(IListener<IEvent<XYItemEntity>> il) {
        this.esource.removeListener(il);
    }

    protected void setTarget(ChartMouseEvent cme) {
        if (cme.getEntity() != null) {
            if (cme.getEntity() instanceof XYItemEntity) {
                XYItemEntity xyie = (XYItemEntity) cme.getEntity();
                XYDataset ds = xyie.getDataset();
                System.out.println("Series Index of item: "+xyie.getSeriesIndex()+" item number: "+xyie.getItem());
                TARGET target = this.dataset.getTarget(xyie.getSeriesIndex(), xyie.getItem());
                if (lastTarget != null) {
                    ic.remove(lastTarget);
                }
                if (target != null) {
                    lastTarget = target;
                    ic.add(target);
                }
            }
        }

    }

    /* (non-Javadoc)
     * @see org.jfree.chart.ChartMouseListener#chartMouseClicked(org.jfree.chart.ChartMouseEvent)
     */
    @Override
    public void chartMouseClicked(final ChartMouseEvent arg0) {
        final ChartPanelMouseListener cpml = this;
        if (arg0.getEntity() != null) {
            if (arg0.getEntity() instanceof XYItemEntity) {
                XYItemEntity xyie = (XYItemEntity) arg0.getEntity();

                if (arg0.getTrigger().getButton() == MouseEvent.BUTTON1) {
//                
                    if (arg0.getTrigger().isAltDown() && arg0.getTrigger().
                            isShiftDown()) {
//                        System.out.println("Item removed");
                        fireEvent(new XYItemEntityRemovedEvent((XYItemEntity) arg0.getEntity(), cpml));
                    } else if (arg0.getTrigger().isAltDown()) {
//                        System.out.println("Item added");
                        fireEvent(new XYItemEntityAddedEvent((XYItemEntity) arg0.getEntity(), cpml));
                    } else {
                        setTarget(arg0);
//                        System.out.println("Item clicked");
                        fireEvent(new XYItemEntityClickedEvent((XYItemEntity) arg0.getEntity(), cpml));
                    }
                }
            } else if (arg0.getEntity() instanceof LegendItemEntity) {
                JPopupMenu jpm = new JPopupMenu();
                final LegendItemEntity lie = (LegendItemEntity) arg0.getEntity();
                Dataset ds = lie.getDataset();
                Comparable skey = lie.getSeriesKey();
                Plot plot = arg0.getChart().getPlot();
                if (plot instanceof XYPlot) {
                    XYPlot xyplot = arg0.getChart().getXYPlot();
                    if (xyplot.getSeriesCount() > 1) {
                        XYDataset xyds = (XYDataset) ds;
                        XYItemRenderer xyir = xyplot.getRendererForDataset(xyds);

                        xyir.setSeriesVisible(xyds.indexOf(skey), !xyir.isSeriesVisible(xyds.indexOf(skey)));
                        xyir.setSeriesVisibleInLegend(xyds.indexOf(skey),
                                Boolean.TRUE);
                    }
                } else if (plot instanceof CategoryPlot) {
                    CategoryPlot cplot = arg0.getChart().getCategoryPlot();
                    if (cplot.getDatasetCount() > 1) {
                        CategoryDataset cds = (CategoryDataset) ds;
                        CategoryItemRenderer xyir = cplot.getRendererForDataset(
                                cds);
                        int seriesIndex = cds.getColumnIndex(skey);
                        if (seriesIndex == -1) {
                            seriesIndex = cds.getRowIndex(skey);
                        }
                        xyir.setSeriesVisible(seriesIndex,
                                !xyir.isSeriesVisible(
                                seriesIndex));
                        xyir.setSeriesVisibleInLegend(seriesIndex, Boolean.TRUE);
                    }
                }
//                AbstractAction hse = new AbstractAction("Hide") {
//
//                    @Override
//                    public void actionPerformed(ActionEvent ae) {
//                        
//                    }
//                };
//                AbstractAction hse = new AbstractAction("Show") {
//
//                    @Override
//                    public void actionPerformed(ActionEvent ae) {
//                        
//                    }
//                };
//                AbstractAction hse = new AbstractAction("Remove") {
//
//                    @Override
//                    public void actionPerformed(ActionEvent ae) {
//                        
//                    }
//                };
            }
        }
    }

    public XYPlot getSubplot(XYPlot xyp, PlotRenderingInfo pri, Point2D p) {
        if (xyp instanceof CombinedDomainXYPlot) {
            return ((CombinedDomainXYPlot) xyp).findSubplot(pri, p);
        }
        if (xyp instanceof CombinedRangeXYPlot) {
            return ((CombinedRangeXYPlot) xyp).findSubplot(pri, p);
        }
        return xyp;
    }

    private Point2D getDatasetPoint(XYItemEntity xyie) {
        XYDataset xyds = xyie.getDataset();
        int itemIndex = xyie.getItem();
        int seriesIndex = xyie.getSeriesIndex();
        return new Point2D.Double(xyds.getXValue(seriesIndex, itemIndex), xyds.getYValue(seriesIndex, itemIndex));
    }

    /* (non-Javadoc)
     * @see org.jfree.chart.ChartMouseListener#chartMouseMoved(org.jfree.chart.ChartMouseEvent)
     */
    @Override
    public void chartMouseMoved(final ChartMouseEvent arg0) {
        final ChartPanelMouseListener cpml = this;

        if (arg0.getEntity() != null) {
            if (arg0.getEntity() instanceof XYItemEntity) {
                if (arg0.getTrigger().isAltDown()) {
                    XYPlot xyp = arg0.getChart().getXYPlot();
                    if (xyp != null) {
                        fireEvent(new XYItemEntityClickedEvent((XYItemEntity) arg0.getEntity(), cpml));
                    }
                    XYItemEntityMovedEvent xyie = new XYItemEntityMovedEvent((XYItemEntity) arg0.getEntity(), cpml);
                    fireEvent(xyie);
                }
            }
            if (arg0.getEntity() instanceof XYAnnotationEntity) {
                ChartRenderingInfo cri = cp.getChartRenderingInfo();
                EntityCollection entities = cri.getEntityCollection();
                ChartEntity ce = entities.getEntity(arg0.getTrigger().getX(),
                        arg0.getTrigger().getY());
                if (ce instanceof XYItemEntity) {
//                    System.out.println("Entity at position: " + ce.getClass().
//                            getName() + " " + ((XYItemEntity) ce));
                    XYItemEntityMouseOverEvent xyie = new XYItemEntityMouseOverEvent(
                            (XYItemEntity) ce, cpml);
                    fireEvent(xyie);
                }
            }
        }
    }

    @Override
    public Lookup getLookup() {
        return this.lkp;
    }
}
