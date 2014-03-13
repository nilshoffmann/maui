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
 * @author Nils Hoffmann
 */
public class Peak1DContainer extends ADatabaseBackedContainer<IPeakAnnotationDescriptor> {

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
        firePropertyChange(PROP_CHROMATOGRAM, oldDescr, this.chromatogram);
    }

    private Color color = new Color(255, 255, 255, 0);

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
