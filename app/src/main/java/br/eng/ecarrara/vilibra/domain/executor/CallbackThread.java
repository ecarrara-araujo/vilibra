package br.eng.ecarrara.vilibra.domain.executor;

/**
 * Thread provided for the callback in which the callback methods must be executed.
 */
public interface CallbackThread {

    void post(Runnable runnable);

}
