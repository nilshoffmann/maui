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
package net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers;

import com.db4o.activation.ActivationPurpose;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.container.MetaDataContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IMetaDataDescriptor;
import uk.ac.ebi.pride.jmztab.model.Metadata;

public class MzTabMetaDataContainer extends MetaDataContainer<IMetaDataDescriptor> {

    public static MzTabMetaDataContainer create(Metadata metadata) {
        MzTabMetaDataContainer mzt = new MzTabMetaDataContainer();
        mzt.setName("metaData");
        mzt.setDisplayName("Meta Data");
        mzt.setAssays(AssayContainer.create(metadata));
        mzt.setContacts(ContactContainer.create(metadata));
        mzt.setParams(ParamContainer.create(metadata));
        mzt.setCvs(CVContainer.create(metadata));

//        metadata.getFalseDiscoveryRate();
//        
//        metadata.getPeptideColUnitList();
//        metadata.getProteinColUnitList();
//        metadata.getPsmColUnitList();
//        metadata.getSmallMoleculeColUnitList();
        mzt.setSamples(SampleContainer.create(metadata));
        mzt.setFixedMods(FixedModContainer.create(metadata));
        mzt.setInstruments(InstrumentContainer.create(metadata));
        mzt.setMsRuns(MsRunContainer.create(metadata));
        mzt.setSoftwares(SoftwareContainer.create(metadata));
        mzt.setStudyVariables(StudyVariableContainer.create(metadata));

//        metadata.getTabDescription();
//        metadata.getUriList();
        mzt.setVariableMods(VariableModContainer.create(metadata));
//        metadata.getVariableModMap();

        return mzt;
    }

    @Override
    public void removeMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMembers(Collection<IMetaDataDescriptor> members) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<IMetaDataDescriptor> getMembers() {
        ArrayList<IMetaDataDescriptor> members = new ArrayList<IMetaDataDescriptor>();
        members.add(getContacts());
        members.add(getStudyVariables());
        members.add(getInstruments());
        members.add(getCvs());
        members.add(getParams());
        members.add(getMsRuns());
        members.add(getAssays());
        members.add(getSoftwares());
        members.add(getFixedMods());
        members.add(getVariableMods());
        return members;
    }

    private SampleContainer samples;

    public static final String PROP_SAMPLES = "samples";

    /**
     * Get the value of samples
     *
     * @return the value of samples
     */
    public SampleContainer getSamples() {
        activate(ActivationPurpose.READ);
        return samples;
    }

    /**
     * Set the value of samples
     *
     * @param samples new value of samples
     */
    public void setSamples(SampleContainer samples) {
        activate(ActivationPurpose.WRITE);
        SampleContainer oldSamples = this.samples;
        this.samples = samples;
        getPropertyChangeSupport().firePropertyChange(PROP_SAMPLES, oldSamples, samples);
    }

    private AssayContainer assays;

    public static final String PROP_ASSAYS = "assays";

    /**
     * Get the value of assay
     *
     * @return the value of assay
     */
    public AssayContainer getAssays() {
        activate(ActivationPurpose.READ);
        return assays;
    }

    /**
     * Set the value of assay
     *
     * @param assay new value of assay
     */
    public void setAssays(AssayContainer assays) {
        activate(ActivationPurpose.WRITE);
        AssayContainer oldAssays = this.assays;
        this.assays = assays;
        getPropertyChangeSupport().firePropertyChange(PROP_ASSAYS, oldAssays, assays);
    }

//    ContactContainer cc = ContactContainer.create(metadata);
    private ContactContainer contacts;

    public static final String PROP_CONTACTS = "contacts";

    /**
     * Get the value of contacts
     *
     * @return the value of contacts
     */
    public ContactContainer getContacts() {
        activate(ActivationPurpose.READ);
        return contacts;
    }

    /**
     * Set the value of contacts
     *
     * @param contacts new value of contacts
     */
    public void setContacts(ContactContainer contacts) {
        activate(ActivationPurpose.WRITE);
        ContactContainer oldContacts = this.contacts;
        this.contacts = contacts;
        getPropertyChangeSupport().firePropertyChange(PROP_CONTACTS, oldContacts, contacts);
    }

//        ParamContainer pc = ParamContainer.create(metadata);
    private ParamContainer params;

    public static final String PROP_PARAMS = "params";

    /**
     * Get the value of params
     *
     * @return the value of params
     */
    public ParamContainer getParams() {
        activate(ActivationPurpose.READ);
        return params;
    }

    /**
     * Set the value of params
     *
     * @param params new value of params
     */
    public void setParams(ParamContainer params) {
        activate(ActivationPurpose.WRITE);
        ParamContainer oldParams = this.params;
        this.params = params;
        getPropertyChangeSupport().firePropertyChange(PROP_PARAMS, oldParams, params);
    }

//        CVContainer cvc = CVContainer.create(metadata);
    private CVContainer cvs;

    public static final String PROP_CVS = "cvs";

    /**
     * Get the value of cvs
     *
     * @return the value of cvs
     */
    public CVContainer getCvs() {
        activate(ActivationPurpose.READ);
        return cvs;
    }

