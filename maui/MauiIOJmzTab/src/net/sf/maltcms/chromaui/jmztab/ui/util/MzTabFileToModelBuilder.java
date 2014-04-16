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
package net.sf.maltcms.chromaui.jmztab.ui.util;

import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.CommentsContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabFileContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.MzTabMetaDataContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.PeptideContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.ProteinContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.PsmContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.containers.SmallMoleculeContainer;
import org.openide.util.NotImplementedException;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;

/**
 *
 * @author Nils Hoffmann
 */
public class MzTabFileToModelBuilder {

    public MzTabFileContainer createFromFile(MZTabFile file) {
        MzTabFileContainer container = new MzTabFileContainer();
        container.setComments(CommentsContainer.create(file.getComments()));
        container.setMetaData(MzTabMetaDataContainer.create(file.getMetadata()));
        container.setPsms(PsmContainer.create(file));
        container.setProteins(ProteinContainer.create(file));
        container.setPeptides(PeptideContainer.create(file));
        container.setSmallMolecules(SmallMoleculeContainer.create(file));
        return container;
    }

    public MzTabFileContainer createFromProject(IChromAUIProject project) {
        throw new NotImplementedException();
    }

}
