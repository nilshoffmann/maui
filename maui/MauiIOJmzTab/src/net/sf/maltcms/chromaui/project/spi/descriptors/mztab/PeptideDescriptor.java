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
import uk.ac.ebi.pride.jmztab.model.CVParam;
import uk.ac.ebi.pride.jmztab.model.MZBoolean;
import uk.ac.ebi.pride.jmztab.model.Modification;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Peptide;
import uk.ac.ebi.pride.jmztab.model.Reliability;
import uk.ac.ebi.pride.jmztab.model.SpectraRef;
import uk.ac.ebi.pride.jmztab.model.SplitList;

public class PeptideDescriptor extends AMzTabRecordDescriptor<Peptide> {

    public String getSequence() {
        return getRecord().getSequence();
    }

    public void setSequence(String sequence) {
        getRecord().setSequence(sequence);
    }

    public String getAccession() {
        return getRecord().getAccession();
    }

    public void setAccession(String accession) {
        getRecord().setAccession(accession);
    }

    public MZBoolean getUnique() {
        return getRecord().getUnique();
    }

    public void setUnique(MZBoolean unique) {
        getRecord().setUnique(unique);
    }

    public void setUnique(String uniqueLabel) {
        getRecord().setUnique(uniqueLabel);
    }

    public String getDatabase() {
        return getRecord().getDatabase();
    }

    public void setDatabase(String database) {
        getRecord().setDatabase(database);
    }

    public String getDatabaseVersion() {
        return getRecord().getDatabaseVersion();
    }

    public void setDatabaseVersion(String databaseVersion) {
        getRecord().setDatabaseVersion(databaseVersion);
    }

    public SplitList<Param> getSearchEngine() {
        return getRecord().getSearchEngine();
    }

    public boolean addSearchEngineParam(Param param) {
        return getRecord().addSearchEngineParam(param);
    }

    public boolean addSearchEngineParam(String paramLabel) {
        return getRecord().addSearchEngineParam(paramLabel);
    }

    public void setSearchEngine(SplitList<Param> searchEngine) {
        getRecord().setSearchEngine(searchEngine);
    }

    public void setSearchEngine(String searchEngineLabel) {
        getRecord().setSearchEngine(searchEngineLabel);
    }

    public SplitList<Param> getBestSearchEngineScore() {
        return getRecord().getBestSearchEngineScore();
    }

    public boolean addBestSearchEngineScoreParam(Param param) {
        return getRecord().addBestSearchEngineScoreParam(param);
    }

    public boolean addBestSearchEngineScoreParam(String paramLabel) {
        return getRecord().addBestSearchEngineScoreParam(paramLabel);
    }

    public void setBestSearchEngineScore(SplitList<Param> bestSearchEngineScore) {
        getRecord().setBestSearchEngineScore(bestSearchEngineScore);
    }

    public void setBestSearchEngineScore(String bestSearchEngineScoreLabel) {
        getRecord().setBestSearchEngineScore(bestSearchEngineScoreLabel);
    }

    public SplitList<Param> getSearchEngineScore(MsRun msRun) {
        return getRecord().getSearchEngineScore(msRun);
    }

    public void setSearchEngineScore(String logicalPosition, SplitList<Param> searchEngineScore) {
        getRecord().setSearchEngineScore(logicalPosition, searchEngineScore);
    }

    public void setSearchEngineScore(MsRun msRun, SplitList<Param> searchEngineScore) {
        getRecord().setSearchEngineScore(msRun, searchEngineScore);
    }

    public boolean addSearchEngineScoreParam(MsRun msRun, CVParam param) {
        return getRecord().addSearchEngineScoreParam(msRun, param);
    }

    public void setSearchEngineScore(String logicalPosition, String paramsLabel) {
        getRecord().setSearchEngineScore(logicalPosition, paramsLabel);
    }

    public void setSearchEngineScore(MsRun msRun, String paramsLabel) {
        getRecord().setSearchEngineScore(msRun, paramsLabel);
    }

    public Reliability getReliability() {
        return getRecord().getReliability();
    }

    public void setReliability(Reliability reliability) {
        getRecord().setReliability(reliability);
    }

    public void setReliability(String reliabilityLabel) {
        getRecord().setReliability(reliabilityLabel);
    }

    public SplitList<Modification> getModifications() {
        return getRecord().getModifications();
    }

    public boolean addModification(Modification modification) {
        return getRecord().addModification(modification);
    }

    public void setModifications(SplitList<Modification> modifications) {
        getRecord().setModifications(modifications);
    }

    public void setModifications(String modificationsLabel) {
        getRecord().setModifications(modificationsLabel);
    }

    public SplitList<Double> getRetentionTime() {
        return getRecord().getRetentionTime();
    }

    public boolean addRetentionTime(Double rt) {
        return getRecord().addRetentionTime(rt);
    }

    public boolean addRetentionTime(String rtLabel) {
        return getRecord().addRetentionTime(rtLabel);
    }

    public void setRetentionTime(SplitList<Double> retentionTime) {
        getRecord().setRetentionTime(retentionTime);
    }

    public void setRetentionTime(String retentionTimeLabel) {
        getRecord().setRetentionTime(retentionTimeLabel);
    }

    public SplitList<Double> getRetentionTimeWindow() {
        return getRecord().getRetentionTimeWindow();
    }

    public boolean addRetentionTimeWindow(Double rtw) {
        return getRecord().addRetentionTimeWindow(rtw);
    }

    public boolean addRetentionTimeWindow(String retentionTimeWindowLabel) {
        return getRecord().addRetentionTimeWindow(retentionTimeWindowLabel);
    }

    public void setRetentionTimeWindow(SplitList<Double> retentionTimeWindow) {
        getRecord().setRetentionTimeWindow(retentionTimeWindow);
    }

    public void setRetentionTimeWindow(String retentionTimeWindowLabel) {
        getRecord().setRetentionTimeWindow(retentionTimeWindowLabel);
    }

    public Integer getCharge() {
        return getRecord().getCharge();
    }

    public void setCharge(Integer charge) {
        getRecord().setCharge(charge);
    }

    public void setCharge(String chargeLabel) {
        getRecord().setCharge(chargeLabel);
    }

    public Double getMassToCharge() {
        return getRecord().getMassToCharge();
    }

    public void setMassToCharge(Double massToCharge) {
        getRecord().setMassToCharge(massToCharge);
    }

    public void setMassToCharge(String massToChargeLabel) {
        getRecord().setMassToCharge(massToChargeLabel);
    }

    public URI getURI() {
        return getRecord().getURI();
    }

    public void setURI(URI uri) {
        getRecord().setURI(uri);
    }

    public void setURI(String uriLabel) {
        getRecord().setURI(uriLabel);
    }

    public SplitList<SpectraRef> getSpectraRef() {
        return getRecord().getSpectraRef();
    }

    public boolean addSpectraRef(SpectraRef specRef) {
        return getRecord().addSpectraRef(specRef);
    }

    public void setSpectraRef(SplitList<SpectraRef> spectraRef) {
        getRecord().setSpectraRef(spectraRef);
    }

    public void setSpectraRef(String spectraRefLabel) {
        getRecord().setSpectraRef(spectraRefLabel);
    }

    public String toString() {
        return getRecord().toString();
    }
    

}
