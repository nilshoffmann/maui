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
package net.sf.maltcms.db.search.spi.tasks;

import cross.tools.StringTools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.db.api.CrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.chromaui.ui.support.api.FileTools;
import org.apache.log4j.spi.LoggingEvent;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class DBCreateTask extends AProgressAwareRunnable {

	private final IChromAUIProject project;
	private final DatabaseContainer databaseContainer;
	private final String databaseName;
	private final DatabaseType databaseType;
	private final List<Double> maskedMasses;
	private boolean cancelled = false;

	private boolean overwriteFile(File fo) {
		if (fo.exists()) {
			NotifyDescriptor.Confirmation ndc = new NotifyDescriptor.Confirmation(
					"Can not import database! File with the same name already exists at: " + fo.getPath() + " Delete?", "Database exists", NotifyDescriptor.YES_NO_OPTION,
					NotifyDescriptor.WARNING_MESSAGE);
			Object o = DialogDisplayer.getDefault().notify(ndc);
			if (o.equals(NotifyDescriptor.OK_OPTION)) {
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		getProgressHandle().setDisplayName("Creating " + databaseType + " database " + databaseName);
		getProgressHandle().switchToIndeterminate();
		List<FileObject> createdFiles = new LinkedList<FileObject>();
		IDatabaseDescriptor databaseDescriptor = null;
		try {
			FileObject baseDir = FileUtil.createFolder(project.getProjectDirectory(),
					"databases");
			String name = StringTools.deBlank(databaseName, "_");
			File dbFile1 = new File(FileUtil.toFile(baseDir), name + ".db4o");
//			int unit = 0;
			if (cancelled) {
				return;
			}

			if (overwriteFile(dbFile1)) {
				FileObject dbFile = FileUtil.createData(dbFile1);
				createdFiles.add(dbFile);

				ICrudProvider provider = CrudProvider.getProviderFor(
						FileUtil.toFile(dbFile).toURI().toURL());
				try {
					provider.open();
					databaseDescriptor = DescriptorFactory.newDatabaseDescriptor(
							FileUtil.toFile(dbFile).getAbsolutePath(),
							databaseType);
					databaseDescriptor.setMaskedMasses(maskedMasses);
					databaseContainer.addMembers(databaseDescriptor);
				} catch (Exception e) {
					Exceptions.printStackTrace(e);
				} finally {
					provider.close();
				}
			}

			if (cancelled) {
				if (databaseDescriptor != null) {
					databaseContainer.removeMembers(databaseDescriptor);
				}
				for (FileObject file : createdFiles) {
					file.delete();
				}
			}
		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);

			//undo actions
			if (databaseDescriptor != null) {
				databaseContainer.removeMembers(databaseDescriptor);
			}
			for (FileObject file : createdFiles) {
				try {
					file.delete();
				} catch (IOException ex1) {
					Exceptions.printStackTrace(ex1);
				}
			}
		} finally {
			getProgressHandle().finish();
		}
	}

	@Override
	public boolean cancel() {
		this.cancelled = true;
		return this.cancelled;
	}
}
