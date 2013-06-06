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
package net.sf.maltcms.chromaui.project.spi.wizard;

import cross.tools.StringTools;
import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Document;
import org.apache.commons.io.FileUtils;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle;

public class DBProjectVisualPanel1 extends JPanel implements DocumentListener,
        IWizardValidatable {

    public static final String PROP_PROJECT_NAME = "projectName";

    public DBProjectVisualPanel1() {
        initComponents();
        // Register listener on the textFields to make the automatic updates
        projectNameTextField.getDocument().addDocumentListener(this);
        projectLocationTextField.getDocument().addDocumentListener(this);
        setName(NbBundle.getMessage(DBProjectVisualPanel1.class,
                "LBL_CreateProjectStep"));
    }

    public String getProjectName() {
        return this.projectNameTextField.getText();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectNameLabel = new javax.swing.JLabel();
        projectNameTextField = new javax.swing.JTextField();
        projectLocationLabel = new javax.swing.JLabel();
        projectLocationTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        createdFolderLabel = new javax.swing.JLabel();
        createdFolderTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputFiles = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        outputFolderTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        copyFiles = new javax.swing.JCheckBox();

        setMinimumSize(new java.awt.Dimension(640, 480));
        setPreferredSize(new java.awt.Dimension(640, 480));

        projectNameLabel.setLabelFor(projectNameTextField);
        org.openide.awt.Mnemonics.setLocalizedText(projectNameLabel, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.projectNameLabel.text")); // NOI18N

        projectLocationLabel.setLabelFor(projectLocationTextField);
        org.openide.awt.Mnemonics.setLocalizedText(projectLocationLabel, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.projectLocationLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.browseButton.text")); // NOI18N
        browseButton.setActionCommand(org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.browseButton.actionCommand")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        createdFolderLabel.setLabelFor(createdFolderTextField);
        org.openide.awt.Mnemonics.setLocalizedText(createdFolderLabel, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.createdFolderLabel.text")); // NOI18N

        createdFolderTextField.setEditable(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.jLabel1.text")); // NOI18N

        inputFiles.setModel(getListModel());
        jScrollPane1.setViewportView(inputFiles);
        inputFiles.setModel(getListModel());
        inputFiles.setCellRenderer(new FileListCellRenderer());

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        outputFolderTextField.setText(org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.outputFolderTextField.text")); // NOI18N
        outputFolderTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputFolderTextFieldActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(copyFiles, org.openide.util.NbBundle.getMessage(DBProjectVisualPanel1.class, "DBProjectVisualPanel1.copyFiles.text")); // NOI18N
        copyFiles.setEnabled(false);
        copyFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(projectNameLabel)
                    .addComponent(projectLocationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createdFolderLabel)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outputFolderTextField)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addComponent(projectNameTextField)
                    .addComponent(projectLocationTextField)
                    .addComponent(createdFolderTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(browseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(copyFiles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectNameLabel)
                    .addComponent(projectNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectLocationLabel)
                    .addComponent(projectLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createdFolderLabel)
                    .addComponent(createdFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(copyFiles))
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        String command = evt.getActionCommand();
        if ("BROWSE".equals(command)) {
            FileChooserBuilder fcb = new FileChooserBuilder(DBProjectVisualPanel1.class);
            fcb.setTitle("Select Project Location");
            fcb.setDirectoriesOnly(true);
            String path = this.projectLocationTextField.getText();
            JFileChooser jfc = fcb.createFileChooser();
            if (path.length() > 0) {
                File f = new File(path);
                if (f.exists()) {
                    jfc.setSelectedFile(f);
                }
            }
            if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(this)) {
                File projectDir = jfc.getSelectedFile();
                projectLocationTextField.setText(FileUtil.normalizeFile(
                        projectDir).getAbsolutePath());
            }
            firePropertyChange("VALIDATE", null, null);
        }

    }//GEN-LAST:event_browseButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FileChooserBuilder fcb = new FileChooserBuilder(DBProjectVisualPanel1.class);
        final String[] fileExtensions = new String[]{"csv", "tsv", "txt", "cdf", "nc", "mz5", "mzml", "mzxml", "mzdata"};
        Arrays.sort(fileExtensions);
        //fcb.setFilesOnly(true);
        fcb.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String ext = StringTools.getFileExtension(file.getName().toLowerCase());
                    int idx = Arrays.binarySearch(fileExtensions, ext);
                    if(idx>=0) {
                        System.out.println("Found matching file extension at index: "+idx+"="+fileExtensions[idx]);
                        return true;
                    }
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "Peak lists, Raw chromatograms (recursive)";
            }
        });
        fcb.setTitle("Select Input Files");
//        jfc.setMultiSelectionEnabled(true);
        File[] files = fcb.showMultiOpenDialog();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    Collection<File> l = FileUtils.listFiles(f, fileExtensions, true);
                    for(File file:l) {
						System.err.println("Adding file below selected directory: "+file);
                        getListModel().addElement(file);
                    }
                } else {
					System.err.println("Adding selected file: "+f);
                    getListModel().addElement(f);
                }
            }
            firePropertyChange("VALIDATE", null, null);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        getListModel().clear();
        firePropertyChange("VALIDATE", null, null);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void outputFolderTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputFolderTextFieldActionPerformed
    }//GEN-LAST:event_outputFolderTextFieldActionPerformed

    private void copyFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyFilesActionPerformed
        firePropertyChange("VALIDATE", null, null);
    }//GEN-LAST:event_copyFilesActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JCheckBox copyFiles;
    private javax.swing.JLabel createdFolderLabel;
    private javax.swing.JTextField createdFolderTextField;
    private javax.swing.JList inputFiles;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField outputFolderTextField;
    private javax.swing.JLabel projectLocationLabel;
    private javax.swing.JTextField projectLocationTextField;
    private javax.swing.JLabel projectNameLabel;
    private javax.swing.JTextField projectNameTextField;
    // End of variables declaration//GEN-END:variables
    private DefaultListModel dlm = null;

    private DefaultListModel getListModel() {
        if (dlm == null) {
            dlm = new DefaultListModel();
        }
        return dlm;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        //same problem as in 31086, initial focus on Cancel button
        projectNameTextField.requestFocus();
    }

    @Override
    public boolean valid(WizardDescriptor wizardDescriptor) {

        if (projectNameTextField.getText().length() == 0) {
            // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_ERROR_MESSAGE:
            wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE,
                    "Project Name is not a valid folder name.");
            return false; // Display name not specified
        }
        File f = new File(projectLocationTextField.getText());
        System.out.println("Project location: " + f.getAbsolutePath() + " exists: " + f.
                exists() + " isDir: " + f.isDirectory());
        if (!f.isDirectory()) {
            String message = "Project Folder is not a valid path.";
            wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE,
                    message);
            return false;
        }
        final File destFolder = new File(createdFolderTextField.getText());
        System.out.println("Dest folder");
        File projLoc = destFolder;
        while (projLoc != null && !projLoc.exists()) {
            projLoc = projLoc.getParentFile();
        }
        if (projLoc == null || !projLoc.canWrite()) {
            wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE,
                    "Project Folder cannot be created.");
            return false;
        }
        System.out.println("Project location");
