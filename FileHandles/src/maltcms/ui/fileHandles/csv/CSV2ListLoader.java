package maltcms.ui.fileHandles.csv;

import cross.datastructures.tuple.Tuple2D;
import cross.io.csv.CSVReader;
import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Callable;
import javax.swing.table.DefaultTableModel;
import org.openide.filesystems.FileObject;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author nilshoffmann
 */
public class CSV2ListLoader implements Callable<DefaultTableModel> {

    private final FileObject[] f;
    private final ProgressHandle ph;
    private DefaultTableModel dtm = null;

    CSV2ListLoader(ProgressHandle ph, FileObject...f) {
        this.f = f;
        this.ph = ph;
    }

    @Override
    public DefaultTableModel call() throws Exception {
        ph.start(100);
        int[] lengths = new int[f.length];
        for(int i = 0;i<f.length;i++) {
            CSVReader csvr = new CSVReader();
            BufferedInputStream bis = new BufferedInputStream(f[i].getInputStream());
            //ph.progress("Opening file", 10*(i+1)/f.length);
            Tuple2D<Vector<Vector<String>>, Vector<String>> t = csvr.read(bis);
            bis.close();
            //ph.progress("Extracting headers", 20*(i+1)/f.length);
            HashMap<String, Vector<String>> hm = csvr.getColumns(t);
            //ph.progress("Extracting columns", 50*(i+1)/f.length);
            Tuple2D<Vector<Vector<String>>, Vector<String>> columns = getColumns(hm);
            //ph.progress("Creating TableModel", 80*(i+1)/f.length);
            lengths[i] = columns.getFirst().get(0).size();
            if(this.dtm==null){
                this.dtm = new DefaultTableModel(toRows(convertColumns(columns.getFirst())), columns.getSecond());
            }else{
                Vector<Vector<?>> rows = toRows(convertColumns(columns.getFirst()));
                for(Vector<?> row:rows) {
                    this.dtm.addRow(row);
                }
            }
            //ph.progress("Finished!", 100*(i+1)/f.length);
            ph.progress("Reading data",100*(i+1)/f.length);
        }
        int sum = 0;
        for(int i = 0;i<f.length;i++) {
            sum+=lengths[i];
        }
        String[] filenames = new String[sum];
        int tmp = 0;
        for(int i = 0;i<f.length;i++) {
            for(int j = 0;j<lengths[i];j++) {
                filenames[tmp] = f[i].getName();
                tmp++;
            }
        }
        this.dtm.addColumn("Filename", filenames);
        ph.progress("Done",100);
        ph.finish();
        return this.dtm;
    }

    private Vector<Vector<?>> toRows(Vector<Vector<?>> cols) {
        Vector<Vector<?>> rows = new Vector<Vector<?>>();
        for (int j = 0; j < cols.get(0).size(); j++) {
            Vector<Object> row = new Vector<Object>();
            for (int i = 0; i < cols.size(); i++) {
                row.add(cols.get(i).get(j));
            }
            System.out.println("Row= "+row);
            rows.add(row);
        }
        return rows;

    }

    private Tuple2D<Vector<Vector<String>>, Vector<String>> getColumns(HashMap<String, Vector<String>> hm) {
        Vector<Vector<String>> v = new Vector<Vector<String>>();
        Vector<String> headers = new Vector<String>(hm.keySet());
        for (String s : headers) {
            v.add(hm.get(s));
            System.out.println("Column: "+s+" = "+hm.get(s));
        }
        return new Tuple2D<Vector<Vector<String>>, Vector<String>>(v, headers);
    }

    private Class<?> getClassForColumn(Vector<String> col) {
        Class<?> c = null;
        for (String s : col) {
            try {
                Long.valueOf(s);
                c = Long.class;
                break;
            } catch (NumberFormatException nfe) {
            }

            try {
                Double.valueOf(s);
                c = Double.class;
                break;
            } catch (NumberFormatException nfe) {
            }

            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
                c = Boolean.class;
                break;
            } else {
                c = String.class;
            }
            break;
        }
        System.out.println("Class for column is: "+c.getName());
        return c;
    }

    private Vector<Vector<?>> convertColumns(Vector<Vector<String>> v) {
        Vector<Vector<?>> r = new Vector<Vector<?>>();
        for (Vector<String> vs : v) {
            r.add(convertColumn(vs, getClassForColumn(vs)));
        }
        return r;
    }

    private Vector<?> convertColumn(Vector<String> v, Class<?> c) {
        System.out.println("Class for column is: "+c.getClass().getName());
        if (c.equals(Long.class) || c.equals(Integer.class)) {
            System.out.println("Converting to Long/Integer");
            Vector<Long> ret = new Vector<Long>();
            for (String s : v) {
                ret.add(Long.valueOf(s));
            }
            return ret;
        } else if (c.equals(Double.class) || c.equals(Float.class)) {
            System.out.println("Converting to Double/Float");
            Vector<Double> ret = new Vector<Double>();
            for (String s : v) {
                ret.add(Double.valueOf(s));
            }
            return ret;
        } else if (c.equals(Boolean.class)) {
            System.out.println("Converting to boolean");
            Vector<Boolean> ret = new Vector<Boolean>();
            for (String s : v) {
                ret.add(Boolean.valueOf(s));
            }
            return ret;
        }else if (c.equals(String.class)){
            Vector<String> ret = new Vector<String>();
            for(String s:v) {
                ret.add(s);
            }
            return ret;
        }
        return new Vector<Object>();
    }
}
