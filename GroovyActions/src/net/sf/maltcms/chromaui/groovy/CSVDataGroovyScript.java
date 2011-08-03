/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import maltcms.ui.fileHandles.csv.CSVDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

/**
 *
 * @author nilshoffmann
 */
public interface CSVDataGroovyScript extends Runnable {
    public void setProject(IChromAUIProject project);
    public void setDataObjects(CSVDataObject...dobjects);
}
