/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package maltcms.ui.fileHandles.properties.tools;

import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.openide.util.NotImplementedException;

/**
 *
 * @author Nils Hoffmann
 */
public class HashTableModel implements TableModel {
    protected Class<?> c = null;
    protected boolean editable = true;
    protected Vector<String> header;
    protected Map<String, Object> property;
    protected boolean simplePropertyStyle = true;
    protected JTable table = null;

    public HashTableModel(Vector<String> header, Map<String, Object> property, Class<?> c) {
        this.header = header;
        this.property = property;
        this.editable = true;
        this.c = c;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
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

    //    public WidgetTableModel(Vector<String> header, Map<String, String> property, final boolean editable) {
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            String key = this.property.keySet().toArray(new String[]{})[rowIndex];
            if (this.simplePropertyStyle) {
                System.out.println("Using short property names for class: " + this.c);
                System.out.println("Key: " + key);
                if (c != null && key.startsWith(c.getName())) {
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
                return key;
            }
        } else {
            return this.property.get(this.property.keySet().toArray(new String[]{})[rowIndex]).toString();
        }
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
    public void removeTableModelListener(TableModelListener l) {
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setJTable(JTable t) {
        this.table = t;
    }

    public void setSimplePropertyStyle(boolean cut) {
        this.simplePropertyStyle = cut;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            throw new NotImplementedException("No use");
        } else {
            this.property.put(this.property.keySet().toArray(new String[]{})[rowIndex], aValue.toString());
            if (this.table != null) {
                this.table.repaint();
            }
        }
    }
    
}
