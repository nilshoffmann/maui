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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.project.api.container.MetaDataContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IMetaDataDescriptor;
import org.openide.util.Exceptions;
import uk.ac.ebi.pride.jmztab.model.Comment;
import uk.ac.ebi.pride.jmztab.model.MZTabColumnFactory;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.Section;
import uk.ac.ebi.pride.jmztab.model.SmallMolecule;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;

/**
 *
 * @author Nils Hoffmann
 */
public class MzTabFileContainer extends MetaDataContainer<IMetaDataDescriptor> {
    private static final long serialVersionUID = -5170938846384192737L;

    
    
    /**
     *
     * @param mzTabFile
     * @return
     */
    public static MzTabFileContainer fromMzTabFile(File mzTabFile) {
        try {
            MZTabFileParser mzfp = new MZTabFileParser(mzTabFile, System.out);
            MZTabFile file = mzfp.getMZTabFile();
            MzTabFileContainer container = new MzTabFileContainer();
            container.setLevel(0);
            MzTabMetaDataContainer mdc = MzTabMetaDataContainer.create(file.getMetadata());
            mdc.setLevel(1);
            container.setMetaData(mdc);
            container.setName(mzTabFile.getName());
            container.setDisplayName(file.getMetadata().getMZTabID());
            container.setShortDescription("MzTab Version: " + file.getMetadata().getMZTabVersion() + " Mode: " + file.getMetadata().getMZTabMode() + " Type: " + file.getMetadata().getMZTabType() + " Description: " + file.getMetadata().getDescription());
            CommentsContainer cc = CommentsContainer.create(file.getComments());
            cc.setLevel(1);
            container.setComments(cc);
            PeptideContainer pc = PeptideContainer.create(file);
            pc.setLevel(1);
            container.setPeptides(pc);
            ProteinContainer proc = ProteinContainer.create(file);
            proc.setLevel(1);
            container.setProteins(proc);
            PsmContainer psmc = PsmContainer.create(file);
            psmc.setLevel(1);
            container.setPsms(psmc);
            SmallMoleculeContainer smc = SmallMoleculeContainer.create(file);
            smc.setLevel(1);
            container.setSmallMolecules(smc);
            return container;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    /**
     *
     * @param container
     * @return
     */
    public static MZTabFile toMzTabFile(MzTabFileContainer container) {
        MZTabFile file = new MZTabFile(MzTabMetaDataContainer.toMetaData(container.getMetaData()));
        file.setSmallMoleculeColumnFactory(MZTabColumnFactory.getInstance(Section.Small_Molecule));
        for (Comment c : CommentsContainer.toComments(container.getComments())) {
            //FIXME: Comment does not support retrieval of original line number
//            file.addComment(c., c);
            Logger.getLogger(MzTabFileContainer.class.getName()).warning("Currently skipping output of comments due to missing line numbers!");
        }
        for (SmallMolecule sm : SmallMoleculeContainer.toSmallMolecules(container.getSmallMolecules())) {
            file.addSmallMolecule(sm);
        }
        return file;
    }

    /**
     *
     * @param f
     */
    @Override
    public void removeMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    /**
     *
     * @param f
     */
    @Override
    public void addMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    /**
     *
     * @param f
     */
    @Override
    public void setMembers(IMetaDataDescriptor... f) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    /**
     *
     * @param members
     */
    @Override
    public void setMembers(Collection<IMetaDataDescriptor> members) {
        throw new UnsupportedOperationException("MzTabFileContainer does not support generic member access!");
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<IMetaDataDescriptor> getMembers() {
        ArrayList<IMetaDataDescriptor> members = new ArrayList<>();
        members.add(getMetaData());
        members.add(getComments());
        members.add(getPeptides());
        members.add(getProteins());
        members.add(getPsms());
        members.add(getSmallMolecules());
        return members;
    }

    private MzTabMetaDataContainer metaData;

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
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
