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
package net.sf.maltcms.chromaui.chromatogram2Dviewer.datastructures.annotations;

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
