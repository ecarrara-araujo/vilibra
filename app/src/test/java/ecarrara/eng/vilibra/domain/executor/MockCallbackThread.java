package ecarrara.eng.vilibra.domain.executor;

/**
 *
 */
public class MockCallbackThread implements CallbackThread {
    @Override public void post(Runnable runnable) {
        runnable.run();
    }
}
