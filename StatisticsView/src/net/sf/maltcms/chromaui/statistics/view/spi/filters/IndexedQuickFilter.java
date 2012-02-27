/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.statistics.view.spi.filters;

import org.netbeans.swing.etable.QuickFilter;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class IndexedQuickFilter<T> implements QuickFilter {
    
    private int index = 0;
    
    @Override
    public boolean accept(Object o) {
        if(o instanceof double[]) {
            
        }else if(o instanceof int[]) {
            
        }else if(o instanceof float[]) {
            
        }else if(o instanceof char[]) {
            
        }else if(o instanceof short[]) {
            
        }else if(o instanceof long[]) {
            
        }else if(o instanceof boolean[]) {
            
        }
        return false;
    }
    
}
