/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.ui.support.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import net.sf.maltcms.execution.api.ICompletionService;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
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
    
    public static <T> Future<T> createAndRun(String taskName, AProgressAwareCallable<T> runnable, ICompletionService<T> es) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                taskName, runnable);
        runnable.setProgressHandle(handle);
        return es.submit(runnable);
    }
}
