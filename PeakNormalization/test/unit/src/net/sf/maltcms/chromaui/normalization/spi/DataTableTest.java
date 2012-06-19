/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi;

import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.rosuda.REngine.REXP;

/**
 *
 * @author nilshoffmann
 */
public class DataTableTest {

    public DataTableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

//    public static DataTable createTestDataTable() {
//
//        //create treatment groups
//        ITreatmentGroupDescriptor tg1 = DescriptorFactory.newTreatmentGroupDescriptor("control");
//        ITreatmentGroupDescriptor tg2 = DescriptorFactory.newTreatmentGroupDescriptor("treatment1");
//        ITreatmentGroupDescriptor tg3 = DescriptorFactory.newTreatmentGroupDescriptor("treatment2");
//        ITreatmentGroupDescriptor[] treatmentGroups = {tg1, tg2, tg3};
//        int N = 3 * treatmentGroups.length, M = 10;
//
//        List<IChromatogramDescriptor> chromatograms = new LinkedList<IChromatogramDescriptor>();
//        for (int k = 0; k < N; k++) {
//            IChromatogramDescriptor chromDesc = DescriptorFactory.newChromatogramDescriptor("chrom-" + k, treatmentGroups[N % k], null, DescriptorFactory.newNormalizationDescriptor());
//            chromatograms.add(chromDesc);
//        }
//        IPeakNormalizer pn = new IdentityNormalizer();
//        PeakGroupContainer pgc = new PeakGroupContainer();
//        int incompleteCases = 0;
//        for (int i = 0; i < M; i++) {
//            IPeakGroupDescriptor ipgd = new PeakGroupDescriptor();
//            ipgd.setPeakGroupContainer(pgc);
//            ipgd.setIndex(i);
//            ipgd.setName(i + "");
//            ipgd.setDisplayName(ipgd.getName());
//            for ( ) {
//                int 
//            }
//            k = 3 + (int) Math.rint((Math.random() - 0.5d) * 2);
//            System.out.println("Adding " + k + " peaks to group " + i);
//            List<IPeakAnnotationDescriptor> peakAnnotations = new LinkedList<IPeakAnnotationDescriptor>();
//            for (int j = 0; j < k; j++) {
//                IPeakAnnotationDescriptor pad = new PeakAnnotationDescriptor();
//                pad.s peakAnnotations
//                .add(pad);
//            }
//            ipgd.setPeakAnnotationDescriptors(null);
//            pgc.addMembers(ipgd);
//        }
//        DataTable dt = new DataTable();
//    }

    /**
     * Test of toDataFrame method, of class DataTable.
     */
    @Test
    public void testToDataFrame() {
        System.out.println("toDataFrame");
        DataTable instance = null;
        REXP expResult = null;
        REXP result = instance.toDataFrame();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPeakGroupContainer method, of class DataTable.
     */
    @Test
    public void testGetPeakGroupContainer() {
        System.out.println("getPeakGroupContainer");
        DataTable instance = null;
        PeakGroupContainer expResult = null;
        PeakGroupContainer result = instance.getPeakGroupContainer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNormalizer method, of class DataTable.
     */
    @Test
    public void testGetNormalizer() {
        System.out.println("getNormalizer");
        DataTable instance = null;
        IPeakNormalizer expResult = null;
        IPeakNormalizer result = instance.getNormalizer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class DataTable.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        DataTable instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class DataTable.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        DataTable instance = null;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canEqual method, of class DataTable.
     */
    @Test
    public void testCanEqual() {
        System.out.println("canEqual");
        Object other = null;
        DataTable instance = null;
        boolean expResult = false;
        boolean result = instance.canEqual(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class DataTable.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        DataTable instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class DataTable.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        DataTable instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
