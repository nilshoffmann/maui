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
package maltcms.ui.heatmap3Dviewer;

import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codecimpl.JPEGImageEncoder;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import org.openide.util.Exceptions;
import processing.core.*;

public class Heatmap3DViewer extends PApplet implements ComponentListener {

    /**
     * Heatmap3DViewer.
     *
     * Converts a flat image into spatial data points and rotates the points
     * around the center.
     */
    PImage extrude;
    int[][] values;
//float angle = 0;
//float z = 0;
    float[] eye = new float[]{0, 0, 0};
    float[] acceleration = new float[]{0, 0, 0};
    float[] at = new float[]{0, 0, 0};
    boolean posInit = false;
    float phi = 0;
    float rho = 0;
    float theta = 0;
    float zoff = 0;
    float tx = 0, ty = 0, tz = 0;
    boolean rotate = false;
    boolean toggleHelp = false;
    boolean animate = false;
    PFont myFont;
//float[] start = new float[]{0,0,0};
    Surface s = null;
    String file = null;
    File currentOutputFile = null;
    Component parentComp;
    private int startWidth = 400, startHeight = 300;

    public Heatmap3DViewer(Component parentComp) {
        this.parentComp = parentComp;
//        startWidth = width;
//        startHeight= height;
//        setup();
    }

    public void setSurfaceFile(String s) {
        this.file = s;
        setSurface();
    }

