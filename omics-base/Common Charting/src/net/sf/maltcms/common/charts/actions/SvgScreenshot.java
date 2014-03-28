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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.System.getProperty;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.batik.dom.GenericDOMImplementation;
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
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;
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
    //@ActionReference(path = "Menu/Window", position = 300),
    @ActionReference(path = "Shortcuts", name = "DS-F10")
})
@Messages("CTL_SvgScreenshot=Svg Screenshot")
public final class SvgScreenshot implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Frame f = getDefault().getMainWindow();
        DOMImplementation impl
                = getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document myFactory = impl.createDocument(svgNS, "svg", null);
        SVGGeneratorContext ctx = createDefault(myFactory);
        ctx.setEmbeddedFontsOn(true);
        GenericImageHandler ihandler = new CachedImageHandlerBase64Encoder();
        ctx.setGenericImageHandler(ihandler);
        SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);
        g2d.setTransform(getTranslateInstance(0, -f.getHeight()));
        f.invalidate();
        f.revalidate();
        f.print(g2d);
        Writer out = null;
        try {
            File outputDir = new File(getProperty("user.home"), "maui-screenshots");
            outputDir.mkdirs();
            Date d = new Date();
            File file = new File(outputDir, new SimpleDateFormat().format(d) + ".svg");
            out = new OutputStreamWriter(new FileOutputStream(file));
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
