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
package maltcms.ui.fileHandles.properties.pipelineWizard;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.netbeans.spi.wizard.WizardPage;

/**
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class PreprocessingPane extends WizardPage {

	/**
     * 
     */
	private static final long serialVersionUID = 4992924104511397101L;

	/** Creates new form PreprocessingPane */
	public PreprocessingPane() {
		initComponents();
	}

	public static String getDescription() {
		return "Preprocessing";
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Mass Filter"));
        setPreferredSize(new java.awt.Dimension(500, 200));

        jLabel8.setText("Mass Bins");

        jTextField7.setToolTipText("Enter nominal (integer) masses, separated by spaces");

        jList2.setModel(this.dlm1);
        jList2.setName("maltcms.commands.fragments.preprocessing.DenseArrayProducer.maskMasses"); // NOI18N
        jScrollPane2.setViewportView(jList2);

        jButton2.setText("Add");
        jButton2.setToolTipText("Add mass bins entered to the left");
        jButton2.setActionCommand("addMassBin");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2addMassBinFilter(evt);
            }
        });

        jButton5.setText("Remove");
        jButton5.setToolTipText("Remove selected mass bins");
        jButton5.setActionCommand("removeMassBin");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5removeMassBinFilter(evt);
            }
        });

        jCheckBox1.setText("Invert");
        jCheckBox1.setToolTipText("Check to invert behavior of filter to keep given masses");
        jCheckBox1.setActionCommand("invertMassFilter");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertMassFilter(evt);
            }
        });

        jLabel1.setText("Remove");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(jCheckBox1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(51, 51, 51))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jButton2addMassBinFilter(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2addMassBinFilter
		if (evt.getActionCommand().equals("addMassBin")) {
			if (this.jTextField7 != null) {
				String s = this.jTextField7.getText();
				if (!s.isEmpty()) {
					String[] masses = s.split(" ");
					StringBuffer massVals = new StringBuffer();
					for (String mass : masses) {
						Integer m = Integer.parseInt(mass);
						massVals.append(m + ",");
						if (!this.dlm1.contains(m)) {
							this.dlm1.addElement(m);
						}
					}
					putWizardData(
					        "maltcms.commands.fragments.DenseArrayProducer.maskMasses",
					        massVals.toString().substring(0,
					                massVals.length() - 1));
				}
			}
		}
	}// GEN-LAST:event_jButton2addMassBinFilter

	private void jButton5removeMassBinFilter(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5removeMassBinFilter
		if (evt.getActionCommand().equals("removeMassBin")) {
			if (this.jTextField7 != null) {
				String s = this.jTextField7.getText();
				// have text in text field and no entry selected -> remove all
				// entries in text field from model
				if (!s.isEmpty()
				        && this.jList2.getSelectedIndices().length == 0) {
					String[] masses = s.split(" ");
					for (String mass : masses) {
						Integer m = Integer.parseInt(mass);
						this.dlm1.removeElement(m);
					}
					this.jTextField7.setText("");
					putWizardData(
					        "maltcms.commands.fragments.DenseArrayProducer.maskMasses",
					        "");
				} else {
					Object[] indices = this.jList2.getSelectedValues();
					for (Object obj : indices) {
						this.dlm1.removeElement(obj);
					}
					Object[] o = this.dlm1.toArray();
					Arrays.sort(o);
					StringBuilder sb = new StringBuilder();
					for (Object obj : o) {
						sb.append(obj);
						sb.append(" ");
					}
					this.jTextField7.setText(sb.toString().trim());
					putWizardData(
					        "maltcms.commands.fragments.DenseArrayProducer.maskMasses",
					        sb.toString().trim().replaceAll(" ", ","));
				}
			}
		}
	}// GEN-LAST:event_jButton5removeMassBinFilter

	private void invertMassFilter(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_invertMassFilter
		if (evt.getActionCommand().equals("invertMassFilter")) {
			if (this.jCheckBox1.isSelected()) {
				this.jLabel1.setText("Keep");
			} else {
				this.jLabel1.setText("Remove");
			}
		}
	}// GEN-LAST:event_invertMassFilter

	private DefaultListModel dlm1 = new DefaultListModel();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables

}
