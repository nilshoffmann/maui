/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.table.DefaultTableModel;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.util.Exceptions;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mw
 */
public class CSV2ListOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public CSV2ListOpenSupport(CSVDataObject.Entry entry) {
        super(entry);
    }

    protected CloneableTopComponent createCloneableTopComponent() {
        final CSVDataObject dobj = (CSVDataObject) entry.getDataObject();
        final CSV2ListTopComponent tc = new CSV2ListTopComponent();
        tc.setDisplayName(dobj.getName());

        final ProgressHandle ph = ProgressHandleFactory.createHandle("Loading file " + dobj.getPrimaryFile().getName());
        final ExecutorService es = Executors.newSingleThreadExecutor();
        try {
            final Future<DefaultTableModel> f = es.submit(new CSV2ListLoader(dobj.getPrimaryFile().getInputStream(), ph));
            try {
                tc.setTableModel(f.get());
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
                ph.finish();
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
                ph.finish();
            }
        } catch (FileNotFoundException fne) {
            Exceptions.printStackTrace(fne);
            ph.finish();
        }

        return tc;
    }
}

