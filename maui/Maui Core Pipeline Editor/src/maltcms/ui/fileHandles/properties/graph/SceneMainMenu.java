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

import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import maltcms.ui.fileHandles.properties.tools.SceneExporter;
import maltcms.ui.fileHandles.properties.wizards.PipelinePropertiesWizardAction;
import org.openide.awt.StatusDisplayer;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author alex
 */
public class SceneMainMenu implements PopupMenuProvider, ActionListener {

    private static final String ADD_NEW_NODE_ACTION = "addNewNodeAction"; // NOI18N
    private static final String ADD_NEW_INPUT_ACTION = "addNewInputAction"; // NOI18N
    private static final String ADD_NEW_GENERAL_ACTION = "addNewGeneralAction"; // NOI18N
    private static final String EXPORT_SCENE = "exportScene";
    public static final String CONNECTION_MODE = "connect";
    public static final String MOVE_MODE = "select";
    private PipelineGraphScene scene;
    private JPopupMenu menu;
    private Point point;
    private int nodeCount = 0;

    public SceneMainMenu(PipelineGraphScene scene) {
        this.scene = scene;
        menu = new JPopupMenu("Scene Menu");
        JMenuItem item;

        item = new JMenuItem("Add New Pipeline Node");
        item.setActionCommand(ADD_NEW_NODE_ACTION);
        item.addActionListener(this);
        menu.add(item);

//        item = new JMenuItem("Add Pipeline Input Node");
//        item.setActionCommand(ADD_NEW_INPUT_ACTION);
//        item.addActionListener(this);
//        menu.add(item);

//        item = new JMenuItem("Add General Pipeline Config Node");
//        item.setActionCommand(ADD_NEW_GENERAL_ACTION);
//        item.addActionListener(this);
//        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Export Scene");
        item.setActionCommand(EXPORT_SCENE);
        item.addActionListener(this);
        menu.add(item);

//        menu.addSeparator();
//
//        item = new JMenuItem("Connection Mode");
//        item.setActionCommand(CONNECTION_MODE);
//        item.addActionListener(this);
//        menu.add(item);
//
//        item = new JMenuItem("Move Mode");
//        item.setActionCommand(MOVE_MODE);
//        item.addActionListener(this);
//        menu.add(item);
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        this.point = point;
        return menu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ADD_NEW_NODE_ACTION.equals(e.getActionCommand())) {
            PipelineElementWidget node = (PipelineElementWidget) this.scene.addNode("" + nodeCount++);
            this.scene.validate();
            e.setSource(node);

            CallableSystemAction csa = PipelinePropertiesWizardAction.getInstance();
            csa.actionPerformed(e);

            if (!node.getLastAction().equals("cancelled")) {
                this.scene.getSceneAnimator().animatePreferredLocation(node, point);
            } else {
                this.scene.removeNodeWithEdges((String) this.scene.findObject(node));
                this.scene.validate();
            }
        } else if (ADD_NEW_INPUT_ACTION.equals(e.getActionCommand())) {
//            if (!this.scene.hasRootNode()) {
//            } else {
//                System.out.println("Already has root node");
//            }
        } else if (ADD_NEW_GENERAL_ACTION.equals(e.getActionCommand())) {
            if (!this.scene.hasGeneralNode()) {
                PipelineGeneralConfigWidget node = (PipelineGeneralConfigWidget) this.scene.addNode(PipelineGraphScene.GENERAL_WIDGET);
                this.scene.validate();
                e.setSource(node);

                CallableSystemAction csa = PipelinePropertiesWizardAction.getInstance();
                csa.actionPerformed(e);

                if (!node.getLastAction().equals("cancelled")) {
                    this.scene.getSceneAnimator().animatePreferredLocation(node, point);
                } else {
                    this.scene.removeNodeWithEdges((String) this.scene.findObject(node));
                    this.scene.validate();
                }
            } else {
                System.out.println("Scene already has general node");
            }
        } else if (EXPORT_SCENE.equals(e.getActionCommand())) {
            SceneExporter.showSaveDialog(this.scene);
        } else if (CONNECTION_MODE.equals(e.getActionCommand())) {
            this.scene.setActiveTool(CONNECTION_MODE);
            StatusDisplayer.getDefault().setStatusText("Connection Mode");
        } else if (MOVE_MODE.equals(e.getActionCommand())) {
            this.scene.setActiveTool(MOVE_MODE);
            StatusDisplayer.getDefault().setStatusText("Move Mode");
        }
    }
}
