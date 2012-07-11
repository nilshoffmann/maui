/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.ui;

import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.spi.nodes.DescriptorNode;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author hoffmann
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
            protected Node createNodeForKey(IToolDescriptor key) {
                Lookup.getDefault().lookup(INodeFactory.class).createDescriptorNode(key, Children.LEAF, lookup);
                Node customerNode;
                try {
                    customerNode = new DescriptorNode(key);
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                    customerNode = Node.EMPTY;
                }
                //customerNode.setDisplayName(key.getName());
                return customerNode;
            }
        }, true)));
        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dp, "Select Tool Results for Deletion",NotifyDescriptor.OK_CANCEL_OPTION);
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            return dp.getExplorerManager().getSelectedNodes()[0].getLookup().lookupAll(IToolDescriptor.class);
        }
        return Collections.emptyList();
    }
}
