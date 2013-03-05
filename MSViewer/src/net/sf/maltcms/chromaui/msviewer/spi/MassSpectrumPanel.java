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
package net.sf.maltcms.chromaui.msviewer.spi;

import net.sf.maltcms.db.search.api.ui.DatabaseSearchPanel;
import java.awt.Color;
import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.IScan1D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.ms.Scan1D;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.charts.dataset.MSSeries;
import net.sf.maltcms.chromaui.charts.format.ScaledNumberFormatter;
import net.sf.maltcms.chromaui.charts.labels.TopKItemsLabelGenerator;
import net.sf.maltcms.chromaui.charts.tools.ChromatogramVisualizerTools;
import net.sf.maltcms.chromaui.lookupResultListener.api.AbstractLookupResultListener;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.chromaui.lookupResultListener.api.LookupResultListeners;
import net.sf.maltcms.db.search.api.IQuery;
import net.sf.maltcms.db.search.api.IQueryFactory;
import net.sf.maltcms.db.search.api.IQueryResult;
import net.sf.maltcms.db.search.api.QueryResultList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import ucar.ma2.Array;

/**
 * In order to receive IScan events, the MassSpectrumPanel registers on the
 * supplied Lookup object. However, you should always place a
 * IChromatogramDescriptor Object alongside with the IScan object in the
 * providing lookup.
 *
 * @author nilshoffmann
 * @author Mathias Wilhelm
 */
public class MassSpectrumPanel extends JPanel implements LookupListener {

    private XYSeriesCollection sc, tmp;
    private XYPlot plot;
    private HashMap<Comparable, Double> scales = new HashMap<Comparable, Double>();
    private ExecutorService es = Executors.newFixedThreadPool(1);
    private List<?> annotations = Collections.emptyList();
    private List<Point> selectedPoints = new LinkedList<Point>();
    private int topK = 10;
    private int activeMS = -1;
    private ScaledNumberFormatter defaultNumberFormat = new ScaledNumberFormatter();
    private ChartPanel cp;
    private Result<ISelection> lookupResult;
//    private Result<IScan2D> scan2DSelection;
    private Result<IMetabolite> metaboliteSelection;
    private Result<IPeakAnnotationDescriptor> peakAnnotationDescriptorResult;
    private HashMap<MSSeries, IScan> seriesToScan = new LinkedHashMap<MSSeries, IScan>();
    private DatabaseSearchPanel ddp = null;
    private Lookup contentProviderLookup;
    private boolean addSeriesToTopPlot = true;

    public MassSpectrumPanel(Lookup contentProviderLookup) {
        System.out.println("Opening MassSpectrumPanel");
        this.contentProviderLookup = contentProviderLookup;
        initComponents();
        initChartComponents();
    }

    /**
     * Creates new form MassSpectrumPanel
     */
    public MassSpectrumPanel() {
        this(Utilities.actionsGlobalContext());
    }

