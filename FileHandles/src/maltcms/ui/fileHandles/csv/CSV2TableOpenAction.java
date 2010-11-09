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

public final class CSV2TableOpenAction implements ActionListener {

    private final List<DataObject> context;

    public CSV2TableOpenAction(List<DataObject> context) {
        this.context = new LinkedList<DataObject>(context);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        CSV2TableOpenSupport ms = new CSV2TableOpenSupport(((CSVDataObject) context.get(0)).getPrimaryEntry(),context);
        ms.open();
    }
}
