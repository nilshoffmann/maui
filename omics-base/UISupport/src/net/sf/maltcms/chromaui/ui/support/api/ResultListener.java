package net.sf.maltcms.chromaui.ui.support.api;

/**
 * A listener for results of type R. Allows to register instances of this with
 * {@link CancellableRunnable} for asynchronous notification of available
 * results.
 *
 * @author Nils Hoffmann
 */
public interface ResultListener<R> {

    public void listen(R r);
}
