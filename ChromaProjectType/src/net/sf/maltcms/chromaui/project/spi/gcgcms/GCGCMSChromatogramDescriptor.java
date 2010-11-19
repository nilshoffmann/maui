/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi.gcgcms;

import cross.datastructures.fragments.FileFragment;
import java.io.File;
import java.net.URI;
import maltcms.datastructures.ms.ChromatogramFactory;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;

/**
 *
 * @author hoffmann
 */
public class GCGCMSChromatogramDescriptor implements IChromatogramDescriptor{

    private URI resourceLocation;

    @Override
    public URI getResourceLocation() {
        return this.resourceLocation;
    }

    @Override
    public void setResourceLocation(URI u) {
        this.resourceLocation = u;
    }

    @Override
    public IChromatogram getChromatogram() {
        ChromatogramFactory cf = new ChromatogramFactory();
        return cf.createChromatogram2D(new FileFragment(new File(getResourceLocation())));
    }

    @Override
    public String toString() {
        return "GCGCMSChromatogramDescriptor{" + "resourceLocation=" + resourceLocation +'}';
    }

}
