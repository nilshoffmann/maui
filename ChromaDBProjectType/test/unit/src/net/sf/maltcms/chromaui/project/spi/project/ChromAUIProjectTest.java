/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import junit.framework.TestCase;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnnotation;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbModuleSuite.Configuration;
import org.openide.util.Exceptions;

/**
 *
 * @author hoffmann
 */
public class ChromAUIProjectTest extends TestCase{

    public ChromAUIProjectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    public static junit.framework.Test suite() {
        Configuration config = NbModuleSuite.createConfiguration(ChromAUIProjectTest.class);
        return NbModuleSuite.create(config);//ChromAUIProjectTest.class);
   }
    
    /**
     * Test of addContainer method, of class ChromAUIProject.
     */
    @Test
    public void testAddContainer() {
        System.out.println("addContainer");
        try {
            File f;
            ChromAUIProject cap;
            File userdir = new File(System.getProperty("user.home"));
            f = new File(new File(userdir, UUID.randomUUID().toString()), "chromauiproject.db4o");
            f.getParentFile().mkdirs();
            f.createNewFile();
            f.deleteOnExit();
            f.getParentFile().deleteOnExit();
            //Db4oEmbedded.openFile(f.getAbsolutePath());


            cap = new ChromAUIProject();
            cap.activate(f.toURI().toURL());
            cap.getCrudProvider();
//            ChromatogramContainer icc = new ChromatogramContainer();
            ChromatogramDescriptor gcd1 = new ChromatogramDescriptor();
            gcd1.setResourceLocation(new File("test/a/chrom1.cdf").getAbsolutePath());
            ChromatogramDescriptor gcd2 = new ChromatogramDescriptor();
            gcd2.setResourceLocation(new File("test/a/chrom2.cdf").getAbsolutePath());
            ChromatogramDescriptor gcd3 = new ChromatogramDescriptor();
            gcd3.setResourceLocation(new File("test/b/chrom2.cdf").getAbsolutePath());
            ChromatogramDescriptor gcd4 = new ChromatogramDescriptor();
            gcd4.setResourceLocation(new File("test/b/chrom2.cdf").getAbsolutePath());
//            icc.add(gcd1, gcd2);

            TreatmentGroupContainer icg = new TreatmentGroupContainer();
            TreatmentGroupDescriptor tgd = new TreatmentGroupDescriptor("Group A");
            gcd1.setTreatmentGroup(tgd);
            gcd2.setTreatmentGroup(tgd);
            
//            Species sp1 = new Species();
//            sp1.setOntology("AREGA AREGA");
//            sp1.setPubmedId("231908123");
//            IAnnotation sa1 = new SpeciesAnnotation();
//            sa1.setAnnotation(sp1);
//            icg.addAnnotations(sa1.getClass(), sa1); 
//            tgd.add(gcd1, gcd2);
            icg.add(gcd1,gcd2);

            TreatmentGroupDescriptor tgd2 = new TreatmentGroupDescriptor("Group B");
            gcd3.setTreatmentGroup(tgd2);
            gcd4.setTreatmentGroup(tgd2);
//            tgd2.add(gcd3, gcd4);
            icg.add(gcd3,gcd4);

            IContainer[] ic = new IContainer[]{icg};
            cap.addContainer(ic);

//            Collection<ChromatogramContainer> cc = cap.getContainer(ChromatogramContainer.class);
//            System.out.println("Query returned " + cc.size() + " ChromatogramContainer");
//            for (ChromatogramContainer cont : cc) {
//                System.out.println("Container has " + cont.get() + " files");
//                for (IChromatogramDescriptor descr : cont.get()) {
//                    System.out.println(descr.getResourceLocation());
//                }
//            }

            Collection<TreatmentGroupContainer> itgc = cap.getContainer(TreatmentGroupContainer.class);
            System.out.println("Query returned " + itgc.size() + " ITreatmentGroupContainer");
            for (TreatmentGroupContainer cont : itgc) {
                System.out.println("TreatmentContainer has name: " + cont.getName());
                for (IChromatogramDescriptor descr : cont.get()) {
                    System.out.println("TreatmentGroup has name: " + descr.getTreatmentGroup().getName());
                    System.out.println(descr);
                }
//                for (IAnnotation sa:cont.getAnnotations(SpeciesAnnotation.class)) {
//                    System.out.println(sa.toString());
//                }
            }
        }catch(IOException ioex) {
            Exceptions.printStackTrace(ioex);
        } finally {
            
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
