/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.loaders.DataObject;

/**
 *
 * @author nilshoffmann
 */
public interface RawDataGroovyScript extends Runnable {
    
    public void setProject(IChromAUIProject project);
    public void setDataObjects(DataObject...dobjects);
    
}
