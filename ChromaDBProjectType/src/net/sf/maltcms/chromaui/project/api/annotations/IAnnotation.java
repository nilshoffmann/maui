/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.annotations;

import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IAnnotation<T> extends IDescriptor {
    public T getAnnotation();
    
    public void setAnnotation(T t);
}
