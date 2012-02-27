/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibielefeld.cebitec.lstutz.pca.data;

import java.awt.Color;
import java.util.ArrayList;
import javax.vecmath.Color3f;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;

/**
 *
 * @author nilshoffmann
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
