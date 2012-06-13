/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.project.api.container;

import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableArrayList;
import java.awt.Image;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class PeakGroupContainer extends ADatabaseBackedContainer<IPeakGroupDescriptor> {

    private List<StatisticsContainer> statisticsContainers = new ActivatableArrayList<StatisticsContainer>();
    
    public final String PROP_STATISTICSCONTAINERS = "statisticsContainers";

    public List<StatisticsContainer> getStatisticsContainers() {
        activate(ActivationPurpose.READ);
        return statisticsContainers;
    }

    public void setStatisticsContainers(List<StatisticsContainer> statisticsContainers) {
        activate(ActivationPurpose.WRITE);
        List<StatisticsContainer> old = this.statisticsContainers;
        this.statisticsContainers = statisticsContainers;
        firePropertyChange(PROP_STATISTICSCONTAINERS,old,this.statisticsContainers);
    }
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/PeakGroup.png");
    }
}
