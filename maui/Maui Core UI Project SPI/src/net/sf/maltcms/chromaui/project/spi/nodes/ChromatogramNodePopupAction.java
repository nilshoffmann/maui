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
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JMenuItem;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(category = "ContainerNodeActions/ChromatogramNode",
        id = "net.sf.maltcms.chromaui.project.spi.nodes.ChromatogramNodePopupAction")
@ActionRegistration(displayName = "#CTL_ChromatogramNodePopupAction", lazy = true)
@NbBundle.Messages("CTL_ChromatogramNodePopupAction=Open")
public final class ChromatogramNodePopupAction extends SystemAction implements Presenter.Menu, Presenter.Popup, ContextAwareAction {

    private final NodePopupMenuAction action = new NodePopupMenuAction(false, "Open","Open", "Actions/ContainerNodeActions/ChromatogramNode/Open");

    @Override
    public void actionPerformed(ActionEvent ev) {
        // do nothing -- should never be called
    }

    @Override
    public String getName() {
        return (String) action.getValue(Action.NAME);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
        // If you will provide context help then use:
        // return new HelpCtx(RSMEditorActionAction.class);
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return action.getMenuPresenter();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return action.getPopupPresenter();
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        if (actionContext.lookupAll(IChromatogramDescriptor.class).isEmpty()) {
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
        return this;
    }

//    private final List<IChromatogramDescriptor> chromatograms;
//    
//    public ChromatogramNodePopupAction(List<IChromatogramDescriptor> chromatograms) {
//        this.chromatograms = chromatograms;
//        setActions(Utilities.actionsForPath("Actions/ContainerNodeActions/ChromatogramNode/Open").toArray(new Action[0]));
//    }
//    
//    @Override
//    public Action createContextAwareInstance(Lookup lkp) {
//        if (!lkp.lookupAll(IChromatogramDescriptor.class).isEmpty()) {
//            setEnabled(true);
//        }else{
//            setEnabled(false);
//        }
//        return super.createContextAwareInstance(lkp);
//    }
}
