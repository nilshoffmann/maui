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
package maltcms.ui.project;

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Mathias Wilhelm
 */
public class SimpleChromAProjectInformation implements ProjectInformation {

    /**
     *
     */
    public static final String ICON = "maltcms/ui/project/resources/MaltcmsWorkflowResult.png";
    private Icon i = ImageUtilities.image2Icon(ImageUtilities.loadImage(ICON));
    private SimpleChromAProject project;

    /**
     *
     * @param project
     */
    public SimpleChromAProjectInformation(SimpleChromAProject project) {
        this.project = project;
    }

    /**
     *
     * @return
     */
    @Override
    public Icon getIcon() {
        return this.i;
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return project.getProjectDirectory().getName();
    }

    /**
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return getName();
    }

    /**
     *
     * @param pcl
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        //do nothing, won't change
    }

    /**
     *
     * @param pcl
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        //do nothing, won't change
    }

    /**
     *
     * @return
     */
    @Override
    public Project getProject() {
        return this.project;
    }
}
