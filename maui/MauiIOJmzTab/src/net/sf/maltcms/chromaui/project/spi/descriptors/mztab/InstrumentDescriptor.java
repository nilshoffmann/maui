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
import java.util.List;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.Instrument;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Param;

/**
 *
 * @author Nils Hoffmann
 */
public class InstrumentDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    /**
     *
     */
    public final static String PROP_INSTRUMENT = "instrument";
    private static final long serialVersionUID = -3897134427829554736L;

    private Instrument instrument;

    /**
     *
     * @return
     */
    public Instrument getInstrument() {
        activate(ActivationPurpose.READ);
        return instrument;
    }

    /**
     *
     * @param element
     */
    public void setInstrument(Instrument element) {
        activate(ActivationPurpose.WRITE);
        Instrument old = this.instrument;
        this.instrument = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getName().getName());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_INSTRUMENT, old, element);
    }

    /**
     *
     * @return
     */
    public Integer getElementId() {
        return getInstrument().getId();
    }

    /**
     *
     * @param id
     */
    public void setElementId(Integer id) {
        getInstrument().setId(id);
    }

    /**
     *
     * @return
     */
    public Param getElementName() {
        return getInstrument().getName();
    }

    /**
     *
     * @param name
     */
    public void setElementName(Param name) {
        getInstrument().setName(name);
    }

    /**
     *
     * @return
     */
    public Param getSource() {
        return getInstrument().getSource();
    }

    /**
     *
     * @param source
     */
    public void setSource(Param source) {
        getInstrument().setSource(source);
    }

    /**
     *
     * @return
     */
    public List<Param> getAnalyzer() {
        return getInstrument().getAnalyzerList();
    }

    /**
     *
     * @param analyzer
     */
    public void setAnalyzer(List<Param> analyzer) {
        getInstrument().getAnalyzerList().clear();
        getInstrument().getAnalyzerList().addAll(analyzer);
    }

    /**
     *
     * @return
     */
    public Param getDetector() {
        return getInstrument().getDetector();
    }

    /**
     *
     * @param detector
     */
    public void setDetector(Param detector) {
        getInstrument().setDetector(detector);
    }

    @Override
    public String toString() {
        return getInstrument().toString();
    }

    /**
     *
     * @return
     */
    public MetadataElement getElement() {
        return getInstrument().getElement();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return getInstrument().getReference();
    }

}
