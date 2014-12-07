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
package net.sf.maltcms.chromaui.project.api.extimport;

import java.io.File;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

/**
 *
 * @author Nils Hoffmann
 */
public interface IFileResultFinder {

    /**
     * Return an array of files, representing processing results.
     * @param outputDirectory the output directory, e.g. the Maltcms workflow output directory.
     * @return an array of files. Can be empty.
     */
    File[] getResults(File outputDirectory);
    
    /**
     * Return a descriptor for display in dialogs.
     * @param project the IChromAUIProject associated to the results.
     * @param outputDirectory the output directory to check for importable results.
     * @return the IFileBasedToolResultDescriptor, providing a Runnable to execute on the results.
     */
    IFileBasedToolResultDescriptor createDescriptor(IChromAUIProject project, File outputDirectory);
    
}
