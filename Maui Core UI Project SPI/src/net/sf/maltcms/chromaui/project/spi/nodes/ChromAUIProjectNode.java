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
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.Action;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.*;
import org.openide.util.Lookup.Result;
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

	protected Action createMenuItem(String name, String path) {
		Collection<? extends Action> actions = Utilities.
                actionsForPath(path);
		ProjectNodePopupAction pnia = new ProjectNodePopupAction(name);
		pnia.setActions(actions.toArray(new Action[actions.size()]));
		return pnia;
	}
	
    @Override
    public Action[] getActions(boolean arg0) {
        Set<Action> nodeActions = new LinkedHashSet<Action>();
		nodeActions.add(createMenuItem("Pipelines","Actions/ChromAUIProjectLogicalView/Pipelines"));
		nodeActions.add(createMenuItem("Peaks","Actions/ChromAUIProjectLogicalView/Peaks"));
		nodeActions.add(createMenuItem("Database","Actions/ChromAUIProjectLogicalView/Database"));
		nodeActions.add(createMenuItem("Scripts","Actions/ChromAUIProjectLogicalView/Scripts"));
		nodeActions.add(null);
		nodeActions.addAll(Utilities.actionsForPath("Actions/ContainerNodeActions/DefaultActions"));
		
//		Collection<? extends Action> projectActions = Utilities.
//                actionsForPath("Actions/ChromAUIProjectLogicalView");
//		nodeActions.addAll(projectActions);
//                CommonProjectActions.newFileAction(),
//                null,
//                CommonProjectActions.copyProjectAction(),
        nodeActions.add(null);
//        nodeActions.add(CommonProjectActions.deleteProjectAction());
//                null,
//                CommonProjectActions.setAsMainProjectAction(),
        nodeActions.add(CommonProjectActions.closeProjectAction());
//                null,
//        nodeActions.add(CommonProjectActions.customizeProjectAction());

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
