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
package net.sf.maltcms.chromaui.jmztab.ui.validation;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit;
import net.sf.maltcms.chromaui.jmztab.ui.api.MzTabDataObject;
import net.sf.maltcms.chromaui.jmztab.ui.validation.annotations.ErrorAnnotation;
import net.sf.maltcms.chromaui.jmztab.ui.validation.annotations.WarningAnnotation;
import net.sf.maltcms.chromaui.jmztab.ui.validation.nodes.ErrorNodeChildFactory;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.io.output.WriterOutputStream;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.editor.AnnotationType;
import org.netbeans.editor.AnnotationTypes;
import org.openide.awt.UndoRedo;
import org.openide.cookies.LineCookie;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.text.Annotation;
import org.openide.text.Line;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.TopComponent;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabError;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorType.Level;

@MultiViewElement.Registration(
        displayName = "#LBL_MzTab_VALIDATION",
        iconBase = "net/sf/maltcms/chromaui/jmztab/ui/api/MzTab.png",
        mimeType = "text/mztab",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "MzTabValidation",
        position = 2000
)
@Messages("LBL_MzTab_VALIDATION=Validation")
public final class MzTabValidationElement extends TopComponent implements MultiViewElement, ExplorerManager.Provider, Lookup.Provider {

    private MzTabDataObject obj;
    private final JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;
    private DefaultComboBoxModel<Level> dcbm;
    private OutlineView view = null;
    private transient ExplorerManager manager = new ExplorerManager();
    private AtomicBoolean validationInProgress = new AtomicBoolean();

    public MzTabValidationElement(Lookup lkp) {
        obj = lkp.lookup(MzTabDataObject.class);
        assert obj != null;
        initComponents();
        view = new OutlineView();
        view.setQuickSearchAllowed(true);
        view.setTreeSortable(true);
        view.setPropertyColumns("message", "Message");
        view.getOutline().setRootVisible(false);
        add(view, BorderLayout.CENTER);
        ActionMap map = this.getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
        associateLookup(ExplorerUtils.createLookup(manager, map));
    }

    @Override
    public String getName() {
        return "MzTabVisualElement";
    }

    private ComboBoxModel getErrorLevelModel() {
        if (dcbm == null) {
            dcbm = new DefaultComboBoxModel<>(Level.values());
        }
        return dcbm;
    }

    private Level getSelectedErrorLevel() {
        return (Level) jComboBox1.getSelectedItem();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        validate = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel1 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jComboBox1 = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(validate, org.openide.util.NbBundle.getMessage(MzTabValidationElement.class, "MzTabValidationElement.validate.text")); // NOI18N
        validate.setFocusable(false);
        validate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        validate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        validate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateActionPerformed(evt);
            }
        });
        jToolBar1.add(validate);
        jToolBar1.add(jSeparator1);
        jToolBar1.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MzTabValidationElement.class, "MzTabValidationElement.jLabel1.text")); // NOI18N
        jToolBar1.add(jLabel1);
        jToolBar1.add(filler2);

        jComboBox1.setModel(getErrorLevelModel());
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBox1);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void validateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validateActionPerformed
        if (validationInProgress.compareAndSet(false, true)) {
            ValidationRunnable vr = new ValidationRunnable();
            ValidationRunnable.createAndRun("Validating mzTab file...", vr);
        }
    }//GEN-LAST:event_validateActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton validate;
    // End of variables declaration//GEN-END:variables
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    @Override
    public void componentActivated() {
        ExplorerUtils.activateActions(manager, true);
        view.requestFocusInWindow();
    }

    @Override
    public void componentDeactivated() {
        ExplorerUtils.activateActions(manager, false);
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    private final class ValidationRunnable extends AProgressAwareRunnable {

        @Override
        public void run() {
            try {
                getProgressHandle().setDisplayName("Validating mzTab file...");
                getProgressHandle().start();
                getProgressHandle().progress("Parsing...");

                final MZTabFileParser parser;
                try {
                    InputOutput io = IOProvider.getDefault().getIO("MzTab Validation", false);
                    io.getOut().reset();
                    try (WriterOutputStream wos = new WriterOutputStream(io.getOut())) {
                        parser = new MZTabFileParser(FileUtil.toFile(obj.getPrimaryFile()), wos, getSelectedErrorLevel(), 1000);
                        getProgressHandle().progress("Retrieving error list...");
                        MZTabErrorList mztel = parser.getErrorList();
                        //FIXME currently deactivated due to unresolved NullPointerException
//                        DataObject objWithError = DataObject.find(obj.getPrimaryFile());
//                        LineCookie cookie = (LineCookie) objWithError.getLookup().lookup(LineCookie.class);
//                        Line.Set lineSet = cookie.getLineSet();
//                        for (int i = 0; i < mztel.size(); i++) {
//                            MZTabError error = mztel.getError(i);
//                            final Line line = lineSet.getOriginal(error.getLineNumber());
//                            if (line != null) {
//                                Annotation ann = null;
//                                switch (error.getType().getLevel()) {
//                                    case Error:
//                                        ann = new ErrorAnnotation(error.getMessage());
//                                        break;
//                                    case Warn:
//                                        ann = new WarningAnnotation(error.getMessage());
//                                        break;
//                                    case Info:
//                                        ann = new WarningAnnotation(error.getMessage());
//                                        break;
//                                    default:
//                                        throw new IllegalStateException("Unhandled enum value: " + error.getType().getLevel());
//                                }
//                                if (ann != null) {
//                                    try {
//                                        ann.attach(line);
//                                    } catch (NullPointerException npe) {
//
//                                    }
//                                }
//                            } else {
//                                Logger.getLogger(MzTabValidationElement.class.getName()).warning("Could not retrieve line number for error: " + error.toString());
//                            }
//                        }
                        AbstractNode rootNode = new AbstractNode(Children.create(new ErrorNodeChildFactory(mztel), true));
                        manager.setRootContext(rootNode);
                        getProgressHandle().progress("Setting results...");
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } finally {
                getProgressHandle().finish();
                validationInProgress.compareAndSet(true, false);
            }
        }

    };
}
