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
package net.sf.maltcms.chromaui.metabolitedb.spi.runnables;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import javax.swing.SwingUtilities;
import lombok.Data;
import net.sf.maltcms.chromaui.metabolitedb.MetaboliteDatabaseViewerTopComponent;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class MetaboliteDatabaseViewerOpenRunner extends AProgressAwareRunnable {

	private final IDatabaseDescriptor context;

	@Override
	public void run() {
		try {
			progressHandle.start();
			progressHandle.progress("Opening Database");
			Runnable r = new Runnable() {
				@Override
				public void run() {
					MetaboliteDatabaseViewerTopComponent mdvtp = new MetaboliteDatabaseViewerTopComponent();
					mdvtp.open();
					mdvtp.setDatabaseDescriptor(context);
					IRegistry registry = Lookup.getDefault().lookup(IRegistryFactory.class).getDefault();
					registry.registerTopComponentFor(context, mdvtp);
					progressHandle.finish();
				}
			};
			SwingUtilities.invokeLater(r);

		} catch (Exception e) {
			Exceptions.printStackTrace(e);
			progressHandle.finish();
		}
	}
}
