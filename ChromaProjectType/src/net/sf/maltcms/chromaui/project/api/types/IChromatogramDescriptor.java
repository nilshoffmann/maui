/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import java.net.URI;
import maltcms.datastructures.ms.IChromatogram;

/**
 *
 * @author hoffmann
 */
public interface IChromatogramDescriptor {

    URI getResourceLocation();

    void setResourceLocation(URI u);

    IChromatogram getChromatogram();

}
