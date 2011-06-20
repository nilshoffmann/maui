/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.api;

import java.net.URL;

/**
 *
 * @author nilshoffmann
 */
public interface ICrudProviderFactory {
    
    public ICrudProvider getCrudProvider(URL databaseLocation, ICredentials ic);
    
}
