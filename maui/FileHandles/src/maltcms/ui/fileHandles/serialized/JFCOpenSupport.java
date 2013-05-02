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
package maltcms.ui.fileHandles.serialized;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.jfree.chart.JFreeChart;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.util.Exceptions;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mw
 */
class JFCOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public JFCOpenSupport(JFCDataObject.Entry entry) {
        super(entry);
    }

    protected CloneableTopComponent createCloneableTopComponent() {
        final JFCDataObject dobj = (JFCDataObject) entry.getDataObject();
        final JFCTopComponent tc = new JFCTopComponent();
        tc.setDisplayName(dobj.getName());

        final ProgressHandle ph = ProgressHandleFactory.createHandle("Loading file " + dobj.getPrimaryFile().getName());
        tc.setDisplayName(dobj.getPrimaryFile().getName());
        final ExecutorService es = Executors.newSingleThreadExecutor();

        final Future<JFreeChart> f = es.submit(new JFCLoader(dobj.getPrimaryFile().getPath(), ph));
        try {
            tc.setChart(f.get());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            ph.finish();
        }

        return tc;
    }
}
