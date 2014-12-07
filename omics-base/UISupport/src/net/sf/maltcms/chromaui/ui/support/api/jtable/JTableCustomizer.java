/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.ui.support.api.jtable;

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

    /**
     *
     * @param table
     */
    public static void changeComperatorToDoubleIfPossible(JTable table) {

        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
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

    /**
     *
     * @param table
     */
    public static void changeComparators(JTable table) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);
        for (int i = 0; i < table.getColumnCount(); i++) {
            Class<?> c = table.getColumnClass(i);
            if (c.equals(Long.class)) {
                rowSorter.setComparator(i, new Comparator<Long>() {

                    @Override
                    public int compare(Long s1, Long s2) {
                        return s1.compareTo(s2);
                    }
                });
            } else if (c.equals(Integer.class)) {
                rowSorter.setComparator(i, new Comparator<Integer>() {

                    @Override
                    public int compare(Integer s1, Integer s2) {
                        return s1.compareTo(s2);
                    }
                });
            } else if (c.equals(Double.class)) {
                rowSorter.setComparator(i, new Comparator<Double>() {

                    @Override
                    public int compare(Double s1, Double s2) {
                        return s1.compareTo(s2);
                    }
                });
            } else if (c.equals(Float.class)) {
                rowSorter.setComparator(i, new Comparator<Float>() {

                    @Override
                    public int compare(Float s1, Float s2) {
                        return s1.compareTo(s2);
                    }
                });
            } else {
                rowSorter.setComparator(i, new Comparator<Object>() {

                    @Override
                    public int compare(Object s1, Object s2) {
                        return s1.toString().compareTo(s2.toString());
                    }
                });
            }
        }
    }

    /**
     *
     * @param table
     */
    public static void fitAllColumnWidth(JTable table) {
        table.setAutoResizeMode(0);
        int size = table.getColumnCount();
        for (int i = 0; i < size; i++) {
            fitColumnWidth(table, i);
        }
    }

    /**
     *
     * @param table
     * @param colIndex
     */
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
                component
                        = renderer.getTableCellRendererComponent(table, valueAt, false, false,
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
