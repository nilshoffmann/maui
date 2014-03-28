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
package de.unibielefeld.gi.kotte.laborprogramm.centralLookup.impl;

import de.unibielefeld.gi.kotte.laborprogramm.centralLookup.CentralLookup;
import de.unibielefeld.gi.kotte.laborprogramm.centralLookup.ICentralLookupFactory;
import java.util.concurrent.ConcurrentHashMap;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nils Hoffmann
 */
@ServiceProvider(service = ICentralLookupFactory.class)
public class CentralLookupFactory implements ICentralLookupFactory {

    private ConcurrentHashMap<String, CentralLookup> namedLookupMap = new ConcurrentHashMap<>();

    @Override
    public CentralLookup getDefault() {
        return CentralLookup.getDefault();
    }

    @Override
    public CentralLookup getNamed(String name) {
        CentralLookup lookup = null;
        if (namedLookupMap.containsKey(name)) {
            lookup = namedLookupMap.get(name);
        } else {
            lookup = new CentralLookup();
            namedLookupMap.put(name, lookup);
        }
        return lookup;
    }

    @Override
    public void removeNamed(String name) {
        if (namedLookupMap.containsKey(name)) {
            namedLookupMap.remove(name);
        }
    }
}
