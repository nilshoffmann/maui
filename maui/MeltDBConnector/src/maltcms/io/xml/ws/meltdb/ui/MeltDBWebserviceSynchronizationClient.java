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
package maltcms.io.xml.ws.meltdb.ui;

import JavaServices.Web.MELT.Peak;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.ws.WebServiceClient;
import maltcms.io.xml.ws.meltdb.MeltDBSession;

/**
 *
 * @author nilshoffmann
 */
public class MeltDBWebserviceSynchronizationClient {

    private JPanel panel = null;
    private JPanel controlpanel = null;
    private JList projectsList = null;
    private JList expList = null;
    private JList chromList = null;
    private JList compList = null;
    private JList peaksList = null;
    private List<Peak> userPeaks = new ArrayList<>();
    private final MeltDBSession ms;
    private ExecutorService es = Executors.newCachedThreadPool();
    private int activePeakID = -1;

    /**
     *
     * @param ms
     */
    public MeltDBWebserviceSynchronizationClient(MeltDBSession ms) {
        this.ms = ms;
    }

    /**
     *
     * @return
     */
    public JPanel getControlPanel() {
        if (this.controlpanel == null) {
            this.controlpanel = new JPanel();
        }
        return this.controlpanel;
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        if (this.panel == null) {
            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
            this.panel.add(getControlPanel());
            JScrollPane plsp = new JScrollPane(getProjectsList());
            plsp.setBorder(BorderFactory.createTitledBorder("Projects"));
            this.panel.add(plsp);

            JScrollPane elsp = new JScrollPane(getExperimentsList());
            elsp.setBorder(BorderFactory.createTitledBorder("Experiments"));
            this.panel.add(elsp);
            JScrollPane clsp = new JScrollPane(getChromatogramsList());
            clsp.setBorder(BorderFactory.createTitledBorder("Chromatograms"));
            this.panel.add(clsp);
//			JScrollPane lcsp = new JScrollPane(getLabeledCompoundsList());
//			lcsp.setBorder(BorderFactory.createTitledBorder("Labeled Compounds"));
//			this.panel.add(lcsp);
            JScrollPane lcsp = new JScrollPane(getPeaksList());
            lcsp.setBorder(BorderFactory.createTitledBorder("Peaks"));
            this.panel.add(lcsp);
            JFrame jf = new JFrame();
            jf.add(addPeakPanel());
            jf.setVisible(true);
            jf.pack();
        }
        return this.panel;
    }

    /**
     *
     * @return
     */
    public JPanel addPeakPanel() {
        JPanel jp = new JPanel();
        //BoxLayout bl = new BoxLayout(jp,BoxLayout.Y_AXIS);
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        String[] labels = new String[]{"rt", "rt_min", "rt_max", "scan_index", "area", "intensity"};
        SortedMap<String, JTextArea> sm = new TreeMap<>();
        for (String s : labels) {
            JTextArea jta = getLabeledTextArea(s);
            jp.add(jta);
            //jp.add(Box.createVerticalGlue());
            sm.put(s, jta);
        }
        PeakCreateAction pca = new PeakCreateAction("Create Peak");
        pca.setMap(sm);
        JButton jb = new JButton(pca);
        jp.add(jb);

        SubmitPeaksAction spa = new SubmitPeaksAction("Submit Peaks");
        JButton jc = new JButton(spa);
        jp.add(jc);
        //rt, rt_min, rt_max, scan_index, area, intensity
        return jp;
    }

    /**
     *
     * @param cp
     */
    public void addUserPeak(Peak cp) {
        this.userPeaks.add(cp);
    }

    /**
     *
     * @return
     */
    public List<Peak> getUserPeaks() {
        return this.userPeaks;
    }

    /**
     *
     */
    public class SubmitPeaksAction extends AbstractAction {

        /**
         *
         */
        private static final long serialVersionUID = -2036573505164092880L;

        /**
         *
         */
        public SubmitPeaksAction() {
            super();
        }

