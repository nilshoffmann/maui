/*
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */

package net.sf.maltcms.chromaui.project.spi.descriptors.mztab;

import com.db4o.activation.ActivationPurpose;
import java.util.SortedMap;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.Assay;
import uk.ac.ebi.pride.jmztab.model.AssayQuantificationMod;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Sample;


public class AssayDescriptor extends ABasicDescriptor implements IMzTabDescriptor {
    public final static String PROP_ASSAY = "assay";
    
    private Assay assay;
    
    public Assay getAssay() {
        activate(ActivationPurpose.READ);
        return assay;
    }
    
    public void setAssay(Assay element) {
        activate(ActivationPurpose.WRITE);
        Assay old = this.assay;
        this.assay = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_ASSAY, old, element);
    }
    
    public Integer getElementId() {
        return assay.getId();
    }
    
    public void setElementId(Integer id) {
        assay.setId(id);
    }

    public Param getQuantificationReagent() {
        return assay.getQuantificationReagent();
    }

    public Sample getSample() {
        return assay.getSample();
    }

    public MsRun getMsRun() {
        return assay.getMsRun();
    }

    public SortedMap<Integer, AssayQuantificationMod> getQuantificationModMap() {
        return assay.getQuantificationModMap();
    }

    public void setQuantificationReagent(Param quantificationReagent) {
        assay.setQuantificationReagent(quantificationReagent);
    }

    public void setSample(Sample sample) {
        assay.setSample(sample);
    }

    public void setMsRun(MsRun msRun) {
        assay.setMsRun(msRun);
    }

    public void addQuantificationMod(AssayQuantificationMod mod) {
        assay.addQuantificationMod(mod);
    }

    public void addQuantificationModParam(Integer id, Param param) {
        assay.addQuantificationModParam(id, param);
    }

    public void addQuantificationModSite(Integer id, String site) {
        assay.addQuantificationModSite(id, site);
    }

    public void addQuantificationModPosition(Integer id, String position) {
        assay.addQuantificationModPosition(id, position);
    }

    @Override
    public String toString() {
        return assay.toString();
    }

    public MetadataElement getElement() {
        return assay.getElement();
    }

    public String getReference() {
        return assay.getReference();
    }
    
}