    private void initChartComponents() {

        this.sc = new XYSeriesCollection();
        XYBarDataset d = new XYBarDataset(sc, 0.5d);
        XYBarRenderer renderer = new XYBarRenderer(0.1d);
        StandardXYBarPainter sp = new StandardXYBarPainter();
        renderer.setBarPainter(sp);
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        NumberAxis intensityAxis = new NumberAxis("intensity");
        intensityAxis.setNumberFormatOverride(defaultNumberFormat);
        intensityAxis.setUpperMargin(0.10d);
        NumberAxis mzAxis = new NumberAxis("m/z");
        mzAxis.setAutoRangeIncludesZero(false);
        this.plot = new XYPlot(d, mzAxis, intensityAxis, renderer);
        this.plot.setForegroundAlpha(0.85f);

        plot.setDomainCrosshairLockedOnData(true);
        plot.setDomainCrosshairVisible(true);
        ((XYBarRenderer) plot.getRenderer()).setShadowVisible(false);
        ((XYBarRenderer) plot.getRenderer()).setDrawBarOutline(false);
        ((XYBarRenderer) plot.getRenderer()).setBaseFillPaint(Color.RED);
        ((XYBarRenderer) plot.getRenderer()).setBarPainter(
                new StandardXYBarPainter());
        plot.getRenderer().setBaseItemLabelsVisible(true);
        plot.getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyd, int i, int i1) {
                Comparable comp = xyd.getSeriesKey(i);
                double x = xyd.getXValue(i, i1);
                double y = xyd.getYValue(i, i1);
                StringBuilder sb = new StringBuilder();
                sb.append(comp);
                sb.append(": ");
                sb.append("x=");
                sb.append(String.format("%.2f", x));
                sb.append(" y=");
                sb.append(String.format("%.2f", y));
                return sb.toString();
            }
        });
        JFreeChart msChart = new JFreeChart(this.plot);
        msChart.addChangeListener(this.defaultNumberFormat);
        System.out.println("Creating ms chart 3");
        this.cp = new ChartPanel(msChart);
        this.cp.setInitialDelay(1);
        this.cp.getChart().getLegend().setVisible(true);
        this.clearActionPerformed(null);
        this.jPanel2.removeAll();
        this.jPanel2.add(cp);
        this.jPanel2.repaint();
    }

    public void addIdentification() {
        // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
        NotifyDescriptor nd = new NotifyDescriptor(
                ddp, // instance of your panel
                "Select Databases and Settings", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );
        ddp.updateView();
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            if (ddp.getSelectedDatabases().isEmpty()) {
                return;
            }
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    IQuery<IScan> query = Lookup.getDefault().lookup(
                            IQueryFactory.class).createQuery(
                            ddp.getSelectedDatabases(), ddp.getRetentionIndexCalculator(), ddp.getSelectedMetabolitePredicate(), ddp.getMatchThreshold(),
                            ddp.getMaxNumberOfHits(), ddp.getRIWindow(), seriesToScan.values().
                            toArray(new IScan[seriesToScan.size()]));
                    try {
                        List<QueryResultList<IScan>> results = query.call();
                        Box outerBox = Box.createVerticalBox();
                        for (QueryResultList<IScan> mdqrl : results) {
                            for (IQueryResult<IScan> result : mdqrl) {
                                Box vbox = Box.createVerticalBox();
                                JLabel label = new JLabel("Results for scan " + result.getScan().getScanIndex() + " at " + result.getScan().getScanAcquisitionTime() + " with ri: " + result.getRetentionIndex());
                                JLabel dbLabel = new JLabel("DB: " + result.getDatabaseDescriptor().
                                        getResourceLocation());
                                vbox.add(label);
                                vbox.add(dbLabel);
                                JLabel parameterLabel = new JLabel("Minimum Score: " + ddp.getMatchThreshold() + "; Maximum #Hits Returned: " + ddp.getMaxNumberOfHits());
                                vbox.add(parameterLabel);
                                DefaultTableModel dlm = new DefaultTableModel(new String[]{
                                            "Score", "RI", "Name", "ID", "MW",
                                            "Formula",
                                            "Max Mass"}, 0);
                                for (IMetabolite metabolite : result.getMetabolites()) {
                                    String name = metabolite.getName();
                                    if (name.lastIndexOf("_") != -1) {
                                        name = name.substring(
                                                name.lastIndexOf(
                                                "_") + 1);
                                    }
                                    dlm.addRow(new Object[]{result.getScoreFor(
                                                metabolite), result.getRiFor(
                                                metabolite), name, metabolite.getID(), metabolite.getMW(),
                                                metabolite.getFormula(),
                                                metabolite.getMaxMass()});
                                }
                                JTable jl = new JTable(dlm);
                                JScrollPane resultScrollPane = new JScrollPane(
                                        jl);
                                jl.setAutoCreateRowSorter(true);
                                jl.setUpdateSelectionOnSort(true);
                                vbox.add(resultScrollPane);
                                outerBox.add(vbox);
                                outerBox.add(Box.createVerticalStrut(10));
                            }
                        }
                        JScrollPane jsp = new JScrollPane(outerBox);
                        NotifyDescriptor nd = new NotifyDescriptor(
                                jsp, // instance of your panel
                                "Database Search Results", // title of the dialog
                                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                                // otherwise specify options as:
                                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                                NotifyDescriptor.OK_OPTION // default option is "Yes"
                                );
                        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                        }
                        //DBConnectionManager.close();
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            };
            RequestProcessor.getDefault().post(r);

        }
    }

