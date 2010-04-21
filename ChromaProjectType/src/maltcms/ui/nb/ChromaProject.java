/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.nb;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.sourceforge.maltcms.chromauiproject.AttributesType;
import net.sourceforge.maltcms.chromauiproject.ConfigResourceType;
import net.sourceforge.maltcms.chromauiproject.ProcessingPipelinesType;
import net.sourceforge.maltcms.chromauiproject.ReportsType;
import net.sourceforge.maltcms.chromauiproject.ResourcesType;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * Implementation of a ChromA project type.
 * Abstracts from the concrete XML schema used to represent the
 * logical structure of a project on disk.
 * Should be used as a starting point for other project type
 * implementations/extensions. See @link{http://platform.netbeans.org/tutorials/nbm-projectextension.html}
 * for details on writing a project type extension.
 *
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 */
public class ChromaProject implements Project{

    private FileObject projectDir;

    private ProjectState state;

    private net.sourceforge.maltcms.chromauiproject.Project project;

    private Lookup lkp;

    /**
     * Build a ChromaProject from the project dir, a given ProjectState and
     * the bound xml file containing the project info.
     * @param projectDir
     * @param state
     * @param project
     */
    public ChromaProject(FileObject projectDir, ProjectState state, net.sourceforge.maltcms.chromauiproject.Project project) {
        this.projectDir = projectDir;
        this.state = state;
        this.project = project;
    }

    @Override
    public FileObject getProjectDirectory() {
        return this.projectDir;
    }

    /**
     * Obtain a direct reference to the underlying bound xml file.
     * Allows direct manipulations, so be careful!
     * @return the bound xml document of this project
     */
    public net.sourceforge.maltcms.chromauiproject.Project getProject() {
        return this.project;
    }

    /**
     * Delegate method to underlying bound xml file.
     * @return the <code>resources</code> node of this project
     */
    public ResourcesType getResources() {
        return getProject().getResources();
    }

    /**
     * Delegate method to underlying bound xml file.
     * @return the <code>attributes</code> node of this project
     */
    public AttributesType getAttributes() {
        return getProject().getAttributes();
    }

    /**
     * Delegate method to underlying bound xml file.
     * @return the <code>configResources</code> node of this project
     */
    public ConfigResourceType getConfigResource() {
        return getProject().getConfigResource();
    }

    /**
     * Delegate method to underlying bound xml file.
     * @return the <code>processingPipelines</code> node of this project
     */
    public ProcessingPipelinesType getProcessingPipelines() {
        return getProject().getProcessingPipelines();
    }

    /**
     * Delegate method to underlying bound xml file.
     * @return the resources node of this project
     */
    public ReportsType getReports() {
        return getProject().getReports();
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                        state, //allow outside code to mark the project as needing saving
                        new Info(), //Project information implementation
                        new ChromaProjectLogicalView(this), //Logical view of project implementation
                    });
        }
        return lkp;

    }

    private final class Info implements ProjectInformation {

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(
                    "maltcms/ui/nb/resources/chromaProject.png"));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return ChromaProject.this;
        }
    }


}
