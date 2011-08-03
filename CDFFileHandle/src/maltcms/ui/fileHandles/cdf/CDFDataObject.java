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
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

public class CDFDataObject extends MultiDataObject implements IFileFragmentDataObject {

    private InstanceContent ic = new InstanceContent();
    private IFileFragment objDelegate = null;

    public CDFDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        objDelegate = new FileFragment(new File(pf.getPath()));
        ic.add(objDelegate);
    }

    @Override
    protected Node createNodeDelegate() {
        return new DataNode(this, Children.LEAF, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(getCookieSet().getLookup(), Lookups.singleton(new CDFDataObjectNavigatorLookupHint()));
    }

    @Override
    public IFileFragment getFragment() {
        return this.objDelegate;
    }
    
    private static final class CDFDataObjectNavigatorLookupHint implements NavigatorLookupHint 
    {

        @Override
        public String getContentType() {
            return "application/x-cdf";
        }
        
    };
}
