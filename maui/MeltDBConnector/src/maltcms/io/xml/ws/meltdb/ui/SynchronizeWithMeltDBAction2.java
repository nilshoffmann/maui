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
package maltcms.io.xml.ws.meltdb.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import maltcms.io.xml.ws.meltdb.MeltDBSession;
import maltcms.io.xml.ws.meltdb.WebServiceClient;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.netbeans.api.keyring.Keyring;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(
        category = "File",
        id = "maltcms.io.xml.ws.meltdb.ui.SynchronizeWithMeltDBAction2")
@ActionRegistration(
        displayName = "#CTL_SynchronizeWithMeltDBAction2")
@ActionReference(path = "Menu/File", position = 1423, separatorAfter = 1424)
@Messages("CTL_SynchronizeWithMeltDBAction2=Synchronize With MeltDB 2")
public final class SynchronizeWithMeltDBAction2 implements ActionListener {

    private final IChromAUIProject context;

    /**
     *
     * @param context
     */
    public SynchronizeWithMeltDBAction2(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        final CredentialsPanel cp = new CredentialsPanel();
        String userName = NbPreferences.forModule(SynchronizeWithMeltDBAction2.class).get("userName", System.getProperty("user.name"));
        cp.setUserName(userName);
        cp.setStoreInKeyring(NbPreferences.forModule(SynchronizeWithMeltDBAction2.class).getBoolean("storeInKeyring", true));
        char[] password = Keyring.read(userName + "@meltdb");
        if (password != null) {
            cp.setPassword(password);
        }
        DialogDescriptor dd = new DialogDescriptor(cp, "MeltDB Login", true, DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String user = cp.getUserName();
                NbPreferences.forModule(SynchronizeWithMeltDBAction2.class).put("userName", user);
                NbPreferences.forModule(SynchronizeWithMeltDBAction2.class).putBoolean("storeInKeyring", cp.isStoreInKeyring());
                Keyring.save(user + "@meltdb", cp.getPassword(), "MeltDB account");
                cp.setUserName(null);
                char[] c = cp.getPassword();
                for (int i = 0; i < c.length; i++) {
                    c[i] = 0;
                }
                cp.setPassword(new char[0]);
                MeltDBSession session = new MeltDBSession(user, Keyring.read(user + "@meltdb"));
                WebServiceClient mw = new WebServiceClient(session);
                Dialog d = DialogDisplayer.getDefault().createDialog(new DialogDescriptor(mw.getPanel(), "MeltDB Synchronization", true, null));
                d.setVisible(true);
            }
        });
        DialogDisplayer.getDefault().notify(dd);
    }

}
