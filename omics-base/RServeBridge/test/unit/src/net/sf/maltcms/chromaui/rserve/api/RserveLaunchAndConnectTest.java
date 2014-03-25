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
package net.sf.maltcms.chromaui.rserve.api;

import java.util.Arrays;
import net.sf.maltcms.chromaui.rserve.spi.StartRserveOriginal;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Nils Hoffmann
 */
public class RserveLaunchAndConnectTest {

    /**
     * Test of getDefaultConnection method, of class RserveConnectionFactory.
     */
    @Test
    public void testGetDefaultConnection() throws RserveException, REXPMismatchException {
        StartRserveOriginal.checkLocalRserve();
        RConnection connection = new RConnection();
        try {
            REXP rexp = connection.eval("x <- seq(from=1,by=1,to=10);");
            try {
                System.out.println("Result: "
                        + Arrays.toString(rexp.asDoubles()));
            } catch (REXPMismatchException ex) {
                throw ex;
            }
        } catch (RserveException ex) {
            throw ex;
        } finally {
            try {
                connection.serverShutdown();
            } catch (RserveException re) {
                System.err.println("serverShutdown failed, trying normal shutdown!");
            }
            System.out.println("Shutting down connection!");
            connection.shutdown();
            System.out.println("Closing connection!");
            connection.close();
        }
//        try {
//            REXP rexp = connection.eval("x <- seq(from=1,by=1,to=10);");
//            try {
//                System.out.println("Result: "
//                        + Arrays.toString(rexp.asDoubles()));
//            } catch (REXPMismatchException ex) {
//                throw ex;
//            }
//        } catch (RserveException ex) {
//            throw ex;
//        } finally {
//            try{
//                connection.serverShutdown();
//            }catch(RserveException re) {
//                System.err.println("serverShutdown failed, trying normal shutdown!");
//            }
//            System.out.println("Shutting down connection!");
//            connection.shutdown();
//            System.out.println("Closing connection!");
//            connection.close();
//        } 
    }
}
