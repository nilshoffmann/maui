/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.IDatabase;

/**
 *
 * @author hoffmann
 */
public interface IUserDatabaseContainer<T extends IDatabase> {

    Collection<T> getUserDatabases();

    void setUserDatabases(T... f);

    void addUserDatabases(T... f);

    void removeUserDatabases(T... f);
}
