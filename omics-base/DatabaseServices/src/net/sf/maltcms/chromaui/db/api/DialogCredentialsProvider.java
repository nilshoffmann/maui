/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.api;

import net.sf.maltcms.chromaui.db.api.ICredentialsProvider;

/**
 *
 * @author hoffmann
 */
public final class DialogCredentialsProvider implements ICredentialsProvider{

    private String name = null;

    @Override
    public final String getName() {
        if(this.name ==null) {
            getCredentials();
        }
        return this.name;
    }

    @Override
    public final char[] getPassword() {
//        Keyring k;
        return null;
    }

    @Override
    public final void getCredentials() {

    }

}
