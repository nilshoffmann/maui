/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.lookupResultListener.api;

import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
 */
public interface ILookupResultListener {

    void deregister();

    void register(Lookup targetLookup);

}
