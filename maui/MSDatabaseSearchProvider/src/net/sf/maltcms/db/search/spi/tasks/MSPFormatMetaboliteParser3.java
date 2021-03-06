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
package net.sf.maltcms.db.search.spi.tasks;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import cross.datastructures.collections.CachedLazyList;
import cross.datastructures.collections.IElementProvider;
import cross.exception.ConstraintViolationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.Metabolite;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;

/**
 *
 * @author Nils Hoffmann
 */
@Slf4j
public class MSPFormatMetaboliteParser3 {

    private ArrayDouble.D1 masses = null;
    private ArrayInt.D1 intensities = null;
    private String name = null;
    private StringBuffer data = null;
    private int dbno = -1;
    private String id = null;
    private String idType = null;
    private String comments = null;
    private int npeaks = 0;
    private int points = 0;
    private int linecounter = 0;
//    private ArrayList<IMetabolite> metabolites;
    private String syndate;
    private String synname;
    private String sp;
    private double ri;
    private double rt;
    private String rtUnit;
    private String formula;
    private String casNumber;
    private double mw;
    private boolean nextIsPeakData = false;
    private int nnpeaks = 0;
    private int metaboliteCounter = 1;
    private Locale locale = Locale.US;
    private URI link;
    private NumberFormat localeAwareFormat;

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.localeAwareFormat = null;
    }

    public NumberFormat getNumberFormat() {
        if (localeAwareFormat == null) {
            localeAwareFormat = NumberFormat.getInstance(locale);
        }
        return localeAwareFormat;
    }

    public IMetabolite handleLine(String line) {
        IMetabolite metabolite = null;
        if (line.startsWith("Name: ")) {
            handleName(line.substring("Name: ".length()).trim());
        } else if (line.startsWith("Synon: ")) {
            handleSynon(line.substring("Synon: ".length()).trim());
        } else if (line.startsWith("DB#: ")) {
            handleDBNo(line.substring("DB#: ".length()).trim());
        } else if (line.startsWith("Comment: ")) {
            handleComments(line.substring("Comment: ".length()).trim());
        } else if (line.startsWith("Comments: ")) {
            handleComments(line.substring("Comments: ".length()).trim());
        } else if (line.startsWith("CAS#: ") || line.startsWith("CASNO: ")) {
            handleCasNumber(line.trim());
            // System.out.println("Skipping "+line);
        } else if (line.startsWith("Num Peaks: ")) {
            nnpeaks++;
            handleNumPeaks(line.substring(("Num Peaks: ").length()).trim());
            nextIsPeakData = true;
        } else if (line.startsWith("Formula: ")) {
            handleFormula(line.substring(("Formula: ").length()).trim());
        } else if (line.startsWith("MW: ")) {
            handleMW(line.substring(("MW: ").length()).trim());
        } else if (line.isEmpty()) {// next metabolite
            if (data != null) {
                handleData(data.toString());
                metabolite = createMetabolite();
            }
        } else {// we have data
            if (nextIsPeakData) {
                if (data == null) {
                    data = new StringBuffer();
                }
                data.append(line);
            }
            // System.out.println(data.toString());
        }
        linecounter++;
        return metabolite;
    }

    public IMetabolite createMetabolite() {
        if (this.id == null) {
            this.id = this.name;
        }
        if (this.dbno == -1) {
            this.dbno = metaboliteCounter++;
        }
        if (this.name == null) {
            Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.WARNING, "Error creating metabolite, name={0}; id={1}", new Object[]{this.name, this.id});
            throw new RuntimeException("Error creating metabolite, name=" + this.name
                    + "; id=" + this.id);
        }
        IMetabolite m = new Metabolite(this.name, this.id, this.idType,
                this.dbno, this.comments, this.formula, this.syndate, this.ri,
                this.rt, this.rtUnit, (int) this.mw, this.sp, this.synname,
                this.masses, this.intensities);
        m.setLink(this.link);
        if (m == null) {
            Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).warning("Error creating metabolite");
            throw new RuntimeException("Error creating metabolite");
        }
        //this.metabolites.add(m);
