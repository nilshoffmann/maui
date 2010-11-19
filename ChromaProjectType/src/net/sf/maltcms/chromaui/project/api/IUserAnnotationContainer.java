/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.types.IUserAnnotation;

/**
 *
 * @author hoffmann
 */
public interface IUserAnnotationContainer<T extends IUserAnnotation> extends IContainer{

    Collection<T> getUserAnnotations();

    void setUserAnnotations(T... f);

    void addUserAnnotations(T... f);

    void removeUserAnnotationsFiles(T... f);

}
