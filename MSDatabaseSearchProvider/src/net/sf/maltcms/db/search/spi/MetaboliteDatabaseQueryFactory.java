/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import java.util.List;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQuery;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryFactory;
import net.sf.maltcms.db.search.ui.DatabaseDefinitionPanel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service = IMetaboliteDatabaseQueryFactory.class)
public class MetaboliteDatabaseQueryFactory implements
        IMetaboliteDatabaseQueryFactory {

    @Override
    public IMetaboliteDatabaseQuery createQuery(
            List<IDatabaseDescriptor> descriptors,
            double matchThreshold, int maxHits, IScan... scans) {
        return new MetaboliteDatabaseQuery(descriptors, scans,
                matchThreshold, maxHits);
    }

    @Override
    public IMetaboliteDatabaseQuery createQuery(IChromAUIProject project,
            double matchThreshold, int maxHits, IScan... scans) {
        DatabaseDefinitionPanel panel = new DatabaseDefinitionPanel(project);
        // Create a custom NotifyDescriptor, specify the panel instance as a parameter + other params
        NotifyDescriptor nd = new NotifyDescriptor(
                panel, // instance of your panel
                "Available Databases", // title of the dialog
                NotifyDescriptor.OK_CANCEL_OPTION, // it is Yes/No dialog ...
                NotifyDescriptor.PLAIN_MESSAGE, // ... of a question type => a question mark icon
                null, // we have specified YES_NO_OPTION => can be null, options specified by L&F,
                // otherwise specify options as:
                //     new Object[] { NotifyDescriptor.YES_OPTION, ... etc. },
                NotifyDescriptor.OK_OPTION // default option is "Yes"
                );

        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            return createQuery(panel.getDatabaseDescriptors(), matchThreshold, maxHits, 
                    scans);
        }
        return null;
    }
}
