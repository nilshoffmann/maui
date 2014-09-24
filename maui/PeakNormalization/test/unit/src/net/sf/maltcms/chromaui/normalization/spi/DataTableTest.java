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
package net.sf.maltcms.chromaui.normalization.spi;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.rosuda.REngine.REXP;

/**
 *
 * @author nilshoffmann
 */
public class DataTableTest {

    private DataTable testDataTable;

    /**
     *
     */
    @Before
    public void setUp() {
        this.testDataTable = createTestDataTable();
    }

    /**
     *
     * @return
     */
    public DataTable createTestDataTable() {

        //create treatment groups
        ITreatmentGroupDescriptor tg1 = DescriptorFactory.newTreatmentGroupDescriptor("control");
        ITreatmentGroupDescriptor tg2 = DescriptorFactory.newTreatmentGroupDescriptor("treatment1");
        ITreatmentGroupDescriptor tg3 = DescriptorFactory.newTreatmentGroupDescriptor("treatment2");
        ITreatmentGroupDescriptor[] treatmentGroups = {tg1, tg2, tg3};
        int N = 3 * treatmentGroups.length, M = 10;

        List<IChromatogramDescriptor> chromatograms = new LinkedList<>();
        for (int k = 0; k < N; k++) {
            int groupIdx = k % 3;
            IChromatogramDescriptor chromDesc = DescriptorFactory.newChromatogramDescriptor("chrom-" + k, treatmentGroups[groupIdx], null, DescriptorFactory.newNormalizationDescriptor());
            chromatograms.add(chromDesc);
        }
        IPeakNormalizer pn = new IdentityNormalizer();
        PeakGroupContainer pgc = new PeakGroupContainer();
        for (int i = 0; i < M; i++) {
            IPeakGroupDescriptor ipgd = DescriptorFactory.newPeakGroupDescriptor("testPeakGroup" + i);
            ipgd.setPeakGroupContainer(pgc);
            ipgd.setIndex(i);
            ipgd.setName(i + "");
            ipgd.setDisplayName(ipgd.getName());
            int k = (int) Math.rint(Math.random() * N);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding {0} peaks to group {1}", new Object[]{k, i});
            List<IPeakAnnotationDescriptor> peakAnnotations = new LinkedList<>();
            for (int j = 0; j < k; j++) {
                IPeakAnnotationDescriptor pad = DescriptorFactory.newPeakAnnotationDescriptor(chromatograms.get(j), "peak" + j + " in row " + i, 0, new double[0], Double.NaN, Double.NaN, Double.NaN, Double.NaN, "", "", "", "", Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
                peakAnnotations.add(pad);
            }
            ipgd.setPeakAnnotationDescriptors(peakAnnotations);
            pgc.addMembers(ipgd);
        }
        DataTable dt = new DataTable(pgc, pn, "TestDataTable", DataTable.ImputationMode.ZERO);
        return dt;
    }

    /**
     * Test of toDataFrame method, of class DataTable.
     */
    @Test
    public void testToDataFrame() throws Exception {
        DataTable instance = createTestDataTable();
        REXP result = instance.toDataFrame();
        assertNotNull(result);
    }
}
