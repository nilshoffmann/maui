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
import com.db4o.consistency.ConsistencyChecker;
import com.db4o.consistency.ConsistencyReport;
import com.db4o.consistency.SlotDetail;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.foundation.Pair;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        FileChooserBuilder fcb = new FileChooserBuilder(CheckDatabaseConsistency.class);
        fcb.setDefaultWorkingDirectory(new File(System.getProperty("netbeans.projects.dir", System.getProperty("user.home"))));
        File f = fcb.showOpenDialog();
        if (f != null) {
            InputOutput io = IOProvider.getDefault().getIO("Db4o Database Consistency Check", true);
            io.select();
            try {
                ConsistencyChecker cc = new ConsistencyChecker(Db4oEmbedded.openFile(f.getAbsolutePath()));
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
                io.getOut().close();
                io.getErr().close();
            } catch (DatabaseFileLockedException dfle) {
                Logger.getLogger(CheckDatabaseConsistency.class.getName()).log(Level.WARNING, "Database file {0} is locked!", f);
            }
        }
    }
}