    public void showFileChooser() {
        if (animate) {
            noLoop();
        }
        JFileChooser jfc = null;
        if (currentOutputFile != null) {
            jfc = new JFileChooser(currentOutputFile.getParentFile());
        } else {
            jfc = new JFileChooser();
        }
        int ret = jfc.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            currentOutputFile = jfc.getSelectedFile();
        }
        if (animate) {
            loop();
        }
    }

    @Override
    public void setup() {
        try {
            size(startWidth, startHeight, PApplet.OPENGL);
        } catch (RendererChangeException rce) {
            //rce.printStackTrace();
        }
        colorMode(ARGB, 1);
        lights();
        myFont = createFont("Arial", 12);
        textFont(myFont);
        noLoop();
        setSurface();
    }

    private void setSurface() {
        if (this.file != null) {
            s = new Surface(this.file);
        }
        redraw();
    }

    @Override
    public void draw() {
        background(0);
        if (animate) {
            double incr = 0.001;
            animateUpdate(rho + incr, phi, theta);
//            animateUpdate(rho + incr, phi + incr, theta + incr);
        }
        //model transformations
        pushMatrix();
        translate(tx + (width / 2), ty + (height / 2), tz);
        rotateX(radians(rho));
        rotateY(radians(phi));
        rotateZ(radians(theta));
        if (s != null) {
            s.draw();
        }
        popMatrix();
        //toggle display help in 2D
        if (!toggleHelp) {
            hint(DISABLE_DEPTH_TEST);
            textMode(SCREEN);
            fill(255, 255, 255, 128);
            text("press 'h' for help", 10, 10 + textAscent());
            hint(ENABLE_DEPTH_TEST);
        }

        //display help
        if (toggleHelp) {
            hint(DISABLE_DEPTH_TEST);
            fill(192, 192, 192, 128);
            textMode(SCREEN);
            rectMode(CENTER);
            translate(width / 2, height / 2);
            rect(0, 0, 0.9f * width, 0.9f * height);

            translate(-width / 2, -height / 2);
            fill(255, 255, 255);
            float w = 0.9f * width - 2 * 10;
            float h = 0.9f * height - 2 * 10;
            System.out.println("width: " + w + " height: " + h);
            rectMode(CORNER);
            text(buildHelpString(), (width / 2) - (0.9f * width / 2) + 10, (height / 2) - (0.9f * height / 2) + 10 + textAscent(), w, h);
            hint(ENABLE_DEPTH_TEST);
        }

    }

    private String createHelpItem(String formatString, Object... values) {
        return String.format(Locale.US, formatString, values);
    }

    private StringBuilder createHelpGroup(String title, String primaryFormatString, String secondaryFormatString, Object value, Object... items) {
        final StringBuilder sb = new StringBuilder();
        sb.append(createHelpItem(primaryFormatString, title, value));
        sb.append("\n");
        if (items.length % 2 == 0) {
            for (int i = 0; i < items.length; i += 2) {
                sb.append(createHelpItem(secondaryFormatString, items[i], items[i + 1]));
                sb.append("\n");
            }
        } else {
            throw new IllegalArgumentException("Length of items array must be a multiple of two!");
        }
        return sb;
    }

    private String buildHelpString() {
        final StringBuilder sb = new StringBuilder();
        final String nfs = "%-10s\t%10.2f";
        final String bfs = "%-10s\t%10b";
        final String dfs = "\t'%s':\t%s";
        //sb.append("NAVIGATION\n");
        sb.append(createHelpGroup("SCALE", nfs, dfs, s.scaleValue, new Object[]{"i", "Increase scale", "o", "Decrease scale"}));

        sb.append(createHelpGroup("POS Y", nfs, dfs, tx, new Object[]{"w", "Move surface up", "s", "Move surface down"}));
        sb.append(createHelpGroup("POS X", nfs, dfs, ty, new Object[]{"a", "Move surface left", "d", "Move surface right"}));
        sb.append(createHelpGroup("POS Z", nfs, dfs, tz, new Object[]{"q", "Move away from surface", "e", "Move towards surface"}));

        sb.append(createHelpGroup("ROT X", nfs, dfs, rho, new Object[]{"UP", "Rotate about x axis (clockwise)", "DOWN", "Rotate about x axis (counter clockwise)"}));
        sb.append(createHelpGroup("ROT Y", nfs, dfs, phi, new Object[]{"LEFT", "Rotate about y axis (clockwise)", "RIGHT", "Rotate about y axis (counter clockwise)"}));
        sb.append(createHelpGroup("ROT Z", nfs, dfs, theta, new Object[]{"ALT+LEFT", "Rotate about z axis (clockwise)", "ALT+RIGHT", "Rotate about z axis (counter clockwise)"}));

        //sb.append("DISPLAY\n");
        sb.append(createHelpGroup("ANIMATE", bfs, dfs, animate, new Object[]{"g", "Toggle animation"}));
        sb.append(createHelpGroup("RESOLUTION", nfs, dfs, 1.0d / s.STEP_SIZE, new Object[]{"*", "Double resolution", "/", "Halve resolution"}));
        sb.append(createHelpGroup("THRESHOLD", nfs, dfs, s.threshold, new Object[]{"m", "Increase threshold", "n", "Decrease threshold"}));
        sb.append(createHelpGroup("WIREFRAME", bfs, dfs, s.wireframe, new Object[]{"c", "Toggle wireframe display"}));
        sb.append(createHelpGroup("DIFF VIEW", bfs, dfs, s.differential, new Object[]{"x", "Toggle differential display"}));
        sb.append(createHelpGroup("LOG SCALE", bfs, dfs, s.logScale, new Object[]{"l", "Use log scale (does not work with differential display)"}));
        sb.append(createHelpGroup("SQRT SCALE", bfs, dfs, s.sqrtScale, new Object[]{"k", "Use sqrt scale (does not work with differential display)"}));

        //sb.append("IO\n");
        sb.append(createHelpGroup("SAVE TO FILE", bfs, dfs, "", new Object[]{"p", "Save current view to file"}));
        return sb.toString();
    }

    private void toggleAnimation() {
        animate = !animate;
        if (animate) {
            loop();
        } else {
            noLoop();
        }
        //redraw();
    }

    private void animateUpdate(double rhoIncr, double phiIncr, double thetaIncr) {
        theta += thetaIncr;
        rho += rhoIncr;
        phi += phiIncr;
    }

    @Override
    public void keyPressed() {
        if (key == 'i' || key == 'o') {
            if (key == 'i') {
                s.scaleValue += 1.0f;
            }
            if (key == 'o' && s.scaleValue > 1) {
                s.scaleValue -= 1.0f;
            }
        } else if (key == 'w') {
            ty -= 5;
        } else if (key == 's') {
            ty += 5;
        } else if (key == 'h') {
            toggleHelp = !toggleHelp;
        } else if (key == 'a') {
            tx -= 5;
        } else if (key == 'd') {
            tx += 5;
        } else if (key == 'q') {
            tz -= 5;
        } else if (key == 'e') {
            tz += 5;
        } else if (key == 'c') {
            s.wireframe = !s.wireframe;
        } else if (key == 'm') {
            s.threshold += 1.0f;
        } else if (key == 'n') {
            if (s.threshold >= 1.0f) {
                s.threshold -= 1f;
            }
        } else if (key == 'x') {
            s.differential = !s.differential;
        } else if (key == 'f') {
            s.flip = !s.flip;
        } else if (key == 'p') {
            showFileChooser();
            saveImage(currentOutputFile);
        } else if (key == 'k') {
            s.sqrtScale = !s.sqrtScale;
        } else if (key == 'l') {
            s.logScale = !s.logScale;
        } else if (key == 'g') {
            toggleAnimation();
        } else if (key == '*') {
            s.STEP_SIZE *= 2;
        } else if (key == '/') {
            if (s.STEP_SIZE >= 2) {
                s.STEP_SIZE /= 2;
            }
        } else {
            if (key == CODED) {
                if (keyCode == LEFT) {
                    if (keyEvent.isAltDown()) {
                        theta--;
                    } else {
                        phi--;
                    }
                } else if (keyCode == RIGHT) {
                    if (keyEvent.isAltDown()) {
                        theta++;
                    } else {
                        phi++;
                    }
                } else if (keyCode == UP) {
                    rho++;
                } else if (keyCode == DOWN) {
                    rho--;
                }
            }

        }
        redraw();
    }
