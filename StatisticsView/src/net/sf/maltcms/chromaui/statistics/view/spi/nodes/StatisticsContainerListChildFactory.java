/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.statistics.view.spi.nodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.statistics.view.api.IStatisticsDescriptorComparator;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class StatisticsContainerListChildFactory extends ChildFactory<IStatisticsDescriptor> {

    private final StatisticsContainer statsContainer;
    private Comparator<IStatisticsDescriptor> comparator = new PvalueComparator();
    private int sortByFactor = 0;
    private IChromAUIProject project;

    public StatisticsContainerListChildFactory(IChromAUIProject project,
            StatisticsContainer statsContainer, int sortByFactor) {
        this.project = project;
        this.statsContainer = statsContainer;
        this.sortByFactor = sortByFactor;
    }

    public Comparator<IStatisticsDescriptor> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<IStatisticsDescriptor> comparator) {
        this.comparator = comparator;
        refresh(true);
    }

    public int getSortByFactor() {
        return sortByFactor;
    }

    public void setSortByFactor(int sortByFactor) {
        this.sortByFactor = sortByFactor;
        refresh(true);
    }

    @Override
    protected boolean createKeys(List<IStatisticsDescriptor> toPopulate) {
        for (IStatisticsDescriptor idesc : this.statsContainer.getMembers()) {
            if (Thread.interrupted()) {
                return false;
            } else {
                if (idesc != null) {
                    toPopulate.add(idesc);
                }
            }
        }
        Collections.sort(toPopulate, comparator);
        return true;
    }

    @ServiceProvider(service = IStatisticsDescriptorComparator.class)
    public class DisplayNameComparator implements
            IStatisticsDescriptorComparator {

        @Override
        public int compare(IStatisticsDescriptor t, IStatisticsDescriptor t1) {
            if (t.getClass().equals(t1.getClass())) {
                return t.compareTo(t1);
            }
            return t.getDisplayName().compareTo(t1.getDisplayName());
        }
    }

    @ServiceProvider(service = IStatisticsDescriptorComparator.class)
    public class PvalueComparator implements
            IStatisticsDescriptorComparator {

        @Override
        public int compare(IStatisticsDescriptor t, IStatisticsDescriptor t1) {
            if (t.getClass().equals(t1.getClass())) {
                if (t instanceof IAnovaDescriptor && t1 instanceof IAnovaDescriptor) {
                    double[] pv1 = ((IAnovaDescriptor) t).getPvalues();
                    double[] pv2 = ((IAnovaDescriptor) t1).getPvalues();
                    if (sortByFactor < 0) {
                        for (int i = 0; i < Math.min(pv1.length, pv2.length); i++) {
                            if (pv1[i] < pv2[i]) {
                                return -1;
                            } else if (pv1[i] > pv2[i]) {
                                return 1;
                            }
                        }
                        return 0;
                    } else if (sortByFactor < pv1.length && sortByFactor < pv2.length) {
                        return Double.compare(pv1[sortByFactor],
                                pv2[sortByFactor]);
                    }
                }


                return t.compareTo(t1);
            }
            return t.getDisplayName().compareTo(t1.getDisplayName());
        }
    }

    @Override
    protected Node createNodeForKey(IStatisticsDescriptor key) {
        if (key == null) {
            return Node.EMPTY;
        }
        INodeFactory nodeFactory = Lookup.getDefault().lookup(INodeFactory.class);
        if (key instanceof IAnovaDescriptor) {
            return nodeFactory.createDescriptorNode(key, Children.LEAF, Lookups.fixed(
                    project, key,
                    ((IAnovaDescriptor) key).getPeakGroupDescriptor()));
        } else {
            return nodeFactory.createDescriptorNode(key, Children.LEAF, Lookups.fixed(
                    project));
        }
    }
}
