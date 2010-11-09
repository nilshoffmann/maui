/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.exception.ResourceNotAvailableException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.windows.CloneableTopComponent;
import ucar.ma2.Array;
import ucar.nc2.Dimension;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//maltcms.ui.fileHandles.cdf//CDFView//EN",
autostore = false)
public final class CDFViewTopComponent extends CloneableTopComponent implements MouseListener {

    private static CDFViewTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "CDFViewTopComponent";
    private String file;
    //private ViewerPanel dv;
//    private PreferencesExt prefs;
//    private XMLStore store;

    public CDFViewTopComponent() {
        initComponents();
        this.jTable1.addMouseListener((MouseListener)this);
        setName(NbBundle.getMessage(CDFViewTopComponent.class, "CTL_CDFViewTopComponent"));
        setToolTipText(NbBundle.getMessage(CDFViewTopComponent.class, "HINT_CDFViewTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
//        try {
//            String prefStore = ucar.util.prefs.XMLStore.makeStandardFilename(".unidata", "NetcdfUI22.xml");
//            store = ucar.util.prefs.XMLStore.createFromFile(prefStore, null);
//            prefs = store.getPreferences();
//            //Debug.setStore(prefs.node("Debug"));
//        } catch (IOException e) {
//            System.out.println("XMLStore Creation failed " + e);
//        }
//        this.dv = new ViewerPanel((PreferencesExt) prefs.node("varTable"));
//        add(this.dv);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        final int idx = this.jTable1.getSelectedRows()[0];
        if (idx >= 0) {
            if (me.getButton() == MouseEvent.BUTTON3) {
                JPopupMenu jpm = new JPopupMenu("Cell actions");
                AbstractAction showVariableContent = new AbstractAction("Show Variable Content") {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        Object o = jTable1.getValueAt(idx, 1);
                        if (o instanceof String) {
                            String s = (String) o;
                            JTextArea jta = new JTextArea();
                            IFileFragment iff = new FileFragment(new File(file));
                            IVariableFragment ivf = iff.getChild(s);
                            StringBuilder sb = new StringBuilder();
                            if (ivf.getName().equals("mass_values") || ivf.getName().equals("intensity_values") || ivf.getName().equals("binned_mass_values") || ivf.getName().equals("binned_intensity_values")) {
                                if (ivf.getName().startsWith("binned")) {
                                    appendIndexedArrayData(iff, ivf, "binned_scan_index", sb, s);
                                } else {
                                    appendIndexedArrayData(iff, ivf, "scan_index", sb, s);
                                }
                            } else {
                                appendArrayData(ivf, sb, iff, s);
                            }
                            jta.setText(sb.toString());
                            DialogDescriptor nd = new DialogDescriptor(jta, "Variable content", true, null);
                            DialogDisplayer.getDefault().notify(nd);
                        }
                    }

                    private void appendArrayData(IVariableFragment ivf, StringBuilder sb, IFileFragment iff, String s) {
                        Array a = ivf.getArray();
                        String as = a.toString();
                        sb.append("Parent file:");
                        String filename = iff.getAbsolutePath();
                        for (int i = 0; i < filename.length(); i++) {
                            if (i % 80 == 0) {
                                sb.append("\n");
                            }
                            sb.append(filename.charAt(i));
                        }
                        sb.append("\n");
                        sb.append("Variable name: " + s + "\n");
                        if (ivf.getDimensions() == null || ivf.getDimensions().length == 0) {
                            sb.append("Shape: " + Arrays.toString(a.getShape()) + "\n");
                        } else {
                            sb.append("Dimensions: " + Arrays.toString(ivf.getDimensions()) + "\n");
                        }
                        for (int i = 0; i < as.length(); i++) {
                            if (i % 80 == 0) {
                                sb.append("\n");
                            }
                            sb.append(as.charAt(i));
                        }
                    }

                    private void appendIndexedArrayData(IFileFragment iff, IVariableFragment ivf, String indexVar, StringBuilder sb, String s) {
                        IVariableFragment scanIndexVar = null;
                        try {
                            scanIndexVar = iff.getChild(indexVar);
                            ivf.setIndex(scanIndexVar);
                            List<Array> ivScans = ivf.getIndexedArray();
                            sb.append("Parent file:");
                            String filename = iff.getAbsolutePath();
                            for (int i = 0; i < filename.length(); i++) {
                                if (i % 80 == 0) {
                                    sb.append("\n");
                                }
                                sb.append(filename.charAt(i));
                            }
                            sb.append("\n");
                            sb.append("Variable name: " + s + "\n");
                            if (ivf.getDimensions() == null || ivf.getDimensions().length == 0) {
                                sb.append("Shape: " + Arrays.toString(ivf.getArray().getShape()) + "\n");
                            } else {
                                sb.append("Dimensions: " + Arrays.toString(ivf.getDimensions()) + "\n");
                            }
                            sb.append("Indexed by: "+indexVar+"\n");
                            for (int j = 0; j < ivScans.size(); j++) {
                                if (j > 0) {
                                    sb.append("\n");
                                }
                                sb.append("Scan " + (j + 1) + "/" + ivScans.size());
                                String as = ivScans.get(j).toString();
                                for (int i = 0; i < as.length(); i++) {
                                    if (i % 80 == 0) {
                                        sb.append("\n");
                                    }
                                    sb.append(as.charAt(i));
                                }
                            }
                        } catch (ResourceNotAvailableException rnae) {
                        }
                    }
                };

                jpm.add(showVariableContent);
                jpm.show(this.jTable1, me.getX(), me.getY());


            }

        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    private class ViewerPanel extends JPanel {
//        DatasetViewer dsViewer;
//        JSplitPane split;
//        NetcdfFile ncfile = null;
//
//        void setFile(String file) {
//            try {
//                this.ncfile = NetcdfFile.open(file);
//                dsViewer.setDataset(this.ncfile);
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
//        ViewerPanel(PreferencesExt dbPrefs) {
//            dsViewer = new DatasetViewer(dbPrefs);
//            add(dsViewer, BorderLayout.CENTER);
//      AbstractButton infoButton = BAMutil.makeButtcon("Information", "Detail Info", false);
//      infoButton.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          if (ncfile != null) {
//            detailTA.setText(ncfile.getDetailInfo());
//            detailTA.gotoTop();
//            detailWindow.show();
//          }
//        }
//      });
//      buttPanel.add(infoButton);
//
//      AbstractAction dumpAction = new AbstractAction() {
//        public void actionPerformed(ActionEvent e) {
//          NetcdfFile ds = dsViewer.getDataset();
//          if (ds != null) {
//            if (ncdumpPanel == null) {
//              makeComponent(tabbedPane, "NCDump");
//            }
//            ncdumpPanel.setNetcdfFile(ds);
//            tabbedPane.setSelectedComponent(ncdumpPanel);
//          }
//        }
//      };
//      BAMutil.setActionProperties(dumpAction, "Dump", "NCDump", false, 'D', -1);
//      BAMutil.addActionToContainer(buttPanel, dumpAction);
//
//      AbstractAction netcdfAction = new AbstractAction() {
//        public void actionPerformed(ActionEvent e) {
//          String location = ncfile.getLocation();
//          if (location == null) location = "test";
//          int pos = location.lastIndexOf(".");
//          if (pos > 0)
//            location = location.substring(0, pos);
//
//          String filename = fileChooser.chooseFilenameToSave(location + ".nc");
//          if (filename == null) return;
//          doWriteNetCDF(filename);
//        }
//      };
//      BAMutil.setActionProperties(netcdfAction, "netcdf", "Write local netCDF file", false, 'S', -1);
//      BAMutil.addActionToContainer(buttPanel, netcdfAction);
//
//      AbstractButton compareButton = BAMutil.makeButtcon("Select", "Compare to another file", false);
//      compareButton.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          String filename = fileChooser.chooseFilename();
//          if (filename == null) return;
//
//          NetcdfFile compareFile = null;
//          try {
//            compareFile = openFile(filename, addCoords, null);
//
//            Formatter f = new Formatter();
//            CompareNetcdf cn = new CompareNetcdf(true, false, false);
//            cn.compare(ncfile, compareFile, f);
//
//            detailTA.setText(f.toString());
//            detailTA.gotoTop();
//            detailWindow.show();
//
//          } catch (Throwable ioe) {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream(10000);
//            ioe.printStackTrace(new PrintStream(bos));
//            detailTA.setText(bos.toString());
//            detailTA.gotoTop();
//            detailWindow.show();
//
//          } finally {
//            if (compareFile != null)
//              try {
//                compareFile.close();
//              }
//              catch (Exception eek) {
//              }
//          }
//        }
//      });
//      buttPanel.add(compareButton);
//
//      AbstractAction attAction = new AbstractAction() {
//        public void actionPerformed(ActionEvent e) {
//          dsViewer.showAtts();
//        }
//      };
//      BAMutil.setActionProperties(attAction, "FontDecr", "global attributes", false, 'A', -1);
//      BAMutil.addActionToContainer(buttPanel, attAction);

        /* AbstractAction syncAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
        NetcdfFile ds = dsViewer.getDataset();
        if (ds != null)
        try {
        ds.syncExtend();
        dsViewer.setDataset(ds);
        } catch (IOException e1) {
        e1.printStackTrace();
        }
        }
        };
        BAMutil.setActionProperties(syncAction, null, "SyncExtend", false, 'D', -1);
        BAMutil.addActionToContainer(buttPanel, syncAction); */
//        }
    }

    public void setFile(CDFDataObject cdo) {
        this.file = cdo.getPrimaryFile().getPath();
        System.out.println("File: " + this.file);


        this.jTable1.setAutoCreateRowSorter(true);


        this.jTable1.setAutoCreateColumnsFromModel(true);


        this.jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        this.jTable1.setModel(createTableModel(this.file));


    }

    private TableModel createTableModel(String file) {
        DefaultTableModel dtm = new DefaultTableModel();
        IFileFragment f = new FileFragment(new File(file));
        List<IVariableFragment> vars = FragmentTools.getAggregatedVariables(f);
        dtm.setColumnIdentifiers(new Object[]{"Parent", "Name", "Data Type", "Dimensions"});


        for (IVariableFragment ivf : vars) {
            System.out.println("Adding variable: " + ivf);
            Dimension[] dims = ivf.getDimensions();
            StringBuilder sb = new StringBuilder();


            for (int i = 0; i
                    < dims.length; i++) {
                sb.append(dims[i].getName() + ": " + dims[i].getLength() + " | ");


            }
            dtm.addRow(new Object[]{ivf.getParent().getAbsolutePath(), ivf.getName(), ivf.getDataType(), sb.substring(0, Math.max(0, sb.length() - 3))});


        }
        return dtm;


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttPanel = new javax.swing.JToolBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        buttPanel.setRollover(true);

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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(buttPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar buttPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized CDFViewTopComponent getDefault() {
        if (instance == null) {
            instance = new CDFViewTopComponent();


        }
        return instance;


    }

    /**
     * Obtain the CDFViewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized CDFViewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);




        if (win == null) {
            Logger.getLogger(CDFViewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");





            return getDefault();
        }


        if (win instanceof CDFViewTopComponent) {
            return (CDFViewTopComponent) win;




        }
        Logger.getLogger(CDFViewTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");





        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;


    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings


    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;


        }
        instance.readPropertiesImpl(p);


        return instance;


    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version


    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;

    }
}
