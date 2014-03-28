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
package net.sf.maltcms.chromaui.foldChangeViewer.charts.datasets;

import java.util.List;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Nils Hoffmann
 */
public class FoldChangeDataset extends ADataset1D<StatisticsContainer, FoldChangeElement> {

    private final double minX, maxX, minY, maxY;
    private final int[][] ranks;
    private String displayName = "Fold Change Dataset";
    private final Lookup lookup;

    public FoldChangeDataset(List<INamedElementProvider<? extends StatisticsContainer, ? extends FoldChangeElement>> l, String displayName, Lookup lookup) {
        super(l, new IDisplayPropertiesProvider() {

            @Override
            public String getName(ISelection selection) {
                FoldChangeElement target = (FoldChangeElement) selection.getTarget();
                return "Peak group: " + target.getPeakGroup().getName() + " - " + target.getPeakGroup().getMajorityDisplayName();
            }

            @Override
            public String getDisplayName(ISelection selection) {
                FoldChangeElement target = (FoldChangeElement) selection.getTarget();
                return "Peak group: " + target.getPeakGroup().getDisplayName() + " - " + target.getPeakGroup().getMajorityDisplayName();
            }

            @Override
            public String getShortDescription(ISelection selection) {
                FoldChangeElement target = (FoldChangeElement) selection.getTarget();
                return "Peak group: " + target.getPeakGroup().getShortDescription() + " - " + target.getPeakGroup().getMajorityDisplayName() + " - " + target.getPeakGroup().getShortDescription();
            }

            @Override
            public String getSourceName(ISelection selection) {
                StatisticsContainer source = (StatisticsContainer) selection.getSource();
                return source.getName();
            }

            @Override
            public String getSourceDisplayName(ISelection selection) {
                StatisticsContainer source = (StatisticsContainer) selection.getSource();
                return source.getDisplayName();
            }

            @Override
            public String getSourceShortDescription(ISelection selection) {
                StatisticsContainer source = (StatisticsContainer) selection.getSource();
                return source.getShortDescription();
            }

            @Override
            public String getTargetName(ISelection selection) {
                return getName(selection);
            }

            @Override
            public String getTargetDisplayName(ISelection selection) {
                return getDisplayName(selection);
            }

            @Override
            public String getTargetShortDescription(ISelection selection) {
                return getShortDescription(selection);
            }

        });
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        ranks = new int[l.size()][];
        int nepCnt = 0;
        for (INamedElementProvider<? extends StatisticsContainer, ? extends FoldChangeElement> nep : l) {
            int[] nranks = new int[nep.size()];
            for (int i = 0; i < nep.size(); i++) {
                FoldChangeElement fce = nep.get(i);
                minX = Math.min(minX, fce.getFoldChange());
                minY = Math.min(minY, fce.getPvalue());
                maxX = Math.max(maxX, fce.getFoldChange());
                maxY = Math.max(maxY, fce.getPvalue());
                nranks[i] = i;
            }
            ranks[nepCnt++] = nranks;
        }

        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.displayName = displayName;
        this.lookup = lookup;
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(super.getLookup(), lookup);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        fireDatasetChanged();
    }

    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMaxX() {
        return maxX;
    }

    @Override
    public double getMinY() {
        return minY;
    }

    @Override
    public double getMaxY() {
        return maxY;
    }

    @Override
    public Number getX(int i, int i1) {
        return getTarget(i, i1).getFoldChange();
    }

    @Override
    public Number getY(int i, int i1) {
        return getTarget(i, i1).getPvalue();
    }

    @Override
    public int[][] getRanks() {
        return ranks;
    }

    @Override
    public Number getStartX(int i, int i1) {
        return getX(i, i1);
    }

    @Override
    public double getStartXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    @Override
    public Number getEndX(int i, int i1) {
        return getXValue(i, i1);
    }

    @Override
    public double getEndXValue(int i, int i1) {
        return getXValue(i, i1);
    }

    @Override
    public Number getStartY(int i, int i1) {
        return getY(i, i1);
    }

    @Override
    public double getStartYValue(int i, int i1) {
        return getYValue(i, i1);
    }

    @Override
    public Number getEndY(int i, int i1) {
        return getY(i, i1);
    }

    @Override
    public double getEndYValue(int i, int i1) {
        return getYValue(i, i1);
    }

}
