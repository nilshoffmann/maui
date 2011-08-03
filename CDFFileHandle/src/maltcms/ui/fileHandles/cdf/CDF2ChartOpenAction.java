/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.openide.loaders.DataObject;

public final class CDF2ChartOpenAction implements ActionListener {

    private final List<DataObject> context;

    public CDF2ChartOpenAction(List<DataObject> context) {
        this.context = context;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        CDF2ChartOpenSupport cdf2 = new CDF2ChartOpenSupport(((CDFDataObject) context.get(0)).getPrimaryEntry(), context);
        try {
            cdf2.open();
        } catch (NullPointerException npe) {
        }
    }
}
