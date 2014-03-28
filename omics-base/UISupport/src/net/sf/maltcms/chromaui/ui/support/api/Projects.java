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
package net.sf.maltcms.chromaui.ui.support.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author Nils Hoffmann
 */
public class Projects {

    public static Node[] getNodesFor(Collection<Project> projects) {
        Node[] projectNodes = new Node[projects.size()];
        int i = 0;
        for (Project p : projects) {
            projectNodes[i++] = new FilterNode(p.getLookup().lookup(LogicalViewProvider.class).createLogicalView(), Children.LEAF);
        }
        return projectNodes;
    }

    public static <T extends Project> Collection<T> getSelectedOpenProject(Class<T> projectClass, String title, String comboBoxTitle) {
        Collection<Project> openProjects = new ArrayList<>(Arrays.asList(OpenProjects.getDefault().getOpenProjects()));
        DialogPanel panel = new DialogPanel();
        panel.init(comboBoxTitle, true);
        Children.Array ca = new Children.Array();
        ca.add(getNodesFor(openProjects));
        panel.getExplorerManager().setRootContext(new AbstractNode(ca));
        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(panel, title, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            Node[] selectedNodes = panel.getExplorerManager().getSelectedNodes();
            List<T> selectedProjects = new LinkedList<>();
            for (Node n : selectedNodes) {
                selectedProjects.addAll(n.getLookup().lookupAll(projectClass));
            }
            return Collections.singletonList(selectedProjects.get(0));
        }
        return Collections.emptyList();
    }

    public static <T extends Project> Collection<T> getSelectedOpenProjects(Class<T> projectClass, String title, String comboBoxTitle) {
        Collection<Project> openProjects = new ArrayList<>(Arrays.asList(OpenProjects.getDefault().getOpenProjects()));
        DialogPanel panel = new DialogPanel();
        panel.init(comboBoxTitle, false);
        Children.Array ca = new Children.Array();
        ca.add(getNodesFor(openProjects));
        panel.getExplorerManager().setRootContext(new AbstractNode(ca));
        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(panel, title, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            Node[] selectedNodes = panel.getExplorerManager().getSelectedNodes();
            List<T> selectedProjects = new LinkedList<>();
            for (Node n : selectedNodes) {
                selectedProjects.addAll(n.getLookup().lookupAll(projectClass));
            }
            return selectedProjects;
        }
        return Collections.emptyList();
    }
}
