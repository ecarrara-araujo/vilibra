package ecarrara.eng.vilibra.domain.executor;

import ecarrara.eng.vilibra.domain.error.Error;

/**
 * Callback interface to be used by {@link Interactor} to notify the requester.
 */
public interface Callback <D> {

    CallbackThread getCallbackThread();
    void onFinished(D processed);
    void onError(Error error);

}
