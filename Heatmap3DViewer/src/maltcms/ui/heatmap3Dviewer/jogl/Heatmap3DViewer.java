package maltcms.ui.heatmap3Dviewer.jogl;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Heatmap3DViewer extends JPanel implements KeyListener, MouseListener {

    private initRenderer renderer;
    private GLCanvas canvas;
    private Animator loop;
    private JFrame frame;
    private GLU glu;
    private GL gl;
    private boolean toggleHelp;
    private int STEP_SIZE = 1, // Width And Height Of Each Quad (NEW)
            xLocation, yLocation, screenWidth, screenHeight,
            canvasHeight,
            canvasWidth;
    private float tx = 0, ty = 0, tz = 0, rho=0, theta=0, phi=0;
    // int[] displayListIds = null;
    private float eyeX = 0, eyeY = 35, eyeZ = 50, eyeOrthZ = 50;
    private float z = 0;
    private float fov = 60;
    private Surface s = null;
    // Data (NEW)
    private float scaleValue = 255f, // Scale Value For The Terrain (NEW)
            HEIGHT_RATIO = 10f; // Ratio That The Y Is Scaled According To The
    // X And Z (NEW)
    private boolean keys[] = new boolean[1024], // Array Used For The Keyboard Routine
            bRender = true; // Polygon Flag Set To TRUE By Default (NEW)

    // Loads The .RAW File And Stores It In pHeightMap
    private Surface loadHeatmap(String strName) {
        System.out.println("Loading new heightmap: " + strName);
        return new Surface(strName, 1);
    }

    public static void main(String[] args) {
        Heatmap3DViewer demo = new Heatmap3DViewer();
    }

    public Heatmap3DViewer() {
        frame = new JFrame("Heatmap3DViewer");
        canvasWidth = 640;
        canvasHeight = 480;
        xLocation = (screenWidth - canvasWidth) >> 1;
        yLocation = (screenHeight - canvasHeight) >> 1;
        frame.setLocation(xLocation, yLocation);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setIconImage(new ImageIcon("data/icon.png").getImage());
//        JFileChooser jfc = new JFileChooser();
//        int option = jfc.showOpenDialog(frame);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            File file = jfc.getSelectedFile();
//            setFile(file);
//        }

    }

    public void setFile(File f) {
        canvas = new GLCanvas(new GLCapabilities());
        canvas.setSize(new Dimension(canvasWidth, canvasHeight));
        canvas.addGLEventListener((renderer = new initRenderer(f.getAbsolutePath())));
        canvas.requestFocus();
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);

        frame.addKeyListener(this);
        frame.addWindowListener(new shutDownWindow());
        frame.getContentPane().add(new JTextField("Hallo JOGL"),
                BorderLayout.NORTH);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
//        add(canvas);
    }

    public class initRenderer implements GLEventListener {

        private float rho = 0f;
        private int resSteps = 10;
        private int stepSize = 4;

        public initRenderer(String heightMapFile) {
            s = loadHeatmap(heightMapFile);
        }

        public void setStepSize(int i) {
            this.stepSize = Math.max(0, Math.min(i, resSteps - 1));
            System.out.println("Received res step to set: " + i);
            System.out.println("Using resolution step: " + this.stepSize);
        }

        public int getStepSize() {
            return stepSize;
        }

        @Override
        public void init(GLAutoDrawable drawable) {

            GL gl = drawable.getGL();
            gl.glShadeModel(gl.GL_SMOOTH); // Enable Smooth Shading
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
            gl.glClearDepth(2000.0f); // Depth Buffer Setup
            gl.glEnable(gl.GL_DEPTH_TEST); // Enables Depth Testing
            gl.glDepthFunc(gl.GL_LEQUAL); // The Type Of Depth Testing To Do
            gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST); // Really
            // //
            // Nice
            // Perspective
            // Calculations
            // s = LoadRawFile(this.heightmapFile, MAP_SIZE * MAP_SIZE,
            // g_HeightMap); // (NEW)

            boolean supportedVBO = gl.isExtensionAvailable("GL_ARB_vertex_buffer_object");
            if (supportedVBO) {
                System.out.println("Vertex Buffer Objects Supported");
            } else {
                System.out.println("Vertex Buffer Objects NOT Supported - part of this example will not execute");

            }

            gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
                    GL.GL_MODULATE);
            // GL.GL_BLEND );
            // GL.GL_REPLACE );
            // GL.GL_DECAL );

            // Add texture highlights.
            // displayListIds = new int[resSteps];
            // for (int i = 0; i < resSteps; i++) {
            s.setStepSize(2);
            s.toggleUseDisplayList();
            s.create(gl);
            // System.out.println(Arrays.toString(displayListIds));

            loop = new FPSAnimator(drawable, 30, true);
            loop.start();
        }

        // public void set2DView(GL gl, int left, int bottom, int width, int
        // height) {
        // gl.glMatrixMode(GL.GL_PROJECTION);
        // gl.glLoadIdentity();
        // gl.glViewport(left, bottom, width, height);
        // gl.glScissor(left, bottom, width, height);
        // gl.glOrtho(left, left + width, bottom, bottom + height, -1, 1);
        //
        // gl.glMatrixMode(GL.GL_MODELVIEW);
        // gl.glLoadIdentity();
        // }
        public void set3DView(GL gl, GLU glu, int left, int bottom, int width,
                int height, float fov, float zNear, float zFar) {
            // set up perspective projection
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            // gl.glViewport(left, bottom, width, height);
            // gl.glScissor(left, bottom, width, height);
            glu.gluPerspective(fov, ((float) width) / ((float) height), zNear,
                    zFar);

            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            processKeyboard();

            GL gl = drawable.getGL();
            GLU glu = new GLU();
            // set clear
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

            gl.glEnable(GL.GL_CULL_FACE);
            // gl.glEnable(GL.GL_LIGHTING);
            // gl.glEnable(GL.GL_LIGHT0);
            gl.glEnable(GL.GL_AUTO_NORMAL);
            gl.glEnable(GL.GL_NORMALIZE);
            gl.glFrontFace(GL.GL_CW);
            gl.glCullFace(GL.GL_BACK);
            gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 64.0f);

            gl.glLoadIdentity(); // Reset The Matrix

            // Position View Up Vector
            glu.gluLookAt(eyeX, eyeY, 10 * eyeZ, 0, 0, 0, 0, 1, 0); // This
            // Determines
            // Where The
            // Camera's
            // Position
            // And View
            // Is

            // gl.glScalef(scaleValue, scaleValue * HEIGHT_RATIO, scaleValue);

            gl.glPushMatrix();
            gl.glTranslatef(tx + (canvasWidth / 2), ty + (canvasHeight / 2), tz);
            gl.glRotatef(rho,0,0,1);
            gl.glRotatef(0, phi, 0, 1);
            gl.glRotatef(0,0,theta,1);
