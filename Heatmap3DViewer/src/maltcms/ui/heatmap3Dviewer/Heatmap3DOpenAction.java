/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.heatmap3Dviewer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;

public final class Heatmap3DOpenAction implements ActionListener {

    private final List<DataObject> context;

    public Heatmap3DOpenAction(List<DataObject> context) {
        this.context = new LinkedList<DataObject>(context);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for(DataObject dob:context) {
            Heatmap3DOpenSupport hmos = new Heatmap3DOpenSupport(((PNGHeatmapDataObject)dob).getPrimaryEntry());
            hmos.open();
        }
    }
}
