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
package net.sf.maltcms.chromaui.db.spi.db4o.options;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.maltcms.chromaui.db.api.db4o.DB4oCrudProviderFactory;
import org.openide.util.NbPreferences;

final class Db4oGeneralSettingsPanel extends javax.swing.JPanel implements ValidationMessageListener, DocumentListener {

	private final Db4oGeneralSettingsOptionsPanelController controller;

	Db4oGeneralSettingsPanel(Db4oGeneralSettingsOptionsPanelController controller) {
		this.controller = controller;
		initComponents();
		validationMessage.setText(" ");
		// TODO listen to changes in form fields and call controller.changed()
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        verboseDiagnostics = new javax.swing.JCheckBox();
        createAutomaticBackups = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        backupInterval = new javax.swing.JFormattedTextField();
        validationMessage = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(349, 28));

        org.openide.awt.Mnemonics.setLocalizedText(verboseDiagnostics, org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.verboseDiagnostics.text")); // NOI18N
        verboseDiagnostics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verboseDiagnosticsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(createAutomaticBackups, org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.createAutomaticBackups.text")); // NOI18N
        createAutomaticBackups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAutomaticBackupsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.jLabel3.text")); // NOI18N

        jTextField2.setEditable(false);
        jTextField2.setText(org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.jTextField2.text")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, createAutomaticBackups, org.jdesktop.beansbinding.ELProperty.create("${selected}"), jTextField2, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        backupInterval.setColumns(5);
        backupInterval.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        backupInterval.setText(org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.backupInterval.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, createAutomaticBackups, org.jdesktop.beansbinding.ELProperty.create("${selected}"), backupInterval, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        backupInterval.getDocument().addDocumentListener(this);

        org.openide.awt.Mnemonics.setLocalizedText(validationMessage, org.openide.util.NbBundle.getMessage(Db4oGeneralSettingsPanel.class, "Db4oGeneralSettingsPanel.validationMessage.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(verboseDiagnostics, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(backupInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                                    .addComponent(jTextField2)))
                            .addComponent(createAutomaticBackups))
                        .addGap(10, 10, 10))
                    .addComponent(validationMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createAutomaticBackups)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(backupInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(verboseDiagnostics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(validationMessage)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void createAutomaticBackupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAutomaticBackupsActionPerformed
		controller.changed();
    }//GEN-LAST:event_createAutomaticBackupsActionPerformed

    private void verboseDiagnosticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verboseDiagnosticsActionPerformed
		controller.changed();
    }//GEN-LAST:event_verboseDiagnosticsActionPerformed

	void load() {
		verboseDiagnostics.setSelected(NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("verboseDiagnostics", false));
		createAutomaticBackups.setSelected(NbPreferences.forModule(DB4oCrudProviderFactory.class).getBoolean("createAutomaticBackups", false));
		backupInterval.setValue(Long.valueOf(NbPreferences.forModule(DB4oCrudProviderFactory.class).getInt("backupInterval", 10)));
	}

	void store() {
		NbPreferences.forModule(DB4oCrudProviderFactory.class).putBoolean("verboseDiagnostics", verboseDiagnostics.isSelected());
		NbPreferences.forModule(DB4oCrudProviderFactory.class).putBoolean("createAutomaticBackups", createAutomaticBackups.isSelected());
		NbPreferences.forModule(DB4oCrudProviderFactory.class).putInt("backupInterval", ((Long) backupInterval.getValue()).intValue());
	}

	boolean valid() {
		boolean clearIfValid = true;
		if (createAutomaticBackups.isSelected()) {
			Long backupIntervalValue = (Long) backupInterval.getValue();
			if (backupIntervalValue.longValue() < 1) {
				validationMessage(new ValidationMessage(backupInterval, ValidationMessage.Type.ERROR, "Backup Interval must not be <1!"));
				return false;
			}
			validationMessage(new ValidationMessage(createAutomaticBackups, ValidationMessage.Type.INFO, "Projects will be automtically saved and reopened!"));
			clearIfValid = false;
		} else if (verboseDiagnostics.isSelected()) {
			validationMessage(new ValidationMessage(createAutomaticBackups, ValidationMessage.Type.INFO, "Projects will be automtically saved and reopened!"));
			clearIfValid = false;
		}

		if (clearIfValid) {
			validationMessage(new ValidationMessage(this, ValidationMessage.Type.CLEAR, ""));
		}
		return true;
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField backupInterval;
    private javax.swing.JCheckBox createAutomaticBackups;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel validationMessage;
    private javax.swing.JCheckBox verboseDiagnostics;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

	@Override
	public void validationMessage(ValidationMessage message) {
		switch (message.getType()) {
			case CLEAR:
				validationMessage.setIcon(null);
				validationMessage.setText("");
				break;
			case INFO:
				validationMessage.setText(message.getMessage());
				break;
			case WARNING:
				validationMessage.setText("<html><font color=red>" + message.getMessage() + "</font></html>");
				break;
			case ERROR:
				validationMessage.setText("<html><font color=red>" + message.getMessage() + "<font color=red></html>");
				break;
		}
	}

	@Override
	public void insertUpdate(DocumentEvent de) {
		controller.changed();
	}

	@Override
	public void removeUpdate(DocumentEvent de) {
		controller.changed();
	}

	@Override
	public void changedUpdate(DocumentEvent de) {
		controller.changed();
	}
}