//            gl.glRotatef(rho += 1f, 0, 0, 1);
            // System.out.println("Using displayList: "+displayListIds[STEP_SIZE]);

            s.draw(gl);
            gl.glPopMatrix();

            //display help
            if (toggleHelp) {
                gl.glDisable(GL.GL_DEPTH_TEST);
                //glu.fill(192, 192, 192, 128);
                //textMode(SCREEN);
                //rectMode(CENTER);
//                translate(width / 2, height / 2);
//                rect(0, 0, 0.9f * width, 0.9f * height);
//
//                translate(-width / 2, -height / 2);
//                fill(255, 255, 255);
//                float w = 0.9f * width - 2 * 10;
//                float h = 0.9f * height - 2 * 10;
//                System.out.println("width: " + w + " height: " + h);
//                rectMode(CORNER);
//                text(buildHelpString(), (width / 2) - (0.9f * width / 2) + 10, (height / 2) - (0.9f * height / 2) + 10 + textAscent(), w, h);
                gl.glEnable(GL.GL_DEPTH_TEST);
            }

            gl.glFlush();
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int xstart, int ystart,
                int width, int height) {

            height = (height == 0) ? 1 : height;
            GL gl = drawable.getGL();
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(gl.GL_PROJECTION);
            gl.glLoadIdentity();

            GLU glu = new GLU();
            glu.gluPerspective(60, (float) width / height, 0.1f, 5000);
            gl.glMatrixMode(gl.GL_MODELVIEW);
            gl.glLoadIdentity();
            // set2DView(drawable.getGL(), drawable.getWidth(),
            // drawable.getHeight());
        }

        @Override
        public void displayChanged(GLAutoDrawable drawable,
                boolean modeChanged, boolean deviceChanged) {
        }
    }

    public void processKeyboard() {

        if (keys[KeyEvent.VK_UP]) {// Is the UP ARROW key Being Pressed?
            // float[] t = this.s.getTranslation();
            // this.s.setTranslation(t[0],t[1],t[2]+1); // Increase the
            // scale
            // value
            // to zoom in
            this.eyeZ = this.eyeZ + 1.0f;// +(this.eyeZ/32.0f);
            this.eyeOrthZ++;
        } else if (keys[KeyEvent.VK_DOWN]) {// Is the DOWN ARROW key Being
            // Pressed?
            // float[] t = this.s.getTranslation();
            // this.s.setTranslation(t[0],t[1],t[2]-1); // Decrease the
            // scale
            // value
            // to zoom out
            this.eyeZ = this.eyeZ - 1.0f;// -(this.eyeZ/32.0f);
            this.eyeOrthZ--;
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        System.out.println("Key released: " + evt.getKeyCode());
        // keys[evt.getKeyCode()] = false;
        // System.out.println("Key typed: "+evt.getKeyCode());
        if (keys[KeyEvent.VK_F]) {

            // if (currentTime - lastKeyEvent > typedKeyEventDelay) {
            this.s.toggleWireframe();
            // }
            keys[KeyEvent.VK_F] = false;
        } else if (keys[KeyEvent.VK_MULTIPLY]) {
            // if (currentTime - lastKeyEvent > typedKeyEventDelay) {
            s.setStepSize(s.getStepSize() + 1);
            // }
            keys[KeyEvent.VK_MULTIPLY] = false;
        } else if (keys[KeyEvent.VK_DIVIDE]) {
            // if (currentTime - lastKeyEvent > typedKeyEventDelay) {
            s.setStepSize(s.getStepSize() - 1);
            // }
            keys[KeyEvent.VK_DIVIDE] = false;
        } else if (keys[KeyEvent.VK_L]) {
            // System.out.println("Received 'L' in "
            // + (currentTime - lastKeyEvent) + " with min delay: "
            // + typedKeyEventDelay);
            // if (currentTime - lastKeyEvent > typedKeyEventDelay) {
            this.s.toggleUseDisplayList();
            // }
            keys[KeyEvent.VK_L] = false;
        }
        char key = evt.getKeyChar();
        if (key == 'i' || key == 'o') {
            if (key == 'i') {
                this.s.setScale(this.s.getScale()+1.0f);
                //s.scaleValue += 1.0f;
            }
            if (key == 'o' && s.getScale() > 1) {
                this.s.setScale(this.s.getScale()-1.0f);
//                s.scaleValue -= 1.0f;
            }
        } else if (key == 'w') {
            //ty -= 5;
        } else if (key == 's') {
            //ty += 5;
        } else if (key == 'h') {
            toggleHelp = !toggleHelp;
        } else if (key == 'a') {
            //tx -= 5;
        } else if (key == 'd') {
            //tx += 5;
        } else if (key == 'q') {
            //tz -= 5;
        } else if (key == 'e') {
            //tz += 5;
        } else if (key == 'c') {
            //s.wireframe = !s.wireframe;
        } else if (key == 'm') {
            //s.threshold += 1.0f;
        } else if (key == 'n') {
//            if (s.threshold >= 1.0f) {
//                s.threshold -= 1f;
//            }
        } else if (key == 'x') {
            //s.differential = !s.differential;
        } else if (key == 'f') {
//            this.s.
            //s.flip = !s.flip;
        } else if (key == 'p') {
            //showFileChooser();
            //saveImage(currentOutputFile);
        } else if (key == 'k') {
            //s.sqrtScale = !s.sqrtScale;
        } else if (key == 'l') {
            //s.logScale = !s.logScale;
        } else if (key == 'g') {
            //toggleAnimation();
        } else if (key == '*') {
            //s.STEP_SIZE *= 2;
        } else if (key == '/') {
//            if (s.STEP_SIZE >= 2) {
//                s.STEP_SIZE /= 2;
//            }
        } else {
//            if (key == CODED) {
//                if (keyCode == LEFT) {
//                    if (keyEvent.isAltDown()) {
//                        theta--;
//                    } else {
//                        phi--;
//                    }
//                } else if (keyCode == RIGHT) {
//                    if (keyEvent.isAltDown()) {
//                        theta++;
//                    } else {
//                        phi++;
//                    }
//                } else if (keyCode == UP) {
//                    rho++;
//                } else if (keyCode == DOWN) {
//                    rho--;
//                }
//            }

        }
        // keys[KeyEvent.KEY_TYPED] = false;
        keys[evt.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent evt) {
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        System.out.println("Key pressed: " + evt.getKeyCode());
        keys[evt.getKeyCode()] = true;

        if (keys[KeyEvent.VK_ESCAPE]) {
            loop.stop();
            System.exit(0);
        }
    }

    @Override
    public void mouseExited(MouseEvent evt) {
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        if (evt.getModifiers() == 16) {
            bRender = !bRender;
        }
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
    }


    public class shutDownWindow extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            loop.stop();
        }
    }
}
