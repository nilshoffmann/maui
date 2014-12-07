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
package net.sf.maltcms.chromaui.io.gcImageBlobImporter.parser;

import cross.datastructures.tuple.Tuple2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nils Hoffmann
 */
public class GcImageBlobParser {

    public enum ColumnName {

        BLOBID(),
        COMPOUND_NAME(),
        GROUP_NAME(),
        INCLUSION(),
        INTERNAL_STANDARD(),
        RETENTION_I,
        RETENTION_II,
        PEAK_VALUE,
        VOLUME;

        public static ColumnName fromString(String name) {
            switch (name) {
                case "BlobID":
                    return BLOBID;
                case "Compound Name":
                    return COMPOUND_NAME;
                case "Group Name":
                    return GROUP_NAME;
                case "Inclusion":
                    return INCLUSION;
                case "Internal Standard":
                    return INTERNAL_STANDARD;
                case "Retention I (min)":
                    return RETENTION_I;
                case "Retention II (sec)":
                    return RETENTION_II;
                case "Peak Value":
                    return PEAK_VALUE;
                case "Volume":
                    return VOLUME;
                default:
                    throw new IllegalArgumentException("Unsupported column name '" + name + "'");
            }
        }
    };

    public static String FIELD_SEPARATOR = "\t";
    public static String QUOTATION_CHARACTER = "\"";
    public static Locale defaultLocale = Locale.getDefault();

    public static HashMap<String, String> getFilenameToGroupMap(File f) {
        List<String> header = null;
        HashMap<String, String> filenameToGroupMap = new LinkedHashMap<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line = "";
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] lineArray = line.split(String.valueOf(FIELD_SEPARATOR));
                    if (lineCount > 0) {
                        //                        System.out.println(
                        //                                "Adding file to group mapping: " + lineArray[0] + " " + lineArray[1]);
                        filenameToGroupMap.put(lineArray[0], lineArray[1]);
                    }
                    lineCount++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GcImageBlobParser.class.getName()).log(Level.SEVERE,
                    null, ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(GcImageBlobParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return filenameToGroupMap;
    }

    public static int getIndexOfHeaderColumn(List<ColumnName> header,
            ColumnName columnName) {
        int idx = 0;
        for (ColumnName str : header) {
            if (str == columnName) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    public static List<ColumnName> getHeaderColumns(String[] lineArray) {
        List<ColumnName> l = new ArrayList<>();
        for(String col:lineArray) {
            try {
                l.add(ColumnName.fromString(col));
            }catch(IllegalArgumentException iae) {
                Logger.getLogger(GcImageBlobParser.class.getName()).log(Level.WARNING, "Skipping unsupported column key {0}", col);
            }
        }
        return l;
    }
    
    public static LinkedHashSet<ColumnName> getHeader(File f, boolean normalizeColumnNames) {
        LinkedHashSet<ColumnName> globalHeader = new LinkedHashSet<>();
        List<ColumnName> header = null;
        String fileName = f.getName().substring(0, f.getName().lastIndexOf(
                "."));
        //System.out.println("Processing report " + fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line = "";
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] lineArray = splitLine(line, FIELD_SEPARATOR, QUOTATION_CHARACTER);//line.split(String.valueOf(FIELD_SEPARATOR));
                    if (header == null) {
                        header = getHeaderColumns(lineArray);
                        break;
                    }
                    lineCount++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GcImageBlobParser.class.getName()).log(
                    Level.SEVERE, null, ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(GcImageBlobParser.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        globalHeader.addAll(header);
        return globalHeader;
    }
    public static final String doubleQuotePattern = "";
    public static final String msPattern = "";

    public static String[] splitLine(String line, String fieldSeparator, String quoteSymbol) {
        switch (fieldSeparator) {
            case ",":
                Pattern p = Pattern.compile("((\")([^\"]*)(\"))");
                Matcher m = p.matcher(line);
                List<String> results = new LinkedList<>();
                int match = 1;
                while (m.find()) {
                    results.add(m.group(3).trim());
                }
                Pattern endPattern = Pattern.compile(",([\"]{0,1}([^\"]*)[^\"]{0,1}$)");
                Matcher m2 = endPattern.matcher(line);
                while (m2.find()) {
                    results.add(m2.group(1).trim());
                }
                return results.toArray(new String[results.size()]);
            case "\t":
                return line.replaceAll("\"", "").split("\t");
            default:
                throw new IllegalArgumentException("Field separator " + fieldSeparator + " is not supported, only ',' and '\t' are valid!");
        }
    }

    public static List<TableRow> parseBody(LinkedHashSet<ColumnName> globalHeader,
            File f, boolean normalizeColumnNames) {
        List<TableRow> body = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));

            String line = "";
            int lineCount = 0;
            List<ColumnName> header = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    ArrayList<String> lineList = new ArrayList<>(Arrays.asList(splitLine(line, FIELD_SEPARATOR, QUOTATION_CHARACTER)));//.split(String.valueOf(FIELD_SEPARATOR))));
                    if (header == null) {
                        header = getHeaderColumns(lineList.toArray(new String[lineList.size()]));
                    } else {
                        TableRow tr = new TableRow();
                        for (ColumnName headerColumn : globalHeader) {
                            int localIndex = getIndexOfHeaderColumn(header,
                                    headerColumn);
                            if (localIndex >= 0 && localIndex < lineList.size()) {//found column name
                                tr.put(headerColumn, lineList.get(localIndex));
                            } else {//did not find column name
                                tr.put(headerColumn, null);
                            }
                        }
                        body.add(tr);
                    }
                    lineCount++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GcImageBlobParser.class.getName()).log(Level.SEVERE,
                    null, ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(GcImageBlobParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return body;
    }

    public static Tuple2D<LinkedHashSet<ColumnName>, List<TableRow>> parseReport(
            File f) {
        return parseReport(f, true);
    }

    public static Tuple2D<LinkedHashSet<ColumnName>, List<TableRow>> parseReport(
            File f, boolean normalizeColumnNames) {
        if (f.getName().toLowerCase().endsWith("csv")) {
//            System.out.println("CSV Mode");
            GcImageBlobParser.FIELD_SEPARATOR = ",";
            GcImageBlobParser.QUOTATION_CHARACTER = "\"";
        } else if (f.getName().toLowerCase().endsWith("tsv") || f.getName().toLowerCase().endsWith("txt")) {
//            System.out.println("TSV Mode");
            GcImageBlobParser.FIELD_SEPARATOR = "\t";
            GcImageBlobParser.QUOTATION_CHARACTER = "";
        }
        LinkedHashSet<ColumnName> header = getHeader(f, normalizeColumnNames);
        List<TableRow> table = parseBody(header, f, normalizeColumnNames);
        return new Tuple2D<>(header, table);
    }

    public static double parseDouble(ColumnName columnName, TableRow tr) {
        String value = tr.get(columnName);
        return parseDouble(value);
    }

    public static double parseDouble(String s) {
        return parseDouble(s, defaultLocale);
    }

    public static double parseDouble(String s, Locale locale) {
        if (s == null || s.isEmpty()) {
            return Double.NaN;
        }
        try {
            return NumberFormat.getNumberInstance(locale).parse(s).doubleValue();
        } catch (ParseException ex) {
            try {
                return NumberFormat.getNumberInstance(Locale.US).parse(s).
                        doubleValue();
            } catch (ParseException ex1) {
                return Double.NaN;
            }
        }
    }

}
