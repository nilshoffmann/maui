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
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;

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
