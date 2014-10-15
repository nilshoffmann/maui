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
package net.sf.maltcms.chromaui.db.spi.db4o.actions;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.consistency.ConsistencyChecker;
import com.db4o.consistency.ConsistencyReport;
import com.db4o.consistency.SlotDetail;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.InvalidIDException;
import com.db4o.ext.StoredClass;
import com.db4o.foundation.Pair;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.NoAuthCredentials;
import net.sf.maltcms.chromaui.db.spi.db4o.DB4oCrudProvider;
import net.sf.maltcms.chromaui.ui.support.api.Projects;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

@ActionID(
        category = "Tools",
        id = "net.sf.maltcms.chromaui.db.spi.db4o.actions.CheckDatabaseConsistency")
@ActionRegistration(
        displayName = "#CTL_CheckDatabaseConsistency")
@ActionReference(path = "Menu/Tools", position = 0, separatorAfter = 50)
@Messages("CTL_CheckDatabaseConsistency=Check Database Consistency")
public final class CheckDatabaseConsistency implements ActionListener {

    private boolean isConsistent(File dbFile, InputOutput io) {
        EmbeddedObjectContainer container = null;
        try {
            container = Db4oEmbedded.openFile(dbFile.getAbsolutePath());
            ConsistencyChecker cc = new ConsistencyChecker(container);
            ConsistencyReport cr = cc.checkSlotConsistency();
            if (cr.consistent()) {
                io.getOut().println("Database is consistent");
            } else {
                io.getErr().println("Database is not consistent!");
                if (cr.dupes().size() > 0) {
                    io.getErr().println("Found the following duplicates:");
                    for (Pair<SlotDetail, SlotDetail> p : cr.dupes()) {
                        io.getErr().println("Duplicate: ");
                        io.getErr().println("\t" + p.first + "\t" + p.second);
                    }
                }
                if (cr.overlaps().size() > 0) {
                    io.getErr().println("Found the following overlaps:");
                    for (Pair<SlotDetail, SlotDetail> p : cr.overlaps()) {
                        io.getErr().println("Overlap: ");
                        io.getErr().println("\t" + p.first + "\t" + p.second);
                    }
                }
                io.getOut().println(cr.toString());
            }
            return cr.consistent();
        } catch (Exception e) {
            Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.WARNING, "Caught exception while opening database " + dbFile, e);
            throw new RuntimeException(e);
        } finally {
            if (container != null) {
                try {
                    container.close();
                } catch (Db4oIOException e) {
                    Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.WARNING, "Caught exception while closing database " + dbFile, e);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Collection<? extends Project> projects = Projects.getSelectedOpenProject(Project.class, "Select Project to check", "Project");
        if (projects.isEmpty()) {
            Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.INFO, "No project selected, returning!");
            return;
        }
        Project project = projects.iterator().next();
        if (OpenProjects.getDefault().isProjectOpen(project)) {
            OpenProjects.getDefault().close(new Project[]{project});
        }
        InputOutput io = null;
        try {
            io = IOProvider.getDefault().getIO("Db4o Database Consistency Check", true);
            io.select();
            File f = new File(FileUtil.toFile(project.getProjectDirectory()), "maui.mpr");
            if (f.canRead()) {
                Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.INFO, "Database file {0} is accessible!", f);
            } else {
                Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.WARNING, "Database file {0} is not readable!", f);
                NotifyDescriptor ndd2 = new NotifyDescriptor.Confirmation("Database at " + f.getAbsolutePath() + " is not a valid Maui database (.mpr)!", "Invalid database file", NotifyDescriptor.DEFAULT_OPTION, NotifyDescriptor.WARNING_MESSAGE);
                DialogDisplayer.getDefault().notify(ndd2);
                return;
            }
            try {
                if (!isConsistent(f, io)) {
                    NotifyDescriptor ndd = new NotifyDescriptor.Confirmation("Database at " + f.getAbsolutePath() + " is inconsistent!\nAttempt to fix?\nSelecting 'No' will cancel all other attempts!", "Attempt fix of corrupt db?", NotifyDescriptor.YES_NO_OPTION, NotifyDescriptor.QUESTION_MESSAGE);
                    Object obj = DialogDisplayer.getDefault().notify(ndd);
                    if (obj.equals(NotifyDescriptor.OK_OPTION)) {
                        DB4oCrudProvider icpSource;
                        DB4oCrudProvider icpTarget;
                        icpSource = new DB4oCrudProvider(f,
                                new NoAuthCredentials(), Lookup.getDefault().lookup(
                                        ClassLoader.class));
                        icpSource.open();
                        File repairBackupFile = new File(f.getAbsolutePath(), ".repBackup");
                        icpTarget = new DB4oCrudProvider(repairBackupFile,
                                new NoAuthCredentials(), Lookup.getDefault().lookup(
                                        ClassLoader.class));
                        icpTarget.open();
                        try {
                            copyDatabase(io, icpSource.getObjectContainer(), icpTarget.getObjectContainer());
                            if (isConsistent(repairBackupFile, io)) {
                                io.getOut().println("Database is consistent after copying! Replacing original database with repaired one!");
                                icpSource.close();
                                icpTarget.close();
                                f.delete();
                                repairBackupFile.renameTo(f);
                                //restore project that was initially open
                                OpenProjects.getDefault().open(new Project[]{project}, false, true);
                            } else {
                                NotifyDescriptor ndd2 = new NotifyDescriptor.Confirmation("Database at " + f.getAbsolutePath() + " could not be repaired!", "Could not repair database", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.ERROR_MESSAGE);
                                DialogDisplayer.getDefault().notify(ndd2);
                            }
                        } finally {
                            //close, just in case
                            icpSource.close();
                            icpTarget.close();
                        }
                    }
                } else {
                    //restore project that was initially open
                    OpenProjects.getDefault().open(new Project[]{project}, false, true);
                }
            } catch (DatabaseFileLockedException dfle) {
                Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.WARNING, "Database file " + f + " is locked!", dfle);
            } catch (RuntimeException re) {
                Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.WARNING, "Caught runtime exception while checking database consistency!", re);
            }
        } finally {
            if (io != null) {
                io.getOut().close();
                io.getErr().close();
            }
        }
    }

    private void copyDatabase(InputOutput io, ObjectContainer sourceContainer, ObjectContainer targetContainer) {
        for (StoredClass storedClass : sourceContainer.ext().storedClasses()) {
            final long[] ids = storedClass.getIDs();
            for (long id : ids) {
                try {
                    final Object object = sourceContainer.ext().getByID(id);
                    sourceContainer.ext().activate(object, 1);
                    // store the person to another database
                    targetContainer.store(object);
                    io.getOut().println("Copying object of type " + storedClass.getName() + " with id " + id);
                    Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.INFO, "Copying object {0}", object);
                } catch (DatabaseClosedException | InvalidIDException | Db4oIOException e) {
                    io.getErr().println("Could not copy object of type " + storedClass.getName() + " with id " + id + "! Cause: " + e.getLocalizedMessage());
                    Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.WARNING, "Could not copy object of type {0} with id {1}", new Object[]{storedClass.getName(), id});
                }
            }
        }
    }
}
