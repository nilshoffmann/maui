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
package net.sf.maltcms.db.search.spi;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.Scan1D;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import net.sf.maltcms.db.search.api.IQuery;
import net.sf.maltcms.db.search.api.IQueryFactory;
import net.sf.maltcms.db.search.api.ui.DatabaseSearchPanel;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.Array;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service = IQueryFactory.class)
public class QueryFactory implements
        IQueryFactory {

    @Override
    public IQuery<IScan> createQuery(
            List<IDatabaseDescriptor> descriptors,
            RetentionIndexCalculator retentionIndexCalculator,
            AMetabolitePredicate predicate,
            double matchThreshold, int maxHits, double riWindow, IScan... scans) {
        return new ScanDatabaseQuery(descriptors, retentionIndexCalculator,
                predicate, scans,
                matchThreshold, maxHits);
    }

    @Override
    public IQuery<IScan> createQuery(IChromAUIProject project,
            AMetabolitePredicate predicate,
            double matchThreshold, int maxHits, double riWindow, IScan... scans) {
        DatabaseSearchPanel panel = new DatabaseSearchPanel(project);
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
            return createQuery(panel.getSelectedDatabases(), panel.getRetentionIndexCalculator(), panel.getSelectedMetabolitePredicate(), matchThreshold,
                    maxHits, riWindow,
                    scans);
        }
        return null;
    }

    public static IScan[] toScanArray(
            List<IPeakAnnotationDescriptor> peakAnnotationDescriptors) {
        IScan[] s = new IScan[peakAnnotationDescriptors.size()];
        for (int i = 0; i < s.length; i++) {
            IPeakAnnotationDescriptor descr = peakAnnotationDescriptors.get(i);
            s[i] = new Scan1D(Array.factory(descr.getMassValues()), Array.factory(descr.getIntensityValues()), descr.getIndex(), descr.getApexTime());
        }
        return s;
    }

    @Override
    public IQuery<IPeakAnnotationDescriptor> createQuery(IChromAUIProject project,
            List<IPeakAnnotationDescriptor> peakAnnotationDescriptors, double riWindow) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Using {0} peak annotation descriptors!", peakAnnotationDescriptors.size());
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
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            if (ddp.getSelectedDatabases().isEmpty()) {
                Logger.getLogger(getClass().getName()).info("No databases selected!");
                return null;
            }
            IQuery<IPeakAnnotationDescriptor> query = createQuery(
                    ddp.getSelectedDatabases(),
                    ddp.getRetentionIndexCalculator(), ddp.getSelectedMetabolitePredicate(), ddp.getMatchThreshold(),
                    ddp.getMaxNumberOfHits(), peakAnnotationDescriptors, riWindow);
            return query;
        }
        return null;
    }

    @Override
    public IQuery<IPeakAnnotationDescriptor> createQuery(List<IDatabaseDescriptor> descriptors, RetentionIndexCalculator retentionIndexCalculator, AMetabolitePredicate predicate, double matchThreshold, int maxHits, List<IPeakAnnotationDescriptor> peakAnnotationDescriptors, double riWindow) {
        return new PeakAnnotationDatabaseQuery(descriptors, retentionIndexCalculator,
                predicate, peakAnnotationDescriptors.toArray(new IPeakAnnotationDescriptor[peakAnnotationDescriptors.size()]),
                matchThreshold, maxHits, riWindow);
    }
}
