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

import java.net.URI;
import uk.ac.ebi.pride.jmztab.model.MZBoolean;
import uk.ac.ebi.pride.jmztab.model.Modification;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Peptide;
import uk.ac.ebi.pride.jmztab.model.Reliability;
import uk.ac.ebi.pride.jmztab.model.SpectraRef;
import uk.ac.ebi.pride.jmztab.model.SplitList;

/**
 *
 * @author Nils Hoffmann
 */
public class PeptideDescriptor extends AMzTabRecordDescriptor<Peptide> {
    private static final long serialVersionUID = -3844143548240087178L;

    /**
     *
     * @return
     */
    public String getSequence() {
        return getRecord().getSequence();
    }

    /**
     *
     * @param sequence
     */
    public void setSequence(String sequence) {
        getRecord().setSequence(sequence);
    }

    /**
     *
     * @return
     */
    public String getAccession() {
        return getRecord().getAccession();
    }

    /**
     *
     * @param accession
     */
    public void setAccession(String accession) {
        getRecord().setAccession(accession);
    }

    /**
     *
     * @return
     */
    public MZBoolean getUnique() {
        return getRecord().getUnique();
    }

    /**
     *
     * @param unique
     */
    public void setUnique(MZBoolean unique) {
        getRecord().setUnique(unique);
    }

    /**
     *
     * @param uniqueLabel
     */
    public void setUnique(String uniqueLabel) {
        getRecord().setUnique(uniqueLabel);
    }

    /**
     *
     * @return
     */
    public String getDatabase() {
        return getRecord().getDatabase();
    }

    /**
     *
     * @param database
     */
    public void setDatabase(String database) {
        getRecord().setDatabase(database);
    }

    /**
     *
     * @return
     */
    public String getDatabaseVersion() {
        return getRecord().getDatabaseVersion();
    }

    /**
     *
     * @param databaseVersion
     */
    public void setDatabaseVersion(String databaseVersion) {
        getRecord().setDatabaseVersion(databaseVersion);
    }

    /**
     *
     * @return
     */
    public SplitList<Param> getSearchEngine() {
        return getRecord().getSearchEngine();
    }

    /**
     *
     * @param param
     * @return
     */
    public boolean addSearchEngineParam(Param param) {
        return getRecord().addSearchEngineParam(param);
    }

    /**
     *
     * @param paramLabel
     * @return
     */
    public boolean addSearchEngineParam(String paramLabel) {
        return getRecord().addSearchEngineParam(paramLabel);
    }

    /**
     *
     * @param searchEngine
     */
    public void setSearchEngine(SplitList<Param> searchEngine) {
        getRecord().setSearchEngine(searchEngine);
    }

    /**
     *
     * @param searchEngineLabel
     */
    public void setSearchEngine(String searchEngineLabel) {
        getRecord().setSearchEngine(searchEngineLabel);
    }

       /**
     *
     * @param id
     * @return
     */
    public Double getBestSearchEngineScore(Integer id) {
        return getRecord().getBestSearchEngineScore(id);
    }

    /**
     *
     * @param id
     * @param bestSearchEngineScore
     */
    public void setBestSearchEngineScore(Integer id, Double bestSearchEngineScore) {
        getRecord().setBestSearchEngineScore(id, bestSearchEngineScore);
    }

    /**
     *
     * @param id
     * @param bestSearchEngineScoreLabel
     */
    public void setBestSearchEngineScore(Integer id, String bestSearchEngineScoreLabel) {
        getRecord().setBestSearchEngineScore(id, bestSearchEngineScoreLabel);
    }

    /**
     *
     * @param id
     * @param msRun
     * @return
     */
    public Double getSearchEngineScore(Integer id, MsRun msRun) {
        return getRecord().getSearchEngineScore(id, msRun);
    }

    /**
     *
     * @param id
     * @param msRun
     * @param paramsLabel
     */
    public void setSearchEngineScore(Integer id, MsRun msRun, String paramsLabel) {
        getRecord().setSearchEngineScore(id, msRun, paramsLabel);
    }

    /**
     *
     * @param id
     * @param searchEngineScore
     * @param msRun
     */
    public void setSearchEngineScore(Integer id, MsRun msRun, Double searchEngineScore) {
        getRecord().setSearchEngineScore(id, msRun, searchEngineScore);
    }

    /**
     *
     * @return
     */
    public Reliability getReliability() {
        return getRecord().getReliability();
    }

    /**
     *
     * @param reliability
     */
    public void setReliability(Reliability reliability) {
        getRecord().setReliability(reliability);
    }

