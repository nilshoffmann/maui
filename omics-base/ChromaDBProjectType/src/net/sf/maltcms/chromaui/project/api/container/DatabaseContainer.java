/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.container;

import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import org.openide.util.ImageUtilities;

/**
 *
 * @author nilshoffmann
 */
public class DatabaseContainer extends ADatabaseBackedContainer<IDatabaseDescriptor> {
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/DBDescriptor.png");
    }
    
}
