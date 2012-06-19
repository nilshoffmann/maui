/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy.api;

import org.netbeans.api.progress.ProgressHandle;
import org.openide.util.Cancellable;
import org.openide.util.Lookup;

/**
 *
 * @author hoffmann
 */
public interface GroovyLookupScript extends Runnable, Cancellable {
    public String getName();
    public void setLookup(Lookup lookup);
    public void setProgressHandle(ProgressHandle progressHandle);
}
