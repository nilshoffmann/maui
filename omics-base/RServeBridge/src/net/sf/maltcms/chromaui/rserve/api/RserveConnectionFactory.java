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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import net.sf.maltcms.chromaui.rserve.spi.StreamHog;
import org.netbeans.api.keyring.Keyring;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Nils Hoffmann
 */
public class RserveConnectionFactory implements PreferenceChangeListener {

    private static RserveConnectionFactory rcf = new RserveConnectionFactory();
    public static final String KEY_RBINARY_LOCATIONS = "rbinaryLocations";
    public static final String KEY_RSERVE_HOST = "rserveHost";
    public static final String KEY_RSERVE_PORT = "rservePort";
    public static final String KEY_RSERVE_LOCAL = "true";
    public static final String KEY_RSERVE_USER = "rserveUser";
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
    public static final String defaultRserveCalls = "Rserve,INLINEARGUMENT";
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
    private String rserveRemoteHost;
    private String rserveRemotePort;
    private boolean debug = false;
    private Thread shutdownHook;
    private int lock;
    private String userName;
    private AtomicBoolean startedLocal = new AtomicBoolean(false);

    private RserveConnectionFactory() {
        NbPreferences.forModule(RserveConnectionFactory.class).addPreferenceChangeListener(this);
        loadPreferences();
    }

    private void loadPreferences() {
//		closeConnection();
        unixRlocations = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RBINARY_LOCATIONS, defaultUnixRlocations).split(",");
        rArgs = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RARGS, defaultRargs);
        String rbinary = NbPreferences.forModule(RserveConnectionFactory.class).get(KEY_RBINARY_LOCATION, null);
        if (rbinary != null && !rbinary.isEmpty()) {
            rBinaryLocation = new File(rbinary);
        }
        rserveCalls = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVECALLS, defaultRserveCalls).split(",");
        rServeArgs = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVEARGS, defaultRserveArgs);
        rserveCall = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVECALL, null);
        rserveRemoteHost = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVE_HOST, "127.0.0.1");
        rserveRemotePort = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVE_PORT, "6311");
        boolean localServer = Boolean.valueOf(NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVE_LOCAL, "true"));
//		if (localServer != isLocalServer) {
//			closeConnection();
//		}
        isLocalServer = localServer;
        userName = NbPreferences.forModule(RserveConnectionFactory.class).get(RserveConnectionFactory.KEY_RSERVE_USER, null);
        StringBuilder sb = new StringBuilder();
        sb.append("unixRlocations: ").append(Arrays.toString(unixRlocations)).append("\n");
        sb.append("rBinaryLocation: ").append(rBinaryLocation).append("\n");
        sb.append("rArgs: ").append(rArgs).append("\n");
        sb.append("rServeArgs: ").append(rServeArgs).append("\n");
        sb.append("rserveCalls: ").append(Arrays.toString(rserveCalls)).append("\n");
        sb.append("rserveCall: ").append(rserveCall).append("\n");
        sb.append("Remote Host: ").append(rserveRemoteHost).append("\n");
        sb.append("Remote Port: ").append(rserveRemotePort).append("\n");
        sb.append("Current mode: ").append(isLocalServer ? "Local" : "Remote").append("\n");
        Logger.getLogger(RserveConnectionFactory.class.getName()).info(sb.toString());
    }

    public static RserveConnectionFactory getInstance() {
        return rcf;
    }

    //DO NOT CHANGE!!!
    public static RConnection getDefaultConnection() {
//		return RserveConnectionFactory.hotfixConnection();
        RserveConnectionFactory factory = RserveConnectionFactory.getInstance();
        if (factory.isLocalServer) {
            return factory.getLocalConnection();
        } else {
            return factory.getRemoteConnection();
        }
    }

    private void setConnection(RConnection connection) {
        if (this.activeConnection == null) {
            this.activeConnection = connection;
//			lock = this.activeConnection.lock();
        } else {
            throw new IllegalStateException("Can not reset active connection! Call RserveConnectionFactory.closeConnection() before!");
        }
    }

