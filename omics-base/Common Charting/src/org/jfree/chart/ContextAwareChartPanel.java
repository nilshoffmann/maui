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
package org.jfree.chart;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.imageio.ImageIO.write;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import static org.apache.batik.dom.GenericDOMImplementation.getDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import static org.apache.batik.svggen.SVGGeneratorContext.createDefault;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jfree.chart.panel.Overlay;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.actions.ActionPresenterProvider;
import org.openide.util.actions.Presenter;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class ContextAwareChartPanel extends ChartPanel implements LookupListener {

    private List<Overlay> overlays = new ArrayList<>();
    private IActionProvider popupMenuActionProvider = null;

    public ContextAwareChartPanel(JFreeChart chart) {
        super(chart);
    }

    public ContextAwareChartPanel(JFreeChart chart, boolean useBuffer) {
        super(chart, useBuffer);
    }

    public ContextAwareChartPanel(JFreeChart chart, boolean properties, boolean save, boolean print, boolean zoom, boolean tooltips) {
        super(chart, properties, save, print, zoom, tooltips);
    }

    public ContextAwareChartPanel(JFreeChart chart, int width, int height, int minimumDrawWidth, int minimumDrawHeight, int maximumDrawWidth, int maximumDrawHeight, boolean useBuffer, boolean properties, boolean save, boolean print, boolean zoom, boolean tooltips) {
        super(chart, width, height, minimumDrawWidth, minimumDrawHeight, maximumDrawWidth, maximumDrawHeight, useBuffer, properties, save, print, zoom, tooltips);
    }

    public ContextAwareChartPanel(JFreeChart chart, int width, int height, int minimumDrawWidth, int minimumDrawHeight, int maximumDrawWidth, int maximumDrawHeight, boolean useBuffer, boolean properties, boolean copy, boolean save, boolean print, boolean zoom, boolean tooltips) {
        super(chart, width, height, minimumDrawWidth, minimumDrawHeight, maximumDrawWidth, maximumDrawHeight, useBuffer, properties, copy, save, print, zoom, tooltips);
    }

    public void setPopupMenuActionProvider(IActionProvider actionProvider) {
        this.popupMenuActionProvider = actionProvider;
        actionProvider.getLookup().lookupResult(Object.class).addLookupListener(this);
    }

    @Override
    public void addOverlay(Overlay overlay) {
        super.addOverlay(overlay);
        overlays.add(overlay);
    }

    @Override
    public void removeOverlay(Overlay overlay) {
        super.removeOverlay(overlay);
        overlays.remove(overlay);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        super.mouseClicked(event); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void print(Graphics g) {
        boolean refreshBuffer = getRefreshBuffer();
        setRefreshBuffer(true);
        super.print(g);
        setRefreshBuffer(refreshBuffer);
    }

    public void paintChart(Graphics2D g2) {
        getChart().draw(g2, new Rectangle2D.Double(0, 0, getWidth(), getHeight()), getChartRenderingInfo());
        for (Overlay overlay : overlays) {
            overlay.paintOverlay(g2, this);
        }
        repaint();
    }

    private List<Component> actionsToPopup(Action[] actions, Lookup lookup) {
        // keeps actions for which was menu item created already (do not add them twice)
        Set<Action> counted = new HashSet<Action>();
        // components to be added (separators are null)
        List<Component> components = new ArrayList<Component>();
        Logger.getLogger(ContextAwareChartPanel.class.getName()).log(Level.FINE, "Processing {0} actions!", actions.length);
        for (Action action : actions) {
            if (action != null && counted.add(action)) {
                // switch to replacement action if there is some
                if (action instanceof ContextAwareAction) {
                    Action contextAwareAction = ((ContextAwareAction) action).createContextAwareInstance(lookup);
                    if (contextAwareAction == null) {
                        Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "ContextAwareAction.createContextAwareInstance(context) returns null. That is illegal!" + " action={0}, context={1}", new Object[]{action, lookup});
                    } else {
                        if (((ContextAwareAction) action).isEnabled()) {
                            action = contextAwareAction;
                        } else {
                            //hide menu item
                            action = null;
                        }
                    }
                }
                if (action != null) {
                    JMenuItem item;
                    if (action instanceof Presenter.Popup) {
                        item = ((Presenter.Popup) action).getPopupPresenter();
                        if (item == null) {
                            Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "findContextMenuImpl, getPopupPresenter returning null for {0}", action);
                            continue;
                        }
                    } else {
                        // We need to correctly handle mnemonics with '&' etc.
                        item = ActionPresenterProvider.getDefault().createPopupPresenter(action);
                    }

                    for (Component c : ActionPresenterProvider.getDefault().convertComponents(item)) {
                        if (c instanceof JSeparator) {
                            components.add(null);
                        } else {
                            components.add(c);
                        }
                    }
                }
            } else {
                components.add(null);
            }
        }
        Logger.getLogger(ContextAwareChartPanel.class.getName()).log(Level.FINE, "Added {0} menu items!", components.size());
        return components;
    }

    @Override
    protected JPopupMenu createPopupMenu(boolean properties,
            boolean copy, boolean save, boolean print, boolean zoom) {
        JPopupMenu result = new JPopupMenu(localizationResources.getString("Chart") + ":");
        boolean separator = false;
        if (popupMenuActionProvider != null) {
            Action[] actions = popupMenuActionProvider.getActions();
            List<Component> components = actionsToPopup(actions, popupMenuActionProvider.getLookup());
            boolean nonempty = false; // has anything been added yet?
            boolean pendingSep = false; // should there be a separator before any following item?
            for (Component c : components) {
                try {
                    if (c == null) {
                        pendingSep = nonempty;
                    } else {
                        nonempty = true;
                        if (pendingSep) {
                            pendingSep = false;
                            result.addSeparator();
                        }
                        result.add(c);
                    }
                } catch (RuntimeException ex) {
                    Exceptions.attachMessage(ex, "Current component: " + c); // NOI18N
                    Exceptions.attachMessage(ex, "List of components: " + components); // NOI18N
                    Exceptions.attachMessage(ex, "List of actions: " + Arrays.asList(actions)); // NOI18N
                    Exceptions.printStackTrace(ex);
                }
            }
            if (popupMenuActionProvider.getActions().length > 0) {
                result.addSeparator();
            }
        } else {
            Logger.getLogger(ContextAwareChartPanel.class.getName()).log(Level.WARNING, "Popup menu provider is null!");
        }

        if (properties) {
            JMenuItem propertiesItem = new JMenuItem(
                    localizationResources.getString("Properties..."));
            propertiesItem.setActionCommand(PROPERTIES_COMMAND);
            propertiesItem.addActionListener(this);
            result.add(propertiesItem);
            separator = true;
        }

        if (copy) {
            if (separator) {
                result.addSeparator();
            }
            JMenuItem copyItem = new JMenuItem(
                    localizationResources.getString("Copy"));
            copyItem.setActionCommand(COPY_COMMAND);
            copyItem.addActionListener(this);
            result.add(copyItem);
            separator = !save;
        }

        if (save) {
            if (separator) {
                result.addSeparator();
            }
            JMenu saveSubMenu = new JMenu(localizationResources.getString("Save_as..."));
            JMenuItem pngItem = new JMenuItem("PNG...");
//            pngItem.setActionCommand("SAVE_AS_PNG");
            pngItem.setActionCommand(SAVE_COMMAND);
            pngItem.addActionListener(this);
            saveSubMenu.add(pngItem);

//            if (createSVGGraphics2D(10, 10) != null) {
            JMenuItem svgItem = new JMenuItem("SVG...");
            svgItem.setActionCommand("SAVE_AS_SVG");
            svgItem.addActionListener(this);
            saveSubMenu.add(svgItem);
//            }
//
//            if (isOrsonPDFAvailable()) {
//                JMenuItem pdfItem = new JMenuItem("PDF...");
//                pdfItem.setActionCommand("SAVE_AS_PDF");
//                pdfItem.addActionListener(this);
//                saveSubMenu.add(pdfItem);
//            }
            result.add(saveSubMenu);
            separator = true;
        }

        if (print) {
            if (separator) {
                result.addSeparator();
            }
            JMenuItem printItem = new JMenuItem(
                    localizationResources.getString("Print..."));
            printItem.setActionCommand(PRINT_COMMAND);
            printItem.addActionListener(this);
            result.add(printItem);
            separator = true;
        }

        if (zoom) {
            if (separator) {
                result.addSeparator();
            }

            JMenu zoomInMenu = new JMenu(
                    localizationResources.getString("Zoom_In"));

            JMenuItem zoomInBothMenuItem = new JMenuItem(
                    localizationResources.getString("All_Axes"));
            zoomInBothMenuItem.setActionCommand(ZOOM_IN_BOTH_COMMAND);
            zoomInBothMenuItem.addActionListener(this);
            zoomInMenu.add(zoomInBothMenuItem);

            zoomInMenu.addSeparator();

            JMenuItem zoomInDomainMenuItem = new JMenuItem(
                    localizationResources.getString("Domain_Axis"));
            zoomInDomainMenuItem.setActionCommand(ZOOM_IN_DOMAIN_COMMAND);
            zoomInDomainMenuItem.addActionListener(this);
            zoomInMenu.add(zoomInDomainMenuItem);

            JMenuItem zoomInRangeMenuItem = new JMenuItem(
                    localizationResources.getString("Range_Axis"));
            zoomInRangeMenuItem.setActionCommand(ZOOM_IN_RANGE_COMMAND);
            zoomInRangeMenuItem.addActionListener(this);
            zoomInMenu.add(zoomInRangeMenuItem);

            result.add(zoomInMenu);

            JMenu zoomOutMenu = new JMenu(
                    localizationResources.getString("Zoom_Out"));

            JMenuItem zoomOutBothMenuItem = new JMenuItem(
                    localizationResources.getString("All_Axes"));
            zoomOutBothMenuItem.setActionCommand(ZOOM_OUT_BOTH_COMMAND);
            zoomOutBothMenuItem.addActionListener(this);
            zoomOutMenu.add(zoomOutBothMenuItem);

            zoomOutMenu.addSeparator();

            JMenuItem zoomOutDomainMenuItem = new JMenuItem(
                    localizationResources.getString("Domain_Axis"));
            zoomOutDomainMenuItem.setActionCommand(
                    ZOOM_OUT_DOMAIN_COMMAND);
            zoomOutDomainMenuItem.addActionListener(this);
            zoomOutMenu.add(zoomOutDomainMenuItem);

            JMenuItem zoomOutRangeMenuItem = new JMenuItem(
                    localizationResources.getString("Range_Axis"));
            zoomOutRangeMenuItem.setActionCommand(ZOOM_OUT_RANGE_COMMAND);
            zoomOutRangeMenuItem.addActionListener(this);
            zoomOutMenu.add(zoomOutRangeMenuItem);

            result.add(zoomOutMenu);

            JMenu autoRangeMenu = new JMenu(
                    localizationResources.getString("Auto_Range"));

            JMenuItem zoomResetBothMenuItem = new JMenuItem(
                    localizationResources.getString("All_Axes"));
            zoomResetBothMenuItem.setActionCommand(
                    ZOOM_RESET_BOTH_COMMAND);
            zoomResetBothMenuItem.addActionListener(this);
            autoRangeMenu.add(zoomResetBothMenuItem);

            autoRangeMenu.addSeparator();
            JMenuItem zoomResetDomainMenuItem = new JMenuItem(
                    localizationResources.getString("Domain_Axis"));
            zoomResetDomainMenuItem.setActionCommand(
                    ZOOM_RESET_DOMAIN_COMMAND);
            zoomResetDomainMenuItem.addActionListener(this);
            autoRangeMenu.add(zoomResetDomainMenuItem);

            JMenuItem zoomResetRangeMenuItem = new JMenuItem(
                    localizationResources.getString("Range_Axis"));
            zoomResetRangeMenuItem.setActionCommand(
                    ZOOM_RESET_RANGE_COMMAND);
            zoomResetRangeMenuItem.addActionListener(this);
            autoRangeMenu.add(zoomResetRangeMenuItem);

            result.addSeparator();
            result.add(autoRangeMenu);

        }

        return result;

    }

    /**
     *
     * Opens a file chooser and gives the user an opportunity to save the chart
     * in PNG format.
     *
     * @throws IOException if there is an I/O error.
     */
    @Override
    public void doSaveAs() throws IOException {
        FileChooserBuilder fcb = new FileChooserBuilder(ContextAwareChartPanel.class);
        JFileChooser fileChooser = fcb.createFileChooser();
//		JFileChooser fileChooser = new JFileChooser();
//		fileChooser.setCurrentDirectory(getDefaultDirectoryForSaveAs());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                localizationResources.getString("PNG_Image_Files"), "png");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);

        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getPath();
            if (isEnforceFileExtensions()) {
                if (!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
            }
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            paintComponent(g2);
            write(image, "PNG", new File(filename));
        }
    }

    public void saveAsSvg() {
        FileChooserBuilder fcb = new FileChooserBuilder(ContextAwareChartPanel.class);
        JFileChooser fileChooser = fcb.createFileChooser();
//		JFileChooser fileChooser = new JFileChooser();
//		fileChooser.setCurrentDirectory(getDefaultDirectoryForSaveAs());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SVG Files", "svg");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);

        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getPath();
            if (isEnforceFileExtensions()) {
                if (!filename.endsWith(".svg")) {
                    filename = filename + ".svg";
                }
            }
            DOMImplementation impl
                    = getDOMImplementation();
            String svgNS = "http://www.w3.org/2000/svg";
            Document myFactory = impl.createDocument(svgNS, "svg", null);
            SVGGeneratorContext ctx = createDefault(myFactory);
            ctx.setEmbeddedFontsOn(true);
            SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);
            paintChart(g2d);
            Writer out = null;
            try {
                out = new OutputStreamWriter(new FileOutputStream(new File(filename)));
                g2d.stream(out, true);
                g2d.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        super.actionPerformed(event);
        String command = event.getActionCommand();

        if (command.equals("SAVE_AS_SVG")) {
            System.out.println("This is a message");
            saveAsSvg();
        }

    }

    @Override
    public void resultChanged(LookupEvent le) {
        Runnable popupMenuCreator = new Runnable() {

            @Override
            public void run() {
                setPopupMenu(createPopupMenu(true, true, true, true, true));
                System.err.println("This is an error");
            }

        };
        SwingUtilities.invokeLater(popupMenuCreator);

    }
}
