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
package net.sf.maltcms.chromaui.normalization.spi.nodes;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class AnovaNode extends BeanNode {

    public AnovaNode(Object bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }

    public AnovaNode(Object bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public AnovaNode(Object bean) throws IntrospectionException {
        super(bean);
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] actions = super.getActions(context);
        List<Action> finalActions = new ArrayList<Action>();
        List<? extends Action> pathActions = Utilities.actionsForPath(
                "Actions/DescriptorNodeActions/IPeakGroupDescriptor");
        finalActions.addAll(pathActions);
        finalActions.add(null);
        finalActions.addAll(Utilities.actionsForPath("Actions/DescriptorNodeActions/DefaultActions"));
        finalActions.addAll(Arrays.asList(actions));
        return finalActions.toArray(new Action[finalActions.size()]);
    }
}
