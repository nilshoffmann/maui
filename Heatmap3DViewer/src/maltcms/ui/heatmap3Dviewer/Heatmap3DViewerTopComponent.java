/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.heatmap3Dviewer;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//maltcms.ui.heatmap3Dviewer//Heatmap3DViewer//EN",
autostore = false)
public final class Heatmap3DViewerTopComponent extends CloneableTopComponent {

    private static Heatmap3DViewerTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "Heatmap3DViewerTopComponent";

    private Heatmap3DViewer hv = null;

    private URL url = null;

    public void setFile(URL u) {
        this.url = u;
        setupHeatmap3DViewer(this.url);
    }

    private void setupHeatmap3DViewer(URL u) {
        hv = new Heatmap3DViewer(this);
        addComponentListener(hv);
        addFocusListener(hv);
        add(hv,BorderLayout.CENTER);
        try {
            hv.setSurfaceFile(new File(u.toURI()).getAbsolutePath());
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }
        hv.init();
    }

    public Heatmap3DViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(Heatmap3DViewerTopComponent.class, "CTL_Heatmap3DViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(Heatmap3DViewerTopComponent.class, "HINT_Heatmap3DViewerTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized Heatmap3DViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new Heatmap3DViewerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the Heatmap3DViewerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized Heatmap3DViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(Heatmap3DViewerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof Heatmap3DViewerTopComponent) {
            return (Heatmap3DViewerTopComponent) win;
        }
        Logger.getLogger(Heatmap3DViewerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        
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
        
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
