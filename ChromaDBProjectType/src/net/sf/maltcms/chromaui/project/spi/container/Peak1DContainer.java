/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi.container;

import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.container.ADatabaseBackedContainer;
import net.sf.maltcms.chromaui.project.api.types.Peak1DAnnotation;
import org.openide.util.ImageUtilities;

/**
 *
 * @author nilshoffmann
 */
public class Peak1DContainer extends ADatabaseBackedContainer<Peak1DAnnotation>{

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/Peak.png");
    }
    
}
