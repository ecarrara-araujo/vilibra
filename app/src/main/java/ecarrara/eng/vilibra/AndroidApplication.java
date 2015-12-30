package ecarrara.eng.vilibra;

import android.app.Application;

import ecarrara.eng.vilibra.data.cache.BookContentProviderCache;
import ecarrara.eng.vilibra.domain.cache.Cache;
import ecarrara.eng.vilibra.data.repository.BookBorrowingContentProviderRepository;
import ecarrara.eng.vilibra.data.repository.BookRestApiRepository;
import ecarrara.eng.vilibra.domain.entity.Book;
import ecarrara.eng.vilibra.domain.executor.Executor;
import ecarrara.eng.vilibra.domain.executor.ThreadExecutor;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;
import ecarrara.eng.vilibra.domain.repository.BookCachedRepository;
import ecarrara.eng.vilibra.domain.repository.BookRepository;

/**
 * Application class to handle global state objects configuration.
 */
public class AndroidApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        this.initializeServiceLocator();
    }

    private void initializeServiceLocator() {
        BookRepository bookRepository = initializeBookRepository();
        BookBorrowingRepository bookBorrowingRepository = initializeBookBorrowingRepository();
        Executor executor = new ThreadExecutor();

        ServiceLocator serviceLocator = new ServiceLocator(
                executor, bookBorrowingRepository, bookRepository);
        ServiceLocator.load(serviceLocator);
    }

    private BookRepository initializeBookRepository() {
        BookRepository restApiBookRepository = new BookRestApiRepository();
        Cache<String, Book> bookCache = new BookContentProviderCache(getApplicationContext());
        BookRepository bookCachedRepository =
                new BookCachedRepository(restApiBookRepository, bookCache);

        return bookCachedRepository;
    }

    private BookBorrowingRepository initializeBookBorrowingRepository() {
        return new BookBorrowingContentProviderRepository(
                getApplicationContext().getContentResolver());
    }


}
