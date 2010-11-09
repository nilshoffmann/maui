/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties;

import javax.swing.table.DefaultTableModel;
import maltcms.ui.fileHandles.csv.CSVTableViewDescription;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
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
        final CSVTableViewDescription tc = new CSVTableViewDescription();
        MultiViewDescription[] dsc = {tc};
//        final CSVTableView tc = new CSVTableView();
//        tc.setDisplayName(dobj.getName());

        tc.setTableModel(PropertyLoader.getModel(entry.getFile().getPath()));

        CloneableTopComponent ctc = MultiViewFactory.createCloneableMultiView(dsc, dsc[0]);
        ctc.setDisplayName(dobj.getName());
        return ctc;
    }
}
