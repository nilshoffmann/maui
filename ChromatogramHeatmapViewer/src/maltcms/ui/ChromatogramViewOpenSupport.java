/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import cross.Factory;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.io.csv.ColorRampReader;
import cross.tools.ImageTools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import maltcms.tools.MaltcmsTools;
import maltcms.ui.charts.GradientPaintScale;
import maltcms.ui.charts.JFreeChartViewer;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import maltcms.ui.views.ChromMSHeatmapPanel;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.ui.RectangleEdge;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.Index;
import ucar.ma2.IndexIterator;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author Nils Hoffmann
 */
public class ChromatogramViewOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public ChromatogramViewOpenSupport(CDFDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent(){

        CompositeConfiguration cfg = new CompositeConfiguration();
        cfg.addConfiguration(new SystemConfiguration());
        try {
            PropertiesConfiguration pcfg = new PropertiesConfiguration(JFreeChartViewer.class.getClassLoader().getResource("cfg/default.properties"));
            cfg.addConfiguration(pcfg);
            Factory.getInstance().configure(cfg);
        } catch (ConfigurationException e) {
            PropertiesConfiguration pcfg;
            try {
                pcfg = new PropertiesConfiguration("cfg/default.properties");
                cfg.addConfiguration(pcfg);
                Factory.getInstance().configure(cfg);
            } catch (ConfigurationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }


        CDFDataObject dobj = (CDFDataObject) entry.getDataObject();
        ChromatogramViewTopComponent cvtc = new ChromatogramViewTopComponent(dobj.getPrimaryFile().getPath());
        System.out.println("filename: " + dobj.getPrimaryFile().getPath());
        
        return cvtc;
    }

}
