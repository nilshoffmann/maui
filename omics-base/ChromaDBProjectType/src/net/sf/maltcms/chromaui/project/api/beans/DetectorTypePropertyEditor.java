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
package net.sf.maltcms.chromaui.project.api.beans;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class DetectorTypePropertyEditor extends PropertyEditorSupport {

    public DetectorTypePropertyEditor() {
    }

    @Override
    public String getAsText() {
        IDetectorType id = (IDetectorType) getValue();
        return id.getDetectorType();
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        IDetectorType dt = getDetectorTypeForName(string);
        setValue(dt);
    }

    private IDetectorType getDetectorTypeForName(String name) throws IllegalArgumentException {
        String[] tags = getTags();
        int idx = Arrays.binarySearch(tags, name);
        if (idx < 0) {
            throw new IllegalArgumentException(name + " is not a valid value for detector type! Valid ones are: " + Arrays.deepToString(tags));
        }
        Collection<? extends IDetectorType> detectorTypes = Lookup.getDefault().lookupAll(IDetectorType.class);
        for (IDetectorType dt : detectorTypes) {
            if (name.equals(dt.getDetectorType())) {
                return dt;
            }
        }
        throw new IllegalArgumentException("Could not find a matching detector type for name " + name);
    }

    @Override
    public String[] getTags() {
        Collection<? extends IDetectorType> detectorTypes = Lookup.getDefault().lookupAll(IDetectorType.class);
        List<String> names = new LinkedList<>();
        for (IDetectorType dt : detectorTypes) {
            names.add(dt.getDetectorType());
        }
        return names.toArray(new String[names.size()]);
    }
}
