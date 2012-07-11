/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.runnables;

import java.io.File;
import java.util.*;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.project.spi.ui.Dialogs;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author hoffmann
 */
@Data
public class DeletePeakAnnotationsRunnable extends AProgressAwareRunnable {

    private final IChromAUIProject project;

    @Override
    public void run() {
        try {
            progressHandle.start(3);
            progressHandle.progress("Retrieving Tool Descriptors", 1);
            final Set<IToolDescriptor> tools = new LinkedHashSet<IToolDescriptor>();
            for (IChromatogramDescriptor chrom : project.getChromatograms()) {
                for (Peak1DContainer container : project.getPeaks(chrom)) {
                    tools.add(container.getTool());
                }
            }
            Collection<? extends IToolDescriptor> selectedTools = Dialogs.showAndSelectToolDescriptors(tools, Lookups.singleton(project));
            if (!selectedTools.isEmpty()) {
                tools.clear();
                tools.addAll(selectedTools);

                
                progressHandle.progress("Retrieving Peak Containers for " + tools.size() + " Tools", 2);
                List<Peak1DContainer> peakContainers = new ArrayList<Peak1DContainer>();
                for (IChromatogramDescriptor chrom : project.getChromatograms()) {
                    for (Peak1DContainer container : project.getPeaks(chrom)) {
                        if (tools.contains(container.getTool())) {
                            peakContainers.add(container);
                        }
                    }
                }
                progressHandle.progress("Removing Peak Containers for " + tools.size() + " Tools", 3);
                project.removeContainer(peakContainers.toArray(new Peak1DContainer[peakContainers.size()]));
                project.refresh();

            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
    }
}
