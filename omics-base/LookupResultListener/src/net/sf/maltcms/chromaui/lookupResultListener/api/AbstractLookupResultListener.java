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
package net.sf.maltcms.chromaui.lookupResultListener.api;

import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class AbstractLookupResultListener<T> implements LookupListener, ILookupResultListener {

    private Result<? extends T> result;

    private final Class<? extends T> typeToListenFor;

    private final Lookup contentProviderLookup;

    public AbstractLookupResultListener(Class<? extends T> typeToListenFor) {
        this(typeToListenFor,Utilities.actionsGlobalContext());
    }

    public AbstractLookupResultListener(Class<? extends T> typeToListenFor, Lookup contentProviderLookup) {
        this.typeToListenFor = typeToListenFor;
        this.contentProviderLookup = contentProviderLookup;
    }

    @Override
    public void register(Lookup targetLookup) {
        result = targetLookup.lookupResult(typeToListenFor);
        if(result!=null) {
            result.addLookupListener(this);
            resultChanged(new LookupEvent(result));
        }
    }

    @Override
    public void deregister() {
        if(result!=null) {
            result.removeLookupListener(this);
        }
        result = null;
    }

    public Result<? extends T> getResult() {
        return result;
    }

    public Lookup getContentProviderLookup() {
        return contentProviderLookup;
    }

    public Class<? extends T> getTypeToListenFor() {
        return typeToListenFor;
    }

}
