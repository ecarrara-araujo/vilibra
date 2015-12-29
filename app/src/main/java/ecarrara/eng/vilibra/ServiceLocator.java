package ecarrara.eng.vilibra;

import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;
import ecarrara.eng.vilibra.domain.repository.BookRepository;

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

    public static void load(ServiceLocator serviceLocator) {
        serviceLocatorInstance = serviceLocator;
    }

    public static BookBorrowingRepository bookBorrowingRepository() {
        return serviceLocatorInstance.bookBorrowingRepository;
    }

    public static BookRepository bookRepository() {
        return serviceLocatorInstance.bookRepository;
    }

    public ServiceLocator(BookBorrowingRepository bookBorrowingRepository,
                          BookRepository bookRepository) {
        this.bookBorrowingRepository = bookBorrowingRepository;
        this.bookRepository = bookRepository;
    }
}
