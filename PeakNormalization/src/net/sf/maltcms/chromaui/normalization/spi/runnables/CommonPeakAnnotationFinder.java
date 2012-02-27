/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi.runnables;

import lombok.Data;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class CommonPeakAnnotationFinder extends AProgressAwareRunnable {

    private final PeakGroupContainer container;

    @Override
    public void run() {
        try {
            progressHandle.start();
            
            progressHandle.finish();
        } catch (Exception e) {
            progressHandle.finish();
        }
    }

}