//        System.out.println("Adding id!");
//        Runnable s = new Runnable() {
//
//            @Override
//            public void run() {
//
////                Point l = selectedPoints.get(selectedPoints.size() - 1);
////                System.out.println("Identification for " + l);
//                XYSeries xys = sc.getSeries(sc.getSeriesCount() - 1);
//                List<Double> massFilter = new ArrayList<Double>(Arrays.asList(new Double[]{
//                            73d, 74d, 75d, 147d, 148d, 149d}));
//                Array masses = new ArrayDouble.D1(xys.getItemCount());
//                Array intensities = new ArrayDouble.D1(xys.getItemCount());
//                double minMassT = 70;
//                intensities = filterQuerySpectrum(xys, masses, minMassT,
//                        intensities, massFilter);
//                MinMax mm = MAMath.getMinMax(intensities);
////                Tuple2D<Array,Array> mss = ChromatogramVisualizerTools.getMS(l, ic.getFilename());
////                Array ms = mss.getFirst();
////                Array inten = mss.getSecond();
//                PeakIdentification pi = PeakIdentification.getInstance();
//                System.out.println("Start");
////                Tuple2D<Array, Tuple2D<Double, IMetabolite>> bestHit = pi.getBest(ms);
//                Tuple2D<Array, Tuple2D<Double, IMetabolite>> bestHit = pi.
//                        getBest(masses, intensities);
//                System.out.println("Done");
//
//                XYSeries s = null;//ChromatogramVisualizerTools.convertToSeries(ms, bestHit.getSecond(), ic.getFilename());
//                String hitName = bestHit.getSecond().getSecond().getName();
//                hitName = hitName.substring(hitName.lastIndexOf("_") + 1);
//                s = new XYSeries(hitName + " (" + (int) (1000d * bestHit.
//                        getSecond().getFirst()) + ")");
//                Array bhmass = bestHit.getSecond().getSecond().getMassSpectrum().
//                        getFirst();
//                Array bhint = bestHit.getSecond().getSecond().getMassSpectrum().
//                        getSecond();
//                System.out.println(bestHit.getSecond().getSecond().getID());
//                MinMax bhmm = MAMath.getMinMax(bhint);
//                System.out.println(
//                        "MM max: " + mm.max + " bhmm max: " + bhmm.max);
//                for (int i = 0; i < bhmass.getShape()[0]; i++) {
//                    double mz = bhmass.getDouble(i);
//                    double intens = bhint.getDouble(i);
//                    double normIntens = (intens / (bhmm.max)) * mm.max;
//                    System.out.println(
//                            "MZ: " + mz + " intens: " + intens + " normIntens: " + normIntens);
//                    s.add(mz, normIntens);
//                }
//                //sc = new XYSeriesCollection();
////                sc.addSeries(ChromatogramVisualizerTools.convertToSeries(bestHit.getFirst(), l.x + ", " + l.y));
//                sc.removeAllSeries();
//                sc.addSeries(xys);
//                sc.addSeries(s);
////                try {
////                    sc.getSeries(s.getKey());
////                } catch (Exception e) {
////                    sc.addSeries(s);
////                }
//                ((XYPlot) cp.getChart().getPlot()).setDataset(sc);
//                updateActiveMassSpectrum();
//                addTopKLabels(topK, activeMS);
////                } else {
////                    sc = new XYSeriesCollection();
////                    sc.addSeries(s);
////                    //cp.repaint();
////                    ((XYPlot) cp.getChart().getPlot()).setDataset(sc);
////                }
//            }
//
//    private Array filterQuerySpectrum(XYSeries xys, Array masses,
//            double minMassT, Array intensities, List<Double> massFilter) {
//        for (int i = 0; i < xys.getItemCount(); i++) {
//            double mass = xys.getX(i).doubleValue();
//            masses.setDouble(i, mass);
//            if (mass >= minMassT) {
//                intensities.setDouble(i, xys.getY(i).doubleValue());
//            }
//        }
//        List<Integer> maskedIndices = MaltcmsTools.findMaskedMasses(
//                masses, massFilter, 0);
//        intensities = ArrayTools.filterIndices(intensities,
//                maskedIndices, 0);
//        return intensities;
//    }
//        };
//        ExecutorService e = Executors.newSingleThreadExecutor();
//        e.execute(s);
//        e.shutdown();
//        try {
//            e.awaitTermination(1, TimeUnit.MINUTES);
//        } catch (InterruptedException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//}
    private void updateActiveMassSpectrum() {
        Object[] obj = new Object[sc.getSeriesCount()];
        for (int i = 0; i < sc.getSeriesCount(); i++) {
            obj[i] = sc.getSeries(i).getKey();
        }
        activeMassSpectrum.setModel(new DefaultComboBoxModel(obj));
    }

    private void addTopKLabels(int topk, int series) {
        System.out.println("addTopKLabels: " + topk + " " + series);
        if (series >= 0) {
            SortedMap<Double, Double> tm = null;
            if (addSeriesToTopPlot) {
                tm = new TreeMap<Double, Double>();
            } else {
                tm = new TreeMap<Double, Double>(new Comparator<Double>() {
                    @Override
                    public int compare(Double t, Double t1) {
                        return -Double.compare(t, t1);
                    }
                });
            }
            for (int i = 0; i < sc.getItemCount(series); i++) {
                double mass = sc.getXValue(series, i);
                double intens = sc.getYValue(series, i);
                tm.put(intens, mass);
            }
            if (activeMS >= 0) {
                System.out.println("Updating plot");
                plot.getRenderer().setBaseItemLabelsVisible(true);
                plot.getRenderer().setSeriesItemLabelGenerator(activeMS,
                        new TopKItemsLabelGenerator(tm, topk));
                plot.getRenderer().setSeriesItemLabelsVisible(activeMS, true);
                plot.notifyListeners(new PlotChangeEvent(plot));
                ChartCustomizer.setSeriesColors(plot, 0.95f);
                ChartCustomizer.setSeriesStrokes(plot, 2.0f);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        clear = new javax.swing.JButton();
        addMs = new javax.swing.JToggleButton();
        addSeriesTop = new javax.swing.JToggleButton();
        absoluteRelativeToggle = new javax.swing.JToggleButton();
        fixXAxis = new javax.swing.JToggleButton();
        fixYAxis = new javax.swing.JToggleButton();
        hideAnnotations = new javax.swing.JToggleButton();
        activeMassSpectrum = new javax.swing.JComboBox();
        remove = new javax.swing.JButton();
        identify = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("net/sf/maltcms/chromaui/msviewer/spi/Bundle"); // NOI18N
        clear.setText(bundle.getString("MassSpectrumPanel.clear.text")); // NOI18N
        clear.setFocusable(false);
        clear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        clear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        jToolBar1.add(clear);

        addMs.setText(bundle.getString("MassSpectrumPanel.addMs.text")); // NOI18N
        addMs.setFocusable(false);
        addMs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addMs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addMs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMsActionPerformed(evt);
            }
        });
        jToolBar1.add(addMs);

        addSeriesTop.setSelected(true);
        addSeriesTop.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.addSeriesTop.text")); // NOI18N
        addSeriesTop.setFocusable(false);
        addSeriesTop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSeriesTop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addSeriesTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSeriesTopActionPerformed(evt);
            }
        });
        jToolBar1.add(addSeriesTop);

        absoluteRelativeToggle.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.absoluteRelativeToggle.text")); // NOI18N
        absoluteRelativeToggle.setFocusable(false);
        absoluteRelativeToggle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        absoluteRelativeToggle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        absoluteRelativeToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                absoluteRelativeToggleActionPerformed(evt);
            }
        });
        jToolBar1.add(absoluteRelativeToggle);

        fixXAxis.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.fixXAxis.text")); // NOI18N
        fixXAxis.setFocusable(false);
        fixXAxis.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fixXAxis.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        fixXAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fixXAxisActionPerformed(evt);
            }
        });
        jToolBar1.add(fixXAxis);

        fixYAxis.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.fixYAxis.text")); // NOI18N
        fixYAxis.setFocusable(false);
        fixYAxis.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fixYAxis.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        fixYAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fixYAxisActionPerformed(evt);
            }
        });
        jToolBar1.add(fixYAxis);

        hideAnnotations.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.hideAnnotations.text")); // NOI18N
        hideAnnotations.setFocusable(false);
        hideAnnotations.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        hideAnnotations.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        hideAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideAnnotationsActionPerformed(evt);
            }
        });
        jToolBar1.add(hideAnnotations);

        activeMassSpectrum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeMassSpectrumActionPerformed(evt);
            }
        });
        jToolBar1.add(activeMassSpectrum);

        remove.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.remove.text")); // NOI18N
        remove.setFocusable(false);
        remove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        remove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jToolBar1.add(remove);

        identify.setText(org.openide.util.NbBundle.getMessage(MassSpectrumPanel.class, "MassSpectrumPanel.identify.text")); // NOI18N
        identify.setEnabled(false);
        identify.setFocusable(false);
        identify.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        identify.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        identify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                identifyActionPerformed(evt);
            }
        });
        jToolBar1.add(identify);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addMsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMsActionPerformed
    }//GEN-LAST:event_addMsActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        this.sc.removeAllSeries();
        this.sc = new XYSeriesCollection();
        this.scales.clear();
        this.seriesToScan.clear();
        this.cp.repaint();
        this.activeMassSpectrum.setModel(
                new DefaultComboBoxModel(new Object[]{}));
        this.plot.setDataset(sc);
    }//GEN-LAST:event_clearActionPerformed

    private void fixXAxisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fixXAxisActionPerformed
        this.plot.getDomainAxis().setAutoRange(!fixXAxis.isSelected());
    }//GEN-LAST:event_fixXAxisActionPerformed

    private void fixYAxisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fixYAxisActionPerformed
        this.plot.getRangeAxis().setAutoRange(!fixYAxis.isSelected());
    }//GEN-LAST:event_fixYAxisActionPerformed

    private void hideAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideAnnotationsActionPerformed
