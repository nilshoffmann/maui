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

import java.util.ArrayList;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.visual.model.ObjectScene;

/**
 *
 * @author alex
 */
public class EdgeMenu implements PopupMenuProvider, ActionListener {

    private static final String DELETE_TRANSITION = "deleteTransition"; // NOI18N
    private PipelineGraphScene scene;
    private JPopupMenu menu;
    private ConnectionWidget edge;
    private Point point;

    public EdgeMenu(PipelineGraphScene scene) {
        this.scene = scene;
        menu = new JPopupMenu("Transition Menu");
        JMenuItem item;

        item = new JMenuItem("Delete Transition");
        item.setActionCommand(DELETE_TRANSITION);
        item.addActionListener(this);
        menu.add(item);

    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        if (widget instanceof ConnectionWidget) {
            this.edge = (ConnectionWidget) widget;
            this.point = point;
            return menu;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(DELETE_TRANSITION)) {
//            scene.removeEdge ((String) scene.findObject (edge));
            this.scene.getConnectionWidget().removeChild(this.edge);
            this.scene.validate();
        }
    }

    private boolean removeControlPoint(Point point, ArrayList<Point> list, double deleteSensitivity) {
        for (Point elem : list) {
            if (elem.distance(point) < deleteSensitivity) {
                list.remove(elem);
                return true;
            }
        }
        return false;
    }
}
