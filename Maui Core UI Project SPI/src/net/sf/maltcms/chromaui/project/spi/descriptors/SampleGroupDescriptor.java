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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import com.db4o.activation.ActivationPurpose;
import java.awt.Color;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;

/**
 *
 * @author Nils Hoffmann
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
