/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.exception.ResourceNotAvailableException;
import java.awt.BorderLayout;
import java.beans.IntrospectionException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
//import org.openide.util.ImageUtilities;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 * TODO: convert to use ExplorerManager and BeanTreeView
 */
public final class CDFViewTopComponent extends JComponent implements ExplorerManager.Provider, NavigatorPanel, LookupListener {

    //private String file;
    private transient ExplorerManager em = new ExplorerManager();
    private transient BeanTreeView beanTreeView;// = new TreeTableView();
    private Lookup.Result<IFileFragmentDataObject> result = null;
    private Lookup lookup = null;

    public CDFViewTopComponent() {
        initComponents();
        this.beanTreeView = new BeanTreeView();
        this.beanTreeView.setRootVisible(false);
        add(this.beanTreeView, BorderLayout.CENTER);
        this.lookup = ExplorerUtils.createLookup(this.em, getActionMap());
        setName(NbBundle.getMessage(CDFViewTopComponent.class, "CTL_CDFViewTopComponent"));
        setToolTipText(NbBundle.getMessage(CDFViewTopComponent.class, "HINT_CDFViewTopComponent"));
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return this.em;
    }

    @Override
    public void resultChanged(LookupEvent le) {
        System.out.println("Received resultChanged!");
        Collection<? extends IFileFragmentDataObject> files = result.allInstances();
        updateView(files);
    }

    protected void updateView(Collection<? extends IFileFragmentDataObject> files) {
        if (!files.isEmpty()) {
            HashSet<IFileFragmentDataObject> hs = new LinkedHashSet<IFileFragmentDataObject>(files);
            List<Node> l = new ArrayList<Node>();
            for (IFileFragmentDataObject f : hs) {
                System.out.println("File: " + f.getFragment());

                Node n;
                try {
                    n = new FilterNode(DataObject.find(FileUtil.toFileObject(new File(f.getFragment().getAbsolutePath()))).getNodeDelegate(), Children.create(new DataFileFactory(f.getFragment()), true));
                    l.add(n);
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
            System.out.println("Nodes: "+l);
            Children.Array ca = new Children.Array();//new FilterNode.Children.Array();
            
            ca.add(l.toArray(new Node[l.size()]));
            System.out.println(ca);
            this.em.setRootContext(new AbstractNode(ca));
            //this.beanTreeView.setRootVisible(false);
        }else{
            this.em.setRootContext(Node.EMPTY);
        }
    }

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(CDFViewTopComponent.class, "CTL_CDFViewTopComponent");
    }

    @Override
    public String getDisplayHint() {
        return NbBundle.getMessage(CDFViewTopComponent.class, "HINT_CDFViewTopComponent");
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void panelActivated(Lookup lkp) {
        System.out.println("panelActivated");
        result = lkp.lookupResult(IFileFragmentDataObject.class);
        result.addLookupListener(this);
        // get actual data and recompute content
        if (result != null && !result.allInstances().isEmpty()) {
            updateView(result.allInstances());
            ExplorerUtils.activateActions(em, true);
        }
    }

    @Override
    public void panelDeactivated() {
        ExplorerUtils.activateActions(em, false);
        result.removeLookupListener(this);
        result = null;
    }

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
                    return true;
                } else {
                    if (ivf.getName().equals("source_files")) {
//                        Collection<IFileFragment> sourceFiles = ff.getSourceFiles();
                    } else {
                        System.out.println("Adding variable: " + ivf);
                        list.add(ivf);
                    }
                }
            }
            try {
                for (IFileFragment sourceFile : cross.datastructures.tools.FragmentTools.getSourceFiles(ff)) {
                    if (Thread.interrupted()) {
                        return true;
                    } else {
                        System.out.println("Adding source file: " + sourceFile);
                        list.add(sourceFile);
                    }
                }
            } catch (ResourceNotAvailableException rnae) {
            }
            return true;
        }

        @Override
        protected Node[] createNodesForKey(IFragment key) {
            if (key instanceof IVariableFragment) {
                try {
                    System.out.println("Creating VariableFragment node: " + key);
                    return new Node[]{new BeanNode((IVariableFragment) key)};
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return new Node[]{};
            } else if (key instanceof IFileFragment) {
                try {
                    //                try{
                    System.out.println("Creating FileFragment node: " + key);
                    FileObject fo = FileUtil.toFileObject(new File(((IFileFragment) key).getAbsolutePath()));
                    return new Node[]{new FilterNode(DataObject.find(fo).getNodeDelegate(), Children.create(new DataFileFactory((IFileFragment) key), true))};
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
    protected void componentActivated() {
        ExplorerUtils.activateActions(em, true);
    }
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(em, false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
