/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import net.sf.maltcms.chromaui.project.api.IDescriptor;

/**
 *
 * @author hoffmann
 */
public interface IUserAnnotation<T> extends IDescriptor{

    public void setUserAnnotationType(T t);

    public T getUserAnnotationType();

}
