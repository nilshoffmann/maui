/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.events.RefreshNodes;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author nilshoffmann
 */
public class ChromatogramChildNodeFactory extends ChildFactory<IPeakAnnotationDescriptor>
        implements
        LookupListener {

    private final IChromatogramDescriptor chromatogramDescriptor;
    private Lookup lkp;

    public ChromatogramChildNodeFactory(
            IChromatogramDescriptor chromatogramDescriptor, Lookup lkp) {
//        System.out.println("Created ContainerNode Factory for " + cp.getClass());
        this.chromatogramDescriptor = chromatogramDescriptor;
        this.lkp = lkp;
        Result<RefreshNodes> lookupResult = lkp.lookupResult(
                RefreshNodes.class);
        lookupResult.addLookupListener(this);
    }

    @Override
    protected boolean createKeys(List<IPeakAnnotationDescriptor> list) {
        if(chromatogramDescriptor.getPeakAnnotationDescriptors()==null) {
            return true;
        }
        for (IPeakAnnotationDescriptor pad : chromatogramDescriptor.
                getPeakAnnotationDescriptors()) {
            if (Thread.interrupted()) {
                return true;
            } else {
                list.add(pad);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(IPeakAnnotationDescriptor key) {
        try {
            //leaf node, add current lookup, make containter available generically
            DescriptorNode cn = new DescriptorNode(key, new ProxyLookup(lkp,
                    Lookups.fixed(key)));
            return cn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            refresh(true);
        }
    }
}
