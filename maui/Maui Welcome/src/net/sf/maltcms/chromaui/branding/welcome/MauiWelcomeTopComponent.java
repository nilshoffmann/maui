/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.branding.welcome;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//net.sf.maltcms.chromaui.branding.welcome//MauiWelcome//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "MauiWelcomeTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "net.sf.maltcms.chromaui.branding.welcome.MauiWelcomeTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Window" /*, position = 333 */),
    @ActionReference(path = "Menu/Help" /*, position = 333 */)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MauiWelcomeAction",
        preferredID = "MauiWelcomeTopComponent")
@Messages({
    "CTL_MauiWelcomeAction=Welcome Center",
    "CTL_MauiWelcomeTopComponent=Welcome Center",
    "HINT_MauiWelcomeTopComponent=The Maui Welcome Center"
})
public final class MauiWelcomeTopComponent extends TopComponent implements HyperlinkListener {

    public MauiWelcomeTopComponent() {
        initComponents();
        setName(Bundle.CTL_MauiWelcomeTopComponent());
        setToolTipText(Bundle.HINT_MauiWelcomeTopComponent());
        searchAndIdentify.setVisible(false);
        quantify.setVisible(false);
        statistics.setVisible(false);
        maltcmsIntegration.setVisible(false);
    }

    private String readText(URL u) {
        BufferedReader bis = null;
        try {
            bis = new BufferedReader(new InputStreamReader(u.openStream()));
            StringBuilder sb = new StringBuilder();
            String buffer;
            while ((buffer = bis.readLine()) != null) {
                sb.append(buffer).append("\n");
            }
            Logger.getLogger(getClass().getName()).info(sb.toString());
            return sb.toString();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return "";
    }

    private void setContent(URL u) {
        URL editorUrl = editorPane.getPage();
        if (editorUrl == null || !editorUrl.equals(u)) {
            editorPane.setContentType("text/html");
            HTMLEditorKit kit = new HTMLEditorKit();
            editorPane.setEditorKit(kit);
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
            String bodyRule = "body { font-family: " + font.getFamily() + "; "
                    + "font-size: " + font.getSize() + "pt; }";
            String h1Rule = "h1 { font-size: " + (font.getSize() + 6) + "pt; font-weight: bold;}";
            String h2Rule = "h2 { font-size: " + (font.getSize() + 4) + "pt; font-weight: bold;}";
            String h3Rule = "h3 { font-size: " + (font.getSize() + 2) + "pt; font-weight: bold;}";
            try {
                editorPane.removeHyperlinkListener(this);
                editorPane.addHyperlinkListener(this);
                StyleSheet style = kit.getStyleSheet();
                style.addRule(bodyRule);
                style.addRule(h1Rule);
                style.addRule(h2Rule);
                style.addRule(h3Rule);
                Document doc = kit.createDefaultDocument();
                editorPane.setDocument(doc);
                editorPane.setPage(u);
                editorPane.invalidate();
                editorPane.revalidate();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            invalidate();
            validate();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        quantify = new javax.swing.JButton();
        searchAndIdentify = new javax.swing.JButton();
        visualize = new javax.swing.JButton();
        firstSteps = new javax.swing.JButton();
        statistics = new javax.swing.JButton();
        importPeaks = new javax.swing.JButton();
        newProject = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        customPipelines = new javax.swing.JButton();
        groovyActions = new javax.swing.JButton();
        maltcmsIntegration = new javax.swing.JButton();
        display = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();

        org.openide.awt.Mnemonics.setLocalizedText(quantify, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.quantify.text")); // NOI18N
        quantify.setEnabled(false);
        quantify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantifyActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(searchAndIdentify, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.searchAndIdentify.text")); // NOI18N
        searchAndIdentify.setEnabled(false);
        searchAndIdentify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchAndIdentifyActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(visualize, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.visualize.text")); // NOI18N
        visualize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualizeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(firstSteps, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.firstSteps.text")); // NOI18N
        firstSteps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstStepsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(statistics, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.statistics.text")); // NOI18N
        statistics.setEnabled(false);
        statistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statisticsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(importPeaks, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.importPeaks.text")); // NOI18N
        importPeaks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importPeaksActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(newProject, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.newProject.text")); // NOI18N
        newProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProjectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstSteps, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newProject, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importPeaks, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(visualize, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchAndIdentify, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantify, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statistics, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {firstSteps, importPeaks, newProject, quantify, searchAndIdentify, statistics, visualize});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(firstSteps)
                .addGap(18, 18, 18)
                .addComponent(newProject)
                .addGap(18, 18, 18)
                .addComponent(importPeaks)
                .addGap(18, 18, 18)
                .addComponent(visualize)
                .addGap(18, 18, 18)
                .addComponent(searchAndIdentify)
                .addGap(18, 18, 18)
                .addComponent(quantify)
                .addGap(18, 18, 18)
                .addComponent(statistics)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(customPipelines, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.customPipelines.text")); // NOI18N
        customPipelines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customPipelinesActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(groovyActions, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.groovyActions.text")); // NOI18N
        groovyActions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groovyActionsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(maltcmsIntegration, org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.maltcmsIntegration.text")); // NOI18N
        maltcmsIntegration.setEnabled(false);
        maltcmsIntegration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maltcmsIntegrationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customPipelines, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(groovyActions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(maltcmsIntegration, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customPipelines)
                .addGap(18, 18, 18)
                .addComponent(groovyActions)
                .addGap(18, 18, 18)
                .addComponent(maltcmsIntegration)
                .addContainerGap(204, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(MauiWelcomeTopComponent.class, "MauiWelcomeTopComponent.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        display.setPreferredSize(new java.awt.Dimension(100, 100));
        display.setLayout(new java.awt.BorderLayout());

        editorPane.setEditable(false);
        editorPane.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(editorPane);

        display.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 653, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(display, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 416, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jTabbedPane1)
                        .addComponent(display, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap()))
        );

        jScrollPane2.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 655, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void firstStepsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstStepsActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/firstSteps.html"));
    }//GEN-LAST:event_firstStepsActionPerformed

    private void newProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProjectActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/newProject.html"));
    }//GEN-LAST:event_newProjectActionPerformed

    private void importPeaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importPeaksActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/importPeaks.html"));
    }//GEN-LAST:event_importPeaksActionPerformed

    private void visualizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizeActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/visualize.html"));
    }//GEN-LAST:event_visualizeActionPerformed

    private void searchAndIdentifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchAndIdentifyActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/searchAndIdentify.html"));
    }//GEN-LAST:event_searchAndIdentifyActionPerformed

    private void quantifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantifyActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/quantify.html"));
    }//GEN-LAST:event_quantifyActionPerformed

    private void statisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statisticsActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/statistics.html"));
    }//GEN-LAST:event_statisticsActionPerformed

    private void customPipelinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customPipelinesActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/customPipelines.html"));
    }//GEN-LAST:event_customPipelinesActionPerformed

    private void groovyActionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groovyActionsActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/groovyActions.html"));
    }//GEN-LAST:event_groovyActionsActionPerformed

    private void maltcmsIntegrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maltcmsIntegrationActionPerformed
        setContent(MauiWelcomeTopComponent.class.getResource("resources/maltcmsIntegration.html"));
    }//GEN-LAST:event_maltcmsIntegrationActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton customPipelines;
    private javax.swing.JPanel display;
    private javax.swing.JEditorPane editorPane;
    private javax.swing.JButton firstSteps;
    private javax.swing.JButton groovyActions;
    private javax.swing.JButton importPeaks;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton maltcmsIntegration;
    private javax.swing.JButton newProject;
    private javax.swing.JButton quantify;
    private javax.swing.JButton searchAndIdentify;
    private javax.swing.JButton statistics;
    private javax.swing.JButton visualize;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        setContent(MauiWelcomeTopComponent.class.getResource("resources/firstSteps.html"));
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

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent he) {
        URL u = he.getURL();
        if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (u.getProtocol().equals("http") || u.getProtocol().equals("https")) {
                Logger.getLogger(getClass().getName()).info(u.getPath());
                if (u.getPath().startsWith("/maui/")) {
                    String path = u.getPath().substring("/maui".length());
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Path: {0}", path);
                    if (path.startsWith("/OptionsDialog")) {
                        String optionsPanelRegistration = path.substring("/OptionsDialog/".length());
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Opening options panel: {0}", optionsPanelRegistration);
                        OptionsDisplayer.getDefault().open(optionsPanelRegistration);
                    } else {
                        Actions.execFileSystemAction(path, new ActionEvent(this, 1, u.getPath()));
//                        Collection<? extends Action> l = Utilities.actionsForPath(path);
//                        if (l.size() > 1) {
//                            throw new NotImplementedException("Support for multiple actions not yet implemented!");
//                        } else if (l.size() == 1) {
//                            l.iterator().next().actionPerformed(new ActionEvent(this, 1, u.getPath()));
//                        }
                    }
                } else {
                    try {
                        Desktop.getDesktop().browse(u.toURI());
                    } catch (URISyntaxException | IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
    }
}
