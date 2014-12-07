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

/**
 *
 * @author Nils Hoffmann
 */
public class AssayDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_ASSAY = "assay";
    private static final long serialVersionUID = 4773404413883853123L;

    private Assay assay;

    /**
     *
     * @return
     */
    public Assay getAssay() {
        activate(ActivationPurpose.READ);
        return assay;
    }

    /**
     *
     * @param element
     */
    public void setAssay(Assay element) {
        activate(ActivationPurpose.WRITE);
        Assay old = this.assay;
        this.assay = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getReference());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_ASSAY, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getAssay().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getAssay().setId(id);
    }

    /**
     *
     * @return
     */
    public Param getQuantificationReagent() {
        return getAssay().getQuantificationReagent();
    }

    /**
     *
     * @return
     */
    public Sample getSample() {
        return getAssay().getSample();
    }

    /**
     *
     * @return
     */
    public MsRun getMsRun() {
        return getAssay().getMsRun();
    }

    /**
     *
     * @return
     */
    public SortedMap<Integer, AssayQuantificationMod> getQuantificationModMap() {
        return getAssay().getQuantificationModMap();
    }

    /**
     *
     * @param quantificationReagent
     */
    public void setQuantificationReagent(Param quantificationReagent) {
        getAssay().setQuantificationReagent(quantificationReagent);
    }

    /**
     *
     * @param sample
     */
    public void setSample(Sample sample) {
        getAssay().setSample(sample);
    }

    /**
     *
     * @param msRun
     */
    public void setMsRun(MsRun msRun) {
        getAssay().setMsRun(msRun);
    }

    /**
     *
     * @param mod
     */
    public void addQuantificationMod(AssayQuantificationMod mod) {
        getAssay().addQuantificationMod(mod);
    }

    /**
     *
     * @param id
     * @param param
     */
    public void addQuantificationModParam(Integer id, Param param) {
        getAssay().addQuantificationModParam(id, param);
    }

    /**
     *
     * @param id
     * @param site
     */
    public void addQuantificationModSite(Integer id, String site) {
        getAssay().addQuantificationModSite(id, site);
    }

    /**
     *
     * @param id
     * @param position
     */
    public void addQuantificationModPosition(Integer id, String position) {
        getAssay().addQuantificationModPosition(id, position);
    }

    @Override
    public String toString() {
        return getAssay().toString();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getAssay().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getAssay().getReference();
    }

}
