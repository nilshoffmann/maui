/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.ITreatmentGroup;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author hoffmann
 */
public class GCGCMSProject implements IChromAUIProject<GCGCMSChromatogramDescriptor,TreatmentGroupDescriptor>, Project {

    InstanceContent ic = new InstanceContent();
    Lookup lookup = new AbstractLookup(ic);
    DB4oCrudProvider crud = new DB4oCrudProvider(null);
    private final FileObject projectFile;
    private final ProjectState state;

    public GCGCMSProject(FileObject projectFile, ProjectState state) {
        this.projectFile = projectFile;
        this.state = state;
    }

    @Override
    public Collection<IChromatogram2D> getInputFiles() {
        return crud.retrieve(IChromatogram2D.class);
    }

    @Override
    public void setInputFiles(IChromatogram2D... f) {
        crud.create(Arrays.asList(f));
    }

    private Collection<IChromatogramDescriptor> createDescriptorsFor(IChromatogram2D... f) {
        List<IChromatogramDescriptor> desc = new LinkedList<IChromatogramDescriptor>();
        for (IChromatogram2D icr : f) {
            URI uri = new File(icr.getParent().getAbsolutePath()).toURI();
            GCGCMSChromatogramDescriptor gcd = new GCGCMSChromatogramDescriptor();
            gcd.setResourceLocation(uri);
            gcd.setType(icr.getClass());
            desc.add(gcd);
        }
        return desc;
    }

    @Override
    public void addInputFiles(IChromatogram2D... f) {
        crud.create(Arrays.asList(f));
    }

    @Override
    public void removeInputFiles(IChromatogram2D... f) {
        crud.delete(Arrays.asList(f));
    }

    @Override
    public Collection<ITreatmentGroup<IChromatogram2D>> getTreatmentGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTreatmentGroups(ITreatmentGroup<IChromatogram2D>... itg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addTreatmentGroups(ITreatmentGroup<IChromatogram2D>... itg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeTreatmentGroups(IChromatogram2D... itg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileObject getProjectDirectory() {
        return this.projectFile.getParent();
    }

    @Override
    public Lookup getLookup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
