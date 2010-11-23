/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.extensions;

import cross.datastructures.tuple.Tuple2D;
import java.util.BitSet;
import maltcms.commands.distances.ArrayCos;
import maltcms.commands.distances.ArrayRankCorr;
import maltcms.commands.distances.IArrayDoubleComp;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.db.similarities.Similarity;
import maltcms.tools.MaltcmsTools;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.IndexIterator;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author mwilhelm
 */
public class DotProductMetaboliteSimilarity extends Similarity<IMetabolite> {

//    private IArrayDoubleComp iadc = new ArrayDot();
    private IArrayDoubleComp iadc = new TestDist();

    @Override
    public double get(Tuple2D<Array, Array> query, IMetabolite t1) {
        Tuple2D<Array, Array> tpl1 = new Tuple2D<Array, Array>(t1.getMassSpectrum().getFirst(), t1.getMassSpectrum().getSecond());
        Tuple2D<Array, Array> tpl2 = query;
        double sim = similarity(tpl1, tpl2);
//        System.out.println(t1.getName());
        return sim;
    }

    protected double similarity(Tuple2D<Array, Array> t1,
            Tuple2D<Array, Array> t2) {
        double resolution = 1.0d;
        MinMax mm1 = MAMath.getMinMax(t1.getFirst());
        MinMax mm2 = MAMath.getMinMax(t2.getFirst());
        // Union, greatest possible interval
        double max = Math.max(mm1.max, mm2.max);
        double min = Math.min(mm1.min, mm2.min);
        int bins = MaltcmsTools.getNumberOfIntegerMassBins(min, max, resolution);

        ArrayDouble.D1 s1 = null, s2 = null;
        ArrayDouble.D1 dmasses1 = new ArrayDouble.D1(bins);

        s1 = new ArrayDouble.D1(bins);
        maltcms.tools.ArrayTools.createDenseArray(t1.getFirst(), t1.getSecond(),
                new Tuple2D<Array, Array>(dmasses1, s1), ((int) Math.floor(min)), ((int) Math.ceil(max)), bins,
                resolution, 0.0d);
//		}
        //normalization to 0..1
        double maxS1 = MAMath.getMaximum(s1);
        s1 = (ArrayDouble.D1) maltcms.tools.ArrayTools.mult(s1, 1000.0d / maxS1);

        ArrayDouble.D1 dmasses2 = new ArrayDouble.D1(bins);
        s2 = new ArrayDouble.D1(bins);
        maltcms.tools.ArrayTools.createDenseArray(t2.getFirst(), t2.getSecond(),
                new Tuple2D<Array, Array>(dmasses2, s2),
                ((int) Math.floor(min)), ((int) Math.ceil(max)), bins,
                resolution, 0.0d);
//        normalization
        double maxS2 = MAMath.getMaximum(s2);
        s2 = (ArrayDouble.D1) maltcms.tools.ArrayTools.mult(s2, 1000.0d / maxS2);

        double d = this.iadc.apply(((int) Math.floor(min)), ((int) Math.ceil(max)), 0.0d, 0.0d, s1, s2);
        //System.out.println("Sim:" + d);
//        double norm1 = this.iadc.apply(-1, -1, 0.0d, 0.0d, s1, s1);
//        System.out.println("Self dot product of db spectrum: " + norm1);
//        double norm2 = this.iadc.apply(-1, -1, 0.0d, 0.0d, s2, s2);
//        System.out.println("Self dot product of query spectrum: " + norm2);
//        d /= Math.sqrt((norm1 * norm2));
        return d;
    }

    @Override
    public double get(IMetabolite t1, IMetabolite t2) {
        Tuple2D<ArrayDouble.D1, ArrayInt.D1> t = t1.getMassSpectrum();
        return get(new Tuple2D<Array, Array>(t.getFirst(), t.getSecond()), t2);
    }

    private class TestDist implements IArrayDoubleComp {

