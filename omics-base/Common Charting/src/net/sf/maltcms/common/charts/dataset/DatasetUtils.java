/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.common.charts.dataset;

import java.awt.geom.Point2D;
import java.util.ArrayList;
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
}
