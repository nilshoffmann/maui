/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi;

import cross.datastructures.fragments.FileFragment;
import java.io.File;
import java.net.URI;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram2D;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;

/**
 *
 * @author hoffmann
 */
public class GCGCMSChromatogramDescriptor<T extends IChromatogram2D> implements IChromatogramDescriptor<T>{

    private URI resourceLocation;

    private Class<T> type;

    @Override
    public URI getResourceLocation() {
        return this.resourceLocation;
    }

    @Override
    public void setResourceLocation(URI u) {
        this.resourceLocation = u;
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    @Override
    public void setType(Class<T> c) {
        this.type = c;
    }

    @Override
    public IChromatogram getChromatogram() {
        ChromatogramFactory cf = new ChromatogramFactory();
        return cf.createChromatogram2D(new FileFragment(new File(getResourceLocation())));
    }

}
