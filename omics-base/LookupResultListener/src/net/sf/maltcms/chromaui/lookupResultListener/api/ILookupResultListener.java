/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.lookupResultListener.api;

import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author nilshoffmann
 */
public interface ILookupResultListener extends LookupListener {

    void deregister();

    void register(Lookup targetLookup);
    
    @Override
    void resultChanged(LookupEvent result);

}
