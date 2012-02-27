/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.rserve.api;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import net.sf.maltcms.chromaui.rserve.spi.StreamHog;
import org.openide.util.Exceptions;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class RserveConnectionFactory extends Thread {

    private static RserveConnectionFactory rcf = new RserveConnectionFactory();
    private static boolean isLocalServer = false;

    private static RConnection launchRserve() {
        //otherwise start a new one
        String rbinaryLocation = getRBinaryLocation();

        if (rbinaryLocation != null) {
            boolean isWindows = false;
            String osname = System.getProperty("os.name");
            if (osname != null && osname.length() >= 7 && osname.substring(0,
                    7).
                    equals("Windows")) {
                isWindows = true; /* Windows startup */
            }
            System.out.println(
                    "Running Rserve " + rbinaryLocation + " CMD Rserve --vanilla");
            ProcessBuilder pb = new ProcessBuilder(rbinaryLocation,
                    "CMD",
                    "Rserve",
                    "--vanilla");
            try {
                Process rserveProcess = pb.start();
                System.out.println(
                        "waiting for Rserve to start ... (" + rserveProcess + ")");
                // we need to fetch the output - some platforms will die if you don't ...
                StreamHog errorHog = new StreamHog(
                        rserveProcess.getErrorStream(),
                        false);
                StreamHog outputHog = new StreamHog(
                        rserveProcess.getInputStream(),
                        false);
                if (!isWindows) /* on Windows the process will never return, so we cannot wait */ {
                    try {
                        rserveProcess.waitFor();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                System.out.println(
                        "call terminated, let us try to connect ...");

                RConnection testConnection = null;
                for (int i = 0; i < 5; i++) {
                    try {
                        testConnection = new RConnection();
                    } catch (RserveException ex) {
                        System.err.println(
                                "Failed to connect on try " + (1 + i) + "/5");
                    }
                }
                if (testConnection != null) {
                    isLocalServer = true;
                    Runtime.getRuntime().addShutdownHook(rcf);
                    return testConnection;
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            System.out.println(
                    "Could not start local instance of Rserve!");
        }
        return null;
    }

    @Override
    public void run() {
        if (isLocalServer) {
            try {
                RConnection testConnection = new RConnection();
                testConnection.shutdown();
            } catch (RserveException re) {
            }
        }
    }

    public static String getRBinaryLocation() {
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
                return null;
            }
            if (installPath == null) {
                System.out.println(
                        "ERROR: canot find path to R. Make sure reg is available and R was installed with registry settings.");
                return null;
            }
            return installPath + "\\bin\\R.exe";
        }
        String[] locations = new String[]{
            "/Library/Frameworks/R.framework/Resources/bin/R",
            "/usr/local/lib/R/bin/R",
            "/usr/lib/R/bin/R",
            "/usr/local/bin/R",
            "/sw/bin/R",
            "/usr/common/bin/R",
            "/opt/bin/R",
            "/opt/local/bin/R"
        };
        for (String location : locations) {
            if (new File(location).exists()) {
                return location;
            }
        }
        return null;
    }

    public static RConnection getLocalConnection() throws RserveException {
            try {
                RConnection testConnection = new RConnection();
                System.out.println("Using local connection");
                return testConnection;
            } catch (RserveException re) {
            }
            launchRserve();
            try {
                RConnection testConnection = new RConnection();
                System.out.println("Using local connection after starting rserve locally");
                return testConnection;
            } catch (RserveException re) {
            }
            return null;
    }

    public static RConnection getRemoteConnection(InetAddress address, int port,
            String username, String password) throws RserveException {
        RConnection connection = new RConnection(address.getHostAddress(), port);
        if (connection.needLogin()) {
            connection.login(username, password);
            return connection;
        }
        return null;
    }
}
