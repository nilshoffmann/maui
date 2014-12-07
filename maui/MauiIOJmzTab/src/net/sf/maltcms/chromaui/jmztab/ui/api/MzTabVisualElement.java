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

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

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
public final class MzTabVisualElement extends JPanel implements MultiViewElement {

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

        jTabbedPane1 = new javax.swing.JTabbedPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
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

}