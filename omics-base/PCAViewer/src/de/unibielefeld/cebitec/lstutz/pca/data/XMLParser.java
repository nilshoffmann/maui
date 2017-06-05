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
package de.unibielefeld.cebitec.lstutz.pca.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.jogamp.vecmath.Color3f;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * Class is currently not implemented!
 *
 * @author Nils Hoffmann
 *
 */
@Deprecated

public class XMLParser {

    private InputStream is;
    private BufferedReader bf;

    public XMLParser(InputStream input) {
        this.is = input;
        InputStreamReader isr = new InputStreamReader(is);
        this.bf = new BufferedReader(isr);

    }

    public void setIs(InputStream is) {
        this.is = is;
        InputStreamReader isr = new InputStreamReader(is);
        this.bf = new BufferedReader(isr);
    }

    public ArrayList<DataModel> parse_data() {
        ArrayList<DataModel> data = new ArrayList<>();
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(bf);
            Element root = doc.getRootElement();
            Heading head = new Heading();
            head.setHeading_x(root.getAttributeValue("X-Axis"));
            head.setHeading_y(root.getAttributeValue("Y-Axis"));
            head.setHeading_z(root.getAttributeValue("Z-Axis"));
            for (Element cluster : (List<Element>) root.getChildren()) {
                Color3f s = ParserUtilities.parse_hex_color(cluster.getAttributeValue("color"));
                for (Element item : (List<Element>) cluster.getChildren()) {
                    DataModel d = new DataModel();
                    ArrayList<Double> coords = new ArrayList<>();
                    coords.add(Double.parseDouble(item.getAttributeValue("x")));
                    coords.add(Double.parseDouble(item.getAttributeValue("y")));
                    coords.add(Double.parseDouble(item.getAttributeValue("z")));
                    d.setCoordinates(coords);
                    d.setColor(s);
                    d.setShape(item.getAttributeValue("shape"));
                    d.setLabel(item.getAttributeValue("name"));
                    d.setHeading(head);
                    d.setLink(item.getAttributeValue("URL"));
                    d.setAnnotation(item.getAttributeValue("annotation"));
                    data.add(d);
                }
            }
        } catch (JDOMException | IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return data;
    }
}
