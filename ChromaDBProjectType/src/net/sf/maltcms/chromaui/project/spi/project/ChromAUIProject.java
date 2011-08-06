/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProviderFactory;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.NoAuthCredentials;
import net.sf.maltcms.chromaui.project.spi.container.DatabaseContainer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.ProjectSettings;
import net.sf.maltcms.chromaui.project.spi.container.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.events.RefreshNodes;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

/**
 *
 * Implementation of a ChromAUI project type.
 * Abstracts from the concrete db schema used to represent the
 * logical structure of a project on disk.
 * Should be used as a starting point for other project type
 * implementations/extensions. See @link{http://platform.netbeans.org/tutorials/nbm-projectextension.html}
 * for details on writing a project type extension.
 *
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 */
@org.openide.util.lookup.ServiceProvider(service = Project.class)
public class ChromAUIProject implements IChromAUIProject {

    private ICrudProvider icp;
    private ICrudSession ics;
    private ProjectState state;
    private Lookup lkp;
    private FileObject projectDatabaseFile;
    private FileObject parentFile;
    private InstanceContent ic = new InstanceContent();

    @Override
    public void activate(URL projectDatabaseFile) {
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("maui.prj");
//        EntityManager em = factory.createEntityManager();

        if (this.icp != null) {
            this.icp.close();
        }
        File pdbf;
        try {
            pdbf = new File(projectDatabaseFile.toURI());
            this.parentFile = FileUtil.toFileObject(pdbf.getParentFile());
            this.projectDatabaseFile = FileUtil.createData(parentFile, pdbf.getName());//FileUtil.toFileObject(pdbf);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    @Override
    public void addContainer(IContainer... ic) {
        ics.create((Object) ic);
    }

    @Override
    public void removeContainer(IContainer... ic) {
        ics.delete((Object) ic);
    }

    @Override
    public void updateContainer(IContainer... ic) {
        ics.update((Object) ic);
    }

    protected void refresh() {
        ic.add(new RefreshNodes());
        ic.set(Arrays.asList(new RefreshNodes()), null);
    }

    @Override
    public <T extends IContainer> Collection<T> getContainer(Class<T> c) {
        Collection<T> l = ics.retrieve(c);
        return l;
    }

    public static void main(String[] args) {
    }

    @Override
    public ICrudProvider getCrudProvider() {
        if (this.icp == null) {
            openSession();
        }
        return this.icp;
    }

    @Override
    public FileObject getProjectDirectory() {
        return this.parentFile;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(
                    state, //allow outside code to mark the project as needing saving
                    new Info(), //Project information implementation
                    new ChromAUIProjectLogicalView(this), //Logical view of project implementation
                    new OpenCloseHook(),
                    ic);
        }
        return lkp;

    }

    @Override
    public void setState(ProjectState state) {
        this.state = state;
    }

    @Override
    public Collection<IChromatogramDescriptor> getChromatograms() {
        ArrayList<IChromatogramDescriptor> al = new ArrayList<IChromatogramDescriptor>();
        for (TreatmentGroupContainer cc : getContainer(
                TreatmentGroupContainer.class)) {
            al.addAll(cc.get());
        }
        return al;
    }

    @Override
    public Collection<ITreatmentGroupDescriptor> getTreatmentGroups() {
        HashSet<ITreatmentGroupDescriptor> tgs = new LinkedHashSet<ITreatmentGroupDescriptor>();
        for (TreatmentGroupContainer cc : getContainer(
                TreatmentGroupContainer.class)) {
            for (IChromatogramDescriptor icd : cc.get()) {
                tgs.add(icd.getTreatmentGroup());
            }
        }
        return tgs;
    }

    @Override
    public Collection<IDatabaseDescriptor> getDatabases() {
        getContainer(DatabaseContainer.class);
        HashSet<IDatabaseDescriptor> tgs = new LinkedHashSet<IDatabaseDescriptor>();
        for (DatabaseContainer cc : getContainer(
                DatabaseContainer.class)) {
            for (IDatabaseDescriptor icd : cc.get()) {
                tgs.add(icd);
            }
        }
        return tgs;
    }

    @Override
    public FileObject getOutputDir() {
        return getSetting("output.basedir", FileObject.class);
    }

    @Override
    public void setOutputDir(FileObject f) {
        ProjectSettings ps = getSettings();
        ps.put("output.basedir", f);
        ics.update(ps);
    }

    protected <T> T getSetting(String key, Class<T> c) throws NullPointerException, ClassCastException {
        ProjectSettings ps = getSettings();
        if (ps.containsKey(key)) {
            return c.cast(ps.get(key));
        }
        throw new NullPointerException("No element for key: " + key);
    }

    protected ProjectSettings getSettings() {
        Collection<ProjectSettings> l = ics.retrieve(ProjectSettings.class);
        if (l.isEmpty()) {
            ProjectSettings ps = new ProjectSettings();
            ics.create(Arrays.asList(ps));
            l = ics.retrieve(ProjectSettings.class);
        }
        ProjectSettings ps = l.toArray(new ProjectSettings[l.size()])[0];
        return ps;
    }

    @Override
    public FileObject getLocation() {
        return this.parentFile;
    }

    @Override
    public void openSession() {
        try {
            if (icp != null) {
                closeSession();
            }
            if (projectDatabaseFile == null) {
                throw new IllegalStateException("Project database file not set, please call 'activate(URL url)' with the appropriate location before 'openSession()' is called!");
            }
            icp = Lookup.getDefault().lookup(ICrudProviderFactory.class).
                    getCrudProvider(projectDatabaseFile.getURL(),
                    new NoAuthCredentials(), Lookup.getDefault().lookup(
                    ClassLoader.class));//new DB4oCrudProvider(pdbf, new NoAuthCredentials(), this.getClass().getClassLoader());
            icp.open();
            ics = icp.createSession();
            ics.open();
        } catch (FileStateInvalidException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void closeSession() {
        if (icp != null) {
            ics.close();
            icp.close();
            icp = null;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        ics.update(pce.getNewValue());
        refresh();
    }

    @Override
    public void addToLookup(Object... obj) {
        for (Object object : obj) {
            this.ic.add(object);
        }
    }

    private final class OpenCloseHook extends ProjectOpenedHook {

        @Override
        protected void projectOpened() {
            openSession();
        }

        @Override
        protected void projectClosed() {
            closeSession();
        }
    }

    private final class Info implements ProjectInformation {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(
                getProject());

        public void firePropertyChange(PropertyChangeEvent pce) {
            pcs.firePropertyChange(pce);
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(
                    ImageUtilities.loadImage(
                    "net/sf/maltcms/chromaui/project/resources/MauiProject.png"));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            this.pcs.addPropertyChangeListener(pcl);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            this.pcs.removePropertyChangeListener(pcl);
        }

        @Override
        public Project getProject() {
            return ChromAUIProject.this;
        }
    }
}
