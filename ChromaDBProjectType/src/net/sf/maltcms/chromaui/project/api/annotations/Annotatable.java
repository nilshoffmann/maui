/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.annotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author nilshoffmann
 */
public class Annotatable implements IAnnotatable {
    private HashMap<Class<? extends IAnnotation>,List<IAnnotation>> annotations = new LinkedHashMap<Class<? extends IAnnotation>,List<IAnnotation>>();
    
    @Override
    public void addAnnotations(Class<? extends IAnnotation> c, IAnnotation... annotations) {
        List<IAnnotation> l = this.annotations.get(c);
        if(l==null) {
            l = Arrays.asList(annotations);
        }else{
            l.addAll(Arrays.asList(annotations));
        }
    }

    @Override
    public List<IAnnotation> getAnnotations(Class<? extends IAnnotation> annotationType) {
        List<IAnnotation> l = this.annotations.get(annotationType);
        if(l==null) {
            return Collections.emptyList();
        }
        return l;
    }

    @Override
    public void removeAnnotations(Class<? extends IAnnotation> c, IAnnotation... annotations) {
        List<IAnnotation> l = this.annotations.get(c);
        if(l!=null) {
            l.removeAll(Arrays.asList(annotations));
        }
    }
}
