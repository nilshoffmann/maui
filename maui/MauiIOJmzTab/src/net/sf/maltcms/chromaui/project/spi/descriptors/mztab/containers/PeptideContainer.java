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
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.PeptideDescriptor;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.Peptide;

/**
 *
 * @author Nils Hoffmann
 */
public class PeptideContainer extends BasicMzTabMetaDataContainer<PeptideDescriptor> {
    private static final long serialVersionUID = 7679399920707639250L;

    /**
     *
     * @param mzTabFile
     * @return
     */
    public static PeptideContainer create(MZTabFile mzTabFile) {
        PeptideContainer c = new PeptideContainer();
        c.setLevel(1);
        c.setName("peptides");
        c.setDisplayName("Peptides");
        Collection<Peptide> map = mzTabFile.getPeptides();
        for (Peptide p : map) {
            PeptideDescriptor d = new PeptideDescriptor();
            d.setRecord(p);
            d.setDisplayName(p.getSequence());
            c.addMembers(d);
        }
        return c;
    }
}
