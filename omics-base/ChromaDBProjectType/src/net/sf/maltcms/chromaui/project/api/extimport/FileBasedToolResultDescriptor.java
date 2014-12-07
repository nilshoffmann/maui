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

import com.db4o.activation.ActivationPurpose;
import java.io.File;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;

/**
 *
 * @author Nils Hoffmann
 */
public class FileBasedToolResultDescriptor extends ABasicDescriptor implements IFileBasedToolResultDescriptor {
    
    private File outputDirectory;

    /**
     *
     * @return
     */
    @Override
    public File getOutputDirectory() {
        activate(ActivationPurpose.READ);
        return this.outputDirectory;
    }

    /**
     *
     * @param outputDirectory
     */
    @Override
    public void setOutputDirectory(File outputDirectory) {
        activate(ActivationPurpose.WRITE);
        File old = this.outputDirectory;
        this.outputDirectory = outputDirectory;
        firePropertyChange(PROP_OUTPUTDIRECTORY, old, outputDirectory);
    }
    
    private volatile AProgressAwareRunnable progressAwareRunnable;
    
    /**
     *
     * @return
     */
    @Override
    public AProgressAwareRunnable getProgressAwareRunnable() {
        activate(ActivationPurpose.READ);
        return this.progressAwareRunnable;
    }

    /**
     *
     * @param progressAwareRunnable
     */
    @Override
    public void setProgressAwareRunnable(AProgressAwareRunnable progressAwareRunnable) {
        activate(ActivationPurpose.WRITE);
        AProgressAwareRunnable old = this.progressAwareRunnable;
        this.progressAwareRunnable = progressAwareRunnable;
        firePropertyChange(PROP_PROGRESSAWARERUNNABLE, old, progressAwareRunnable);
    }
}
