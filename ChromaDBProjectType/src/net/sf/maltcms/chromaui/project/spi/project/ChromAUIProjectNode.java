/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import java.awt.Image;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.Action;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromAUIProjectNode extends AbstractNode {

    private final ChromAUIProject project;
    
    public ChromAUIProjectNode(ChromAUIProject project) {
        super(Children.create(new ChromaUIProjectNodesFactory(project), true), Lookups.singleton(project));
        this.project = project;
        //this.displayName = this.project.getProjectDirectory().getName();
        setName(this.project.getProjectDirectory().getName());
        setShortDescription(this.project.getLocation().getPath());
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Set<Action> nodeActions = new LinkedHashSet<Action>(Utilities.actionsForPath("Actions/ChromAUIProjectLogicalView"));
//                CommonProjectActions.newFileAction(),
//                null,
//                CommonProjectActions.copyProjectAction(),
//                CommonProjectActions.deleteProjectAction(),
//                null,
//                CommonProjectActions.setAsMainProjectAction(),
        nodeActions.add(CommonProjectActions.closeProjectAction());
//                null,
        nodeActions.add(SystemAction.get(PropertiesAction.class));

        return nodeActions.toArray(new Action[nodeActions.size()]);
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("maltcms/ui/nb/resources/chromaProject.png");
    }

    @Override
    public Image getOpenedIcon(
            int type) {
        return getIcon(type);
    }

//    @Override
//    public String getDisplayName() {
//        return this.displayName;
//    }
}
