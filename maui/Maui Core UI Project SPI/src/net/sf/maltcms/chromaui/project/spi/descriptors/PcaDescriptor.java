/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableArrayList;
import com.db4o.collections.ActivatableHashMap;
import com.db4o.collections.ActivatableList;
import com.db4o.collections.ActivatableMap;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayDouble.D1;
import ucar.ma2.ArrayDouble.D2;

/**
 *
 * @author Nils Hoffmann
 */
public class PcaDescriptor extends ADescriptor implements IPcaDescriptor {

    @Override
    public int compareTo(IBasicDescriptor t) {
        return getDisplayName().compareTo(t.getDisplayName());
    }
    private ActivatableMap<ITreatmentGroupDescriptor, Color> groupColors = new ActivatableHashMap<>();

    @Override
    public Map<ITreatmentGroupDescriptor, Color> getGroupColors() {
        activate(ActivationPurpose.READ);
        return groupColors;
    }

    @Override
    public void setGroupColors(Map<ITreatmentGroupDescriptor, Color> map) {
        activate(ActivationPurpose.WRITE);
        Map<ITreatmentGroupDescriptor, Color> old = groupColors;
        this.groupColors = new ActivatableHashMap<>(map);
        firePropertyChange(PROP_GROUPCOLORS, old, groupColors);
    }
    private ArrayDouble.D2 rotation;

    @Override
    public void setRotation(D2 rotation) {
        activate(ActivationPurpose.WRITE);
        ArrayDouble.D2 old = this.rotation;
        this.rotation = rotation;
        firePropertyChange(PROP_ROTATION, old, this.rotation);
    }

    @Override
    public D2 getRotation() {
        activate(ActivationPurpose.READ);
        return rotation;
    }
    private ArrayDouble.D1 sdev;

    @Override
    public void setSdev(D1 sdev) {
        activate(ActivationPurpose.WRITE);
        ArrayDouble.D1 old = this.sdev;
        this.sdev = sdev;
        firePropertyChange(PROP_SDEV, old, this.sdev);
    }

    @Override
    public D1 getSdev() {
        activate(ActivationPurpose.READ);
        return this.sdev;
    }
    private ArrayDouble.D1 center;

    @Override
    public void setCenter(D1 center) {
        activate(ActivationPurpose.WRITE);
        ArrayDouble.D1 old = this.center;
        this.center = center;
        firePropertyChange(PROP_CENTER, old, this.center);
    }

    @Override
    public D1 getCenter() {
        activate(ActivationPurpose.READ);
        return this.center;
    }
    private boolean scale = false;

    @Override
    public boolean isScale() {
        activate(ActivationPurpose.READ);
        return this.scale;
    }

    @Override
    public void setScale(boolean scale) {
        activate(ActivationPurpose.WRITE);
        boolean old = this.scale;
        this.scale = scale;
        firePropertyChange(PROP_SCALE, old, this.scale);
    }
    private ArrayDouble.D2 x;

    @Override
    public void setX(D2 x) {
        activate(ActivationPurpose.WRITE);
        ArrayDouble.D2 old = this.x;
        this.x = x;
        firePropertyChange(PROP_X, old, this.x);
    }

    @Override
    public D2 getX() {
        activate(ActivationPurpose.READ);
        return this.x;
    }
    private PeakGroupContainer peakGroupContainer;

    @Override
    public PeakGroupContainer getPeakGroupContainer() {
        activate(ActivationPurpose.READ);
        return this.peakGroupContainer;
    }

    @Override
    public void setPeakGroupContainer(PeakGroupContainer peakGroupContainer) {
        activate(ActivationPurpose.WRITE);
        peakGroupContainer.setProject(getProject());
        PeakGroupContainer old = this.peakGroupContainer;
        this.peakGroupContainer = peakGroupContainer;
        firePropertyChange(PROP_PEAKGROUPCONTAINER, old, this.peakGroupContainer);
    }
    private ActivatableList<IChromatogramDescriptor> cases = new ActivatableArrayList<>(0);

    @Override
    public void setCases(List<IChromatogramDescriptor> chromatograms) {
        activate(ActivationPurpose.WRITE);
        for (IChromatogramDescriptor descr : chromatograms) {
            descr.setProject(getProject());
        }
        List<IChromatogramDescriptor> old = this.cases;
        this.cases = new ActivatableArrayList<>(chromatograms);
        firePropertyChange(PROP_CASES, old, this.cases);
    }

    @Override
    public List<IChromatogramDescriptor> getCases() {
        activate(ActivationPurpose.READ);
        return cases;
    }
    private ActivatableList<IPeakGroupDescriptor> variables = new ActivatableArrayList<>(0);

    @Override
    public void setVariables(List<IPeakGroupDescriptor> peakGroup) {
        activate(ActivationPurpose.WRITE);
        for (IPeakGroupDescriptor peakGroups : peakGroup) {
            peakGroups.setProject(getProject());
        }
        List<IPeakGroupDescriptor> old = this.variables;
        this.variables = new ActivatableArrayList<>(peakGroup);
        firePropertyChange(PROP_VARIABLES, old, this.variables);
    }

    @Override
    public List<IPeakGroupDescriptor> getVariables() {
        activate(ActivationPurpose.READ);
        return this.variables;
    }
}
