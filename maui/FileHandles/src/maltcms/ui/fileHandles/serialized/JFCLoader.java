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
package maltcms.ui.fileHandles.serialized;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;
import org.jfree.chart.JFreeChart;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author Mathias Wilhelm
 */
public class JFCLoader implements Callable<JFreeChart> {

    private final String filename;
    private final ProgressHandle ph;

    JFCLoader(String filename, ProgressHandle ph) {
        this.filename = filename;
        this.ph = ph;
    }

    @Override
    public JFreeChart call() throws Exception {
        ph.start(100);
        JFreeChart chart = null;
        try {
            ph.progress("Opening file", 33);
            try (ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(
                            filename)))) {
                ph.progress("Reading object", 66);
                chart = (JFreeChart) ois.readObject();
            }
        } catch (final ClassNotFoundException e) {
            ph.progress("ClassNotFoundException", 80);
            e.printStackTrace();
        } catch (final IOException e) {
            ph.progress("IOException", 80);
            e.printStackTrace();
        }
        ph.progress("Finished!", 100);
        ph.finish();

        return chart;
    }
}
