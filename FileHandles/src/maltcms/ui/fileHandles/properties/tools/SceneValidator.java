/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import net.sf.maltcms.apps.Maltcms;
import cross.Factory;
import cross.commands.fragments.IFragmentCommand;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.pipeline.CommandPipeline;
import cross.datastructures.pipeline.ICommandSequence;
import cross.datastructures.tuple.TupleND;
import cross.exception.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import org.apache.commons.configuration.CompositeConfiguration;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author mw
 */
public class SceneValidator {

    private PipelineGraphScene scene;

    public SceneValidator(PipelineGraphScene scene) {
        this.scene = scene;
    }

    public String validate() {
        List<PipelineElementWidget> pipeline = SceneParser.getPipeline(this.scene);
        List<Widget> other = SceneParser.getNonPipeline(this.scene);
        int nodeCount = SceneParser.getPipelinesLayer(this.scene).getChildren().size();
        if (other.size() > 1) {
            return "At least one element ist not connected to the pipeline.";
        }
        if (pipeline.size() == 0) {
            return "A pipeline must have at least one element.";
        }
        if (pipeline.size() + other.size() != nodeCount) {
            return "This tool does not support multiple pipelines.";
        }

        return checkPipeline(pipeline);
    }

    private String checkPipeline(List<PipelineElementWidget> pipeline) {
        final Maltcms m = Maltcms.getInstance();
        final CompositeConfiguration cfg = m.getDefaultConfig();
        Factory.getInstance().configure(cfg);

        ICommandSequence cs = new CommandPipeline();
        List<IFragmentCommand> p = new ArrayList<IFragmentCommand>();
        for (PipelineElementWidget w : pipeline) {
            try {
                p.add((IFragmentCommand) Factory.getInstance().getObjectFactory().instantiate(Class.forName(w.getClassName())));
            } catch (ClassNotFoundException ex) {
//                Exceptions.printStackTrace(ex);
                return ex.getMessage();
            }
        }
        cs.setCommands(p);
//        System.out.println("COMMMANDSEQUENCE SIZE IS: " + cs.getCommands().size());
        try {
            cs.setInput(new TupleND<IFileFragment>());
        } catch (ConstraintViolationException ex) {
            if (!ex.getMessage().contains("smaller than minimum supplied")) {
//                Exceptions.printStackTrace(ex);
                return ex.getMessage();
            }
        }

//        Factory.getInstance().configure(cfg);
//        ICommandSequence cs = Factory.getInstance().getObjectFactory().instantiate(CommandPipeline.class);
//        cs.setInput(null);
        return "Everything looks fine";
    }
}
