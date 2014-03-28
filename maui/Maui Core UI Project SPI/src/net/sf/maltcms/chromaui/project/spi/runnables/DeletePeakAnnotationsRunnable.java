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
 * @author Nils Hoffmann
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
				project.removeDescriptor(selectedTools.toArray(new IToolDescriptor[selectedTools.size()]));
                project.refresh();

            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
    }
}
