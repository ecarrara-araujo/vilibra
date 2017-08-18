package br.eng.ecarrara.vilibra;

import br.eng.ecarrara.vilibra.domain.executor.Executor;
import br.eng.ecarrara.vilibra.domain.repository.BookBorrowingRepository;
import br.eng.ecarrara.vilibra.book.domain.BookRepository;

/**
 * Service Locator that holds instances for the main services of the application.
 * It is responsible for the all the configuration, any necessary changes can be done here and will
 * reflect in the whole app.
 * This also can be done with Dependency Injection.
 *
 */
public class ServiceLocator {

    private static ServiceLocator serviceLocatorInstance;

    private BookBorrowingRepository bookBorrowingRepository;
    private BookRepository bookRepository;
    private Executor executor;

    public static void load(ServiceLocator serviceLocator) {
        serviceLocatorInstance = serviceLocator;
    }

    public static BookBorrowingRepository bookBorrowingRepository() {
        return serviceLocatorInstance.bookBorrowingRepository;
    }

    public static BookRepository bookRepository() {
        return serviceLocatorInstance.bookRepository;
    }

    public static Executor executor() { return serviceLocatorInstance.executor; }

    public ServiceLocator(Executor executor,
                          BookBorrowingRepository bookBorrowingRepository,
                          BookRepository bookRepository) {
        this.executor = executor;
        this.bookBorrowingRepository = bookBorrowingRepository;
        this.bookRepository = bookRepository;
    }
}