        /**
         * @param name
         * @param icon
         */
        public SubmitPeaksAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * @param name
         */
        public SubmitPeaksAction(String name) {
            super(name);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean b = true;
            for (Peak cp : getUserPeaks()) {
                try {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Peak @{0}", cp.getRt());
                    int ids = ms.createPeak(ms.getActiveProject(), ms.getActiveChromatogram(), cp.getRt(), cp.getRt_min(), cp.getRt_max(), cp.getScan_index(), cp.getArea(), cp.getIntensity());
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Id: {0}", ids);
                    if (ids <= 0) {
                        b = false;
                        Logger.getLogger(getClass().getName()).warning("Problem occurred while adding userPeaks");
                    }
                } catch (RemoteException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            //all clear, userPeaks have been transmitted successfully
            if (b) {
                getUserPeaks().clear();
            }
        }
    }

    /**
     *
     */
    public class PeakCreateAction extends AbstractAction {

        /**
         *
         */
        private static final long serialVersionUID = -5584984611571026324L;
        private SortedMap<String, JTextArea> sm;
        private List<Peak> peaks = new ArrayList<>();
        private MeltDBSession ms;

        /**
         * @param name
         * @param icon
         */
        public PeakCreateAction(String name, Icon icon) {
            super(name, icon);
            // TODO Auto-generated constructor stub
        }

        /**
         * @param name
         */
        public PeakCreateAction(String name) {
            super(name);
            // TODO Auto-generated constructor stub
        }

        /**
         *
         */
        public PeakCreateAction() {
        }

        /**
         *
         * @param mdbs
         */
        public void setSession(MeltDBSession mdbs) {
            this.ms = mdbs;
        }

        /**
         *
         * @param sm
         */
        public void setMap(SortedMap<String, JTextArea> sm) {
            this.sm = sm;
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (this.sm != null) {
                String rt = this.sm.get("rt").getText();
                String rt_min = this.sm.get("rt_min").getText();
                String rt_max = this.sm.get("rt_max").getText();
                int scan_index = Integer.parseInt(this.sm.get("scan_index").getText());
                float area = Float.parseFloat(this.sm.get("area").getText());
                float intensity = Float.parseFloat(this.sm.get("intensity").getText());
                Logger.getLogger(getClass().getName()).info("Adding peak");
                Peak p = createPeak(-1, rt, rt_min, rt_max, scan_index, area, intensity);
                addUserPeak(p);
            }

        }
    }

    private static Peak createPeak(int id, String rt, String rt_min, String rt_max, int scan_index, float area, float intensity) {
        Peak p = new Peak();
        p.setId(id);
        p.setRt(rt);
        p.setRt_min(rt_min);
        p.setRt_max(rt_max);
        p.setScan_index(scan_index);
        p.setArea(area);
        p.setIntensity(intensity);
        return p;
    }

    private JTextArea getLabeledTextArea(String label) {
        JTextArea jta = new JTextArea();
        jta.setBorder(BorderFactory.createTitledBorder(label));
        return jta;
    }

    /**
     *
     * @return
     */
    public JList getLabeledCompoundsList() {
        if (this.compList == null) {
            this.compList = new JList(new String[]{});
            this.compList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionListener lsl = new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Selected: {0}", ((JList) e.getSource()).getModel().getElementAt(e.getFirstIndex()));
                }
            };
            this.compList.addListSelectionListener(lsl);
        }
        return this.compList;
    }

