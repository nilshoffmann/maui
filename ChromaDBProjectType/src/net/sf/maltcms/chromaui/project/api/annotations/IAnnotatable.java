/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.annotations;

import java.util.List;

/**
 *
 * @author nilshoffmann
 */
public interface IAnnotatable {
    public void addAnnotations(Class<? extends IAnnotation> c, IAnnotation... annotations);

    public void removeAnnotations(Class<? extends IAnnotation> c, IAnnotation... annotations);
    
    public List<IAnnotation> getAnnotations(Class<? extends IAnnotation> annotationType);
}
