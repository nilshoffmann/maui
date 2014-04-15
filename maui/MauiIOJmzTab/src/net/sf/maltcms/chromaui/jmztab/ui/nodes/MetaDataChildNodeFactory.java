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
package net.sf.maltcms.chromaui.jmztab.ui.nodes;

import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import uk.ac.ebi.pride.jmztab.model.Metadata;
import uk.ac.ebi.pride.jmztab.model.MetadataElement;
import static uk.ac.ebi.pride.jmztab.model.MetadataElement.CV;

public class MetaDataChildNodeFactory extends ChildFactory<MetadataElement> {

    private final Metadata metaData;

    public MetaDataChildNodeFactory(Metadata metaData) {
        this.metaData = metaData;
    }

    @Override
    protected boolean createKeys(List<MetadataElement> list) {
        if (metaData.getTabDescription() != null) {
            list.add(MetadataElement.MZTAB);
        }
        if (metaData.getTitle() != null) {
            list.add(MetadataElement.TITLE);
        }
        if (metaData.getDescription() != null) {
            list.add(MetadataElement.DESCRIPTION);
        }
        if (!metaData.getContactMap().isEmpty()) {
            list.add(MetadataElement.CONTACT);
        }
        if (!metaData.getPublicationMap().isEmpty()) {
            list.add(MetadataElement.PUBLICATION);
        }
        if (!metaData.getInstrumentMap().isEmpty()) {
            list.add(MetadataElement.INSTRUMENT);
        }
        if (!metaData.getSampleProcessingMap().isEmpty()) {
            list.add(MetadataElement.SAMPLE_PROCESSING);
        }
        if (metaData.getQuantificationMethod() != null) {
            list.add(MetadataElement.QUANTIFICATION_METHOD);
        }
        if (!metaData.getSoftwareMap().isEmpty()) {
            list.add(MetadataElement.SOFTWARE);
        }
        if (!metaData.getStudyVariableMap().isEmpty()) {
            list.add(MetadataElement.STUDY_VARIABLE);
        }
        if (!metaData.getSampleMap().isEmpty()) {
            list.add(MetadataElement.SAMPLE);
        }
        if (!metaData.getMsRunMap().isEmpty()) {
            list.add(MetadataElement.MS_RUN);
        }
        if (!metaData.getAssayMap().isEmpty()) {
            list.add(MetadataElement.ASSAY);
        }
        if (!metaData.getCvMap().isEmpty()) {
            list.add(MetadataElement.CV);
        }
        if (!metaData.getCustomList().isEmpty()) {
            list.add(MetadataElement.CUSTOM);
        }
        if (!metaData.getFalseDiscoveryRate().isEmpty()) {
            list.add(MetadataElement.FALSE_DISCOVERY_RATE);
        }
        if (!metaData.getFixedModMap().isEmpty()) {
            list.add(MetadataElement.FIXED_MOD);
        }
        if (!metaData.getVariableModMap().isEmpty()) {
            list.add(MetadataElement.VARIABLE_MOD);
        }
        if (!metaData.getPeptideColUnitList().isEmpty()) {
            list.add(MetadataElement.PEPTIDE);
        }
        if (!metaData.getProteinColUnitList().isEmpty()) {
            list.add(MetadataElement.PROTEIN);
        }
        if (!metaData.getSmallMoleculeColUnitList().isEmpty()) {
            list.add(MetadataElement.SMALL_MOLECULE);
        }
        return true;
    }

    private static class GenericChildFactory<T> extends ChildFactory<T> {

        private final Collection<? extends T> keys;

        public GenericChildFactory(Collection<? extends T> keys) {
            this.keys = keys;
        }

