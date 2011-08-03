/*
 * Copyright (C) 2009, 2010 Mathias Wilhelm mwilhelm A T
 * TechFak.Uni-Bielefeld.DE
 * 
 * This file is part of Cross/Maltcms.
 * 
 * Cross/Maltcms is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Cross/Maltcms is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Cross/Maltcms. If not, see <http://www.gnu.org/licenses/>.
 * 
 * $Id: PeakIdentification.java 129 2010-06-25 11:57:02Z nilshoffmann $
 */
package maltcms.ui.viewer.extensions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import maltcms.datastructures.ms.IMetabolite;
import maltcms.db.MetaboliteQueryDB;
import maltcms.tools.ArrayTools2;

import org.slf4j.Logger;

import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.IndexIterator;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oIOException;

import cross.Logging;
import cross.datastructures.tuple.Tuple2D;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import maltcms.db.similarities.MetaboliteSimilarity;

/**
 * Will do the identification.
 * 
 * @author Mathias Wilhelm(mwilhelm A T TechFak.Uni-Bielefeld.DE)
 */
public class PeakIdentification {

    private final Logger log = Logging.getLogger(this);
    private MetaboliteQueryDB mqdb;
    private ObjectContainer oc;
    private boolean doSearch = true;
//    private String dbFile = "/vol/maltcms/db/gmd/GMD_20100614_VAR5_FAME.db4o";
    private String dbFile = "/vol/maltcms/db/nist/NIST-EIMS-TMS.db4o";
    private double threshold = 0.0d;
    private int k = 1;
    private boolean dbAvailable = true;
    private List<Integer> masqMasses = new ArrayList<Integer>();
    private ObjectSet<IMetabolite> dbMetabolites = null;

    public static PeakIdentification pi = null;

    public static PeakIdentification getInstance() {
        if (pi == null) {
            pi = new PeakIdentification();
        }
        return pi;
    }

