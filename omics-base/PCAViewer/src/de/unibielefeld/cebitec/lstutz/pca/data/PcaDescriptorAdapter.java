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
package de.unibielefeld.cebitec.lstutz.pca.data;

import java.awt.Color;
import java.util.ArrayList;
import javax.vecmath.Color3f;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;

/**
 *
 * @author Nils Hoffmann
 */
public class PcaDescriptorAdapter {

    public ArrayList<DataModel> parse_data(IPcaDescriptor pcaDescr) {
        ArrayList<DataModel> data = new ArrayList<DataModel>();
        Heading head = new Heading();
        head.setHeading_x("PC1");
        head.setHeading_y("PC2");
        head.setHeading_z("PC3");
        System.out.println("Shape of x: "+pcaDescr.getX().shapeToString());
        int pcs = 3;//Math.min(3,pcaDescr.getX().getShape()[1]);
        int row = 0;
        for(IChromatogramDescriptor descr:pcaDescr.getCases()) {
            DataModel dm = new DataModel();
            dm.setLabel(descr.getDisplayName());
            dm.setHeading(head);
            Color c = descr.getTreatmentGroup().getColor();
            if(c == null) {
                c = Color.RED;
            }
            dm.setColor(new Color3f(c));//pcaDescr.get
            for(int i = 0;i<pcs;i++) {
                dm.getCoordinates().add(pcaDescr.getX().get(row, i));
            }
            data.add(dm);
            row++;
        }
        System.out.println(data.size() + " Eintraege importiert...");
        return data;
    }
}