        @Override
        protected boolean createKeys(List<T> list) {
            int i = 0;
            for (T t : keys) {
                if (t != null) {
                    list.add(t);
                } else {
                    Logger.getLogger(MetaDataChildNodeFactory.class.getName()).log(Level.WARNING, "Key " + i + " was null!");
                }
                i++;
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(T key) {
            BeanNode<T> node;
            try {
                node = new BeanNode<T>(key);
                return node;
            } catch (IntrospectionException ex) {
                Logger.getLogger(MetaDataChildNodeFactory.class.getName()).log(Level.WARNING, "Caught IntrospectionException while creating node for key " + key, ex);
            }
            return Node.EMPTY;
        }
    }

    private <T> Node nullSafeBeanNode(T t) throws IntrospectionException {
        if (t == null) {
            return Node.EMPTY;
        }
        return new BeanNode<T>(t);
    }

    private <T> Children nullSafeChildren(Collection<? extends T> c) {
        if (c == null) {
            return Children.LEAF;
        }
        return Children.create(new GenericChildFactory<T>(c), true);
    }

    @Override
    protected Node createNodeForKey(MetadataElement key) {
        try {
            if (key == null) {
                return Node.EMPTY;
            }
            AbstractNode node = null;
            switch (key) {
                case MZTAB:
                    return nullSafeBeanNode(metaData.getTabDescription());
                case TITLE:
                    return nullSafeBeanNode(metaData.getTitle());
                case DESCRIPTION:
                    return nullSafeBeanNode(metaData.getDescription());
                case CONTACT:
                    node = new AbstractNode(nullSafeChildren(metaData.getContactMap().values()));
                    node.setDisplayName("Contacts");
                    return node;
                case PUBLICATION:
                    node = new AbstractNode(nullSafeChildren(metaData.getPublicationMap().values()));
                    node.setDisplayName("Publications");
                    return node;
                case INSTRUMENT:
                    node = new AbstractNode(nullSafeChildren(metaData.getInstrumentMap().values()));
                    node.setDisplayName("Instrument");
                    return node;
                case SAMPLE_PROCESSING:
                    node = new AbstractNode(nullSafeChildren(metaData.getSampleProcessingMap().values()));
                    node.setDisplayName("Sample Processing");
                    return node;
                case QUANTIFICATION_METHOD:
                    //PARAM
                    return nullSafeBeanNode(metaData.getQuantificationMethod());
                case SOFTWARE:
                    node = new AbstractNode(nullSafeChildren(metaData.getSoftwareMap().values()));
                    node.setDisplayName("Software");
                    return node;
                case STUDY_VARIABLE:
                    node = new AbstractNode(nullSafeChildren(metaData.getStudyVariableMap().values()));
                    node.setDisplayName("Study Variables");
                    return node;
                case SAMPLE:
                    node = new AbstractNode(nullSafeChildren(metaData.getSampleMap().values()));
                    node.setDisplayName("Samples");
                    return node;
                case MS_RUN:
                    node = new AbstractNode(nullSafeChildren(metaData.getMsRunMap().values()));
                    node.setDisplayName("MS Run");
                    return node;
                case ASSAY:
                    node = new AbstractNode(nullSafeChildren(metaData.getAssayMap().values()));
                    node.setDisplayName("Assay");
                    return node;
                case COLUNIT:
                    return Node.EMPTY;
                //proteins, peptides, psms and small molecules
//                    return createNodes(metaData.get.values());
                case CV:
                    node = new AbstractNode(nullSafeChildren(metaData.getCvMap().values()));
                    node.setDisplayName("Controlled Vocabularies");
                    return node;
                case CUSTOM:
                    node = new AbstractNode(nullSafeChildren(metaData.getCustomList()));
                    node.setDisplayName("Custom Parameters");
                    return node;
                case FALSE_DISCOVERY_RATE:
                    return nullSafeBeanNode(metaData.getFalseDiscoveryRate());
                case FIXED_MOD:
                    node = new AbstractNode(nullSafeChildren(metaData.getFixedModMap().values()));
                    node.setDisplayName("Fixed Modifications");
                    return node;
                case VARIABLE_MOD:
                    node = new AbstractNode(nullSafeChildren(metaData.getVariableModMap().values()));
                    node.setDisplayName("Variable Modifications");
                    return node;
                case PEPTIDE:
                    node = new AbstractNode(nullSafeChildren(metaData.getPeptideColUnitList()));
                    node.setDisplayName("Peptide Column Units");
                    return node;
                case PROTEIN:
                    node = new AbstractNode(nullSafeChildren(metaData.getProteinColUnitList()));
                    node.setDisplayName("Protein Column Units");
                    return node;
                case SMALL_MOLECULE:
                    node = new AbstractNode(nullSafeChildren(metaData.getSmallMoleculeColUnitList()));
                    node.setDisplayName("Small Molecule Column Units");
                    return node;
                default:
                    return Node.EMPTY;
//                    throw new AssertionError();
            }
        } catch (IntrospectionException ie) {
            Logger.getLogger(MetaDataChildNodeFactory.class.getName()).log(Level.WARNING, "Caught IntrospectionException while creating node for key " + key.name(), ie);
        }
        return Node.EMPTY;
    }
}
