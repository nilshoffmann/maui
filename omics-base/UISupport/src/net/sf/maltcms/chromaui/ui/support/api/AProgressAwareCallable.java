/*
 * $license$
 *
 * $Id$
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
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
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
