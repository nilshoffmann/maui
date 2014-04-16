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

import java.util.Collection;
import net.sf.maltcms.chromaui.project.api.container.MetaDataContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.PsmDescriptor;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.PSM;

public class PsmContainer extends MetaDataContainer<PsmDescriptor> {

    public static PsmContainer create(MZTabFile mzTabFile) {
        PsmContainer c = new PsmContainer();
        c.setName("psms");
        c.setDisplayName("PSMs");
        Collection<PSM> map = mzTabFile.getPSMs();
        for (PSM p : map) {
            PsmDescriptor d = new PsmDescriptor();
            d.setRecord(p);
            d.setDisplayName(p.getAccession()==null?"NA":p.getAccession());
            c.addMembers(d);
        }
        return c;
    }
}
