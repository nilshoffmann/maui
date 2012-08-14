/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.wizards;

import maltcms.ui.fileHandles.properties.tools.HashTableModel;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.openide.util.NotImplementedException;

/**
 *
 * @author mw
 */
public class WidgetTableModel extends HashTableModel {
    private PipelineElementWidget w = null;

    public WidgetTableModel(Vector<String> header, Map<String, Object> property, Class<?> c) {
        super(header, property, c);
    }

    // TODO remove dirty style
    public void setPipelineElementWidgetNode(PipelineElementWidget w) {
        this.w = w;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            throw new NotImplementedException("No use");
        } else {
            if (this.w == null) {
                this.property.put(this.property.keySet().toArray(new String[]{})[rowIndex], aValue.toString());
            } else {
                this.w.setProperty(this.property.keySet().toArray(new String[]{})[rowIndex], aValue.toString());
            }
            if (this.table != null) {
                this.table.repaint();
            }
        }
    }
}
