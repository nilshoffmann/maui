/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.Action;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author nilshoffmann
 */
public class ChromAUIProjectNode extends AbstractNode implements PropertyChangeListener, LookupListener {

    private Result<IMetabolite> metaboliteSelection;
    
    public ChromAUIProjectNode(IChromAUIProject project) {
        super(Children.create(new ChromaUIProjectNodesFactory(project), true),
                Lookups.singleton(project));
        //this.displayName = this.project.getProjectDirectory().getName();
        setName(project.getProjectDirectory().getName());
        setShortDescription(project.getLocation().getPath());
        WeakListeners.propertyChange(this, project);
        metaboliteSelection = Utilities.actionsGlobalContext().lookupResult(IMetabolite.class);
//        project.getLookup().lookup(ProjectInformation.class).addPropertyChangeListener(this);
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Set<Action> nodeActions = new LinkedHashSet<Action>(Utilities.
                actionsForPath("Actions/ChromAUIProjectLogicalView"));
//                CommonProjectActions.newFileAction(),
//                null,
//                CommonProjectActions.copyProjectAction(),
        nodeActions.add(CommonProjectActions.deleteProjectAction());
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

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if(pce.getPropertyName().equals(PROP_NAME)) {
            fireNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_DISPLAY_NAME)) {
            fireDisplayNameChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        if(pce.getPropertyName().equals(PROP_SHORT_DESCRIPTION)) {
            fireShortDescriptionChange((String)pce.getOldValue(),(String)pce.getNewValue());
        }
        firePropertyChange(pce.getPropertyName(), pce.getOldValue(), pce.getNewValue());
    }

    @Override
    public void resultChanged(LookupEvent le) {
        System.out.println("Received IMetabolite in global selection: "+metaboliteSelection.allInstances().toString());
    }
}
