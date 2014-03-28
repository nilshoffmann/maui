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
package net.sf.maltcms.chromaui.ui.support.api;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import net.sf.mpaxs.api.ICompletionService;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Nils Hoffmann
 */
//@Data
public abstract class AProgressAwareCallable<T> implements Callable<T>,
        Cancellable {

    protected ProgressHandle progressHandle;
    private boolean cancel = false;

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public ProgressHandle getProgressHandle() {
        return progressHandle;
    }

    public void setProgressHandle(ProgressHandle progressHandle) {
        this.progressHandle = progressHandle;
    }

    @Override
    public boolean cancel() {
        cancel = true;
        progressHandle.finish();
        return cancel;
    }

    public static <T> Future<T> createAndRun(String taskName,
            AProgressAwareCallable<T> runnable) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                taskName, runnable);
        runnable.setProgressHandle(handle);
        final RequestProcessor rp = new RequestProcessor(
                taskName);
        Future<T> task = rp.submit(runnable);
        return task;
    }

    public static <T> Future<T> createAndRun(String taskName, AProgressAwareCallable<T> runnable, ExecutorService es) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                taskName, runnable);
        runnable.setProgressHandle(handle);
        return es.submit(runnable);
    }

    public static <T> Future<T> createAndRun(String taskName, AProgressAwareCallable<T> runnable, ICompletionService<T> es) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                taskName, runnable);
        runnable.setProgressHandle(handle);
        return es.submit(runnable);
    }
}
