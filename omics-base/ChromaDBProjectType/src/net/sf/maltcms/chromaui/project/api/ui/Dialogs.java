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
package net.sf.maltcms.chromaui.project.api.ui;

import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.ui.support.api.ui.ChoiceViewDialogPanel;
import net.sf.maltcms.chromaui.ui.support.api.ui.OutlineViewDialogPanel;
import net.sf.maltcms.chromaui.ui.support.api.nodes.Nodes;
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
public class Dialogs {

    private static class DescriptorDialog<T extends IBasicDescriptor> {

        private Collection<? extends T> selectedDescriptors = Collections.emptyList();
        private final Collection<? extends T> descriptors;
        private final boolean singleSelection;
        private final Lookup lookup;
        private final Class<? extends T> descriptorClazz;

        public DescriptorDialog(Collection<? extends T> descriptors, boolean singleSelection, Lookup lookup, Class<? extends T> descriptorClazz) {
            this.descriptors = descriptors;
            this.singleSelection = singleSelection;
            this.lookup = lookup;
            this.descriptorClazz = descriptorClazz;
        }

        void showDialog(final String title, final String label) {
            Runnable r = null;
            if(descriptors.isEmpty()) {
                Logger.getLogger(Dialogs.class.getName()).log(Level.WARNING,"Descriptor selection was empty!");
                return;
            }
            if (singleSelection) {
                r = new Runnable() {

                    @Override
                    public void run() {
                        ChoiceViewDialogPanel dp = new ChoiceViewDialogPanel();
                        dp.init(label, false);
                        dp.getExplorerManager().setRootContext(new AbstractNode(Children.create(new ChildFactory<T>() {

                            @Override
                            protected boolean createKeys(List list) {
                                list.addAll(descriptors);
                                return true;
                            }

                            @Override
                            protected Node createNodeForKey(final T key) {
                                INodeFactory nodeFactory = Lookup.getDefault().lookup(INodeFactory.class);
                                Node toolNode = nodeFactory.createDescriptorNode(key, Children.LEAF, lookup);
                                FilterNode fn = new FilterNode(toolNode) {
                                    @Override
                                    public String getDisplayName() {
                                        return key.getDisplayName() + " " + key.getDate();
                                    }
                                };
                                return fn;
                            }
                        }, true)));
                        try {
                            Node n = dp.getExplorerManager().getRootContext().getChildren().getNodeAt(0);
                            dp.getExplorerManager().setSelectedNodes(new Node[]{n});
                            dp.setSelectedItem(n);
                        } catch (PropertyVetoException ex) {
                            Exceptions.printStackTrace(ex);
                        }
//                        dp.setSelectedIndex(0);
                        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dp, title, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
                        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                            Set<T> toolDescriptors = new LinkedHashSet<>();
                            Node[] selectedNodes = dp.getExplorerManager().getSelectedNodes();
                            for (Node n : selectedNodes) {
                                Logger.getLogger(Dialogs.class.getName()).log(Level.INFO, "Adding single selection to descriptor list!");
                                toolDescriptors.add(n.getLookup().lookup(descriptorClazz));
                            }
                            selectedDescriptors = toolDescriptors;
                        }
                    }
                };
            } else {
                r = new Runnable() {

                    @Override
                    public void run() {
                        OutlineViewDialogPanel dp = new OutlineViewDialogPanel();
                        dp.init(label, false);
                        dp.getExplorerManager().setRootContext(new AbstractNode(Children.create(new ChildFactory<T>() {

                            @Override
                            protected boolean createKeys(List list) {
                                list.addAll(descriptors);
                                return true;
                            }

                            @Override
                            protected Node createNodeForKey(final T key) {
                                INodeFactory nodeFactory = Lookup.getDefault().lookup(INodeFactory.class);
                                Node toolNode = Nodes.checkable(nodeFactory.createDescriptorNode(key, Children.LEAF, lookup));
                                FilterNode fn = new FilterNode(toolNode) {
                                    @Override
                                    public String getDisplayName() {
                                        return key.getDisplayName() + " " + key.getDate();
                                    }
                                };
                                return fn;
                            }
                        }, true)));
                        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dp, title, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE);
                        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                            Set<T> toolDescriptors = new LinkedHashSet<>();
                            Node[] selectedNodes = dp.getExplorerManager().getRootContext().getChildren().getNodes(true);
                            for (Node n : selectedNodes) {
                                CheckableNode cn = n.getLookup().lookup(CheckableNode.class);
                                Logger.getLogger(Dialogs.class.getName()).log(Level.INFO, "Found checkable node in lookup!");
                                if (cn != null && cn.isSelected()) {
                                    Logger.getLogger(Dialogs.class.getName()).log(Level.INFO, "Adding checked node's descriptor to descriptor list!");
                                    toolDescriptors.addAll(n.getLookup().lookupAll(descriptorClazz));
                                }
                            }
                            selectedDescriptors = toolDescriptors;
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
            return selectedDescriptors;
        }

    }

    public static <T extends IBasicDescriptor> Collection<? extends T> showAndSelectDescriptors(final Collection<T> itd, final Lookup lookup, Class<? extends T> descriptorClazz, String title, String label) {
        return showAndSelectDescriptors(itd, lookup, false, descriptorClazz, label, title);
    }

    public static <T extends IBasicDescriptor> Collection<? extends T> showAndSelectDescriptors(final Collection<T> itd, final Lookup lookup, final boolean singleSelection, Class<? extends T> descriptorClazz, String title, String label) {
        DescriptorDialog<T> tdd = new DescriptorDialog<>(itd, singleSelection, lookup, descriptorClazz);
        tdd.showDialog(title, label);
        return tdd.getSelectedDescriptors();
    }
}
