/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi;

import org.junit.Test;
import org.netbeans.junit.NbModuleSuite;
import net.sf.maltcms.chromaui.db.spi.db4o.DB4oCrudProvider;
import net.sf.maltcms.chromaui.db.api.NoAuthCredentials;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.io.File;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import org.netbeans.junit.NbTestCase;
import static org.junit.Assert.*;

/**
 *
 * @author hoffmann
 */
public class DB4oCrudProviderTest extends NbTestCase {

    public class IntStringTuple {

        private Integer first;
        private String second;
        
        public IntStringTuple(Integer itg, String str) {
            this.first = first;
            this.second = second;
        }

        public Integer getFirst() {
            return first;
        }

        public void setFirst(Integer first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }

    class Tuple2D<T,U> {
        private T first;
        private U second;
        public Tuple2D(T t, U u) {
            this.first = t;
            this.second = u;
        }

        public T getFirst() {
            return first;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        public U getSecond() {
            return second;
        }

        public void setSecond(U second) {
            this.second = second;
        }

        @Override
        public String toString() {
            return "["+first+","+second+"]";
        }
    }

    public DB4oCrudProviderTest() {
    }

    public static junit.framework.Test suite() {
        return NbModuleSuite.create(NbModuleSuite.createConfiguration(DB4oCrudProviderTest.class));
    }

//    public DB4oCrudProviderTest() {
//    }
    /**
     * Test of create method, of class DB4oCrudProvider.
     */
    @Test
    public void testCreate() {

        //BEGIN SETUP, DO NOT COPY
        IntStringTuple a = new IntStringTuple(1, "Have");
        IntStringTuple b = new IntStringTuple(2, "a");
        IntStringTuple c = new IntStringTuple(3, "nice");
        File f = new File("test/db4oCrud/chromauiproject.db4o");
        f.getParentFile().mkdirs();
        f.deleteOnExit();
        if (f.exists()) {
            f.delete();
        }
        //END SETUP
        System.out.println("Testing project in " + f.getAbsolutePath());
        //Credentials
        ICredentials ic = new NoAuthCredentials();
        //CrudProvider
        DB4oCrudProvider instance = new DB4oCrudProvider(f, ic, this.getClass().
                getClassLoader());
        try {
            //initialize and open database
            instance.open();

            System.out.println("create");
            //Create a new session
            ICrudSession icr = instance.createSession();
            try {
                //CREATE
                icr.create(Arrays.asList(a, b, c));
                System.out.println("retrieve");
                Collection<IntStringTuple> expResult = Arrays.asList(a, b, c);
                //RETRIEVE
                Collection<IntStringTuple> result = icr.retrieve(
                        IntStringTuple.class);
                System.out.println(expResult);
                System.out.println(result);
                Collections.sort(new ArrayList<IntStringTuple>(result),
                        new ComparatorImpl());
                assertEquals(expResult, result);
                //UPDATE
                b.setSecond("b");
                icr.update(Arrays.asList(b));
                result = icr.retrieve(IntStringTuple.class);
                System.out.println(expResult);
                System.out.println(result);
                Collections.sort(new ArrayList<IntStringTuple>(result),
                        new ComparatorImpl());
                assertEquals(expResult, result);

                //DELETE
                expResult = Arrays.asList(b, c);
                icr.delete(Arrays.asList(a));
                result = icr.retrieve(IntStringTuple.class);
                System.out.println(expResult);
                System.out.println(result);
                Collections.sort(new ArrayList<IntStringTuple>(result),
                        new ComparatorImpl());
                assertEquals(expResult, result);
            } finally {
                icr.close();
            }

        } finally {
            instance.close();
        }


    }

    /**
     * Test of update method, of class DB4oCrudProvider.
     */
    @Test
    public void testUpdate() {
//        System.out.println("update");
//        Collection<? extends Object> o = null;
//        ICredentials ic = null;
//        DB4oCrudProvider instance = null;
//        instance.update(o, ic);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class DB4oCrudProvider.
     */
    @Test
    public void testDelete() {
//        System.out.println("delete");
//        Collection<? extends Object> o = null;
//        ICredentials ic = null;
//        DB4oCrudProvider instance = null;
//        instance.delete(o, ic);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieve method, of class DB4oCrudProvider.
     */
    @Test
    public void testRetrieve() {
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getSODAQuery method, of class DB4oCrudProvider.
     */
    @Test
    public void testGetSODAQuery() {
//        System.out.println("getSODAQuery");
//        ICredentials ic = null;
//        DB4oCrudProvider instance = null;
//        Query expResult = null;
//        Query result = instance.getSODAQuery(ic);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    private static class ComparatorImpl implements Comparator<IntStringTuple> {

        @Override
        public int compare(IntStringTuple o1, IntStringTuple o2) {
            if (o1.getFirst() instanceof Integer && o2.getFirst() instanceof Integer) {
                Integer i1 = (Integer) o1.getFirst();
                Integer i2 = (Integer) o2.getFirst();
                return i1.compareTo(i2);
            }
            return 0;
        }
    }
}