    private PeakIdentification() {
//        for (int i = 50; i <70; i++) {
//            this.masqMasses.add(i);
//        }
//        this.masqMasses.add(73);
//        this.masqMasses.add(74);
//        this.masqMasses.add(75);
//        this.masqMasses.add(147);
//        this.masqMasses.add(148);
//        this.masqMasses.add(149);
    }

//    public Tuple2D<Array, Tuple2D<Double, IMetabolite>> getBest(final Array oms) {
    public Tuple2D<Array, Tuple2D<Double, IMetabolite>> getBest(final Array omasses, final Array ointens) {
        if ((this.dbFile == null) || this.dbFile.isEmpty()) {
            this.dbAvailable = false;
        }
        if (this.dbAvailable && this.doSearch) {
            if (this.oc == null) {
                if (new File(this.dbFile).exists()) {
                    this.log.debug("Opening DB locally as file!");
                    this.oc = Db4o.openFile(this.dbFile);
                } else {
                    URL url;
                    try {
                        url = new URL(this.dbFile);
                        this.log.debug("Opening DB via Client!");
                        this.oc = Db4o.openClient(url.getHost(), url.getPort(),
                                url.getUserInfo(), "default");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            if (this.dbMetabolites == null) {
                dbMetabolites = this.oc.query(IMetabolite.class);
            }
            long s = System.currentTimeMillis();
//            final ArrayDouble.D1 massValues = new ArrayDouble.D1(oms.getShape()[0]);
//            final IndexIterator iter = massValues.getIndexIterator();
//            int c = 0;
//            while (iter.hasNext()) {
//                iter.setIntNext(c++);
//            }
//            Array ms = prepareMS(oms);

//            final Tuple2D<Array, Array> query = new Tuple2D<Array, Array>(
//                    massValues, ms);
             final Tuple2D<Array, Array> query = new Tuple2D<Array, Array>(
                    omasses, ointens);

            // final MScanSimilarityPredicate ssp = new
            // MScanSimilarityPredicate(
            // query);
            // ssp.setThreshold(this.threshold);
            //
            // this.mqdb.setPredicate(ssp);
            // final QueryCallable<IMetabolite> qc = this.mqdb.getCallable();
            // ObjectSet<IMetabolite> osRes = null;

            List<Tuple2D<Double, IMetabolite>> hits = null;
            try {
                // osRes = qc.call();

                // this.log.info("Received {} hits from ObjectSet!",
                // osRes.size());

                // if ((osRes != null) && (osRes.size() != 0)) {
                final List<Tuple2D<Double, IMetabolite>> l = new ArrayList<Tuple2D<Double, IMetabolite>>();
                final MetaboliteSimilarity mss = new MetaboliteSimilarity();
//                DotProductMetaboliteSimilarity mss = new DotProductMetaboliteSimilarity();
                // for (final IMetabolite im : osRes) {
//                PrintStream bos = new PrintStream(new FileOutputStream("/vol/maltcms/pi.csv"));
//                PrintStream oldOut = System.out;
//                System.setOut(bos);
//                System.out.println("IntersecPeakCount\tUnknownPeakCount\tLibraryPeakCount\tUnionPeakCount\tRelSharedPeakCount\tRelUnknPeakCount\tRelLibPeakCount\tSharedRankCorr\tRankCorr\tMeanPeakIntensRatio\tSharedCosine\tCosine\tName");
                for (final IMetabolite im : dbMetabolites) {
                    double val = mss.get(query, im);
                    if (val >= this.threshold) {
                        l.add(new Tuple2D<Double, IMetabolite>(val, im));
                    }
                }
                if (this.k > 1) {
                    final Comparator<Tuple2D<Double, IMetabolite>> comp = new Comparator<Tuple2D<Double, IMetabolite>>() {

                        @Override
                        public int compare(
                                final Tuple2D<Double, IMetabolite> o1,
                                final Tuple2D<Double, IMetabolite> o2) {
                            return o1.getFirst().compareTo(o2.getFirst());
                        }
                    };
                    Collections.sort(l, Collections.reverseOrder(comp));
                    hits = l.subList(0, Math.min(l.size(), this.k + 1));
                    // for (Tuple2D<Double, IMetabolite> tuple2D : hits) {
                    // System.out.println(""
                    // + (int) (tuple2D.getFirst() * 1000.0) + ""
                    // + im.getRetentionIndex() + "" + im.getFormula()
                    // + im.getID());
                    // }
                } else {
                    double max = Double.NEGATIVE_INFINITY;
                    IMetabolite maxMet = null;
                    for (final Tuple2D<Double, IMetabolite> t : l) {
                        if (max < t.getFirst() && t.getFirst() > this.threshold) {
                            max = t.getFirst();
                            maxMet = t.getSecond();
                        }
                    }
                    if (max > this.threshold) {
                        hits = new ArrayList<Tuple2D<Double, IMetabolite>>();
                        hits.add(new Tuple2D<Double, IMetabolite>(max, maxMet));
                    }
                }
//                System.setOut(oldOut);
                // }
                // qc.terminate();
                // } catch (final InterruptedException e) {
                // e.printStackTrace();
                // } catch (final ExecutionException e) {
                // e.printStackTrace();
            } catch (final Db4oIOException e) {
                e.printStackTrace();
                this.log.error("Stopping DB search.");
                this.dbAvailable = false;
            } catch (final NullPointerException e) {
                e.printStackTrace();
                this.log.error("Stopping DB search.");
                this.dbAvailable = false;
            } catch (final Exception e) {
                e.printStackTrace();
            }

            if (hits != null && hits.size() > 0) {
                return new Tuple2D<Array, Tuple2D<Double, IMetabolite>>(omasses, hits.get(0));
            }

            System.out.println("identification took " + (System.currentTimeMillis()-s)/1000 + "s");
        }

        return null;
    }

    public Array prepareMS(Array ms) {
        long s = System.currentTimeMillis();
        ms = ArrayTools2.createIntegerArray(ms, this.masqMasses);
//			System.out.println("Array before: "+ms);
//			int[] maxIndices = MaltcmsTools.getTopKIndicesOfMaxIntensity(ms, 100);
//			Arrays.sort(maxIndices);
//			System.out.println("Indices to filter: "+Arrays.toString(maxIndices));
//			List<Integer> filterVals = new ArrayList<Integer>(maxIndices.length);
//			for (int i = 0; i < maxIndices.length; i++) {
//	            filterVals.add(Integer.valueOf(maxIndices[i]));
//            }


//        ArrayInt.D1 newMS = (ArrayInt.D1) ArrayTools2.filter(ms.copy(), new LogFilter(true));
//        System.out.println(newMS);
//        List<Integer> filterVals = new ArrayList<Integer>();
//        int v;
//        final int di = 2;
//        System.out.print("Masses to hold: ");
//        for (int i = 1; i < newMS.getShape()[0] - 1; i++) {
//            v = newMS.get(i);
////            if (newMS.get(i-1) < v && newMS.get(i+1) < v) {
//            if (v > 6) {
////                System.out.println("M");
////                for (int j = -di; j <= di; j++) {
//                int j = 0;
//                if (!filterVals.contains(i + j)) {
//                    System.out.print(i + j + ",");
//                    filterVals.add(i + j);
//                }
////                }
//            }
//        }
//        System.out.println("");
//
//        ms = ArrayTools.filterIndices(ms, filterVals, true, 0);
////        System.out.println("Array after: " + ms);
//
//        System.out.println("preparation took " + (System.currentTimeMillis() - s) + "ms");

        return ms;
    }
}
