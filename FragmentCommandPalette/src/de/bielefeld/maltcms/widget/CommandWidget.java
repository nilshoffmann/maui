/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.widget;

import de.bielefeld.maltcms.nodes.CommandNode;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author mwilhelm
 */
public class CommandWidget extends LabelWidget {

    private CommandNode cNode;

    public CommandWidget(Scene scene, CommandNode node) {
        super(scene);
        this.cNode = node;
    }

    public CommandWidget(Scene scene, String label, CommandNode node) {
        super(scene, label);
        this.cNode = node;
    }

    public CommandNode getcNode() {
        return cNode;
    }

    @Override
    public void notifyStateChanged(ObjectState oldState, ObjectState newState) {
        setBorder(newState.isSelected() ? BorderFactory.createLineBorder(8) : BorderFactory.createEmptyBorder(8));
    }
}
