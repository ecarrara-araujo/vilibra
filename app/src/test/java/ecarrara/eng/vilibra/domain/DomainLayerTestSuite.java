package ecarrara.eng.vilibra.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ecarrara.eng.vilibra.domain.executor.ThreadExecutor;
import ecarrara.eng.vilibra.domain.executor.ThreadExecutorTest;
import ecarrara.eng.vilibra.domain.presentation.presenter.BorrowedBooksPresenterTest;
import ecarrara.eng.vilibra.domain.repository.BookCachedRepositoryTest;
import ecarrara.eng.vilibra.domain.usecase.ListBookBorrowingsTest;

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