//        this.annotations = this.plot.getAnnotations();
//        this.plot.clearAnnotations();
//        if (!hideAnnotations.isSelected()) {
//            int i = 0;
//            for (Object o : this.annotations) {
//                if (i == this.annotations.size() - 1) {
//                    this.plot.addAnnotation((XYAnnotation) o, true);
//                } else {
//                    this.plot.addAnnotation((XYAnnotation) o, false);
//                }
//                i++;
//
//            }
//        }
        int idx = activeMassSpectrum.getSelectedIndex();
        if (activeMS >= idx) {
//            this.plot.getRenderer().setBaseItemLabelsVisible(!hideAnnotations.isSelected());
            this.plot.getRenderer().setSeriesItemLabelsVisible(idx,
                    !hideAnnotations.isSelected());
            hideAnnotations.setSelected(!this.plot.getRenderer().
                    isSeriesItemLabelsVisible(idx));
        }
    }//GEN-LAST:event_hideAnnotationsActionPerformed

    private void activeMassSpectrumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeMassSpectrumActionPerformed
        int key = activeMassSpectrum.getSelectedIndex();
        activeMS = key;
        //addTopKLabels(topK, key);
        activeMassSpectrum.setSelectedIndex(key);
        hideAnnotations.setSelected(!this.plot.getRenderer().
                isSeriesItemLabelsVisible(key));
    }//GEN-LAST:event_activeMassSpectrumActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        int key = activeMassSpectrum.getSelectedIndex();
        if (!seriesToScan.isEmpty()) {
            seriesToScan.remove(this.sc.getSeries(key));
            this.sc.removeSeries(key);
            updateActiveMassSpectrum();
        }
    }//GEN-LAST:event_removeActionPerformed

    private void identifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_identifyActionPerformed
        System.out.println("Identifying");
        if (seriesToScan.isEmpty()) {
            NotifyDescriptor nd = new NotifyDescriptor.Message(
                    "Please select at least one mass spectrum!");
            DialogDisplayer.getDefault().notify(nd);
        } else {
            addIdentification();
        }
    }//GEN-LAST:event_identifyActionPerformed

    private void absoluteRelativeToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_absoluteRelativeToggleActionPerformed
