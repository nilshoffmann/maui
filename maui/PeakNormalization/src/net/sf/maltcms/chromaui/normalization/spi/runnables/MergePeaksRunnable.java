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
package net.sf.maltcms.chromaui.normalization.spi.runnables;

import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;

/**
 *
 * @author Nils Hoffmann
 */
public class MergePeaksRunnable extends AProgressAwareRunnable {

	private final List<Peak1DContainer> context;
	private final IChromAUIProject project;

	public MergePeaksRunnable(IChromAUIProject project, List<Peak1DContainer> context) {
		this.project = project;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			progressHandle.start(3);
			progressHandle.progress("Retrieving Tool Descriptors", 1);
		} catch (Exception e) {
			Exceptions.printStackTrace(e);
		} finally {
			progressHandle.finish();
		}
	}
}
