/**
 * 
 */
package maltcms.ui.events;

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
import java.util.List;

public class ChartPanelMouseListener implements XYItemEntityEventSource, ChartMouseListener{

    private ChartPanel cp;
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private final EventSource<XYItemEntity> esource = new EventSource<XYItemEntity>(1);

    public ChartPanelMouseListener(ChartPanel cp1) {
        this.cp = cp1;
        //this.cp.addChartMouseListener(this);
    }

    public void addListener(IListener<IEvent<XYItemEntity>> il) {
        this.esource.addListener(il);
    }

    public void fireEvent(IEvent<XYItemEntity> ievent) {
        this.esource.fireEvent(ievent);
    }

    public void removeListener(IListener<IEvent<XYItemEntity>> il) {
        this.esource.removeListener(il);
    }

    /* (non-Javadoc)
     * @see org.jfree.chart.ChartMouseListener#chartMouseClicked(org.jfree.chart.ChartMouseEvent)
     */
    @Override
    public void chartMouseClicked(final ChartMouseEvent arg0) {
//        XYPlot xyp = getSubplot(arg0.getChart().getXYPlot(), this.cp.getChartRenderingInfo().getPlotInfo(), arg0.getTrigger().getPoint());
//        Point2D mapped = xyp.getDomainAxis().
        // Point2D p = getDataCoordinates(arg0);
        //

        //System.out.println("Mouse click at local coordinates: "+p+" global "+arg0.getTrigger().getX()+", "+arg0.getTrigger().getY());
        final ChartPanelMouseListener cpml = this;
        if (arg0.getEntity() != null && arg0.getEntity() instanceof XYItemEntity) {
            if (arg0.getTrigger().getButton() == MouseEvent.BUTTON1) {
//                Runnable r = new Runnable() {
//                    @Override
//                    public void run() {
                        double cx = arg0.getEntity().getArea().getBounds2D().getCenterX();
                        double cy = arg0.getEntity().getArea().getBounds2D().getCenterY();
                        Point2D.Double center = new Point2D.Double(cx,cy);
//                        updateCrosshairs(arg0.getChart().getXYPlot(),center);
                        if (arg0.getTrigger().isAltDown() && arg0.getTrigger().isShiftDown()) {
                        	System.out.println("Item removed");
                            fireEvent(new XYItemEntityRemovedEvent((XYItemEntity) arg0.getEntity(), cpml));
                        } else if (arg0.getTrigger().isAltDown()) {
                        	System.out.println("Item added");
                        	fireEvent(new XYItemEntityAddedEvent((XYItemEntity) arg0.getEntity(), cpml));
                        } else {
                        	System.out.println("Item clicked");
                            fireEvent(new XYItemEntityClickedEvent((XYItemEntity) arg0.getEntity(), cpml));
                        }
                        //System.out.println("Fired Mouse Clicked in "+(System.currentTimeMillis()-start));
                    //}
//                };
//                es.submit(r);
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
        int seriesIndex =xyie.getSeriesIndex();
        return new Point2D.Double(xyds.getXValue(seriesIndex, itemIndex),xyds.getYValue(seriesIndex, itemIndex));
    }

//    private void updateCrosshairs(XYPlot xy, Point2D p) {
//        if (xy instanceof CombinedDomainXYPlot) {
//            List<?> l = ((CombinedDomainXYPlot) xy).getSubplots();
//            for(Object o:l) {
//                XYPlot xyp = (XYPlot)o;
//                updateCrosshairs(xyp, p);
//            }
//        }else if (xy instanceof CombinedRangeXYPlot) {
//            List<?> l = ((CombinedDomainXYPlot) xyp).getSubplots();
//
//        }else {
//            xy.setDomainCrosshairValue(p.getX());
//            xy.setRangeCrosshairValue(p.getY());
//            xy.setDomainCrosshairVisible(true);
//            xy.setRangeCrosshairVisible(true);
//        }
//
//    }

    /* (non-Javadoc)
     * @see org.jfree.chart.ChartMouseListener#chartMouseMoved(org.jfree.chart.ChartMouseEvent)
     */
    @Override
    public void chartMouseMoved(final ChartMouseEvent arg0) {
        //System.out.println("Mouse moved");
//        	Point2D p = getDataCoordinates(arg0);
        final ChartPanelMouseListener cpml = this;

        if (arg0.getEntity() != null) {
//            Runnable r = new Runnable() {
//
//                public void run() {
                    if (arg0.getEntity() instanceof XYItemEntity) {
                        if (arg0.getTrigger().isAltDown()) {
                            XYPlot xyp = arg0.getChart().getXYPlot();
                            if(xyp!=null) {
                                fireEvent(new XYItemEntityClickedEvent((XYItemEntity) arg0.getEntity(), cpml));
                            }
                            XYItemEntityMovedEvent xyie = new XYItemEntityMovedEvent((XYItemEntity) arg0.getEntity(), cpml);
                            fireEvent(xyie);
                        }
                    }
                    if (arg0.getEntity() instanceof XYAnnotationEntity) {
                        ChartRenderingInfo cri = cp.getChartRenderingInfo();
                        EntityCollection entities = cri.getEntityCollection();
                        ChartEntity ce = entities.getEntity(arg0.getTrigger().getX(), arg0.getTrigger().getY());
                        if (ce instanceof XYItemEntity) {
                            System.out.println("Entity at position: " + ce.getClass().getName() + " " + ((XYItemEntity) ce));
                            XYItemEntityMouseOverEvent xyie = new XYItemEntityMouseOverEvent((XYItemEntity) ce, cpml);
                            fireEvent(xyie);
                        }
                    }
              //  }
//            };
//            es.submit(r);

        } else {
        }

    }

}
