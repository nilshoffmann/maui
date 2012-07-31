/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.beans.Encoder;
import java.beans.Statement;
import java.util.ArrayList;

/**
 *
 * @author hoffmann
 */
public class Path2DPersistenceHelper {

    int windingRule;
    ArrayList segments = new ArrayList();
    int[] segTypes;
    float[][] segCoords;
    // Number of segments in the path
    int segCnt = 0;
    
    public Path2DPersistenceHelper(Shape shape) {
        this(new Path2D.Float(shape));
    }

    public Path2DPersistenceHelper(Path2D path) {
        // Winding rule
        this.windingRule = path.getWindingRule();

        // Determine how many segments there are in the path
        PathIterator pi = path.getPathIterator(new AffineTransform());
        while (!pi.isDone()) {
            pi.next();
            segCnt++;
        }
        // Create an internal representation of the segment types and their
        // coordinates
        segTypes = new int[segCnt];
        segCoords = new float[segCnt][];
        PathIterator pi2 = path.getPathIterator(new AffineTransform());
        int i = 0;
        while (!pi2.isDone()) {
            float[] c = new float[6];
            segTypes[i] = pi2.currentSegment(c);
            segCoords[i] = c;
            pi2.next();
            i++;
        }
    }

    /**
     * Generates the statements needed for generating the restored state of the
     * GeneralPath used to construct this descriptor
     *
     * @param out
     * @param path
     */
    public Shape generate() {
//        out.writeStatement(new Statement(path,
//                "setWindingRule",
//                new Object[]{new Integer(path.getWindingRule())}));
        Path2D.Float p = new Path2D.Float();
        for (int i = 0; i < segTypes.length; i++) {
            int segType = segTypes[i];
            float[] coords = segCoords[i];
            String statementName = null;
            Object[] parms = null;
            switch (segType) {
                case PathIterator.SEG_CLOSE:
                    statementName = "closePath";
                    parms = new Object[]{};
                    break;
                case PathIterator.SEG_CUBICTO:
                    statementName = "curveTo";
                    parms = new Object[]{new Float(coords[0]), new Float(coords[1]), new Float(coords[2]),
                        new Float(coords[3]), new Float(coords[4]), new Float(coords[5])};
                    break;
                case PathIterator.SEG_LINETO:
                    statementName = "lineTo";
                    parms = new Object[]{new Float(coords[0]), new Float(coords[1])};
                    break;
                case PathIterator.SEG_MOVETO:
                    statementName = "moveTo";
                    parms = new Object[]{new Float(coords[0]), new Float(coords[1])};
                    break;
                case PathIterator.SEG_QUADTO:
                    statementName = "quadTo";
                    parms = new Object[]{new Float(coords[0]), new Float(coords[1]), new Float(coords[2]),
                        new Float(coords[3])};
                    break;
            }
            
        }
        return p.createTransformedShape(AffineTransform.getTranslateInstance(0, 0));
    }
}
