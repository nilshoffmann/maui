/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.ui.DatabaseSearchPanel;
import net.sf.maltcms.db.search.spi.tasks.DBProjectPeaksAnnotationTask;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Actions",
id = "net.sf.maltcms.db.search.spi.actions.AnnotatePeaksOnProject")
@ActionRegistration(displayName = "#CTL_AnnotatePeaksOnProject")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView")})
@Messages("CTL_AnnotatePeaksOnProject=Search DB")
public final class AnnotatePeaksOnProject implements ActionListener {

    private final IChromAUIProject context;

    public AnnotatePeaksOnProject(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (context.getChromatograms().isEmpty()) {
            return;
        }

        DatabaseSearchPanel ddp = new DatabaseSearchPanel(context);
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
        doSearch(databases, ricalc, predicate, matchThreshold, maxNumberOfHits, riWindow, clearExistingMatches);
    }

    private void doSearch(List<IDatabaseDescriptor> databases, RetentionIndexCalculator ricalc, AMetabolitePredicate predicate, double matchThreshold, int maxNumberOfHits, double riWindow, boolean clearExistingMatches) {
        DBProjectPeaksAnnotationTask dbppat = new DBProjectPeaksAnnotationTask(context, databases, ricalc, predicate, matchThreshold, maxNumberOfHits, riWindow, clearExistingMatches);
        DBProjectPeaksAnnotationTask.createAndRun("Peak Annotation on "+context.getProjectDirectory().getName(), dbppat);
    }
}
