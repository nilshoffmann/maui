/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import maltcms.ui.fileHandles.serialized.JFCTopComponent;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mw
 */
public class CSV2JFCOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public CSV2JFCOpenSupport(CSVDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        CSVDataObject dobj = (CSVDataObject) entry.getDataObject();
        JFCTopComponent jtc = new JFCTopComponent();
        CSV2JFCLoader jl = new CSV2JFCLoader();
        jl.load(dobj, jtc);
        return jtc;
    }
}
