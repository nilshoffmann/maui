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
package net.sf.maltcms.chromaui.statistics.pcaViewer.tasks;

import de.unibielefeld.cebitec.lstutz.pca.data.ParserUtilities;
import de.unibielefeld.cebitec.lstutz.pca.data.PcaDescriptorAdapter;
import de.unibielefeld.cebitec.lstutz.pca.visual.StandardGUI;
import javax.swing.SwingUtilities;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;
import net.sf.maltcms.chromaui.statistics.pcaViewer.PCAViewerTopComponent;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class PCAOpenActionRunnable extends AProgressAwareRunnable {

	private final IPcaDescriptor pcaDescriptor;

	@Override
	public void run() {
		ProgressHandle handle = ProgressHandleFactory.createHandle(
				"Creating 1D Chromatogram plot");
		try {
			handle.setDisplayName("Loading pca data");//+new File(this.files.getResourceLocation()).getName());
			handle.progress("Initializing...");
			String name = "MeltDB 3D Viewer";

			final StandardGUI gui = new StandardGUI(name, ParserUtilities.group_data(new PcaDescriptorAdapter().parse_data(pcaDescriptor, 0, 1, 2)));
			handle.progress("Creating view...");
			Runnable r = new Runnable() {
				@Override
				public void run() {
					PCAViewerTopComponent pvtc = new PCAViewerTopComponent();
					pvtc.setProject(pcaDescriptor.getProject());
					pvtc.setData(gui);
					pvtc.open();
					pvtc.requestActive();
				}
			};
			SwingUtilities.invokeLater(r);
		} finally {
			handle.finish();
		}
	}
}
