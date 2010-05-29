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

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.awt.StatusDisplayer;
import org.openide.util.ImageUtilities;

/**
 * @author Alex
 */
public class PipelineGraphScene extends GraphScene.StringGraph {

    public static final Image IMAGE = ImageUtilities.loadImage("maltcms/ui/fileHandles/properties/resources/node.png"); // NOI18N
    public static final String INPUT_WIDGET = "Pipeline Input";
    public static final String GENERAL_WIDGET = "General Configuration";
    private LayerWidget mainLayer = new LayerWidget(this);
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer = new LayerWidget(this);
    private LayerWidget backgroundLayer = new LayerWidget(this);
//    private WidgetAction moveAction = ActionFactory.createMoveAction();
    private WidgetAction moveAction = ActionFactory.createAlignWithMoveAction(mainLayer, interractionLayer, null);
    private Router router = RouterFactory.createFreeRouter();
//    private WidgetAction connectAction = ActionFactory.createExtendedConnectAction(interractionLayer, new SceneConnectProvider(this));
    private WidgetAction connectAction = ActionFactory.createConnectAction(interractionLayer, new SceneConnectProvider(this));
    private WidgetAction reconnectAction = ActionFactory.createReconnectAction(new SceneReconnectProvider(this));
    private WidgetAction moveControlPointAction = ActionFactory.createFreeMoveControlPointAction();
    private WidgetAction selectAction = ActionFactory.createSelectAction(new ObjectSelectProvider());
    private NodeMenu nodeMenu = new NodeMenu(this);
    private EdgeMenu edgeMenu = new EdgeMenu(this);
    private SceneLayout layout;
    //private PipelineInputWidget root;
    private PipelineGeneralConfigWidget general;
    private boolean shortLabel = false;

    public PipelineGraphScene() {
//        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        //root = (PipelineInputWidget) addNode(INPUT_WIDGET);

        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
        addChild(interractionLayer);
        getActions().addAction(ActionFactory.createRectangularSelectAction(this, backgroundLayer));
        getActions().addAction(ActionFactory.createPopupMenuAction(new SceneMainMenu(this)));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createEditAction(new EditProvider() {

            @Override
            public void edit(Widget widget) {
                layout.invokeLayout();
            }
        }));

//        setToolTipText("Left mouse click for creating a new Node");
        initGrids();

        setActiveTool(SceneMainMenu.MOVE_MODE);
        StatusDisplayer.getDefault().setStatusText("Selection Mode");
    }

    @Override
    protected Widget attachNodeWidget(String node) {
        IconNodeWidget label;
        if (node.equals(INPUT_WIDGET)) {
            label = new PipelineInputWidget(this);
        } else if (node.equals(GENERAL_WIDGET)) {
            label = new PipelineGeneralConfigWidget(this);
        } else {
            label = new PipelineElementWidget(this);
        }
        if (this.shortLabel) {
            String[] tmp = node.split("\\.");
            label.setLabel(tmp[tmp.length - 1]);
        } else {
            label.setLabel(node);
        }
        label.setToolTipText("Hold 'Ctrl'+'Mouse Right Button' to create Edge");
        label.setImage(IMAGE);
        label.createActions(SceneMainMenu.CONNECTION_MODE).addAction(connectAction);
        label.createActions(SceneMainMenu.MOVE_MODE).addAction(moveAction);
        mainLayer.addChild(label);
        label.getActions().addAction(ActionFactory.createPopupMenuAction(nodeMenu));
        return label;
    }

    @Override
    protected Widget attachEdgeWidget(String edge) {
        ConnectionWidget connection = new ConnectionWidget(this);
        connection.setRouter(router);
        connection.setToolTipText("Double-click for Add/Remove Control Point");
        connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        connection.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        connection.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        connectionLayer.addChild(connection);
        connection.getActions().addAction(reconnectAction);
        connection.getActions().addAction(createSelectAction());
        connection.getActions().addAction(ActionFactory.createAddRemoveControlPointAction());
        connection.getActions().addAction(moveControlPointAction);
        connection.getActions().addAction(ActionFactory.createPopupMenuAction(edgeMenu));
        return connection;
    }

    @Override
    protected void attachEdgeSourceAnchor(String edge, String oldSourceNode, String sourceNode) {
        ConnectionWidget widget = (ConnectionWidget) findWidget(edge);
        Widget sourceNodeWidget = findWidget(sourceNode);
        widget.setSourceAnchor(sourceNodeWidget != null ? AnchorFactory.createFreeRectangularAnchor(sourceNodeWidget, true) : null);
    }

    @Override
    protected void attachEdgeTargetAnchor(String edge, String oldTargetNode, String targetNode) {
        ConnectionWidget widget = (ConnectionWidget) findWidget(edge);
        Widget targetNodeWidget = findWidget(targetNode);
        widget.setTargetAnchor(targetNodeWidget != null ? AnchorFactory.createFreeRectangularAnchor(targetNodeWidget, true) : null);
    }

    private class ObjectSelectProvider implements SelectProvider {

        @Override
        public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return false;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        @Override
        public void select(Widget widget, Point localLocation, boolean invertSelection) {
            Object object = findObject(widget);

            if (object != null) {
                if (getSelectedObjects().contains(object)) {
                    return;
                }
                userSelectionSuggested(Collections.singleton(object), invertSelection);
            } else {
                userSelectionSuggested(Collections.emptySet(), invertSelection);
            }
        }
    }

    public void initGrids() {
        Image sourceImage = ImageUtilities.loadImage("maltcms/ui/fileHandles/properties/resources/paper_grid17.png"); // NOI18N
        int width = sourceImage.getWidth(null);
        int height = sourceImage.getHeight(null);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(sourceImage, 0, 0, null);
        graphics.dispose();
        TexturePaint PAINT_BACKGROUND = new TexturePaint(image, new Rectangle(0, 0, width, height));
        setBackground(PAINT_BACKGROUND);
        repaint();
        revalidate(false);
        validate();
    }

    public void setLayout(SceneLayout layout) {
        this.layout = layout;
    }

    public boolean hasGeneralNode() {
        return this.general != null;
    }

    public Widget getGeneral() {
        if (this.general == null) {
            return createGeneralWidget();
        }
        return this.general;
    }

    public Widget createGeneralWidget() {
        Widget w = addNode(GENERAL_WIDGET);
        this.validate();
        this.getSceneAnimator().animatePreferredLocation(w, new Point(10, 300));
        this.validate();
        return w;
    }

    public void setShortLabelActive(boolean activateShortLabel) {
        if (activateShortLabel) {
            this.shortLabel = true;
            for (Widget w : this.mainLayer.getChildren()) {
                if (w instanceof PipelineElementWidget) {
                    PipelineElementWidget wt = (PipelineElementWidget) w;
                    String[] tmp = wt.getClassName().split("\\.");
                    wt.getLabelWidget().setLabel(tmp[tmp.length - 1]);
                }
            }
        } else {
            this.shortLabel = false;
            for (Widget w : this.mainLayer.getChildren()) {
                if (w instanceof PipelineElementWidget) {
                    PipelineElementWidget wt = (PipelineElementWidget) w;
                    wt.getLabelWidget().setLabel(wt.getClassName());
                }
            }
        }
    }

    public LayerWidget getMainLAyer() {
        return this.mainLayer;
    }
}
