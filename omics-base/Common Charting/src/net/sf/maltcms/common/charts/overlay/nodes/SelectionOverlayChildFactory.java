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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import static org.openide.util.Exceptions.printStackTrace;
import org.openide.util.WeakListeners;
import static org.openide.util.lookup.Lookups.fixed;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionOverlayChildFactory extends ChildFactory<ISelection> {

    private final SelectionOverlay so;
    private final Set<ISelection> selection;
    private final Object source;

    public SelectionOverlayChildFactory(Object source, Set<ISelection> selection, SelectionOverlay so) {
        this.so = so;
        this.source = source;
        this.selection = selection;
        so.addPropertyChangeListener(WeakListeners.propertyChange(new SelectionOverlayPropertyChangeListener(), so));
    }

    @Override
    protected boolean createKeys(List<ISelection> list) {
        list.addAll(selection);
        return true;
    }

    @Override
    protected Node createNodeForKey(ISelection key) {
        try {
            SelectionNode selectionNode = new SelectionNode(key);
            FilterNode fn = new FilterNode(selectionNode, Children.LEAF, new ProxyLookup(selectionNode.getLookup(), fixed(key, key.getSource(), key.getTarget())));
            return fn;
        } catch (IntrospectionException ex) {
            printStackTrace(ex);

        }
        return Node.EMPTY;
    }

    private class SelectionOverlayPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getPropertyName().equals(SelectionOverlay.PROP_SELECTION)) {
                refresh(true);
            }
        }
    }
}