    /**
     *
     * @return
     */
    public JList getChromatogramsList() {
        if (this.chromList == null) {
            this.chromList = new JList(new String[]{});
            this.chromList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionListener lsl = new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    ms.setActiveChromatogram((String) ((JList) e.getSource()).getSelectedValue());
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Active Chromatogram is: {0}", ms.getActiveChromatogram());
                    disableComponent(
                            getLabeledCompoundsList());
                    Runnable fetch = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final List<String> s = ms.getLabeledCompoundsByChromatogram(ms.getActiveProject(), ms.getActiveChromatogram());
                                Runnable update = new Runnable() {

                                    @Override
                                    public void run() {

                                        if (s != null) {
                                            DefaultListModel dlm = new DefaultListModel();
                                            for (String str : s) {
                                                dlm.addElement(str);
                                            }
                                            getLabeledCompoundsList().setModel(dlm);
                                            enableComponent(
                                                    getLabeledCompoundsList());
                                        } else {
                                            disableComponent(getLabeledCompoundsList());
                                        }
                                    }
                                };
                                SwingUtilities.invokeLater(update);
                            } catch (RemoteException re) {
                                handleRemoteException(re);
                            }

                        }
                    };
                    es.submit(fetch);
                }
            };
            this.chromList.addListSelectionListener(lsl);
        }
        return this.chromList;
    }

    /**
     *
     * @return
     */
    public JList getPeaksList() {
        if (this.peaksList == null) {
            this.peaksList = new JList(new String[]{});
            this.peaksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            final AbstractAction pfa = new AbstractAction("Fetch peaks") {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    //ms.setActiveChromatogram((String)((JList)e.getSource()).getSelectedValue());
                    //System.out.println("Active Peak is: "+ms.getActivePeak());
                    disableComponent(getLabeledCompoundsList());
                    Runnable fetch = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Logger.getLogger(getClass().getName()).log(Level.INFO, "Retrieving peaks for project: {0} and chromatogram: {1}", new Object[]{ms.getActiveProject(), ms.getActiveChromatogram()});
                                final List<Integer> s = ms.getAvailablePeaks(ms.getActiveProject(), ms.getActiveChromatogram());
                                Logger.getLogger(getClass().getName()).log(Level.INFO, "Retrieved {0} peaks!", s.size());
                                Runnable update = new Runnable() {

                                    @Override
                                    public void run() {

                                        if (s != null) {
                                            DefaultListModel dlm = new DefaultListModel();

                                            List<Peak> peaks;
                                            try {
                                                peaks = ms.getPeaks(ms.getActiveProject(), ms.getActiveChromatogram(), s);
                                                for (Peak p : peaks) {
                                                    activePeakID = p.getId();
                                                    dlm.addElement("Peak at scan index: " + p.getScan_index() + " [ rt_min: " + p.getRt_min() + ", (rt_apex: " + p.getRt() + "), rt_max: " + p.getRt_max() + " area: " + p.getArea() + " apexIntensity: " + p.getIntensity());
                                                    //addUserPeak(p);
                                                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Retrieved peak with id {0} {1}", new Object[]{p.getId(), p.getRt()});
                                                }
                                            } catch (RemoteException ex) {
                                                Logger.getLogger(WebServiceClient.class.getName()).log(Level.SEVERE, null, ex);
                                            }
//											}
                                            getPeaksList().setModel(dlm);
//                                            getPeaksList().addMouseListener(new MouseAdapter() {
//
//                                                @Override
//                                                public void mouseClicked(MouseEvent e) {
//                                                    super.mouseClicked(e);
//
//
//
//
//                                                    if (e.getButton() == MouseEvent.BUTTON3) {
//                                                        JPopupMenu jpm = new JPopupMenu();
//                                                        AbstractAction fetchEICPeaks = new AbstractAction("Get EIC peaks") {
//
//                                                            /**
//                                                             *
//                                                             */
//                                                            private static final long serialVersionUID = 1L;
//
//                                                            @Override
//                                                            public void actionPerformed(ActionEvent e) {
//                                                                try {
//                                                                    List<EicPeak> eicPeaks = ms.getEICPeaks(ms.getActiveProject(), ms.getActiveChromatogram(), activePeakID);
//                                                                    if (eicPeaks == null) {
//                                                                        System.out.println("EIC peaks is null!");
//                                                                    } else {
//                                                                        for (EicPeak ep : eicPeaks) {
//                                                                            System.out.println(ep.getId() + " " + ep.getMz() + " " + ep.getMzMin() + " " + ep.getMzMax());
//                                                                        }
//                                                                    }
//                                                                } catch (RemoteException e1) {
//                                                                    // TODO Auto-generated catch block
//                                                                    e1.printStackTrace();
//                                                                }
//
//                                                            }
//                                                        };
//                                                        jpm.add(fetchEICPeaks);
//                                                        jpm.show(e.getComponent(), e.getX(), e.getY());
//                                                    }
//                                                }
//                                            });
                                            enableComponent(
                                                    getPeaksList());
                                        } else {
                                            disableComponent(getPeaksList());
                                        }
                                    }
                                };
                                SwingUtilities.invokeLater(update);
                            } catch (RemoteException re) {
                                handleRemoteException(re);
                            }

                        }
                    };
                    es.submit(fetch);
                }
            };
            this.chromList.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu jpm = new JPopupMenu();
                        jpm.add(pfa);
                        jpm.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
        }
        return this.peaksList;
    }

    /**
     *
     * @return
     */
    public JList getExperimentsList() {
        if (this.expList == null) {
            this.expList = new JList(new String[]{});
            this.expList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionListener lsl = new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    ms.setActiveExperiment((String) ((JList) e.getSource()).getSelectedValue());
                    if (ms.getActiveExperiment() == null || ms.getActiveExperiment().isEmpty()) {
                        Logger.getLogger(getClass().getName()).info("Could not complete request, selection for active experiment is empty!");
                        return;
                    }
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Active Experiment is: {0}", ms.getActiveExperiment());
                    disableComponent(
                            getChromatogramsList());
                    disableComponent(
                            getLabeledCompoundsList());
                    Runnable fetch = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final List<String> s = ms.getAvailableChromatogramsForExperiment(ms.getActiveProject(), ms.getActiveExperiment());
                                Runnable update = new Runnable() {

                                    @Override
                                    public void run() {

                                        if (s != null) {
                                            DefaultListModel dlm = new DefaultListModel();
                                            for (String str : s) {
                                                dlm.addElement(str);
                                            }
                                            getChromatogramsList().setModel(dlm);
                                            enableComponent(
                                                    getChromatogramsList());
                                        } else {
                                            disableComponent(getChromatogramsList());
                                        }
                                    }
                                };
                                SwingUtilities.invokeLater(update);
                            } catch (RemoteException re) {
                                handleRemoteException(re);
                            }

                        }
                    };
                    es.submit(fetch);
                }
            };
            this.expList.addListSelectionListener(lsl);
        }
        return this.expList;
    }

    private void handleRemoteException(RemoteException re) {
        Logger.getLogger(getClass().getName()).warning("RemoteException occurred while trying to access webservice!");
        Logger.getLogger(getClass().getName()).warning(re.getLocalizedMessage());
    }

    /**
     *
     * @return
     */
    public JList getProjectsList() {
        if (this.projectsList == null) {
            this.projectsList = new JList(new String[]{});
            controlpanel.add(new JButton(new AbstractAction("Get Projects") {

                /**
                 *
                 */
                private static final long serialVersionUID = 4181777796862959771L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final List<String> s = ms.getAvailableProjects();
                                Runnable update = new Runnable() {

                                    @Override
                                    public void run() {

                                        if (s != null) {
                                            DefaultListModel dlm = new DefaultListModel();
                                            for (String str : s) {
                                                dlm.addElement(str);
                                            }
                                            getProjectsList().setModel(dlm);
                                            enableComponent(
                                                    getProjectsList());
                                        } else {
                                            disableComponent(getProjectsList());
                                        }
                                    }
                                };
                                SwingUtilities.invokeLater(update);
                            } catch (RemoteException re) {
                                handleRemoteException(re);
                            }

                        }
                    };
                    es.submit(r);
                }
            }));
            this.projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionListener lsl = new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Selected index: {0}", e.getFirstIndex());
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Source event occurred on: {0}", e.getSource().getClass().getName());
                    final String project = (String) ((JList) e.getSource()).getSelectedValue();
                    ms.setActiveProject(project);
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Active project: {0}", ms.getActiveProject());
                    disableComponent(
                            getExperimentsList());
                    disableComponent(
                            getChromatogramsList());
                    disableComponent(
                            getLabeledCompoundsList());
                    Runnable fetch = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Logger.getLogger(getClass().getName()).log(Level.INFO, "Active project: {0}", ms.getActiveProject());
                                final List<String> s = ms.getAvailableExperiments(ms.getActiveProject());
                                Runnable update = new Runnable() {

                                    @Override
                                    public void run() {

                                        if (s != null) {
                                            DefaultListModel dlm = new DefaultListModel();
                                            for (String str : s) {
                                                dlm.addElement(str);
                                            }
                                            getExperimentsList().setModel(dlm);
                                            enableComponent(
                                                    getExperimentsList());
                                        } else {
                                            disableComponent(getExperimentsList());
                                        }
                                    }
                                };
                                SwingUtilities.invokeLater(update);
                            } catch (RemoteException re) {
                                handleRemoteException(re);
                            }

                        }
                    };
                    es.submit(fetch);
                }
            };
            this.projectsList.addListSelectionListener(lsl);
        }

        return this.projectsList;
    }

    private void disableComponent(final JComponent jc) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                jc.setEnabled(false);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    private void enableComponent(final JComponent jc) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                jc.setEnabled(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
