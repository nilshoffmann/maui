/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import java.beans.PropertyChangeListener;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import java.net.URL;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 *
 * @author hoffmann
 */
public interface IChromAUIProject extends Project, IMauiProject {

    ICrudProvider getCrudProvider();
    
    void activate(URL dbfile);
    
    void setState(ProjectState state);
    
    FileObject getOutputDir();
    
    void setOutputDir(FileObject f);

    FileObject getLocation();
    
    void openSession();
    
    void closeSession();
    
    void refresh();
    
    void addPropertyChangeListener(PropertyChangeListener pcl);
    
    void removePropertyChangeListener(PropertyChangeListener pcl);

}
