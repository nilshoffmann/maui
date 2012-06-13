/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.api;

/**
 *
 * @author hoffmann
 */
public interface ICredentialsProvider {

    public String getName();

    public char[] getPassword();

    public void getCredentials();

}
