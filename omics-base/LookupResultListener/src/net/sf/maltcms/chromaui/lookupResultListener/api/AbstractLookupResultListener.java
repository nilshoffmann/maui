/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.lookupResultListener.api;

import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author nilshoffmann
 */
public abstract class AbstractLookupResultListener<T> implements LookupListener, ILookupResultListener {

    private Result<? extends T> result;

    private Class<? extends T> typeToListenFor;

    private Lookup contentProviderLookup;

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
