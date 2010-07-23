/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import java.awt.Component;
import java.util.Comparator;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Bunkowski
 */
public class JTableCustomizer {

    JTable jtable;

    public static void changeComperatorToDoubleIfPossible(JTable table) {

        TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(rowSorter);
        try {
            for (int i = 0; i < table.getColumnCount(); i++) {
                boolean allValuesAreDoubles = true;
                for (int j = 0; j < table.getRowCount(); j++) {
                    if (!(table.getValueAt(j,
                            i) instanceof Double)) {
                        allValuesAreDoubles = false;
                        break;
                    }
                }
                if (allValuesAreDoubles) {
                    rowSorter.setComparator(i, new Comparator<Double>() {

                        @Override
                        public int compare(Double s1, Double s2) {
                            return Double.compare(s1, s2);
                        }
                    });
                }
            }
        } catch (Exception ex) {
        }
    }

    public static void fitAllColumnWidth(JTable table) {
        table.setAutoResizeMode(0);
        int size = table.getColumnCount();
        for (int i = 0; i < size; i++) {
            fitColumnWidth(table, i);
        }
    }

    public static void fitColumnWidth(JTable table, int colIndex) {
        try {
            TableColumn column = table.getColumnModel().getColumn(colIndex);
            if (column == null) {
                return;
            }

            int modelIndex = column.getModelIndex();
            TableCellRenderer renderer, headerRenderer;
            Component component;
            int colContentWidth = 0;
            int headerWidth = 0;
            int rows = table.getRowCount();

//		 Get width of column header
            headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }

            Component comp = headerRenderer.getTableCellRendererComponent(
                    table, column.getHeaderValue(), false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width
                    + table.getIntercellSpacing().width;

//		 Get max width of column content
            for (int i = 0; i < rows; i++) {
                renderer = table.getCellRenderer(i, modelIndex);
                Object valueAt = table.getValueAt(i, modelIndex);
                component =
                        renderer.getTableCellRendererComponent(table, valueAt, false, false,
                        i, modelIndex);
                colContentWidth = Math.max(colContentWidth,
                        component.getPreferredSize().width
                        + table.getIntercellSpacing().width);
            }
            int colWidth = Math.max(colContentWidth, headerWidth) + 15;
            column.setPreferredWidth(colWidth);
            //column.setWidth(colWidth);
            //System.out.println("requiredWidth="+colWidth);
        } catch (Exception ex) {
            return;
        }
    }
}

