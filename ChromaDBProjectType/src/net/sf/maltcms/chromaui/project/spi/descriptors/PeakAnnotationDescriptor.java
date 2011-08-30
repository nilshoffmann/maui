/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import lombok.Data;
import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

/**
 *
 * @author nilshoffmann
 */
@Data
public class PeakAnnotationDescriptor implements IPeakAnnotationDescriptor {

    private Peak1D peak;
    private String displayName;
}
