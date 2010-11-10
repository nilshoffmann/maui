/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import java.net.URI;

/**
 *
 * @author hoffmann
 */
public interface IDatabase {

    URI getLocation();

    void setLocation(URI u);

    String getName();

    void setName(String name);

    DatabaseType getType();

    void setType(DatabaseType type);

}
