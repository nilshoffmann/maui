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
import uk.ac.ebi.pride.jmztab.model.Protein;
import uk.ac.ebi.pride.jmztab.model.Reliability;
import uk.ac.ebi.pride.jmztab.model.SplitList;

public class ProteinDescriptor extends AMzTabRecordDescriptor<Protein> {

    public String getAccession() {
        return getRecord().getAccession();
    }

    public void setAccession(String accession) {
        getRecord().setAccession(accession);
    }

    public String getDescription() {
        return getRecord().getDescription();
    }

    public void setDescription(String description) {
        getRecord().setDescription(description);
    }

    public Integer getTaxid() {
        return getRecord().getTaxid();
    }

    public void setTaxid(Integer taxid) {
        getRecord().setTaxid(taxid);
    }

    public void setTaxid(String taxid) {
        getRecord().setTaxid(taxid);
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

    public SplitList<Param> getSearchEngine() {
        return getRecord().getSearchEngine();
    }

    public void setSearchEngine(SplitList<Param> searchEngine) {
        getRecord().setSearchEngine(searchEngine);
    }

    public boolean addSearchEngineParam(Param param) {
        return getRecord().addSearchEngineParam(param);
    }

    public boolean addSearchEngineParam(String paramLabel) {
        return getRecord().addSearchEngineParam(paramLabel);
    }

    public void setSearchEngine(String searchEngineLabel) {
        getRecord().setSearchEngine(searchEngineLabel);
    }

    public SplitList<Param> getBestSearchEngineScore() {
        return getRecord().getBestSearchEngineScore();
    }

    public void setBestSearchEngineScore(SplitList<Param> bestSearchEngineScore) {
        getRecord().setBestSearchEngineScore(bestSearchEngineScore);
    }

    public boolean addBestSearchEngineScoreParam(Param param) {
        return getRecord().addBestSearchEngineScoreParam(param);
    }

    public boolean addBestSearchEngineScoreParam(String paramLabel) {
        return getRecord().addBestSearchEngineScoreParam(paramLabel);
    }

    public void setBestSearchEngineScore(String searchEngineScoreLabel) {
        getRecord().setBestSearchEngineScore(searchEngineScoreLabel);
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

    public Integer getNumPSMs(MsRun msRun) {
        return getRecord().getNumPSMs(msRun);
    }

    public void setNumPSMs(String logicalPosition, Integer numPSMs) {
        getRecord().setNumPSMs(logicalPosition, numPSMs);
    }

    public void setNumPSMs(MsRun msRun, Integer numPSMs) {
        getRecord().setNumPSMs(msRun, numPSMs);
    }

    public void setNumPSMs(String logicalPosition, String numPSMsLabel) {
        getRecord().setNumPSMs(logicalPosition, numPSMsLabel);
    }

    public void setNumPSMs(MsRun msRun, String numPSMsLabel) {
        getRecord().setNumPSMs(msRun, numPSMsLabel);
    }

    public Integer getNumPeptidesDistinct(MsRun msRun) {
        return getRecord().getNumPeptidesDistinct(msRun);
    }

    public void setNumPeptidesDistinct(MsRun msRun, Integer numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(msRun, numPeptidesDistinct);
    }

    public void setNumPeptidesDistinct(String logicalPosition, Integer numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(logicalPosition, numPeptidesDistinct);
    }

    public void setNumPeptidesDistinct(MsRun msRun, String numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(msRun, numPeptidesDistinct);
    }

    public void setNumPeptidesDistinct(String logicalPosition, String numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(logicalPosition, numPeptidesDistinct);
    }

    public Integer getNumPeptidesUnique(MsRun msRun) {
        return getRecord().getNumPeptidesUnique(msRun);
    }

    public void setNumPeptidesUnique(String logicalPosition, Integer numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(logicalPosition, numPeptidesUnique);
    }

    public void setNumPeptidesUnique(MsRun msRun, Integer numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(msRun, numPeptidesUnique);
    }

    public void setNumPeptidesUnique(String logicalPosition, String numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(logicalPosition, numPeptidesUnique);
    }

    public void setNumPeptidesUnique(MsRun msRun, String numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(msRun, numPeptidesUnique);
    }

    public SplitList<String> getAmbiguityMembers() {
        return getRecord().getAmbiguityMembers();
    }

    public boolean addAmbiguityMembers(String member) {
        return getRecord().addAmbiguityMembers(member);
    }

    public void setAmbiguityMembers(SplitList<String> ambiguityMembers) {
        getRecord().setAmbiguityMembers(ambiguityMembers);
    }

    public void setAmbiguityMembers(String ambiguityMembersLabel) {
        getRecord().setAmbiguityMembers(ambiguityMembersLabel);
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

    public URI getURI() {
        return getRecord().getURI();
    }

    public void setURI(URI uri) {
        getRecord().setURI(uri);
    }

    public void setURI(String uriLabel) {
        getRecord().setURI(uriLabel);
    }

    public SplitList<String> getGOTerms() {
        return getRecord().getGOTerms();
    }

    public boolean addGOTerm(String term) {
        return getRecord().addGOTerm(term);
    }

    public void setGOTerms(SplitList<String> goTerms) {
        getRecord().setGOTerms(goTerms);
    }

    public void setGOTerms(String goTermsLabel) {
        getRecord().setGOTerms(goTermsLabel);
    }

    public Double getProteinCoverage() {
        return getRecord().getProteinCoverage();
    }

    public void setProteinConverage(Double proteinConverage) {
        getRecord().setProteinConverage(proteinConverage);
    }

    public void setProteinConverage(String proteinConverageLabel) {
        getRecord().setProteinConverage(proteinConverageLabel);
    }

    public String toString() {
        return getRecord().toString();
    }

}
