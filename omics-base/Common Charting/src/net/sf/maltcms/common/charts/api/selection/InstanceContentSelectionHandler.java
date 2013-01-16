/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.api.selection;

import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
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
    private final Set<Object> activeSelection = new LinkedHashSet<Object>();
    private final SelectionOverlay overlay;
    private final Mode mode;

    public InstanceContentSelectionHandler(InstanceContent content, SelectionOverlay overlay, Mode mode) {
        this.content = content;
        this.overlay = overlay;
        this.mode = mode;
    }

    public void addToSelection(Object point) {
        activeSelection.add(point);
        content.add(point);
    }

    public void removeFromSelection(Object point) {
        content.remove(point);
        activeSelection.remove(point);
    }

    public void clear() {
        for (Object pt : activeSelection) {
            content.remove(pt);
        }
        activeSelection.clear();
        overlay.clear();
    }

    @Override
    public void selectionStateChanged(SelectionChangeEvent ce) {
        if (ce.getSelection() != null) {
            XYSelection.Type type = ce.getSelection().getType();
            switch (type) {
                case CLEAR:
                    clear();
                    break;
                case CLICK:
                    if (mode == Mode.ON_CLICK) {
                        if (activeSelection.contains(ce.getSelection().getPayload())) {
                            removeFromSelection(ce.getSelection().getPayload());
                        } else {
                            addToSelection(ce.getSelection().getPayload());
                        }
                    }
                    break;
                case HOVER:
                    if (mode == Mode.ON_HOVER) {
                        addToSelection(ce.getSelection().getPayload());
                    }
                    break;
                case KEYBOARD:
                    if (mode == Mode.ON_KEY) {
                        if (activeSelection.contains(ce.getSelection().getPayload())) {
                            removeFromSelection(ce.getSelection().getPayload());
                        } else {
                            addToSelection(ce.getSelection().getPayload());
                        }
                    }
                    break;
            }
        }
    }
}
