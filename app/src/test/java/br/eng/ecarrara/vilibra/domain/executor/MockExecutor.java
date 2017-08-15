package br.eng.ecarrara.vilibra.domain.executor;

/**
 * Mock an {@link Interactor} {@link Executor} simply executing the Interactor sequentially.
 */
public class MockExecutor implements Executor {

    @Override public void execute(Interactor interactor) {
        interactor.operation();
    }

}
