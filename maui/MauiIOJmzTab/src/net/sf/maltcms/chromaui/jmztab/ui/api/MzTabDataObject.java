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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.jmztab.ui.util.MzTabFileToModelBuilder;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabFileContainer;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;
import org.openide.windows.TopComponent;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;

/**
 *
 * @author Nils Hoffmann
 */
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
public class MzTabDataObject extends MultiDataObject implements FileChangeListener {

    /**
     *
     * @param pf
     * @param loader
     * @throws DataObjectExistsException
     * @throws IOException
     */
    public MzTabDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/mztab", true);
        pf.addFileChangeListener(WeakListeners.create(FileChangeListener.class, this, pf));
    }

    /**
     *
     * @return
     */
    @Override
    protected int associateLookup() {
        return 1;
    }

    /**
     *
     * @param lkp
     * @return
     */
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

    /**
     *
     * @return
     */
    @Override
    protected Node createNodeDelegate() {
        MzTabDataNode mzt = new MzTabDataNode(this, Children.LEAF);
        return mzt;
//        try {
//            final MZTabFileParser parser = new MZTabFileParser(FileUtil.toFile(getPrimaryFile()), System.out);
//            MZTabFile file = parser.getMZTabFile();
//            if (parser.getErrorList().isEmpty()) {
//                MzTabFileToModelBuilder mfmb = new MzTabFileToModelBuilder();
//                MzTabFileContainer container = mfmb.createFromFile(file);
//                Children c = Lookup.getDefault().lookup(INodeFactory.class).createContainerChildren(container, Lookup.EMPTY);
//                MzTabDataNode dataNode = new MzTabDataNode(this, c);
//                return dataNode;
//            } else {
//                //TODO change to display parsing errors in IDE Log directly
//                Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Errors encountered while parsing file: {0}", getPrimaryFile().getPath());
//                for (int i = 0; i < parser.getErrorList().size(); i++) {
//                    Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Error {0}:{1}", new Object[]{i, parser.getErrorList().getError(i)});
//                }
//                MzTabDataNode mzt = new MzTabDataNode(this, Children.LEAF);
//                mzt.setDisplayName("Error parsing file " + getPrimaryFile().getNameExt());
//                FilterNode fn = new FilterNode(mzt) {
//
//                    @Override
//                    public Image getIcon(int type) {
//                        return ImageUtilities.createDisabledImage(super.getIcon(type));
//                    }
//
//                };
//                return fn;
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(MzTabDataObject.class.getName()).log(Level.SEVERE, "Failed to parse file: " + getPrimaryFile().getPath(), ex);
//            return Node.EMPTY;
//        }
    }

    /**
     *
     * @param fe
     */
    @Override
    public void fileFolderCreated(FileEvent fe) {

    }

    /**
     *
     * @param fe
     */
    @Override
    public void fileDataCreated(FileEvent fe) {
//        FileUtil.refreshFor(FileUtil.toFile(fe.getFile()));
    }

    /**
     *
     * @param fe
     */
    @Override
    public void fileChanged(FileEvent fe) {
        FileUtil.refreshFor(FileUtil.toFile(fe.getFile()));
    }

    /**
     *
     * @param fe
     */
    @Override
    public void fileDeleted(FileEvent fe) {
//        FileUtil.refreshFor(FileUtil.toFile(fe.getFile()));
    }

    /**
     *
     * @param fre
     */
    @Override
    public void fileRenamed(FileRenameEvent fre) {

    }

    /**
     *
     * @param fae
     */
    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {

    }

}
