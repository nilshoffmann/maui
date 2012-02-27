/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.charts.dataset;

import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.datastructures.IElementProvider;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface NamedElementProvider<SOURCE,TARGET> extends IElementProvider<TARGET> {
    
    public SOURCE getSource();
    
    public Comparable getKey();
    
    public void setKey(Comparable key);
    
    public IChromAUIProject getProject();
    
    public void setProject(IChromAUIProject project);
    
}
