/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import ucar.ma2.ArrayDouble;

/**
 *
 * @author Nils Hoffmann
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
