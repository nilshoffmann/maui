/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.api.dataset;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Nils Hoffmann
 */
public class DatasetUtils {

    public static Numeric1DDataset<Point2D> createDataset() {
        INamedElementProvider<? extends List<Point2D>, ? extends Point2D> nep = new INamedElementProvider<List<Point2D>, Point2D>() {
            private List<Point2D> points = createSamplePoints(5000);

            @Override
            public List<Point2D> getSource() {
                return points;
            }

            @Override
            public Comparable getKey() {
                return "Sample dataset";
            }

            @Override
            public void setKey(Comparable key) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int size() {
                return points.size();
            }

            @Override
            public Point2D get(int i) {
                return points.get(i);
            }

            @Override
            public List<Point2D> get(int start, int stop) {
                return points.subList(start, stop);
            }

            @Override
            public void reset() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        List<INamedElementProvider<? extends List<Point2D>, ? extends Point2D>> l = new ArrayList<INamedElementProvider<? extends List<Point2D>, ? extends Point2D>>();
        l.add(nep);
        return new Numeric1DDataset<Point2D>(l);
    }

    public static List<Point2D> createSamplePoints(int n) {
        Random r = new Random(System.nanoTime());
        List<Point2D> points = new ArrayList<Point2D>();
        for (int i = 0; i < n; i++) {
            points.add(new Point2D.Double(2 * i + (r.nextGaussian() - 0.5d), r.nextDouble() * 256.0d));
        }
        return points;
    }
    
    private static class SortablePair {

        private final int index;
        private final double value;
        
        public SortablePair(int index, double value) {
            this.index = index;
            this.value = value;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public double getValue() {
            return this.value;
        }
        
    }
    
    private static class SortablePairComparator implements Comparator<SortablePair> {

        @Override
        public int compare(SortablePair t, SortablePair t1) {
            return Double.compare(t.getValue(), t1.getValue());
        }
        
    }

    public static int[] ranks(double[] domainValues, boolean descending) {
        SortablePair[] sp = new SortablePair[domainValues.length];
        for (int i = 0; i < domainValues.length; i++) {
            SortablePair sortablePair = new SortablePair(i, domainValues[i]);
            sp[i] = sortablePair;
        }
        Comparator<SortablePair> comp = new SortablePairComparator();
        if(descending) {
            Arrays.sort(sp, Collections.reverseOrder(comp));
        }else{
            Arrays.sort(sp, comp);
        }
        final int[] ranks = new int[domainValues.length];
        for (int i = 0; i < sp.length; i++) {
            ranks[i] = sp[i].getIndex();
        }
        return ranks;
    }
}
