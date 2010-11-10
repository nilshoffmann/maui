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
public class GCMSChromatogramContainer extends AContainer implements IChromatogramContainer<GCMSChromatogramDescriptor>{

    public GCMSChromatogramContainer(ICrudProvider db) {
        super(db);
    }

    @Override
    public Collection<GCMSChromatogramDescriptor> getInputFiles() {
        return getCrudProvider().retrieve(GCMSChromatogramDescriptor.class);
    }

    @Override
    public void setInputFiles(GCMSChromatogramDescriptor... f) {
        getCrudProvider().update(Arrays.asList(f));
    }

    @Override
    public void addInputFiles(GCMSChromatogramDescriptor... f) {
        getCrudProvider().create(Arrays.asList(f));
    }

    @Override
    public void removeInputFiles(GCMSChromatogramDescriptor... f) {
        getCrudProvider().delete(Arrays.asList(f));
    }
}
