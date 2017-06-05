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
package maltcms.ui.fileHandles.properties.graph;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import maltcms.ui.fileHandles.properties.graph.widget.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.wizards.PipelinePropertiesWizardAction;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.BeanNode;
import org.openide.nodes.NodeOperation;
import org.openide.util.Exceptions;
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

        item = new JMenuItem("Edit Properties");
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
        switch (e.getActionCommand()) {
            case DELETE_NODE_ACTION:
                this.scene.removeNodeWithEdges((String) this.scene.findObject(this.node));
                this.scene.validate();
                break;
            case EDIT_PROPERTIES_ACTION:
                //Fallunterscheidung zwischen verschiedenen nodes
                e.setSource(this.node);
                if (e.getSource() instanceof PipelineElementWidget) {
                    PipelineElementWidget pipelineElement = (PipelineElementWidget) e.getSource();
                    try {
                        BeanNode<Object> beanNode = new BeanNode<>(pipelineElement.getBean());
                        NodeOperation.getDefault().showProperties(beanNode);
                    } catch (IntrospectionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                    CallableSystemAction csa = PipelinePropertiesWizardAction.getInstance();
                    csa.actionPerformed(e);
                }
                break;
        }
    }
}
