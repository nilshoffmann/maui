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

    public void actionPerformed(ActionEvent ev) {
        new ToolsUIStarter();
    }
}
