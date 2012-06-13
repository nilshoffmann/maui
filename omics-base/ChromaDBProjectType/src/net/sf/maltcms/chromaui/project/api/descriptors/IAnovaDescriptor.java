/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IAnovaDescriptor extends IStatisticsDescriptor, IHypothesisTestDescriptor {
    String PROP_FVALUES = "fvalues";

    /**
     * Get the value of fValues
     *
     * @return the value of fValues
     */
    double[] getFvalues();

    /**
     * Set the value of fValue
     *
     * @param fValue new value of fValue
     */
    void setFvalues(double[] fValues);
    
    String PROP_PEAKGROUPDESCRIPTOR = "peakGroupDescriptor";

    /**
     * Get the value of peakGroupDescriptor
     *
     * @return the value of peakGroupDescriptor
     */
    IPeakGroupDescriptor getPeakGroupDescriptor();

    /**
     * Set the value of peakGroupDescriptor
     *
     * @param peakGroupDescriptor new value of peakGroupDescriptor
     */
    void setPeakGroupDescriptor(IPeakGroupDescriptor peakGroupDescriptor);
    
}
