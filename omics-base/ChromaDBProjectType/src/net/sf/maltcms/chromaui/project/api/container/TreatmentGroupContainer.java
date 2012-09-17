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
import java.awt.Color;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Nils Hoffmann
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
