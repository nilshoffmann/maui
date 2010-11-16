/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.io.xml.ws.meltdb.ws;

import java.net.URI;
import net.sf.maltcms.chromaui.db.api.ICredentials;
import net.sf.maltcms.chromaui.db.api.ICredentialsProvider;

/**
 *
 * @author hoffmann
 */
public final class MeltDBCredentials implements ICredentials{

    private volatile transient char[] password = null;

    private String username;

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public void provideCredentials(ICredentialsProvider icp) {
        
    }

    @Override
    public boolean authenticate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
