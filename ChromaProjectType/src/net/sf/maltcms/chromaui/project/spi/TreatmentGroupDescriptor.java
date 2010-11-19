/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupDescriptor implements ITreatmentGroupDescriptor<IChromatogramDescriptor> {

    private String name = "NN";
    private Collection<IChromatogramDescriptor> chromatograms = new ArrayList<IChromatogramDescriptor>();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String s) {
        this.name = s;
    }

    @Override
    public Collection<IChromatogramDescriptor> getMembers() {
        return this.chromatograms;
    }

    @Override
    public void setMembers(IChromatogramDescriptor... t) {
        this.chromatograms = Arrays.asList(t);
    }

    @Override
    public void addMembers(IChromatogramDescriptor... t) {
        this.chromatograms.addAll(Arrays.asList(t));
    }

    @Override
    public void deleteMembers(IChromatogramDescriptor... t) {
        this.chromatograms.removeAll(Arrays.asList(t));
    }

    @Override
    public String toString() {
        return "TreatmentGroupDescriptor{" + "name=" + name + "chromatograms=" + chromatograms + '}';
    }


   
}
