/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi;

import maltcms.datastructures.ms.ITreatmentGroup;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupDescriptor implements ITreatmentGroupDescriptor<ITreatmentGroup> {

    private ITreatmentGroup itg;

    @Override
    public String getName() {
        return itg.getName();
    }

    @Override
    public void setName(String s) {
        itg.setName(s);
    }

    @Override
    public ITreatmentGroup getTreatmentGroup() {
        return this.itg;
    }

    @Override
    public void setTreatmentGroup(ITreatmentGroup t) {
        this.itg = t;
    }
}
