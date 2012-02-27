/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.concurrent.Executors;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.db.search.api.IQuery;
import net.sf.maltcms.db.search.api.IQueryFactory;
import net.sf.maltcms.db.search.api.IQueryResult;
import net.sf.maltcms.db.search.api.QueryResultList;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.ui.DatabaseSearchPanel;
import net.sf.maltcms.db.search.spi.tasks.DBPeakAnnotationQueryTask;
import net.sf.maltcms.db.search.spi.tasks.DBProjectPeaksAnnotationTask;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "ContainerNodeActions/Peak1DContainer",
id = "net.sf.maltcms.db.search.spi.actions.AnnotatePeaks")
@ActionRegistration(displayName = "#CTL_AnnotatePeaks")
@ActionReferences({})
@Messages("CTL_AnnotatePeaks=Search DB")
public final class AnnotatePeaks implements ActionListener {

    private final List<Peak1DContainer> context;

    public AnnotatePeaks(List<Peak1DContainer> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        final IChromAUIProject project = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
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
        doSearch(project, databases, ricalc, predicate, matchThreshold, maxNumberOfHits, riWindow, clearExistingMatches);
    }
    private void doSearch(IChromAUIProject project, List<IDatabaseDescriptor> databases, RetentionIndexCalculator ricalc, AMetabolitePredicate predicate, double matchThreshold, int maxNumberOfHits, double riWindow, boolean clearExistingMatches) {
        DBProjectPeaksAnnotationTask dbppat = new DBProjectPeaksAnnotationTask(project, databases, ricalc, predicate, matchThreshold, maxNumberOfHits, riWindow, clearExistingMatches);
        DBProjectPeaksAnnotationTask.createAndRun("Peak Annotation on "+project.getProjectDirectory().getName(), dbppat, Executors.newSingleThreadExecutor());
    }
}
