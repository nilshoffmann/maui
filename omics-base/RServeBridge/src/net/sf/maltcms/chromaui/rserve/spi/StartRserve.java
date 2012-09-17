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
package net.sf.maltcms.chromaui.rserve.spi;

import java.io.*;
import org.rosuda.REngine.Rserve.RConnection;

/**
 * simple class that start Rserve locally if it's not running already - see
 * mainly
 * <code>checkLocalRserve</code> method. It spits out quite some debugging
 * outout of the console, so feel free to modify it for your application if
 * desired.<p> <i>Important:</i> All applications should shutdown every Rserve
 * that they started! Never leave Rserve running if you started it after your
 * application quits since it may pose a security risk. Inform the user if you
 * started an Rserve instance.
 */
public class StartRserve {

    /**
     * shortcut to
     * <code>launchRserve(cmd, "--no-save --slave", "--no-save --slave", false)</code>
     */
    public static boolean launchRserve(String cmd) {
        return launchRserve(cmd, "--no-save --slave", "--no-save --slave", false);
    }

    /**
     * attempt to start Rserve. Note: parameters are <b>not</b> quoted, so avoid
     * using any quotes in arguments
     *
     * @param cmd command necessary to start R
     * @param rargs arguments are are to be passed to R
     * @param rsrvargs arguments to be passed to Rserve
     * @return
     * <code>true</code> if Rserve is running or was successfully started,
     * <code>false</code> otherwise.
     */
    public static boolean launchRserve(String cmd, String rargs, String rsrvargs,
            boolean debug) {
        try {
            Process p;
            boolean isWindows = false;
            String osname = System.getProperty("os.name");
            if (osname != null && osname.length() >= 7 && osname.substring(0, 7).
                    equals("Windows")) {
                isWindows = true; /*
                 * Windows startup
                 */
                p = Runtime.getRuntime().exec(
                        "\"" + cmd + "\" -e \"library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args='" + rsrvargs + "')\" " + rargs);
            } else /*
             * unix startup
             */ {
                try {
                    String execString = cmd + " CMD Rserve " + rargs;
                    System.out.println("Starting via " + execString);
                    p = Runtime.getRuntime().exec(execString);
                } catch (Exception e) {
                    try {
                        String execString = cmd + " -e \"library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args='" + rsrvargs + "')\" " + rargs;
                        System.out.println("Starting via " + execString);
                        p = Runtime.getRuntime().exec(execString);
                    } catch (Exception e1) {
                        String execString = "/bin/sh" + " -c"
                                + " echo 'library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args=\"" + rsrvargs + "\")' | " + cmd + " " + rargs;
                        System.out.println("Starting via " + execString);
                        p = Runtime.getRuntime().exec(execString);
                    }
                }
//                p = Runtime.getRuntime().exec(
//                            "/bin/sh"+" -c"+
//                            " echo 'library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args=\"" + rsrvargs + "\")' | " + cmd + " " + rargs
//                        );
            }
            System.out.println("waiting for Rserve to start ... (" + p + ")");
            // we need to fetch the output - some platforms will die if you don't ...
            StreamHog errorHog = new StreamHog(p.getErrorStream(), false);
            StreamHog outputHog = new StreamHog(p.getInputStream(), false);
            if (!isWindows) /*
             * on Windows the process will never return, so we cannot wait
             */ {
                p.waitFor();
            }
            System.out.println("call terminated, let us try to connect ...");
        } catch (Exception x) {
            System.out.println("failed to start Rserve process with " + x.getMessage());
            return false;
        }
        int attempts = 5; /*
         * try up to 5 times before giving up. We can be conservative here,
         * because at this point the process execution itself was successful and
         * the start up is usually asynchronous
         */
        while (attempts > 0) {
            try {
                RConnection c = new RConnection();
                System.out.println("Rserve is running.");
                c.close();
                return true;
            } catch (Exception e2) {
                System.out.println("Try failed with: " + e2.getMessage());
            }
            /*
             * a safety sleep just in case the start up is delayed or
             * asynchronous
             */
            try {
                Thread.sleep(500);
            } catch (InterruptedException ix) {
            };
            attempts--;
        }
        return false;
    }

    /**
     * checks whether Rserve is running and if that's not the case it attempts
     * to start it using the defaults for the platform where it is run on. This
     * method is meant to be set-and-forget and cover most default setups. For
     * special setups you may get more control over R with <<code>launchRserve</code>
     * instead.
     */
    public static boolean checkLocalRserve() {
        if (isRserveRunning()) {
            return true;
        }
        String osname = System.getProperty("os.name");
        if (osname != null && osname.length() >= 7 && osname.substring(0, 7).
                equals("Windows")) {
            System.out.println(
                    "Windows: query registry to find where R is installed ...");
            String installPath = null;
            try {
                Process rp = Runtime.getRuntime().exec(
                        "reg query HKLM\\Software\\R-core\\R");
                StreamHog regHog = new StreamHog(rp.getInputStream(), true);
                rp.waitFor();
                regHog.join();
                installPath = regHog.getInstallPath();
            } catch (Exception rge) {
                System.out.println(
                        "ERROR: unable to run REG to find the location of R: " + rge);
                return false;
            }
            if (installPath == null) {
                System.out.println(
                        "ERROR: canot find path to R. Make sure reg is available and R was installed with registry settings.");
                return false;
            }
            return launchRserve(installPath + "\\bin\\R.exe");
        }
        if (launchRserve("R")) {
            return true;
        } else {
//            return ((new File("/opt/bin/R")).exists() && launchRserve(
//                "/opt/bin/R"));
            return (((new File("/opt/bin/R")).exists() && launchRserve(
                    "/opt/bin/R")) || /*
                     * try some common unix locations of R
                     */ ((new File(
                    "/Library/Frameworks/R.framework/Resources/bin/R")).exists() && launchRserve(
                    "/Library/Frameworks/R.framework/Resources/bin/R"))
                    || ((new File("/usr/local/lib/R/bin/R")).exists() && launchRserve(
                    "/usr/local/lib/R/bin/R"))
                    || ((new File("/usr/lib/R/bin/R")).exists() && launchRserve(
                    "/usr/lib/R/bin/R"))
                    || ((new File("/usr/local/bin/R")).exists() && launchRserve(
                    "/usr/local/bin/R"))
                    || ((new File("/sw/bin/R")).exists() && launchRserve("/sw/bin/R"))
                    || ((new File("/usr/common/bin/R")).exists() && launchRserve(
                    "/usr/common/bin/R"))
                    || ((new File("/opt/bin/R")).exists() && launchRserve(
                    "/opt/bin/R"))
                    );
        }
    }

    /**
     * check whether Rserve is currently running (on local machine and default
     * port).
     *
     * @return
     * <code>true</code> if local Rserve instance is running,
     * <code>false</code> otherwise
     */
    public static boolean isRserveRunning() {
        try {
            RConnection c = new RConnection();
            System.out.println("Rserve is running.");
            c.close();
            return true;
        } catch (Exception e) {
            System.out.println(
                    "First connect try failed with: " + e.getMessage());
        }
        return false;
    }

    /**
     * just a demo main method which starts Rserve and shuts it down again
     */
    public static void main(String[] args) {
        System.out.println("result=" + checkLocalRserve());
        try {
            RConnection c = new RConnection();
            c.shutdown();
        } catch (Exception x) {
        };
    }
}
