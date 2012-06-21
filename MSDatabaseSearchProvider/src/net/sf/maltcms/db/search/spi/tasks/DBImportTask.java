/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.db.search.spi.tasks;

import cross.tools.StringTools;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.db.api.CrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.DatabaseContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.types.DatabaseType;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.chromaui.ui.support.api.FileTools;
import net.sf.maltcms.db.search.spi.parser.MSPFormatMetaboliteParser2;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class DBImportTask extends AProgressAwareRunnable {

    private File[] selectedDatabases;
    private String databaseContainerName;
    private DatabaseType databaseType;
    private Locale locale;
    private List<Double> maskedMasses;
    private IChromAUIProject project;
    private boolean cancelled = false;

    private boolean overwriteFile(File fo) {
        if (fo.exists()) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
                    "Can not import database! File with the same name already exists at: " + fo.getPath(),
                    NotifyDescriptor.WARNING_MESSAGE));
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        getProgressHandle().setDisplayName(getClass().
                getName());
        getProgressHandle().start(selectedDatabases.length);
        DatabaseContainer dbContainer = new DatabaseContainer();
        dbContainer.setName(getDatabaseContainerName());
        dbContainer.setDisplayName(getDatabaseContainerName());
        try {
            FileObject baseDir = FileUtil.createFolder(project.getProjectDirectory(),
                    "databases");
            int unit = 0;
            List<FileObject> createdFiles = new LinkedList<FileObject>();
            for (File file : selectedDatabases) {
                if (cancelled) {
                    return;
                }
                getProgressHandle().progress("Importing database " + file.getName(), unit);
                String name = StringTools.removeFileExt(file.getName());

                if (file.getName().toLowerCase().endsWith("msp")) {
                    File dbFile1 = new File(FileUtil.toFile(baseDir),
                            name + ".db4o");
                    if (overwriteFile(dbFile1)) {
                        FileObject dbFile = FileUtil.createData(dbFile1);
                        createdFiles.add(dbFile);
                        getProgressHandle().progress("Parsing msp", unit);
//                        EmbeddedConfiguration ec = com.db4o.Db4oEmbedded.
//                                newConfiguration();
//                        ec.common().reflectWith(new JdkReflector(
//                                Lookup.getDefault().lookup(ClassLoader.class)));
//                        EmbeddedObjectContainer eoc = Db4oEmbedded.openFile(ec,
//                                FileUtil.toFile(dbFile).getAbsolutePath());
                        MSPFormatMetaboliteParser2 mfmp = new MSPFormatMetaboliteParser2();
                        mfmp.setLocale(locale);
                        Collection<IMetabolite> metabolites = mfmp.parse(file);
                        if (metabolites.isEmpty()) {
                            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
                                    "Database import failed, please check output for details!",
                                    NotifyDescriptor.WARNING_MESSAGE));
//                            eoc.close();
                            cancelled = true;
                        } else {
                            ICrudProvider provider = CrudProvider.getProviderFor(
                                    FileUtil.toFile(dbFile).toURI().toURL());
                            try {
                                provider.open();
                                ICrudSession session = provider.createSession();
                                System.out.println("Adding metabolites to database!");
                                for (IMetabolite im : metabolites) {
                                    session.create(im);
                                }
                                
                                IDatabaseDescriptor descriptor = DescriptorFactory.newDatabaseDescriptor(
                                        FileUtil.toFile(dbFile).getAbsolutePath(),
                                        databaseType);
                                descriptor.setMaskedMasses(maskedMasses);
                                dbContainer.addMembers(descriptor);
                                project.addContainer(dbContainer);
                            } catch (Exception e) {
                                Exceptions.printStackTrace(e);
                            } finally {
                                provider.close();
                            }
                        }
                    }
                } else {

                    File dbFile = new File(FileUtil.toFile(baseDir),
                            name + ".db4o");
                    if (overwriteFile(dbFile)) {
                        getProgressHandle().progress("Copying db4o file");
                        createdFiles.add(FileUtil.createData(dbFile));
                        FileTools.copyFile(file, dbFile);
                        IDatabaseDescriptor descriptor = DescriptorFactory.newDatabaseDescriptor(
                                dbFile.getAbsolutePath(),
                                databaseType);
                        descriptor.setMaskedMasses(maskedMasses);
                        dbContainer.addMembers(descriptor);
                        project.addContainer(dbContainer);
                    }

                }
                unit++;
            }
            if (cancelled) {
                for (FileObject file : createdFiles) {
                    file.delete();
                }
                return;
            }
            getProgressHandle().finish();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public boolean cancel() {
        this.cancelled = true;
        return this.cancelled;
    }
}
