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
package net.sf.maltcms.chromaui.project.spi.wizard;

import java.awt.BorderLayout;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;
import net.sf.maltcms.chromaui.project.spi.descriptors.NormalizationDescriptor;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

public class DBProjectVisualPanel3 extends JPanel implements IWizardValidatable,
        ExplorerManager.Provider, Lookup.Provider, PropertyChangeListener {

    public static final String PROP_FILE_TO_NORMALIZATION = "fileToNormalization";
    private Set<FileToNormalizationDescriptor> fileToNormalization = new LinkedHashSet<>();
    private ExplorerManager manager = new ExplorerManager();
    private InstanceContent content = null;
    private Lookup lookup = null;
    private OutlineView view = null;

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        firePropertyChange("VALIDATE", null, this);
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        ExplorerUtils.activateActions(manager, true);
    }

    @Override
    public void removeNotify() {
        ExplorerUtils.activateActions(manager, false);
        super.removeNotify();
    }

    public class FileRootNode extends AbstractNode {

        public FileRootNode(Children children, Lookup lookup) {
            super(children, lookup);
        }

        public FileRootNode(Children children) {
            super(children);
        }
    }

    public class FileNormalizationChildFactory extends ChildFactory<FileToNormalizationDescriptor> {

        private Set<FileToNormalizationDescriptor> descriptors;

        public FileNormalizationChildFactory(
                Set<FileToNormalizationDescriptor> descriptors) {
            this.descriptors = descriptors;
        }

        @Override
        protected boolean createKeys(
                List<FileToNormalizationDescriptor> toPopulate) {
            for (FileToNormalizationDescriptor ftnd : descriptors) {
                if (Thread.interrupted()) {
                    return true;
                } else {
                    toPopulate.add(ftnd);
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(FileToNormalizationDescriptor key) {
            try {
                BeanNode node = new BeanNode(key);
                return node;
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
            return Node.EMPTY;
        }
    }

    public DBProjectVisualPanel3() {
        initComponents();
        setName(NbBundle.getMessage(DBProjectVisualPanel3.class,
                "LBL_AssignNormalizationStep"));

//        this.panel = panel;
        // Register listener on the textFields to make the automatic updates
//        projectNameTextField.getDocument().addDocumentListener(this);
//        projectLocationTextField.getDocument().addDocumentListener(this);
//        outputFolder.getDocument().addDocumentListener(this);
    }

    private Set<FileToNormalizationDescriptor> getFileToNormalizationDescriptors(
            WizardDescriptor settings) {
        HashMap<File, INormalizationDescriptor> map = (HashMap<File, INormalizationDescriptor>) settings.
                getProperty(
                        PROP_FILE_TO_NORMALIZATION);
        if (map != null) {
            Set<FileToNormalizationDescriptor> list = new LinkedHashSet<>();
            for (File f : map.keySet()) {
                FileToNormalizationDescriptor fnd = new FileToNormalizationDescriptor(
                        f, map.get(f));
                fnd.getPropertyChangeSupport().addPropertyChangeListener(this);
                list.add(fnd);
            }
            return list;
        }
        return Collections.emptySet();
    }

    private void setFileToNormalizationDescriptors(WizardDescriptor settings,
            Set<FileToNormalizationDescriptor> list) {
        HashMap<File, INormalizationDescriptor> map = new LinkedHashMap<>();
        for (FileToNormalizationDescriptor ftnd : list) {
            map.put(ftnd.getFile(), ftnd.getNormalizationDescriptor());
        }
        settings.putProperty(PROP_FILE_TO_NORMALIZATION, map);
    }

//    public String getProjectName() {
////        return this.projectNameTextField.getText();
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        checkDifferentNormalizationTypes = new javax.swing.JCheckBox();

        setMinimumSize(new java.awt.Dimension(640, 480));
        setPreferredSize(new java.awt.Dimension(640, 480));
        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        checkDifferentNormalizationTypes.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkDifferentNormalizationTypes, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel3.class, "DBProjectVisualPanel3.checkDifferentNormalizationTypes.text")); // NOI18N
        checkDifferentNormalizationTypes.setToolTipText(org.openide.util.NbBundle.getMessage(DBProjectVisualPanel3.class, "DBProjectVisualPanel3.checkDifferentNormalizationTypes.toolTipText")); // NOI18N
        checkDifferentNormalizationTypes.setFocusable(false);
        checkDifferentNormalizationTypes.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        checkDifferentNormalizationTypes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        checkDifferentNormalizationTypes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkDifferentNormalizationTypesActionPerformed(evt);
            }
        });
        jToolBar1.add(checkDifferentNormalizationTypes);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void checkDifferentNormalizationTypesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkDifferentNormalizationTypesActionPerformed
        firePropertyChange("VALIDATE", null, this);
    }//GEN-LAST:event_checkDifferentNormalizationTypesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkDifferentNormalizationTypes;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

