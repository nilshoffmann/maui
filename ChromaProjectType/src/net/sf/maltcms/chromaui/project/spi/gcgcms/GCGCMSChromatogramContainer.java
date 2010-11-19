/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi.gcgcms;

import java.util.Arrays;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.ADatabaseBackedContainer;
import net.sf.maltcms.chromaui.project.api.IChromatogramContainer;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;

/**
 *
 * @author hoffmann
 */
public class GCGCMSChromatogramContainer extends ADatabaseBackedContainer implements IChromatogramContainer<GCGCMSChromatogramDescriptor>{

    public GCGCMSChromatogramContainer(ICrudProvider db) {
        super(db);
    }

    @Override
    public Collection<GCGCMSChromatogramDescriptor> getInputFiles() {
        ICrudSession icr = getCrudProvider().createSession();
        Collection<GCGCMSChromatogramDescriptor> c = icr.retrieve(GCGCMSChromatogramDescriptor.class);
        icr.close();
        return c;
    }

    @Override
    public void setInputFiles(GCGCMSChromatogramDescriptor... f) {
        ICrudSession icr = getCrudProvider().createSession();
        icr.create(Arrays.asList(f));
        icr.close();
    }

    @Override
    public void addInputFiles(GCGCMSChromatogramDescriptor... f) {
        ICrudSession icr = getCrudProvider().createSession();
        icr.create(Arrays.asList(f));
        icr.close();
    }

    @Override
    public void removeInputFiles(GCGCMSChromatogramDescriptor... f) {
        ICrudSession icr = getCrudProvider().createSession();
        icr.delete(Arrays.asList(f));
        icr.close();
    }

}
