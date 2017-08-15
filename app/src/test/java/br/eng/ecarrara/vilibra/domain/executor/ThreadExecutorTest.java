package br.eng.ecarrara.vilibra.domain.executor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ThreadExecutorTest {

    private Executor executor;

    @Before public void setUp() throws Exception {
        this.executor = new ThreadExecutor();
    }

    @Test public void testSuccessfulInteractorExecution() {
        Interactor<Void> interactor = mock(Interactor.class);
        this.executor.execute(interactor);
        verify(interactor).operation();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullInteractorExecution() {
        this.executor.execute(null);
    }
}