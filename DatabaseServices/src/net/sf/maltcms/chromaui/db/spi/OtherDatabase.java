/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi;

import java.net.URI;
import net.sf.maltcms.chromaui.db.api.DatabaseType;
import net.sf.maltcms.chromaui.db.api.IDatabase;

/**
 *
 * @author hoffmann
 */
public class OtherDatabase
        implements IDatabase {

    private URI location;
    private String name;
    private final DatabaseType type = DatabaseType.OTHER;

    @Override
    public URI getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(URI u) {
        this.location = u;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DatabaseType getType() {
        return this.type;
    }
}
