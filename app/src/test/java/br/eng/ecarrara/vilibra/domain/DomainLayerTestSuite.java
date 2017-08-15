package br.eng.ecarrara.vilibra.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.eng.ecarrara.vilibra.domain.executor.ThreadExecutor;
import br.eng.ecarrara.vilibra.domain.executor.ThreadExecutorTest;
import br.eng.ecarrara.vilibra.domain.presentation.presenter.BorrowedBooksPresenterTest;
import br.eng.ecarrara.vilibra.domain.repository.BookCachedRepositoryTest;
import br.eng.ecarrara.vilibra.domain.usecase.ListBookBorrowingsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        /* Repositories */
        BookCachedRepositoryTest.class,

        /* Presenters */
        BorrowedBooksPresenterTest.class,

        /* Executor */
        ThreadExecutorTest.class,

        /* Use Cases */
        ListBookBorrowingsTest.class
})
public class DomainLayerTestSuite {
}
