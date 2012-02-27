/*
 * $license$
 *
 * $Id$
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