//        System.out.println("Parsed Metabolite Nr. " + this.metabolites.size()
//                + ": " + m.getName());
        //System.out.println("Parsed Metabolite : " + m.toString());
        this.name = null;
        this.id = null;
        this.link = null;
        this.idType = null;
        this.dbno = -1;
        this.comments = null;
        this.syndate = null;
        this.ri = 0;
        this.sp = null;
        this.synname = null;
        this.masses = null;
        this.intensities = null;
        this.points = 0;
        this.mw = 0;
        this.rt = 0;
        this.rtUnit = null;
        this.formula = null;
        this.npeaks = 0;
        this.data = null;
        this.casNumber = null;
        this.nextIsPeakData = false;
        return m;
    }

    public void handleCasNumber(String line) {
        casNumber = line.substring(line.lastIndexOf(":")).trim();
    }

    public void handleName(String name) {
        this.name = name;
    }

    public void handleSynon(String synon) {
        if (synon.startsWith("DATE:")) {
            handleSynonDate(synon.substring("DATE:".length()).trim());
        } else if (synon.startsWith("NAME:")) {
            handleSynonName(synon.substring("NAME:".length()).trim());
        } else if (synon.startsWith("SP:")) {
            handleSynonSP(synon.substring("SP:".length()).trim());
        } else if (synon.startsWith("CHROMA4D-ID:")) {
            this.idType = "CHROMA4D-ID";
            handleSynonID(synon.substring("CHROMA4D-ID:".length()).trim());
        } else if (synon.startsWith("MPIMP-ID:")) {
            this.idType = "MPIMP-ID";
            handleSynonMPIMPID(synon.substring("MPIMP-ID:".length()).trim());
        } else if (synon.startsWith("GMD LINK:")) {
            this.idType = "MPIMP-GUID";
            handleSynonGmdLink(synon.substring("GMD LINK:".length()).trim());
        } else if (synon.startsWith("ID:")) {
            this.idType = "ID";
            handleSynonID(synon.substring("ID:".length()).trim());
        } else if (synon.startsWith("RI:")) {
            handleSynonRI(synon.substring(("RI:").length()).trim());
        } else if (synon.startsWith("RT:")) {
            handleSynonRT(synon.substring(("RT:").length()).trim());
        } else if (synon.startsWith("MATCH:")) {
            handleSynonMATCH(synon.substring(("MATCH:").length()).trim());
        } else if (synon.startsWith("##")) {
            handleSynonNist2Lib(synon.substring(("##").length()));
        } else {
            Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.WARNING, "Unknown SYNON attribute: {0}", synon);
            // System.exit(-1);
        }
    }

    public void handleSynonNist2Lib(String nist2LibSynon) {
        if (nist2LibSynon.startsWith("Retention-Index") && nist2LibSynon.contains("=")) {
            String[] split = nist2LibSynon.split("=");
            this.ri = parseDoubleString(split[1].trim());
        } else if (nist2LibSynon.startsWith("Retention-Time") && nist2LibSynon.contains("=")) {
            String[] split = nist2LibSynon.split("=");
            this.rt = parseDoubleString(split[1].trim());
        }
    }

    public void handleSynonDate(String date) {
        // System.out.println("Date: "+date);
        this.syndate = date;
    }

    public void handleSynonName(String name) {
        // System.out.println("Name: "+name);
        this.synname = name;
    }

    public void handleSynonSP(String sp) {
        // System.out.println("Synon: SP:"+sp);
        this.sp = sp;
    }

    public void handleSynonMPIMPID(String id) {
        // System.out.println("Synon: MPIMP-ID:"+id);
        handleSynonID(id);
    }

    public void handleSynonID(String id) {
        // System.out.println("Synon: ID:"+id);
        this.id = id;
    }

    public void handleSynonRI(String ri) {
        // System.out.println("Synon: RI:"+ri);
        this.ri = parseDoubleString(ri);
    }

    public void handleSynonRT(String rt) {
        // System.out.println("Synon: RT:"+rt);
        // Assume sec or min prefix
        if (rt.startsWith("sec") || rt.startsWith("min")) {
            this.rtUnit = rt.substring(0, 3);
            this.rt = parseDoubleString(rt.substring(4));
        } else {
            this.rtUnit = "sec";
            this.rt = parseDoubleString(rt);
        }

    }

    public void handleSynonGmdLink(String link) {
        try {
            this.link = URI.create(link);
        } catch (IllegalArgumentException iae) {
            log.error("Exception while parsing link: " + link, iae);
        }
        String uuidPrefix = link.replace("http://gmd.mpimp-golm.mpg.de/Analytes/", "");
        uuidPrefix = uuidPrefix.substring(0, uuidPrefix.indexOf(".") - 1);
        this.id = uuidPrefix;//.substring(link.lastIndexOf("/")+1,link.length());
//		this.id = this.id.substring(this.id.lastIndexOf("."));
    }

    public void handleSynonMATCH(String match) {
        Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "IGNORING ATTRIBUTE MATCH={0}", match);
    }

    public void handleFormula(String formula) {
        // System.out.println("formula: "+formula);
        this.formula = formula;
    }

    public void handleMW(String mw) {
        // System.out.println("MW: "+mw);
        this.mw = parseDoubleString(mw);
    }

    protected double parseDoubleString(String number) {
        double num = Double.NaN;
        NumberFormat nf = NumberFormat.getInstance(locale);
        Number parsedNumber;
        try {
            parsedNumber = nf.parse(number);
            num = parsedNumber.doubleValue();
        } catch (ParseException ex) {
            log.error("Exception while parsing: ", ex);
            throw new RuntimeException(ex);
        }
        return num;
    }

    protected int parseIntString(String number) {
        int num = -1;
        NumberFormat nf = NumberFormat.getInstance(locale);
        Number parsedNumber;
        try {
            parsedNumber = nf.parse(number);
            num = parsedNumber.intValue();
        } catch (ParseException ex) {
            log.error("Exception while parsing: ", ex);
            throw new RuntimeException(ex);
        }
        return num;
    }

    public void handleComments(String comments) {
        // System.out.println("Comments: "+comments);
        this.comments = comments;
    }

    public void handleDBNo(String dbno) {
        // System.out.println("DB#: "+dbno);
        this.dbno = parseIntString(dbno);
    }

    public void handleNumPeaks(String numpeaks) {
        // System.out.println("Num Peaks: "+numpeaks);
        this.npeaks = parseIntString(numpeaks);
    }

    public void handleData(String data) {
        if (this.masses == null) {
            this.masses = new ArrayDouble.D1(this.npeaks);
        }
        if (this.intensities == null) {
            this.intensities = new ArrayInt.D1(this.npeaks);
        }
        // System.out.println(data);
        if (data.contains(";")) {
            parseMZI(data, " ", ";");
            // System.out.println(this.masses);
            // System.out.println(this.intensities);
        } else if (data.contains(" ")) {
            // System.out.println("Parsing msp compatible data!");
            parseMZI(data, ":", " ");
        } else {
            Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "This is no valid data line! {0}", data);
        }
    }

    private void parseMZI(String data, String pairsep, String recordsep) {
        // System.out.println("Parsing mzI data: "+data);
        String filteredData = data.replaceAll("\\s+", " ");
        String[] pairs = filteredData.split(recordsep);
//		System.out.println("Point pairs: " + Arrays.deepToString(pairs));
        for (String p : pairs) {
            p = p.trim();
            if (!p.isEmpty()) {
                // System.out.println(p);
                String[] pair = p.split(pairsep);
//                System.out.println(Arrays.toString(pair));
                if (pair.length == 2) {
                    this.masses.set(this.points,
                            parseDoubleString(pair[0].trim()));
                    this.intensities.set(this.points, parseIntString(pair[1].trim()));
                    this.points++;
                } else {
                    Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.WARNING, "Incorrect split result for pair: {0}! Omitting rest!", p);
                    return;
                }
            }
        }
