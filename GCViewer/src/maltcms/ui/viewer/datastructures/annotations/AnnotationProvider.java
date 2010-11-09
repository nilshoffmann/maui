/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.viewer.datastructures.annotations;

import java.io.File;
import java.util.List;
import org.jfree.chart.annotations.XYAnnotation;

/**
 *
 * @author nilshoffmann
 */
public interface AnnotationProvider<T> {

    public List<XYAnnotation> load(File f);

    public void store(List<XYAnnotation> l, File f);

    public XYAnnotation create(T t);

}
