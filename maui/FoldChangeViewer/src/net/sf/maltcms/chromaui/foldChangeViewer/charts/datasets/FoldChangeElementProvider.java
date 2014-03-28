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

import cross.datastructures.tools.EvalTools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;

/**
 *
 * @author Nils Hoffmann
 */
public class FoldChangeElementProvider implements INamedElementProvider<StatisticsContainer, FoldChangeElement> {

    private final StatisticsContainer statisticsContainer;
    private transient List<FoldChangeElement> foldChangeElements;
    private Comparable name;

    public FoldChangeElementProvider(Comparable key, StatisticsContainer statisticsContainer, ITreatmentGroupDescriptor lhsGroup, ITreatmentGroupDescriptor rhsGroup, IPeakNormalizer normalizer) {
        EvalTools.notNull(lhsGroup, this);
        EvalTools.notNull(rhsGroup, this);
        EvalTools.notNull(normalizer, this);
        this.name = key;
        this.statisticsContainer = statisticsContainer;
        this.foldChangeElements = new ArrayList<FoldChangeElement>();
        int i = 0;
        for (IStatisticsDescriptor statd : statisticsContainer.getMembers()) {
            if (statd instanceof IAnovaDescriptor) {
                IAnovaDescriptor descr = (IAnovaDescriptor) statd;
                if (descr.getPvalues() != null) {
                    FoldChangeElement element = new FoldChangeElement(
                            ((IAnovaDescriptor) statd).getPeakGroupDescriptor(),
                            lhsGroup,
                            rhsGroup,
                            normalizer,
                            descr.getPvalues()[0]
                    );
                    double foldChange = element.getFoldChange();
                    if(Double.isInfinite(foldChange) || Double.isNaN(foldChange)) {
//                        System.err.println("Fold change is not a number or infinite, removing item from dataset!");
                    }else{
                        this.foldChangeElements.add(element);
                        i++;
                    }
                }
            }
        }
//        System.out.println("Retained "+foldChangeElements.size()+" fold change elements!");
        Collections.sort(foldChangeElements, new Comparator<FoldChangeElement>() {

            @Override
            public int compare(FoldChangeElement o1, FoldChangeElement o2) {
                return Double.compare(o1.getFoldChange(), o2.getFoldChange());
            }
        });
    }

    @Override
    public Comparable getKey() {
        return name;
    }

    @Override
    public void setKey(Comparable key) {
        this.name = key;
    }

    @Override
    public int size() {
        return foldChangeElements.size();
    }

    @Override
    public FoldChangeElement get(int i) {
        return foldChangeElements.get(i);
    }

    @Override
    public List<FoldChangeElement> get(int i, int i1) {
        int nscans = i1 - i;
        ArrayList<FoldChangeElement> al = new ArrayList<FoldChangeElement>(nscans);
        for (int j = 0; j < i1; j++) {
            al.add(get(i + j));
        }
        return al;
    }

    @Override
    public void reset() {
    }

    @Override
    public StatisticsContainer getSource() {
        return statisticsContainer;
    }
}
