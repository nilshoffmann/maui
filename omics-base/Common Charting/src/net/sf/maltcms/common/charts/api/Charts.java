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
package net.sf.maltcms.common.charts.api;

import java.beans.IntrospectionException;
import static javax.swing.SwingUtilities.invokeLater;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.overlay.ChartOverlay;
import net.sf.maltcms.common.charts.overlay.nodes.OverlayNode;
import net.sf.maltcms.common.charts.ui.XYChartTopComponent;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import static org.openide.util.Exceptions.printStackTrace;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Nils Hoffmann
 */
public class Charts {

    public static Node overlayNode(ChartOverlay chartOverlay) {
        try {
            OverlayNode<ChartOverlay> node = new OverlayNode<>(chartOverlay);
            return node;
        } catch (IntrospectionException ex) {
            printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    public static Node overlayNode(ChartOverlay chartOverlay, Children children) {
        try {
            OverlayNode<ChartOverlay> node = new OverlayNode<>(chartOverlay, children);
            return node;
        } catch (IntrospectionException ex) {
            printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    public static Node overlayNode(ChartOverlay chartOverlay, Children children, Lookup lookup) {
        try {
            OverlayNode<ChartOverlay> node = new OverlayNode<>(chartOverlay, children, lookup);
            return node;
        } catch (IntrospectionException ex) {
            printStackTrace(ex);
        }
        return Node.EMPTY;
    }

    public static <TARGET> void openXYChart(final Class<TARGET> typeClass, final ADataset1D<?, TARGET> dataset, final XYChartBuilder builder, final TaskListener listener) {
        Task t = RequestProcessor.getDefault().create(new Runnable() {
            @Override
            public void run() {
                invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        TopComponent tc = WindowManager.getDefault().findTopComponent("navigatorTC");
                        if (tc != null) {
                            tc.open();
                        }
                    }
                });
                invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        XYChartTopComponent<TARGET> xytc = new XYChartTopComponent<>(typeClass, dataset, builder);
                        xytc.open();
                        xytc.requestActive();
                    }
                });

            }
        });
        t.addTaskListener(listener);
        RequestProcessor.getDefault().post(t);
    }

}
