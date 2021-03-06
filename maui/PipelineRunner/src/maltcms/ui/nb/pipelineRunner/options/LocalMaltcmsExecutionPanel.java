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
package maltcms.ui.nb.pipelineRunner.options;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JComponent;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import maltcms.ui.nb.pipelineRunner.ui.PipelineRunnerTopComponent;
import net.sf.maltcms.chromaui.project.api.IMaltcmsClassPathProvider;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.modules.Places;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

final class LocalMaltcmsExecutionPanel extends javax.swing.JPanel {

    private final LocalMaltcmsExecutionOptionsPanelController controller;
    private boolean formValid = false;
    private String maltcmsDownloadBase = "http://sourceforge.net/projects/maltcms/files/maltcms/maltcms-";

    LocalMaltcmsExecutionPanel(LocalMaltcmsExecutionOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        maltcmsInstallationPath = new javax.swing.JTextField();
        select = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        maltcmsVersion = new javax.swing.JTextField();
        commandLineOptions = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        download = new javax.swing.JButton();
        maltcmsOnlineVersion = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        maltcmsOptions = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        useDrmaaApiCheckBox = new javax.swing.JCheckBox();
        nativeSpecTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        pathToShellTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        notificationLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(750, 450));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel1.text")); // NOI18N

        maltcmsInstallationPath.setColumns(20);
        maltcmsInstallationPath.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.maltcmsInstallationPath.text")); // NOI18N
        maltcmsInstallationPath.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                maltcmsInstallationPathPropertyChange(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(select, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.select.text")); // NOI18N
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel2.text")); // NOI18N

        maltcmsVersion.setEditable(false);
        maltcmsVersion.setColumns(20);
        maltcmsVersion.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.maltcmsVersion.text")); // NOI18N

        commandLineOptions.setColumns(20);
        commandLineOptions.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.commandLineOptions.text")); // NOI18N
        commandLineOptions.setToolTipText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.commandLineOptions.toolTipText")); // NOI18N
        commandLineOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commandLineOptionsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(download, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.download.text")); // NOI18N
        download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadActionPerformed(evt);
            }
        });

