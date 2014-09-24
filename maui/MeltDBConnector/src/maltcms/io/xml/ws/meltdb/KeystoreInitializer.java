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
package maltcms.io.xml.ws.meltdb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.OnStart;

/**
 *
 * @author Nils Hoffmann
 */
@OnStart
public class KeystoreInitializer implements Runnable {

    @Override
    public void run() {
        BufferedInputStream fis = new BufferedInputStream(WebServiceClient.class.getClassLoader().getResourceAsStream("maltcms/io/xml/ws/meltdb/meltdbKeystore.ks"));
        File f;
        try {
            f = File.createTempFile("meltdbKeystore", "ks");
            try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
                Logger.getLogger(getClass().getName()).info("Copying keystore data!");
                int i;
                while ((i = fis.read()) != -1) {
                    fos.write(i);
                }
                fos.flush();
            }
            Logger.getLogger(getClass().getName()).info("Setting keystore properties");
            System.setProperty("javax.net.ssl.trustStore", f.getAbsolutePath());
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");//"meltdb-cert");
        } catch (IOException ex) {
            Logger.getLogger(WebServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
