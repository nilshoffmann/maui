/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.rserve.api;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.naming.AuthenticationNotSupportedException;
import net.sf.maltcms.chromaui.rserve.spi.StreamHog;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class RserveConnectionFactory extends Thread implements PreferenceChangeListener {

    public static final String KEY_RBINARY_LOCATIONS = "rbinaryLocations";
    private static RserveConnectionFactory rcf = new RserveConnectionFactory();
    private boolean isLocalServer = false;
    private RConnection activeConnection = null;
    public static final String defaultUnixRlocations = "/Library/Frameworks/R.framework/Resources/bin/R,"
            + "/usr/local/lib/R/bin/R,"
            + "/usr/lib/R/bin/R,"
            + "/usr/local/bin/R,"
            + "/sw/bin/R,"
            + "/usr/common/bin/R,"
            + "/opt/bin/R,"
            + "/opt/local/bin/R,"
            + "/vol/r-2.13/bin/R";
    public static final String KEY_RSERVECALLS = "rserveCalls";
    public static final String defaultRserveCalls = "Rserve,/vol/r-2.13/lib/R/library/Rserve/libs/i386/Rserve-bin.so";
    public static final String KEY_RARGS = "rargs";
    public static final String defaultRargs = "--vanilla --slave";
    public static final String KEY_RSERVEARGS = "rserveArgs";
    public static final String defaultRserveArgs = "";
    public static final String KEY_RBINARY_LOCATION = "rBinaryLocation";
    public static final String KEY_RSERVECALL = "rServeCall";
    private String[] unixRlocations;
    private File rBinaryLocation;
    private String rArgs;
    private String rServeArgs;
    private String[] rserveCalls;
    private String rserveCall;

    private RserveConnectionFactory() {
        Runtime.getRuntime().addShutdownHook(this);
        NbPreferences.forModule(RserveConnectionFactory.class).addPreferenceChangeListener(this);
        unixRlocations = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RBINARY_LOCATIONS, defaultUnixRlocations).split(",");
        rArgs = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RARGS, defaultRargs);
        String rbinary = NbPreferences.forModule(RserveConnectionFactory.class).get(KEY_RBINARY_LOCATION, null);
        if (rbinary != null) {
            rBinaryLocation = new File(rbinary);
        }
        rserveCalls = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVECALLS, defaultRserveCalls).split(",");
        rServeArgs = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RARGS, defaultRserveArgs);
        rserveCall = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVECALL, null);
    }

    public static RserveConnectionFactory getInstance() {
        return rcf;
    }

    //DO NOT CHANGE!!!
    public static RConnection getDefaultConnection() {
        return RserveConnectionFactory.hotfixConnection();
    }

    private void setConnection(RConnection connection) {
        if (this.activeConnection == null) {
            this.activeConnection = connection;
        } else {
            throw new IllegalStateException("Can not reset active connection! Call RserveConnectionFactory.closeConnection() before!");
        }
    }

    public void closeConnection() {
        if (this.activeConnection != null) {
            System.out.println("Closing connection to Rserve!");
            if (isLocalServer) {
                try {
                    System.out.println("Shutting down local server!");
                    this.activeConnection.serverShutdown();
                    this.activeConnection.shutdown();
                } catch (RserveException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            this.activeConnection.close();
            this.activeConnection = null;
        }
    }

    public boolean isWindows() {
        String osname = System.getProperty("os.name");
        if (osname != null && osname.length() >= 7 && osname.substring(0, 7).
                equals("Windows")) {
            return true;
        }
        return false;
    }

    public String buildExecString() {
        StringBuilder sb = new StringBuilder();
        if (rBinaryLocation == null) {
            rBinaryLocation = getRBinaryLocation();
        }
        sb.append(rBinaryLocation.getAbsolutePath());
        sb.append(" CMD ");
        if (rserveCall == null) {
            int exitValue = -1;
            for (String rserveCallValue : rserveCalls) {
                try {
                    String tmpExecString = sb.toString()+rserveCallValue;
                    Process p = startProcess(tmpExecString);
                    exitValue = p.waitFor();
                    if (exitValue == 0) {
                        rserveCall = rserveCallValue;
                        break;
                    }
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        sb.append(rserveCall.trim());
        sb.append(" ");
        sb.append(rArgs.trim());
        sb.append(" ");
        sb.append(rServeArgs.trim());
        return sb.toString().trim();
    }

    public static RConnection hotfixConnection() {
        return RserveConnectionFactory.getInstance().getLocalConnection();
    }

    public Process startProcess(String commandString) {
        boolean isWindows = isWindows();
        try {
            Process p = Runtime.getRuntime().exec(commandString);
            StreamHog errorHog = new StreamHog(
                    p.getErrorStream(),
                    true);
            StreamHog outputHog = new StreamHog(
                    p.getInputStream(),
                    true);
            return p;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public void startProcessAndWait(String commandString) {
        boolean isWindows = isWindows();
        try {
            Process p = Runtime.getRuntime().exec(commandString);
            StreamHog errorHog = new StreamHog(
                    p.getErrorStream(),
                    true);
            StreamHog outputHog = new StreamHog(
                    p.getInputStream(),
                    true);
            if (!isWindows) /* on Windows the process will never return, so we cannot wait */ {
                try {
                    p.waitFor();
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    protected RConnection connectLocal() {
        System.out.println(
                "Trying to connect to local Rserve ...");
        RConnection connection = null;
        try {
            connection = new RConnection();
            setConnection(connection);
            return connection;
        } catch (RserveException ex) {
            System.err.println("Failed to connect to Rserve ... launching local instance!");
            startProcessAndWait(buildExecString());
            for (int i = 0; i < 5; i++) {
                try {
                    connection = new RConnection();
                    setConnection(connection);
                    return connection;
                } catch (RserveException ex2) {
                    System.err.println(
                            "Failed to connect on try " + (1 + i) + "/5");
                }
            }
        }
        return null;
    }

    protected RConnection connectRemote(InetAddress address, int port, String username, String password) {
        System.out.println(
                "Trying to connect to remote Rserve at " + address + ":" + port + " ...");
        RConnection connection;
        for (int i = 0; i < 5; i++) {
            try {
                connection = new RConnection(address.getHostAddress(), port);
                if (connection.needLogin()) {
                    connection.login(username, password);
                    setConnection(connection);
                    return connection;
                } else {
                    throw new RuntimeException("Warning: Remote Rserve does not require authentication!");
                }
            } catch (RserveException ex) {
                System.err.println(
                        "Failed to connect on try " + (1 + i) + "/5");
            }
        }
        return null;
    }

    @Override
    public void run() {
        closeConnection();
    }

    /**
     * Returns the r binary location
     * @return 
     */
    public File getRBinaryLocation() {
        if (rBinaryLocation != null) {
            return rBinaryLocation;
        }
        String osname = System.getProperty("os.name");
        if (osname != null && osname.length() >= 7 && osname.substring(0, 7).
                equals("Windows")) {
            return getRBinaryLocationWindows();
        }
        return getRBinaryLocationUnix();
    }

    public void setRBinaryLocation(File location) {
        if (location.exists()) {
            rBinaryLocation = location;
        } else {
            throw new IllegalArgumentException("File " + location + " does not exist!");
        }
    }

    public File getRBinaryLocationUnix() {
        if (rBinaryLocation != null) {
            return rBinaryLocation;
        }
        for (String location : defaultUnixRlocations.split(",")) {
            if (new File(location).exists()) {
                return new File(location);
            }
        }
        return null;
    }

    public File getRBinaryLocationWindows() {
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
            return new File(installPath, "\\bin\\R.exe");
        }
        return null;

    }

    /**
     * If the factory already has an active connection, the active connection is returned.
     * Otherwise, this method tries to connectLocal to an already running local instance of Rserve.
     * If that fails, it will try to launch a new Rserve instance and connect to it. Otherwise
     * it will return null.
     * @return
     * @throws RserveException 
     */
    public RConnection getLocalConnection() {
        if (activeConnection != null) {
            return activeConnection;
        }
        return connectLocal();
    }

    /**
     * If the factory already has an active connection, the active connection is returned.
     * Otherwise, this method creates a remote connection to an rserve running at address and port.
     * This method does not allow unauthenticated access to Rserve.
     * @param address
     * @param port
     * @param username
     * @param password
     * @return an active RConnection to the remote server or null if anything failed
     */
    public RConnection getRemoteConnection(InetAddress address, int port,
            String username, String password) {
        if (activeConnection != null) {
            return activeConnection;
        }
        return connectRemote(address, port, username, password);
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent pce) {
        NbPreferences.forModule(RserveConnectionFactory.class);
    }
}
