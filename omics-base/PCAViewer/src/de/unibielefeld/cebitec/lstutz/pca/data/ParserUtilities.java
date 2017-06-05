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
package de.unibielefeld.cebitec.lstutz.pca.data;

import java.util.ArrayList;
import java.util.HashMap;
import org.jogamp.vecmath.Color3f;

public class ParserUtilities {

    public static Color3f parse_hex_color(String hex) {
        Color3f temp = new Color3f();
        temp.x = (((Double) parse_tupel(hex.substring(0, 2))).floatValue() / 255);
        temp.y = (((Double) parse_tupel(hex.substring(2, 4))).floatValue() / 255);
        temp.z = (((Double) parse_tupel(hex.substring(4, 6))).floatValue() / 255);
        return temp;
    }

    private static Double parse_tupel(String tupel) {
        return 16 * worth(tupel.charAt(0)) + worth(tupel.charAt(1));
    }

    private static Double worth(char c) {
        switch (c) {
            case '0':
                return 0.0;
            case '1':
                return 1.0;
            case '2':
                return 2.0;
            case '3':
                return 3.0;
            case '4':
                return 4.0;
            case '5':
                return 5.0;
            case '6':
                return 6.0;
            case '7':
                return 7.0;
            case '8':
                return 8.0;
            case '9':
                return 9.0;
            case 'A':
                return 10.0;
            case 'B':
                return 11.0;
            case 'C':
                return 12.0;
            case 'D':
                return 13.0;
            case 'E':
                return 14.0;
            case 'F':
                return 15.0;
        }
        return -1.0;
    }

    public static HashMap<String, ArrayList<DataModel>> group_data(ArrayList<DataModel> list) {
        HashMap<String, ArrayList<DataModel>> temp = new HashMap<>();
        for (DataModel d : list) {
            String key = d.getColor().x + "," + d.getColor().y + "," + d.getColor().z;
            if (temp.get(key) != null) {
                temp.get(key).add(d);
            } else {
                ArrayList<DataModel> l = new ArrayList<>();
                l.add(d);
                temp.put(key, l);
            }
        }
        return temp;
    }

}
