/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.db.search.spi;

import java.util.List;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQuery;
import net.sf.maltcms.db.search.api.IMetaboliteDatabaseQueryFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=IMetaboliteDatabaseQueryFactory.class)
public class MetaboliteDatabaseQueryFactory implements
        IMetaboliteDatabaseQueryFactory {

    @Override
    public IMetaboliteDatabaseQuery createQuery(
            List<IDatabaseDescriptor> databaseDescriptors,
            double matchThreshold, IScan... scans) {
        return new MetaboliteDatabaseQuery(databaseDescriptors, scans,
                matchThreshold);
    }
}
