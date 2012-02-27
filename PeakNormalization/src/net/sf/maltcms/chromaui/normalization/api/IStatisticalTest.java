/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.api;

import org.rosuda.REngine.Rserve.RSession;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IStatisticalTest {
    public String getName();
    
    public void setName();
    
    public void perform(RSession session);
}
