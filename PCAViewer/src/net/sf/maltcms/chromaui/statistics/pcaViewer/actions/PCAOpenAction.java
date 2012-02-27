/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.statistics.pcaViewer.actions;

import de.unibielefeld.cebitec.lstutz.pca.data.ImportUtilities;
import de.unibielefeld.cebitec.lstutz.pca.data.ParserUtilities;
import de.unibielefeld.cebitec.lstutz.pca.data.PcaDescriptorAdapter;
import de.unibielefeld.cebitec.lstutz.pca.data.XMLParser;
import de.unibielefeld.cebitec.lstutz.pca.visual.StandardGUI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;

import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;
import net.sf.maltcms.chromaui.statistics.pcaViewer.PCAViewerTopComponent;
import net.sf.maltcms.chromaui.statistics.pcaViewer.spi.runnables.PeakGroupPcaRunnable;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "DescriptorNodeActions/IPcaDescriptor",
id = "net.sf.maltcms.chromaui.statistics.pcaViewer.actions.PCAOpenAction")
@ActionRegistration(displayName = "#CTL_PCAOpenAction")
@ActionReferences({})
@Messages("CTL_PCAOpenAction=PCA")
public final class PCAOpenAction implements ActionListener {

    private final IPcaDescriptor context;

    public PCAOpenAction(IPcaDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
//        PeakGroupPcaRunnable pgpr = new PeakGroupPcaRunnable(context);
//        PeakGroupPcaRunnable.createAndRun("Peak group pca", pgpr);
        
        String name = "MeltDB 3D Viewer";
//        URL url = getClass().getResource("/de/unibielefeld/cebitec/lstutz/pca/dataset/meltdb.xml");
////        URL url = getClass().getResource("/de/unibielefeld/cebitec/lstutz/pca/dataset/end_test.xml");
////        if (args[0].equals("xml")) {
//        //ImportUtilities.get_url_input_stream(url)
//        XMLParser parser;
//        try {
//            parser = new XMLParser(url.openStream());
            StandardGUI gui = new StandardGUI(name, ParserUtilities.group_data(new PcaDescriptorAdapter().parse_data(context)));
            PCAViewerTopComponent pvtc = new PCAViewerTopComponent();
            pvtc.setData(gui);
            pvtc.open();
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }

//        } else if (args[0].equals("csv")) {
//            CSVParser parser = new CSVParser(ImportUtilities.get_url_input_stream(url));
//            StandardGUI gui = new StandardGUI(name, ParserUtilities.group_data(parser.parse_data()));
//        }
    }
}
