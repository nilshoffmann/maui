/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer;

import cross.Factory;
import maltcms.ui.charts.JFreeChartViewer;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mw
 */
public class GCViewerOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public GCViewerOpenSupport(CDFDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {

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
        System.out.println("filename: " + dobj.getPrimaryFile().getPath());
        GCViewerTopComponent jtc = new GCViewerTopComponent(dobj.getPrimaryFile().getPath());
        return jtc;
    }
}
