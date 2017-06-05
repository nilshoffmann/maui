/*
 * Maui, Maltcms User Interface.
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code
 * under. Certain files or entire directories may not be covered by this
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.project.spi.project;

import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import cross.exception.ConstraintViolationException;
import cross.exception.ResourceNotAvailableException;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProviderFactory;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.NoAuthCredentials;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.IMauiSubprojectProviderFactory;
import net.sf.maltcms.chromaui.project.api.ProjectSettings;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.ChromAUIProjectLogicalView;
import net.sf.maltcms.chromaui.project.spi.runnables.DeletePeakAnnotationsRunnable;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.support.LookupProviderSupport;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * Implementation of a ChromAUI project type. Abstracts from the concrete db
 * schema used to represent the logical structure of a project on disk. Should
 * be used as a starting point for other project type
 * implementations/extensions. See
 * @link{http://platform.netbeans.org/tutorials/nbm-projectextension.html} for
 * details on writing a project type extension.
 *
 * Supports the dynamic lookup of subproject provider implementations via
 * {@link IMauiSubprojectProviderFactory}. Add the
 * {@code @ServiceProvider(service=IMauiSubprojectProviderFactory.class)}
 * annotation to your custom implementation to have it automatically registered
 * in this project.
 *
 * @author Nils Hoffmann
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
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private PropertiesConfiguration projectSettings;

    public synchronized void removePropertyChangeListener(String string, PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(string, pl);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(pl);
    }

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(pl);
    }

    public ChromAUIProject() {
        lkp = LookupProviderSupport.createCompositeLookup(new AbstractLookup(
                ic), "Projects/net-sf-maltcms-chromaui-project/Lookup");
        if (state != null) {
            ic.add(state);
        }
        ic.add(this);
        //Logical view of project implementation
        ic.add(new OpenCloseHook());
        //allow outside code to mark the project as needing saving
        ic.add(new Info());
        //Project information implementation
        ic.add(new ChromAUIProjectLogicalView(this));
        //Customization dialogs
        ic.add(new ChromAUIProjectCustomizerProvider(this));
        //ClassPath Provider for Maltcms installations
        ic.add(new MaltcmsClassPathProvider());
        //Action provider for default project actions
        ic.add(new ActionProviderImpl(this));
        ic.add(new DeleteOperationImpl(this));
        ic.add(new CopyOperationImpl(this));
        //SubProject Providers for Maltcms processing results
        for (IMauiSubprojectProviderFactory factory : Lookup.getDefault().lookupAll(IMauiSubprojectProviderFactory.class)) {
            ic.add(factory.provide(getLookup().lookup(IChromAUIProject.class)));
        }
    }

    @Override
    public void activate(URL projectDatabaseFile) {
        if (this.icp != null) {
            this.icp.close();
        }
        File pdbf;
        try {
            pdbf = Utilities.toFile(projectDatabaseFile.toURI());
            this.parentFile = FileUtil.toFileObject(pdbf.getParentFile());
            this.projectDatabaseFile = FileUtil.createData(parentFile, pdbf.getName());//FileUtil.toFileObject(pdbf);
        } catch (IOException | URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    @Override
    public void addContainer(IContainer... ic) {
        for (IContainer container : ic) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding container: {0}", ic.toString());
            ics.create(container);
            if (container.getProject() == null) {
                container.setProject(this);
            }
        }
        refresh();
    }

    @Override
    public void removeContainer(IContainer... ic) {
        DialogDisplayer dd = DialogDisplayer.getDefault();
        Object result = dd.notify(new NotifyDescriptor.Confirmation(
                "Really delete " + ic.length + " container" + (ic.length > 1 ? "s" : "") + "?",
                "Confirm container deletion", NotifyDescriptor.YES_NO_OPTION));
        if (result.equals(NotifyDescriptor.YES_OPTION)) {
            for (IContainer container : ic) {
                ics.delete(container);
            }
            refresh();
        }
    }

    @Override
    public void removeDescriptor(IBasicDescriptor... descriptor) {
        DialogDisplayer dd = DialogDisplayer.getDefault();
        boolean deleteAll = false;
        if (descriptor.length > 1) {
            Object result = dd.notify(new NotifyDescriptor.Confirmation(
                    "Delete all " + descriptor.length + " descriptor" + (descriptor.length > 1 ? "s" : "") + "?\nYes will delete all selected Descriptors, No to select each Descriptor individually.",
                    "Confirm descriptor deletion", NotifyDescriptor.YES_NO_CANCEL_OPTION));
            if (result.equals(NotifyDescriptor.CANCEL_OPTION)) {
                return;
            } else if (result.equals(NotifyDescriptor.NO_OPTION)) {
                deleteAll = false;
            }
        }
        if (deleteAll) {
            ics.delete(Arrays.asList(descriptor));
        } else {
            for (IBasicDescriptor descr : descriptor) {
                if (!(descr instanceof IContainer)) {
                    Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor.Confirmation(
                            "Really delete descriptor: " + descr.getDisplayName() + "?",
                            "Confirm descriptor deletion", NotifyDescriptor.YES_NO_OPTION));
                    if (result.equals(NotifyDescriptor.YES_OPTION)) {
                        ics.delete(descr);
                    }
                }
            }
        }
        refresh();
    }

    @Override
    public void updateContainer(IContainer... ic) {
        ics.update((Object) ic);
        refresh();
    }

    @Override
    public void refresh() {
        pcs.firePropertyChange(new PropertyChangeEvent(
                this, "container", false, true));
    }

    @Override
    public <T extends IContainer> Collection<T> getContainer(Class<T> c) {
        if (icp != null) {
            ICrudSession localSession = null;
            try {
                localSession = icp.createSession();
                Collection<T> l = localSession.retrieve(c);
                localSession.close();
                for (IContainer container : l) {
                    if (container.getProject() == null) {
                        container.setProject(this);
                    }
                }
                return l;
            } finally {
                if (localSession != null) {
                    localSession.close();
                }
            }
        }
        return Collections.emptyList();
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
        return lkp;

    }

    @Override
    public void setState(ProjectState state) {
        if (this.state != null) {
            ic.remove(this.state);
        }
        ic.add(state);
        this.state = state;
    }

    @Override
    public Collection<IChromatogramDescriptor> getChromatograms() {
        ArrayList<IChromatogramDescriptor> al = new ArrayList<>();
        for (TreatmentGroupContainer cc : getContainer(
                TreatmentGroupContainer.class)) {
            al.addAll(cc.getMembers());
        }
        return al;
    }

    @Override
    public Collection<ITreatmentGroupDescriptor> getTreatmentGroups() {
        HashSet<ITreatmentGroupDescriptor> tgs = new LinkedHashSet<>();
        for (TreatmentGroupContainer cc : getContainer(
                TreatmentGroupContainer.class)) {
            for (IChromatogramDescriptor icd : cc.getMembers()) {
                tgs.add(icd.getTreatmentGroup());
            }
        }
        return tgs;
    }

    @Override
    public Collection<ISampleGroupDescriptor> getSampleGroups() {
        HashSet<ISampleGroupDescriptor> tgs = new LinkedHashSet<>();
        for (SampleGroupContainer cc : getContainer(
                SampleGroupContainer.class)) {
            for (IChromatogramDescriptor icd : cc.getMembers()) {
                tgs.add(icd.getSampleGroup());
            }
        }
        return tgs;
    }

    @Override
    public Collection<SampleGroupContainer> getSampleGroupsForTreatmentGroup(ITreatmentGroupDescriptor treatmentGroup) {
        HashSet<SampleGroupContainer> tgs = new LinkedHashSet<>();
        for (SampleGroupContainer cc : getContainer(
                SampleGroupContainer.class)) {
            for (IChromatogramDescriptor icd : cc.getMembers()) {
                if (icd.getTreatmentGroup().getName().equals(treatmentGroup.getName())) {
                    tgs.add(cc);
                }
            }
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Sample groups: {0}", tgs);
        return tgs;
    }

    @Override
    public Collection<IDatabaseDescriptor> getDatabases() {
        HashSet<IDatabaseDescriptor> tgs = new LinkedHashSet<>();
        for (DatabaseContainer cc : getContainer(
                DatabaseContainer.class)) {
            for (IDatabaseDescriptor icd : cc.getMembers()) {
                tgs.add(icd);
            }
        }
        return tgs;
    }

    @Override
    public synchronized FileObject getOutputDir() {
        String outputBasedir = getSetting(ProjectSettings.KEY_OUTPUT_BASEDIR);
        if (outputBasedir != null) {
            if(outputBasedir.startsWith("file:")) {
                URI uri = URI.create(outputBasedir);
                return FileUtil.toFileObject(new File(uri));
            }
            return FileUtil.toFileObject(new File(outputBasedir));
        } else {
            Logger.getLogger(ChromAUIProject.class.getName()).log(Level.WARNING, "Failed to retrieve setting '{0}'. Restoring default value!", new Object[]{ProjectSettings.KEY_OUTPUT_BASEDIR});
            FileObject outputDir = null;
            try {
                outputDir = FileUtil.createFolder(parentFile, "output");
            } catch (IOException ex) {
                File outdir = new File(FileUtil.toFile(parentFile), "output");
                outdir.mkdirs();
                outputDir = FileUtil.toFileObject(outdir);
            }
            getSettings().addProperty(ProjectSettings.KEY_OUTPUT_BASEDIR, outputDir.getPath());
            Logger.getLogger(ChromAUIProject.class.getName()).log(Level.INFO, "Updated setting '{0}': {1}", new Object[]{ProjectSettings.KEY_OUTPUT_BASEDIR, outputDir.getPath()});
            return outputDir;
        }
    }

    @Override
    public void setOutputDir(FileObject f) {
        Configuration ps = getSettings();
        ps.setProperty(ProjectSettings.KEY_OUTPUT_BASEDIR, f.getPath());
    }

    @Override
    public File getOutputLocation(Object importer) {
        File outputDir = FileUtil.toFile(getOutputDir());
        String name = "";
        if (importer instanceof Class) {
            name = ((Class) importer).getSimpleName();
        } else {
            name = importer.getClass().getSimpleName();
        }
        outputDir = new File(outputDir, name);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM-dd-yyyy_HH-mm-ss", Locale.US);
        outputDir = new File(outputDir, dateFormat.format(
                new Date()));
        outputDir.mkdirs();
        return outputDir;
    }

    protected synchronized String getSetting(String key) {
        Configuration ps = getSettings();
        if (ps.containsKey(key)) {
            return ps.getString(key);
        }
        return null;
    }

    protected synchronized Configuration getSettings() {
        File preferencesFile = new File(FileUtil.toFile(getProjectDirectory()), "project.properties");
        if (!preferencesFile.exists()) {
            try {
                projectSettings = new PropertiesConfiguration(preferencesFile);
                projectSettings.setAutoSave(true);
                if (ics == null) {
                    openSession();
                }
                Collection<ProjectSettings> l = ics.retrieve(ProjectSettings.class);
                if (!l.isEmpty()) {
                    Logger.getLogger(ChromAUIProject.class.getName()).log(Level.INFO, "Converting old project settings.");
                    l = ics.retrieve(ProjectSettings.class);
                    ProjectSettings ps = l.iterator().next();
                    for (String key : ps.keySet()) {
                        Logger.getLogger(ChromAUIProject.class.getName()).log(Level.INFO, "Updating setting {0}={1}", new Object[]{key, ps.get(key).toString()});
                        if (ps.get(key) instanceof FileObject) {
                            FileObject fo = (FileObject) ps.get(key);
                            String path = fo.getPath();
                            Logger.getLogger(ChromAUIProject.class.getName()).log(Level.INFO, "Setting path for {0} to {1}.", new Object[]{key, path});
                            projectSettings.setProperty(key, path);
                        } else {
                            projectSettings.setProperty(key, ps.get(key).toString());
                        }
                    }
                    Logger.getLogger(ChromAUIProject.class.getName()).log(Level.INFO, "Deleting old project settings from database!");
                    ics.delete(ps);
                }
                projectSettings.save();
            } catch (ConfigurationException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            try {
                projectSettings = new PropertiesConfiguration(preferencesFile);
                projectSettings.setAutoSave(true);
            } catch (ConfigurationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return projectSettings;
    }

    @Override
    public FileObject getLocation() {
        return this.parentFile;
    }

    @Override
    public synchronized void openSession() {
        try {
            if (icp != null) {
                closeSession();
            }
            if (projectDatabaseFile == null) {
                throw new IllegalStateException(
                        "Project database file not set, please call 'activate(URL url)' with the appropriate location before 'openSession()' is called!");
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
    public synchronized void closeSession() {
        if (icp != null) {
            icp.close();
            icp = null;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce
    ) {
        pcs.firePropertyChange(pce);
    }

    @Override
    public void addToLookup(Object... obj
    ) {
        for (Object object : obj) {
            if (object != null) {
                this.ic.add(object);
            }
        }
    }

    @Override
    public Collection<Peak1DContainer> getPeaks(
            IChromatogramDescriptor descriptor
    ) {
        Collection<Peak1DContainer> containers = getContainer(
                Peak1DContainer.class);
        List<Peak1DContainer> peaks = new ArrayList<Peak1DContainer>();

        for (Peak1DContainer container : containers) {
            if (container != null) {
                IChromatogramDescriptor chrom = container.getChromatogram();
                if (chrom != null) {
                    String chromName = chrom.getDisplayName();
                    String descrName = descriptor.getDisplayName();
                    if (chromName != null && descrName != null) {
                        if (chromName.equals(descrName)) {
                            peaks.add(container);
                        }
                    }
                }
            }
        }

        return peaks;
    }

    @Override
    public File getImportLocation(Object importer
    ) {
        File importDir = getImportDirectory();
        if (importer instanceof Class) {
            importDir = new File(importDir, ((Class) importer).getSimpleName());
        } else {
            importDir = new File(importDir, importer.getClass().getSimpleName());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM-dd-yyyy_HH-mm-ss", Locale.US);
        importDir = new File(importDir, dateFormat.format(
                new Date()));
        importDir.mkdirs();
        return importDir;
    }

    public Map<String, Map<File, List<File>>> getImportDirectoryFiles() {
        File importDir = getImportDirectory();
        File[] toolFiles = importDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        Map<String, Map<File, List<File>>> importDirectoryFiles = new LinkedHashMap<>();
        for (File tool : toolFiles) {
            importDirectoryFiles.put(tool.getName(), getToolFiles(tool));
        }
        return importDirectoryFiles;
    }

    public Map<File, List<File>> getImportDirectoryFilesFor(String toolname) {
        File importDir = getImportDirectory();
        File tool = new File(importDir, toolname);
        if (tool.isDirectory()) {
            return getToolFiles(tool);
        }
        return Collections.emptyMap();
    }

    private Map<File, List<File>> getToolFiles(File tool) {
        File[] dates = tool.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        Map<File, List<File>> instanceFiles = new LinkedHashMap<>();
        for (File date : dates) {
            instanceFiles.put(date, new ArrayList<>(FileUtils.listFiles(date, null, true)));
        }
        return instanceFiles;
    }

    @Override
    public File getImportDirectory() {
        try {
            return FileUtil.toFile(FileUtil.createFolder(getProjectDirectory(), "import"));
        } catch (IOException ex) {
            File outdir = new File(FileUtil.toFile(getProjectDirectory()), "import");
            outdir.mkdirs();
            return outdir;
        }
    }

    @Override
    public File getOutputDirectory() {
        return FileUtil.toFile(getOutputDir());
    }

    /**
     *
     * @param <T> the IDescriptor type to query for
     * @param descriptorId the requested id of the container
     * @param descriptorClass the explicit class of the container
     * @throws ConstraintViolationException if more than one object with the
     * requested id was found
     * @throws ResourceNotAvailableException if no object with the requested id
     * was found
     * @throws IllegalStateException if the database was not yet initialized
     * @return
     */
    @Override
    public <T extends IBasicDescriptor> T getDescriptorById(final UUID descriptorId, Class<? extends T> descriptorClass) {
        if (icp != null) {
            ICrudSession ics = icp.createSession();
            try {
                IQuery<? extends T> query = ics.newQuery(descriptorClass);
                Collection<T> results = query.retrieve(new Predicate<T>() {
                    private static final long serialVersionUID = -7402207122617612419L;

                    @Override
                    public boolean match(T et) {
                        return descriptorId.equals(et.getId());
                    }
                });
                if (results.isEmpty()) {
                    throw new ResourceNotAvailableException("Object with id=" + descriptorId.toString() + " not found in database!");
                }
                if (results.size() > 1) {
                    throw new ConstraintViolationException("Query by unique id=" + descriptorId.toString() + " returned " + results.size() + " instances. Expected 1!");
                }
                return results.iterator().next();
            } finally {
                ics.close();
            }
        }
        throw new IllegalStateException("Database not initialized!");
    }

    /**
     *
     * @param <T> the IContainer type to query for
     * @param containerId the requested id of the container
     * @param containerClass the explicit class of the container
     * @throws ConstraintViolationException if more than one object with the
     * requested id was found
     * @throws ResourceNotAvailableException if no object with the requested id
     * was found
     * @throws IllegalStateException if the database was not yet initialized
     * @return
     */
    @Override
    public <T extends IContainer> T getContainerById(final UUID containerId, Class<? extends T> containerClass) {
        if (icp != null) {
            ICrudSession ics = icp.createSession();
            try {
                IQuery<? extends T> query = ics.newQuery(containerClass);
                Collection<T> results = query.retrieve(new Predicate<T>() {
                    private static final long serialVersionUID = -7402207122617612419L;

                    @Override
                    public boolean match(T et) {
                        return containerId.equals(et.getId());
                    }
                });
                if (results.isEmpty()) {
                    throw new ResourceNotAvailableException("Object with id=" + containerId.toString() + " not found in database!");
                }
                if (results.size() > 1) {
                    throw new ConstraintViolationException("Query by unique id=" + containerId.toString() + " returned " + results.size() + " instances. Expected 1!");
                }
                return results.iterator().next();
            } finally {
                ics.close();
            }
        }
        throw new IllegalStateException("Database not initialized!");
    }

    @Override
    public Set<IToolDescriptor> getToolsForPeakContainers() {
        final Set<IToolDescriptor> tools = new LinkedHashSet<>();
        ICrudSession session = null;
        try {
            session = getCrudProvider().createSession();
            session.open();
            Query query = session.getSODAQuery();
            query.constrain(Peak1DContainer.class);
            query.descend("tool").constrain(IToolDescriptor.class);
            ObjectSet<Peak1DContainer> peakContainers = query.execute();
            for (Peak1DContainer td : peakContainers) {
                Logger.getLogger(DeletePeakAnnotationsRunnable.class.getName()).
                        log(Level.INFO, "Adding tool descriptor {0}", new Object[]{td.getTool().getDisplayName()});
                tools.add(td.getTool());
            }
        } catch (AuthenticationException ae) {
            Exceptions.printStackTrace(ae);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (AuthenticationException ae) {
                Exceptions.printStackTrace(ae);
            }
        }
        return tools;
    }

    @Override
    public UUID getId() {
        Configuration settings = getSettings();
        if (settings.containsKey(ProjectSettings.KEY_UID)) {
            return UUID.fromString((String) settings.getString(ProjectSettings.KEY_UID));
        } else {
            UUID uid = UUID.randomUUID();
            settings.setProperty(ProjectSettings.KEY_UID, uid.toString());
            return uid;
        }
    }

