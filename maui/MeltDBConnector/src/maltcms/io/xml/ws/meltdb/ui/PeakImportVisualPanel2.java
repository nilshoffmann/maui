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
package maltcms.io.xml.ws.meltdb.ui;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import maltcms.io.xml.ws.meltdb.MeltDBSession;
import maltcms.io.xml.ws.meltdb.WebServiceClient;
import org.openide.util.Exceptions;

public final class PeakImportVisualPanel2 extends JPanel implements ListSelectionListener {

    private String userName;
    private char[] password;
    private JList projectsList;
    private String activeProject;
    public static final String PROP_WEBSERVICECLIENT = "WEBSERVICECLIENT";
    public static final String PROP_ACTIVE_PROJECT = "ACTIVE_PROJECT";

    /**
     * Creates new form PeakImportVisualPanel2
     */
    public PeakImportVisualPanel2() {
        initComponents();
        //        KeyStore ks;
        URL url = getClass().getResource("/maltcms/io/xml/ws/meltdb/meltdbcacerts");
        //        Logger.getLogger(getClass().getName()).log(Level.INFO, "Loading truststore from "+url);
        //        System.setProperty("javax.net.ssl.keyStore", url.getPath());
        //        //System.setProperty("jjavax.net.ssl.keyStoreType", "pkcs12");
        //        System.setProperty("javax.net.ssl.keyStorePassword", "");
        //        System.setProperty("javax.net.ssl.trustStore", url.getPath());
        //        System.setProperty("javax.net.ssl.trustStorePassword", "");

        KeyStore ks;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(url.openStream(), null);
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf
                = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            //            X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            //            SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
            context.init(null, tmf.getTrustManagers(), null);
            SSLContext.setDefault(context);//getSocketFactory();
        } catch (KeyManagementException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchAlgorithmException ex) {
            Exceptions.printStackTrace(ex);
        } catch (CertificateException ex) {
            Exceptions.printStackTrace(ex);
        } catch (KeyStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String getName() {
        return "Select Project";
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public void setPassword(char[] password) {
        this.password = password;
        if (this.userName != null) {
            initMeltDBSession(password);
        }
    }

    private WebServiceClient initMeltDBSession(char[] password) {
        MeltDBSession ms = new MeltDBSession(userName, password);
        WebServiceClient wsc = new WebServiceClient(ms);
        add(wsc.getControlPanel());
        projectsList = wsc.getProjectsList();
        projectsList.addListSelectionListener(this);
        JScrollPane plsp = new JScrollPane(projectsList);
        plsp.setBorder(BorderFactory.createTitledBorder("Projects"));
        add(plsp);
        //
        //            JScrollPane elsp = new JScrollPane(getExperimentsList());
        //            elsp.setBorder(BorderFactory.createTitledBorder("Experiments"));
        //            this.panel.add(elsp);
        //            JScrollPane clsp = new JScrollPane(getChromatogramsList());
        //            clsp.setBorder(BorderFactory.createTitledBorder("Chromatograms"));
        //            this.panel.add(clsp);
        //        add(wsc.getPanel(), BorderLayout.CENTER);
        //        jf.setVisible(true);
        //        jf.pack();
        revalidate();
        firePropertyChange(PROP_WEBSERVICECLIENT, wsc, wsc);
        return wsc;
    }

//    public JList getProjectsList() {
//
//    }
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        String oldValue = activeProject;
        activeProject = (String) projectsList.getSelectedValue();
        firePropertyChange(PROP_ACTIVE_PROJECT, oldValue, projectsList.getSelectedValue());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
