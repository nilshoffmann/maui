/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi;

import java.util.Arrays;
import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.AContainer;
import net.sf.maltcms.chromaui.project.api.IChromatogramContainer;
import net.sf.maltcms.chromaui.project.api.ICrudProvider;

/**
 *
 * @author hoffmann
 */
public class GCGCMSChromatogramContainer extends AContainer implements IChromatogramContainer<GCGCMSChromatogramDescriptor>{

    public GCGCMSChromatogramContainer(ICrudProvider db) {
        super(db);
    }

    @Override
    public Collection<GCGCMSChromatogramDescriptor> getInputFiles() {
        return getCrudProvider().retrieve(GCGCMSChromatogramDescriptor.class);
    }

    @Override
    public void setInputFiles(GCGCMSChromatogramDescriptor... f) {
        getCrudProvider().update(Arrays.asList(f));
    }

    @Override
    public void addInputFiles(GCGCMSChromatogramDescriptor... f) {
        getCrudProvider().create(Arrays.asList(f));
    }

    @Override
    public void removeInputFiles(GCGCMSChromatogramDescriptor... f) {
        getCrudProvider().delete(Arrays.asList(f));
    }
}
