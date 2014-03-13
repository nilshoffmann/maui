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
import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import org.openide.util.ImageUtilities;

/**
 * FIXME IStatisticsDescriptor needs a refactoring
 *
 * @author Nils Hoffmann
 */
public class StatisticsContainer extends ADatabaseBackedContainer<IStatisticsDescriptor> {

    private String method;
    public static final String PROP_METHOD = "method";

    /**
     * Get the value of method
     *
     * @return the value of method
     */
    public String getMethod() {
        activate(ActivationPurpose.READ);
        return method;
    }

    /**
     * Set the value of method
     *
     * @param method new value of method
     */
    public void setMethod(String method) {
        activate(ActivationPurpose.WRITE);
        String oldMethod = this.method;
        this.method = method;
        firePropertyChange(PROP_METHOD, oldMethod, method);
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/Statistics.png");
    }

}
