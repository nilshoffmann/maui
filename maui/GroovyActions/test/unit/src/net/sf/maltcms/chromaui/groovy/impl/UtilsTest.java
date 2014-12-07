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
package net.sf.maltcms.chromaui.groovy.impl;

import cross.test.LogMethodName;
import cross.test.SetupLogging;
import java.io.File;
import java.util.UUID;
import junit.framework.Assert;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Nils Hoffmann
 */
public class UtilsTest extends NbTestCase {

    /**
     *
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     *
     */
    @Rule
    public LogMethodName lmn = new LogMethodName();

    /**
     *
     */
    @Rule
    public SetupLogging logging = new SetupLogging();

    /**
     *
     * @param name
     */
    public UtilsTest(String name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public static junit.framework.Test suite() {
        NbModuleSuite.Configuration config = NbModuleSuite.createConfiguration(UtilsTest.class);
        config.enableClasspathModules(true);
        config.enableModules("*");
        config.gui(false);
        return config.suite();
    }

    /**
     * Test of getScriptLocation method, of class Utils.
     */
    @Test
    public void testGetScriptLocation() throws Exception {
        File f;
        final File userdir = folder.newFolder();
        f = new File(new File(userdir, UUID.randomUUID().toString()), "chromauiproject.mpr");
        f.getParentFile().mkdirs();
        f.createNewFile();
        f.deleteOnExit();
        f.getParentFile().deleteOnExit();

        IChromAUIProject cap = mock(IChromAUIProject.class);
        when(cap.getLocation()).thenReturn(FileUtil.toFileObject(f));
        Assert.assertNotNull(cap.getLocation());
        for (FileObject fobj : Utils.getScriptLocations(cap)) {
            Assert.assertTrue(fobj.isFolder());
            Assert.assertTrue(FileUtil.isParentOf(FileUtil.toFileObject(f), fobj));
        }
    }

}
