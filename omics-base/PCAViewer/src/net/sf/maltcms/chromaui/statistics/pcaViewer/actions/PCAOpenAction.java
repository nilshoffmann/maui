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
package net.sf.maltcms.chromaui.statistics.pcaViewer.actions;

import de.unibielefeld.cebitec.lstutz.pca.data.ParserUtilities;
import de.unibielefeld.cebitec.lstutz.pca.data.PcaDescriptorAdapter;
import de.unibielefeld.cebitec.lstutz.pca.visual.StandardGUI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;
import net.sf.maltcms.chromaui.statistics.pcaViewer.PCAViewerTopComponent;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
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
            pvtc.requestActive();
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }

//        } else if (args[0].equals("csv")) {
//            CSVParser parser = new CSVParser(ImportUtilities.get_url_input_stream(url));
//            StandardGUI gui = new StandardGUI(name, ParserUtilities.group_data(parser.parse_data()));
//        }
    }
}
