/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.heatmap3Dviewer;

import java.net.URL;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.util.Exceptions;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author nilshoffmann
 */
public class Heatmap3DOpenSupport extends OpenSupport implements OpenCookie, CloseCookie{

    private URL u;

    public Heatmap3DOpenSupport(MultiDataObject.Entry dob) {
        super(dob);
        try {
            u = dob.getFile().getURL();
        } catch (FileStateInvalidException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        Heatmap3DViewerTopComponent htc = new Heatmap3DViewerTopComponent();
        htc.setFile(u);
        return htc;
    }

}
