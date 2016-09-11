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
package maltcms.ui.fileHandles.cdf;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.exception.ResourceNotAvailableException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import maltcms.ui.fileHandles.cdf.nodes.VariableFragmentNode;
import net.sf.maltcms.chromaui.lookupResultListener.api.AbstractLookupResultListener;
import org.apache.commons.io.FileUtils;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import ucar.ma2.Range;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;

/**
 * Top component which displays hierarchical content of a cdf file. 
 */
@NavigatorPanel.Registration(mimeType = "application/x-cdf", displayName = "#LBL_CDF_FILE_NAVIGATOR")
@NbBundle.Messages("LBL_CDF_FILE_NAVIGATOR=netCDF File Navigator")
public final class CDFViewTopComponent extends JComponent implements ExplorerManager.Provider, NavigatorPanel, LookupListener {

    //private String file;
    private transient ExplorerManager em = new ExplorerManager();
    private transient BeanTreeView beanTreeView;// = new TreeTableView();
    private Lookup.Result<IFileFragmentDataObject> result = null;
    private SelectionListener selectionListener;
    private Lookup lookup = null;

    /**
     *
     */
    public CDFViewTopComponent() {
        initComponents();
        this.beanTreeView = new BeanTreeView();
        this.beanTreeView.setRootVisible(false);
        add(this.beanTreeView, BorderLayout.CENTER);
        this.lookup = ExplorerUtils.createLookup(this.em, getActionMap());
        setName(NbBundle.getMessage(CDFViewTopComponent.class, "CTL_CDFViewTopComponent"));
        setToolTipText(NbBundle.getMessage(CDFViewTopComponent.class, "HINT_CDFViewTopComponent"));
        selectionListener = new SelectionListener(IVariableFragment.class, Utilities.actionsGlobalContext());
        selectionListener.register(Utilities.actionsGlobalContext());
    }

    /**
     *
     * @return
     */
    @Override
    public ExplorerManager getExplorerManager() {
        return this.em;
    }

    /**
     *
     * @param le
     */
    @Override
    public void resultChanged(LookupEvent le) {
        Logger.getLogger(getClass().getName()).info("Received resultChanged!");
        Collection<? extends IFileFragmentDataObject> files = result.allInstances();
        updateView(files);
    }

    private class SelectionListener extends AbstractLookupResultListener<IVariableFragment> {

        public SelectionListener(Class<? extends IVariableFragment> typeToListenFor) {
            super(typeToListenFor);
        }

        public SelectionListener(Class<? extends IVariableFragment> typeToListenFor, Lookup contentProviderLookup) {
            super(typeToListenFor, contentProviderLookup);
        }

        @Override
        public void resultChanged(LookupEvent le) {
            variableMetadata.setText("");
            for (IVariableFragment ivf : selectionListener.getResult().allInstances()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Name: ").append(ivf.getName()).append("\n");
                sb.append("Data Type: ").append(ivf.getDataType()).append("\n");
                sb.append("Dimensions:\n");
                Dimension[] dimensions = ivf.getDimensions();
                if (dimensions != null) {
                    for (Dimension dim : dimensions) {
                        sb.append(dim.toString()).append("\n");
                    }
                }
                if (dimensions == null || dimensions.length == 0) {
                    sb.append("\n");
                }

                Range[] ranges = ivf.getRange();
                sb.append("Ranges:\n");
                if (ranges != null) {
                    for (Range r : ranges) {
                        sb.append(r.toString()).append("\n");
                    }
                }
                if (ranges == null || ranges.length == 0) {
                    sb.append("\n");
                }
                sb.append("Attributes:\n");
                List<Attribute> attributes = ivf.getAttributes();
                for (Attribute a : attributes) {
                    sb.append(a.toString()).append("\n");
                }
                if (attributes.isEmpty()) {
                    sb.append("\n");
                }
//				sb.append("Data:\n");
//				String dataString = ivf.getArray().toString();
//				for(int i = 0;i<(dataString.length()/80)+1;i++) {
//					sb.append(dataString.substring(i, Math.min(dataString.length(),i+80))).append("\n");
//				}
                variableMetadata.setText(sb.toString());
                return;
            }
        }
    }

