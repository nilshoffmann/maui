/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import cross.Factory;
import cross.exception.NotImplementedException;
import java.util.Arrays;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import maltcms.ui.charts.JFreeChartViewer;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import net.sf.maltcms.chromaui.charts.dataset.NamedElementProvider;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DDataset;
import net.sf.maltcms.chromaui.charts.dataset.chromatograms.Chromatogram1DElementProvider;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author Nils Hoffmann
 */
public class ChromatogramViewOpenSupport extends OpenSupport implements
        OpenCookie, CloseCookie {

    public ChromatogramViewOpenSupport(CDFDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {

        CompositeConfiguration cfg = new CompositeConfiguration();
        cfg.addConfiguration(new SystemConfiguration());
        try {
            PropertiesConfiguration pcfg = new PropertiesConfiguration(JFreeChartViewer.class.
                    getClassLoader().getResource("cfg/default.properties"));
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

        Lookup lkp = Utilities.actionsGlobalContext();
        IChromatogramDescriptor descriptor = lkp.lookup(
                IChromatogramDescriptor.class);
        if (descriptor == null) {
            descriptor = DescriptorFactory.newChromatogramDescriptor();
            descriptor.setResourceLocation(entry.getFile().getPath());
        }
        IChromAUIProject project = lkp.lookup(IChromAUIProject.class); 
//        CDFDataObject dobj = (CDFDataObject) entry.getDataObject();
//        MassSpectrumViewTopComponent secondaryView = MassSpectrumViewTopComponent.findInstance();
//        secondaryView.setProject(project);
//        secondaryView.open();
        if(descriptor.getChromatogram() instanceof IChromatogram1D) {
            NamedElementProvider<IChromatogram, IScan> nep = new Chromatogram1DElementProvider(descriptor.getDisplayName(),(IChromatogram1D)descriptor.getChromatogram());
            Chromatogram1DDataset ds = new Chromatogram1DDataset(Arrays.asList(nep));
            ChromatogramViewTopComponent cvtc = new ChromatogramViewTopComponent(project, Arrays.asList(descriptor),ds);
//            secondaryView.setLookup(cvtc.getLookup());
            return cvtc;
        }else{
            throw new NotImplementedException("Currently no support for 2D chromatograms!");
        }
        
        
//        ChromatogramViewTopComponent cvtc = new ChromatogramViewTopComponent(dobj.getPrimaryFile().getPath(),secondaryView);
//        System.out.println("filename: " + dobj.getPrimaryFile().getPath());

        
//        cvtc.requestActive();
//        return cvtc;
    }
}
