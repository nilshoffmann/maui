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
package net.sf.maltcms.chromaui.jmztab.ui.api;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.jmztab.ui.nodes.MetaDataChildNodeFactory;
import net.sf.maltcms.chromaui.jmztab.ui.nodes.MetaDataNode;
import net.sf.maltcms.chromaui.jmztab.ui.util.MzTabFileToModelBuilder;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabFileContainer;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import uk.ac.ebi.pride.jmztab.model.Comment;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.PSM;
import uk.ac.ebi.pride.jmztab.model.Peptide;
import uk.ac.ebi.pride.jmztab.model.Protein;
import uk.ac.ebi.pride.jmztab.model.SmallMolecule;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;

@Messages({
    "LBL_MzTab_LOADER=Files of MzTab"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_MzTab_LOADER",
        mimeType = "text/mztab",
        extension = {"mztab", "mzTab"},
        showInFileChooser = {"mzTab"},
        position = 250
)
@MIMEResolver.Registration(
        displayName = "#LBL_MzTab_LOADER",
        resource = "MzTabResolver.xml",
        showInFileChooser = {"mzTab-xml.txt"},
        position = 251
)
@DataObject.Registration(
        mimeType = "text/mztab",
        iconBase = "net/sf/maltcms/chromaui/jmztab/ui/api/MzTab.png",
        displayName = "#LBL_MzTab_LOADER",
        position = 250
)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300
    ),
    @ActionReference(
            path = "Loaders/text/mztab/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400
    )
})
public class MzTabDataObject extends MultiDataObject {

