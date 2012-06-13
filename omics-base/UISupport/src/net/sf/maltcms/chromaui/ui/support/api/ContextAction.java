/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.ui.support.api;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author nilshoffmann
 */
public abstract class ContextAction<T> extends AbstractAction implements
        LookupListener, ContextAwareAction {

    private Lookup context;
    Lookup.Result<? extends T> lkpInfo;
    private Class<? extends T> contextType;

    public ContextAction() {
        this(Utilities.actionsGlobalContext());
    }

    public ContextAction(Class<? extends T> contextType) {
        this();
        this.contextType = contextType;
    }

    protected ContextAction(Lookup lkp) {
        this.context = lkp;
    }

    void init() {
        if (SwingUtilities.isEventDispatchThread()) {
            if (lkpInfo != null) {
                return;
            }

            //The thing we want to listen for the presence or absence of
            //on the global selection
            lkpInfo = context.lookupResult(contextType);
            lkpInfo.addLookupListener(this);
            //resultChanged(null);
            setEnabled(false);
        }
    }

    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(!lkpInfo.allInstances().isEmpty());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        init();
        doAction(lkpInfo.allInstances());
    }

    public abstract void doAction(Collection<? extends T> instances);
}
