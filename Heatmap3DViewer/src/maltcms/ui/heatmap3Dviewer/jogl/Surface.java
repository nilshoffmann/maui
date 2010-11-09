/**
 * 
 */
package maltcms.ui.heatmap3Dviewer.jogl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.media.j3d.Texture;
import javax.media.jai.ImageMIPMap;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 * 
 * 
 */
public class Surface {

    private int STEP_SIZE = 4;
    private float scaleValue = 1.0f;
    private boolean logScale = false;
    private boolean flip = false;
    private boolean differential = false;
    private boolean wireframe = false;
    private float threshold = 0.0f;
    private int alphav = 192;
    private float ZZ = 0;
    private int currentSample = 0;
    private int maxSample = 0;
    private BufferedImage currentImage;
    private float scaleFactor = 1.0f;
    private int dlID = -1;
    private boolean useDisplayList = false;
    private boolean removeDisplayList = false;
    private Rectangle2D.Float selection;

    public Surface(String file, int maxZoomLevels) {
        this.maxSample = maxZoomLevels;
        this.currentImage = PlanarImage.wrapRenderedImage(
                JAI.create("fileload", file)).getAsBufferedImage();// subSampleImage(maxZoomLevels,
        // scaleFactorBetweenLevels,
        // file);
        // this.numBands = this.currentImage.getSampleModel().getNumBands();
        this.currentSample = 0;
        // this.currentImage = this.im.getCurrentImage();
    }

    public void zoomIn() {
        // this.currentImage = this.im.getImage(++this.currentSample);
    }

    public void zoomOut() {
        // this.currentImage = this.im.getImage(--this.currentSample);
    }

    public void toggleUseDisplayList() {
        this.useDisplayList = !this.useDisplayList;
        System.out.println("Toggle useDisplayList: " + this.useDisplayList);
        if (!useDisplayList && dlID != -1) {
            removeDisplayList = true;
        }
    }

    public boolean isUseDisplayList() {
        return this.useDisplayList;
    }

    public int getNumberOfScales() {
        return this.maxSample;
    }

    public void setStepSize(int stepSize) {
        if (this.STEP_SIZE == stepSize) {
            return;
        }
        System.out.println("Setting step size to: " + stepSize);
        this.STEP_SIZE = stepSize;
        if (useDisplayList) {
            removeDisplayList = true;
        }
    }

    public int getStepSize() {
        return this.STEP_SIZE;
    }

    public void setScale(float f) {
        this.scaleValue = f;
    }

    public float getScale() {
        return this.scaleValue;
    }

    // public boolean toggleUseTexture() {
    // this.useTexture = !this.useTexture;
    // System.out.println("Toggle useTexture: "+this.useTexture);
    // if(this.useTexture && this.texture == null) {
    // this.genTexture = true;
    // this.removeDisplayList = true;
    // }
    // System.out.println("Generating texture: "+this.genTexture);
    // return this.useTexture;
    // }
    //
    // public boolean isUseTexture() {
    // return this.useTexture;
    // }
    float getHeightFor(Raster r, int X, int Y) {// This Returns The Height From
        // A Height Map Index
        int x = X % r.getWidth(); // Error Check Our x Value
        int y = Y % r.getHeight(); // Error Check Our y Value
        float h = 0;
        int[] vals = new int[r.getNumBands()];
        // if (vals[0] != 0 || vals[1] != 0 || vals[2] != 0) {
        // System.out.println(Arrays.toString(vals));
        // }

        vals = r.getPixel(x, y, vals);
        if (differential) {
            h = ((float) (vals[0] - vals[1] + vals[2]))
                    / (float) (2.0f * 255.0f);
        } else {
            h = ((float) (vals[0] + vals[1] + vals[2])) / (3.0f * 255.0f);
        }
        if (Math.abs(h) < threshold) {
            return 0;
        }
        if (logScale) {
            return (flip) ? (-(scaleValue * ((float) Math.log(1.0 + h))))
                    : ((scaleValue * ((float) Math.log(1.0 + h)))); // Index
            // Into Our
            // Height
            // Array And
            // Return
            // The
            // Height
        }
        return h * 255;
        // return (flip) ? (-(scaleValue * (h))) : ((scaleValue * (h))); //
        // Index
        // Into
        // Our
        // Height
        // Array
        // And
        // Return
        // The
        // Height
    }

    void setVertexColor(GL gl, Raster r, int x, int y) {
        if (r.getNumBands() == 3) {
            gl.glColor4f(r.getSampleFloat(x, y, 0) / 255.0f, r.getSampleFloat(x, y,
                    1) / 255.0f, r.getSampleFloat(x, y, 2) / 255.0f, 1.0f);
        } else if (r.getNumBands() == 4) {
            gl.glColor4f(r.getSampleFloat(x, y, 0) / 255.0f, r.getSampleFloat(x, y,
                    1) / 255.0f, r.getSampleFloat(x, y, 2) / 255.0f, r.getSampleFloat(x, y, 3) / 255.0f);
        }
    }

    public int getWidth() {
        return this.currentImage.getWidth();
    }

    public int getHeight() {
        return this.currentImage.getHeight();
    }

