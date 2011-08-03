/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Image;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author nilshoffmann
 */
public class ChromAUIProjectNode extends AbstractNode {

    public ChromAUIProjectNode(IChromAUIProject project) {
        super(Children.create(new ChromaUIProjectNodesFactory(project), true),
                Lookups.singleton(project));
        //this.displayName = this.project.getProjectDirectory().getName();
        setName(project.getProjectDirectory().getName());
        setShortDescription(project.getLocation().getPath());
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Set<Action> nodeActions = new LinkedHashSet<Action>(Utilities.
                actionsForPath("Actions/ChromAUIProjectLogicalView"));
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
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/MauiProject.png");
    }

    @Override
    public Image getOpenedIcon(
            int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/MauiProjectOpen.png");
    }
//    @Override
//    public String getDisplayName() {
//        return this.displayName;
//    }
}
