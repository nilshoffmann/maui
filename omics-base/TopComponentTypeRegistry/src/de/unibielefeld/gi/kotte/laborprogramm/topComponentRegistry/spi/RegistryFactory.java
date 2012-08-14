/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.spi;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 * RegistryFactory is the default implementation for @see IRegistryFactory.
 * Other Implementations may be retrieved by calling 
 * @see Lookup.getDefault().lookupAll(IRegistryFactory.class).
 * 
 * @author hoffmann
 */
@ServiceProvider(service=IRegistryFactory.class)
public class RegistryFactory implements IRegistryFactory{

    private static IRegistry registry;

    @Override
    public IRegistry getDefault() {
        if(registry==null) {
            registry = new Registry();
        }
        return registry;
    }

}
