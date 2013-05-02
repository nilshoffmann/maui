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
package maltcms.ui.fileHandles.csv;

import cross.tools.MathTools;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import maltcms.ui.fileHandles.serialized.JFCView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
//import org.openide.util.ImageUtilities;

public final class CSVTableView extends JPanel implements MultiViewElement, ListSelectionListener, MouseListener {

    private static CSVTableView instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "CSV2ListTopComponent";
    private JFCView jfctc = null;
    private int domainColumn = -1;
    private int labelColumn = -1;
    private int zValueColumn = -1;
    private Set<Integer> columnsToPlot = new LinkedHashSet<Integer>();
    private int activeColumn = -1;
    private JToolBar toolbar = new JToolBar();
    private MultiViewElementCallback callback = null;
    private boolean cellSelection = false;

    public CSVTableView() {
        initComponents();
        setName(NbBundle.getMessage(CSVTableView.class, "CTL_CSV2ListTopComponent"));
        setToolTipText(NbBundle.getMessage(CSVTableView.class, "HINT_CSV2ListTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        this.jTable1.setAutoCreateRowSorter(true);
        this.jTable1.setColumnSelectionAllowed(true);
        this.jTable1.setCellSelectionEnabled(true);
        this.jTable1.setUpdateSelectionOnSort(true);
        //FIXME refactor into multiview component
//        this.jfctc = new JFCView();
//        JFrame jf = new JFrame();
//        jf.add(jfctc);
//        jf.setVisible(true);
        this.jTable1.addMouseListener(this);
        this.jTable1.getColumnModel().getSelectionModel().addListSelectionListener(this);
        this.jTable1.getColumnModel().setColumnSelectionAllowed(true);


    }

    public void setTableModel(TableModel tm) {
        if (tm == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Received table model was null!");
            return;
        }
        this.jTable1.setModel(tm);
        //default column model
        for (int i = 0; i < tm.getColumnCount(); i++) {
            this.jTable1.getColumnModel().getColumn(i).setCellRenderer(new ColorColumnRenderer(new Color(255, 255, 255, 255), jTable1.getSelectedRows()));
        }
        JTableCustomizer.changeComparators(this.jTable1);
        JTableCustomizer.fitAllColumnWidth(this.jTable1);
        this.jTable1.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 3) {
                    activeColumn = jTable1.getTableHeader().columnAtPoint(e.getPoint());
                    int[] selectedRows = jTable1.getSelectedRows();
                    int minRow = 0;//
                    int maxRow = jTable1.getRowCount() - 1;//
                    int[] selectedColumns = jTable1.getSelectedColumns();
                    int minCol = 0;
                    int maxCol = jTable1.getColumnCount() - 1;
                    if (cellSelection) {
                        jTable1.setRowSelectionAllowed(true);
                        minRow = MathTools.min(selectedRows);
                        maxRow = MathTools.max(selectedRows);
                        minCol = MathTools.min(selectedColumns);
                        maxCol = MathTools.min(selectedColumns);
                    } else {
                    }

                    jTable1.setColumnSelectionInterval(activeColumn, activeColumn);
                    jTable1.setRowSelectionInterval(minRow, maxRow);
                    createAndShowPopupMenu(e);
                }
            }
        });
    }

    public void setChartTarget(JFCView jfctc) {
        this.jfctc = jfctc;
    }

    private JFreeChart buildChart(Collection<Integer> selectedColumns1, int[] selectedRows, int domainColumn) {
        XYSeriesCollection xysc = new XYSeriesCollection();
        List<Integer> selectedColumns = new LinkedList<Integer>(selectedColumns1);
        System.out.println("buildChart called");
        for (int i = 0; i < selectedColumns.size(); i++) {
            XYSeries xys = new XYSeries(jTable1.getColumnName(selectedColumns.get(i)), true, true);
            System.out.println("Creating XYSeries: " + jTable1.getColumnName(selectedColumns.get(i)));
            for (int j = 0; j < selectedRows.length; j++) {
                if (domainColumn != -1) {
//                    System.out.println("Domain column set");
                    Object o = jTable1.getModel().getValueAt(selectedRows[j], domainColumn);
                    boolean skipMissing = false;
                    Number domainValue = Double.valueOf(0);
                    Number rangeValue = Double.valueOf(0);
//                    System.out.println("Class of object: " + o.getClass().getName());
                    if (o instanceof Number) {
//                        System.out.println("domain instanceof Number");
                        domainValue = (Number) o;
                    } else if (o instanceof String) {
                        String value = (String) o;
                        if (value.equals("-") || value.equals("NA")) {
                            skipMissing = true;
                        } else {
//                        System.out.println("domain instanceof String");
                            domainValue = Double.parseDouble((String) o);
                        }
                    }
                    Object val = jTable1.getModel().getValueAt(selectedRows[j], selectedColumns.get(i));
                    if (val instanceof Number) {
//                        System.out.println("instanceof Number");
                        rangeValue = (Number) val;
                    } else if (val instanceof String) {
                        String value = (String) val;
                        if (value.equals("-") || value.equals("NA")) {
                            skipMissing = true;
                        } else {
//                        System.out.println("range value instanceof String");
                            rangeValue = Double.parseDouble((String) val);
                        }
                    }
                    if (!skipMissing) {
                        xys.add(domainValue, rangeValue);
                    }
                } else {
                    System.out.println("No domain column set");
                    Object val = jTable1.getModel().getValueAt(selectedRows[j], selectedColumns.get(i));
                    if (val instanceof Number) {
//                        System.out.println("instanceof Number");
                        Number rangeValue = (Number) val;
                        xys.add(j, rangeValue);
                    } else if (val instanceof String) {
                        String value = (String) val;
                        if (value.equals("-") || value.equals("NA")) {
                        } else {
//                        System.out.println("instanceof String");
                            Number rangeValue = Double.parseDouble((String) val);
                            xys.add(j, rangeValue);
                        }
                    }
                }
            }
            xysc.addSeries(xys);
        }
        String xaxisLabel = domainColumn == -1 ? "row" : jTable1.getModel().getColumnName(domainColumn);
        String yaxisLabel = "";
        if (selectedColumns.isEmpty()) {
            yaxisLabel = "value";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Integer i : selectedColumns) {
                sb.append(jTable1.getModel().getColumnName(i) + " ");
                if(sb.length()>30) {
                    sb.append("...");
                    break;
                }
            }
            yaxisLabel = sb.toString();
        }
        JFreeChart jfc = ChartFactory.createXYLineChart(callback.getTopComponent().getDisplayName(), xaxisLabel, yaxisLabel, xysc, PlotOrientation.VERTICAL, true, true, true);
        return jfc;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, org.openide.util.NbBundle.getMessage(CSVTableView.class, "CSVTableView.jToggleButton1.text")); // NOI18N
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        this.cellSelection = jToggleButton1.isSelected();
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized CSVTableView getDefault() {
        if (instance == null) {
            instance = new CSVTableView();
        }
        return instance;
    }

    public void logTransformColumn(int j, double base) {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            Object o = jTable1.getValueAt(i, j);
            if (o instanceof Double) {
                jTable1.setValueAt(Double.valueOf(Math.log10(((Double) o).doubleValue()) / Math.log10(base)), i, j);
            } else if (o instanceof Float) {
                jTable1.setValueAt(Double.valueOf(Math.log10(((Double) o).doubleValue()) / Math.log10(base)).floatValue(), i, j);
            }
        }
    }

    public void inverseLogTransformColumn(int j, double base) {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            Object o = jTable1.getValueAt(i, j);
            if (o instanceof Double) {
                jTable1.setValueAt(Double.valueOf(Math.pow(base, ((Double) o).doubleValue())), i, j);
            } else if (o instanceof Float) {
                jTable1.setValueAt(Double.valueOf(Math.pow(base, ((Double) o).doubleValue())).floatValue(), i, j);
            }
        }
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        int[] selectedColumns = this.jTable1.getColumnModel().getSelectedColumns();
        int[] selectedRows = this.jTable1.getSelectedRows();
    }

    private void createAndShowPopupMenu(MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON3 && this.jTable1.getSelectedRowCount() > 0 && this.jfctc != null) {

            JPopupMenu jpm = new JPopupMenu();

            //more than one column selected
            if (jTable1.getSelectedColumnCount() > 0) {
                jpm.add(new AbstractAction("Add as y-values") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        for (int col : jTable1.getSelectedColumns()) {
                            columnsToPlot.add(col);
                            jTable1.getColumnModel().getColumn(col).setCellRenderer(new ColorColumnRenderer(new Color(238, 187, 0, 255), jTable1.getSelectedRows()));
                        }
                        jfctc.setChart(buildChart(labelColumn, domainColumn, columnsToPlot, jTable1.getSelectedRows()));
                    }
                });
                //selected rows
                for (final int col : jTable1.getSelectedColumns()) {
                    if (columnsToPlot.contains(Integer.valueOf(col))) {
                        jpm.add(new AbstractAction("Remove y-values from chart") {

                            @Override
                            public void actionPerformed(ActionEvent ae) {
                                jTable1.getColumnModel().getColumn(Integer.valueOf(col)).setCellRenderer(new ColorColumnRenderer(Color.WHITE, jTable1.getSelectedRows()));
                                columnsToPlot.remove(Integer.valueOf(col));
                                jfctc.setChart(buildChart(labelColumn, domainColumn, columnsToPlot, jTable1.getSelectedRows()));
                            }
                        });
                        break;
                    }
                }
                jpm.add(new JPopupMenu.Separator());
                jpm.add(new AbstractAction("Log10 transform") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        final int[] cols = jTable1.getSelectedColumns();
                        for (int i = 0; i < jTable1.getSelectedColumnCount(); i++) {
                            logTransformColumn(cols[i], 10.0d);
                        }

                    }
                });
                jpm.add(new AbstractAction("Inverse Log10 transform") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        final int[] cols = jTable1.getSelectedColumns();
                        for (int i = 0; i < jTable1.getSelectedColumnCount(); i++) {
                            inverseLogTransformColumn(cols[i], 10.0d);
                        }

                    }
                });
            }
            jpm.add(new JPopupMenu.Separator());
            //exactly one selected column, no domain column
            if (jTable1.getSelectedColumnCount() == 1 && domainColumn == -1) {
                jpm.add(new AbstractAction("Set as x-values") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        domainColumn = jTable1.getSelectedColumn();
                        jTable1.getColumnModel().getColumn(domainColumn).setCellRenderer(new ColorColumnRenderer(new Color(255, 0, 0, 255), jTable1.getSelectedRows()));
                        jfctc.setChart(buildChart(labelColumn, domainColumn, columnsToPlot, jTable1.getSelectedRows()));
                    }
                });

            }
            //one domainColumn
            if (domainColumn != -1) {
                jpm.add(new AbstractAction("Reset x-axis values") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        jTable1.getColumnModel().getColumn(domainColumn).setCellRenderer(new ColorColumnRenderer(new Color(255, 255, 255, 255), jTable1.getSelectedRows()));
                        domainColumn = -1;
                        jfctc.setChart(buildChart(labelColumn, domainColumn, columnsToPlot, jTable1.getSelectedRows()));
                    }
                });
            }
            jpm.add(new JPopupMenu.Separator());
            if (jTable1.getSelectedColumnCount() == 1 && labelColumn == -1) {
                jpm.add(new AbstractAction("Set as series labels") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        labelColumn = jTable1.getSelectedColumn();
                        jTable1.getColumnModel().getColumn(labelColumn).setCellRenderer(new ColorColumnRenderer(new Color(255, 255, 0, 255), jTable1.getSelectedRows()));
                        jfctc.setChart(buildChart(labelColumn, domainColumn, columnsToPlot, jTable1.getSelectedRows()));
                    }
                });
            }

            if (jTable1.getSelectedColumnCount() == 1 && labelColumn != -1) {
                jpm.add(new AbstractAction("Reset series labels") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        jTable1.getColumnModel().getColumn(labelColumn).setCellRenderer(new ColorColumnRenderer(new Color(255, 255, 255, 255), jTable1.getSelectedRows()));
                        labelColumn = -1;
                        jfctc.setChart(buildChart(labelColumn, domainColumn, columnsToPlot, jTable1.getSelectedRows()));
                    }
                });
            }

            jpm.show(me.getComponent(), me.getX(), me.getY());
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        createAndShowPopupMenu(me);
    }

    public JFreeChart buildChart(int labelColumn, int domainColumn, Collection<Integer> selectedColumns1, int[] selectedRows) {
        return buildChart(labelColumn, domainColumn, -1, selectedColumns1, selectedRows);
    }

    public JFreeChart buildChart(int labelColumn, int domainColumn, int zValuesColumn, Collection<Integer> selectedColumns1, int[] selectedRows) {
        if (labelColumn != -1 && zValuesColumn == -1) {
            return buildChartWithLabels(labelColumn, domainColumn, selectedColumns1, selectedRows);
        }
        if (labelColumn != -1 && zValuesColumn != -1) {
            return buildBubbleChartWithLabels(labelColumn, zValuesColumn, domainColumn, selectedColumns1, selectedRows);
        }
        return buildChart(selectedColumns1, selectedRows, domainColumn);
    }

    public JFreeChart buildBubbleChartWithLabels(int labelColumn, int zValuesColumn, int domainColumn, Collection<Integer> selectedColumns1, int[] selectedRows) {
        XYSeriesCollection xysc = new XYSeriesCollection();
        List<Integer> selectedColumns = new LinkedList<Integer>();
        selectedColumns.addAll(selectedColumns1);
        //if an additional label column is defined, we use this to further partition
        //the table by group labels
        HashMap<String, Integer> labelToIndex = getLabelToIndex(jTable1, labelColumn);

        //TODO use file label and group label

        for (int i = 0; i < selectedColumns.size(); i++) {
            for (String label : labelToIndex.keySet()) {
                xysc.addSeries(new XYSeries(label + "-" + jTable1.getColumnName(selectedColumns.get(i)), true, true));
            }
            for (int j = 0; j < selectedRows.length; j++) {
                XYSeries xys = xysc.getSeries(jTable1.getModel().getValueAt(selectedRows[j], labelColumn).toString() + "-" + jTable1.getColumnName(selectedColumns.get(i)));
                if (domainColumn != -1) {
                    System.out.println("Domain column set");
                    Object o = jTable1.getModel().getValueAt(selectedRows[j], domainColumn);
                    Number domainValue = Double.valueOf(0);
                    System.out.println("Class of object: " + o.getClass().getName());
                    if (o instanceof Number) {
                        System.out.println("domain instanceof Number");
                        domainValue = ((Number) o);
                    }
                    Object val = jTable1.getModel().getValueAt(selectedRows[j], selectedColumns.get(i));
                    if (val instanceof Number) {
                        System.out.println("instanceof Number");
                        Number rangeValue = ((Number) val);
                        xys.add(domainValue, rangeValue);
                    }
                } else {
                    System.out.println("No domain column set");
                    Object val = jTable1.getModel().getValueAt(selectedRows[j], selectedColumns.get(i));
                    if (val instanceof Number) {
                        System.out.println("instanceof Number");
                        Number rangeValue = ((Number) val);
                        xys.add(j, rangeValue);
                    }
                }

            }
        }
        String xaxisLabel = domainColumn == -1 ? "row" : jTable1.getModel().getColumnName(domainColumn);
        String yaxisLabel = "";
        if (selectedColumns.isEmpty()) {
            yaxisLabel = "value";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Integer i : selectedColumns) {
                sb.append(jTable1.getModel().getColumnName(i) + " ");
                if(sb.length()>30) {
                    sb.append("...");
                    break;
                }
            }
            yaxisLabel = sb.toString();
        }
        JFreeChart jfc = ChartFactory.createXYLineChart(callback.getTopComponent().getDisplayName(), xaxisLabel, yaxisLabel, xysc, PlotOrientation.VERTICAL, true, true, true);
        return jfc;
    }

    private HashMap<String, Integer> getLabelToIndex(JTable jt, int labelColumn) {
        HashSet<String> hm = new LinkedHashSet<String>();
        for (int i = 0; i < jt.getRowCount(); i++) {
            hm.add(String.valueOf(jt.getModel().getValueAt(i, labelColumn)));
        }
        HashMap<String, Integer> placeMap = new LinkedHashMap<String, Integer>();
        int cnt = 0;
        for (String s : hm) {
            System.out.println("Label: " + s);
            placeMap.put(s, cnt++);
        }
        return placeMap;
    }

    public JFreeChart buildChartWithLabels(int labelColumn, int domainColumn, Collection<Integer> selectedColumns1, int[] selectedRows) {
        XYSeriesCollection xysc = new XYSeriesCollection();
        List<Integer> selectedColumns = new LinkedList<Integer>();
        selectedColumns.addAll(selectedColumns1);
        //if an additional label column is defined, we use this to further partition
        //the table by group labels
        HashMap<String, Integer> labelToIndex = getLabelToIndex(jTable1, labelColumn);

        for (int i = 0; i < selectedColumns.size(); i++) {
            for (String label : labelToIndex.keySet()) {
                xysc.addSeries(new XYSeries(label + "-" + jTable1.getColumnName(selectedColumns.get(i)), true, true));
            }
            for (int j = 0; j < selectedRows.length; j++) {
                XYSeries xys = xysc.getSeries(jTable1.getModel().getValueAt(selectedRows[j], labelColumn).toString() + "-" + jTable1.getColumnName(selectedColumns.get(i)));
                if (domainColumn != -1) {
                    System.out.println("Domain column set");
                    Object o = jTable1.getModel().getValueAt(selectedRows[j], domainColumn);
                    Number domainValue = Double.valueOf(0);
                    Number rangeValue = Double.valueOf(0);
                    System.out.println("Class of object: " + o.getClass().getName());
                    boolean skipMissing = false;
                    if (o instanceof Number) {
                        System.out.println("domain instanceof Number");
                        domainValue = ((Number) o);
                    } else if (o instanceof String) {
                        System.out.println("domain instanceof String");
                        String value = (String) o;
                        if (value.equals("-") || value.equals("NA")) {
                            skipMissing = true;
                        } else {
                            domainValue = Double.parseDouble((String) o);
                        }
                    }
                    Object val = jTable1.getModel().getValueAt(selectedRows[j], selectedColumns.get(i));
                    if (val instanceof Number) {
                        System.out.println("instanceof Number");
                        rangeValue = ((Number) val);

                    } else if (val instanceof String) {
                        System.out.println("value instanceof String");
                        String value = (String) val;
                        if (value.equals("-") || value.equals("NA")) {
                            skipMissing = true;
                        } else {
                            rangeValue = Double.parseDouble((String) val);
                        }
                    }
                    if (!skipMissing) {
                        xys.add(domainValue, rangeValue);
                    }
                } else {
                    System.out.println("No domain column set");
                    Object val = jTable1.getModel().getValueAt(selectedRows[j], selectedColumns.get(i));
                    Number rangeValue = Double.valueOf(0);
                    boolean skipMissing = false;
                    if (val instanceof Number) {
                        System.out.println("instanceof Number");
                        rangeValue = ((Number) val);
                    } else if (val instanceof String) {
                        System.out.println("instance of String");
                        String value = (String) val;
                        if (value.equals("-") || value.equals("NA")) {
                            skipMissing = true;
                        } else {
                            rangeValue = Double.parseDouble((String) val);
                        }
                    }
                    if (!skipMissing) {
                        xys.add(j, rangeValue);
                    }
                }

            }
        }
        String xaxisLabel = domainColumn == -1 ? "row" : jTable1.getModel().getColumnName(domainColumn);
        String yaxisLabel = "";
        if (selectedColumns.isEmpty()) {
            yaxisLabel = "value";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Integer i : selectedColumns) {
                sb.append(jTable1.getModel().getColumnName(i) + " ");
                if(sb.length()>30) {
                    sb.append("...");
                    break;
                }
            }
            yaxisLabel = sb.toString();
        }
        JFreeChart jfc = ChartFactory.createXYLineChart(callback.getTopComponent().getDisplayName(), xaxisLabel, yaxisLabel, xysc, PlotOrientation.VERTICAL, true, true, true);
        return jfc;
    }

    @Override
    public void mousePressed(MouseEvent me) {

        if (jTable1.getSelectedColumn() != activeColumn) {
            jTable1.setRowSelectionAllowed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return this.toolbar;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        this.callback = mvec;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public Action[] getActions() {
//        if(callback!=null) {
//            return callback.createDefaultActions();
//        }else{
//            return new Action[]{};
//        }
        return new Action[]{new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                }
            }};
    }

    @Override
    public Lookup getLookup() {
        return Lookups.singleton(this);
    }

    @Override
    public void componentShowing() {
        if (callback != null) {
            callback.updateTitle("Table view");
        }
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
        if (callback != null) {
            callback.updateTitle("Table view");
        }
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }
}
