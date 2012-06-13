/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibielefeld.gi.kotte.laborprogramm.centralLookup;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Class used to house anything one might want to store
 * in a central lookup which can affect anything within
 * the application. It can be thought of as a central context
 * where any application data may be stored and watched.
 *
 * A singleton instance is created using @see getDefault().
 * This class is as thread safe as Lookup. Lookup appears to be safe.
 * @author Wade Chandler
 * @version 1.0
 */
public class CentralLookup extends AbstractLookup implements LookupListener {

    private InstanceContent content = null;
    private static CentralLookup def = new CentralLookup();
    private Map<Class, Result> results = new LinkedHashMap<Class, Result>();

    public CentralLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public CentralLookup() {
        this(new InstanceContent());
    }

    public <T> void addActionsGlobalContextListener(Class<T> type) {
        if (!results.containsKey(type)) {
            Result<T> result = Utilities.actionsGlobalContext().lookupResult(
                    type);
            result.addLookupListener(this);
            results.put(type, result);
        }

    }

    public <T> void removeActionsGlobalContextListener(Class<T> type) {
        results.remove(type);
    }

    public void add(Object instance) {
        Logger.getLogger(CentralLookup.class.getName()).log(Level.INFO,
                "Adding object " + instance + " to central lookup!");
        if (lookup(instance.getClass()) != null) {
            Object instanceInLookup = lookup(instance.getClass());
            if (instance.equals(instanceInLookup)) {
                Logger.getLogger(CentralLookup.class.getName()).log(Level.INFO,
                        "Object " + instance + " already contained in central lookup!");
            } else {
                content.add(instance);
            }
        } else {
            content.add(instance);
        }
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static CentralLookup getDefault() {
        return def;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result l = (Lookup.Result) ev.getSource();
        Collection<?> c = l.allInstances();
        for (Object obj : c) {
            add(obj);
        }
    }
}
