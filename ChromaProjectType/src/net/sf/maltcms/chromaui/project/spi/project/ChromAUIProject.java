/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi.project;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.spi.DB4oCrudProvider;
import net.sf.maltcms.chromaui.db.spi.NoAuthCredentials;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.IContainer;

/**
 *
 * @author hoffmann
 */
public class ChromAUIProject implements IChromAUIProject {

    private final ICrudProvider icp;

    public ChromAUIProject(File projectDatabaseFile) {
        this.icp = new DB4oCrudProvider(projectDatabaseFile, new NoAuthCredentials());
        this.icp.open();
    }

    @Override
    public void addContainer(IContainer... ic) {
        ICrudSession ics = icp.createSession();
        ics.create(Arrays.asList(ic));
        ics.close();
    }

    @Override
    public void removeContainer(IContainer... ic) {
        ICrudSession ics = icp.createSession();
        ics.delete(Arrays.asList(ic));
        ics.close();
    }

    @Override
    public <T extends IContainer> Collection<T> getContainer(Class<T> c) {
        ICrudSession ics = icp.createSession();
        Collection<T> l = ics.retrieve(c);
        ics.close();
        return l;
    }

    public static void main(String[] args) {

    }

    @Override
    public ICrudProvider getCrudProvider() {
        return this.icp;
    }

}
