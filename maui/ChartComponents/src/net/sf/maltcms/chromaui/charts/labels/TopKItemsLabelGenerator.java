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
package net.sf.maltcms.chromaui.charts.labels;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;

/**
 * Creates labels for the top k items with highest value.
 *
 * @author nilshoffmann
 */
public class TopKItemsLabelGenerator implements XYItemLabelGenerator {

    private final Set<Point> topK;
    private final List<Point> sublist;

    /**
     *
     * @param sm
     * @param k
     */
    public TopKItemsLabelGenerator(List<Point> sm, int k) {
//		System.out.println("Length of list: " + sm.size());
//		System.out.println("K=" + k);
        this.sublist = sm.subList(Math.max(0, sm.size() - k), sm.size());
        topK = new HashSet<>();
//		System.out.println("Sublist length: " + this.sublist.size());
        topK.addAll(this.sublist);
//		System.out.println("Top K: " + topK);
    }

    /**
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @return
     */
    @Override
    public String generateLabel(XYDataset arg0, int arg1, int arg2) {
        if (this.topK.contains(new Point(arg1, arg2))) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%.4f", arg0.getXValue(arg1, arg2)));
            return sb.toString();
        }

        return null;
    }
}
