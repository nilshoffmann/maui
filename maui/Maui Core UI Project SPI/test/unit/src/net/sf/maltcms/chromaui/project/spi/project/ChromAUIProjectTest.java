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
package net.sf.maltcms.chromaui.project.spi.project;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import org.junit.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbModuleSuite.Configuration;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Utilities;

/**
 *
 * @author Nils Hoffmann
 */
public class ChromAUIProjectTest extends NbTestCase {

    /**
     *
     * @param name
     */
    public ChromAUIProjectTest(String name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public static junit.framework.Test suite() {
        Configuration config = NbModuleSuite.createConfiguration(ChromAUIProjectTest.class);
        config.enableClasspathModules(true);
        config.enableModules("*");
        config.gui(false);
        return config.suite();//ChromAUIProjectTest.class);
    }
//	
//	protected void setUp() throws Exception {
//        super.setUp();
////        org.netbeans.junit.MockServices.setServices(MockIToDialog.class);
//    } 

    /**
     * Test of addContainer method, of class ChromAUIProject.
     */
    @Test
    public void testAddContainer() throws Exception {
        Logger.getLogger(getClass().getName()).info("addContainer");
        IChromAUIProject cap = null;
        try {
            File f;

            File userdir = new File(System.getProperty("java.io.tmpdir"));
            f = new File(new File(userdir, UUID.randomUUID().toString()), "chromauiproject.db4o");
            f.getParentFile().mkdirs();
            f.createNewFile();
            f.deleteOnExit();
            f.getParentFile().deleteOnExit();

            cap = new ChromAUIProject();
            cap.activate(Utilities.toURI(f).toURL());
            cap.openSession();
//            cap.getCrudProvider();
//            ChromatogramContainer icc = new ChromatogramContainer();
            ChromatogramDescriptor gcd1 = new ChromatogramDescriptor();
            gcd1.setResourceLocation(new File("test/a/chrom1.cdf").getAbsolutePath());
            ChromatogramDescriptor gcd2 = new ChromatogramDescriptor();
            gcd2.setResourceLocation(new File("test/a/chrom2.cdf").getAbsolutePath());
            ChromatogramDescriptor gcd3 = new ChromatogramDescriptor();
            gcd3.setResourceLocation(new File("test/b/chrom2.cdf").getAbsolutePath());
            ChromatogramDescriptor gcd4 = new ChromatogramDescriptor();
            gcd4.setResourceLocation(new File("test/b/chrom2.cdf").getAbsolutePath());
//            icc.addMembers(gcd1, gcd2);

            TreatmentGroupContainer icg = new TreatmentGroupContainer();
            TreatmentGroupDescriptor tgd = new TreatmentGroupDescriptor();
            tgd.setName("Group A");
            gcd1.setTreatmentGroup(tgd);
            gcd2.setTreatmentGroup(tgd);

//            Species sp1 = new Species();
//            sp1.setOntology("AREGA AREGA");
//            sp1.setPubmedId("231908123");
//            IAnnotation sa1 = new SpeciesAnnotation();
//            sa1.setAnnotation(sp1);
//            icg.addAnnotations(sa1.getClass(), sa1); 
//            tgd.addMembers(gcd1, gcd2);
            icg.addMembers(gcd1, gcd2);

            TreatmentGroupDescriptor tgd2 = new TreatmentGroupDescriptor();
            tgd2.setName("Group B");
            gcd3.setTreatmentGroup(tgd2);
            gcd4.setTreatmentGroup(tgd2);
//            tgd2.addMembers(gcd3, gcd4);
            icg.addMembers(gcd3, gcd4);

            IContainer[] ic = new IContainer[]{icg};
            cap.addContainer(ic);

//            Collection<ChromatogramContainer> cc = cap.getContainer(ChromatogramContainer.class);
//            System.out.println("Query returned " + cc.size() + " ChromatogramContainer");
//            for (ChromatogramContainer cont : cc) {
//                System.out.println("Container has " + cont.getMembers() + " files");
//                for (IChromatogramDescriptor descr : cont.getMembers()) {
//                    System.out.println(descr.getResourceLocation());
//                }
//            }
            Collection<TreatmentGroupContainer> itgc = cap.getContainer(TreatmentGroupContainer.class);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Query returned {0} ITreatmentGroupContainer", itgc.size());
            for (TreatmentGroupContainer cont : itgc) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "TreatmentContainer has name: {0}", cont.getName());
                for (IChromatogramDescriptor descr : cont.getMembers()) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "TreatmentGroup has name: {0}", descr.getTreatmentGroup().getName());
                    System.out.println(descr);
                }
//                for (IAnnotation sa:cont.getAnnotations(SpeciesAnnotation.class)) {
//                    System.out.println(sa.toString());
//                }
            }
            TreatmentGroupContainer retrievedTgc = cap.getContainerById(icg.getId(), TreatmentGroupContainer.class);
            Assert.assertEquals(icg.getId(), retrievedTgc);

            IChromatogramDescriptor descr = cap.getDescriptorById(gcd1.getId(), IChromatogramDescriptor.class);
            Assert.assertEquals(gcd1.getId(), descr.getId());
        } finally {
            if (cap != null) {
                cap.closeSession();
            }
        }
        // TODO review the generated test code and removeMembers the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of removeContainer method, of class ChromAUIProject.
     */
//    @Test
//    public void testRemoveContainer() {
////        System.out.println("removeContainer");
////        IContainer[] ic = null;
////        ChromAUIProject instance = null;
////        instance.removeContainer(ic);
//        // TODO review the generated test code and removeMembers the default call to fail.
//        //fail("The test case is a prototype.");
//    }
    /**
     * Test of getContainer method, of class ChromAUIProject.
     */
//    @Test
//    public void testGetContainer() {
////        System.out.println("getContainer");
////        Class<IContainer> c = null;
////        ChromAUIProject instance = null;
////        Collection expResult = null;
////        Collection result = instance.getContainer(c);
////        assertEquals(expResult, result);
////        // TODO review the generated test code and removeMembers the default call to fail.
////        fail("The test case is a prototype.");
//    }
    /**
     * Test of main method, of class ChromAUIProject.
     */
//    @Test
//    public void testMain() {
////        System.out.println("main");
////        String[] args = null;
////        ChromAUIProject.main(args);
////        // TODO review the generated test code and removeMembers the default call to fail.
////        fail("The test case is a prototype.");
//    }
}
