/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui;

import cross.datastructures.fragments.IFileFragment;
import java.awt.BorderLayout;
import java.util.logging.Logger;
import maltcms.ui.events.ChartPanelMouseListener;
import maltcms.ui.views.MassSpectrumChartPanel;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//maltcms.ui//MassSpectrumView//EN",
autostore = false)
public final class MassSpectrumViewTopComponent extends TopComponent {

    private static MassSpectrumViewTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "MassSpectrumViewTopComponent";

    public MassSpectrumViewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MassSpectrumViewTopComponent.class, "CTL_MassSpectrumViewTopComponent"));
        setToolTipText(NbBundle.getMessage(MassSpectrumViewTopComponent.class, "HINT_MassSpectrumViewTopComponent"));
    }

    public void setMSData(final IFileFragment f, final ChartPanelMouseListener cpml) {
        MassSpectrumChartPanel mscp = new MassSpectrumChartPanel(cpml,f);
        add(mscp,BorderLayout.CENTER);
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
    public static synchronized MassSpectrumViewTopComponent getDefault() {
        if (instance == null) {
            instance = new MassSpectrumViewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MassSpectrumViewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized MassSpectrumViewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(MassSpectrumViewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof MassSpectrumViewTopComponent) {
            return (MassSpectrumViewTopComponent) win;
        }
        Logger.getLogger(MassSpectrumViewTopComponent.class.getName()).warning(
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

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