//    public synchronized void closeConnection() {
//        if (this.activeConnection != null) {
//            Logger.getLogger(RserveConnectionFactory.class.getName()).info("Closing connection to Rserve!");
//            if (isLocalServer) {
//                try {
//                    Logger.getLogger(RserveConnectionFactory.class.getName()).info("Shutting down local server!");
//                    this.activeConnection.serverShutdown();
//                } catch (RserveException ex) {
//                    try {
//                        this.activeConnection.shutdown();
//                    } catch (RserveException ex1) {
//                        Exceptions.printStackTrace(ex1);
//                    }
//                } finally {
//                }
//            }
//            this.activeConnection.close();
////			this.activeConnection.unlock(lock);
//            this.activeConnection = null;
//        }
//    }

    public boolean isWindows() {
        String osname = System.getProperty("os.name");
        if (osname != null && osname.length() >= 7 && osname.substring(0, 7).
                equals("Windows")) {
            return true;
        }
        return false;
    }

    public List<String> buildRserveCommand(File rBinaryLocation, boolean debug, String rServeCallValue, String rServeArgs, String rArgs) {
        List<String> commandList = new ArrayList<String>();
        if (isWindows()) {
            commandList.addAll(Arrays.asList("\"" + rBinaryLocation.getAbsolutePath() + "\"", "-e", "\"library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args='" + rServeArgs + "')\""));
        } else {
            if (rServeCallValue.equals("INLINEARGUMENT")) {
                commandList.addAll(Arrays.asList(rBinaryLocation.getAbsolutePath(), "-e", "\"library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args='" + rServeArgs + "')\""));
            } else {
                commandList.addAll(Arrays.asList(rBinaryLocation.getAbsolutePath(), "CMD", "Rserve"));
            }
        }
        String[] rRargs = rArgs.split(" ");
        commandList.addAll(Arrays.asList(rRargs));
        return commandList;
    }

    public synchronized RConnection startLocalAndConnect() {
        //List<String> rserveCmd = new ArrayList<String>();
        if (rBinaryLocation == null) {
            rBinaryLocation = getRBinaryLocation();
        }
        Logger.getLogger(RserveConnectionFactory.class.getName()).log(Level.INFO, "R binary location: {0}", rBinaryLocation);
        if (startedLocal.compareAndSet(false, true)) {
            //rserveCmd.add(rBinaryLocation.getAbsolutePath());
            //rserveCmd.add("CMD");
            if (rserveCall == null) {
                int exitValue = -1;
                for (String rserveCallValue : rserveCalls) {
                    List<String> rserveCmd = buildRserveCommand(rBinaryLocation, debug, rserveCallValue, rServeArgs, rArgs);
                    Logger.getLogger(RserveConnectionFactory.class.getName()).log(Level.INFO, "Trying to run: {0}", rserveCmd);
                    Process p = null;
                    try {
                        p = startProcessAndWait(rserveCmd);
                        for (int i = 0; i < 5; i++) {
                            try {
                                RserveConnection rserveConnection = new RserveConnection(InetAddress.getLoopbackAddress(), 6311, true);
                                RConnection conn = rserveConnection.getConnection();
                                Logger.getLogger(RserveConnectionFactory.class.getName()).log(Level.INFO, "Connected to Rserve version {0}", conn.getServerVersion());
                                Logger.getLogger(RserveConnectionFactory.class.getName()).info("Keeping rserveCall for future reference!");
                                //setConnection(connection);
                                rserveCall = rserveCallValue;
                                //							lock = conn.tryLock();
                                if (p != null) {
                                    p.destroy();
                                }
                                startedLocal.compareAndSet(true, false);
                                return conn;
                            } catch (RserveException ex2) {
                                System.err.println(
                                        "Failed to connect on try " + (1 + i) + "/5");
                            }
                            if (p != null) {
                                p.destroy();
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ix) {
                            };
                        }
                    } catch (Exception e) {
                        if (p != null) {
                            p.destroy();
                        }
                    }
                }
                throw new RuntimeException("Could not determine local Rserve command!");
            }
        } else {
            throw new IllegalStateException("Already tried to start local instance!");
        }
        return null;
//return Arrays.asList(rBinaryLocation.getAbsolutePath(), "CMD", rserveCall);
    }

