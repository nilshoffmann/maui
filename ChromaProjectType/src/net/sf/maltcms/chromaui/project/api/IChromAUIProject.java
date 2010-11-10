/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public interface IChromAUIProject<T extends IChromatogramDescriptor,U extends ITreatmentGroupDescriptor> extends IChromatogramContainer<T>, ITreatmentGroupContainer<U> {

}
