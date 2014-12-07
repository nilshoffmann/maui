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
package net.sf.maltcms.common.charts.actions;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.System.getProperty;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.batik.dom.GenericDOMImplementation.getDOMImplementation;
import org.apache.batik.svggen.CachedImageHandlerBase64Encoder;
import org.apache.batik.svggen.GenericImageHandler;
import org.apache.batik.svggen.SVGGeneratorContext;
import static org.apache.batik.svggen.SVGGeneratorContext.createDefault;
import org.apache.batik.svggen.SVGGraphics2D;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import static org.openide.windows.WindowManager.getDefault;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

@ActionID(
        category = "Window",
        id = "net.sf.maltcms.common.charts.actions.SvgScreenshot"
)
@ActionRegistration(
        displayName = "#CTL_SvgScreenshot"
)
@ActionReferences({
    //only register this action with a keyboard shortcut
    @ActionReference(path = "Shortcuts", name = "DS-F10")
})
@Messages("CTL_SvgScreenshot=Svg Screenshot")
public final class SvgScreenshot implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //retrieve the main window component
//        Frame f = getDefault().getMainWindow();
        Window[] windows = Window.getWindows();
        for(int i = 0;i<windows.length; i++) {
            //set up the svg canvas and xml document
            DOMImplementation impl
                    = getDOMImplementation();
            String svgNS = "http://www.w3.org/2000/svg";
            Document myFactory = impl.createDocument(svgNS, "svg", null);
            SVGGeneratorContext ctx = createDefault(myFactory);
            //embed fonts used to render the UI
            ctx.setEmbeddedFontsOn(true);
            //add image handler to embed images (not scale invariant)
            GenericImageHandler ihandler = new CachedImageHandlerBase64Encoder();
            ctx.setGenericImageHandler(ihandler);
            SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);
            g2d.setTransform(getTranslateInstance(0, 0));//-f.getHeight()));
            Window window = windows[i];
            window.invalidate();
            window.validate();
            window.paint(g2d);
            File outputDir = new File(getProperty("user.home"), "NetBeansScreenshots");
            outputDir.mkdirs();
            Date d = new Date();
            String frameTitle = window.getName();
            if (frameTitle == null || frameTitle.isEmpty()) {
                frameTitle = "-window-" + i;
            } else {
                frameTitle = "-window-" + frameTitle;
            }
            File file = new File(outputDir, new SimpleDateFormat().format(d) + frameTitle + ".svg");
            try (Writer out = new OutputStreamWriter(new FileOutputStream(file))) {
                g2d.stream(out, true);
                g2d.dispose();
                Logger.getLogger(SvgScreenshot.class.getName()).log(Level.INFO, "Saving screenshot to {0}", file);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        Frame[] applicationFrames = Frame.getFrames();
        for (int i = 0; i < applicationFrames.length; i++) {
            //set up the svg canvas and xml document
            DOMImplementation impl
                    = getDOMImplementation();
            String svgNS = "http://www.w3.org/2000/svg";
            Document myFactory = impl.createDocument(svgNS, "svg", null);
            SVGGeneratorContext ctx = createDefault(myFactory);
            //embed fonts used to render the UI
            ctx.setEmbeddedFontsOn(true);
            //add image handler to embed images (not scale invariant)
            GenericImageHandler ihandler = new CachedImageHandlerBase64Encoder();
            ctx.setGenericImageHandler(ihandler);
            SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);
            g2d.setTransform(getTranslateInstance(0, 0));//-f.getHeight()));
            Frame f = applicationFrames[i];
            f.invalidate();
            f.revalidate();
            f.paint(g2d);
            File outputDir = new File(getProperty("user.home"), "NetBeansScreenshots");
            outputDir.mkdirs();
            Date d = new Date();
            String frameTitle = f.getTitle();
            if (frameTitle == null || frameTitle.isEmpty()) {
                frameTitle = "-frame-" + i;
            } else {
                frameTitle = "-frame-" + frameTitle;
            }
            File file = new File(outputDir, new SimpleDateFormat().format(d) + frameTitle + ".svg");
            try (Writer out = new OutputStreamWriter(new FileOutputStream(file))) {
                g2d.stream(out, true);
                g2d.dispose();
                Logger.getLogger(SvgScreenshot.class.getName()).log(Level.INFO, "Saving screenshot to {0}", file);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