    public MzTabDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/mztab", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(
            displayName = "#LBL_MzTab_EDITOR",
            iconBase = "net/sf/maltcms/chromaui/jmztab/ui/api/MzTab.png",
            mimeType = "text/mztab",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "MzTab",
            position = 1000
    )
    @Messages("LBL_MzTab_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }

    @Override
    protected Node createNodeDelegate() {
        try {
            final MZTabFileParser parser = new MZTabFileParser(FileUtil.toFile(getPrimaryFile()), System.out);
            MZTabFile file = parser.getMZTabFile();
            if (parser.getErrorList().isEmpty()) {
                MzTabFileToModelBuilder mfmb = new MzTabFileToModelBuilder();
                MzTabFileContainer container = mfmb.createFromFile(file);
                Children c = Lookup.getDefault().lookup(INodeFactory.class).createContainerChildren(container, Lookup.EMPTY);
                MzTabDataNode dataNode = new MzTabDataNode(this, c);
                return dataNode;
            } else {
                //TODO change to display parsing errors in IDE Log directly
                Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Errors encountered while parsing file: {0}", getPrimaryFile().getPath());
                for (int i = 0; i < parser.getErrorList().size(); i++) {
                    Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Error {0}:{1}", new Object[]{i, parser.getErrorList().getError(i)});
                }
                return new AbstractNode(Children.LEAF) {

                    @Override
                    public Image getIcon(int type) {
                        return ImageUtilities.createDisabledImage(super.getIcon(type));
                    }

                    @Override
                    public String getShortDescription() {
                        StringBuilder sb = new StringBuilder();
//                        Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Errors encountered while parsing file: {0}", getPrimaryFile().getPath());
                        for (int i = 0; i < parser.getErrorList().size(); i++) {
                            sb.append(parser.getErrorList().getError(i)).append("\n");
                        }
                        return sb.toString();
                    }

                    @Override
                    public String getDisplayName() {
                        return "Error parsing file " + getPrimaryFile().getNameExt();
                    }
                };
            }
        } catch (IOException ex) {
            Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Failed to parse file: " + getPrimaryFile().getPath(), ex);
            return Node.EMPTY;
        }
    }

    private static enum Keys {

        MetaData, Comment, Protein, Peptide, PSM, SmallMolecule
    };

    private static class MzTabChildFactory extends ChildFactory<Keys> {

        private final FileObject fileObject;
        private final MZTabFile mzTabFile;

        public MzTabChildFactory(FileObject fileObject, MZTabFile mzTabFile) {
            this.fileObject = fileObject;
            this.mzTabFile = mzTabFile;
        }

//        private void parseFile(FileObject fobj, boolean refresh) {
//            if (fobj != null) {
//                this.fileObject = fobj;
////                if(refresh) {
////                    FileUtil.refreshFor(FileUtil.toFile(fobj));
////                }
//                DataObject dobj;
//                try {
//                    dobj = DataObject.find(fileObject);
//                    dobj.addPropertyChangeListener(WeakListeners.propertyChange(this, dobj));
//                } catch (DataObjectNotFoundException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//                fileObject.addFileChangeListener(WeakListeners.create(FileChangeListener.class, this, fileObject));
//                final MZTabFileParser parser;
//                try {
//                    parser = new MZTabFileParser(FileUtil.toFile(this.fileObject), System.out);
//                    if (parser.getErrorList().isEmpty()) {
//                        mzTabFile = parser.getMZTabFile();
//                    }
//                } catch (IOException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
//        }
        @Override
        protected boolean createKeys(List<Keys> list) {
//            if (mzTabFile == null) {
//                parseFile(fileObject, false);
//            }
            list.add(Keys.MetaData);
            if (!mzTabFile.getComments().isEmpty()) {
                list.add(Keys.Comment);
            }
            if (!mzTabFile.getProteins().isEmpty()) {
                list.add(Keys.Protein);
            }
            if (!mzTabFile.getPeptides().isEmpty()) {
                list.add(Keys.Peptide);
            }
            if (!mzTabFile.getPSMs().isEmpty()) {
                list.add(Keys.PSM);
            }
            if (!mzTabFile.getSmallMolecules().isEmpty()) {
                list.add(Keys.SmallMolecule);
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(Keys key) {
            switch (key) {
                case MetaData:
                    try {
                        MetaDataNode mdn = new MetaDataNode(mzTabFile.getMetadata(), Children.create(new MetaDataChildNodeFactory(mzTabFile.getMetadata()), true));
                        return mdn;
                    } catch (IntrospectionException ex) {
                        return Node.EMPTY;
                    }
                case Comment:
                    AbstractNode bn = new AbstractNode(Children.create(new ChildFactory<Comment>() {

                        @Override
                        protected boolean createKeys(List<Comment> list) {
                            list.addAll(mzTabFile.getComments());
                            return true;
                        }

                        @Override
                        protected Node createNodeForKey(Comment comment) {
                            BeanNode<Comment> commentNode;
                            try {
                                commentNode = new BeanNode<Comment>(comment);
                                commentNode.setDisplayName(comment.getMsg());
                                commentNode.setShortDescription(comment.toString());
                                return commentNode;
                            } catch (IntrospectionException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            return Node.EMPTY;
                        }
                    }, true));
                    bn.setDisplayName("Comments");
                    bn.setName("Comments");
                    bn.setShortDescription("List of comments associated to this file.");
                    return bn;
                case Protein:
                    AbstractNode pn = new AbstractNode(Children.create(new ChildFactory<Protein>() {

                        @Override
                        protected boolean createKeys(List<Protein> list) {
                            list.addAll(mzTabFile.getProteins());
                            return true;
                        }

                        @Override
                        protected Node createNodeForKey(Protein sm) {
                            BeanNode<Protein> smNode;
                            try {
                                smNode = new BeanNode<Protein>(sm);
                                smNode.setDisplayName(sm.getAccession());
                                smNode.setShortDescription(sm.toString());
                                return smNode;
                            } catch (IntrospectionException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            return Node.EMPTY;
                        }
                    }, true));
                    pn.setDisplayName("Proteins");
                    pn.setName("Proteins");
                    pn.setShortDescription("List of proteins associated to this file.");
                    return pn;
                case Peptide:
                    AbstractNode ppn = new AbstractNode(Children.create(new ChildFactory<Peptide>() {

                        @Override
                        protected boolean createKeys(List<Peptide> list) {
                            list.addAll(mzTabFile.getPeptides());
                            return true;
                        }

                        @Override
                        protected Node createNodeForKey(Peptide sm) {
                            BeanNode<Peptide> smNode;
                            try {
                                smNode = new BeanNode<Peptide>(sm);
                                smNode.setDisplayName(sm.getAccession());
                                smNode.setShortDescription(sm.toString());
                                return smNode;
                            } catch (IntrospectionException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            return Node.EMPTY;
                        }
                    }, true));
                    ppn.setDisplayName("Peptides");
                    ppn.setName("Peptides");
                    ppn.setShortDescription("List of peptides associated to this file.");
                    return ppn;
                case PSM:
                    AbstractNode psmn = new AbstractNode(Children.create(new ChildFactory<PSM>() {

                        @Override
                        protected boolean createKeys(List<PSM> list) {
                            list.addAll(mzTabFile.getPSMs());
                            return true;
                        }

                        @Override
                        protected Node createNodeForKey(PSM sm) {
                            BeanNode<PSM> smNode;
                            try {
                                smNode = new BeanNode<PSM>(sm);
                                smNode.setDisplayName(sm.getAccession());
                                smNode.setShortDescription(sm.toString());
                                return smNode;
                            } catch (IntrospectionException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            return Node.EMPTY;
                        }
                    }, true));
                    psmn.setDisplayName("PSMs");
                    psmn.setName("PSMs");
                    psmn.setShortDescription("List of PSMs associated to this file.");
                    return psmn;
                case SmallMolecule:
                    AbstractNode smn = new AbstractNode(Children.create(new ChildFactory<SmallMolecule>() {

                        @Override
                        protected boolean createKeys(List<SmallMolecule> list) {
                            list.addAll(mzTabFile.getSmallMolecules());
                            return true;
                        }

                        @Override
                        protected Node createNodeForKey(SmallMolecule sm) {
                            BeanNode<SmallMolecule> smNode;
                            try {
                                smNode = new BeanNode<SmallMolecule>(sm);
                                smNode.setDisplayName(sm.getDescription());
                                smNode.setShortDescription(sm.toString());
                                return smNode;
                            } catch (IntrospectionException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            return Node.EMPTY;
                        }
                    }, true));
                    smn.setDisplayName("Small Molecules");
                    smn.setName("Small Molecules");
                    smn.setShortDescription("List of small molecules associated to this file.");
                    return smn;

                default:
                    throw new IllegalStateException("Unhandled state in switch: " + key.name());
            }
        }

//        @Override
//        public void fileFolderCreated(FileEvent fe) {
//
//        }
//
//        @Override
//        public void fileDataCreated(FileEvent fe) {
//            parseFile(fe.getFile(), true);
//            refresh(true);
//        }
//
//        @Override
//        public void fileChanged(FileEvent fe) {
//            parseFile(fe.getFile(), true);
//            refresh(true);
//        }
//
//        @Override
//        public void fileDeleted(FileEvent fe) {
//            parseFile(fe.getFile(), true);
//            refresh(true);
//        }
//
//        @Override
//        public void fileRenamed(FileRenameEvent fre) {
//            parseFile(fre.getFile(), true);
//            refresh(true);
//        }
//
//        @Override
//        public void fileAttributeChanged(FileAttributeEvent fae) {
//            parseFile(fae.getFile(), true);
//            refresh(true);
//        }
//
//        @Override
//        public void propertyChange(PropertyChangeEvent evt) {
//            if (evt.getPropertyName().equals(DataObject.PROP_MODIFIED)) {
//                parseFile(fileObject, true);
//                refresh(true);
//            }
//        }
    }

}
