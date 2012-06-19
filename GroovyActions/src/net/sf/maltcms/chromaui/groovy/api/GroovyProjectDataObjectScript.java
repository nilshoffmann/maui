/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy.api;

import java.util.Collection;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObject;
import org.openide.util.Cancellable;

/**
 *
 * @author nilshoffmann
 */
public interface GroovyProjectDataObjectScript<T extends Project, U extends DataObject> extends Runnable, Cancellable {
    public String getName();
    public String getCategory();
    public void create(T project, ProgressHandle progressHandle, Collection<? extends U> dobjects);
//    public void setProject(T project);
//    public void setDataObjects(U...dobjects);
//    public void setProgressHandle(ProgressHandle progressHandle);
}
