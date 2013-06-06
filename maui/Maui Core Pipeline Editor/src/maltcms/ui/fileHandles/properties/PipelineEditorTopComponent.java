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
package maltcms.ui.fileHandles.properties;

import java.io.IOException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import maltcms.ui.fileHandles.properties.graph.SceneMainMenu;
import maltcms.ui.fileHandles.properties.tools.SceneExporter;
import maltcms.ui.fileHandles.properties.tools.SceneLayouter;
import maltcms.ui.fileHandles.properties.tools.SceneValidator;
import org.netbeans.api.actions.Savable;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//de.chromA.pipelineEditor//PipelineEditor//EN",
		autostore = false)
public final class PipelineEditorTopComponent extends CloneableTopComponent {

	private static PipelineEditorTopComponent instance;
	/**
	 * path to the icon used by the component and its open action
	 */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
	private static final String PREFERRED_ID = "PipelineEditorTopComponent";
	private SceneLayout layout;
	private PipelineGraphScene scene;
	private SceneValidator sceneValidator;
	private FileObject baseFile;
	protected JComponent view;
	private double zoomSteps = 0.2d;
	private final InstanceContent content;
	private boolean hasChanges = false;
	private final PipelineSavable savable;

	public PipelineEditorTopComponent() {
		content = new InstanceContent();
		associateLookup(new AbstractLookup(content));
		this.savable = new PipelineSavable();
		this.scene = new PipelineGraphScene();
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.view = this.scene.createView();

		initComponents();

		layout = new SceneLayout(this.scene) {
			@Override
			protected void performLayout() {
				if (scene.getChildren().size() > 0) {
					Rectangle rectangle = new Rectangle(0, 0, 1, 1);
					for (Widget widget : scene.getChildren()) {
						rectangle = rectangle.union(widget.convertLocalToScene(widget.getBounds()));
					}
					Dimension dim = rectangle.getSize();
					Dimension viewDim = jScrollPane1.getViewportBorderBounds().getSize();
					scene.setZoomFactor(Math.min((float) viewDim.width / dim.width, (float) viewDim.height / dim.height));
				}
			}
		};
		this.scene.setLayout(layout);

		//add(this.view, java.awt.BorderLayout.CENTER);

		setName(NbBundle.getMessage(PipelineEditorTopComponent.class, "CTL_PipelineEditorTopComponent"));
		setToolTipText(NbBundle.getMessage(PipelineEditorTopComponent.class, "HINT_PipelineEditorTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

		this.sceneValidator = new SceneValidator(this.scene);
	}

	public void setPipelineGraphScene(PipelineGraphScene scene) {
		this.scene = scene;
	}

	public PipelineGraphScene getPipelineGraphScene() {
		return this.scene;
	}

	public void setBaseFile(FileObject baseFile) {
		if (this.baseFile != null && !this.baseFile.getPath().equals(baseFile.getPath())) {
			SceneExporter.showSaveDialog(this.scene);
			content.remove(this.baseFile);
		}
		this.baseFile = baseFile;
		this.scene.setBaseFile(this.baseFile);
		content.add(this.baseFile);
		setChanged(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        GridsToggleButton = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton8 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jToggleButton2 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane(this.view);

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);

        GridsToggleButton.setIcon(ImageUtilities.image2Icon(ImageUtilities.loadImage("maltcms/ui/fileHandles/properties/resources/dots.gif")));
        GridsToggleButton.setSelected(true);
        GridsToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GridsToggleButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(GridsToggleButton);
        jToolBar1.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton1.text")); // NOI18N
        jButton1.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton1.toolTipText")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton2.text")); // NOI18N
        jButton2.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton2.toolTipText")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton3.text")); // NOI18N
        jButton3.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton3.toolTipText")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);
        jToolBar1.add(jSeparator2);

        org.openide.awt.Mnemonics.setLocalizedText(jButton5, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton5.text")); // NOI18N
        jButton5.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton5.toolTipText")); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        org.openide.awt.Mnemonics.setLocalizedText(jButton6, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton6.text")); // NOI18N
        jButton6.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton6.toolTipText")); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        org.openide.awt.Mnemonics.setLocalizedText(jButton7, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton7.text")); // NOI18N
        jButton7.setToolTipText(org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton7.toolTipText")); // NOI18N
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);
        jToolBar1.add(jSeparator3);

        org.openide.awt.Mnemonics.setLocalizedText(jButton8, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton8.text")); // NOI18N
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);
        jToolBar1.add(jSeparator4);

        jToggleButton1.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jToggleButton1.text")); // NOI18N
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);
        jToolBar1.add(jSeparator5);

        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jButton4.text")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);
        jToolBar1.add(jSeparator6);

        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton2, org.openide.util.NbBundle.getMessage(PipelineEditorTopComponent.class, "PipelineEditorTopComponent.jToggleButton2.text")); // NOI18N
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton2);

        add(jToolBar1, java.awt.BorderLayout.NORTH);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void GridsToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GridsToggleButtonActionPerformed
		if (GridsToggleButton.isSelected()) {
			this.scene.initGrids();
		} else {
			this.scene.setBackground(Color.WHITE);
			this.scene.validate();
		}
}//GEN-LAST:event_GridsToggleButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		double cz = this.scene.getZoomFactor();
		if (cz - this.zoomSteps > 0) {
			this.scene.setZoomFactor(this.scene.getZoomFactor() - this.zoomSteps);
			this.scene.validate();
		}
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		double cz = this.scene.getZoomFactor();
		if (cz + this.zoomSteps < 5) {
			this.scene.setZoomFactor(this.scene.getZoomFactor() + this.zoomSteps);
			this.scene.validate();
		}
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		this.layout.invokeLayout();
		this.scene.validate();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
		SceneLayouter.layoutHorizontal(this.scene);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
		SceneLayouter.layoutVertical(this.scene);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
		String message = this.sceneValidator.validate();
		JOptionPane.showMessageDialog(this, message,
				"Validation message", 1);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
		this.scene.setShortLabelActive(this.jToggleButton1.isSelected());
		this.scene.validate();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		SceneExporter.showSaveDialog(this.scene);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
		SceneLayouter.layoutDiagonal(this.scene);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
		if (this.jToggleButton2.isSelected()) {
			this.scene.setActiveTool(SceneMainMenu.CONNECTION_MODE);
		} else {
			this.scene.setActiveTool(SceneMainMenu.MOVE_MODE);
		}
		// TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton GridsToggleButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

	/**
	 * Gets default instance. Do not use directly: reserved for *.settings files
	 * only, i.e. deserialization routines; otherwise you could get a
	 * non-deserialized instance. To obtain the singleton instance, use
	 * {@link #findInstance}.
	 */
	public static synchronized PipelineEditorTopComponent getDefault() {
		if (instance == null) {
			instance = new PipelineEditorTopComponent();
		}
		return instance;
	}

	/**
	 * Obtain the PipelineEditorTopComponent instance. Never call
	 * {@link #getDefault} directly!
	 */
	public static synchronized PipelineEditorTopComponent findInstance() {
		TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
		if (win == null) {
			Logger.getLogger(PipelineEditorTopComponent.class.getName()).warning(
					"Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
			return getDefault();
		}
		if (win instanceof PipelineEditorTopComponent) {
			return (PipelineEditorTopComponent) win;
		}
		Logger.getLogger(PipelineEditorTopComponent.class.getName()).warning(
				"There seem to be multiple components with the '" + PREFERRED_ID
				+ "' ID. That is a potential source of errors and unexpected behavior.");
		return getDefault();
	}

	@Override
	public int getPersistenceType() {
		return TopComponent.PERSISTENCE_ONLY_OPENED;
	}

	@Override
	public void componentOpened() {
		// TODO add custom code on component opening
	}

	@Override
	public void componentClosed() {
		// TODO add custom code on component closing
	}

	void writeProperties(java.util.Properties p) {
		// better to version settings since initial version as advocated at
		// http://wiki.apidesign.org/wiki/PropertyFiles
		p.setProperty("version", "1.0");
		p.setProperty("activeFile", this.baseFile.getPath());
		// TODO store your settings
	}

	Object readProperties(java.util.Properties p) {
		if (instance == null) {
			instance = this;
		}
		instance.readPropertiesImpl(p);
		return instance;
	}

	private void readPropertiesImpl(java.util.Properties p) {
		String version = p.getProperty("version");
		if (version.equals("1.0")) {
			try {
				setBaseFile(FileUtil.createData(new File(p.getProperty("activeFile"))));
			} catch (IOException ex) {
				Exceptions.printStackTrace(ex);
			}
		}
		// TODO read your settings according to their version
	}

	public void setChanged(boolean b) {
		if (this.hasChanges && !b) {
			content.add(savable);
		} else if (b) {
			this.hasChanges = b;
			content.add(savable);
		} else {
			content.remove(savable);
		}
	}

	private class PipelineSavable implements Savable {

		@Override
		public void save() throws IOException {
			Confirmation msg = new NotifyDescriptor.Confirmation(
					"Save the pipeline configuration?",
					NotifyDescriptor.OK_CANCEL_OPTION,
					NotifyDescriptor.QUESTION_MESSAGE);
			if(DialogDisplayer.getDefault().notify(msg)==NotifyDescriptor.OK_OPTION) {
				System.out.println("Faking save action!");
				setChanged(false);
			}
		}
	}

	@Override
	protected String preferredID() {
		return PREFERRED_ID;
	}
}
