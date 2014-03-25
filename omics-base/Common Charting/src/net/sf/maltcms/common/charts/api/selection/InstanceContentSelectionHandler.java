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
package net.sf.maltcms.common.charts.api.selection;

import java.beans.IntrospectionException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection.Type;
import net.sf.maltcms.common.charts.overlay.nodes.SelectionOverlayNode;
import net.sf.maltcms.common.charts.overlay.nodes.SelectionSourceChildFactory;
import org.openide.nodes.Children;
import static org.openide.nodes.Children.create;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import static org.openide.util.Exceptions.printStackTrace;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils Hoffmann
 */
public class InstanceContentSelectionHandler implements ISelectionChangeListener {

    public enum Mode {

        ON_CLICK, ON_HOVER, ON_KEY
    };
    private final InstanceContent content;
    private final Deque<ISelection> activeSelection = new LinkedList<>();
    private final SelectionOverlay overlay;
    private Mode mode;
    private final int capacity;
    private Lookup.Provider dataset;
    // private TARGET lastItem = null;
    private ExecutorService es = newSingleThreadExecutor();
    private AtomicBoolean updatePending = new AtomicBoolean(false);
    private Node selectionOverlayNode;

    public InstanceContentSelectionHandler(InstanceContent content, SelectionOverlay overlay, Mode mode, Lookup.Provider dataset) {
        this(content, overlay, mode, dataset, Integer.MAX_VALUE);
        updateNode();
    }

    public InstanceContentSelectionHandler(InstanceContent content, SelectionOverlay overlay, Mode mode, Lookup.Provider dataset, int capacity) {
        this.content = content;
        this.overlay = overlay;
        this.mode = mode;
        this.dataset = dataset;
        this.capacity = capacity;
    }

    public void setMode(Mode mode) {
        clear();
        this.mode = mode;
    }

    public void setDataset(Lookup.Provider dataset) {
        this.dataset = dataset;
        updateNode();
    }

    private void updateNode() {
        if (selectionOverlayNode != null) {
            content.remove(selectionOverlayNode);
        }
        try {
            selectionOverlayNode = new SelectionOverlayNode(overlay, create(new SelectionSourceChildFactory(overlay, dataset), true));
        } catch (IntrospectionException ex) {
            printStackTrace(ex);
            selectionOverlayNode = Node.EMPTY;
        }
        content.add(selectionOverlayNode);
    }

    private void addToSelection(ISelection selection) {
        if (!activeSelection.contains(selection)) {
            if (activeSelection.size() == capacity) {
                ISelection removed = activeSelection.removeFirst();
                content.remove(removed);
            }
            activeSelection.add(selection);
            content.add(selection);
            updateNode();
        }
    }

    private void removeFromSelection(ISelection selection) {
        content.remove(selection);
        activeSelection.remove(selection);
        updateNode();
    }

    public void clear() {
        for (Object pt : activeSelection) {
            content.remove(pt);
        }
        activeSelection.clear();
        overlay.clear();
        updateNode();
    }

    @Override
    public void selectionStateChanged(SelectionChangeEvent ce) {
        if (ce.getSelection() != null) {
            Type type = ce.getSelection().getType();
            switch (type) {
                case CLEAR:
                    System.out.println("Clearing instance content!");
                    clear();
                    break;
                case CLICK:
                    if (mode == Mode.ON_CLICK) {
                        if (activeSelection.contains(ce.getSelection())) {
                            removeFromSelection(ce.getSelection());
                        } else {
                            addToSelection(ce.getSelection());
                        }
                    }
                    break;
                case HOVER:
                    if (mode == Mode.ON_HOVER) {
                        if (activeSelection.contains(ce.getSelection())) {
                            removeFromSelection(ce.getSelection());
                        } else {
                            addToSelection(ce.getSelection());
                        }
                    }
                    break;
                case KEYBOARD:
                    if (mode == Mode.ON_KEY) {
                        if (activeSelection.contains(ce.getSelection())) {
                            removeFromSelection(ce.getSelection());
                        } else {
                            addToSelection(ce.getSelection());
                        }
                    }
                    break;
            }
        }
    }
}
