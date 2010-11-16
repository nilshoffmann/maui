/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.gcgcms;

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
import net.sf.maltcms.chromaui.db.spi.DB4oCrudProvider;
import net.sf.maltcms.chromaui.project.spi.TreatmentGroupDescriptor;
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
public class GCGCMSProject {

//    InstanceContent ic = new InstanceContent();
//    Lookup lookup = new AbstractLookup(ic);
//    DB4oCrudProvider crud = new DB4oCrudProvider(null);
//    private final FileObject projectFile;
//    private final ProjectState state;
//
//    public GCGCMSProject(FileObject projectFile, ProjectState state) {
//        this.projectFile = projectFile;
//        this.state = state;
//    }
//
//    @Override
//    public Collection<IChromatogram2D> getInputFiles() {
//        return crud.retrieve(IChromatogramDescriptor.class);
//    }
//
//    @Override
//    public void setInputFiles(IChromatogram2D... f) {
//        crud.create(createDescriptorsFor(f));
//    }
//
//    private Collection<IChromatogramDescriptor> createDescriptorsFor(IChromatogram2D... f) {
//        List<IChromatogramDescriptor> desc = new LinkedList<IChromatogramDescriptor>();
//        for (IChromatogram2D icr : f) {
//            URI uri = new File(icr.getParent().getAbsolutePath()).toURI();
//            GCGCMSChromatogramDescriptor gcd = new GCGCMSChromatogramDescriptor();
//            gcd.setResourceLocation(uri);
//            gcd.setType(icr.getClass());
//            desc.add(gcd);
//        }
//        return desc;
//    }
//
//    private Collection<IChromatogram2D> getChromatogramsFor(IChromatogramDescriptor... f) {
//        List<IChromatogram2D> desc = new LinkedList<IChromatogram2D>();
//        for (IChromatogramDescriptor icr : f) {
//            desc.add(icr.getChromatogram());
//        }
//        return desc;
//    }
//
//    @Override
//    public void addInputFiles(IChromatogram2D... f) {
//        crud.create(Arrays.asList(f));
//    }
//
//    @Override
//    public void removeInputFiles(IChromatogram2D... f) {
//        crud.delete(Arrays.asList(f));
//    }
//
//    @Override
//    public Collection<ITreatmentGroup<IChromatogram2D>> getTreatmentGroups() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setTreatmentGroups(ITreatmentGroup<IChromatogram2D>... itg) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void addTreatmentGroups(ITreatmentGroup<IChromatogram2D>... itg) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void removeTreatmentGroups(IChromatogram2D... itg) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public FileObject getProjectDirectory() {
//        return this.projectFile.getParent();
//    }
//
//    @Override
//    public Lookup getLookup() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Collection<GCGCMSChromatogramDescriptor> getInputFiles() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setInputFiles(GCGCMSChromatogramDescriptor... f) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void addInputFiles(GCGCMSChromatogramDescriptor... f) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void removeInputFiles(GCGCMSChromatogramDescriptor... f) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Collection<TreatmentGroupDescriptor> getTreatmentGroups() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setTreatmentGroups(TreatmentGroupDescriptor... itg) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void addTreatmentGroups(TreatmentGroupDescriptor... itg) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void removeTreatmentGroups(TreatmentGroupDescriptor... itg) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}
