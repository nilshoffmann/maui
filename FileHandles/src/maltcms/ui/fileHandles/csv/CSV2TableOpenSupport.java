/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.table.DefaultTableModel;
import maltcms.ui.charts.JFCViewDescription;
import maltcms.ui.fileHandles.serialized.JFCView;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.OpenSupport;
import org.openide.util.Exceptions;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mw
 * @author nils.hoffmann@cebitec.uni-bielefeld.de
 */
public class CSV2TableOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    private List<CSVDataObject> auxDataObjects = new LinkedList<CSVDataObject>();

    public CSV2TableOpenSupport(CSVDataObject.Entry entry) {
        super(entry);
    }

    public CSV2TableOpenSupport(CSVDataObject.Entry entry, DataObject...auxDataObjects) {
        this(entry);
        addDataObjects(Arrays.asList(auxDataObjects));
    }

    public CSV2TableOpenSupport(CSVDataObject.Entry entry, List<DataObject> auxDataObjects) {
        this(entry);
        addDataObjects(auxDataObjects);
    }

    private void addDataObjects(List<DataObject> l) {
        for (DataObject dobj : l) {
            if (dobj instanceof CSVDataObject) {
                auxDataObjects.add((CSVDataObject) dobj);
            }
        }
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        final CSVDataObject dobj = (CSVDataObject) entry.getDataObject();
        final CSVTableViewDescription tc = new CSVTableViewDescription();
        final JFCViewDescription jv = new JFCViewDescription();
        MultiViewDescription[] dsc = {tc,jv};

        final ProgressHandle ph = ProgressHandleFactory.createHandle("Loading file " + dobj.getPrimaryFile().getName());
        final ExecutorService es = Executors.newSingleThreadExecutor();
        final FileObject[] files = new FileObject[1 + auxDataObjects.size()];
        for (int i = 0; i < files.length; i++) {
            if (i == 0) {
                files[0] = dobj.getPrimaryFile();
            } else {
                files[i] = auxDataObjects.get(i - 1).getPrimaryFile();
            }
        }
        final Future<DefaultTableModel> f = es.submit(new CSV2TableLoader(ph, files));
        try {
            tc.setTableModel(f.get());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
        ph.finish();
        tc.setChartTarget((JFCView)jv.createElement());
        CloneableTopComponent ctc = MultiViewFactory.createCloneableMultiView(dsc, dsc[0]);
        ctc.setDisplayName(dobj.getPrimaryFile().getName());
        
        return ctc;
    }
}

