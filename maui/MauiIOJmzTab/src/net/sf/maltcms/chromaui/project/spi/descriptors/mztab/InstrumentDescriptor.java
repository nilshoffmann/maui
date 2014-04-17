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
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.Instrument;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import uk.ac.ebi.pride.jmztab.model.Param;


public class InstrumentDescriptor extends ABasicDescriptor implements IMzTabDescriptor {

    public final static String PROP_INSTRUMENT = "instrument";

    private Instrument instrument;

    public Instrument getInstrument() {
        activate(ActivationPurpose.READ);
        return instrument;
    }

    public void setInstrument(Instrument element) {
        activate(ActivationPurpose.WRITE);
        Instrument old = this.instrument;
        this.instrument = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getName().getName());
        setShortDescription(element.toString());
        getPropertyChangeSupport().firePropertyChange(PROP_INSTRUMENT, old, element);
    }

    public Integer getElementId() {
        return getInstrument().getId();
    }

    public void setElementId(Integer id) {
        getInstrument().setId(id);
    }

    public Param getElementName() {
        return getInstrument().getName();
    }

    public void setElementName(Param name) {
        getInstrument().setName(name);
    }

    public Param getSource() {
        return getInstrument().getSource();
    }

    public void setSource(Param source) {
        getInstrument().setSource(source);
    }

    public Param getAnalyzer() {
        return getInstrument().getAnalyzer();
    }

    public void setAnalyzer(Param analyzer) {
        getInstrument().setAnalyzer(analyzer);
    }

    public Param getDetector() {
        return getInstrument().getDetector();
    }

    public void setDetector(Param detector) {
        getInstrument().setDetector(detector);
    }

    public String toString() {
        return getInstrument().toString();
    }

    public MetadataElement getElement() {
        return getInstrument().getElement();
    }

    public String getReference() {
        return getInstrument().getReference();
    }
    

}