//        this.defaultNumberFormat.setRelativeMode(absoluteRelativeToggle.isSelected());
        for (MSSeries mss : seriesToScan.keySet()) {
            mss.setNormalize(absoluteRelativeToggle.isSelected());
        }
        this.cp.chartChanged(new AxisChangeEvent(this.plot.getRangeAxis()));
    }//GEN-LAST:event_absoluteRelativeToggleActionPerformed

    private void addSeriesTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSeriesTopActionPerformed
        this.addSeriesToTopPlot = this.addSeriesTop.isSelected();
    }//GEN-LAST:event_addSeriesTopActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton absoluteRelativeToggle;
    private javax.swing.JComboBox activeMassSpectrum;
    private javax.swing.JToggleButton addMs;
    private javax.swing.JToggleButton addSeriesTop;
    private javax.swing.JButton clear;
    private javax.swing.JToggleButton fixXAxis;
    private javax.swing.JToggleButton fixYAxis;
    private javax.swing.JToggleButton hideAnnotations;
    private javax.swing.JButton identify;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton remove;
    // End of variables declaration//GEN-END:variables

    @Override
    public void resultChanged(LookupEvent ev) {
        System.out.println("MassSpectrumPanel received LookupEvent");
        Collection<? extends ISelection> coll = lookupResult.allInstances();
        if (coll.size() > 0) {
            System.out.println("Received " + coll.size() + " ISelection objects from lookup");
//            System.out.println("Found " + contentProviderLookup.lookupAll(IChromatogram.class).size() + " chromatograms in lookup!");
//            IChromatogram ichromDescr = contentProviderLookup.lookup(IChromatogram.class);
            boolean add = addMs.isSelected();
            if (coll.size() > 1) {
                add = true;
            }
//            if (ichromDescr != null) {
            for (ISelection scan : coll) {
                IScan target = null;
                if (scan.getTarget() instanceof IScan) {
                    System.out.println("Target is IScan");
                    target = (IScan) scan.getTarget();
                }
                IChromatogram source = null;
                if (scan.getSource() instanceof IChromatogram) {
                    System.out.println("Source if IChromatogram");
                    source = (IChromatogram) scan.getSource();
                }
                if (source != null && target != null) {
                    System.out.println("Source and target are not null!");
                    String name = source.getParent().getName();
                    setData(target, name, add);
                }
            }
//            }
        }
        if (!metaboliteSelection.allInstances().isEmpty()) {
            System.out.println("Received MetaboliteProxy from lookup");
            boolean add = addMs.isSelected();
            if (metaboliteSelection.allInstances().size() > 1) {
                add = true;
            }
            for (IMetabolite metabolite : metaboliteSelection.allInstances()) {
                final IScan scan = new Scan1D(metabolite.getMassSpectrum().getFirst(), metabolite.getMassSpectrum().getSecond(), metabolite.getScanIndex(), metabolite.getRetentionTime());
                setData(scan, metabolite.getID(), add);
            }
        }
        if (!peakAnnotationDescriptorResult.allInstances().isEmpty()) {
            System.out.println("Received peak annotations from lookup");
            boolean add = addMs.isSelected();
            if (peakAnnotationDescriptorResult.allInstances().size() > 1) {
                add = true;
            }
            for (IPeakAnnotationDescriptor ipad : peakAnnotationDescriptorResult.allInstances()) {
                final IScan scan = new Scan1D(Array.factory(ipad.getMassValues()), Array.factory(ipad.getIntensityValues()), ipad.getIndex(), ipad.getApexTime());
                setData(scan, ipad.getDisplayName(), add);
            }
        }
//        if (!scan2DSelection.allInstances().isEmpty()) {
//            System.out.println("Received scan 2d from lookup");
//            IChromatogram ichromDescr = contentProviderLookup.lookup(IChromatogram.class);
//            if (ichromDescr != null) {
//                boolean add = addMs.isSelected();
//                if (scan2DSelection.allInstances().size() > 1) {
//                    add = true;
//                }
//                for (IScan2D scan : scan2DSelection.allInstances()) {
//                    String name = ichromDescr.getParent().getName();
//                    setData(scan, name, add);
//                }
//            }
//        }
    }

    protected void setData(final IScan scan, final String name, final boolean add) {
        Runnable s = new Runnable() {
            @Override
            public void run() {
                System.out.println("Change ms called in MassSpectrumPanel");
                if (scan instanceof IScan2D) {
                    MSSeries s = ChromatogramVisualizerTools.getMSSeries2D(
                            (IScan2D) scan, name, addSeriesToTopPlot);
                    s.setNormalize(absoluteRelativeToggle.isSelected());
                    if (add) {
                        try {
                            sc.getSeries(s.getKey());
                        } catch (Exception e) {
                            sc.addSeries(s);
                            seriesToScan.put(s, scan);
                        }

                    } else {
                        seriesToScan.clear();
                        sc = new XYSeriesCollection();
                        sc.addSeries(s);
                        seriesToScan.put(s, scan);
                        //cp.repaint();
//                            XYPlot plot = (XYPlot) cp.getChart().getPlot();
                        plot.setDataset(sc);
                    }
                    updateActiveMassSpectrum();
                    activeMS = seriesToScan.size() - 1;
                    addTopKLabels(topK, activeMS);
                    ChartCustomizer.setSeriesColors(plot, 0.95f);
                    ChartCustomizer.setSeriesStrokes(plot, 2.0f);
                } else if (scan instanceof IScan1D) {
                    MSSeries s = ChromatogramVisualizerTools.getMSSeries1D(
                            (IScan1D) scan, name, addSeriesToTopPlot);
                    //s.setKey(s.getKey());
                    s.setNormalize(absoluteRelativeToggle.isSelected());
                    if (add) {
                        try {
                            sc.getSeries(s.getKey());
                        } catch (Exception e) {
                            sc.addSeries(s);
                            seriesToScan.put(s, scan);
                        }

                    } else {
                        seriesToScan.clear();
                        sc = new XYSeriesCollection();
                        sc.addSeries(s);
                        seriesToScan.put(s, scan);
                        //cp.repaint();
//                            XYPlot plot = (XYPlot) cp.getChart().getPlot();
                        plot.setDataset(sc);
                        sc.addChangeListener(plot);
                    }
                    updateActiveMassSpectrum();
                    activeMS = seriesToScan.size() - 1;
                    addTopKLabels(topK, activeMS);
                    ChartCustomizer.setSeriesColors(plot, 0.95f);
                    ChartCustomizer.setSeriesStrokes(plot, 2.0f);
                }
            }
        };
        es.submit(s);
    }

    public void componentOpened() {
        lookupResult = contentProviderLookup.lookupResult(ISelection.class);
        lookupResult.addLookupListener(this);
        metaboliteSelection = contentProviderLookup.lookupResult(IMetabolite.class);
        metaboliteSelection.addLookupListener(this);
        peakAnnotationDescriptorResult = contentProviderLookup.lookupResult(IPeakAnnotationDescriptor.class);
        peakAnnotationDescriptorResult.addLookupListener(this);
    }

    public void componentClosed() {
        if (lookupResult != null) {
            lookupResult.removeLookupListener(this);
        }
        if (metaboliteSelection != null) {
            metaboliteSelection.removeLookupListener(this);
        }
        if (peakAnnotationDescriptorResult != null) {
            peakAnnotationDescriptorResult.removeLookupListener(this);
        }
    }
}