//	public static RConnection hotfixConnection() {
//		return RserveConnectionFactory.getInstance().getLocalConnection();
//	}
    public Process startProcess(List<String> command) {
        boolean isWindows = isWindows();
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();//Runtime.getRuntime().exec(command);
            StreamHog errorHog = new StreamHog(
                    p.getErrorStream(),
                    debug);
            StreamHog outputHog = new StreamHog(
                    p.getInputStream(),
                    debug);
            return p;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public Process startProcessAndWait(List<String> commandString) {
        boolean isWindows = isWindows();
        Process p = null;
        try {
            ProcessBuilder bp = new ProcessBuilder(commandString);
            p = bp.start();
            StreamHog errorHog = new StreamHog(
                    p.getErrorStream(),
                    debug);
            StreamHog outputHog = new StreamHog(
                    p.getInputStream(),
                    debug);
            if (!isWindows) /*
             * on Windows the process will never return, so we cannot wait
             */ {
                try {
                    p.waitFor();
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            return p;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            if (p != null) {
                Logger.getLogger(RserveConnectionFactory.class.getName()).info("Trying to destroy process!");
                p.destroy();
            }
        }
        return p;
    }

    protected synchronized RConnection connectLocal() {
        Logger.getLogger(RserveConnectionFactory.class.getName()).info(
                "Trying to connect to local Rserve ...");
        RConnection connection = null;
        RserveConnection rserveConnection;
        try {
            Logger.getLogger(RserveConnectionFactory.class.getName()).info("Checking for an already running Rserve");
            rserveConnection = new RserveConnection(InetAddress.getLoopbackAddress(), 6311, false);
            connection = rserveConnection.getConnection();
            isLocalServer = true;
            //setConnection(connection);
            Logger.getLogger(RserveConnectionFactory.class.getName()).info("Found an already running Rserve instance!");
            setConnection(connection);
            return connection;
        } catch (RserveException ex) {
            Logger.getLogger(RserveConnectionFactory.class.getName()).info("Failed to connect to local Rserve ... launching new local instance!");
            connection = startLocalAndConnect();
            if (connection != null) {
                if (connection.isConnected()) {
                    isLocalServer = true;
                    setConnection(connection);
                    Logger.getLogger(RserveConnectionFactory.class.getName()).info("Connected to Rserve!");
                    return connection;
                }
            }
//			Process p = null;
//			try {
//				p = startProcessAndWait();
//				for (int i = 0; i < 5; i++) {
//					try {
//						connection = new RConnection();
//						//setConnection(connection);
//						isLocalServer = true;
//						return connection;
//					} catch (RserveException ex2) {
//						if (p != null) {
//							p.destroy();
//						}
//						System.err.println(
//								"Failed to connect on try " + (1 + i) + "/5");
//					}
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException ix) {
//					};
//				}
//			} catch (Exception e) {
//				if (p != null) {
//					p.destroy();
//				}
//				System.err.println("Failed to connect to Rserve: " + e.getLocalizedMessage());
//			}
        }
        throw new NullPointerException();
    }

    protected RConnection connectRemote(InetAddress address, int port, String username, char[] password) {
        Logger.getLogger(RserveConnectionFactory.class.getName()).log(
                Level.INFO, "Trying to connect to remote Rserve at {0}:{1} ...", new Object[]{address, port});
        RserveConnection rserveConnection;
        for (int i = 0; i < 5; i++) {
            try {
                rserveConnection = new RserveConnection(address, port, username, String.valueOf(password), false);
                setConnection(rserveConnection.getConnection());
                return activeConnection;
            } catch (RserveException ex) {
                ex.printStackTrace();
                System.err.println(
                        "Failed to connect on try " + (1 + i) + "/5");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ix) {
            };
        }
        return null;
    }

    /**
     * Returns the r binary location
     *
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
            Logger.getLogger(RserveConnectionFactory.class.getName()).info(
                    "Windows: querying registry to find where R is installed ...");
            String installPath = null;
            try {
                Process rp = Runtime.getRuntime().exec(
                        "reg query HKLM\\Software\\R-core\\R");
                StreamHog regHog = new StreamHog(rp.getInputStream(), true);
                rp.waitFor();
                regHog.join();
                installPath = regHog.getInstallPath();
                Logger.getLogger(RserveConnectionFactory.class.getName()).info("Using R installation location: " + installPath);
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
            if (new File(installPath, "\\bin\\x64\\R.exe").exists()) {
                return new File(installPath, "\\bin\\x64\\R.exe");
            }
            System.out.println("Falling back to default 32bit R instance");
            return new File(installPath, "\\bin\\R.exe");
        }
        return null;

    }

    /**
     * If the factory already has an active connection, the active connection is
     * returned. Otherwise, this method tries to connectLocal to an already
     * running local instance of Rserve. If that fails, it will try to launch a
     * new Rserve instance and connect to it. Otherwise it will return null.
     *
     * @return
     * @throws RserveException
     */
    public RConnection getLocalConnection() {
        if (activeConnection == null) {
            activeConnection = connectLocal();
        } else {
            if (!activeConnection.isConnected()) {
                activeConnection = null;
                activeConnection = connectLocal();
            }
        }
        return activeConnection;
    }

    /**
     * If the factory already has an active connection, the active connection is
     * returned. Otherwise, this method creates a remote connection to an rserve
     * running at address and port. This method does not allow unauthenticated
     * access to Rserve.
     *
     * @param address
     * @param port
     * @param username
     * @param password
     * @return an active RConnection to the remote server or null if anything
     * failed
     */
    public RConnection getRemoteConnection(InetAddress address, int port,
            String username, char[] password) {
        if (activeConnection != null) {
            if (!activeConnection.isConnected()) {
                activeConnection = null;
            }else{
                return activeConnection;
            }
        }
        return connectRemote(address, port, username, password);
    }

    public RConnection getRemoteConnection() {
        if (activeConnection != null) {
            if(!activeConnection.isConnected()) {
                activeConnection = null;
            }else{
                return activeConnection;
            }
        }
        InetAddress address;
        try {
            address = InetAddress.getByName(rserveRemoteHost);
            int port = Integer.parseInt(rserveRemotePort);
            String username = userName;
            char[] password = Keyring.read("Rserve://" + rserveRemoteHost.trim());
            return connectRemote(address, port, username, password);
        } catch (UnknownHostException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent pce) {
        loadPreferences();
    }
//	public Thread getShutdownHook() {
//		if (shutdownHook == null) {
//			shutdownHook = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					RserveConnectionFactory.getInstance().closeConnection();
//				}
//			});
//		}
//		return shutdownHook;
//	}
}
