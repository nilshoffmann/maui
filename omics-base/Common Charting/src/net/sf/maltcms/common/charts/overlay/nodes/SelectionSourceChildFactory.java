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
package net.sf.maltcms.common.charts.overlay.nodes;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import static org.openide.nodes.Children.create;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import static org.openide.util.Exceptions.printStackTrace;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import static org.openide.util.lookup.Lookups.fixed;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionSourceChildFactory extends ChildFactory<Object> implements PropertyChangeListener {

    private final Map<Object, Set<ISelection>> sourceToSelection = new LinkedHashMap<>();
    private final SelectionOverlay so;
    private final Lookup.Provider dataset;

    public SelectionSourceChildFactory(SelectionOverlay so, Lookup.Provider dataset) {
        recreateSelection();
        so.addPropertyChangeListener(WeakListeners.propertyChange(this, so));
        this.so = so;
        this.dataset = dataset;
    }

    private void recreateSelection() {
        if (so != null && so.getMouseClickSelection() != null) {
            sourceToSelection.clear();
            for (ISelection sel : so.getMouseClickSelection()) {
                if (sourceToSelection.containsKey(sel.getSource())) {
                    Set<ISelection> selection = sourceToSelection.get(sel.getSource());
                    selection.add(sel);
                } else {
                    Set<ISelection> selection = new LinkedHashSet<>();
                    selection.add(sel);
                    sourceToSelection.put(sel.getSource(), selection);
                }
            }
        }
    }

    @Override
    protected boolean createKeys(List<Object> list) {
        list.addAll(sourceToSelection.keySet());
        return true;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        try {
            ISelection selection = sourceToSelection.get(key).iterator().next();
            Lookup lookup = fixed(selection, dataset.getLookup().lookup(IDisplayPropertiesProvider.class));
            SourceNode sn = new SourceNode(key, create(new SelectionOverlayChildFactory(key, sourceToSelection.get(key), so), true), lookup);
            return sn;
        } catch (IntrospectionException ex) {
            printStackTrace(ex);
            return Node.EMPTY;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(SelectionOverlay.PROP_SELECTION)) {
            recreateSelection();
            refresh(true);
        }
    }
}
