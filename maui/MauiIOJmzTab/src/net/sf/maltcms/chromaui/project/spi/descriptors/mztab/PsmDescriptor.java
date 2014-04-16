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
import uk.ac.ebi.pride.jmztab.model.PSM;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Reliability;
import uk.ac.ebi.pride.jmztab.model.SpectraRef;
import uk.ac.ebi.pride.jmztab.model.SplitList;

public class PsmDescriptor extends AMzTabRecordDescriptor<PSM> {

    public String getSequence() {
        return getRecord().getSequence();
    }

    public void setSequence(String sequence) {
        getRecord().setSequence(sequence);
    }

    public String getPSM_ID() {
        return getRecord().getPSM_ID();
    }

    public void setPSM_ID(Integer psmId) {
        getRecord().setPSM_ID(psmId);
    }

    public void setPSM_ID(String psmIdLabel) {
        getRecord().setPSM_ID(psmIdLabel);
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

    public SplitList<Param> getSearchEngineScore() {
        return getRecord().getSearchEngineScore();
    }

    public boolean addSearchEngineScoreParam(Param param) {
        return getRecord().addSearchEngineScoreParam(param);
    }

    public boolean addSearchEngineScoreParam(String paramLabel) {
        return getRecord().addSearchEngineScoreParam(paramLabel);
    }

    public void setSearchEngineScore(SplitList<Param> searchEngineScore) {
        getRecord().setSearchEngineScore(searchEngineScore);
    }

    public void setSearchEngineScore(String searchEngineScoreLabel) {
        getRecord().setSearchEngineScore(searchEngineScoreLabel);
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

    public Integer getCharge() {
        return getRecord().getCharge();
    }

    public void setCharge(Integer charge) {
        getRecord().setCharge(charge);
    }

    public void setCharge(String chargeLabel) {
        getRecord().setCharge(chargeLabel);
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

    public String getPre() {
        return getRecord().getPre();
    }

    public void setPre(String pre) {
        getRecord().setPre(pre);
    }

    public String getPost() {
        return getRecord().getPost();
    }

    public void setPost(String post) {
        getRecord().setPost(post);
    }

    public String getStart() {
        return getRecord().getStart();
    }

    public void setStart(String start) {
        getRecord().setStart(start);
    }

    public String getEnd() {
        return getRecord().getEnd();
    }

    public void setEnd(String end) {
        getRecord().setEnd(end);
    }

    public String toString() {
        return getRecord().toString();
    }

}
