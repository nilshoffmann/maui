/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.openide.loaders.DataObject;

public final class CDF2ToolsUIOpenAction implements ActionListener {

    private final DataObject context;

    public CDF2ToolsUIOpenAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        
            //FIXME vorher gucken, ob es nicht schon offen ist!
            CDFOpenSupport ms = new CDFOpenSupport(((CDFDataObject) context).getPrimaryEntry());
            ms.open();
        
//        new ToolsUIStarter();
    }
}