//        if (proj == null) {
//            String message = "Project Folder is not a valid path.";
//            wizardDescriptor.putProperty("WizardPanel_errorMessage", message);
//            return false;
//        }

        File[] kids = destFolder.listFiles();
        if (destFolder.exists() && kids != null && kids.length > 0) {
            // Folder exists and is not empty
            wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE,
                    "Project Folder already exists and is not empty.");
            return false;
        }

        System.out.println("valid called, number of elements in list model: " + getListModel().
                size());
        if (getListModel().isEmpty()) {
            wizardDescriptor.putProperty("WizardPanel_errorMessage",
                    "Please add at least one raw chromatogram file.");
            return false;
        }

        for (Object o : getListModel().toArray()) {
            File of = (File) o;
            if (!of.exists()) {
                wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE,
                        "Input data file: " + of.getAbsolutePath() + " does not exist.");
                return false;
            }
            if (!of.canRead()) {
                wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE,
                        "Input data file: " + of.getAbsolutePath() + " can not be read, check file permissions.");
                return false;
            }
        }

//        if (outputFolder.getText().isEmpty()) {
//            wizardDescriptor.putProperty("WizardPanel_errorMessage",
//                    "No output base directory selected.");
//            return false;
//        }
//        
//        if (new File(outputFolder.getText()).isFile()) {
//            wizardDescriptor.putProperty("WizardPanel_errorMessage",
//                    "Output base directory is a file.");
//            return false;
//        }

        wizardDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, "");
        return true;
    }

    @Override
    public void store(WizardDescriptor d) {
        String name = projectNameTextField.getText().trim();
        String folder = createdFolderTextField.getText().trim();
        String outputDir = outputFolderTextField.getText().trim();
        Boolean doCopyFiles = copyFiles.isSelected();

        d.putProperty("projdir", new File(folder));
        d.putProperty("name", name);
        d.putProperty("output.basedir", new File(outputDir));
        d.putProperty("copy.files", doCopyFiles);
        StringBuilder sb = new StringBuilder();
        for (Object o : getListModel().toArray()) {
            File of = (File) o;
            sb.append(of.getAbsolutePath()).append(",");
        }
        if (sb.length() > 0) {
            d.putProperty("input.dataInfo", sb.substring(0, sb.length() - 1));
        }
//        d.putProperty("output.basedir", FileUtil.normalizeFile(new File(outputFolder.getText())).getAbsolutePath());
    }

    @Override
    public void read(WizardDescriptor settings) {
        File projectLocation = (File) settings.getProperty("projdir");
        if (projectLocation == null || projectLocation.getParentFile() == null || !projectLocation.
                getParentFile().isDirectory()) {
            try {
//                ProjectChooser.setProjectsFolder(new File(new File(System.getProperty("user.home")),"MauiProjects"));
                projectLocation = ProjectChooser.getProjectsFolder();
            } catch (AssertionError ae) {
                projectLocation = new File(System.getProperty("user.home"));
            }
        } else {
            projectLocation = projectLocation.getParentFile();
        }
        this.projectLocationTextField.setText(projectLocation.getAbsolutePath());

        File outputDir = (File) settings.getProperty("output.basedir");
        if (outputDir == null) {
            outputDir = FileUtil.normalizeFile(new File(projectLocation,
                    "output"));
        }

        this.outputFolderTextField.setText(outputDir.getAbsolutePath());

        getListModel().clear();
        String inputFiles = ((String) settings.getProperty("input.dataInfo"));
        if (inputFiles != null) {
            String[] ifs = inputFiles.split(",");
            for (String s : ifs) {
                getListModel().addElement(new File(s));
            }
        }

        String projectName = (String) settings.getProperty("name");
        if (projectName == null) {
            projectName = NbBundle.getMessage(DBProjectVisualPanel1.class,
                    "DBProjectVisualPanel1.projectNameTextField.text");
        }

        this.projectNameTextField.setText(projectName);
        this.projectNameTextField.selectAll();

        Boolean doCopyFiles = (Boolean) settings.getProperty("copy.files");
        if (doCopyFiles == null) {
            copyFiles.setSelected(false);
        } else {
            copyFiles.setSelected(doCopyFiles);
        }

//        this.outputFolder.setText((String) settings.getProperty("output.basedir"));
    }

    @Override
    public void validate(WizardDescriptor d) throws WizardValidationException {
        // nothing to validate
    }

    // Implementation of DocumentListener --------------------------------------
    @Override
    public void changedUpdate(DocumentEvent e) {
        updateTexts(e);
        if (this.projectNameTextField.getDocument() == e.getDocument()) {
            firePropertyChange(PROP_PROJECT_NAME, null,
                    this.projectNameTextField.getText());
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateTexts(e);
        if (this.projectNameTextField.getDocument() == e.getDocument()) {
            firePropertyChange(PROP_PROJECT_NAME, null,
                    this.projectNameTextField.getText());
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateTexts(e);
        if (this.projectNameTextField.getDocument() == e.getDocument()) {
            firePropertyChange(PROP_PROJECT_NAME, null,
                    this.projectNameTextField.getText());
        }
    }

    /**
     * Handles changes in the Project name and project directory,
     */
    private void updateTexts(DocumentEvent e) {

        Document doc = e.getDocument();

        if (doc == projectNameTextField.getDocument() || doc == projectLocationTextField.
                getDocument()) {
            // Change in the project name

            String projectName = projectNameTextField.getText();
            String projectFolder = projectLocationTextField.getText();

            //if (projectFolder.trim().length() == 0 || projectFolder.equals(oldName)) {
            createdFolderTextField.setText(
                    projectFolder + File.separatorChar + projectName);
            //}
            String otxt = outputFolderTextField.getText();
//            if (.isEmpty()) {
            File outputDir = new File(new File(projectFolder, projectName),
                    "output");
            outputFolderTextField.setText(FileUtil.normalizeFile(outputDir).
                    getAbsolutePath());
//            }

        }
//        if (panel != null) {
//            panel.fireChangeEvent(); // Notify that the panel changed
//        }
    }

    class FileListCellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList jlist, Object o,
                int i, boolean bln, boolean bln1) {
            if (o instanceof File) {
                String filename = ((File) o).getName();
                JLabel jl = new JLabel(filename);
                jl.setToolTipText(((File) o).getAbsolutePath());
                return jl;
            } else {
                return new DefaultListCellRenderer().
                        getListCellRendererComponent(jlist, o, i, bln, bln1);
            }
        }
    }
}
