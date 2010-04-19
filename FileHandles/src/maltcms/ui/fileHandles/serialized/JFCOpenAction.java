/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.serialized;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.openide.loaders.DataObject;

public final class JFCOpenAction implements ActionListener {

    private final List<DataObject> context;

    public JFCOpenAction(List<DataObject> context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        for (DataObject dataObject : context) {
            //FIXME
            JFCOpenSupport ms = new JFCOpenSupport(((JFCDataObject) dataObject).getPrimaryEntry());
            ms.open();
        }
    }
}
