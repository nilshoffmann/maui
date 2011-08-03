/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project;

import java.io.IOException;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.project.ChromAUIProjectLogicalView;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

public class MauiProjectDataObject extends MultiDataObject {

    public MauiProjectDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
    }

    @Override
    protected Node createNodeDelegate() {
        try {
            IChromAUIProject p = (IChromAUIProject) ProjectManager.getDefault().findProject(this.getPrimaryFile().getParent());
            return p.getLookup().lookup(ChromAUIProjectLogicalView.class).createLogicalView();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NullPointerException npe) {
            Exceptions.printStackTrace(npe);
        }
        return Node.EMPTY;
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(createNodeDelegate().getLookup());
    }
}
