/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.graph;

import de.bielefeld.maltcms.nodes.Command;
import de.bielefeld.maltcms.nodes.CommandNode;
import de.bielefeld.maltcms.widget.CommandWidget;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.NodeTransfer;

/**
 *
 * @author mw
 */
public class PipelineAcceptProvider implements AcceptProvider {

    private PipelineGraphScene scene;
    private PipelineSelectProvider selectProvider;
    private NodeMenu nodeMenu;

    public PipelineAcceptProvider(PipelineGraphScene s) {
        this.scene = s;
        this.selectProvider = new PipelineSelectProvider(this.scene);
        this.nodeMenu = new NodeMenu(this.scene);
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
        return ConnectorState.ACCEPT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable transferable) {
        final CommandNode cn = ((CommandNode) NodeTransfer.node(transferable, NodeTransfer.DND_COPY)).clone();
        final Command g = cn.getLookup().lookup(Command.class);
        if (g != null) {
            final LabelWidget commandWidget = new CommandWidget(scene, g.getName(), cn);
            commandWidget.setPreferredLocation(point);

            commandWidget.getActions().addAction(ActionFactory.createExtendedConnectAction(
                    this.scene.getConnectionWidget(), new PipelineConnectProvider(this.scene)));

            commandWidget.getActions().addAction(
                    ActionFactory.createAlignWithMoveAction(
                    this.scene.getLayerWidget(), this.scene.getInteractionWidget(),
                    ActionFactory.createDefaultAlignWithMoveDecorator()));

            commandWidget.getActions().addAction(ActionFactory.createSelectAction(this.selectProvider));
            commandWidget.getActions().addAction(ActionFactory.createPopupMenuAction(this.nodeMenu));

            this.scene.getLayerWidget().addChild(commandWidget);
        }
    }
}
