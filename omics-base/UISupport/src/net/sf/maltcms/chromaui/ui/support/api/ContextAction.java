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
package net.sf.maltcms.chromaui.ui.support.api;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class ContextAction<T> extends AbstractAction implements
        LookupListener, ContextAwareAction, Lookup.Provider {

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

    @Override
    public Lookup getLookup() {
        return context;
    }
   
    public abstract void doAction(Collection<? extends T> instances);
}
