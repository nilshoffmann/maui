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

import java.util.SortedMap;
import net.sf.maltcms.chromaui.project.spi.descriptors.mztab.SoftwareDescriptor;
import uk.ac.ebi.pride.jmztab.model.Metadata;
import uk.ac.ebi.pride.jmztab.model.Software;

/**
 *
 * @author Nils Hoffmann
 */
public class SoftwareContainer extends BasicMzTabMetaDataContainer<SoftwareDescriptor> {
    private static final long serialVersionUID = 7284489697138381757L;

    /**
     *
     * @param metadata
     * @return
     */
    public static SoftwareContainer create(Metadata metadata) {
        SoftwareContainer c = new SoftwareContainer();
        c.setLevel(2);
        c.setName("software");
        c.setDisplayName("Software");
        SortedMap<Integer, Software> map = metadata.getSoftwareMap();
        for (Integer key : map.keySet()) {
            SoftwareDescriptor d = new SoftwareDescriptor();
            d.setSoftware(map.get(key));
            c.addMembers(d);
        }
        return c;
    }

}
