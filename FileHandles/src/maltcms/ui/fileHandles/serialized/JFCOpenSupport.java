/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.serialized;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.jfree.chart.JFreeChart;
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
class JFCOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public JFCOpenSupport(JFCDataObject.Entry entry) {
        super(entry);
    }

    protected CloneableTopComponent createCloneableTopComponent() {
        final JFCDataObject dobj = (JFCDataObject) entry.getDataObject();
        final JFCTopComponent tc = new JFCTopComponent();
        tc.setDisplayName(dobj.getName());

        final ProgressHandle ph = ProgressHandleFactory.createHandle("Loading file " + dobj.getPrimaryFile().getName());
        tc.setDisplayName(dobj.getPrimaryFile().getName());
        final ExecutorService es = Executors.newSingleThreadExecutor();

        final Future<JFreeChart> f = es.submit(new JFCLoader(dobj.getPrimaryFile().getPath(), ph));
        try {
            tc.setChart(f.get());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            ph.finish();
        }

        return tc;
    }
}
