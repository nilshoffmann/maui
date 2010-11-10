/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.types;

import maltcms.datastructures.ms.IChromatogram;

/**
 *
 * @author hoffmann
 */
public interface IUserAnnotation<T,CHROM extends IChromatogram> {

    public void setUserAnnotationType(T t);

    public T getUserAnnotationType();

    public void setAssociation(CHROM...ic);

    public CHROM[] getAssociation();

}