//    @Override
//    public void addNotify() {
//        super.addNotify();
//        //same problem as in 31086, initial focus on Cancel button
////        groupTextField.requestFocus();
//    }
    @Override
    public boolean valid(WizardDescriptor wizardDescriptor) {

//        if (fileToNormalization.isEmpty()) {
        // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_ERROR_MESSAGE:
        wizardDescriptor.putProperty(WizardDescriptor.PROP_INFO_MESSAGE,
                "Please review normalization properties for every file.");
        // Display name not specified
//        }
        if (checkDifferentNormalizationTypes.isSelected()) {
            NormalizationType nt = null;
            for (FileToNormalizationDescriptor ftnd : fileToNormalization) {
                if (nt == null) {
                    nt = ftnd.getNormalizationType();
                } else {
                    if (nt != ftnd.getNormalizationType()) {
                        wizardDescriptor.putProperty(
                                WizardDescriptor.PROP_ERROR_MESSAGE,
                                "Normalization type differs for " + ftnd.getFile().
                                getName());
                        return false;
                    }
                }
            }
        }

        wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, "");
        return true;
    }

    @Override
    public void store(WizardDescriptor d) {
//        String name = projectNameTextField.getText().trim();
//        String folder = createdFolderTextField.getText().trim();
//        d.putProperty("projdir", new File(folder));
//        d.putProperty("name", name);
        setFileToNormalizationDescriptors(d, fileToNormalization);
//        StringBuilder sb = new StringBuilder();
//        for (Object o : getListModel().toArray()) {
//            File of = (File) o;
//            sb.append(FileUtil.normalizeFile(of).getAbsolutePath() + ",");
//        }
//        if (sb.length() > 0) {
//            d.putProperty("input.dataInfo", sb.substring(0, sb.length() - 1));
//        }
//        d.putProperty("output.basedir", FileUtil.normalizeFile(new File(outputFolder.getText())).getAbsolutePath());
    }

    @Override
    public void read(WizardDescriptor settings) {

        Set<FileToNormalizationDescriptor> descriptors = getFileToNormalizationDescriptors(
                settings);
        if (descriptors.isEmpty()) {
            descriptors = new LinkedHashSet<>();
            String inputFiles = ((String) settings.getProperty("input.dataInfo"));
            if (inputFiles != null) {
                String[] ifs = inputFiles.split(",");
                for (String s : ifs) {
                    FileToNormalizationDescriptor ftnd = new FileToNormalizationDescriptor(
                            new File(s), new NormalizationDescriptor());
                    ftnd.getPropertyChangeSupport().addPropertyChangeListener(
                            this);
                    descriptors.add(ftnd);
                }
//            firePropertyChange("FILES", ui, ui);
            }
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Normalization Descriptors: {0}", descriptors);
        fileToNormalization = descriptors;
        FileRootNode frn = new FileRootNode(Children.create(new FileNormalizationChildFactory(
                fileToNormalization), true));
        content = new InstanceContent();
        lookup = new ProxyLookup(new AbstractLookup(content), ExplorerUtils.
                createLookup(manager, getActionMap()));
        if (view != null) {
            remove(view);
        }
        view = new OutlineView("Chromatograms");
        view.setPropertyColumns("normalizationType",
                "Normalization Type", "value", "Normalization Value");
        view.getOutline().setRootVisible(false);
        view.removePropertyColumn("Chromatograms");
        add(view, BorderLayout.CENTER);
        manager.setRootContext(frn);
        firePropertyChange("VALIDATE", null, this);
    }

    @Override
    public void validate(WizardDescriptor d) throws WizardValidationException {
        // nothing to validate
        firePropertyChange("VALIDATE", null, this);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
}
