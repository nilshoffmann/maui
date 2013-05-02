/**
 * 
 */
package maltcms.ui.nb;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;


/**
 * Implementation of @see{ProjectFactory} service provider.
 * Will look for a file <code>chromaUIProject.xml</code> in directories
 * and accept those as project directories.
 *
 * @author Nils Hoffmann
 *
 */
@org.openide.util.lookup.ServiceProvider(service=ProjectFactory.class)
public class ChromaProjectFactory implements ProjectFactory {

    public static final String PROJECT_FILE = "chromaUIProject.xml";

    @Override
    public boolean isProject(FileObject fo) {
        //System.out.println("Checking if "+fo.getPath()+" is a valid project!");
        return fo.getFileObject(PROJECT_FILE) != null;
    }

    @Override
    public org.netbeans.api.project.Project loadProject(FileObject fo, ProjectState ps) throws IOException {
        
        if(isProject(fo)) {
            System.out.println("Loading project from "+fo.getPath());
            ChromaProject cp = new ChromaProject(fo.getFileObject(PROJECT_FILE), ps, unmarshall(new File(fo.getPath(),PROJECT_FILE)));
            return cp;
        }else{
            return null;
        }
    }

    @Override
    public void saveProject(org.netbeans.api.project.Project prjct) throws IOException, ClassCastException {
        ChromaProject cp = (ChromaProject)prjct;
        net.sourceforge.maltcms.chromauiproject.Project p = cp.getProject();
        File f = new File(prjct.getProjectDirectory().getPath(),PROJECT_FILE);
        System.out.println("Saving project to: "+f.getAbsolutePath());
        marshall(p, f);
    }

    private net.sourceforge.maltcms.chromauiproject.Project unmarshall(File f) {
        JAXBContext jc;
        try {
                jc = JAXBContext.newInstance("net.sourceforge.maltcms.chromauiproject");
                final Unmarshaller u = jc.createUnmarshaller();
                final net.sourceforge.maltcms.chromauiproject.Project p = (net.sourceforge.maltcms.chromauiproject.Project) u.unmarshal(f);
                return p;
        } catch (final JAXBException e) {
                throw new RuntimeException(e.fillInStackTrace());
        }
    }

    private void marshall(net.sourceforge.maltcms.chromauiproject.Project p, File f) {
        JAXBContext jc;
        try {
                jc = JAXBContext.newInstance("net.sourceforge.maltcms.chromauiproject");
                final Marshaller u = jc.createMarshaller();
                u.marshal(p, f);
        } catch (final JAXBException e) {
                throw new RuntimeException(e.fillInStackTrace());
        }
    }

}
