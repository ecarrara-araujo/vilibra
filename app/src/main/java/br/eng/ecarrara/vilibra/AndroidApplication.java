package br.eng.ecarrara.vilibra;

import android.app.Application;

import br.eng.ecarrara.vilibra.data.cache.BookContentProviderCache;
import br.eng.ecarrara.vilibra.domain.cache.Cache;
import br.eng.ecarrara.vilibra.data.repository.BookBorrowingContentProviderRepository;
import br.eng.ecarrara.vilibra.data.repository.BookRestApiRepository;
import br.eng.ecarrara.vilibra.domain.entity.Book;
import br.eng.ecarrara.vilibra.domain.executor.Executor;
import br.eng.ecarrara.vilibra.domain.executor.ThreadExecutor;
import br.eng.ecarrara.vilibra.domain.repository.BookBorrowingRepository;
import br.eng.ecarrara.vilibra.domain.repository.BookCachedRepository;
import br.eng.ecarrara.vilibra.domain.repository.BookRepository;
import timber.log.Timber;

/**
 * Application class to handle global state objects configuration.
 */
public class AndroidApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        initializeLoggingInfrastructure();
        this.initializeServiceLocator();
    }

    private void initializeLoggingInfrastructure() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
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
