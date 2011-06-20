/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
