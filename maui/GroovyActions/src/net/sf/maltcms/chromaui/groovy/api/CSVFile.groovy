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

package net.sf.maltcms.chromaui.groovy.api

/**
 * @author Nils Hoffmann
 */
class CSVFile {

    List<String[]> rows = []
    List<String> header = []
    boolean addMissingFirstColumnLabel = true

    public CSVFile(File f) {
        this()
        load(f)
    }

    public CSVFile() {

    }

    public CSVFile(List<String[]> data) {
        this()
        int idx = 0;
        for (String[] str: data) {
            if (idx == 0) {
                header = str
            } else {
                rows.add(str)
            }
            idx++
        }
    }

    public void load(File f) {
        String[] lines = f.text.split('\n')
        rows = lines.collect {it.split('\t')}
        //remove the string header
        header = new ArrayList<String>(Arrays.asList(rows.remove(0)))
        if(rows.size()>0 && header.size() < rows[0].length) {
            header.add(0,"id")
        }
        header = header.collect{it-> if(it.contains("\"")) { return it.replace("\"","")} else {
                return it
            }}
    }

    public void save(File f) {
        List<String[]> rowData = []
        rowData.add(0, header)
        rowData.addAll(rows)
        StringBuilder sb = new StringBuilder();
        for (stra in rowData) {
            def arr = stra.each {it -> it.trim()}
            sb.append(arr.join("\t") + "\n")
        }
        f.getParentFile().mkdirs()
        f.setText(sb.toString())
    }

    public List<String> getColumn(String column) {
        int idx = getColumnIndexFor(column)
        def finalRows = []
        for (row in rows) {
            def myRow = []
            int i = 0;
            for (str in row) {
                if (i == idx) {
                    myRow.add(str)
                }
                i++;
            }
            finalRows.add(myRow)
        }
        return finalRows
    }

    public void merge(String primaryKeyColumn, CSVFile c) {
        //        if(c.rows.size()!=this.rows.size()) {
        //            throw new IllegalArgumentException("Size mismatch! Trying to add content with different number of rows!");
        //        }
        int pkcIdx = getColumnIndexFor(primaryKeyColumn)
        int cpkcIdx = c.getColumnIndexFor(primaryKeyColumn)
        println "Merging using ${primaryKeyColumn} as primary key for merge!"
        println "Source table is \n${c.header}\n${c.rows}"
        println "Target table is \n${header}\n${rows}"

        for (headerField in c.header) {
            if (!header.contains(headerField)) {
                println "Adding new header field ${headerField}"
                header.add(headerField)
                int i = 0;
                List<String> ccol = c.getColumn(headerField);
                println "Appending column"
                for (crow in c.getRows()) {
                    int ridx = getRowIndexFor(crow[cpkcIdx], cpkcIdx)
                    if (ridx != -1) {
                        rows.get(ridx).add(ccol.get(i));
                    }
                    i++;
                }
            } else {
                println "Target CSV file already contains header column with column: " + headerField
            }
        }
    }

    public List<String> getRow(String indexName, String column) {
        int idx = getRowIndexFor(indexName, getColumnIndexFor(column))
        return rows.get(idx)
    }

    public int getRowIndexFor(String indexName, int columnIndex) {
        int i = 0;
        for (val in rows) {
            if (columnIndex > 0 && columnIndex < val.length) {
                if (val[columnIndex] == (indexName)) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    public int getColumnIndexFor(String column) {
        return header.findIndexOf {it -> it.equals(column)}
    }

    public void sortByColumn(String column, boolean ascending) {
        int idx = header.findIndexOf {it -> it.equals(column)}
        if (rows[0][idx] instanceof String) {
            def mc1 = [
                compare: {a, b -> a[idx].toString().compareTo(b[idx].toString())}
            ] as Comparator
            if (!ascending) {
                def rmc1 = Collections.reverseOrder(mc1)
                Collections.sort(rows, rmc1)
            } else {
                Collections.sort(rows, mc1)
            }
        } else {
            def mc1 = [
                compare: {a, b -> a[idx].toDouble() <=> b[idx].toDouble()}
            ] as Comparator
            if (!ascending) {
                def rmc1 = Collections.reverseOrder(mc1)
                Collections.sort(rows, rmc1)
            } else {
                Collections.sort(rows, mc1)
            }
        }

    }
}
