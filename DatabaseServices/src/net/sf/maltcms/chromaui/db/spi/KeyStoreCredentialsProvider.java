/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.spi;

import net.sf.maltcms.chromaui.db.api.ICredentialsProvider;

/**
 *
 * @author hoffmann
 */
public class KeyStoreCredentialsProvider implements ICredentialsProvider{

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public char[] getPassword() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getCredentials() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
