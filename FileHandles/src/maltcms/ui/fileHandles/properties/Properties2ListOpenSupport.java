/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties;

import maltcms.ui.fileHandles.csv.CSV2ListTopComponent;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mwilhelm
 */
public class Properties2ListOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public Properties2ListOpenSupport(PropertiesDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        final PropertiesDataObject dobj = (PropertiesDataObject) entry.getDataObject();

        final CSV2ListTopComponent tc = new CSV2ListTopComponent();
        tc.setDisplayName(dobj.getName());
        tc.setTableModel(PropertyLoader.getModel(entry.getFile().getPath()));

        return tc;
    }
}
