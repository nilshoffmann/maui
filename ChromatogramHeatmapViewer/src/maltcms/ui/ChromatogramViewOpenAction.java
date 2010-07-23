/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import org.openide.loaders.DataObject;

public final class ChromatogramViewOpenAction implements ActionListener {

    private final DataObject context;

    public ChromatogramViewOpenAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        ChromatogramViewOpenSupport os = new ChromatogramViewOpenSupport(((CDFDataObject) context).getPrimaryEntry());
        os.open();
    }
}
