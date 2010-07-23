/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.project;

import maltcms.ui.nb.ChromaProject;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.LookupProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author nilshoffmann
 */
public class LookupProviderImpl implements LookupProvider {

    @Override
    public Lookup createAdditionalLookup(Lookup lookup) {

        Project prj = lookup.lookup(Project.class);

        //If there is a web module provider in the
        //project's lookup, add a new lookup item to
        //the lookup, which we will look for to determine
        //whether a node should be created:
        ChromaProject cp = lookup.lookup(ChromaProject.class);
        if (cp != null) {
            System.out.println("Found a chromaproject instance for "+prj.getProjectDirectory().getPath());
            return Lookups.fixed(new ProcessingPipelineResultLookupItem(prj));
        }

        System.out.println("Did not find a chromaproject instance for "+prj.getProjectDirectory().getPath());
        //If there is no ChromaProject module in the lookup,
        //we do not add a new item to our lookup,
        //so that later a node will not be created:
        return Lookups.fixed();

    }

}
