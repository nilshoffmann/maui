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
package net.sf.maltcms.chromaui.branding.welcome;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Nils Hoffmann
 * @author Ernie Rael for the original post on the NetBeans list
 */
public class Actions {

    public static void execFileSystemAction(String path, ActionEvent e) {
        Action act = fetchFileSystemAction(path);
        if (act != null && act.isEnabled()) {
            act.actionPerformed(e);
        } else {
            Logger.getLogger(Actions.class.getName()).log(Level.WARNING, "Failed to invoke action for path: {0}", path);
        }
    }

    /**
     * Get an Action from the file system at the given path. Check if it is a
     * SystemAction, if not then try to create it.
     *
     * @return an Action, null if couldn't get or create one
     */
    public static Action fetchFileSystemAction(String path) {
        FileObject fo = Repository.getDefault().getDefaultFileSystem()
                .getRoot().getFileObject(path);
        if (fo == null) {
            return null;
        }

        InstanceCookie ck = null;
        Action act = null;
        try {
            ck = DataObject.find(fo).getCookie(InstanceCookie.class);
        } catch (DataObjectNotFoundException ex) {
        }
        if (ck != null) {
            try {

                if (SystemAction.class.isAssignableFrom(ck.instanceClass())) {
                    @SuppressWarnings("unchecked")
                    Class<SystemAction> sa = (Class<SystemAction>) ck.instanceClass();
                    act = SystemAction.get(sa);
                }
            } catch (IOException | ClassNotFoundException ex) {
            }
            if (act == null) {
                // if its not a SystemAction try creating one
                Object o = null;
                try {
                    o = ck.instanceCreate();
                } catch (IOException | ClassNotFoundException ex) {
                }
                if (o instanceof Action) {
                    act = (Action) o;
                }
            }
        }
        return act;
    }
}
