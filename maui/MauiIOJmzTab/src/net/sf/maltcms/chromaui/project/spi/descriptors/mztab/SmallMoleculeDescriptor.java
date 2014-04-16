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
import uk.ac.ebi.pride.jmztab.model.Modification;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Reliability;
import uk.ac.ebi.pride.jmztab.model.SmallMolecule;
import uk.ac.ebi.pride.jmztab.model.SpectraRef;
import uk.ac.ebi.pride.jmztab.model.SplitList;

public class SmallMoleculeDescriptor extends AMzTabRecordDescriptor<SmallMolecule> {

    public SplitList<String> getIdentifier() {
        return getRecord().getIdentifier();
    }

    public boolean addIdentifier(String identifier) {
        return getRecord().addIdentifier(identifier);
    }

    public void setIdentifier(SplitList<String> identifier) {
        getRecord().setIdentifier(identifier);
    }

    public void setIdentifier(String identifierLabel) {
        getRecord().setIdentifier(identifierLabel);
    }

    public String getChemicalFormula() {
        return getRecord().getChemicalFormula();
    }

    public void setChemicalFormula(String chemicalFormula) {
        getRecord().setChemicalFormula(chemicalFormula);
    }

    public String getSmiles() {
        return getRecord().getSmiles();
    }

    public void setSmiles(String smiles) {
        getRecord().setSmiles(smiles);
    }

    public String getInchiKey() {
        return getRecord().getInchiKey();
    }

    public void setInchiKey(String inchiKey) {
        getRecord().setInchiKey(inchiKey);
    }

    public String getDescription() {
        return getRecord().getDescription();
    }

    public void setDescription(String description) {
        getRecord().setDescription(description);
    }

    public Double getExpMassToCharge() {
        return getRecord().getExpMassToCharge();
    }

    public void setExpMassToCharge(Double expMassToCharge) {
        getRecord().setExpMassToCharge(expMassToCharge);
    }

    public void setExpMassToCharge(String expMassToChargeLabel) {
        getRecord().setExpMassToCharge(expMassToChargeLabel);
    }

    public Double getCalcMassToCharge() {
        return getRecord().getCalcMassToCharge();
    }

    public void setCalcMassToCharge(Double calcMassToCharge) {
        getRecord().setCalcMassToCharge(calcMassToCharge);
    }

    public void setCalcMassToCharge(String calcMassToChargeLabel) {
        getRecord().setCalcMassToCharge(calcMassToChargeLabel);
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

    public Integer getTaxid() {
        return getRecord().getTaxid();
    }

    public void setTaxid(Integer taxid) {
        getRecord().setTaxid(taxid);
    }

    public void setTaxid(String taxidLabel) {
        getRecord().setTaxid(taxidLabel);
    }

    public String getSpecies() {
        return getRecord().getSpecies();
    }

    public void setSpecies(String species) {
        getRecord().setSpecies(species);
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

    public Reliability getReliability() {
        return getRecord().getReliability();
    }

    public void setReliability(Reliability reliability) {
        getRecord().setReliability(reliability);
    }

    public void setReliability(String reliabilityLabel) {
        getRecord().setReliability(reliabilityLabel);
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

    public void setSearchEngineScore(MsRun msRun, SplitList<Param> searchEngineScore) {
        getRecord().setSearchEngineScore(msRun, searchEngineScore);
    }

    public void setSearchEngineScore(String logicalPosition, SplitList<Param> searchEngineScore) {
        getRecord().setSearchEngineScore(logicalPosition, searchEngineScore);
    }

    public void setSearchEngineScore(String logicalPosition, String paramsLabel) {
        getRecord().setSearchEngineScore(logicalPosition, paramsLabel);
    }

    public boolean addSearchEngineScoreParam(MsRun msRun, CVParam param) {
        return getRecord().addSearchEngineScoreParam(msRun, param);
    }

    public void setSearchEngineScore(MsRun msRun, String paramsLabel) {
        getRecord().setSearchEngineScore(msRun, paramsLabel);
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

    public String toString() {
        return getRecord().toString();
    }
    
    

}