//

    @Override
    public void mouseReleased() {
        if (mouseButton == RIGHT) {
            posInit = !posInit;
        }
        redraw();
//  if(posInit && mouseButton == LEFT) {
//    eye[0] = mouseX-width/2;
//    eye[1] = mouseY-height/2;
//  }
    }

    @Override
    public void componentResized(ComponentEvent ce) {
        Dimension d = ce.getComponent().getSize();
        updateSize(d);
    }

    private void updateSize(Dimension d) {
        startWidth = d.width;
        startHeight = d.height;
        try {
            size(startWidth, startHeight, P3D);
        } catch (RendererChangeException rce) {
            //rce.printStackTrace();
        }
        //        resize(startWidth,startHeight);
    }

    @Override
    public void componentMoved(ComponentEvent ce) {
    }

    @Override
    public void componentShown(ComponentEvent ce) {
        Dimension d = ce.getComponent().getSize();
        updateSize(d);
    }

    @Override
    public void componentHidden(ComponentEvent ce) {
    }

    @Override
    public void processFocusEvent(FocusEvent fe) {
        super.processFocusEvent(fe);
        updateSize(this.parentComp.getSize());
    }

    class Surface {

        int[][] values;
        int width, height;
        int STEP_SIZE = 2;
        int xscale = 5, yscale = 5;
        float scaleValue = 500.0f;
        boolean logScale = false;
        boolean sqrtScale = false;
        boolean flip = false;
        boolean differential = false;
        boolean wireframe = false;
        float threshold = 0.0f;
        int alphav = 192;
        float ZZ = -256;

        Surface(String file) {
            PImage img = loadImage(file);
            width = img.width;
            height = img.height;
            values = new int[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = img.get(x, y);
                    values[x][y] = pixel;//int(brightness(pixel));
                }
            }
        }

        public int getHeightFor(int[][] values, int X, int Y) {// This Returns The Height From A Height Map Index
            int x = X % values.length;     // Error Check Our x Value
            int y = Y % values[0].length;     // Error Check Our y Value
            float h = 0;
            if (differential) {
                h = ((red(values[x][y]) - green(values[x][y])) + blue(values[x][y])) / (2 * 255);
                if (sqrtScale) {
//                    h = ((sqrt(red(values[x][y])) - sqrt(green(values[x][y]))) + sqrt(blue(values[x][y]))) / sqrt((2 * 255));
                    h = h > 0 ? sqrt(h) : -sqrt(h);
                    //h/=(sqrt(2*255));
                } else if (logScale) {
                    h = h > 0 ? log(1.0f + h) : -log(1.0f + h);
                } else {
//                    h = ((red(values[x][y]) - green(values[x][y])) + blue(values[x][y])) / (2 * 255);
                }
            } else {
                h = (red(values[x][y]) + green(values[x][y]) - blue(values[x][y])) / (2 * 255);
            }
            if (abs(h) < threshold) {
                return 0;
            }
//            if (logScale && !sqrtScale) {
//                return (flip) ? (-(int) (scaleValue * (log(1.0f + h)))) : ((int) (scaleValue * (log(1.0f + h)))); // Index Into Our Height Array And Return The Height
//            } else if (sqrtScale && !logScale) {
//                return (flip) ? (-(int) (scaleValue * (sqrt(h)))) : ((int) (scaleValue * (sqrt(h))));
//            }
            return (flip) ? (-(int) (scaleValue * (h))) : ((int) (scaleValue * (h))); // Index Into Our Height Array And Return The Height
        }

        public void setVertexColor(int[][] values, int x, int y) {
            if (wireframe) {
                noFill();
                stroke(color((red(values[x][y])), (green(values[x][y])), blue(values[x][y])));
            } else {
                noStroke();
                fill(color((red(values[x][y])), (green(values[x][y])), blue(values[x][y])));
            }
        }

        public void draw() {
            int X = 0, Y = 0;       // Create Some Variables To Walk The Array With.
            int x, y, z;            // Create Some Variables For Readability
            if (!wireframe) {              // What We Want To Render
                beginShape(QUADS);
                //gl.glBegin( GL.GL_QUADS );  // Render Polygons
            } else {
                beginShape(LINES);
                //gl.glBegin( GL.GL_LINES );  // Render Lines Instead
            }

            if (STEP_SIZE < 1) {
                STEP_SIZE = 1;
            }
            translate(-(width/STEP_SIZE) / 2, -(height/STEP_SIZE) / 2, ZZ);
            int maxx = 0;
            int maxy = 0;
            for (X = 0; X < (values.length - STEP_SIZE); X += STEP_SIZE) {
                for (Y = 0; Y < (values[0].length - STEP_SIZE); Y += STEP_SIZE) {
                    // Get The (X, Y, Z) Value For The Bottom Left Vertex
                    x = X;
                    y = Y;
                    z = getHeightFor(values, X, Y);
                    // Set The Color Value Of The Current Vertex
                    setVertexColor(values, x, y);
                    vertex(x*xscale, y*yscale, z);// Send This Vertex To OpenGL To Be Rendered

                    // Get The (X, Y, Z) Value For The Top Left Vertex
                    x = X;
                    y = Y + STEP_SIZE;
                    z = getHeightFor(values, X, Y + STEP_SIZE);
                    // Set The Color Value Of The Current Vertex
                    setVertexColor(values, x, y);
                    vertex(x*xscale, y*yscale, z);// Send This Vertex To OpenGL To Be Rendered

                    // Get The (X, Y, Z) Value For The Top Right Vertex
                    x = X + STEP_SIZE;
                    y = Y + STEP_SIZE;
                    z = getHeightFor(values, X + STEP_SIZE, Y + STEP_SIZE);
                    // Set The Color Value Of The Current Vertex
                    setVertexColor(values, x, y);
                    vertex(x*xscale, y*yscale, z);// Send This Vertex To OpenGL To Be Rendered

                    // Get The (X, Y, Z) Value For The Bottom Right Vertex
                    x = X + STEP_SIZE;
                    y = Y;
                    z = getHeightFor(values, X + STEP_SIZE, Y);
                    // Set The Color Value Of The Current Vertex
                    setVertexColor(values, x, y);
                    vertex(x*xscale, y*yscale, z);// Send This Vertex To OpenGL To Be Rendered
                    maxx = max(maxx, X + STEP_SIZE);
                    maxy = max(maxy, Y + STEP_SIZE);
                }
            }
            width = maxx;
            height = maxy;

            endShape();
        }
    }

    public void saveImage(File output) {
        if (output.exists()) {
            Object[] opts = {"Yes", "Cancel"};
            int n = JOptionPane.showOptionDialog(this, "File exists, overwrite?", "File exists", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, opts, opts[1]);
            if (n != JOptionPane.YES_OPTION) {
                return;
            }
        }
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        loadPixels();
        img.setRGB(0, 0, width, height, g.pixels, 0, width);
        String extn = output.getName().substring(output.getName().lastIndexOf('.') + 1).toLowerCase();
        if (extn.equals("jpg")) {

            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                JPEGEncodeParam p = new JPEGEncodeParam();
				p.setQuality(0.5f);
                JPEGImageEncoder encoder = new JPEGImageEncoder(out,p);
                encoder.setParam(p);
                encoder.encode(img);
                FileOutputStream fo = new FileOutputStream(output);
                out.writeTo(fo);
            } catch (FileNotFoundException e) {
                Exceptions.printStackTrace(e);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }

        } else if (extn.equals("png")) { // add here as needed

            try {
                javax.imageio.ImageIO.write(img, extn, output);
            } catch (Exception e) {
                Exceptions.printStackTrace(e);
            }

        } else {
            super.saveFrame(output.getName());
        }
    }

    public static void main(String args[]) {
        //PApplet.main(new String[]{"--bgcolor=#FFFFFF", "maltcms.ui.heatmap3Dviewer.Heatmap3DViewer"});
        final JFrame jf = new JFrame();
        final Heatmap3DViewer hv = new Heatmap3DViewer(jf);
        jf.add(hv, BorderLayout.CENTER);
        final JToolBar jb = new JToolBar("Actions");
        jb.add(new AbstractAction("Open") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser jfc = new JFileChooser();
                int ret = jfc.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    hv.setSurfaceFile(jfc.getSelectedFile().getAbsolutePath());
                } else {
                }
            }
        });
        jf.add(jb, BorderLayout.NORTH);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
