/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.api;

import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;

/**
 *
 * @author hoffmann
 */
public interface ICrudProvider {

    ICrudSession createSession() throws AuthenticationException;

    void open() throws AuthenticationException;

    void close() throws AuthenticationException;

}
