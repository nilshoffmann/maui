/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.api;

/**
 *
 * @author hoffmann
 */
public final class NoAuthCredentials implements ICredentials{

    private String name = "Project database";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean authenticate() {
        return true;
    }

    @Override
    public void provideCredentials(ICredentialsProvider icp) {
        
    }

}
