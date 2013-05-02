/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.spi.wizard;

import java.io.File;
import java.net.URI;

/**
 *
 * @author nilshoffmann
 */
public class Util {

    public static Object[] uriStringsToFile(URI[] u) {
        Object[] o = new Object[u.length];
        int i = 0;
        for (URI uri : u) {
            o[i++] = new File(uri);
        }
        return o;
    }

    public static URI[] filesToURIString(Object[] o) {
        URI[] s = new URI[o.length];
        int i = 0;
        for (Object obj : o) {
            s[i++] = ((File) obj).toURI();
        }
        return s;
    }
    
}
