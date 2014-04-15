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
package net.sf.maltcms.chromaui.project.spi;

import java.io.IOException;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

@DataObject.Registration(displayName = "Maui Project", mimeType = "application/db4o")
public class MauiProjectDataObject extends MultiDataObject {

    public MauiProjectDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
    }

    @Override
    protected Node createNodeDelegate() {
        try {
            IChromAUIProject p = (IChromAUIProject) ProjectManager.getDefault().findProject(this.getPrimaryFile().getParent());
            if(p==null) {
                return Node.EMPTY;
            }
            ChromAUIProjectLogicalView logicalView = p.getLookup().lookup(ChromAUIProjectLogicalView.class);
            if (logicalView != null) {
                return logicalView.createLogicalView();
            } else {
                return Node.EMPTY;
            }
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
