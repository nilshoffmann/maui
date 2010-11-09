/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

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
import org.openide.windows.TopComponent;

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
        MassSpectrumViewTopComponent secondaryView = new MassSpectrumViewTopComponent();
        ChromatogramViewTopComponent cvtc = new ChromatogramViewTopComponent(dobj.getPrimaryFile().getPath(),secondaryView);
        System.out.println("filename: " + dobj.getPrimaryFile().getPath());
        
        secondaryView.open();
        cvtc.requestActive();
        return cvtc;
    }

}
