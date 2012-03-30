/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.db.search.api.ri;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.db.search.api.DBConnectionManager;
import net.sf.maltcms.db.search.api.IRetentionIndexDatabase;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class RetentionIndexDatabase implements IRetentionIndexDatabase {

    private RetentionIndexCalculator ric;
    private boolean isothermal = false;

    public RetentionIndexDatabase(IDatabaseDescriptor databaseDescriptor,
            boolean isothermal) {
        if (databaseDescriptor.getType() != DatabaseType.RI) {
            throw new IllegalArgumentException(
                    "Can not use a non-ri database to calculate retention indices!");
        }
        ICrudProvider oc = null;
        try {
            oc = DBConnectionManager.getContainer(new File(databaseDescriptor.
                    getResourceLocation()).toURI().toURL());
            ICrudSession session = oc.createSession();
            session.open();
            Collection<IMetabolite> metabolites = session.retrieve(IMetabolite.class);
            IMetabolite[] metArray = new IMetabolite[metabolites.size()];
            int i = 0;
            for (IMetabolite met : metabolites) {
                metArray[i++] = met;
            }
            Arrays.sort(metArray, new Comparator<IMetabolite>() {

                @Override
                public int compare(IMetabolite t, IMetabolite t1) {
                    return Double.compare(t.getRetentionTime(), t1.
                            getRetentionTime());
                }
            });
            System.out.println("The following metabolites are used as retention indices:");
            System.out.println(Arrays.deepToString(metArray));
            ric = new RetentionIndexCalculator(metArray);
            session.close();
            oc.close();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            if(oc!=null) {
                oc.close();
            }
            throw new RuntimeException(e);
        } finally {
            if(oc!=null){
                oc.close();
            }
        }
    }

    @Override
    public RetentionIndexCalculator getRetentionIndexCalculator() {
        return ric;
    }

    @Override
    public double getRi(double rt) {
        if (isothermal) {
            return ric.getIsothermalKovatsIndex(rt);
        } else {
            return ric.getTemperatureProgrammedKovatsIndex(rt);
        }
    }
}
