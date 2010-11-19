/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;

/**
 *
 * @author hoffmann
 */
public interface IChromatogramContainer<T extends IChromatogramDescriptor> extends IContainer {

    Collection<T> getInputFiles();

    void setInputFiles(T... f);

    void addInputFiles(T... f);

    void removeInputFiles(T... f);
}
