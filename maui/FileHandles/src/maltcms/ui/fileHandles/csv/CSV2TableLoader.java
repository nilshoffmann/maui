/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
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
import java.util.logging.Level;
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

    /**
     *
     * @param ph
     * @param f
     */
    public CSV2TableLoader(ProgressHandle ph, FileObject... f) {
        Set<FileObject> s = new LinkedHashSet<>();
        s.addAll(Arrays.asList(f));
        this.f = s.toArray(new FileObject[]{});
        this.ph = ph;
    }

    @Override
    public DefaultTableModel call() throws Exception {
        ph.start(100);
        int[] lengths = new int[f.length];
        Logger.getLogger(CSV2TableLoader.class.getName()).log(Level.INFO, "Loading {0} files!", f.length);
        for (int i = 0; i < f.length; i++) {
            Logger.getLogger(CSV2TableLoader.class.getName()).log(Level.INFO, "Loading file {0}", f[i].toString());
            CSVReader csvr = new CSVReader();
            Tuple2D<Vector<Vector<String>>, Vector<String>> t;
            //ph.progress("Opening file", 10*(i+1)/f.length);
            try (BufferedInputStream bis = new BufferedInputStream(f[i].getInputStream())) {
                //ph.progress("Opening file", 10*(i+1)/f.length);
                t = csvr.read(bis);
            }
            //ph.progress("Extracting headers", 20*(i+1)/f.length);
            HashMap<String, Vector<String>> hm = csvr.getColumns(t);
            //ph.progress("Extracting columns", 50*(i+1)/f.length);
            Tuple2D<Vector<Vector<String>>, Vector<String>> columns = getColumns(hm);
            //ph.progress("Creating TableModel", 80*(i+1)/f.length);

            if (this.dtm == null) {
                this.dtm = new DefaultTableModel(toRows(convertColumns(columns.getFirst())), columns.getSecond());
            } else {
                Vector<Vector<?>> rows = toRows(convertColumns(columns.getFirst()));
                for (Vector<?> row : rows) {
                    this.dtm.addRow(row);
                }
            }
            if (columns.getFirst().isEmpty()) {
                return this.dtm;
            }
            lengths[i] = columns.getFirst().get(0).size();
            //ph.progress("Finished!", 100*(i+1)/f.length);
            ph.progress("Reading data", 100 * (i + 1) / f.length);
        }
        int sum = 0;
        for (int i = 0; i < f.length; i++) {
            sum += lengths[i];
        }
        String[] filenames = new String[sum];
        int tmp = 0;
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < lengths[i]; j++) {
                filenames[tmp] = f[i].getName();
                tmp++;
            }
        }
        if (f.length > 1) {
            Logger.getLogger(CSV2TableLoader.class.getName()).info("Adding filename column!");
            this.dtm.addColumn("Filename", filenames);
        }
        ph.progress("Done", 100);
        ph.finish();
        return this.dtm;
    }

    private Vector<Vector<?>> toRows(Vector<Vector<?>> cols) {
        Vector<Vector<?>> rows = new Vector<>();
        for (int j = 0; j < cols.get(0).size(); j++) {
            Vector<Object> row = new Vector<>();
            for (Vector<?> col : cols) {
                row.add(col.get(j));
            }
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Row= {0}", row);
            rows.add(row);
        }
        return rows;

    }

    private Tuple2D<Vector<Vector<String>>, Vector<String>> getColumns(HashMap<String, Vector<String>> hm) {
        Vector<Vector<String>> v = new Vector<>();
        Vector<String> headers = new Vector<>(hm.keySet());
        for (String s : headers) {
            v.add(hm.get(s));
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Column: {0} = {1}", new Object[]{s, hm.get(s)});
        }
        return new Tuple2D<>(v, headers);
    }

    private Class<?> getClassForColumn(String colName, Vector<String> col) {
        Class<?> c = null;
        for (String s : col) {
            final String val = replaceNAs(s);

            if (c != null && (c.isAssignableFrom(String.class))) {
                return c;
            }
            try {
                Long.valueOf(val);
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Class for column {0} is: Long", colName);
                c = Long.class;
//                break;
            } catch (NumberFormatException nfe) {
            }

            try {
                Double.valueOf(val);
//                c = Double.class;
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Class for column {0} is: Double", colName);
                c = Double.class;
            } catch (NumberFormatException nfe) {
            }

            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
//                c = Boolean.class;
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Class for column {0} is: Boolean", colName);
                c = Boolean.class;
            } else {
//                c = String.class;
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Class for column {0} is: String", colName);
                c = String.class;
            }
//            break;
        }

//        return c;
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Class for column {0} is String", colName);
        return String.class;
    }

    private Vector<Vector<?>> convertColumns(Vector<Vector<String>> v) {
        Vector<Vector<?>> r = new Vector<>();
        int i = 0;
        for (Vector<String> vs : v) {
            r.add(convertColumn(vs, getClassForColumn(i + "", vs)));
            i++;
        }
        return r;
    }

    private String replaceNAs(String s) {
        if (s.equalsIgnoreCase("NA") || s.equals("-")) {
            return "NaN";
        }
        return s;
    }

    private Vector<?> convertColumn(Vector<String> v, Class<?> c) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Class for column is: {0}", c.getName());
        if (c.equals(Long.class) || c.equals(Integer.class)) {
            Logger.getLogger(getClass().getName()).info("Converting to Long/Integer");
            Vector<Long> ret = new Vector<>();
            for (String s : v) {
                final String tmp = replaceNAs(s);
                ret.add(Long.valueOf(tmp.equals("NaN") ? "-1" : tmp));
            }
            return ret;
        } else if (c.equals(Double.class) || c.equals(Float.class)) {
            Logger.getLogger(getClass().getName()).info("Converting to Double/Float");
            Vector<Double> ret = new Vector<>();
            for (String s : v) {
                ret.add(Double.valueOf(replaceNAs(s)));
            }
            return ret;
        } else if (c.equals(Boolean.class)) {
            Logger.getLogger(getClass().getName()).info("Converting to boolean");
            Vector<Boolean> ret = new Vector<>();
            for (String s : v) {
                final String tmp = replaceNAs(s);
                ret.add(Boolean.valueOf(tmp.equals("NaN") ? "false" : tmp));
            }
            return ret;
        } else if (c.equals(String.class)) {
            Vector<String> ret = new Vector<>();
            for (String s : v) {
                ret.add(s);
            }
            return ret;
        }
        return new Vector<>();
    }
}
