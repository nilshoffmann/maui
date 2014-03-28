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

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;
import net.sf.mpaxs.api.ICompletionService;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;

/**
 * A java.lang.Runnable that is also a org.openide.util.Cancellable and handles
 * it's own org.netbeans.api.progress.ProgressHandle, starting and finishing it.
 *
 * Allows registration of {@link ResultListener} instances for the same generic
 * type used at instantiation time of this class. To notify listeners of the
 * result, call <code>notifyListeners(R result)</code> from within the
 * <code>body()</code> method. All registered listeners will be notified.
 *
 * @author Konstantin Otte
 * @author Nils Hoffmann
 */
public abstract class CancellableRunnable<R> implements Runnable, Cancellable {

    private HashSet<ResultListener<R>> listeners = new HashSet<>();
    private boolean updateOnEventDispatchThread = false;
    public ProgressHandle handle;

    public CancellableRunnable() {
    }

    /**
     * @param updateOnEventDispatchThread if true, each listener will be
     * notified via their own Runnable using
     * javax.swing.SwingUtilities.invokeLater()
     */
    public CancellableRunnable(boolean updateOnEventDispatchThread) {
        this.updateOnEventDispatchThread = updateOnEventDispatchThread;
    }

    public void setHandle(ProgressHandle handle) {
        this.handle = handle;
    }

    public abstract void body();

    @Override
    public void run() {
        this.handle.start();
        try {
            body();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            this.handle.finish();
        }
    }

    @Override
    public boolean cancel() {
        this.handle.finish();
        return true;
    }

    public void notifyListeners(final R result) {
        if (updateOnEventDispatchThread) {
            for (final ResultListener<R> listener : this.listeners) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        listener.listen(result);
                    }
                });
            }
            listeners.clear();
        } else {
            for (final ResultListener<R> listener : this.listeners) {
                listener.listen(result);
            }
            listeners.clear();
        }
    }

    public void addResultListener(ResultListener<R> listener) {
        this.listeners.add(listener);
    }

    public void removeResultListener(ResultListener<R> listener) {
        this.listeners.remove(listener);
    }

    public static Task createAndRun(String taskName,
            CancellableRunnable runnable) {
        final ProgressHandle ph = ProgressHandleFactory.createHandle(taskName, runnable);
        runnable.setHandle(ph);
        final RequestProcessor rp = new RequestProcessor(
                taskName);
        RequestProcessor.Task task = rp.create(runnable);
        task.schedule(0);
        return task;
    }

    public static Future<?> createAndRun(String taskName, CancellableRunnable runnable, ExecutorService es) {
        final ProgressHandle ph = ProgressHandleFactory.createHandle(taskName, runnable);
        runnable.setHandle(ph);
        return es.submit(runnable);
    }

    public static <T> Future<T> createAndRun(String taskName, CancellableRunnable runnable, ICompletionService<T> es, T result) {
        final ProgressHandle ph = ProgressHandleFactory.createHandle(taskName, runnable);
        runnable.setHandle(ph);
        return es.submit(runnable, result);
    }
}
