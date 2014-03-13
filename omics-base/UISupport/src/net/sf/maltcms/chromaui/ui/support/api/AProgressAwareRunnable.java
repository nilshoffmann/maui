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
package net.sf.maltcms.chromaui.ui.support.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import net.sf.mpaxs.api.ICompletionService;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class AProgressAwareRunnable implements Runnable,
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

    public static Task createAndRun(String taskName,
            AProgressAwareRunnable runnable) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                taskName, runnable);
        runnable.setProgressHandle(handle);
        final RequestProcessor rp = new RequestProcessor(
                taskName);
        RequestProcessor.Task task = rp.create(runnable);
        task.schedule(0);
        return task;
    }

    public static Future<?> createAndRun(String taskName, AProgressAwareRunnable runnable, ExecutorService es) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                taskName, runnable);
        runnable.setProgressHandle(handle);
        return es.submit(runnable);
    }

    public static <T> Future<T> createAndRun(String taskName, AProgressAwareRunnable runnable, ICompletionService<T> es, T result) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                taskName, runnable);
        runnable.setProgressHandle(handle);
        return es.submit(runnable, result);
    }
}
