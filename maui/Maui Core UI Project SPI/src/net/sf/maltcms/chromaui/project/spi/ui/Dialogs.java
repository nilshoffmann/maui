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
package net.sf.maltcms.chromaui.project.spi.ui;

import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.spi.nodes.DescriptorNode;
import net.sf.maltcms.chromaui.ui.support.api.DialogPanel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class Dialogs {
    
    public static Collection<? extends IToolDescriptor> showAndSelectToolDescriptors(final Set<IToolDescriptor> itd, final Lookup lookup) {
        return showAndSelectToolDescriptors(itd, lookup, false);
    }
    
    public static Collection<? extends IToolDescriptor> showAndSelectToolDescriptors(final Set<IToolDescriptor> itd, final Lookup lookup, final boolean singleSelection) {
        DialogPanel dp = new DialogPanel();
        dp.init("Annotations of Tool: ",singleSelection);
        dp.getExplorerManager().setRootContext(new AbstractNode(Children.create(new ChildFactory<IToolDescriptor>() {

            @Override
            protected boolean createKeys(List list) {
                list.addAll(itd);
                return true;
            }

            @Override
            protected Node createNodeForKey(final IToolDescriptor key) {
                Lookup.getDefault().lookup(INodeFactory.class).createDescriptorNode(key, Children.LEAF, lookup);
                Node toolNode;
                try {
                    toolNode = new DescriptorNode(key){

						@Override
						public String getDisplayName() {
							return key.getDisplayName()+" "+key.getDate();
						}
					};
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                    toolNode = Node.EMPTY;
                }
                //customerNode.setDisplayName(key.getName());
                return toolNode;
            }
        }, true)));
			NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dp, "Select Tool Results for Deletion",NotifyDescriptor.OK_CANCEL_OPTION);
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            Node[] selectedNodes = dp.getExplorerManager().getSelectedNodes();
            List<IToolDescriptor> toolDescriptors = new LinkedList<IToolDescriptor>();
            for(Node n : selectedNodes) {
                toolDescriptors.addAll(n.getLookup().lookupAll(IToolDescriptor.class));
            }
            return toolDescriptors;
        }
        return Collections.emptyList();
    }
}
