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
            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
            System.out.println("Copying keystore data!");
            int i;
            while ((i = fis.read()) != -1) {
                fos.write(i);
            }
            fos.flush();
            fos.close();
            System.out.println("Setting keystore properties");
            System.setProperty("javax.net.ssl.trustStore", f.getAbsolutePath());
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");//"meltdb-cert");
        } catch (IOException ex) {
            Logger.getLogger(WebServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
