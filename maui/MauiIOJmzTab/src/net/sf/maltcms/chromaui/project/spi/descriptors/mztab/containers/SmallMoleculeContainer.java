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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.SmallMoleculeDescriptor;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.SmallMolecule;

/**
 *
 * @author Nils Hoffmann
 */
public class SmallMoleculeContainer extends BasicMzTabMetaDataContainer<SmallMoleculeDescriptor> {
    private static final long serialVersionUID = -6030991595932627627L;

    /**
     *
     * @param smc
     * @return
     */
    public static List<SmallMolecule> toSmallMolecules(SmallMoleculeContainer smc) {
        List<SmallMolecule> l = new ArrayList<>();
        for (SmallMoleculeDescriptor smd : smc.getMembers()) {
            l.add(smd.getRecord());
        }
        return l;
    }

    /**
     *
     * @param mzTabFile
     * @return
     */
    public static SmallMoleculeContainer create(MZTabFile mzTabFile) {
        SmallMoleculeContainer c = new SmallMoleculeContainer();
        c.setLevel(1);
        c.setName("smallMolecules");
        c.setDisplayName("Small Molecules");
        Collection<SmallMolecule> map = mzTabFile.getSmallMolecules();
        for (SmallMolecule p : map) {
            SmallMoleculeDescriptor d = new SmallMoleculeDescriptor();
            d.setRecord(p);
            d.setDisplayName(p.getDescription());
            c.addMembers(d);
        }
        return c;
    }
}
