/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import java.net.URL;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IUserAnnotation;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 *
 * @author hoffmann
 */
public interface IChromAUIProject extends Project{

    void addContainer(IContainer... ic);

    void removeContainer(IContainer... ic);

    <T extends IContainer> Collection<T> getContainer(Class<T> c);

    ICrudProvider getCrudProvider();
    
    void activate(URL dbfile);
    
    void setState(ProjectState state);
    
    Collection<IChromatogramDescriptor> getChromatograms();
    
    Collection<ITreatmentGroupDescriptor> getTreatmentGroups();
    
    Collection<DatabaseContainer> getDatabases();
    
    FileObject getOutputDir();
    
    void setOutputDir(FileObject f);

    FileObject getLocation();

}
