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

public class MzTabFileContainer extends MetaDataContainer<IMetaDataDescriptor> {

    @Override
    public void removeMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    @Override
    public void addMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    @Override
    public void setMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    @Override
    public void setMembers(Collection<IMetaDataDescriptor> members) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    @Override
    public Collection<IMetaDataDescriptor> getMembers() {
        ArrayList<IMetaDataDescriptor> members = new ArrayList<IMetaDataDescriptor>();
        members.add(getMetaData());
        members.add(getComments());
        members.add(getPeptides());
        members.add(getProteins());
        members.add(getPsms());
        members.add(getSmallMolecules());
        return members;
    }

    private MzTabMetaDataContainer metaData;

    public static final String PROP_METADATA = "metaData";

    /**
     * Get the value of metaData
     *
     * @return the value of metaData
     */
    public MzTabMetaDataContainer getMetaData() {
        activate(ActivationPurpose.READ);
        return metaData;
    }

    /**
     * Set the value of metaData
     *
     * @param metaData new value of metaData
     */
    public void setMetaData(MzTabMetaDataContainer metaData) {
        activate(ActivationPurpose.WRITE);
        MzTabMetaDataContainer oldMetaData = this.metaData;
        this.metaData = metaData;
        getPropertyChangeSupport().firePropertyChange(PROP_METADATA, oldMetaData, metaData);
    }

    private CommentsContainer comments;

    public static final String PROP_COMMENTS = "comments";

    /**
     * Get the value of comments
     *
     * @return the value of comments
     */
    public CommentsContainer getComments() {
        activate(ActivationPurpose.READ);
        return comments;
    }

    /**
     * Set the value of comments
     *
     * @param comments new value of comments
     */
    public void setComments(CommentsContainer comments) {
        activate(ActivationPurpose.WRITE);
        CommentsContainer oldComments = this.comments;
        this.comments = comments;
        getPropertyChangeSupport().firePropertyChange(PROP_COMMENTS, oldComments, comments);
    }

    private ProteinContainer proteins;

    public static final String PROP_PROTEINS = "proteins";

    /**
     * Get the value of proteins
     *
     * @return the value of proteins
     */
    public ProteinContainer getProteins() {
        activate(ActivationPurpose.READ);
        return proteins;
    }

    /**
     * Set the value of proteins
     *
     * @param proteins new value of proteins
     */
    public void setProteins(ProteinContainer proteins) {
        activate(ActivationPurpose.WRITE);
        ProteinContainer oldProteins = this.proteins;
        this.proteins = proteins;
        getPropertyChangeSupport().firePropertyChange(PROP_PROTEINS, oldProteins, proteins);
    }

    private PeptideContainer peptides;

    public static final String PROP_PEPTIDES = "peptides";

    /**
     * Get the value of peptides
     *
     * @return the value of peptides
     */
    public PeptideContainer getPeptides() {
        activate(ActivationPurpose.READ);
        return peptides;
    }

    /**
     * Set the value of peptides
     *
     * @param peptides new value of peptides
     */
    public void setPeptides(PeptideContainer peptides) {
        activate(ActivationPurpose.WRITE);
        PeptideContainer oldPeptides = this.peptides;
        this.peptides = peptides;
        getPropertyChangeSupport().firePropertyChange(PROP_PEPTIDES, oldPeptides, peptides);
    }

    private PsmContainer psms;

    public static final String PROP_PSMS = "psms";

    /**
     * Get the value of psms
     *
     * @return the value of psms
     */
    public PsmContainer getPsms() {
        activate(ActivationPurpose.READ);
        return psms;
    }

    /**
     * Set the value of psms
     *
     * @param psms new value of psms
     */
    public void setPsms(PsmContainer psms) {
        activate(ActivationPurpose.WRITE);
        PsmContainer oldPsms = this.psms;
        this.psms = psms;
        getPropertyChangeSupport().firePropertyChange(PROP_PSMS, oldPsms, psms);
    }

    private SmallMoleculeContainer smallMolecules;

    public static final String PROP_SMALLMOLECULES = "smallMolecules";

    /**
     * Get the value of smallMolecules
     *
     * @return the value of smallMolecules
     */
    public SmallMoleculeContainer getSmallMolecules() {
        activate(ActivationPurpose.READ);
        return smallMolecules;
    }

    /**
     * Set the value of smallMolecules
     *
     * @param smallMolecules new value of smallMolecules
     */
    public void setSmallMolecules(SmallMoleculeContainer smallMolecules) {
        activate(ActivationPurpose.WRITE);
        SmallMoleculeContainer oldSmallMolecules = this.smallMolecules;
        this.smallMolecules = smallMolecules;
        getPropertyChangeSupport().firePropertyChange(PROP_SMALLMOLECULES, oldSmallMolecules, smallMolecules);
    }

}
