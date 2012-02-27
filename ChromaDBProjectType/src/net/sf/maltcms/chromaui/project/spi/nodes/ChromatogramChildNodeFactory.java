/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramChildNodeFactory extends ChildFactory<Peak1DContainer>
        implements
        PropertyChangeListener {

    private final IChromatogramDescriptor chromatogramDescriptor;
    private Lookup lkp;

    public ChromatogramChildNodeFactory(
            IChromatogramDescriptor chromatogramDescriptor, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.chromatogramDescriptor = chromatogramDescriptor;
        this.lkp = lkp;
//        this.lkp.lookup(IChromAUIProject.class).getLookup().lookup(
//                ProjectInformation.class).addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<Peak1DContainer> list) {
        Collection<Peak1DContainer> peakContainer = lkp.lookup(
                IChromAUIProject.class).getPeaks(chromatogramDescriptor);
        if (peakContainer.isEmpty()) {
            System.out.println("Did not find any peaks for " + chromatogramDescriptor.getDisplayName());
            return true;
        }
        for (Peak1DContainer pad : peakContainer) {
            if (Thread.interrupted()) {
                return true;
            } else {
                if (pad != null) {
                    list.add(pad);
                }
            }
        }
        Collections.sort(list);
        return true;
//        list.add(chromatogramDescriptor);
//        return true;
    }

    @Override
    protected Node createNodeForKey(Peak1DContainer key) {
        try {
            //container node, add current lookup, make container available generically
            ContainerNode cn = new ContainerNode(key, Lookups.fixed(this.chromatogramDescriptor, this.lkp.lookup(IChromAUIProject.class)));
            WeakListeners.propertyChange(this, key);
            WeakListeners.propertyChange(this, cn);
            return cn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        refresh(true);
//        this.lkp.lookup(IChromAUIProject.class).refresh();
    }
}
