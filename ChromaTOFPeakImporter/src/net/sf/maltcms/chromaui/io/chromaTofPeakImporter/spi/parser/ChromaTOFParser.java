/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser;

import cross.datastructures.tuple.Tuple2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.Index;

/**
 *
 * @author hoffmann
 */
public class ChromaTOFParser {
    public static String FIELD_SEPARATOR = "\t";
    
    public static HashMap<String, String> getFilenameToGroupMap(File f) {
        List<String> header = null;
        HashMap<String, String> filenameToGroupMap = new LinkedHashMap<String, String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = "";
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] lineArray = line.split(FIELD_SEPARATOR);
                    if (lineCount > 0) {
                        System.out.println(
                                "Adding file to group mapping: " + lineArray[0] + " " + lineArray[1]);
                        filenameToGroupMap.put(lineArray[0], lineArray[1]);
                    }
                    lineCount++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ChromaTOFParser.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return filenameToGroupMap;
    }

    public static int getIndexOfHeaderColumn(List<String> header,
            String columnName) {
        int idx = 0;
        for (String str : header) {
            if (str.equalsIgnoreCase(columnName)) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

//    public static String rearrangeMassSpectra(String massSpectrum) {
//        String[] mziTuples = massSpectrum.split(" ");
//        TreeMap<Float, Integer> tm = new TreeMap<Float, Integer>();
//        for (String tuple : mziTuples) {
//            if (tuple.contains(":")) {
//                String[] tplArray = tuple.split(":");
//                tm.put(Float.valueOf(tplArray[0]), Integer.valueOf(tplArray[1]));
//            } else {
//                System.err.println(
//                        "Warning: encountered strange tuple: " + tuple + " within ms: " + massSpectrum);
//            }
//        }
//        StringBuilder sb = new StringBuilder();
//        for (Float mz : tm.keySet()) {
//            sb.append(mz);
//            sb.append(":");
//            sb.append(tm.get(mz));
//            sb.append(" ");
//        }
//        return sb.toString();
//    }
    public static Tuple2D<double[], int[]> convertMassSpectrum(
            String massSpectrum) {
        String[] mziTuples = massSpectrum.split(" ");
        TreeMap<Float, Integer> tm = new TreeMap<Float, Integer>();
        for (String tuple : mziTuples) {
            if (tuple.contains(":")) {
                String[] tplArray = tuple.split(":");
                tm.put(Float.valueOf(tplArray[0]), Integer.valueOf(tplArray[1]));
            } else {
                System.err.println(
                        "Warning: encountered strange tuple: " + tuple + " within ms: " + massSpectrum);
            }
        }
        double[] masses = new double[tm.keySet().size()];
        int[] intensities = new int[tm.keySet().size()];
        int i = 0;
        for (Float key : tm.keySet()) {
            masses[i] = key;
            intensities[i] = tm.get(key);
            i++;
        }
        return new Tuple2D<double[], int[]>(masses, intensities);
    }

    public static LinkedHashSet<String> getHeader(File f) {
        LinkedHashSet<String> globalHeader = new LinkedHashSet<String>();
        ArrayList<String> header = null;
        String fileName = f.getName().substring(0, f.getName().lastIndexOf(
                "."));
        System.out.println("Processing report " + fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line = "";
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] lineArray = line.split(FIELD_SEPARATOR);
                    if (header == null) {
                        for (int i = 0; i < lineArray.length; i++) {
                            lineArray[i] = lineArray[i].trim().toUpperCase().
                                    replaceAll(" ", "_");
                        }
                        header = new ArrayList<String>(Arrays.asList(
                                lineArray));
                        break;
                    }
                    lineCount++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ChromaTOFParser.class.getName()).log(
                    Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ChromaTOFParser.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        globalHeader.addAll(header);
        return globalHeader;
    }

    public static LinkedHashSet<String> getHeader(File[] inputFiles,
            HashMap<String, String> filenameToGroupMap) {
        LinkedHashSet<String> globalHeader = new LinkedHashSet<String>();
        for (File f : inputFiles) {
            ArrayList<String> header = null;
            String fileName = f.getName().substring(0, f.getName().lastIndexOf(
                    "."));
            System.out.println("Processing report " + fileName);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(f));
                String line = "";
                int lineCount = 0;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] lineArray = line.split(FIELD_SEPARATOR);
                        if (header == null) {
                            for (int i = 0; i < lineArray.length; i++) {
                                lineArray[i] = lineArray[i].trim().toUpperCase().
                                        replaceAll(" ", "_");
                            }
                            header = new ArrayList<String>(Arrays.asList(
                                    lineArray));
                            header.add("SOURCE_FILE");
                            header.add("GROUP");
                            break;
                        }
                        lineCount++;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ChromaTOFParser.class.getName()).log(
                        Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChromaTOFParser.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
            globalHeader.addAll(header);
        }
        return globalHeader;
    }

    public static List<TableRow> parseBody(LinkedHashSet<String> globalHeader,
            File f) {
        List<TableRow> body = new ArrayList<TableRow>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = "";
            int lineCount = 0;
            List<String> header = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    ArrayList<String> lineList = new ArrayList<String>(Arrays.
                            asList(line.split(FIELD_SEPARATOR)));
                    if (header == null) {
                        for (int i = 0; i < lineList.size(); i++) {
                            lineList.set(i, lineList.get(i).trim().toUpperCase().
                                    replaceAll(" ", "_"));
                        }
                        header = new ArrayList<String>(lineList);
                    } else {
                        TableRow tr = new TableRow();
                        for (String headerColumn : globalHeader) {
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
            Logger.getLogger(ChromaTOFParser.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return body;
    }

    public static Tuple2D<LinkedHashSet<String>, List<TableRow>> parseReport(
            File f) {
        LinkedHashSet<String> header = getHeader(f);
        List<TableRow> table = parseBody(header, f);
        return new Tuple2D<LinkedHashSet<String>, List<TableRow>>(header, table);
    }
//    public static void main(String[] args) {
//        File groupFile = new File(args[0]);
//        HashMap<String, String> filenameToGroupMap = getFilenameToGroupMap(
//                groupFile);
//
//
//        List<TableRow> body = new ArrayList<TableRow>();
//        File baseDir = new File(args[1]);
//        File[] inputFiles = baseDir.listFiles(new FileFilter() {
//
//            @Override
//            public boolean accept(File pathname) {
//                return pathname.getName().toLowerCase().endsWith(".txt");
//            }
//        });
//        LinkedHashSet<String> globalHeader = getHeader(inputFiles,
//                filenameToGroupMap);
//        for (File f : inputFiles) {
//            String fileName = f.getName().substring(0, f.getName().lastIndexOf(
//                    "."));
//            System.out.println("Processing report " + fileName);
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(f));
//                String line = "";
//                int lineCount = 0;
//                List<String> header = null;
//                while ((line = br.readLine()) != null) {
//                    if (!line.isEmpty()) {
//                        ArrayList<String> lineList = new ArrayList<String>(Arrays.
//                                asList(line.split("\t")));
//                        if (header == null) {
//                            for (int i = 0; i < lineList.size(); i++) {
//                                lineList.set(i, lineList.get(i).trim().
//                                        toUpperCase().replaceAll(" ", "_"));
//                            }
//                            header = new ArrayList<String>(lineList);
//                            header.add("SOURCE_FILE");
//                            header.add("GROUP");
//                        } else {
//                            TableRow tr = new TableRow();
//                            for (String headerColumn : globalHeader) {
//                                int localIndex = getIndexOfHeaderColumn(header,
//                                        headerColumn);
//                                if (localIndex >= 0 && localIndex < lineList.
//                                        size()) {//found column name
//                                    tr.put(headerColumn,
//                                            lineList.get(localIndex));
//                                } else {//did not find column name
//                                    tr.put(headerColumn, null);
//                                }
//                            }
//                            tr.put("SOURCE_FILE", fileName);
//                            tr.put("GROUP", filenameToGroupMap.get(fileName));
//                            body.add(tr);
//                        }
//                        lineCount++;
//                    }
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(ChromaTOFParser.class.getName()).log(
//                        Level.SEVERE, null, ex);
//            }
//        }
//        String[] columnsToWrite = new String[]{"SOURCE_FILE", "GROUP", "NAME",
//            "R.T._(S)", "UNIQUEMASS", "QUANT_MASSES", "QUANT_S/N", "AREA",
//            "RETENTION_INDEX", "S/N", "SIMILARITY", "PROBABILITY",
//            "APEXING_MASSES", "FULL_WIDTH_AT_HALF_HEIGHT", "INTEGRATIONBEGIN",
//            "INTEGRATIONEND", "NOISE", "SPECTRA"};
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
//                    "theresaChromatofConcat.txt")));
//            for (String str : columnsToWrite) {
//                bw.write(str);
//                bw.write("\t");
//            }
//            bw.write("\n");
//            for (TableRow tr : body) {
//                for (String str : columnsToWrite) {
//                    if (str != null) {
//                        String obj = tr.get(str);
//                        if (obj != null) {
//                            bw.write(tr.get(str));
//                        } else {
//                            bw.write("NA");
//                        }
//                    }
//                    bw.write("\t");
//                }
//                bw.write("\n");
//            }
//            bw.flush();
//            bw.close();
//        } catch (IOException ex) {
//            Logger.getLogger(ChromaTOFParser.class.getName()).log(Level.SEVERE,
//                    null, ex);
//        }
//    }
}
