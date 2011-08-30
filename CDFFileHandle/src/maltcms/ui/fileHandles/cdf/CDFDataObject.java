/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import java.io.File;
import java.io.IOException;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

public class CDFDataObject extends MultiDataObject implements
        IFileFragmentDataObject {

    private InstanceContent ic = new InstanceContent();
    private AbstractLookup lookup = new AbstractLookup(ic);

    public CDFDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        ic.add(new FileFragment(new File(pf.getPath())));
        ic.add(new CDFDataObjectNavigatorLookupHint());
    }

    @Override
    protected Node createNodeDelegate() {
        return new DataNode(this, Children.LEAF);
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(getCookieSet().getLookup(),lookup);
    }

    @Override
    public IFileFragment getFragment() {
        return getLookup().lookup(IFileFragment.class);
    }

    private static final class CDFDataObjectNavigatorLookupHint implements
            NavigatorLookupHint {

        @Override
        public String getContentType() {
            return "application/x-cdf";
        }
    };
}
