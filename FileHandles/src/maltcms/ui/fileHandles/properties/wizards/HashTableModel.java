/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.wizards;

import java.util.Map;
import java.util.Vector;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.openide.util.NotImplementedException;

/**
 *
 * @author mw
 */
public class HashTableModel implements TableModel {

    private Vector<String> header;
    private Map<String, String> property;
    private boolean simplePropertyStyle = false;
    private boolean editable = true;

    public HashTableModel(Vector<String> header, Map<String, String> property) {
        this.header = header;
        this.property = property;
        this.editable = true;
    }

//    public HashTableModel(Vector<String> header, Map<String, String> property, final boolean editable) {
//        this.header = header;
//        this.property = property;
//        this.editable = editable;
//    }
    @Override
    public int getRowCount() {
        int size = this.property.size();
        if (this.property.containsKey(PropertyLoader.OPTIONAL_VARS)) {
            size--;
        }
        if (this.property.containsKey(PropertyLoader.REQUIRED_VARS)) {
            size--;
        }
        if (this.property.containsKey(PropertyLoader.PROVIDED_VARS)) {
            size--;
        }
        return size;
    }

    @Override
    public int getColumnCount() {
        return this.header.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < this.header.size()) {
            return this.header.get(columnIndex);
        }
        return "N/A";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
//            String key = getValueAt(rowIndex, columnIndex - 1).toString();
//            if (!key.equals(NewPipelineElementVisualPanel1.REQUIRED_VARS)
//                    && !key.equals(NewPipelineElementVisualPanel1.OPTIONAL_VARS)
//                    && !key.equals(NewPipelineElementVisualPanel1.PROVIDED_VARS)) {
            return this.editable;
//            }
        }
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            String key = this.property.keySet().toArray(new String[]{})[rowIndex];
            if (this.simplePropertyStyle) {
                String[] tmp = key.split("\\.");
                if (tmp.length > 0) {
                    return tmp[tmp.length - 1];
                } else {
                    return key;
                }
            } else {
                return key;
            }
        } else {
            return this.property.get(this.property.keySet().toArray(new String[]{})[rowIndex]).toString();
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            throw new NotImplementedException("No use");
        } else {
            this.property.put(this.property.keySet().toArray(new String[]{})[rowIndex], aValue.toString());
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

    public void setSimplePropertyStyle(boolean cut) {
        this.simplePropertyStyle = cut;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
