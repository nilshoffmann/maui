/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.api;

/**
 *
 * @author hoffmann
 */
public interface ICredentials {

    String getName();

    void setName(String name);

    void provideCredentials(ICredentialsProvider icp);

    boolean authenticate();

}
