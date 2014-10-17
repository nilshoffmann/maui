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
package net.sf.maltcms.common.charts.overlay.nodes;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import static org.openide.util.Utilities.actionsForPath;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionOverlayNode extends OverlayNode<SelectionOverlay> {

    public SelectionOverlayNode(SelectionOverlay bean) throws IntrospectionException {
        super(bean);
    }

    public SelectionOverlayNode(SelectionOverlay bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public SelectionOverlayNode(SelectionOverlay bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] actions = super.getActions(context);
        List<? extends Action> selectionActions = actionsForPath("Actions/OverlayNodeActions/SelectionOverlay");
        List<Action> nodeActions = new ArrayList<>(selectionActions);
        nodeActions.add(null);
        for (Action a : actions) {
            if (a != null && a.isEnabled()) {
                nodeActions.add(a);
            } else if (a == null) {
                nodeActions.add(a);
            }
        }
        return nodeActions.toArray(new Action[nodeActions.size()]);
    }
}
