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
package net.sf.maltcms.chromaui.project.spi.wizard;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.Test;

/**
 *
 * @author Nils Hoffmann
 */
public class UncPathException {
    
    @Test
    public void testUncPathException() throws URISyntaxException, MalformedURLException {
        File projdir = new File("\\\\Server\\C$\\path\\to\\share");
        URL projectFileUrl = new File(projdir, "project.db4o").toURI().toURL();
        File projdir1 = new File("\\\\user@Server:234\\path\\to\\share");
        URL projectFileUrl1 = new File(projdir1, "project.db4o").toURI().toURL();
        File projdir2 = new File("Z:\\Server\\path\\to\\share");
        URL projectFileUrl2 = new File(projdir2, "project.db4o").toURI().toURL();
        File projdir3 = new File("Z:\\user@Server\\path\\to\\share");
        URL projectFileUrl3 = new File(projdir3, "project.db4o").toURI().toURL();
    }
}
