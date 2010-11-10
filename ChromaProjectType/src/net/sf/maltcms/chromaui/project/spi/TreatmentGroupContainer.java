/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi;

import java.util.Arrays;
import java.util.Collection;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.ITreatmentGroup;
import net.sf.maltcms.chromaui.project.api.AContainer;
import net.sf.maltcms.chromaui.project.api.ITreatmentGroupContainer;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupContainer extends AContainer implements ITreatmentGroupContainer<IChromatogram>{

    public TreatmentGroupContainer(DB4oCrudProvider db) {
        super(db);
    }

    @Override
    public Collection<ITreatmentGroup<?>> getTreatmentGroups() {
        return getCrudProvider().retrieve(ITreatmentGroup.class);
    }

    @Override
    public void setTreatmentGroups(ITreatmentGroup<IChromatogram>... itg) {
        getCrudProvider().update(Arrays.asList(itg));
    }

    @Override
    public void addTreatmentGroups(ITreatmentGroup<IChromatogram>... itg) {
        getCrudProvider().create(Arrays.asList(itg));
    }

    @Override
    public void removeTreatmentGroups(IChromatogram... itg) {
        getCrudProvider().create(Arrays.asList(itg));
    }

}
