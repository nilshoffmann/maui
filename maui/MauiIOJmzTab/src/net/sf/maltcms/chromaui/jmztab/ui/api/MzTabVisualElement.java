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
package net.sf.maltcms.chromaui.jmztab.ui.api;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit;
import net.sf.maltcms.chromaui.jmztab.ui.util.MzTabFileToModelBuilder;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabFileContainer;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;

/**
 *
 * @author Nils Hoffmann
 */
@MultiViewElement.Registration(
        displayName = "#LBL_MzTab_VISUAL",
        iconBase = "net/sf/maltcms/chromaui/jmztab/ui/api/MzTab.png",
        mimeType = "text/mztab",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "MzTabVisual",
        position = 2000
)
@Messages("LBL_MzTab_VISUAL=Visual")
public final class MzTabVisualElement extends TopComponent implements MultiViewElement,
        ExplorerManager.Provider, Lookup.Provider {

    private ExplorerManager manager = new ExplorerManager();
    private OutlineView view = null;
    private MzTabDataObject obj;
    private JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;

    /**
     *
     * @param lkp
     */
    public MzTabVisualElement(Lookup lkp) {
        obj = lkp.lookup(MzTabDataObject.class);
        assert obj != null;
        initComponents();
        view = new OutlineView();
        view.setQuickSearchAllowed(true);
        view.setTreeSortable(true);
//        view.setPropertyColumns("message", "Message");
        view.getOutline().setRootVisible(true);
        add(view, BorderLayout.CENTER);
        ActionMap map = this.getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
        associateLookup(ExplorerUtils.createLookup(manager, map));
        Logger.getLogger(getClass().getName()).info("Setting root context");
        manager.setRootContext(getRootNode(obj));
    }
    
    private Node getRootNode(MzTabDataObject obj) {
        try {
            final MZTabFileParser parser = new MZTabFileParser(FileUtil.toFile(obj.getPrimaryFile()), System.out);
            MZTabFile file = parser.getMZTabFile();
            if (parser.getErrorList().isEmpty()) {
                MzTabFileToModelBuilder mfmb = new MzTabFileToModelBuilder();
                MzTabFileContainer container = mfmb.createFromFile(file);
                Children c = Lookup.getDefault().lookup(INodeFactory.class).createContainerChildren(container, Lookup.EMPTY);
                MzTabDataNode dataNode = new MzTabDataNode(obj, c);
                return dataNode;
            } else {
                //TODO change to display parsing errors in IDE Log directly
                Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Errors encountered while parsing file: {0}", obj.getPrimaryFile().getPath());
                for (int i = 0; i < parser.getErrorList().size(); i++) {
                    Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Error {0}:{1}", new Object[]{i, parser.getErrorList().getError(i)});
                }
                MzTabDataNode mzt = new MzTabDataNode(obj, Children.LEAF);
                mzt.setDisplayName("Error parsing file " + obj.getPrimaryFile().getNameExt());
                FilterNode fn = new FilterNode(mzt) {

                    @Override
                    public Image getIcon(int type) {
                        return ImageUtilities.createDisabledImage(super.getIcon(type));
                    }

                };
                return fn;
            }
        } catch (IOException ex) {
            Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Failed to parse file: " + obj.getPrimaryFile().getPath(), ex);
            return Node.EMPTY;
        }
    }

    @Override
    public String getName() {
        return "MzTabVisualElement";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @return
     */
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    /**
     *
     * @return
     */
    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    /**
     *
     */
    @Override
    public void componentOpened() {
    }

    /**
     *
     */
    @Override
    public void componentClosed() {
    }

    /**
     *
     */
    @Override
    public void componentShowing() {
    }

    /**
     *
     */
    @Override
    public void componentHidden() {
    }

    /**
     *
     */
    @Override
    public void componentActivated() {
    }

    /**
     *
     */
    @Override
    public void componentDeactivated() {
    }

    /**
     *
     * @return
     */
    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    /**
     *
     * @param callback
     */
    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    /**
     *
     * @return
     */
    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

}
