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
package net.sf.maltcms.chromaui.statistics.view.propertyEditors;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import org.openide.nodes.PropertyEditorRegistration;

/**
 *
 * @author Nils Hoffmann
 */
@PropertyEditorRegistration(targetType = {char[].class})
public class CharArrayPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        char[] d = (char[]) getValue();
        if (d == null) {
            return "No array set";
        }
        return Arrays.toString(d);
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        char[] d;

        try {
            String tmp = string.substring(1);
            tmp = tmp.substring(0, tmp.length() - 1);
            String[] values = tmp.split(",");
            d = new char[values.length];
            for (int i = 0; i < values.length; i++) {
                d[i] = values[i].trim().charAt(0);
            }
//            System.out.println(Arrays.toString(d));
            setValue((char[]) d);
        } catch (Exception pe) {
            throw new IllegalArgumentException(pe);
        }
    }
}
