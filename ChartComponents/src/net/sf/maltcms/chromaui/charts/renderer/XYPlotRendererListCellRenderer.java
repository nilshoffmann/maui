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
package net.sf.maltcms.chromaui.charts.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class XYPlotRendererListCellRenderer extends JLabel implements ListCellRenderer {

    public XYPlotRendererListCellRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int i,
            boolean isSelected, boolean hasFocus) {
         //Get the selected index. (The index param isn't
        //always valid, so just use the value.)
//        int selectedIndex = ((Integer)value).intValue();

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if(value instanceof XYItemRenderer) {
            String className = ((XYItemRenderer)value).getClass().getSimpleName();
            className = className.replaceFirst("XY", "");
            className = className.replaceFirst("Renderer","");
            setText(className);
        }

        return this;
    }
    
}
