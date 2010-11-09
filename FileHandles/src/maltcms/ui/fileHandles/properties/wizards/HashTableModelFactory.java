/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.wizards;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;

/**
 *
 * @author nilshoffmann
 */
public class HashTableModelFactory {

    public TableModel create(PipelineElementWidget node, JTable table, boolean simpleMode, Class<?> c) {
        HashTableModel htm =
                PropertyLoader.getModel(node.getProperties(), c);
        htm.setPipelineElementWidgetNode(node);
        table.setModel(htm);
        htm.setJTable(table);
        htm.setSimplePropertyStyle(simpleMode);
        return htm;
    }
}
