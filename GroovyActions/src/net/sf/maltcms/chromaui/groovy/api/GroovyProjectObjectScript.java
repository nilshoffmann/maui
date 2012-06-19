/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy.api;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.openide.util.Cancellable;

/**
 *
 * @author hoffmann
 */
public interface GroovyProjectObjectScript<T extends Project, U> extends Runnable, Cancellable {
    public String getName();
    public String getCategory();
    public void create(T project, ProgressHandle progressHandle, U...objects);    
}