//		System.out.println("Masses: " + masses);
//		System.out.println("Intensities: " + intensities);
    }

    private class MetaboliteProvider implements IElementProvider<IMetabolite> {

        private File f;
        private long records = 0;
        private List<Long> metaboliteStartIndices;
        private RandomAccessFile raf = null;

        MetaboliteProvider(File f) {
            this.f = f;
            raf = null;
            try {
                Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).info("Opening random access file!");
                raf = new RandomAccessFile(f, "r");
                String line = "";
                metaboliteStartIndices = new ArrayList<>();
                int lineCounter = 0;
                long offset = 0;
                Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "RAF file pointer: {0}, RAF length: {1}", new Object[]{raf.getFilePointer(), raf.length()});
                while ((offset = raf.getFilePointer()) < raf.length()) {
                    line = raf.readLine();
                    if (line.startsWith("Name: ")) {
                        records++;
                        metaboliteStartIndices.add(offset);
                    }
                    lineCounter++;
                }
            } catch (FileNotFoundException e) {
                Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).warning(e.getLocalizedMessage());
                log.error("File not found: ", e);
            } catch (IOException e) {
                Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).warning(e.getLocalizedMessage());
                log.error("IO Exception: ", e);
            } finally {
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException ex) {
                        log.error("IO Exception: ", ex);
                    }
                }
            }
            Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "Found {0} metabolites in database file.", records);
        }

        @Override
        public int size() {
            return (int) records;
        }

        @Override
        public IMetabolite get(int i) {
            try {
                Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "Retrieving index {0}", i);
                raf = new RandomAccessFile(f, "r");
                raf.seek(metaboliteStartIndices.get(i));
                long end = raf.length();
                if (i + 1 < metaboliteStartIndices.size()) {
                    end = metaboliteStartIndices.get(i + 1);
                }
                IMetabolite metabolite = null;
                while (raf.getFilePointer() < end) {
                    metabolite = handleLine(raf.readLine());
                }
                if (metabolite == null) {
                    throw new RuntimeException("Could not create metabolite for index " + i);
                }
                Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "Loaded metabolite {0}", i);
                return metabolite;

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException ex) {
                        log.error("IO Exception: ", ex);
                    }
                }
            }
        }

        @Override
        public List<IMetabolite> get(int i, int i1) {
            if ((i >= 0 && i1 > i) && (i1 < size())) {
                List<IMetabolite> l = new ArrayList<>(i1 - i);
                for (int j = i; j <= i1; j++) {
                    IMetabolite m = get(i);
                    if (m != null) {
                        l.add(m);
                    } else {
                        throw new RuntimeException("Could not retrieve metabolite " + i);
                    }
                }
                return l;
            }
            throw new ConstraintViolationException("Index range invalid: " + i + ", " + i1 + " not in range: 0.." + size());
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long sizeLong() {
            return (int) size();
        }

        @Override
        public IMetabolite get(long l) {
            return get((int) l);
        }

        @Override
        public List<IMetabolite> get(long start, long stop) {
            return get((int) start, (int) stop);
        }
    }

    public List<IMetabolite> parse(File f) {
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader(f));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                handleLine(line);
//            }
//            // System.out.println("Found "+nnpeaks+" mass spectra!");
//            // System.exit(-1);
//            // handleLine("\r");
//        } catch (FileNotFoundException e) {
//            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.err.println(e.getLocalizedMessage());
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
//        }
        CachedLazyList<IMetabolite> cll = new CachedLazyList<>(new MSPFormatMetaboliteParser3.MetaboliteProvider(f));
