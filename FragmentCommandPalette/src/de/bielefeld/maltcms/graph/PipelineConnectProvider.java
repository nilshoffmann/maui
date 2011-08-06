/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.graph;

import java.awt.Point;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author mw
 */
public class PipelineConnectProvider implements ConnectProvider {

    private PipelineGraphScene scene;
    private EdgeMenu edgeMenu;

    public PipelineConnectProvider(PipelineGraphScene s) {
        this.scene = s;
        this.edgeMenu = new EdgeMenu(this.scene);
    }

    @Override
    public boolean isSourceWidget(Widget source) {
        return source instanceof LabelWidget && source != null ? true : false;
    }

    @Override
    public ConnectorState isTargetWidget(Widget source, Widget target) {
        return source != target && target instanceof LabelWidget ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene scene, Point sceneLocation) {
        return null;
    }

    @Override
    public void createConnection(Widget source, Widget target) {
        ConnectionWidget conn = new ConnectionWidget(scene);
        conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
        conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
        conn.getActions().addAction(ActionFactory.createPopupMenuAction(this.edgeMenu));
        this.scene.getConnectionWidget().addChild(conn);
    }
}
