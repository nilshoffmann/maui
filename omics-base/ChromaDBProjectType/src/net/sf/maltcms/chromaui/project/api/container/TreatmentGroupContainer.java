/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.container;

import com.db4o.activation.ActivationPurpose;
import java.awt.Color;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import org.openide.util.ImageUtilities;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupContainer extends ADatabaseBackedContainer<IChromatogramDescriptor>
        implements ITreatmentGroupDescriptor {
    
    private ITreatmentGroupDescriptor treatmentGroup;

    public static String PROP_TREATMENTGROUP = "treatmentGroup";
    
    public ITreatmentGroupDescriptor getTreatmentGroup() {
        activate(ActivationPurpose.READ);
        return this.treatmentGroup;
    }

    public void setTreatmentGroup(ITreatmentGroupDescriptor treatmentGroup) {
        activate(ActivationPurpose.WRITE);
        ITreatmentGroupDescriptor old = this.treatmentGroup;
        this.treatmentGroup = treatmentGroup;
        firePropertyChange(PROP_TREATMENTGROUP,
                old, this.treatmentGroup);
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/Group.png");
    }

    @Override
    public String getComment() {
        return treatmentGroup.getComment();
    }

    @Override
    public void setComment(String comment) {
        treatmentGroup.setComment(comment);
    }

    @Override
    public Color getColor() {
        return treatmentGroup.getColor();
    }

    @Override
    public void setColor(Color color) {
        treatmentGroup.setColor(color);
    }
}
