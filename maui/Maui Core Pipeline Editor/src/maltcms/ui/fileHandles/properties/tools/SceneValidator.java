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
package maltcms.ui.fileHandles.properties.tools;

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
        if (pipeline.isEmpty()) {
            return "A pipeline must have at least one element.";
        }
        if (pipeline.size() + other.size() != nodeCount) {
            return "This tool does not support multiple pipelines.";
        }

        return checkPipeline(pipeline);
    }

    private String checkPipeline(List<PipelineElementWidget> pipeline) {
//        final Maltcms m = Maltcms.getInstance();
//        final PropertiesConfiguration cfg = m.getDefaultConfiguration();
//        Factory.getInstance().

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
        if (cs.validate()) {

//        Factory.getInstance().configure(cfg);
//        ICommandSequence cs = Factory.getInstance().getObjectFactory().instantiate(CommandPipeline.class);
//        cs.setInput(null);
            return "Everything looks fine";
        } else {
            return "Validation failed!";
        }
    }
}
