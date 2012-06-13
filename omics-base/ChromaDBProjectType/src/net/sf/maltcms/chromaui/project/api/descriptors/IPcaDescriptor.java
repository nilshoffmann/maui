/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import ucar.ma2.ArrayDouble;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IPcaDescriptor extends IStatisticsDescriptor {
    String PROP_GROUPCOLORS = "groupColors";
    
    Map<ITreatmentGroupDescriptor,Color> getGroupColors();
    
    void setGroupColors(Map<ITreatmentGroupDescriptor,Color> map);
    
    String PROP_ROTATION = "rotation";
    void setRotation(ArrayDouble.D2 rotation);
    ArrayDouble.D2 getRotation();
    
    String PROP_SDEV = "sdev";
    /** 
     * Sdev of principal components
     * @param sdev
     */
    void setSdev(ArrayDouble.D1 sdev);
    ArrayDouble.D1 getSdev();
    
    String PROP_CENTER = "center";
    /**
     * Center of categories
     * @param center 
     */
    void setCenter(ArrayDouble.D1 center);
    ArrayDouble.D1 getCenter();
    
    String PROP_SCALE = "scale";
    boolean isScale();
    void setScale(boolean scale);
    
    String PROP_X = "x";
    void setX(ArrayDouble.D2 x);
    ArrayDouble.D2 getX();
    
    String PROP_PEAKGROUPCONTAINER = "peakGroupContainer";
    void setPeakGroupContainer(PeakGroupContainer peakGroupContainer);
    PeakGroupContainer getPeakGroupContainer();
    
    String PROP_CASES = "rows";
    void setCases(List<IChromatogramDescriptor> chromatograms);
    List<IChromatogramDescriptor> getCases();
    
    String PROP_VARIABLES = "variables";
    void setVariables(List<IPeakGroupDescriptor> peakGroup);
    List<IPeakGroupDescriptor> getVariables();
    
    
}
