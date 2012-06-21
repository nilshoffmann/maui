/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.container;

import com.db4o.activation.ActivationPurpose;
import com.db4o.config.annotations.Indexed;
import java.awt.Color;
import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import org.openide.util.ImageUtilities;

/**
 *
 * @author nilshoffmann
 */
public class Peak1DContainer extends ADatabaseBackedContainer<IPeakAnnotationDescriptor>{

    public final String PROP_CHROMATOGRAM = "chromatogram";
    public final String PROP_COLOR = "color";
    
    @Indexed
    private IChromatogramDescriptor chromatogram;

    public IChromatogramDescriptor getChromatogram() {
        activate(ActivationPurpose.READ);
        return chromatogram;
    }

    public void setChromatogram(IChromatogramDescriptor chromatogram) {
        activate(ActivationPurpose.WRITE);
        IChromatogramDescriptor oldDescr = this.chromatogram;
        this.chromatogram = chromatogram;
        firePropertyChange(PROP_CHROMATOGRAM,oldDescr,this.chromatogram);
    }
    
    private Color color = new Color(255,255,255,0);
    
    public Color getColor() {
        activate(ActivationPurpose.READ);
        return color;
    }
    
    public void setColor(Color color) {
        activate(ActivationPurpose.WRITE);
        Color old = this.color;
        this.color = color;
        firePropertyChange(PROP_COLOR, old, this.color);
    }
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/Peaks.png");
    }
    
}
