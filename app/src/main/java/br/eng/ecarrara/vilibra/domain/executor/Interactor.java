package br.eng.ecarrara.vilibra.domain.executor;

import br.eng.ecarrara.vilibra.domain.error.Error;

/**
 * Interactors are use case objects that holds all the business logic.
 * T defines the data type that this Interactor will be working on.
 *
 * This base class will rely on an implementation of an {@link Executor} to execute the logic.
 *
 */
public abstract class Interactor<T> {

    private Executor executor;
    private Callback<T> callback;

    public Interactor(Executor executor) {
        this.executor = executor;
    }

    /**
     * The business logic for the use case.
     */
    public abstract void operation();

    /**
     * Starts the execution of this {@link Interactor} in the provided {@link Executor} .
     */
    public void execute(final Callback<T> callback) {
        if (callback == null) {
            throw new IllegalArgumentException(
                    "Callback can't be null otherwise the client will not get the result.");
        }
        this.callback = callback;
        this.executor.execute(this);
    }

    protected void notifyError(final Error error) {
        this.callback.getCallbackThread().post(new Runnable() {
            @Override public void run() {
                Interactor.this.callback.onError(error);
            }
        });
    }

    protected void notifyFinished(final T dataProcessed) {
        this.callback.getCallbackThread().post(new Runnable() {
            @Override public void run() {
                Interactor.this.callback.onFinished(dataProcessed);
            }
        });
    }

}
