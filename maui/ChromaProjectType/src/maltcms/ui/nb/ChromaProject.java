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
package maltcms.ui.nb;

import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.sourceforge.maltcms.chromauiproject.AttributeType;
import net.sourceforge.maltcms.chromauiproject.ConfigResourceType;
import net.sourceforge.maltcms.chromauiproject.ProcessingPipelineType;
import net.sourceforge.maltcms.chromauiproject.ReportsType;
import net.sourceforge.maltcms.chromauiproject.ResourceType;
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
 * @author Nils Hoffmann
 */
public class ChromaProject implements Project{

    private FileObject projectFile;

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
    public ChromaProject(FileObject projectFile, ProjectState state, net.sourceforge.maltcms.chromauiproject.Project project) {
        this.projectFile = projectFile;
        this.state = state;
        this.project = project;
    }

    @Override
    public FileObject getProjectDirectory() {
        return this.projectFile.getParent();
    }

    public FileObject getProjectFile() {
        return this.projectFile;
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
    public List<ResourceType> getResources() {
        return getProject().getResources().getResource();
    }

    /**
     * Delegate method to underlying bound xml file.
     * @return the <code>attributes</code> node of this project
     */
    public List<AttributeType> getAttributes() {
        return getProject().getAttributes().getAttribute();
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
    public List<ProcessingPipelineType> getProcessingPipelines() {
        return getProject().getProcessingPipelines().getProcessingPipeline();
    }

    /**
     * Delegate method to underlying bound xml file.
     * @return the resources node of this project
     */
    public ReportsType getReports() {
        return getProject().getReports();
    }

    /**
     * Delegate method to underlying bound xml file.
     * If you want to add ResourceType instances, call
     * <code>addResources</code>
     * @param the <code>resources</code> to set for this project
     */
    public void setResources(List<ResourceType> rt) {
        getProject().getResources().getResource().clear();
        addResources(rt);
    }

    public void addInputFiles(FileObject... fo) {
        List<ResourceType> l = getProject().getResources().getResource();
        for(FileObject fobj:fo) {
            ResourceType rtype = new ResourceType();
            rtype.setType("inputFile");
            rtype.setUri(FileUtil.toFile(fobj).toURI().toString());
            l.add(rtype);
        }
    }

//    public List<FileObject> getInputFiles() {
//        List<FileObject> fos = new LinkedList<FileObject>();
//        List<ResourceType> l = getProject().getResources().getResource();
//    }

    /**
     * Delegate method to underlying bound xml file.
     * @param rt the <code>resources</code> to add to this project.
     */
    public void addResources(List<ResourceType> rt) {
        getProject().getResources().getResource().addAll(rt);
    }


    public void setAttributes(List<AttributeType> at) {
        getProject().getAttributes().getAttribute().clear();
        addAttributes(at);
    }

    public void addAttributes(List<AttributeType> at) {
        getProject().getAttributes().getAttribute().addAll(at);
    }

    public void setConfigResource(ConfigResourceType crt) {
        getProject().setConfigResource(crt);
    }

    public void setProcessingPipelines(List<ProcessingPipelineType> ppt) {
        getProject().getProcessingPipelines().getProcessingPipeline().clear();
        addProcessingPipelines(ppt);
    }

    public void addProcessingPipelines(List<ProcessingPipelineType> ppt) {
        getProject().getProcessingPipelines().getProcessingPipeline().addAll(ppt);
    }

    public void setReports(List<ResourceType> rt) {
        getProject().getReports().getResource().clear();
        addReports(rt);
    }

    public void addReports(List<ResourceType> rt) {
        getProject().getReports().getResource().addAll(rt);
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
