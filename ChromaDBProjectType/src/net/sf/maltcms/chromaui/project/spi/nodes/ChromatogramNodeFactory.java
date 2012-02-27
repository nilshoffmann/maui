/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramNodeFactory extends ChildFactory<IChromatogramDescriptor>
        implements
        PropertyChangeListener {

    private final IChromAUIProject cp;
    private Lookup lkp;

    public ChromatogramNodeFactory(IChromAUIProject cp, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.cp = cp;
        this.lkp = lkp;
//        cp.getLookup().lookup(
//                ProjectInformation.class).addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<IChromatogramDescriptor> list) {
        Collection<? extends IChromatogramDescriptor> container = this.cp.getChromatograms();
        for (IChromatogramDescriptor idesc : container) {
            if (Thread.interrupted()) {
                return true;
            } else {
                if (idesc != null) {
                    list.add(idesc);
                }
            }
        }
        Collections.sort(list, new Comparator<IChromatogramDescriptor>() {

            @Override
            public int compare(IChromatogramDescriptor t,
                    IChromatogramDescriptor t1) {
                return t.getDisplayName().compareTo(t1.getDisplayName());
            }
        });
        return true;
    }

    @Override
    protected Node createNodeForKey(IChromatogramDescriptor cd) {
        Lookup lookup = Lookups.fixed(cd, cp);
        AbstractNode an = new AbstractNode(
                Children.create(new ChromatogramChildNodeFactory(
                cd, lookup), true));
        WeakListeners.propertyChange(this, an);
        WeakListeners.propertyChange(this, cd);
        an.setName(cd.getDisplayName());
        return an;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        System.out.println(getClass().getName() + " received property change event!");
        refresh(true);
    }
}