//	@Override
//	public String toString() {
//		return ProjectUtils.getInformation(this).getDisplayName();
//	}
    private final class OpenCloseHook extends ProjectOpenedHook {

        @Override
        protected void projectOpened() {
            openSession();
        }

        @Override
        protected void projectClosed() {
            IRegistry registry = Lookup.getDefault().lookup(IRegistryFactory.class).getDefault();
            Project project = getLookup().lookup(Project.class);
            registry.closeTopComponentsForProject(project);
            closeSession();
        }
    }

    private final class Info implements ProjectInformation {

        private final PropertyChangeSupport pcs = new PropertyChangeSupport(
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

    private final class DeleteOperationImpl implements DeleteOperationImplementation {

        private final IChromAUIProject project;

        public DeleteOperationImpl(IChromAUIProject project) {
            this.project = project;
        }

        @Override
        public List<FileObject> getMetadataFiles() {
            return Collections.emptyList();
        }

        @Override
        public List<FileObject> getDataFiles() {
            Enumeration<? extends FileObject> e = project.getLocation().getChildren(true);
            ArrayList<FileObject> list = new ArrayList<>();
            list.addAll(Collections.list(e));
            list.add(project.getLocation());
            return list;
        }

        @Override
        public void notifyDeleting() throws IOException {
            closeSession();
        }

        @Override
        public void notifyDeleted() throws IOException {
        }
    }

    private final class CopyOperationImpl implements CopyOperationImplementation {

        private final IChromAUIProject project;

        public CopyOperationImpl(IChromAUIProject project) {
            this.project = project;
        }

        @Override
        public void notifyCopying() throws IOException {
            project.closeSession();
        }

        @Override
        public void notifyCopied(Project prjct, File file, String string) throws IOException {
            project.openSession();
        }

        @Override
        public List<FileObject> getMetadataFiles() {
            return Collections.emptyList();
        }

        @Override
        public List<FileObject> getDataFiles() {
            Enumeration<? extends FileObject> e = project.getLocation().getChildren(true);
            ArrayList<FileObject> list = new ArrayList<>();
            list.addAll(Collections.list(e));
            list.add(project.getLocation());
            return list;
        }
    }

    private final class ActionProviderImpl implements ActionProvider {

        private String[] supported = new String[]{
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY};
        private final IChromAUIProject project;

        public ActionProviderImpl(IChromAUIProject project) {
            this.project = project;
        }

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(project);
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(project);
            }
        }

        @Override
        public boolean isActionEnabled(String command, Lookup lookup) throws IllegalArgumentException {
            switch (command) {
                case ActionProvider.COMMAND_DELETE:
                    return true;
                case ActionProvider.COMMAND_COPY:
                    return true;
                default:
                    throw new IllegalArgumentException(command);
            }
        }
    }
}
