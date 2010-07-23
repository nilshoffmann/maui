/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.openide.loaders.DataObject;

public final class CSV2JFCOpenAction implements ActionListener {

    private final List<DataObject> context;

    public CSV2JFCOpenAction(List<DataObject> context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        boolean collate = false;
        if (collate) {
            
        } else {
            for (DataObject dataObject : context) {
                //FIXME vorher gucken, ob es nicht schon offen ist!
                CSV2JFCOpenSupport ms = new CSV2JFCOpenSupport(((CSVDataObject) dataObject).getPrimaryEntry());
                ms.open();
            }
        }
    }
}
