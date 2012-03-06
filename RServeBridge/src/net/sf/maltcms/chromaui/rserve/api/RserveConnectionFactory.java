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
    private boolean isLocalServer = false;
    private RConnection activeConnection = null;
    
    private RserveConnectionFactory() {
        Runtime.getRuntime().addShutdownHook(this);
    }
    
    public static RserveConnectionFactory getInstance() {
        return rcf;
    }
    
    public static RConnection getDefaultConnection() {
        RserveConnectionFactory factory = RserveConnectionFactory.getInstance();
        if(factory.activeConnection!=null) {
            return factory.activeConnection;
        }
        return RserveConnectionFactory.hotfixConnection();
    }
    
    private void setConnection(RConnection connection) {
        if(this.activeConnection==null) {
            this.activeConnection = connection;
        }else{
            throw new IllegalStateException("Can not reset active connection! Call RserveConnectionFactory.closeConnection() before!");
        }
    }
    
    public void closeConnection() {
        if(this.activeConnection!=null) {
            System.out.println("Closing connection to Rserve!");
            this.activeConnection.close();
            if(isLocalServer) {
                try {
                    System.out.println("Shutting down local server!");
                    this.activeConnection.shutdown();
                } catch (RserveException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            this.activeConnection = null;
        }
    }

    private static Process createFallbackRserveProcess(String cmd, String rsrvargs, String rargs, boolean debug) {
        try {
            String execString = cmd + " -e \"library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args='" + rsrvargs + "')\" " + rargs;
            System.out.println("Starting via " + execString);
            Process p = Runtime.getRuntime().exec(execString);
            return p;
        } catch (Exception e1) {
            String execString = "/bin/sh" + " -c"
                    + " echo 'library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args=\"" + rsrvargs + "\")' | " + cmd + " " + rargs;
            System.out.println("Starting via shell " + execString);
            try {
                Process p = Runtime.getRuntime().exec(execString);
                return p;
            } catch (Exception e2) {
            }
        }
        return null;
    }

    public static RConnection directConnection(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            RConnection testConnection = connect(p, false);
            return testConnection;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        throw new NullPointerException();
    }
    
    public static RConnection hotfixConnection() {
        boolean debug = false;
        String rsrvargs = "";
        String rargs = "";
        String rcmd = "/vol/r-2.13/lib/R/bin/R CMD /vol/r-2.13/lib/R/library/Rserve/libs/i386/Rserve-bin.so --vanilla";
        try {
            Process p = Runtime.getRuntime().exec(rcmd);
            RConnection testConnection = connect(p, false);
            return testConnection;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        throw new NullPointerException();
    }
    
    private static RConnection testConnection() {
        //otherwise start a new one
        String rbinaryLocation = getRBinaryLocation();
        System.out.println("R binary location: " + rbinaryLocation);
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
            Process rserveProcess = null;
            try {
                rserveProcess = pb.start();
            } catch (Exception e) {
                System.out.println("Trying fallback");
                rserveProcess = createFallbackRserveProcess(rbinaryLocation, "", "", false);
            }
            if (rserveProcess != null) {
                System.out.println(
                        "waiting for Rserve to start ... (" + rserveProcess + ")");
                // we need to fetch the output - some platforms will die if you don't ...
                RConnection testConnection = connect(rserveProcess, isWindows);
                if (testConnection == null) {
                    rserveProcess.destroy();
                    rserveProcess = createFallbackRserveProcess(rbinaryLocation, "", "", false);
                    testConnection = connect(rserveProcess,isWindows);
                }

                if (testConnection != null) {
                    rcf.isLocalServer = true;
                    Runtime.getRuntime().addShutdownHook(rcf);
                    return testConnection;
                }
            }

        } else {
            System.out.println(
                    "Could not start local instance of Rserve!");
        }
        return null;
    }

    public static RConnection connect(Process rserveProcess, boolean isWindows) {
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
        if(testConnection!=null) {
            RserveConnectionFactory.getInstance().setConnection(testConnection);
        }
        return testConnection;
    }

    @Override
    public void run() {
        closeConnection();
    }

    public static String getRBinaryLocation() {
        String osname = System.getProperty("os.name");
        if (osname != null && osname.length() >= 7 && osname.substring(0, 7).
                equals("Windows")) {
            System.out.println(
                    "Windows: querying registry to find where R is installed ...");
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
            "/opt/local/bin/R",
            "/vol/r-2.13/bin/R"
        };
        for (String location : locations) {
            if (new File(location).exists()) {
                return location;
            }
        }
        return null;
    }

    public RConnection getLocalConnection() throws RserveException {
        if(activeConnection!=null) {
            return activeConnection;
        }
        try {
            RConnection testConnection = new RConnection();
            System.out.println("Using local connection");
            setConnection(testConnection);
            return testConnection;
        } catch (RserveException re) {
            System.out.println("No local instance of RServe found, starting new one!");
        } catch (IllegalStateException ise) {
            Exceptions.printStackTrace(ise);
        }
        testConnection();
        try {
            RConnection testConnection = new RConnection();
            System.out.println("Using local connection after starting rserve locally");
            setConnection(testConnection);
            return testConnection;
        } catch (IllegalStateException ise) {
            Exceptions.printStackTrace(ise);
        }
        return null;
    }

    public RConnection getRemoteConnection(InetAddress address, int port,
            String username, String password) throws RserveException {
        RConnection connection = new RConnection(address.getHostAddress(), port);
        if (connection.needLogin()) {
            connection.login(username, password);
            setConnection(connection);
            return connection;
        }
        return null;
    }
}