    /**
     *
     * @param files
     */
    protected void updateView(Collection<? extends IFileFragmentDataObject> files) {
        if (!files.isEmpty()) {
            HashSet<IFileFragmentDataObject> hs = new LinkedHashSet<>(files);
            List<Node> l = new ArrayList<>();
            for (IFileFragmentDataObject f : hs) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "File: {0}", f.getFragment());

                Node n;
                try {
                    n = new FilterNode(DataObject.find(FileUtil.toFileObject(new File(f.getFragment().getAbsolutePath()))).getNodeDelegate(), Children.create(new DataFileFactory(f.getFragment()), true));
                    l.add(n);
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
            Children.Array ca = new Children.Array();
            ca.add(l.toArray(new Node[l.size()]));
            this.em.setRootContext(new AbstractNode(ca));
        } else {
            this.em.setRootContext(Node.EMPTY);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(CDFViewTopComponent.class, "CTL_CDFViewTopComponent");
    }

    /**
     *
     * @return
     */
    @Override
    public String getDisplayHint() {
        return NbBundle.getMessage(CDFViewTopComponent.class, "HINT_CDFViewTopComponent");
    }

    /**
     *
     * @return
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     *
     * @param lkp
     */
    @Override
    public void panelActivated(Lookup lkp) {
        Logger.getLogger(getClass().getName()).info("panelActivated");
//        lookup = lkp;
        result = lkp.lookupResult(IFileFragmentDataObject.class);
        result.addLookupListener(this);
        // get actual data and recompute content
        if (result != null && !result.allInstances().isEmpty()) {
            updateView(result.allInstances());
            ExplorerUtils.activateActions(em, true);
        }
    }

    /**
     *
     */
    @Override
    public void panelDeactivated() {
        ExplorerUtils.activateActions(em, false);
        updateView(new ArrayList<IFileFragmentDataObject>(0));
        result.removeLookupListener(this);
        result = null;
    }

    /**
     *
     * @param cdo
     */
    public void setFile(CDFDataObject cdo) {
        updateView(Arrays.asList(cdo));
    }

    class DataFileFactory extends ChildFactory<IFragment> {

        private final IFileFragment ff;

        public DataFileFactory(IFileFragment ff) {
            this.ff = ff;
        }

        @Override
        protected boolean createKeys(List<IFragment> list) {
            List<IVariableFragment> l = FragmentTools.getChildren(ff);
            for (IVariableFragment ivf : l) {
                if (Thread.interrupted()) {
                    return false;
                } else {
                    if (ivf.getName().equals("source_files")) {
//                        Collection<IFileFragment> sourceFiles = ff.getSourceFiles();
                    } else {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding variable: {0}", ivf);
                        list.add(ivf);
                    }
                }
            }
            try {
                for (IFileFragment sourceFile : cross.datastructures.tools.FragmentTools.getSourceFiles(ff).values()) {
                    if (Thread.interrupted()) {
                        return false;
                    } else {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding source file: {0}", sourceFile);
                        list.add(sourceFile);
                    }
                }
            } catch (ResourceNotAvailableException rnae) {
                list.add(null);
            }
            return true;
        }

        @Override
        protected Node[] createNodesForKey(IFragment key) {
            if (key == null) {
                return new Node[]{Node.EMPTY};
            } else if (key instanceof IVariableFragment) {
                try {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Creating VariableFragment node: {0}", key);
                    return new Node[]{new VariableFragmentNode((IVariableFragment) key)};
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return new Node[]{};
            } else if (key instanceof IFileFragment) {
                try {
                    //                try{
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Creating FileFragment node: {0}", key);
                    File f = new File(((IFileFragment) key).getUri());
                    if (f.exists() && f.isFile()) {
                        FileObject fo = FileUtil.toFileObject(new File(((IFileFragment) key).getAbsolutePath()));
                        return new Node[]{new FilterNode(DataObject.find(fo).getNodeDelegate(), Children.create(new DataFileFactory((IFileFragment) key), true))};
                    } else {
                        FileNotFoundNode fnfn = new FileNotFoundNode(Children.LEAF, Lookups.fixed(ff, ((IFileFragment) key).getUri()));
                        fnfn.setDisplayName("Could not find source file.");
                        fnfn.setShortDescription("Could not find source file " + ((IFileFragment) key).getUri());
                        return new Node[]{fnfn};
                    }
                    //                }
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            return new Node[]{};
        }
    }

    // It is good idea to switch all listeners on and off when the
    // component is shown or hidden. In the case of TopComponent use:

    /**
     *
     */
        protected void componentActivated() {
        ExplorerUtils.activateActions(em, true);
    }

    /**
     *
     */
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(em, false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        previewPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        variableMetadata = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        previewPanel.setLayout(new java.awt.BorderLayout());

        variableMetadata.setColumns(20);
        variableMetadata.setRows(5);
        jScrollPane1.setViewportView(variableMetadata);

        previewPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(previewPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JTextArea variableMetadata;
    // End of variables declaration//GEN-END:variables

    private class FileNotFoundNode extends AbstractNode {

        public FileNotFoundNode(Children children) {
            super(children);
        }

        public FileNotFoundNode(Children children, Lookup lookup) {
            super(children, lookup);
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[]{new AbstractAction("Locate missing source file") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    IFileFragment child = lookup.lookup(IFileFragment.class);
                    URI parentLocation = lookup.lookup(URI.class);
                    FileChooserBuilder fcb = new FileChooserBuilder(CDFViewTopComponent.class);
                    File f = fcb.showOpenDialog();
                    URI childLocation = child.getUri();
                    File childFile = new File(childLocation);
                    File tmpChildFile = new File(FileUtils.getTempDirectory(), childFile.getName());
                    //copy original file to tmp location
//                    FileUtils.copyFile(childFile, tmpChildFile);
                }
            }};
        }
    }

}
