/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
import com.db4o.collections.ActivatableArrayList;
import java.awt.Image;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Nils Hoffmann
 */
public class PeakGroupContainer extends ADatabaseBackedContainer<IPeakGroupDescriptor> {

    private List<StatisticsContainer> statisticsContainers = new ActivatableArrayList<>();

    /**
     *
     */
    public final String PROP_STATISTICSCONTAINERS = "statisticsContainers";

    /**
     *
     * @return
     */
    public List<StatisticsContainer> getStatisticsContainers() {
        activate(ActivationPurpose.READ);
        return statisticsContainers;
    }

    /**
     *
     * @param statisticsContainers
     */
    public void setStatisticsContainers(List<StatisticsContainer> statisticsContainers) {
        activate(ActivationPurpose.WRITE);
        List<StatisticsContainer> old = this.statisticsContainers;
        for (StatisticsContainer sc : statisticsContainers) {
            sc.setProject(getProject());
        }
        this.statisticsContainers = statisticsContainers;
        firePropertyChange(PROP_STATISTICSCONTAINERS, old, this.statisticsContainers);
    }

    /**
     *
     * @param type
     * @return
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/PeakGroup.png");
    }

    /**
     *
     * @param members
     */
    @Override
    public void setMembers(Collection<IPeakGroupDescriptor> members) {
        for (IPeakGroupDescriptor descr : members) {
            descr.setPeakGroupContainer(this);
        }
        super.setMembers(members);
    }

    /**
     *
     * @param f
     */
    @Override
    public void setMembers(IPeakGroupDescriptor... f) {
        for (IPeakGroupDescriptor descr : f) {
            descr.setPeakGroupContainer(this);
        }
        super.setMembers(f);
    }

    /**
     *
     * @param f
     */
    @Override
    public void addMembers(IPeakGroupDescriptor... f) {
        for (IPeakGroupDescriptor descr : f) {
            descr.setPeakGroupContainer(this);
        }
        super.addMembers(f);
    }

    /**
     *
     * @param f
     */
    @Override
    public void removeMembers(IPeakGroupDescriptor... f) {
        for (IPeakGroupDescriptor descr : f) {
            descr.setPeakGroupContainer(null);
        }
        super.removeMembers(f);
    }
}
