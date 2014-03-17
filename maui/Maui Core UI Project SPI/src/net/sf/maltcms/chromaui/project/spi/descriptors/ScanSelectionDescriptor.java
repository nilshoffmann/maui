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
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IScanSelectionDescriptor;

/**
 *
 * @author Nils Hoffmann
 */
public class ScanSelectionDescriptor extends ADescriptor implements IScanSelectionDescriptor {

    private IScan scan;

    @Override
    public IScan getScan() {
        activate(ActivationPurpose.READ);
        return scan;
    }

    @Override
    public void setScan(IScan scan) {
        activate(ActivationPurpose.WRITE);
        IScan oldScan = this.scan;
        this.scan = scan;
        firePropertyChange(PROP_SCAN, oldScan, this.scan);
    }

    private IChromatogramDescriptor chromatogramDescriptor;

    @Override
    public IChromatogramDescriptor getChromatogramDescriptor() {
        activate(ActivationPurpose.READ);
        return chromatogramDescriptor;
    }

    @Override
    public void setChromatogramDescriptor(IChromatogramDescriptor chromatogramDescriptor) {
        activate(ActivationPurpose.WRITE);
        chromatogramDescriptor.setProject(getProject());
        IChromatogramDescriptor old = this.chromatogramDescriptor;
        this.chromatogramDescriptor = chromatogramDescriptor;
        firePropertyChange(PROP_CHROM_DESCRIPTOR, old, this.chromatogramDescriptor);
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
