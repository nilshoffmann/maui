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
import uk.ac.ebi.pride.jmztab.model.Modification;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Protein;
import uk.ac.ebi.pride.jmztab.model.Reliability;
import uk.ac.ebi.pride.jmztab.model.SplitList;

/**
 *
 * @author Nils Hoffmann
 */
public class ProteinDescriptor extends AMzTabRecordDescriptor<Protein> {
    private static final long serialVersionUID = -3137781295753546869L;

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
    public String getDescription() {
        return getRecord().getDescription();
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        getRecord().setDescription(description);
    }

    /**
     *
     * @return
     */
    public Integer getTaxid() {
        return getRecord().getTaxid();
    }

    /**
     *
     * @param taxid
     */
    public void setTaxid(Integer taxid) {
        getRecord().setTaxid(taxid);
    }

    /**
     *
     * @param taxid
     */
    public void setTaxid(String taxid) {
        getRecord().setTaxid(taxid);
    }

    /**
     *
     * @return
     */
    public String getSpecies() {
        return getRecord().getSpecies();
    }

    /**
     *
     * @param species
     */
    public void setSpecies(String species) {
        getRecord().setSpecies(species);
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
     * @param searchEngine
     */
    public void setSearchEngine(SplitList<Param> searchEngine) {
        getRecord().setSearchEngine(searchEngine);
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
     * @param searchEngineLabel
     */
    public void setSearchEngine(String searchEngineLabel) {
        getRecord().setSearchEngine(searchEngineLabel);
    }

    /**
     * @param id
     * @param bestSearchEngineScore
     */
    public void setBestSearchEngineScore(Integer id, Double bestSearchEngineScore) {
        getRecord().setBestSearchEngineScore(id, bestSearchEngineScore);
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
     * @param msRun
     * @return
     */
    public Integer getNumPSMs(MsRun msRun) {
        return getRecord().getNumPSMs(msRun);
    }

    /**
     *
     * @param logicalPosition
     * @param numPSMs
     */
    public void setNumPSMs(String logicalPosition, Integer numPSMs) {
        getRecord().setNumPSMs(logicalPosition, numPSMs);
    }

    /**
     *
     * @param msRun
     * @param numPSMs
     */
    public void setNumPSMs(MsRun msRun, Integer numPSMs) {
        getRecord().setNumPSMs(msRun, numPSMs);
    }

    /**
     *
     * @param logicalPosition
     * @param numPSMsLabel
     */
    public void setNumPSMs(String logicalPosition, String numPSMsLabel) {
        getRecord().setNumPSMs(logicalPosition, numPSMsLabel);
    }

    /**
     *
     * @param msRun
     * @param numPSMsLabel
     */
    public void setNumPSMs(MsRun msRun, String numPSMsLabel) {
        getRecord().setNumPSMs(msRun, numPSMsLabel);
    }

    /**
     *
     * @param msRun
     * @return
     */
    public Integer getNumPeptidesDistinct(MsRun msRun) {
        return getRecord().getNumPeptidesDistinct(msRun);
    }

    /**
     *
     * @param msRun
     * @param numPeptidesDistinct
     */
    public void setNumPeptidesDistinct(MsRun msRun, Integer numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(msRun, numPeptidesDistinct);
    }

    /**
     *
     * @param logicalPosition
     * @param numPeptidesDistinct
     */
    public void setNumPeptidesDistinct(String logicalPosition, Integer numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(logicalPosition, numPeptidesDistinct);
    }

    /**
     *
     * @param msRun
     * @param numPeptidesDistinct
     */
    public void setNumPeptidesDistinct(MsRun msRun, String numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(msRun, numPeptidesDistinct);
    }

    /**
     *
     * @param logicalPosition
     * @param numPeptidesDistinct
     */
    public void setNumPeptidesDistinct(String logicalPosition, String numPeptidesDistinct) {
        getRecord().setNumPeptidesDistinct(logicalPosition, numPeptidesDistinct);
    }

    /**
     *
     * @param msRun
     * @return
     */
    public Integer getNumPeptidesUnique(MsRun msRun) {
        return getRecord().getNumPeptidesUnique(msRun);
    }

    /**
     *
     * @param logicalPosition
     * @param numPeptidesUnique
     */
    public void setNumPeptidesUnique(String logicalPosition, Integer numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(logicalPosition, numPeptidesUnique);
    }

    /**
     *
     * @param msRun
     * @param numPeptidesUnique
     */
    public void setNumPeptidesUnique(MsRun msRun, Integer numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(msRun, numPeptidesUnique);
    }

    /**
     *
     * @param logicalPosition
     * @param numPeptidesUnique
     */
    public void setNumPeptidesUnique(String logicalPosition, String numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(logicalPosition, numPeptidesUnique);
    }

    /**
     *
     * @param msRun
     * @param numPeptidesUnique
     */
    public void setNumPeptidesUnique(MsRun msRun, String numPeptidesUnique) {
        getRecord().setNumPeptidesUnique(msRun, numPeptidesUnique);
    }

    /**
     *
     * @return
     */
    public SplitList<String> getAmbiguityMembers() {
        return getRecord().getAmbiguityMembers();
    }

    /**
     *
     * @param member
     * @return
     */
    public boolean addAmbiguityMembers(String member) {
        return getRecord().addAmbiguityMembers(member);
    }

    /**
     *
     * @param ambiguityMembers
     */
    public void setAmbiguityMembers(SplitList<String> ambiguityMembers) {
        getRecord().setAmbiguityMembers(ambiguityMembers);
    }

    /**
     *
     * @param ambiguityMembersLabel
     */
    public void setAmbiguityMembers(String ambiguityMembersLabel) {
        getRecord().setAmbiguityMembers(ambiguityMembersLabel);
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
    public SplitList<String> getGOTerms() {
        return getRecord().getGOTerms();
    }

    /**
     *
     * @param term
     * @return
     */
    public boolean addGOTerm(String term) {
        return getRecord().addGOTerm(term);
    }

    /**
     *
     * @param goTerms
     */
    public void setGOTerms(SplitList<String> goTerms) {
        getRecord().setGOTerms(goTerms);
    }

    /**
     *
     * @param goTermsLabel
     */
    public void setGOTerms(String goTermsLabel) {
        getRecord().setGOTerms(goTermsLabel);
    }

    /**
     *
     * @return
     */
    public Double getProteinCoverage() {
        return getRecord().getProteinCoverage();
    }

    /**
     *
     * @param proteinConverage
     */
    public void setProteinConverage(Double proteinConverage) {
        getRecord().setProteinConverage(proteinConverage);
    }

    /**
     *
     * @param proteinConverageLabel
     */
    public void setProteinConverage(String proteinConverageLabel) {
        getRecord().setProteinConverage(proteinConverageLabel);
    }

    @Override
    public String toString() {
        return getRecord().toString();
    }

}
