package net.sf.maltcms.chromaui.statistics.view.spi.nodes;

import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.statistics.view.api.IStatisticsDescriptorComparator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author hoffmann
 */
@ServiceProvider(service = IStatisticsDescriptorComparator.class)
public class DisplayNameComparator implements
		IStatisticsDescriptorComparator {

	@Override
	public int compare(IStatisticsDescriptor t, IStatisticsDescriptor t1) {
		if (t.getClass().equals(t1.getClass())) {
			return t.compareTo(t1);
		}
		return t.getDisplayName().compareTo(t1.getDisplayName());
	}
}
