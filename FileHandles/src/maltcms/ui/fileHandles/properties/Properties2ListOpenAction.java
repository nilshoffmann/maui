/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.openide.loaders.DataObject;

public final class Properties2ListOpenAction implements ActionListener {

    private final List<DataObject> context;

    public Properties2ListOpenAction(List<DataObject> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (DataObject dataObject : context) {
            System.out.println(dataObject.getPrimaryFile().getPath());
            Properties2ListOpenSupport ms = new Properties2ListOpenSupport(((PropertiesDataObject) dataObject).getPrimaryEntry());
            ms.open();
        }
    }
}