//        this.metabolites = new ArrayList<IMetabolite>();

//        return this.metabolites;
        return cll;
    }

    public static void main(String[] args) {
        MSPFormatMetaboliteParser3 gmp = new MSPFormatMetaboliteParser3();
        if (args.length > 1) {
            ObjectContainer db = Db4oEmbedded.openFile(args[0]);
            EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
            configuration.common().objectClass("maltcms.ms.IMetabolite").cascadeOnUpdate(
                    true);
            String[] files = new String[args.length - 1];
            System.arraycopy(args, 1, files, 0, files.length);
            try {
                int cnt = 0;
                for (String s : files) {
                    File f = new File(s);
                    List<IMetabolite> al = gmp.parse(f);
                    ObjectSet<IMetabolite> numMet = db.query(new Predicate<IMetabolite>() {
                        /**
                         *
                         */
                        private static final long serialVersionUID = -3537350989202183850L;

                        @Override
                        public boolean match(IMetabolite arg0) {
                            return true;
                        }
                    });
                    Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "DB holding {0} metabolites!", numMet.size());
                    int i = 0;
                    int size = al.size();
                    for (IMetabolite me : al) {
                        final String ID = me.getID();
                        // ObjectSet<IMetabolite> os = db.query(new
                        // Predicate<IMetabolite>() {

                        /**
                         *
                         */
                        // private static final long serialVersionUID =
                        // -8580415202887162014L;
                        // @Override
                        // public boolean match(IMetabolite arg0) {
                        // if(arg0.getID().equals(ID)){
                        // return true;
                        // }
                        // return false;
                        // }
                        // });
                        // if(os.size() == 0) {
                        Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "Adding metabolite {0}/{1} :{2} to db!", new Object[]{i + 1, size, me.getName()});
                        Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "ID: {0}", ID);
                        db.store(me);
                        // }else if(os.size()==1) {
                        // System.out.println("Updating metabolite "+(i+1)+"/"+size+" :"+me.getName());
                        // os.get(0).update(me);
                        // db.set(me);
                        // } else if(os.size()>2){
                        // System.err.println("Query returned ambiguous results for "+(i+1)+"/"+size+"!");
                        // for(IMetabolite met:os) {
                        // System.err.println(met.getName());
                        // }
                        // System.exit(-1);
                        // }
                        i++;
                    }
                    cnt += i;
                    Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).info("Committing to database!");
                    db.commit();
                }
                int committed = db.query(IMetabolite.class).size();
                Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).log(Level.INFO, "Processed {0} Metabolites, total in database: {1}!", new Object[]{cnt, committed});
                db.close();

            } finally {
                db.close();
            }

        } else {
            Logger.getLogger(MSPFormatMetaboliteParser3.class.getName()).info(
                    "Usage: MSPFormatMetaboliteParser <OUTFILE> <INFILE_1> ... <INFILE_N>");
        }
        System.exit(0);
    }
}
