/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;

/**
 *
 * @author nilshoffmann
 */
public interface IMauiProject extends PropertyChangeListener {

    void addContainer(IContainer... ic);

    void addToLookup(Object... obj);

    Collection<IChromatogramDescriptor> getChromatograms();
    
//    <T> Collection<T> query(Class<T> c, IMatchPredicate<T> mp);
//    
//    <T> Collection<T> query(Class<T> c, IMatchPredicate<T> mp, Comparator<T> comp);

    <T extends IContainer> Collection<T> getContainer(Class<T> c);

    Collection<IDatabaseDescriptor> getDatabases();

    Collection<Peak1DContainer> getPeaks(IChromatogramDescriptor descriptor);

    Collection<ITreatmentGroupDescriptor> getTreatmentGroups();

    void removeContainer(IContainer... ic);

    void updateContainer(IContainer... ic);
    
    void removeDescriptor(IBasicDescriptor... descriptor);
    
    File getImportLocation(Object importer);
    
}
