/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.openide.loaders.DataObject;

public final class Properties2PipelineEditorOpenAction implements ActionListener {

    private final List<DataObject> context;

    public Properties2PipelineEditorOpenAction(List<DataObject> context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        for (DataObject dataObject : context) {
            //FIXME
            PipelineEditorOpenSupport ms = new PipelineEditorOpenSupport(((PropertiesDataObject) dataObject).getPrimaryEntry());
            ms.open();
        }
    }
}
