/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.ui.plot.chromatogram1D.tasks;

import lombok.Data;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class Chromatogram1DTopComponentLoader extends AProgressAwareRunnable {

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
