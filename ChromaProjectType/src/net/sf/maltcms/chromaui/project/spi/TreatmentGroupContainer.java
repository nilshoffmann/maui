/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi;

import java.util.Arrays;
import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.project.api.ADatabaseBackedContainer;
import net.sf.maltcms.chromaui.project.api.ITreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;

/**
 *
 * @author hoffmann
 */
public class TreatmentGroupContainer extends ADatabaseBackedContainer implements ITreatmentGroupContainer<ITreatmentGroupDescriptor> {

    public TreatmentGroupContainer(ICrudProvider db) {
        super(db);
    }

    @Override
    public Collection<ITreatmentGroupDescriptor> getTreatmentGroups() {
        ICrudSession icr = getCrudProvider().createSession();
        Collection<ITreatmentGroupDescriptor> c = icr.retrieve(ITreatmentGroupDescriptor.class);
        //icr.close();
        return c;
    }

    @Override
    public void setTreatmentGroups(ITreatmentGroupDescriptor... itg) {
        ICrudSession icr = getCrudProvider().createSession();
        icr.create(Arrays.asList(itg));
        icr.close();
    }

    @Override
    public void addTreatmentGroups(ITreatmentGroupDescriptor... itg) {
        ICrudSession icr = getCrudProvider().createSession();
        icr.create(Arrays.asList(itg));
        icr.close();
    }

    @Override
    public void removeTreatmentGroups(ITreatmentGroupDescriptor... itg) {
        ICrudSession icr = getCrudProvider().createSession();
        icr.delete(Arrays.asList(itg));
        icr.close();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TreatmentGroupContainer: "+getName()+"\n");
        Collection<ITreatmentGroupDescriptor> c = getTreatmentGroups();
        for(ITreatmentGroupDescriptor igd:c) {
            sb.append(igd.getName()+"\n");
            sb.append(igd.getMembers()+"\n");
        }
        return sb.toString();
    }
}
