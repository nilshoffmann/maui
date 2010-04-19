/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package maltcms.ui.fileHandles.properties.graph;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import maltcms.ui.fileHandles.properties.wizards.PipelinePropertiesWizardAction;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author alex
 */
public class NodeMenu implements PopupMenuProvider, ActionListener {

    private static final String DELETE_NODE_ACTION = "deleteNodeAction"; // NOI18N
    private static final String EDIT_PROPERTIES_ACTION = "editPropertiesAction";
    private JPopupMenu elementMenu;
    private Widget node;
    private Point point;
    private GraphScene.StringGraph scene;

    public NodeMenu(GraphScene.StringGraph scene) {
        this.scene = scene;
        elementMenu = new JPopupMenu("Pipeline element Menu");
        JMenuItem item;

        item = new JMenuItem("Edit Porperties");
        item.setActionCommand(EDIT_PROPERTIES_ACTION);
        item.addActionListener(this);
        elementMenu.add(item);
        item = new JMenuItem("Delete Node");
        item.setActionCommand(DELETE_NODE_ACTION);
        item.addActionListener(this);
        elementMenu.add(item);
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        this.point = point;
        this.node = widget;
        return elementMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(DELETE_NODE_ACTION)) {
            this.scene.removeNodeWithEdges((String) this.scene.findObject(this.node));
            this.scene.validate();
        } else if (e.getActionCommand().equals(EDIT_PROPERTIES_ACTION)) {
            //Fallunterscheidung zwischen verschiedenen nodes
            e.setSource(this.node);
            CallableSystemAction csa = PipelinePropertiesWizardAction.getInstance();
            csa.actionPerformed(e);
        }
    }
}
