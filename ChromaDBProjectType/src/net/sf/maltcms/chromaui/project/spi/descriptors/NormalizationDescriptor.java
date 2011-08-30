/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.descriptors;

import lombok.Data;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.types.NormalizationType;

/**
 *
 * @author nilshoffmann
 */
@Data
public class NormalizationDescriptor implements INormalizationDescriptor {
    private NormalizationType normalizationType = NormalizationType.DRYWEIGHT;
    private String displayName = "<NA>";
    private double value = 1.0d;
}