    void createWithDisplayList(GL gl) {
        // generate list
        dlID = gl.glGenLists(1);
        // System.out.println("Generating display list: " + dlID);
        gl.glNewList(dlID, GL.GL_COMPILE);
        createPerCall(gl);
        gl.glEndList();
    }

    void draw(GL gl) {
        if (useDisplayList) {
            // System.out.println("Using display list: "+dlID);
            if (removeDisplayList) {
                // System.out.println("Deleting display list: " + dlID);
                gl.glDeleteLists(dlID, 1);
                dlID = -1;
                removeDisplayList = false;
                createWithDisplayList(gl);
            }
            gl.glCallList(dlID);
        } else {
            // System.out.println("Using immediate mode rendering");
            if (dlID != -1) {
                // System.out.println("Deleting display list: " + dlID);
                gl.glDeleteLists(dlID, 1);
                dlID = -1;
            }
            createPerCall(gl);
        }
    }

    void setSelection(Rectangle2D.Float selection) {
        this.selection = selection;
        removeDisplayList = true;
    }

    void create(GL gl) {
        if (useDisplayList) {
            createWithDisplayList(gl);
        }
    }

    void createPerCall(GL gl) {
        // center in 0,0,0

        gl.glPushMatrix();
        gl.glTranslatef(-this.currentImage.getWidth() / 2, -this.currentImage.getHeight() / 2, 0);

        // if (useTexture) {
        // if (genTexture) {
        // texture = com.sun.opengl.util.texture.TextureIO.newTexture(
        // this.currentImage, true);
        // }
        // if(texture !=null) {
        // texture.enable();
        // texture.bind();
        // gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
        // GL.GL_MODULATE);//GL_MODULATE);
        // gl.glEnable(GL.GL_TEXTURE_GEN_S);
        // gl.glTexGenf(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_OBJECT_LINEAR);
        // // gl.glTexGenf(GL.GL_R, GL.GL_TEXTURE_GEN_MODE,
        // GL.GL_OBJECT_LINEAR);
        // // gl.glEnable(GL.GL_TEXTURE_GEN_R);
        // gl.glTexGenfv(GL.GL_S, GL.GL_OBJECT_PLANE, FloatBuffer.wrap(new
        // float[]{0,1,0,0}));
        //
        // }
        // }else {
        // if(texture != null) {
        // texture.disable();
        // gl.glDisable(GL.GL_TEXTURE_GEN_S);
        // }
        // }
        Raster r = this.currentImage.getData();
        int X = 0, Y = 0; // Create Some Variables To Walk The Array With.
        float x, y, z; // Create Some Variables For Readability
        if (!wireframe) { // What We Want To Render
            // beginShape(QUADS);
            gl.glBegin(GL.GL_QUADS); // Render Polygons
        } else {
            // beginShape(LINES);
            gl.glBegin(GL.GL_LINES); // Render Lines Instead
        }

        if (STEP_SIZE < 0) {
            STEP_SIZE = 0;
        }
        int sz = STEP_SIZE + 1;
        int maxx = 0;
        int maxy = 0;
        for (X = 0; X < (r.getWidth() - sz); X += sz) {
            for (Y = 0; Y < (r.getHeight() - sz); Y += sz) {
                // Get The (X, Y, Z) Value For The Bottom Left Vertex
                x = X;
                y = Y;
                z = getHeightFor(r, X, Y);
                // System.out.println("Height 0: "+z);
                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, r, (int) x, (int) y);
                gl.glVertex3f(x, y, z);// Send This Vertex To OpenGL To Be
                // Rendered

                // Get The (X, Y, Z) Value For The Top Left Vertex
                x = X;
                y = Y + sz;
                z = getHeightFor(r, X, Y + sz);
                // System.out.println("Height 1: "+z);
                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, r, (int) x, (int) y);
                gl.glVertex3f(x, y, z);// Send This Vertex To OpenGL To Be
                // Rendered

                // Get The (X, Y, Z) Value For The Top Right Vertex
                x = X + sz;
                y = Y + sz;
                z = getHeightFor(r, X + sz, Y + sz);
                // System.out.println("Height 2: "+z);
                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, r, (int) x, (int) y);
                gl.glVertex3f(x, y, z);// Send This Vertex To OpenGL To Be
                // Rendered

                // Get The (X, Y, Z) Value For The Bottom Right Vertex
                x = X + sz;
                y = Y;
                z = getHeightFor(r, X + sz, Y);
                // System.out.println("Height 3: "+z);
                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, r, (int) x, (int) y);
                gl.glVertex3f(x, y, z);// Send This Vertex To OpenGL To Be
                // Rendered
                maxx = Math.max(maxx, X + sz);
                maxy = Math.max(maxy, Y + sz);
            }
        }

        gl.glEnd();



        gl.glPopMatrix();
        // gl.glLoadIdentity();
    }

    private void createSelection(GL gl) {
        if (this.selection != null) {
        }
    }

    synchronized void toggleWireframe() {
        this.wireframe = !this.wireframe;
        this.removeDisplayList = true;
    }

    void increaseStepSize() {
        this.STEP_SIZE++;
    }

    void decreaseStepSize() {
        this.STEP_SIZE = Math.max(0, --this.STEP_SIZE);
    }
}
