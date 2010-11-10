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
public interface IChromatogramDescriptor<T extends IChromatogram> {

    URI getResourceLocation();

    void setResourceLocation(URI u);

    Class<T> getType();

    void setType(Class<T> c);

    IChromatogram getChromatogram();

}
