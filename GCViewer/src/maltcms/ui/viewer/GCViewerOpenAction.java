/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import org.openide.loaders.DataObject;

public final class GCViewerOpenAction implements ActionListener {

    private final DataObject context;

    public GCViewerOpenAction(DataObject context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        GCViewerOpenSupport os = new GCViewerOpenSupport(((CDFDataObject) context).getPrimaryEntry());
        os.open();
    }
}
