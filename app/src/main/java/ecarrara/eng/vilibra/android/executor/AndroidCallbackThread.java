package ecarrara.eng.vilibra.android.executor;

import android.os.Handler;

import ecarrara.eng.vilibra.domain.executor.CallbackThread;

/**
 * A {@link CallbackThread} mechanism based on Android Handlers.
 */
public class AndroidCallbackThread implements CallbackThread {

    private Handler handler;

    public AndroidCallbackThread() {
        this.handler = new Handler();
    }

    @Override public void post(Runnable runnable) {
        this.handler.post(runnable);
    }
}
