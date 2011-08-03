/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.container;

import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.container.ADatabaseBackedContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.util.ImageUtilities;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupContainer extends ADatabaseBackedContainer<IChromatogramDescriptor>{

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/Group.png");
    }

}
