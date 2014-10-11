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

import net.sf.maltcms.chromaui.ui.support.api.ui.OutlineViewDialogPanel;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.*;
import net.sf.maltcms.chromaui.ui.support.api.nodes.Nodes;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class Projects {
    
    private static class ProjectDialog<T extends Project> {

        private Collection<? extends T> selectedProjects = Collections.emptyList();
        private final Collection<? extends T> projects;
        private final boolean singleSelection;
        private final Class<? extends T> projectClazz;

        public ProjectDialog(Collection<? extends T> projects, boolean singleSelection, Class<? extends T> descriptorClazz) {
            this.projects = projects;
            this.singleSelection = singleSelection;
            this.projectClazz = descriptorClazz;
        }

        void showDialog() {
            Runnable r = null;
            if (singleSelection) {
                r = new Runnable() {

                    @Override
                    public void run() {
                        OutlineViewDialogPanel dp = new OutlineViewDialogPanel();
                        dp.init("Select a Project: ", false);
                        dp.getExplorerManager().setRootContext(new AbstractNode(Children.create(new ChildFactory<T>() {

                            @Override
                            protected boolean createKeys(List<T> list) {
                                list.addAll(projects);
                                return true;
                            }

                            @Override
                            protected Node createNodeForKey(final T key) {
                                return getNodeForProject(key);
                            }
                        }, true)));
                        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dp, "Select Project", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
                        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                            Set<T> toolDescriptors = new LinkedHashSet<>();
                            Node[] selectedNodes = dp.getExplorerManager().getSelectedNodes();
                            for (Node n : selectedNodes) {
                                Logger.getLogger(Projects.class.getName()).log(Level.INFO, "Adding single selection to descriptor list!");
                                toolDescriptors.add(n.getLookup().lookup(projectClazz));
                            }
                            selectedProjects = toolDescriptors;
                        }
                    }
                };
            } else {
                r = new Runnable() {

                    @Override
                    public void run() {
                        OutlineViewDialogPanel dp = new OutlineViewDialogPanel();
                        dp.init("Select Projects: ", false);
                        dp.getExplorerManager().setRootContext(new AbstractNode(Children.create(new ChildFactory<T>() {

                            @Override
                            protected boolean createKeys(List<T> list) {
                                list.addAll(projects);
                                return true;
                            }

                            @Override
                            protected Node createNodeForKey(final T key) {
                                return Nodes.checkable(getNodeForProject(key));
                            }
                        }, true)));
                        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dp, "Select Projects", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
                        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                            Set<T> toolDescriptors = new LinkedHashSet<>();
                            Node[] selectedNodes = dp.getExplorerManager().getRootContext().getChildren().getNodes(true);
                            for (Node n : selectedNodes) {
                                CheckableNode cn = n.getLookup().lookup(CheckableNode.class);
                                Logger.getLogger(Projects.class.getName()).log(Level.INFO, "Found checkable node in lookup!");
                                if (cn != null && cn.isSelected()) {
                                    Logger.getLogger(Projects.class.getName()).log(Level.INFO, "Adding checked node's descriptor to descriptor list!");
                                    toolDescriptors.addAll(n.getLookup().lookupAll(projectClazz));
                                }
                            }
                            selectedProjects = toolDescriptors;
                        }
                    }
                };
            }
            try {
                SwingUtilities.invokeAndWait(r);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        Collection<? extends T> getSelectedDescriptors() {
            return selectedProjects;
        }

    }

    /**
     *
     * @param nodes
     * @return
     */
    public static Node[] getCheckableNodesFor(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = Nodes.checkable(nodes[i]);
        }
        return nodes;
    }
    
    public static Node getNodeForProject(Project project) {
        return new FilterNode(project.getLookup().lookup(LogicalViewProvider.class).createLogicalView(), Children.LEAF);
    }
    
    /**
     *
     * @param projects
     * @return
     */
    public static Node[] getNodesFor(Collection<Project> projects) {
        Node[] projectNodes = new Node[projects.size()];
        int i = 0;
        for (Project p : projects) {
            projectNodes[i++] = getNodeForProject(p);
        }
        return projectNodes;
    }

    /**
     *
     * @param <T>
     * @param projectClass
     * @param title
     * @param comboBoxTitle
     * @return
     */
    public static <T extends Project> Collection<? extends T> getSelectedOpenProject(Class<? extends T> projectClass, String title, String comboBoxTitle) {
        ArrayList<T> openProjects = new ArrayList<>();
        for(Project p:OpenProjects.getDefault().getOpenProjects()) {
            openProjects.add((T)p);
        }
        ProjectDialog<T> dialog = new ProjectDialog<>(openProjects, true, projectClass);
        dialog.showDialog();
        return dialog.getSelectedDescriptors();
//        ChoiceViewDialogPanel panel = new ChoiceViewDialogPanel();
//        panel.init(comboBoxTitle, true);
//        Children.Array ca = new Children.Array();
//        ca.add(getNodesFor(openProjects));
//        panel.getExplorerManager().setRootContext(new AbstractNode(ca));
//        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(panel, title, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
//        // let's display the dialog now...
//        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
//            Node[] selectedNodes = panel.getExplorerManager().getSelectedNodes();
//            List<T> selectedProjects = new LinkedList<>();
//            for (Node n : selectedNodes) {
//                selectedProjects.addAll(n.getLookup().lookupAll(projectClass));
//            }
//            return Collections.singletonList(selectedProjects.get(0));
//        }
//        return Collections.emptyList();
    }

    /**
     *
     * @param <T>
     * @param projectClass
     * @param title
     * @param comboBoxTitle
     * @return
     */
    public static <T extends Project> Collection<? extends T> getSelectedOpenProjects(Class<T> projectClass, String title, String comboBoxTitle) {
        ArrayList<T> openProjects = new ArrayList<>();
        for(Project p:OpenProjects.getDefault().getOpenProjects()) {
            openProjects.add((T)p);
        }
        ProjectDialog<T> dialog = new ProjectDialog<>(openProjects, false, projectClass);
        dialog.showDialog();
        return dialog.getSelectedDescriptors();
//        Collection<Project> openProjects = new ArrayList<>(Arrays.asList(OpenProjects.getDefault().getOpenProjects()));
//        OutlineViewDialogPanel panel = new OutlineViewDialogPanel();
//        panel.init(comboBoxTitle, false);
//        Children.Array ca = new Children.Array();
//        ca.add(getCheckableNodesFor(getNodesFor(openProjects)));
//        panel.getExplorerManager().setRootContext(new AbstractNode(ca));
//        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(panel, title, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
//        // let's display the dialog now...
//        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
//            Node[] selectedNodes = panel.getExplorerManager().getSelectedNodes();
//            List<T> selectedProjects = new LinkedList<>();
//            for (Node n : selectedNodes) {
//                selectedProjects.addAll(n.getLookup().lookupAll(projectClass));
//            }
//            return selectedProjects;
//        }
//        return Collections.emptyList();
    }
}
