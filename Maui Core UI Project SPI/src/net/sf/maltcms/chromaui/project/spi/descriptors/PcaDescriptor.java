/*
 * $license$
 *
 * $Id$
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
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class PcaDescriptor extends ADescriptor implements IPcaDescriptor {

    @Override
    public int compareTo(IBasicDescriptor t) {
        return getDisplayName().compareTo(t.getDisplayName());
    }
    private ActivatableMap<ITreatmentGroupDescriptor, Color> groupColors = new ActivatableHashMap<ITreatmentGroupDescriptor, Color>();

    @Override
    public Map<ITreatmentGroupDescriptor, Color> getGroupColors() {
        activate(ActivationPurpose.READ);
        return groupColors;
    }

    @Override
    public void setGroupColors(Map<ITreatmentGroupDescriptor, Color> map) {
        activate(ActivationPurpose.WRITE);
        Map<ITreatmentGroupDescriptor, Color> old = groupColors;
        this.groupColors = new ActivatableHashMap<ITreatmentGroupDescriptor, Color>(map);
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
        PeakGroupContainer old = this.peakGroupContainer;
        this.peakGroupContainer = peakGroupContainer;
        firePropertyChange(PROP_PEAKGROUPCONTAINER, old, this.peakGroupContainer);
    }
    private ActivatableList<IChromatogramDescriptor> cases = new ActivatableArrayList<IChromatogramDescriptor>(0);

    @Override
    public void setCases(List<IChromatogramDescriptor> chromatograms) {
        activate(ActivationPurpose.WRITE);
        List<IChromatogramDescriptor> old = this.cases;
        this.cases = new ActivatableArrayList<IChromatogramDescriptor>(chromatograms);
        firePropertyChange(PROP_CASES, old, this.cases);
    }

    @Override
    public List<IChromatogramDescriptor> getCases() {
        activate(ActivationPurpose.READ);
        return cases;
    }
    private ActivatableList<IPeakGroupDescriptor> variables = new ActivatableArrayList<IPeakGroupDescriptor>(0);

    @Override
    public void setVariables(List<IPeakGroupDescriptor> peakGroup) {
        activate(ActivationPurpose.WRITE);
        List<IPeakGroupDescriptor> old = this.variables;
        this.variables = new ActivatableArrayList<IPeakGroupDescriptor>(peakGroup);
        firePropertyChange(PROP_VARIABLES, old, this.variables);
    }

    @Override
    public List<IPeakGroupDescriptor> getVariables() {
        activate(ActivationPurpose.READ);
        return this.variables;
    }
}
