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
package de.bielefeld.maltcms.graph;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

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
    private PipelineGraphScene scene;

    public NodeMenu(PipelineGraphScene scene) {
        this.scene = scene;
        elementMenu = new JPopupMenu("Pipeline element Menu");
        JMenuItem item;

        item = new JMenuItem("Delete Command");
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
//            this.scene.removeNodeWithEdges((String) this.scene.findObject(this.node));
            this.scene.getLayerWidget().removeChild(this.node);
            this.scene.validate();
        }
    }
}
