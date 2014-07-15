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
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.api.nodes.IProjectMenuProvider;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromAUIProjectNode extends BeanNode<IChromAUIProject> implements PropertyChangeListener, LookupListener {

    public ChromAUIProjectNode(IChromAUIProject bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }

    public ChromAUIProjectNode(IChromAUIProject bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public ChromAUIProjectNode(IChromAUIProject bean) throws IntrospectionException {
        super(bean, Children.create(new ChromaUIProjectNodesFactory(bean), true),
                new ProxyLookup(bean.getLookup()));
        setName(bean.getProjectDirectory().getName());
        setShortDescription(bean.getLocation().getPath());
        bean.addPropertyChangeListener(WeakListeners.propertyChange(this, bean));
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Set<Action> nodeActions = new LinkedHashSet<Action>();
        INodeFactory f = Lookup.getDefault().lookup(INodeFactory.class);
        List<IProjectMenuProvider> providers = new ArrayList<IProjectMenuProvider>(Lookup.getDefault().lookupAll(IProjectMenuProvider.class));
        Collections.sort(providers, new Comparator<IProjectMenuProvider>() {

            @Override
            public int compare(IProjectMenuProvider o1, IProjectMenuProvider o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });
        for (IProjectMenuProvider ipmp : providers) {
            Collection<? extends Action> actions = Utilities.
                    actionsForPath(ipmp.getActionPath());
            if (!actions.isEmpty()) {
                Action projectMenuAction = f.createMenuItem(ipmp.getName(), actions.toArray(new Action[actions.size()]));
                nodeActions.add(projectMenuAction);
            }
        }
        nodeActions.add(null);
        nodeActions.addAll(Utilities.actionsForPath("Actions/ChromAUIProjectLogicalView/DefaultActions"));
//		nodeActions.add(f.createMenuItem("Default","Actions/ChromAUIProjectLogicalView/DefaultActions"));
        nodeActions.add(null);
        nodeActions.add(CommonProjectActions.newFileAction());
        nodeActions.add(CommonProjectActions.copyProjectAction());
        nodeActions.add(CommonProjectActions.deleteProjectAction());
        nodeActions.add(null);
        nodeActions.add(CommonProjectActions.closeProjectAction());
//                null,
        nodeActions.add(CommonProjectActions.customizeProjectAction());

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
    public String getDisplayName() {
        return ProjectUtils.getInformation(getLookup().lookup(IChromAUIProject.class)).getDisplayName();
    }

    @Override
    public String getHtmlDisplayName() {
        return ProjectUtils.getInformation(getLookup().lookup(IChromAUIProject.class)).getDisplayName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(PROP_NAME)) {
            fireNameChange((String) pce.getOldValue(), (String) pce.getNewValue());
        }
        if (pce.getPropertyName().equals(PROP_DISPLAY_NAME)) {
            fireDisplayNameChange((String) pce.getOldValue(), (String) pce.getNewValue());
        }
        if (pce.getPropertyName().equals(PROP_SHORT_DESCRIPTION)) {
            fireShortDescriptionChange((String) pce.getOldValue(), (String) pce.getNewValue());
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
//        System.out.println("Received IMetabolite in global selection: " + metaboliteSelection.allInstances().toString());
    }
}