    /**
     * Set the value of cvs
     *
     * @param cvs new value of cvs
     */
    public void setCvs(CVContainer cvs) {
        activate(ActivationPurpose.WRITE);
        CVContainer oldCvs = this.cvs;
        this.cvs = cvs;
        getPropertyChangeSupport().firePropertyChange(PROP_CVS, oldCvs, cvs);
    }

//
////        metadata.getFalseDiscoveryRate();
////        
////        metadata.getPeptideColUnitList();
////        metadata.getProteinColUnitList();
////        metadata.getPsmColUnitList();
////        metadata.getSmallMoleculeColUnitList();
//        FixedModContainer fmc = FixedModContainer.create(metadata);
    private FixedModContainer fixedMods;

    public static final String PROP_FIXEDMODS = "fixedMods";

    /**
     * Get the value of fixedMods
     *
     * @return the value of fixedMods
     */
    public FixedModContainer getFixedMods() {
        activate(ActivationPurpose.READ);
        return fixedMods;
    }

    /**
     * Set the value of fixedMods
     *
     * @param fixedMods new value of fixedMods
     */
    public void setFixedMods(FixedModContainer fixedMods) {
        activate(ActivationPurpose.WRITE);
        FixedModContainer oldFixedMods = this.fixedMods;
        this.fixedMods = fixedMods;
        getPropertyChangeSupport().firePropertyChange(PROP_FIXEDMODS, oldFixedMods, fixedMods);
    }

//        InstrumentContainer ic = InstrumentContainer.create(metadata);
    private InstrumentContainer instruments;

    public static final String PROP_INSTRUMENTS = "instruments";

    /**
     * Get the value of instruments
     *
     * @return the value of instruments
     */
    public InstrumentContainer getInstruments() {
        activate(ActivationPurpose.READ);
        return instruments;
    }

    /**
     * Set the value of instruments
     *
     * @param instruments new value of instruments
     */
    public void setInstruments(InstrumentContainer instruments) {
        activate(ActivationPurpose.WRITE);
        InstrumentContainer oldInstruments = this.instruments;
        this.instruments = instruments;
        getPropertyChangeSupport().firePropertyChange(PROP_INSTRUMENTS, oldInstruments, instruments);
    }

//        MsRunContainer mrc = MsRunContainer.create(metadata);
    private MsRunContainer msRuns;

    public static final String PROP_MSRUNS = "msRuns";

    /**
     * Get the value of msRuns
     *
     * @return the value of msRuns
     */
    public MsRunContainer getMsRuns() {
        activate(ActivationPurpose.READ);
        return msRuns;
    }

    /**
     * Set the value of msRuns
     *
     * @param msRuns new value of msRuns
     */
    public void setMsRuns(MsRunContainer msRuns) {
        activate(ActivationPurpose.WRITE);
        MsRunContainer oldMsRuns = this.msRuns;
        this.msRuns = msRuns;
        getPropertyChangeSupport().firePropertyChange(PROP_MSRUNS, oldMsRuns, msRuns);
    }

//        SoftwareContainer sc = SoftwareContainer.create(metadata);
    private SoftwareContainer softwares;

    public static final String PROP_SOFTWARES = "softwares";

    /**
     * Get the value of softwares
     *
     * @return the value of softwares
     */
    public SoftwareContainer getSoftwares() {
        activate(ActivationPurpose.READ);
        return softwares;
    }

    /**
     * Set the value of softwares
     *
     * @param softwares new value of softwares
     */
    public void setSoftwares(SoftwareContainer softwares) {
        activate(ActivationPurpose.WRITE);
        SoftwareContainer oldSoftwares = this.softwares;
        this.softwares = softwares;
        getPropertyChangeSupport().firePropertyChange(PROP_SOFTWARES, oldSoftwares, softwares);
    }

//        StudyVariableContainer svc = StudyVariableContainer.create(metadata);
    private StudyVariableContainer studyVariables;

    public static final String PROP_STUDYVARIABLES = "studyVariables";

    /**
     * Get the value of studyVariables
     *
     * @return the value of studyVariables
     */
    public StudyVariableContainer getStudyVariables() {
        activate(ActivationPurpose.READ);
        return studyVariables;
    }

    /**
     * Set the value of studyVariables
     *
     * @param studyVariables new value of studyVariables
     */
    public void setStudyVariables(StudyVariableContainer studyVariables) {
        activate(ActivationPurpose.WRITE);
        StudyVariableContainer oldStudyVariables = this.studyVariables;
        this.studyVariables = studyVariables;
        getPropertyChangeSupport().firePropertyChange(PROP_STUDYVARIABLES, oldStudyVariables, studyVariables);
    }

//
////        metadata.getTabDescription();
////        metadata.getUriList();
//        VariableModContainer vmc = VariableModContainer.create(metadata);
    private VariableModContainer variableMods;

    public static final String PROP_VARIABLEMODS = "variableMods";

    /**
     * Get the value of variableMods
     *
     * @return the value of variableMods
     */
    public VariableModContainer getVariableMods() {
        activate(ActivationPurpose.READ);
        return variableMods;
    }

    /**
     * Set the value of variableMods
     *
     * @param variableMods new value of variableMods
     */
    public void setVariableMods(VariableModContainer variableMods) {
        activate(ActivationPurpose.WRITE);
        VariableModContainer oldVariableMods = this.variableMods;
        this.variableMods = variableMods;
        getPropertyChangeSupport().firePropertyChange(PROP_VARIABLEMODS, oldVariableMods, variableMods);
    }

////        metadata.getVariableModMap();
}
