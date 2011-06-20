package net.sf.maltcms.chromaui.project.spi.project;

import java.io.IOException;
import net.sf.maltcms.chromaui.project.api.DBProjectFactory;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Utilities;

/**
 * Implementation of @see{ProjectFactory} service provider.
 * Will look for a file <code>chromaUIProject.xml</code> in directories
 * and accept those as project directories.
 *
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 *
 */
@org.openide.util.lookup.ServiceProvider(service=ProjectFactory.class)
public class ChromAUIProjectFactory implements ProjectFactory {

    @Override
    public boolean isProject(FileObject fo) {
        //System.out.println("Checking if "+fo.getPath()+" is a valid project!");
        return fo.getFileObject(DBProjectFactory.PROJECT_FILE) != null;
    }

    @Override
    public org.netbeans.api.project.Project loadProject(FileObject fo, ProjectState ps) throws IOException {

        if(isProject(fo)) {
            System.out.println("Loading project from "+fo.getPath());
            ChromAUIProject cp = new ChromAUIProject();
            cp.setState(ps);
            cp.activate(FileUtil.toFile(fo.getFileObject(DBProjectFactory.PROJECT_FILE)).toURI().toURL());
            return cp;
        }else{
            return null;
        }
    }

    @Override
    public void saveProject(org.netbeans.api.project.Project prjct) throws IOException, ClassCastException {
//        ChromAUIProject cp = (ChromAUIProject)prjct;
//        cp.getCrudProvider().
    }

}

