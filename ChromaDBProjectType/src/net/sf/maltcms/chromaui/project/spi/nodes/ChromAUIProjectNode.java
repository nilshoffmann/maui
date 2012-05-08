/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.Action;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.*;
import org.openide.util.Lookup.Result;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author nilshoffmann
 */
public class ChromAUIProjectNode extends BeanNode<IChromAUIProject> implements PropertyChangeListener, LookupListener {

    private Result<IMetabolite> metaboliteSelection;

    public ChromAUIProjectNode(IChromAUIProject bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }

    public ChromAUIProjectNode(IChromAUIProject bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public ChromAUIProjectNode(IChromAUIProject bean) throws IntrospectionException {
        super(bean,Children.create(new ChromaUIProjectNodesFactory(bean), true),
                Lookups.singleton(bean));
        setName(bean.getProjectDirectory().getName());
        setShortDescription(bean.getLocation().getPath());
        bean.addPropertyChangeListener(WeakListeners.propertyChange(this, bean));
        metaboliteSelection = Utilities.actionsGlobalContext().lookupResult(IMetabolite.class);
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
//        try{
//            firePropertyChange(pce.getPropertyName(), pce.getOldValue(), pce.getNewValue());
//        }catch(IllegalStateException ise) {
//            
//        }
        //getLookup().lookup(IChromAUIProject.class).refresh();
    }

    @Override
    public void resultChanged(LookupEvent le) {
        System.out.println("Received IMetabolite in global selection: "+metaboliteSelection.allInstances().toString());
    }
}
