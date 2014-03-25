/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package de.unibielefeld.gi.kotte.laborprogramm.centralLookup;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import static org.openide.util.Utilities.actionsGlobalContext;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Class used to house anything one might want to store in a central lookup
 * which can affect anything within the application. It can be thought of as a
 * central context where any application data may be stored and watched.
 *
 * A singleton instance is created using @see getDefault(). This class is as
 * thread safe as Lookup. Lookup appears to be safe.
 *
 * @author Wade Chandler
 * @version 1.0
 */
public class CentralLookup extends AbstractLookup implements LookupListener {

    private InstanceContent content = null;
    private static CentralLookup def = new CentralLookup();
    private Map<Class, Result> results = new LinkedHashMap<>();

    public CentralLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public CentralLookup() {
        this(new InstanceContent());
    }

    public <T> void addActionsGlobalContextListener(Class<T> type) {
        if (!results.containsKey(type)) {
            Result<T> result = actionsGlobalContext().lookupResult(
                    type);
            result.addLookupListener(this);
            results.put(type, result);
        }

    }

    public <T> void removeActionsGlobalContextListener(Class<T> type) {
        results.remove(type);
    }

    public void add(Object instance) {
        getLogger(CentralLookup.class.getName()).log(Level.INFO,
                "Adding object " + instance + " to central lookup!");
        if (lookup(instance.getClass()) != null) {
            Object instanceInLookup = lookup(instance.getClass());
            if (instance.equals(instanceInLookup)) {
                getLogger(CentralLookup.class.getName()).log(Level.INFO,
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.content);
        hash = 37 * hash + Objects.hashCode(this.results);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CentralLookup other = (CentralLookup) obj;
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.results, other.results)) {
            return false;
        }
        return true;
    }

}