        @Override
        public Double apply(int i, int i1, double d, double d1, Array library, Array unknown) {
            double thresholdLibrary = 0;
            double thresholdUnkown = 0;
            double massOffset = i;
            double massExp = 1;
            double intExp = 0.5;
            int minSharedPeaks = 5;
            BitSet libraryPeaks = new BitSet();
            fillBitSet(library, thresholdLibrary, libraryPeaks);
            BitSet unknownPeaks = new BitSet();
            fillBitSet(unknown, thresholdUnkown, unknownPeaks);

            BitSet unionPeaks = new BitSet();
            unionPeaks.or(libraryPeaks);
            unionPeaks.or(unknownPeaks);

            BitSet intersecPeaks = new BitSet();
            intersecPeaks.or(libraryPeaks);
            intersecPeaks.and(unknownPeaks);

            int libraryPeakCount = libraryPeaks.cardinality();
            int unkownPeakCount = unknownPeaks.cardinality();
            int intersecPeakCount = intersecPeaks.cardinality();
            int unionPeakCount = unionPeaks.cardinality();
            double rspc = ((double) intersecPeakCount) / ((double) unionPeakCount);
            double urpc = ((double) unkownPeakCount) / ((double) unionPeakCount);
            double lrpc = ((double) libraryPeakCount) / ((double) unionPeakCount);

//
//
//            System.out.println(libraryPeakCount + " - " + unkownPeakCount + " - " + intersecPeakCount);
            double pr = 0;
            if (intersecPeakCount >= minSharedPeaks) {
//                double dp = getDotProduct(library, unknown, massOffset, 1, 1);
                pr = getRatioOfPeakPairs(intersecPeaks, library, unknown, massOffset, 1, 1);
//                System.out.println("dot prod: " + dp);
//                System.out.println("peak ratio: " + pr);
//
//                double v = unkownPeakCount * dp + intersecPeakCount * pr;
//                v /= (unkownPeakCount + intersecPeakCount);
//
//                return v;
            }
//            } else {
//                return 0.0d;
//            }
            ArrayRankCorr arc = new ArrayRankCorr();
            double rcorr = 0;
            double orcorr = 0;
            if (intersecPeakCount >= minSharedPeaks) {
                rcorr = arc.apply(-1, -1, 0, 0, getIndexSubset(library, intersecPeaks), getIndexSubset(unknown, intersecPeaks));
                orcorr = arc.apply(-1, -1, 0, 0, library, unknown);
            }
            double cosine = new ArrayCos().apply(-1, -1, 0, 0, getIndexSubset(library, intersecPeaks), getIndexSubset(unknown, intersecPeaks));
            double ocosine = new ArrayCos().apply(-1, -1, 0, 0, library, unknown);
//            System.out.print(intersecPeakCount+"\t"+unkownPeakCount+"\t"+libraryPeakCount+"\t"+unionPeakCount+"\t"+rspc+"\t"+urpc+"\t"+lrpc+"\t"+rcorr+"\t"+orcorr+"\t"+pr+"\t"+cosine+"\t"+ocosine+"\t");


//            IndexIterator li = library.getIndexIterator();
//            IndexIterator ui = unknown.getIndexIterator();
            double libS = 0;
            double unkS = 0;
            double diff = 0;
            double comm = 0;
            double libTmp, unkTmp;
//            while (li.hasNext() && ui.hasNext()) {
            for (i = 0; i < library.getShape()[0]; i++) {
                libTmp = getW(library, i, massOffset, 3, 1);
                unkTmp = getW(unknown, i, massOffset, 3, 1);
                libS += libTmp;
                unkS += unkTmp;
                diff += Math.abs(unkTmp - libTmp);
                comm += Math.min(unkTmp, libTmp);
            }

            double sim = comm / unkS;
            double sim1 = 1.0d - diff / (unkS + libS);
            System.out.println(libS + "/" + unkS + ": " + diff + "|" + comm + "/" + (libS + unkS) + " --> " + sim*sim1);
            return sim*sim1;

//            return rspc*rcorr;
        }

        private Array getIndexSubset(Array a, BitSet bs) {
            Array ret = Array.factory(a.getElementType(), new int[]{bs.cardinality()});
            int idx = 0;
            for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
                ret.setDouble(idx, a.getDouble(i));
                idx++;
            }
            return ret;
        }

        private double getRatioOfPeakPairs(BitSet intersecPeaks, Array library, Array unknown, double massOffset, double massExp, double intensExp) {

//            int lastSetBit = 0;
            double val = 0;
            double n = 0;
            double sum = 0;
//            for (int i = 0; i < intersecPeaks.cardinality(); i++) {
            for (int i = intersecPeaks.nextSetBit(0); i >= 0; i = intersecPeaks.nextSetBit(i + 1)) {
//                lastSetBit = intersecPeaks.nextSetBit(lastSetBit);
                // System.out.print(i + ", ");
//                val = getW(library, i, massOffset, massExp, intensExp) / getW(library, i - 1, massOffset, massExp, intensExp);
//                val *= getW(unknown, i - 1, massOffset, massExp, intensExp) / getW(unknown, i, massOffset, massExp, intensExp);
                val = getW(library, i, massOffset, massExp, intensExp) / getW(unknown, i, massOffset, massExp, intensExp);
                if (val < 1) {
                    n = 1;
                } else {
                    n = -1;
                }
                if (Math.pow(val, n) != Double.NaN) {
                    sum += Math.pow(val, n);
                }
            }


            //System.out.println(" --- " + sum);

            return (1.0d / intersecPeaks.cardinality()) * sum;
        }

        private double getDotProduct(Array t, Array t1, double massOffset, double massExp, double intensExp) {
            double dp = 0;
            double[] wt = new double[t.getShape()[0]];
            double[] wt1 = new double[t.getShape()[0]];
            for (int k = 0; k < t.getShape()[0]; k++) {
                wt[k] = getW(t, k, massOffset, massExp, intensExp);
                wt1[k] = getW(t1, k, massOffset, massExp, intensExp);
                dp += (wt[k] * wt1[k]);
            }
            double nt = 0, nt1 = 0;
            for (int k = 0; k < t.getShape()[0]; k++) {
                nt += (Math.pow(wt[k], 2.0));
            }
            for (int k = 0; k < t1.getShape()[0]; k++) {
                nt1 += (Math.pow(wt1[k], 2.0));
            }
            return dp / Math.sqrt((nt * nt1));
        }

        private double getW(Array t, int index, double massOffset, double massExp, double intensExp) {
//            if (index > 0) {
            double ret = Math.pow(index + 1 + massOffset, massExp) * Math.pow(t.getDouble(index), intensExp);
            return ret;
//            } else {
//                // durch peak reation kann hier index = -1 sein. bin mir nicht sicher, wie man das am besten umgeht/abfaengt.
//                return 1;
//            }
        }

        private void fillBitSet(Array t, double threshold, BitSet tpeaks) {
            IndexIterator iter1 = t.getIndexIterator();
            int idx = 0;
            while (iter1.hasNext()) {
                double val = iter1.getDoubleNext();
                if (val > threshold) {
                    tpeaks.set(idx);
                }
                idx++;
            }
        }

        @Override
        public double getCompressionWeight() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double getDiagonalWeight() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double getExpansionWeight() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean minimize() {
            return false;
        }

        @Override
        public void configure(Configuration c) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
