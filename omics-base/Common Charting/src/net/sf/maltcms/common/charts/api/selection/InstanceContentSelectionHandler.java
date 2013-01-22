/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.api.selection;

import java.beans.IntrospectionException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.overlay.nodes.SelectionOverlayNode;
import net.sf.maltcms.common.charts.overlay.nodes.SelectionSourceChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Nils Hoffmann
 */
public class InstanceContentSelectionHandler implements ISelectionChangeListener {

    public enum Mode {

        ON_CLICK, ON_HOVER, ON_KEY
    };
    private final InstanceContent content;
    private final Deque<ISelection> activeSelection = new LinkedList<ISelection>();
    private final SelectionOverlay overlay;
    private final Mode mode;
    private final int capacity;
    private final ADataset1D dataset;
    // private TARGET lastItem = null;
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private AtomicBoolean updatePending = new AtomicBoolean(false);
    private Node selectionOverlayNode;

    public InstanceContentSelectionHandler(InstanceContent content, SelectionOverlay overlay, Mode mode, ADataset1D dataset) {
        this(content, overlay, mode, dataset, Integer.MAX_VALUE);
        updateNode();
    }

    public InstanceContentSelectionHandler(InstanceContent content, SelectionOverlay overlay, Mode mode, ADataset1D dataset, int capacity) {
        this.content = content;
        this.overlay = overlay;
        this.mode = mode;
        this.dataset = dataset;
        this.capacity = capacity;
    }

    private void updateNode() {
        if (selectionOverlayNode != null) {
            content.remove(selectionOverlayNode);
        }
        try {
            selectionOverlayNode = new SelectionOverlayNode(overlay, Children.create(new SelectionSourceChildFactory(overlay, dataset), true));
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
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
            XYSelection.Type type = ce.getSelection().getType();
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
                        addToSelection(ce.getSelection());
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
