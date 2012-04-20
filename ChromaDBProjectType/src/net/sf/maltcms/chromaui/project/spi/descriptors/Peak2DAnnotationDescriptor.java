/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;

/**
 *
 * @author nilshoffmann
 */
public class Peak2DAnnotationDescriptor extends PeakAnnotationDescriptor implements IPeak2DAnnotationDescriptor {

    private double firstColumnRt = Double.NaN;
    public static final String PROP_FIRSTCOLUMNRT = "firstColumnRt";

    @Override
    public double getFirstColumnRt() {
        activate(ActivationPurpose.READ);
        return firstColumnRt;
    }

    @Override
    public void setFirstColumnRt(double firstColumnRt) {
        activate(ActivationPurpose.WRITE);
        double old = this.firstColumnRt;
        this.firstColumnRt = firstColumnRt;
        firePropertyChange(PROP_FIRSTCOLUMNRT, old, this.firstColumnRt);
    }
    
    private double secondColumnRt = Double.NaN;
    public static final String PROP_SECONDCOLUMNRT = "secondColumnRt";

    @Override
    public double getSecondColumnRt() {
        activate(ActivationPurpose.READ);
        return secondColumnRt;
    }

    @Override
    public void setSecondColumnRt(double secondColumnRt) {
        activate(ActivationPurpose.WRITE);
        double old = this.secondColumnRt;
        this.secondColumnRt = secondColumnRt;
        firePropertyChange(PROP_SECONDCOLUMNRT, old, this.secondColumnRt);
    }
}
