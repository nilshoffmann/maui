/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import java.awt.BorderLayout;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import maltcms.ui.views.ChromMSHeatmapPanel;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.RequestProcessor;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//maltcms.ui//ChromatogramView//EN",
autostore = false)
public final class ChromatogramViewTopComponent extends CloneableTopComponent {

    private static ChromatogramViewTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "ChromatogramViewTopComponent";
    private volatile String filename;
    private MassSpectrumViewTopComponent secondaryView;

    public ChromatogramViewTopComponent(String filename, MassSpectrumViewTopComponent secondaryView) {
        this();
        this.filename = filename;
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        if (filename != null) {
            this.secondaryView = secondaryView;
            System.out.println("Filename given: " + filename);
            load();
//            this.jPanel1.add(load());
        }
    }

    public ChromatogramViewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ChromatogramViewTopComponent.class, "CTL_ChromatogramViewTopComponent"));
        setToolTipText(NbBundle.getMessage(ChromatogramViewTopComponent.class, "HINT_ChromatogramViewTopComponent"));
    }

    private void load() {
        System.out.println("Running loader");
        SwingWorker<ChromMSHeatmapPanel, Void> sw = new ChromatogramViewLoaderWorker(this, this.filename);
        RequestProcessor.getDefault().post(sw); 
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ChromatogramViewTopComponent getDefault() {
        if (instance == null) {
            instance = new ChromatogramViewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ChromatogramViewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ChromatogramViewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ChromatogramViewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ChromatogramViewTopComponent) {
            return (ChromatogramViewTopComponent) win;
        }
        Logger.getLogger(ChromatogramViewTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public void setPanel(final ChromMSHeatmapPanel jp) {
        final TopComponent tc = this;
        Runnable r = new Runnable() {

            @Override
            public void run() {
                add(jp,BorderLayout.CENTER);
                if(secondaryView!=null) {
                    System.out.println("Setting ms data!");
                    secondaryView.setMSData(jp.getFileFragment(), jp.getChartPanelMouseListener());
                }
                SwingUtilities.updateComponentTreeUI(tc);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
