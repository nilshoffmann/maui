package maltcms.ui.fileHandles.csv;

import cross.datastructures.tuple.Tuple2D;
import maltcms.io.csv.CSVReader;
import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.openide.filesystems.FileObject;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author nilshoffmann
 */
public class CSV2TableLoader implements Callable<DefaultTableModel> {

    private final FileObject[] f;
    private final ProgressHandle ph;
    private DefaultTableModel dtm = null;

    public CSV2TableLoader(ProgressHandle ph, FileObject...f) {
        Set<FileObject> s = new LinkedHashSet<FileObject>();
        s.addAll(Arrays.asList(f));
        this.f = s.toArray(new FileObject[]{});
        this.ph = ph;
    }

    @Override
    public DefaultTableModel call() throws Exception {
        ph.start(100);
        int[] lengths = new int[f.length];
        Logger.getLogger(CSV2TableLoader.class.getName()).info("Loading "+f.length+" files!");
        for(int i = 0;i<f.length;i++) {
            Logger.getLogger(CSV2TableLoader.class.getName()).info("Loading file "+f[i].toString());
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
            
            if(this.dtm==null){
                this.dtm = new DefaultTableModel(toRows(convertColumns(columns.getFirst())), columns.getSecond());
            }else{
                Vector<Vector<?>> rows = toRows(convertColumns(columns.getFirst()));
                for(Vector<?> row:rows) {
                    this.dtm.addRow(row);
                }
            }
            if(columns.getFirst().isEmpty()) {
                return this.dtm;
            }
            lengths[i] = columns.getFirst().get(0).size();
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
        if(f.length>1) {
            Logger.getLogger(CSV2TableLoader.class.getName()).info("Adding filename column!");
            this.dtm.addColumn("Filename", filenames);
        }
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

    private Class<?> getClassForColumn(String colName, Vector<String> col) {
        Class<?> c = null;
        for (String s : col) {
            final String val = replaceNAs(s);

            if(c!=null && (c.isAssignableFrom(String.class))) {
                return c;
            }
            try {
                Long.valueOf(val);
                System.out.println("Class for column "+colName+" is: Long");
                c = Long.class;
//                break;
            } catch (NumberFormatException nfe) {
            }

            try {
                Double.valueOf(val);
//                c = Double.class;
                System.out.println("Class for column "+colName+" is: Double");
                c =  Double.class;
            } catch (NumberFormatException nfe) {
            }

            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
//                c = Boolean.class;
                System.out.println("Class for column "+colName+" is: Boolean");
                c = Boolean.class;
            } else {
//                c = String.class;
                System.out.println("Class for column "+colName+" is: String");
                c = String.class;
            }
//            break;
        }
        
//        return c;
        System.out.println("Class for column "+colName+" is String");
        return String.class;
    }

    private Vector<Vector<?>> convertColumns(Vector<Vector<String>> v) {
        Vector<Vector<?>> r = new Vector<Vector<?>>();
        int i = 0;
        for (Vector<String> vs : v) {
            r.add(convertColumn(vs, getClassForColumn(i+"",vs)));
            i++;
        }
        return r;
    }

    private String replaceNAs(String s) {
        if(s.equalsIgnoreCase("NA")||s.equals("-")) {
                return "NaN";
        }
        return s;
    }

    private Vector<?> convertColumn(Vector<String> v, Class<?> c) {
        System.out.println("Class for column is: "+c.getName());
        if (c.equals(Long.class) || c.equals(Integer.class)) {
            System.out.println("Converting to Long/Integer");
            Vector<Long> ret = new Vector<Long>();
            for (String s : v) {
                final String tmp = replaceNAs(s);
                ret.add(Long.valueOf(tmp.equals("NaN")?"-1":tmp));
            }
            return ret;
        } else if (c.equals(Double.class) || c.equals(Float.class)) {
            System.out.println("Converting to Double/Float");
            Vector<Double> ret = new Vector<Double>();
            for (String s : v) {
                ret.add(Double.valueOf(replaceNAs(s)));
            }
            return ret;
        } else if (c.equals(Boolean.class)) {
            System.out.println("Converting to boolean");
            Vector<Boolean> ret = new Vector<Boolean>();
            for (String s : v) {
                final String tmp = replaceNAs(s);
                ret.add(Boolean.valueOf(tmp.equals("NaN")?"false":tmp));
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