        maltcmsOnlineVersion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1.3.2", "1.3.1", "1.3.0", "1.2.1", "1.2.0", "1.1.0", "LATEST-SNAPSHOT" }));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel4.text")); // NOI18N

        maltcmsOptions.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.maltcmsOptions.text")); // NOI18N
        maltcmsOptions.setToolTipText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.maltcmsOptions.toolTipText")); // NOI18N
        maltcmsOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maltcmsOptionsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(useDrmaaApiCheckBox, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.useDrmaaApiCheckBox.text")); // NOI18N
        useDrmaaApiCheckBox.setToolTipText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.useDrmaaApiCheckBox.toolTipText")); // NOI18N
        useDrmaaApiCheckBox.setEnabled(false);
        useDrmaaApiCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useDrmaaApiCheckBoxActionPerformed(evt);
            }
        });

        nativeSpecTextField.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.nativeSpecTextField.text")); // NOI18N
        nativeSpecTextField.setToolTipText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.nativeSpecTextField.toolTipText")); // NOI18N
        nativeSpecTextField.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel6.text")); // NOI18N

        pathToShellTextField.setText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.pathToShellTextField.text")); // NOI18N
        pathToShellTextField.setToolTipText(org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.pathToShellTextField.toolTipText")); // NOI18N
        pathToShellTextField.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.jLabel7.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(notificationLabel, org.openide.util.NbBundle.getMessage(LocalMaltcmsExecutionPanel.class, "LocalMaltcmsExecutionPanel.notificationLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jSeparator1)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(useDrmaaApiCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pathToShellTextField)
                            .addComponent(maltcmsOnlineVersion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(maltcmsInstallationPath, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                            .addComponent(maltcmsVersion)
                            .addComponent(commandLineOptions)
                            .addComponent(maltcmsOptions)
                            .addComponent(nativeSpecTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(download, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(select, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator3)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(notificationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {download, select});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(download)
                    .addComponent(maltcmsOnlineVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(maltcmsInstallationPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(select))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(maltcmsVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(commandLineOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maltcmsOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useDrmaaApiCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nativeSpecTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathToShellTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notificationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {commandLineOptions, maltcmsInstallationPath, maltcmsOnlineVersion, maltcmsOptions, maltcmsVersion});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {download, select});

    }// </editor-fold>//GEN-END:initComponents

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        FileChooserBuilder fcb = new FileChooserBuilder(LocalMaltcmsExecutionPanel.class);
        fcb.setFilesOnly(false);
        fcb.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                return file.getName().equals("maltcms.jar");
            }

            @Override
            public String getDescription() {
                return "maltcms.jar";
            }
        });
        fcb.setSelectionApprover(new FileChooserBuilder.SelectionApprover() {
            @Override
            public boolean approve(File[] files) {
//                boolean approve = false;
                for (File f : files) {
                    if (f.getName().equals("maltcms.jar")) {
                        return true;
                    }
                }
                return false;
            }
        });
        JFileChooser jfc = fcb.createFileChooser();
        if (!maltcmsInstallationPath.getText().isEmpty()) {
            File basedir = new File(maltcmsInstallationPath.getText());
            jfc.setCurrentDirectory(basedir);
        }
        int ret = jfc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            maltcmsInstallationPath.setText(f.getParent());
            checkVersion(f.getParentFile().getAbsolutePath());
            controller.changed();
        }
    }//GEN-LAST:event_selectActionPerformed

    private void maltcmsInstallationPathPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_maltcmsInstallationPathPropertyChange
        String propName = evt.getPropertyName();
        if (propName.equals("text")) {
            String value = (String) evt.getNewValue();
            checkVersion(value);
        }
    }//GEN-LAST:event_maltcmsInstallationPathPropertyChange

    private void commandLineOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commandLineOptionsActionPerformed
        controller.changed();
    }//GEN-LAST:event_commandLineOptionsActionPerformed

    private void downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadActionPerformed
        DownloadAndInstallTask dait = new DownloadAndInstallTask(this);
        DownloadAndInstallTask.createAndRun("Maltcms download and installation", dait);
    }//GEN-LAST:event_downloadActionPerformed

    private void maltcmsOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maltcmsOptionsActionPerformed
        controller.changed();
    }//GEN-LAST:event_maltcmsOptionsActionPerformed

    private void useDrmaaApiCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useDrmaaApiCheckBoxActionPerformed
        if (useDrmaaApiCheckBox.isSelected()) {
            nativeSpecTextField.setEnabled(true);
            pathToShellTextField.setEnabled(true);
        } else {
            nativeSpecTextField.setEnabled(false);
            pathToShellTextField.setEnabled(false);
        }
        controller.changed();
    }//GEN-LAST:event_useDrmaaApiCheckBoxActionPerformed

    private class DownloadAndInstallTask extends AProgressAwareRunnable {

        private final JComponent component;
        
        DownloadAndInstallTask(JComponent component) {
            this.component = component;
        }
        
        @Override
        public boolean cancel() {
            component.setEnabled(true);
            for (Component c : component.getComponents()) {
                c.setEnabled(true);
            }
            setDrmaaSettingsVisible(false, false);
            if(progressHandle!=null) {
                progressHandle.finish();
            }
            return true;
        }

        @Override
        public void run() {
            try {
                progressHandle.start();
                notificationLabel.setText("<html><font color=blue>Downloading Maltcms, please wait!</font></html>");
                progressHandle.progress("Asking User for Download Location");
                component.setEnabled(false);
                for (Component c : component.getComponents()) {
                    c.setEnabled(false);
                }
                notificationLabel.setEnabled(true);
                File userDir = Places.getUserDirectory();
                File targetDirectory = new File(userDir, "maltcms");
                targetDirectory.mkdirs();
                progressHandle.progress("Downloading Maltcms " + (String) maltcmsOnlineVersion.getSelectedItem());
                String version = (String) maltcmsOnlineVersion.getSelectedItem();
                URL u = null;
                File targetFile = null;
                String maltcmsBasename = null;
                if (version.equals("LATEST-SNAPSHOT")) {
                    u = new URL("http://maltcms.de/maltcms/latest/maltcms-distribution-latest.zip");
                    Path p = Files.createTempDirectory("maltcms-download-tmp");
                    File downloadedFile = new File(p.toFile(), "maltcms-distribution-latest.zip");
                    FileUtils.copyURLToFile(u, downloadedFile);
                    String topLevelArchiveName = getTopLevelArchiveDirectoryName(downloadedFile);
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Top level archive directory name: {0}", topLevelArchiveName);
                    maltcmsBasename = topLevelArchiveName.substring(0, topLevelArchiveName.length() - 1);
                    targetFile = new File(targetDirectory, maltcmsBasename + "-bin.zip");
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "copying to target file {0}", targetFile);
                    FileUtils.copyFile(downloadedFile, targetFile);
                } else {
                    String[] majorMinorMicro = version.split("\\.");
                    maltcmsBasename = "maltcms-" + majorMinorMicro[0] + "." + majorMinorMicro[1] + "." + majorMinorMicro[2];
                    String filename = maltcmsBasename + "-bin.zip";
                    u = new URL(maltcmsDownloadBase + majorMinorMicro[0] + "." + majorMinorMicro[1] + "/" + filename);
                    targetFile = new File(targetDirectory, filename);
                    FileUtils.copyURLToFile(u, targetFile);
                }

                progressHandle.progress("Extracting Maltcms " + (String) maltcmsOnlineVersion.getSelectedItem());
                unzipArchive(targetFile, targetDirectory);
                File maltcmsDistDir = new File(targetDirectory, maltcmsBasename.replace("maltcms-", "maltcms-distribution-"));
                File maltcmsInstallationDir = new File(targetDirectory, maltcmsBasename);
                if (maltcmsDistDir.isDirectory()) {
                    FileUtils.copyDirectory(maltcmsDistDir, maltcmsInstallationDir);
                }
                if (maltcmsInstallationDir.isDirectory()) {
                    progressHandle.progress("Setting Maltcms Installation Path");
                    maltcmsInstallationPath.setText(maltcmsInstallationDir.getAbsolutePath());
                    checkVersion(maltcmsInstallationDir.getAbsolutePath());
                    controller.changed();
                    store();
                } else {
                    throw new IOException("Could not locate unzipped directory! Please check!");
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                component.setEnabled(true);
                for (Component c : component.getComponents()) {
                    c.setEnabled(true);
                }
                setDrmaaSettingsVisible(false, false);
                boolean b = OptionsDisplayer.getDefault().open("maltcmsOptions");
                progressHandle.finish();
            }
        }
    }

    public String getTopLevelArchiveDirectoryName(File archive) {
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                if (entry.isDirectory()) {
                    return entry.getName();
                }
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
        return null;
    }

    public void unzipArchive(File archive, File outputDir) {
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, outputDir);
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    private void createDir(File dir) {
        if (dir.exists()) {
            return;
        }
        if (!dir.mkdirs()) {
            throw new RuntimeException("Can not create directory " + dir);
        }
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {
        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }

        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            inputStream.close();
        }
    }

    void load() {
        maltcmsInstallationPath.setText(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsInstallationPath", ""));
        checkVersion(maltcmsInstallationPath.getText());
        commandLineOptions.setText(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("commandLineOptions", ""));
        maltcmsOptions.setText(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("maltcmsOptions", ""));
        nativeSpecTextField.setText(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("drmaa.nativeSpec", ""));
        pathToShellTextField.setText(NbPreferences.forModule(PipelineRunnerTopComponent.class).get("drmaa.pathToShell", ""));
//        setDrmaaSettingsVisible(NbPreferences.forModule(PipelineRunnerTopComponent.class).getBoolean("drmaa.use", false),NbPreferences.forModule(PipelineRunnerTopComponent.class).getBoolean("drmaa.use", false));
    }

    private void setDrmaaSettingsVisible(boolean enabled, boolean selected) {
        useDrmaaApiCheckBox.setEnabled(enabled);
        useDrmaaApiCheckBox.setSelected(selected);
        nativeSpecTextField.setEnabled(useDrmaaApiCheckBox.isSelected());
        pathToShellTextField.setEnabled(useDrmaaApiCheckBox.isSelected());
    }

    void store() {
        NbPreferences.forModule(PipelineRunnerTopComponent.class).put("maltcmsInstallationPath", maltcmsInstallationPath.getText());
        NbPreferences.forModule(PipelineRunnerTopComponent.class).put("commandLineOptions", commandLineOptions.getText().trim());
        NbPreferences.forModule(PipelineRunnerTopComponent.class).put("maltcmsOptions", maltcmsOptions.getText().trim());
        if (!maltcmsInstallationPath.getText().isEmpty()) {
            NbPreferences.forModule(IMaltcmsClassPathProvider.class).put("extClassPathRoot", maltcmsInstallationPath.getText() + File.separator + "lib");
        }
        NbPreferences.forModule(PipelineRunnerTopComponent.class).put("maltcmsVersion", maltcmsVersion.getText());
        NbPreferences.forModule(PipelineRunnerTopComponent.class).put("drmaa.nativeSpec", nativeSpecTextField.getText());
        NbPreferences.forModule(PipelineRunnerTopComponent.class).put("drmaa.pathToShell", pathToShellTextField.getText());
        NbPreferences.forModule(PipelineRunnerTopComponent.class).putBoolean("drmaa.use", useDrmaaApiCheckBox.isSelected());
    }

    public static String getVersion(String maltcmsPath) {
        File basedir = new File(maltcmsPath);
        File cfgdir = new File(basedir, "cfg");
        try {
            PropertiesConfiguration pc = new PropertiesConfiguration(new File(cfgdir, "application.properties"));
            return pc.getString("application.version", "NA");
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "NA";
    }

    private void checkVersion(String maltcmsPath) {
        maltcmsVersion.setText(getVersion(maltcmsPath));
        maltcmsVersion.setEnabled(true);
        controller.changed();
    }

    boolean valid() {
        notificationLabel.setText("");
        return true;
        // TODO check whether form is consistent and complete
//        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField commandLineOptions;
    private javax.swing.JButton download;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField maltcmsInstallationPath;
    private javax.swing.JComboBox maltcmsOnlineVersion;
    private javax.swing.JTextField maltcmsOptions;
    private javax.swing.JTextField maltcmsVersion;
    private javax.swing.JTextField nativeSpecTextField;
    private javax.swing.JLabel notificationLabel;
    private javax.swing.JTextField pathToShellTextField;
    private javax.swing.JButton select;
    private javax.swing.JCheckBox useDrmaaApiCheckBox;
    // End of variables declaration//GEN-END:variables
}
