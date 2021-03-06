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
package net.sf.maltcms.chromaui.charts.events;

import org.jfree.chart.entity.XYItemEntity;

import cross.event.IEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Nils Hoffmann
 * @param <T>
 */
public class MSChartHandler<T extends IScan> implements
        XYItemEntityEventListener, Lookup.Provider {

    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    private IChromatogram lastChromatogram = null;
    private T scan = null;
    private final ADataset1D<IChromatogram, T> ds;
    private ExecutorService es = Executors.newSingleThreadExecutor();

    /**
     *
     * @param ds
     */
    public MSChartHandler(ADataset1D<IChromatogram, T> ds) {
        this.ds = ds;
    }

    /* (non-Javadoc)
     * @see cross.event.IListener#listen(cross.event.IEvent)
     */

    /**
     *
     * @param v
     */
    
    @Override
    public void listen(final IEvent<XYItemEntity> v) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
//                System.out.println("MSChartHandler received XYItemEntity!");
                XYItemEntity e = v.get();
                if (scan != null) {
//                    System.out.println("Removing old scan");
                    ic.remove(scan);
                }
                if (lastChromatogram != null) {
//                    System.out.println("Removing old chromatogram");
                    ic.remove(lastChromatogram);
                }
//                System.out.println("Removed old entities!");
                scan = ds.getTarget(e.getSeriesIndex(), e.getItem());
//                System.out.println("Retrieved scan");
                lastChromatogram = ds.getSource(e.getSeriesIndex());
//                System.out.println("Retrieved chromatogram");
//                System.out.println("Adding scan and chromatogram to instance content");
                ic.add(scan);
                ic.add(lastChromatogram);
            }
        };
        es.submit(r);
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
