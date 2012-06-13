/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author nilshoffmann
 */
public interface IAnnotation<T> extends IBasicDescriptor {
    public T getAnnotation();
    
    public void setAnnotation(T t);
}
