/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import java.io.File;
import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.IContainer;
import net.sf.maltcms.chromaui.project.api.ITreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.spi.TreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.gcgcms.GCGCMSChromatogramContainer;
import net.sf.maltcms.chromaui.project.spi.gcgcms.GCGCMSChromatogramDescriptor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author hoffmann
 */
public class ChromAUIProjectTest {

    public ChromAUIProjectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of addContainer method, of class ChromAUIProject.
     */
    @Test
    public void testAddContainer() {
        System.out.println("addContainer");

        File f;
        ChromAUIProject cap;
        f = new File("/home/hoffmann/test/db4oCrud/chromauiproject.db4o");
        f.getParentFile().mkdirs();
        f.deleteOnExit();
        if (f.exists()) {
            f.delete();
        }
        cap = new ChromAUIProject(f);

        GCGCMSChromatogramContainer icc = new GCGCMSChromatogramContainer(cap.getCrudProvider());
        GCGCMSChromatogramDescriptor gcd1 = new GCGCMSChromatogramDescriptor();
        gcd1.setResourceLocation(new File("test/a/chrom1.cdf").toURI());
        GCGCMSChromatogramDescriptor gcd2 = new GCGCMSChromatogramDescriptor();
        gcd2.setResourceLocation(new File("test/a/chrom2.cdf").toURI());
        GCGCMSChromatogramDescriptor gcd3 = new GCGCMSChromatogramDescriptor();
        gcd3.setResourceLocation(new File("test/b/chrom2.cdf").toURI());
        GCGCMSChromatogramDescriptor gcd4 = new GCGCMSChromatogramDescriptor();
        gcd4.setResourceLocation(new File("test/b/chrom2.cdf").toURI());
        icc.addInputFiles(gcd1, gcd2);

        ITreatmentGroupContainer icg = new TreatmentGroupContainer(cap.getCrudProvider());
        TreatmentGroupDescriptor tgd = new TreatmentGroupDescriptor();
        tgd.setName("Group A");
        tgd.addMembers(gcd1, gcd2);
        icg.addTreatmentGroups(tgd);

        TreatmentGroupDescriptor tgd2 = new TreatmentGroupDescriptor();
        tgd2.setName("Group B");
        tgd2.addMembers(gcd3, gcd4);
        icg.addTreatmentGroups(tgd, tgd2);

        IContainer[] ic = new IContainer[]{icc, icg};
        cap.addContainer(ic);

        Collection<GCGCMSChromatogramContainer> cc = cap.getContainer(GCGCMSChromatogramContainer.class);
        System.out.println("Query returned " + cc.size() + " GCGCMSChromatogramContainer");
        for (GCGCMSChromatogramContainer cont : cc) {
            System.out.println("Container has " + cont.getInputFiles() + " files");
            for (IChromatogramDescriptor descr : cont.getInputFiles()) {
                System.out.println(descr.getResourceLocation());
            }
        }

        Collection<TreatmentGroupContainer> itgc = cap.getContainer(TreatmentGroupContainer.class);
        System.out.println("Query returned " + itgc.size() + " ITreatmentGroupContainer");
        for (TreatmentGroupContainer cont : itgc) {
            System.out.println("TreatmentContainer has name: " + cont.getName());
            for (ITreatmentGroupDescriptor descr : cont.getTreatmentGroups()) {
                System.out.println("TreatmentGroup has name: "+descr.getName());
                System.out.println(descr.getMembers());
            }
        }

        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of removeContainer method, of class ChromAUIProject.
     */
    @Test
    public void testRemoveContainer() {
//        System.out.println("removeContainer");
//        IContainer[] ic = null;
//        ChromAUIProject instance = null;
//        instance.removeContainer(ic);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getContainer method, of class ChromAUIProject.
     */
    @Test
    public void testGetContainer() {
//        System.out.println("getContainer");
//        Class<IContainer> c = null;
//        ChromAUIProject instance = null;
//        Collection expResult = null;
//        Collection result = instance.getContainer(c);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class ChromAUIProject.
     */
    @Test
    public void testMain() {
//        System.out.println("main");
//        String[] args = null;
//        ChromAUIProject.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