    /**
     *
     * @param reliabilityLabel
     */
    public void setReliability(String reliabilityLabel) {
        getRecord().setReliability(reliabilityLabel);
    }

    /**
     *
     * @return
     */
    public SplitList<Modification> getModifications() {
        return getRecord().getModifications();
    }

    /**
     *
     * @param modification
     * @return
     */
    public boolean addModification(Modification modification) {
        return getRecord().addModification(modification);
    }

    /**
     *
     * @param modifications
     */
    public void setModifications(SplitList<Modification> modifications) {
        getRecord().setModifications(modifications);
    }

    /**
     *
     * @param modificationsLabel
     */
    public void setModifications(String modificationsLabel) {
        getRecord().setModifications(modificationsLabel);
    }

    /**
     *
     * @return
     */
    public SplitList<Double> getRetentionTime() {
        return getRecord().getRetentionTime();
    }

    /**
     *
     * @param rt
     * @return
     */
    public boolean addRetentionTime(Double rt) {
        return getRecord().addRetentionTime(rt);
    }

    /**
     *
     * @param rtLabel
     * @return
     */
    public boolean addRetentionTime(String rtLabel) {
        return getRecord().addRetentionTime(rtLabel);
    }

    /**
     *
     * @param retentionTime
     */
    public void setRetentionTime(SplitList<Double> retentionTime) {
        getRecord().setRetentionTime(retentionTime);
    }

    /**
     *
     * @param retentionTimeLabel
     */
    public void setRetentionTime(String retentionTimeLabel) {
        getRecord().setRetentionTime(retentionTimeLabel);
    }

    /**
     *
     * @return
     */
    public SplitList<Double> getRetentionTimeWindow() {
        return getRecord().getRetentionTimeWindow();
    }

    /**
     *
     * @param rtw
     * @return
     */
    public boolean addRetentionTimeWindow(Double rtw) {
        return getRecord().addRetentionTimeWindow(rtw);
    }

    /**
     *
     * @param retentionTimeWindowLabel
     * @return
     */
    public boolean addRetentionTimeWindow(String retentionTimeWindowLabel) {
        return getRecord().addRetentionTimeWindow(retentionTimeWindowLabel);
    }

    /**
     *
     * @param retentionTimeWindow
     */
    public void setRetentionTimeWindow(SplitList<Double> retentionTimeWindow) {
        getRecord().setRetentionTimeWindow(retentionTimeWindow);
    }

    /**
     *
     * @param retentionTimeWindowLabel
     */
    public void setRetentionTimeWindow(String retentionTimeWindowLabel) {
        getRecord().setRetentionTimeWindow(retentionTimeWindowLabel);
    }

    /**
     *
     * @return
     */
    public Integer getCharge() {
        return getRecord().getCharge();
    }

    /**
     *
     * @param charge
     */
    public void setCharge(Integer charge) {
        getRecord().setCharge(charge);
    }

    /**
     *
     * @param chargeLabel
     */
    public void setCharge(String chargeLabel) {
        getRecord().setCharge(chargeLabel);
    }

    /**
     *
     * @return
     */
    public Double getMassToCharge() {
        return getRecord().getMassToCharge();
    }

    /**
     *
     * @param massToCharge
     */
    public void setMassToCharge(Double massToCharge) {
        getRecord().setMassToCharge(massToCharge);
    }

    /**
     *
     * @param massToChargeLabel
     */
    public void setMassToCharge(String massToChargeLabel) {
        getRecord().setMassToCharge(massToChargeLabel);
    }

    /**
     *
     * @return
     */
    public URI getURI() {
        return getRecord().getURI();
    }

    /**
     *
     * @param uri
     */
    public void setURI(URI uri) {
        getRecord().setURI(uri);
    }

    /**
     *
     * @param uriLabel
     */
    public void setURI(String uriLabel) {
        getRecord().setURI(uriLabel);
    }

    /**
     *
     * @return
     */
    public SplitList<SpectraRef> getSpectraRef() {
        return getRecord().getSpectraRef();
    }

    /**
     *
     * @param specRef
     * @return
     */
    public boolean addSpectraRef(SpectraRef specRef) {
        return getRecord().addSpectraRef(specRef);
    }

    /**
     *
     * @param spectraRef
     */
    public void setSpectraRef(SplitList<SpectraRef> spectraRef) {
        getRecord().setSpectraRef(spectraRef);
    }

    /**
     *
     * @param spectraRefLabel
     */
    public void setSpectraRef(String spectraRefLabel) {
        getRecord().setSpectraRef(spectraRefLabel);
    }

    @Override
    public String toString() {
        return getRecord().toString();
    }

}
