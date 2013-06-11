/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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

import com.db4o.ext.DatabaseFileLockedException;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistryFactory;
import net.sf.maltcms.chromaui.project.spi.ChromAUIProjectLogicalView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProviderFactory;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.db.api.NoAuthCredentials;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.IMauiSubprojectProviderFactory;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.ProjectSettings;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.ProjectState;
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
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * Implementation of a ChromAUI project type. Abstracts from the concrete db
 * schema used to represent the logical structure of a project on disk. Should
 * be used as a starting point for other project type
 * implementations/extensions. See @link{http://platform.netbeans.org/tutorials/nbm-projectextension.html}
 * for details on writing a project type extension.
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
		lkp = new AbstractLookup(
				ic);
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
		for (IContainer container : ic) {
			System.out.println("Adding container: " + ic.toString());
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
		boolean deleteAll = true;
		if (descriptor.length > 1) {
			Object result = dd.notify(new NotifyDescriptor.Confirmation(
					"Delete " + descriptor.length + " descriptor" + (descriptor.length > 1 ? "s" : "") + "?",
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
					Object result = dd.notify(new NotifyDescriptor.Confirmation(
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
//        getLookup().lookup(Info.class).firePropertyChange(new PropertyChangeEvent(
//                this, "REFRESH_NODES", null, this));
	}

	@Override
	public <T extends IContainer> Collection<T> getContainer(Class<T> c) {
		if (icp != null) {
			ICrudSession ics = icp.createSession();
			Collection<T> l = ics.retrieve(c);
			ics.close();
			for (IContainer container : l) {
				if (container.getProject() == null) {
					container.setProject(this);
				}
			}
			return l;
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
		ArrayList<IChromatogramDescriptor> al = new ArrayList<IChromatogramDescriptor>();
		for (TreatmentGroupContainer cc : getContainer(
				TreatmentGroupContainer.class)) {
			al.addAll(cc.getMembers());
		}
		return al;
	}

	@Override
	public Collection<ITreatmentGroupDescriptor> getTreatmentGroups() {
		HashSet<ITreatmentGroupDescriptor> tgs = new LinkedHashSet<ITreatmentGroupDescriptor>();
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
		HashSet<ISampleGroupDescriptor> tgs = new LinkedHashSet<ISampleGroupDescriptor>();
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
		HashSet<SampleGroupContainer> tgs = new LinkedHashSet<SampleGroupContainer>();
		for (SampleGroupContainer cc : getContainer(
				SampleGroupContainer.class)) {
			for (IChromatogramDescriptor icd : cc.getMembers()) {
				if (icd.getTreatmentGroup().getName().equals(treatmentGroup.getName())) {
					tgs.add(cc);
				}
			}
		}
		System.out.println("Sample groups: " + tgs);
		return tgs;
	}

	@Override
	public Collection<IDatabaseDescriptor> getDatabases() {
		HashSet<IDatabaseDescriptor> tgs = new LinkedHashSet<IDatabaseDescriptor>();
		for (DatabaseContainer cc : getContainer(
				DatabaseContainer.class)) {
			for (IDatabaseDescriptor icd : cc.getMembers()) {
				tgs.add(icd);
			}
		}
		return tgs;
	}

	@Override
	public FileObject getOutputDir() {
		try {
			return getSetting("output.basedir", FileObject.class);
		}catch(NullPointerException npe) {
			
		}catch(DatabaseFileLockedException dfle) {
			
		}
		try {
			return FileUtil.createFolder(parentFile, "output");
		} catch (IOException ex) {
			File outdir = new File(FileUtil.toFile(parentFile), "output");
			outdir.mkdirs();
			return FileUtil.toFileObject(outdir);
		}
	}

	@Override
	public void setOutputDir(FileObject f) {
		ProjectSettings ps = getSettings();
		ps.put("output.basedir", f);
		ics.update(ps);
	}

	@Override
	public File getOutputLocation(Object importer) {
		File outputDir = FileUtil.toFile(getOutputDir());
		String name = "";
		if(importer instanceof Class) {
			name = ((Class)importer).getSimpleName();
		}else{
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

	protected synchronized <T> T getSetting(String key, Class<T> c) throws NullPointerException, ClassCastException {
		ProjectSettings ps = getSettings();
		if (ps.containsKey(key)) {
			return c.cast(ps.get(key));
		}
		throw new NullPointerException("No element for key: " + key);
	}

	protected synchronized ProjectSettings getSettings() {
		if (ics == null) {
			openSession();
		}
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
	public void propertyChange(PropertyChangeEvent pce) {
		pcs.firePropertyChange(pce);
	}

	@Override
	public void addToLookup(Object... obj) {
		for (Object object : obj) {
			if (object != null) {
				this.ic.add(object);
			}
		}
	}

	@Override
	public Collection<Peak1DContainer> getPeaks(
			IChromatogramDescriptor descriptor) {
		Collection<Peak1DContainer> containers = getContainer(
				Peak1DContainer.class);
		List<Peak1DContainer> peaks = new ArrayList<Peak1DContainer>();

		for (Peak1DContainer container : containers) {
			if (container != null) {
				IChromatogramDescriptor chrom = container.getChromatogram();
				if (chrom != null) {
					String chromName = chrom.getDisplayName();
					String descrName = descriptor.getDisplayName();
					if (chromName.equals(descrName)) {
						peaks.add(container);
					}
				}
			}
		}

		return peaks;
	}

	@Override
	public File getImportLocation(Object importer) {
		File importDir = new File(FileUtil.toFile(getProjectDirectory()),
				"import");
		importDir = new File(importDir, importer.getClass().getSimpleName());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM-dd-yyyy_HH-mm-ss", Locale.US);
		importDir = new File(importDir, dateFormat.format(
				new Date()));
		importDir.mkdirs();
		return importDir;
	}

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
			ArrayList<FileObject> list = new ArrayList<FileObject>(Collections.list(e));
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
			ArrayList<FileObject> list = new ArrayList<FileObject>(Collections.list(e));
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
			if ((command.equals(ActionProvider.COMMAND_DELETE))) {
				return true;
			} else if ((command.equals(ActionProvider.COMMAND_COPY))) {
				return true;
			} else {
				throw new IllegalArgumentException(command);
			}
		}
	}
}
