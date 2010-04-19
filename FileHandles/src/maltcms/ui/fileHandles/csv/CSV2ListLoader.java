package maltcms.ui.fileHandles.csv;

import cross.datastructures.tuple.Tuple2D;
import cross.io.csv.CSVReader;
import java.io.InputStream;
import java.util.Vector;
import java.util.concurrent.Callable;
import javax.swing.table.DefaultTableModel;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author nilshoffmann
 */
public class CSV2ListLoader implements Callable<DefaultTableModel> {

    private final InputStream is;
    private final ProgressHandle ph;
    private DefaultTableModel dtm = null;

    CSV2ListLoader(InputStream is, ProgressHandle ph) {
        this.is = is;
        this.ph = ph;
    }

    @Override
    public DefaultTableModel call() throws Exception {
        ph.start(100);
        CSVReader csvr = new CSVReader();
        ph.progress("Opening file", 10);
        Tuple2D<Vector<Vector<String>>, Vector<String>> t = csvr.read(this.is);
        ph.progress("Extracting headers", 20);
        Vector<String> headers = t.getSecond();
        ph.progress("Extracting columns", 50);
        Vector<Vector<String>> columns = t.getFirst();
        ph.progress("Creating TableModel", 80);
        this.dtm = new DefaultTableModel(columns, headers);
        ph.progress("Finished!", 100);
        ph.finish();
        return this.dtm;
    }
    
}
