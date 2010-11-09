/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.datastructures.annotations;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import maltcms.datastructures.peak.MaltcmsAnnotationFactory;
import maltcms.datastructures.peak.Peak2D;
import maltcms.io.xml.bindings.annotation.AnnotationType;
import maltcms.io.xml.bindings.annotation.AnnotationsType;
import maltcms.io.xml.bindings.annotation.MaltcmsAnnotation;
import net.sf.maltcms.chromaui.annotations.XYSelectableShapeAnnotation;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author nilshoffmann
 */
public class Peak2DXMLAnnotationProvider implements AnnotationProvider<Peak2D> {

    @Override
    public List<XYAnnotation> load(File f) {
        MaltcmsAnnotationFactory maf = new MaltcmsAnnotationFactory();
        MaltcmsAnnotation ma = maf.load(f);
        List<AnnotationsType> l = ma.getAnnotations();
        List<XYAnnotation> list = new ArrayList<XYAnnotation>(l.size());
        for (AnnotationsType at : l) {
            String type = at.getType();
            if (type.equals(Peak2D.class.getName())) {
                for (AnnotationType atype : at.getAnnotation()) {
                    list.add(create(maf.getPeakAnnotation(atype)));
                }

            } else {
                Logger.getLogger(this.getClass().getName()).warning("Do not know how to handle annotations of type " + type);
            }
        }
        return list;
    }

    @Override
    public void store(List<XYAnnotation> l, File f) {
        MaltcmsAnnotationFactory maf = new MaltcmsAnnotationFactory();
        MaltcmsAnnotation ma = maf.createNewMaltcmsAnnotationType(f.toURI());
        for (XYAnnotation xya : l) {
            if (xya instanceof XYSelectableShapeAnnotation) {
                if (((XYSelectableShapeAnnotation<?>)xya).getT() instanceof Peak2D) {
                    XYSelectableShapeAnnotation<Peak2D> xyp = (XYSelectableShapeAnnotation<Peak2D>) xya;
                    Peak2D p2 = xyp.getT();
                    maf.addPeakAnnotation(ma, getClass().getName(), p2);
                }
            }
        }
        maf.save(ma, f);
    }

    @Override
    public XYAnnotation create(Peak2D t) {
        Point seed = t.getPeakArea().getSeedPoint();
//        PeakArea2D pa2D = t.getPeakArea();
        Shape s = null;
//        if(pa2D==null) {
            s = new Rectangle2D.Double(seed.x - 1, seed.y - 1, 3, 3);
//        }else{
            //List<Point> l = pa2D.getBoundaryPoints();

//        }
        
        XYSelectableShapeAnnotation<Peak2D> xypa = new XYSelectableShapeAnnotation<Peak2D>(seed.x, seed.y, s, t.getName(), TextAnchor.BASELINE_LEFT, t);
        return xypa;
    }
}
