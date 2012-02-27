/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import java.awt.Color;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public class SampleGroupDescriptor extends ADescriptor implements ISampleGroupDescriptor {
    
    private String comment = "<NA>";
    public static final String PROP_COMMENT = "comment";

    /**
     * Get the value of comment
     *
     * @return the value of comment
     */
    @Override
    public String getComment() {
        activate(ActivationPurpose.READ);
        return comment;
    }

    /**
     * Set the value of comment
     *
     * @param comment new value of comment
     */
    @Override
    public void setComment(String comment) {
        activate(ActivationPurpose.WRITE);
        String oldComment = this.comment;
        this.comment = comment;
        firePropertyChange(PROP_COMMENT, oldComment, comment);
    }
    
    private Color color = Color.RED;
    public static final String PROP_COLOR = "color";

    /**
     * Get the value of color
     *
     * @return the value of color
     */
    @Override
    public Color getColor() {
        activate(ActivationPurpose.READ);
        return color;
    }

    /**
     * Set the value of color
     *
     * @param comment new value of color
     */
    @Override
    public void setColor(Color color) {
        activate(ActivationPurpose.WRITE);
        Color oldColor = this.color;
        this.color = color;
        firePropertyChange(PROP_COLOR, oldColor, color);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(IBasicDescriptor t) {
        return getDisplayName().compareTo(t.getDisplayName());
    }
}
