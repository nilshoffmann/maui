/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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

import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

import java.awt.*;

/**
 *
 * @author alex
 */
public class SceneReconnectProvider implements ReconnectProvider {

    private String edge;
    private String originalNode;
    private String replacementNode;
    private GraphScene.StringGraph scene;

    public SceneReconnectProvider(GraphScene.StringGraph scene) {
        this.scene = scene;
    }

    @Override
    public void reconnectingStarted(ConnectionWidget connectionWidget, boolean reconnectingSource) {
    }

    @Override
    public void reconnectingFinished(ConnectionWidget connectionWidget, boolean reconnectingSource) {
    }

    @Override
    public boolean isSourceReconnectable(ConnectionWidget connectionWidget) {
        Object object = scene.findObject(connectionWidget);
        edge = scene.isEdge(object) ? (String) object : null;
        originalNode = edge != null ? scene.getEdgeSource(edge).toString() : null;
        return originalNode != null;
    }

    @Override
    public boolean isTargetReconnectable(ConnectionWidget connectionWidget) {
        Object object = scene.findObject(connectionWidget);
        edge = scene.isEdge(object) ? (String) object : null;
        originalNode = edge != null ? scene.getEdgeTarget(edge).toString() : null;
        return originalNode != null;
    }

    @Override
    public ConnectorState isReplacementWidget(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
        Object object = scene.findObject(replacementWidget);
        replacementNode = scene.isNode(object) ? (String) object : null;
        if (replacementNode != null) {
            return ConnectorState.ACCEPT;
        }
        return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomReplacementWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveReplacementWidget(Scene scene, Point sceneLocation) {
        return null;
    }

    @Override
    public void reconnect(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
        if (replacementWidget == null) {
            scene.removeEdge(edge);
        } else if (reconnectingSource) {
            scene.setEdgeSource(edge, replacementNode);
        } else {
            scene.setEdgeTarget(edge, replacementNode);
        }
        scene.validate();
    }
}
