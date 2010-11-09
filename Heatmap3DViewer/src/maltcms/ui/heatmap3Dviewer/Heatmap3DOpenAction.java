/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.heatmap3Dviewer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import org.openide.loaders.MultiDataObject;

public final class Heatmap3DOpenAction implements ActionListener {

    private final List<MultiDataObject> context;

    public Heatmap3DOpenAction(List<MultiDataObject> context) {
        this.context = new LinkedList<MultiDataObject>(context);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for(MultiDataObject dob:context) {
                Heatmap3DOpenSupport hmos = new Heatmap3DOpenSupport(dob.getPrimaryEntry());
                hmos.open();
        }
    }
}
