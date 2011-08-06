/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.graph;

import de.bielefeld.maltcms.PipelineEditorTopComponent;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author mw
 */
public class PipelineGraphScene extends ObjectScene {

    private PipelineEditorTopComponent petc;
    private Widget selected;
    private LayerWidget layerWidget = new LayerWidget(this);
    private LayerWidget connectionWidget = new LayerWidget(this);
    private LayerWidget interactionWidget = new LayerWidget(this);

    public PipelineGraphScene(PipelineEditorTopComponent petc) {
        this.petc = petc;

        getActions().addAction(ActionFactory.createZoomAction());

        addChild(layerWidget);
        addChild(connectionWidget);
        addChild(interactionWidget);

        getActions().addAction(ActionFactory.createAcceptAction(new PipelineAcceptProvider(this)));
    }

    public LayerWidget getConnectionWidget() {
        return connectionWidget;
    }

    public LayerWidget getInteractionWidget() {
        return interactionWidget;
    }

    public LayerWidget getLayerWidget() {
        return layerWidget;
    }

    public void setSelectedWidget(Widget w) {
        this.selected = w;
        this.petc.setSelected(this.selected);
    }

}
