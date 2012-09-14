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
