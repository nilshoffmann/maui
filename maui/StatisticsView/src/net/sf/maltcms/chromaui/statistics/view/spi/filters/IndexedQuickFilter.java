/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
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
