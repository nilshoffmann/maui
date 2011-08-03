/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.project;

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.ImageUtilities;

/**
 *
 * @author mw
 */
public class SimpleChromAProjectInformation implements ProjectInformation {

    public static final String ICON = "maltcms/ui/project/resources/MaltcmsWorkflowResult.png";
    private Icon i = ImageUtilities.image2Icon(ImageUtilities.loadImage(ICON));
    private SimpleChromAProject project;

    public SimpleChromAProjectInformation(SimpleChromAProject project) {
        this.project = project;
    }

    @Override
    public Icon getIcon() {
        return this.i;
    }

    @Override
    public String getName() {
        return project.getProjectDirectory().getName();
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        //do nothing, won't change
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        //do nothing, won't change
    }

    @Override
    public Project getProject() {
        return this.project;
    }
}
