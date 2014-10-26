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
package net.sf.maltcms.chromaui.project.spi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.NoAuthCredentials;
import net.sf.maltcms.chromaui.db.spi.db4o.DB4oCrudProvider;
import org.junit.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;

/**
 *
 * @author Nils Hoffmann
 */
public class DB4oCrudProviderTest extends NbTestCase {

    /**
     *
     */
    public class IntStringTuple {

        private Integer first;
        private String second;

        /**
         *
         * @param itg
         * @param str
         */
        public IntStringTuple(Integer itg, String str) {
            this.first = itg;
            this.second = str;
        }

        /**
         *
         * @return
         */
        public Integer getFirst() {
            return first;
        }

        /**
         *
         * @param first
         */
        public void setFirst(Integer first) {
            this.first = first;
        }

        /**
         *
         * @return
         */
        public String getSecond() {
            return second;
        }

        /**
         *
         * @param second
         */
        public void setSecond(String second) {
            this.second = second;
        }
    }

    class Tuple2D<T, U> {

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
            return "[" + first + "," + second + "]";
        }
    }

    /**
     *
     * @param name
     */
    public DB4oCrudProviderTest(String name) {
        super(name);
    }

//    public DB4oCrudProviderTest() {
//    }

    /**
     *
     * @return
     */
        public static junit.framework.Test suite() {
        return NbModuleSuite.create(NbModuleSuite.createConfiguration(DB4oCrudProviderTest.class));
    }

//    public DB4oCrudProviderTest() {
//    }
    /**
     * Test of db operations
     */
    @Test
    public void testDbOperations() {

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
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Testing project in {0}", f.getAbsolutePath());
        //Credentials
        ICredentials ic = new NoAuthCredentials();
        //CrudProvider
        ICrudProvider instance = new DB4oCrudProvider(f, ic, this.getClass().
                getClassLoader());
        try {
            //initialize and open database
            instance.open();

            Logger.getLogger(getClass().getName()).info("create");
            //Create a new session
            ICrudSession icr = instance.createSession();
            try {
                //CREATE
                icr.create(Arrays.asList(a, b, c));
                Logger.getLogger(getClass().getName()).info("retrieve");
                Collection<IntStringTuple> expResult = Arrays.asList(a, b, c);
                //RETRIEVE
                Collection<IntStringTuple> result = icr.retrieve(
                        IntStringTuple.class);
                System.out.println(expResult);
                System.out.println(result);
                Collections.sort(new ArrayList<>(result),
                        new ComparatorImpl());
                assertEquals(expResult, result);
                //UPDATE
                b.setSecond("b");
                icr.update(Arrays.asList(b));
                result = icr.retrieve(IntStringTuple.class);
                System.out.println(expResult);
                System.out.println(result);
                Collections.sort(new ArrayList<>(result),
                        new ComparatorImpl());
                assertEquals(expResult, result);

                //DELETE
                expResult = Arrays.asList(b, c);
                icr.delete(Arrays.asList(a));
                result = icr.retrieve(IntStringTuple.class);
                System.out.println(expResult);
                System.out.println(result);
                Collections.sort(new ArrayList<>(result),
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
