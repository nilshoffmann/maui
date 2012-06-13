/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.project.api.container;

import com.db4o.activation.ActivationPurpose;
import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import org.openide.util.ImageUtilities;

/**
 * FIXME IStatisticsDescriptor needs a refactoring 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class StatisticsContainer extends ADatabaseBackedContainer<IStatisticsDescriptor> {

    private String method;
    public static final String PROP_METHOD = "method";

    /**
     * Get the value of method
     *
     * @return the value of method
     */
    public String getMethod() {
        activate(ActivationPurpose.READ);
        return method;
    }

    /**
     * Set the value of method
     *
     * @param method new value of method
     */
    public void setMethod(String method) {
        activate(ActivationPurpose.WRITE);
        String oldMethod = this.method;
        this.method = method;
        firePropertyChange(PROP_METHOD, oldMethod, method);
    }
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(
                "net/sf/maltcms/chromaui/project/resources/Statistics.png");
    }
}
