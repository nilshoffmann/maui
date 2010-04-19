/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import ucar.nc2.dataset.*;
import ucar.nc2.util.*;
import thredds.ui.*;
import ucar.nc2.ui.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import ucar.nc2.ui.ToolsUI;
import ucar.util.prefs.PreferencesExt;
import ucar.util.prefs.XMLStore;

/**
 *
 * @author mw
 */
public class ToolsUIStarter {

    static private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ToolsUI.class);
    static private final String WorldDetailMap = "/optional/nj22/maps/Countries.zip";
    static private final String USMap = "/optional/nj22/maps/US.zip";
    static private final String FRAME_SIZE = "FrameSize";
    static private final String DEBUG_FRAME_SIZE = "DebugWindowSize";
    static private final String GRIDVIEW_FRAME_SIZE = "GridUIWindowSize";
    static private final String GRIDIMAGE_FRAME_SIZE = "GridImageWindowSize";
    static private boolean debugListen = false;
    private ucar.util.prefs.PreferencesExt mainPrefs;
    // UI
    private URLDumpPane urlPanel;
    private JTabbedPane tabbedPane;
    private JTabbedPane iospTabPane;
    private JTabbedPane ftTabPane;
    private JTabbedPane fmrcTabPane;
    private JTabbedPane ncmlTabPane;
    private JFrame parentFrame;
    private FileManager fileChooser;
    // data
    private ucar.nc2.thredds.ThreddsDataFactory threddsDataFactory = new ucar.nc2.thredds.ThreddsDataFactory();
    private boolean setUseRecordStructure = false;
    // debugging
    private JMenu debugFlagMenu;
    private DebugFlags debugFlags;
    // private AbstractAction useDebugWindowAction;
    //private IndependentWindow debugWindow;
    //private TextOutputStreamPane debugPane;
    //  private PrintStream debugOS;
    private boolean debug = false, debugTab = false, debugNcmlWrite = false, debugCB = false;
    private static String wantDataset = null;
    private static PreferencesExt prefs;
    private static boolean done = false;
    private static ToolsUI ui;
    private static JFrame frame;
    private static XMLStore store;

    public ToolsUIStarter() {
        final SplashScreen splash = new SplashScreen();
// put UI in a JFrame

        // prefs storage
        try {
            String prefStore = ucar.util.prefs.XMLStore.makeStandardFilename(".unidata", "NetcdfUI22.xml");
            store = ucar.util.prefs.XMLStore.createFromFile(prefStore, null);
            prefs = store.getPreferences();
            //Debug.setStore(prefs.node("Debug"));
        } catch (IOException e) {
            System.out.println("XMLStore Creation failed " + e);
        }

        frame = new JFrame("NetCDF (4.0) Tools");
        ui = new ToolsUI(prefs, frame);

        frame.setIconImage(BAMutil.getImage("netcdfUI"));

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
                splash.setVisible(false);
                splash.dispose();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (!done) {
                    exit();
                }
            }
        });

        frame.getContentPane().add(ui);
        Rectangle bounds = (Rectangle) prefs.getBean(FRAME_SIZE, new Rectangle(50, 50, 800, 450));
        frame.setBounds(bounds);

        frame.pack();
        frame.setBounds(bounds);
        frame.setVisible(true);

    }

    private static class SplashScreen extends javax.swing.JWindow {

        public SplashScreen() {
            Image image = Resource.getImage("/resources/nj22/ui/pix/ring2.jpg");
            ImageIcon icon = new ImageIcon(image);
            JLabel lab = new JLabel(icon);
            getContentPane().add(lab);
            pack();

            //show();
            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            setLocation(screenSize.width / 2 - (width / 2), screenSize.height / 2 - (height / 2));
            addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    setVisible(false);
                }
            });
            setVisible(true);
        }
    }

    static private void exit() {
        ui.save();
        Rectangle bounds = frame.getBounds();
        prefs.putBeanObject(FRAME_SIZE, bounds);
        try {
            store.save();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        done = true; // on some systems, still get a window close event
        ucar.nc2.util.cache.FileCache cache = NetcdfDataset.getNetcdfFileCache();
        if (cache != null) {
            cache.clearCache(true);
        }
        NetcdfDataset.shutdown(); // shutdown threads
        System.exit(0);
    }
}
