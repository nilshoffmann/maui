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
package net.sf.maltcms.db.search.spi.tasks;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.Metabolite;
import net.sf.maltcms.chromaui.db.api.CrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.MAMath;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class DBAddPeaksTask extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final IDatabaseDescriptor databaseDescriptor;
    private final List<IPeakAnnotationDescriptor> peaks;
    private boolean cancelled = false;

    @Override
    public void run() {
        getProgressHandle().start();
        getProgressHandle().setDisplayName("Adding peaks to database " + databaseDescriptor.getDisplayName());
        getProgressHandle().switchToIndeterminate();
        try {
            ICrudProvider provider = CrudProvider.getProviderFor(new File(databaseDescriptor.getResourceLocation()).toURI().toURL());
            try {
                provider.open();
                List<IMetabolite> metabolites = new ArrayList<>();
                ICrudSession session = provider.createSession();
                Collection<IMetabolite> known = session.retrieve(IMetabolite.class);
                int databaseId = 1 + known.size();
                for (IPeakAnnotationDescriptor pad : peaks) {
                    ArrayDouble.D1 masses = (ArrayDouble.D1) Array.factory(pad.getMassValues());
                    ArrayInt.D1 intensities = (ArrayInt.D1) Array.factory(pad.getIntensityValues());
                    Metabolite m = new Metabolite(
                            pad.getName(),
                            pad.getChromatogramDescriptor().getDisplayName() + "-IDX_" + pad.getChromatogramDescriptor().getChromatogram().getIndexFor(pad.getApexTime()) + "-RT_" + pad.getApexTime(),
                            "maui-user-annotation",
                            databaseId,
                            pad.getShortDescription(),
                            pad.getFormula(),
                            SimpleDateFormat.getDateTimeInstance().format(pad.getDate()),
                            pad.getRetentionIndex(),
                            pad.getApexTime(),
                            "s",
                            (int) pad.getUniqueMass(),
                            "",
                            pad.getDisplayName(),
                            masses,
                            intensities
                    );
                    MAMath.MinMax minMaxIntens = MAMath.getMinMax(intensities);
                    MAMath.MinMax minMaxMasses = MAMath.getMinMax(masses);
                    m.setMinIntensity(minMaxIntens.min);
                    m.setMaxIntensity(minMaxIntens.max);
                    m.setMinMass(minMaxMasses.min);
                    m.setMaxMass(minMaxMasses.max);
                    m.setScanIndex(pad.getChromatogramDescriptor().getChromatogram().getIndexFor(pad.getApexTime()));
                    m.setMw(pad.getUniqueMass());
                    metabolites.add(m);
                    databaseId++;
                }
                session.create(metabolites);
                session.close();
            } catch (Exception e) {
                Exceptions.printStackTrace(e);
            } finally {
                provider.close();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            getProgressHandle().finish();
        }
    }

    @Override
    public boolean cancel() {
        this.cancelled = true;
        return this.cancelled;
    }
}
