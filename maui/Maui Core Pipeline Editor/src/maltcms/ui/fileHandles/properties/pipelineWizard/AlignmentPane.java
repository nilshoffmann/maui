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

import javax.swing.DefaultComboBoxModel;

import org.netbeans.spi.wizard.WizardPage;

/**
 *
 * @author Nils Hoffmann
 */
public class AlignmentPane extends WizardPage {

    /**
     *
     */
    private static final long serialVersionUID = -5324638127680705898L;

    /**
     * Creates new form AlignmentPane
     */
    public AlignmentPane() {
        initComponents();
    }

    public static String getDescription() {
        return "Alignment";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jComboBox1 = new javax.swing.JComboBox();
		jLabel2 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		jTextField2 = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		jTextField3 = new javax.swing.JTextField();

		setBorder(javax.swing.BorderFactory.createTitledBorder("Alignment"));
		setPreferredSize(new java.awt.Dimension(500, 213));

		jLabel1.setText("Local Similarity/Distance");

		jComboBox1.setModel(getLocalSimDistComboBoxModel());
		jComboBox1.setName("alignment.algorithm.distance"); // NOI18N
		jComboBox1.setPrototypeDisplayValue("Linear Correlation Similarity");
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jComboBox1ActionPerformed(evt);
			}
		});

		jLabel2.setText("Anchor Neighborhood Radius");

		jTextField1.setText("25");
		jTextField1
		        .setToolTipText("Number of scans to consider aroung each anchor");
		jTextField1.setName("alignment.anchors.neighborhood"); // NOI18N

		jLabel3.setText("Maximum Allowed Scan Lag");

		jTextField2.setText("0.05");
		jTextField2
		        .setToolTipText("Percentage of maximum allowed lag between chromatograms (between 0 and 1)");
		jTextField2.setName("alignment.algorithm.windowsize"); // NOI18N
		jTextField2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField2ActionPerformed(evt);
			}
		});

		jLabel4.setText("Number of Threads");

		jTextField3.setText("1");
		jTextField3.setName("cross.Factory.maxthreads"); // NOI18N

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout
		        .setHorizontalGroup(layout
		                .createParallelGroup(
		                        javax.swing.GroupLayout.Alignment.LEADING)
		                .addGroup(
		                        javax.swing.GroupLayout.Alignment.TRAILING,
		                        layout
		                                .createSequentialGroup()
		                                .addGap(43, 43, 43)
		                                .addGroup(
		                                        layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.TRAILING)
		                                                .addComponent(jLabel1)
		                                                .addComponent(jLabel2)
		                                                .addComponent(jLabel3)
		                                                .addComponent(jLabel4))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(
		                                        layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.TRAILING)
		                                                .addComponent(
		                                                        jTextField2,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        165,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(
		                                                        jTextField1,
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        165,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(
		                                                        jComboBox1,
		                                                        javax.swing.GroupLayout.Alignment.LEADING,
		                                                        0, 165,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(
		                                                        jTextField3,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        165,
		                                                        Short.MAX_VALUE))
		                                .addGap(37, 37, 37)));
		layout
		        .setVerticalGroup(layout
		                .createParallelGroup(
		                        javax.swing.GroupLayout.Alignment.LEADING)
		                .addGroup(
		                        layout
		                                .createSequentialGroup()
		                                .addContainerGap()
		                                .addGroup(
		                                        layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.BASELINE)
		                                                .addComponent(
		                                                        jComboBox1,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
		                                                .addComponent(jLabel1))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                                .addGroup(
		                                        layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.BASELINE)
		                                                .addComponent(
		                                                        jTextField1,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
		                                                .addComponent(jLabel2))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                                .addGroup(
		                                        layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.BASELINE)
		                                                .addComponent(
		                                                        jTextField2,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
		                                                .addComponent(jLabel3))
		                                .addPreferredGap(
		                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                                .addGroup(
		                                        layout
		                                                .createParallelGroup(
		                                                        javax.swing.GroupLayout.Alignment.BASELINE)
		                                                .addComponent(
		                                                        jTextField3,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
		                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
		                                                .addComponent(jLabel4))
		                                .addContainerGap(
		                                        javax.swing.GroupLayout.DEFAULT_SIZE,
		                                        Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField2ActionPerformed

    private DefaultComboBoxModel dcbm = null;

    protected DefaultComboBoxModel getLocalSimDistComboBoxModel() {
        if (this.dcbm == null) {
            this.dcbm = new DefaultComboBoxModel(new String[]{
                "Cosine Similarity", "Linear Correlation Similarity",
                "Hamming Distance", "Euclidean Distance",
                "TIC Squared Distance"});
        }
        return this.dcbm;
    }

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	private javax.swing.JTextField jTextField3;
	// End of variables declaration//GEN-END:variables

}
