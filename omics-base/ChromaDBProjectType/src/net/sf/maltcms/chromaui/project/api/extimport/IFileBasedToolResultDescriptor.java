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
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;

/**
 *
 * @author Nils Hoffmann
 */
public interface IFileBasedToolResultDescriptor extends IBasicDescriptor{
    final String PROP_OUTPUTDIRECTORY = "outputDirectory";
    /**
     *
     * @return
     */
    File getOutputDirectory();

    /**
     *
     * @param outputDirectory
     */
    void setOutputDirectory(File outputDirectory);
    
    final String PROP_PROGRESSAWARERUNNABLE = "progressAwareRunnable";
    
    void setProgressAwareRunnable(AProgressAwareRunnable runnable);
    
    AProgressAwareRunnable getProgressAwareRunnable();
    
}
