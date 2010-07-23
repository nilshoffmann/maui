/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import org.openide.loaders.DataObject;

public final class CSV2ListOpenAction implements ActionListener {

    private final List<DataObject> context;

    public CSV2ListOpenAction(List<DataObject> context) {
        this.context = new LinkedList<DataObject>(context);
    }

    public void actionPerformed(ActionEvent ev) {
        CSV2ListOpenSupport ms = new CSV2ListOpenSupport(((CSVDataObject) context.remove(0)).getPrimaryEntry());
        ms.addDataObjects(context);
        ms.open();
    }
}
