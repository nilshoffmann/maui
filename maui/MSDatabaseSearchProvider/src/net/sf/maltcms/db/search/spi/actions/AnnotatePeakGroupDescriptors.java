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
package net.sf.maltcms.db.search.spi.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Executors;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.ui.DatabaseSearchPanel;
import net.sf.maltcms.db.search.spi.tasks.DBPeakGroupAnnotationTask;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Maui",
id = "net.sf.maltcms.db.search.spi.actions.AnnotatePeakGroupDescriptors")
@ActionRegistration(displayName = "#CTL_AnnotatePeakGroupDescriptors")
@ActionReferences({@ActionReference(path="Actions/DescriptorNodeActions/IPeakGroupDescriptor")})
@Messages("CTL_AnnotatePeakGroupDescriptors=Search Peaks in DB")
public final class AnnotatePeakGroupDescriptors implements ActionListener {

    private final List<IPeakGroupDescriptor> context;

    public AnnotatePeakGroupDescriptors(List<IPeakGroupDescriptor> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        final IChromAUIProject project = LookupUtils.ensureSingle(Utilities.actionsGlobalContext(), IChromAUIProject.class);
        if (project == null) {
            throw new NullPointerException("No IChromAUI project instance in lookup!");
        }
        DatabaseSearchPanel ddp = new DatabaseSearchPanel(project);
        // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
        NotifyDescriptor nd = new NotifyDescriptor(
                ddp, // instance of your panel
                "Select Databases and Settings", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );
        ddp.updateView();
        List<IDatabaseDescriptor> databases;
        RetentionIndexCalculator ricalc;
        AMetabolitePredicate predicate;
        double matchThreshold;
        int maxNumberOfHits;
        double riWindow;
        boolean clearExistingMatches = false;
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            if (ddp.getSelectedDatabases().isEmpty()) {
                System.out.println("No databases selected!");
                return;
            }
            databases = ddp.getSelectedDatabases();
            ricalc = ddp.getRetentionIndexCalculator();
            predicate = ddp.getSelectedMetabolitePredicate();
            matchThreshold = ddp.getMatchThreshold();
            maxNumberOfHits = ddp.getMaxNumberOfHits();
            riWindow = ddp.getRIWindow();
            clearExistingMatches = ddp.isClearExistingMatches();
        } else {
            return;
        }
        doSearch(context, project, databases, ricalc, predicate, matchThreshold, maxNumberOfHits, riWindow, clearExistingMatches);
    }
    private void doSearch(List<IPeakGroupDescriptor> context, IChromAUIProject project, List<IDatabaseDescriptor> databases, RetentionIndexCalculator ricalc, AMetabolitePredicate predicate, double matchThreshold, int maxNumberOfHits, double riWindow, boolean clearExistingMatches) {
        DBPeakGroupAnnotationTask dbppat = new DBPeakGroupAnnotationTask(context, databases, ricalc, predicate, matchThreshold, maxNumberOfHits, riWindow, clearExistingMatches);
        DBPeakGroupAnnotationTask.createAndRun("Peak Annotation on "+project.getProjectDirectory().getName(), dbppat, Executors.newSingleThreadExecutor());
    }
}
