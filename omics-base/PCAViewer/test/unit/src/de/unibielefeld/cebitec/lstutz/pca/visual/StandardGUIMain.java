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
package de.unibielefeld.cebitec.lstutz.pca.visual;

import de.unibielefeld.cebitec.lstutz.pca.data.CSVParser;
import de.unibielefeld.cebitec.lstutz.pca.data.ParserUtilities;
import de.unibielefeld.cebitec.lstutz.pca.data.XMLParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import net.sf.maltcms.chromaui.statistics.pcaViewer.PCAViewerTopComponent;
import org.openide.util.Exceptions;

/**
 *
 * @author Nils Hoffmann
 */
public class StandardGUIMain {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String name = "MeltDB 3D Viewer";
        URL url = StandardGUIMain.class.getResource("/de/unibielefeld/cebitec/lstutz/pca/dataset/meltdb.xml");
////        URL url = getClass().getResource("/de/unibielefeld/cebitec/lstutz/pca/dataset/end_test.xml");
////        if (args[0].equals("xml")) {
//        //ImportUtilities.get_url_input_stream(url)
        XMLParser parser;
        try {
            parser = new XMLParser(url.openStream());
            StandardGUI gui = new StandardGUI(name, ParserUtilities.group_data(parser.parse_data()));
            PCAViewerTopComponent pvtc = new PCAViewerTopComponent();
            pvtc.setData(gui);
            pvtc.open();
            pvtc.requestActive();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        URL url2 = StandardGUIMain.class.getResource("/de/unibielefeld/cebitec/lstutz/pca/dataset/assault.txt");
        InputStream is = null;
        try {
            is = url2.openStream();
            CSVParser csvParser = new CSVParser(is);
            StandardGUI gui = new StandardGUI(name, ParserUtilities.group_data(csvParser.parse_data()));
            PCAViewerTopComponent pvtc = new PCAViewerTopComponent();
            pvtc.setData(gui);
            pvtc.open();
            pvtc.requestActive();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }
}
