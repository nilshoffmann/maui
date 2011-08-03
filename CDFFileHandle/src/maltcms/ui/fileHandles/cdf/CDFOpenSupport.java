/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.fileHandles.cdf;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author nilshoffmann
 */
public class CDFOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public CDFOpenSupport(CDFDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
//        CDFDataObject dobj = (CDFDataObject) entry.getDataObject();
//        System.out.println("Selected data object: "+dobj);
//        CDFViewTopComponent jtc = new CDFViewTopComponent();
//        jtc.setFile(dobj);
//        return jtc;
        return null;
    }
}
