package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.data.cache.BookContentProviderCache
import br.eng.ecarrara.vilibra.data.repository.BookBorrowingContentProviderRepository
import br.eng.ecarrara.vilibra.data.repository.BookRestApiRepository
import br.eng.ecarrara.vilibra.domain.cache.Cache
import br.eng.ecarrara.vilibra.domain.executor.Executor
import br.eng.ecarrara.vilibra.domain.executor.ThreadExecutor
import br.eng.ecarrara.vilibra.domain.repository.BookBorrowingRepository
import br.eng.ecarrara.vilibra.domain.repository.BookCachedRepository
import br.eng.ecarrara.vilibra.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class ServicesModule() {

    @Binds
    @Named("repository.cached")
    abstract fun providesBookRepository(
            bookCachedRepository: BookCachedRepository
    ): BookRepository

    @Binds
    @Named("repository.restapi")
    abstract fun providesBookRestApiRepository(
            bookRestApiRepository: BookRestApiRepository
    ): BookRepository

    @Binds
    abstract fun providesBookLocalCache(
            bookContentProviderCache: BookContentProviderCache
    ): Cache<String, Book>

    @Binds
    abstract fun providesBookBorrowingRepository(
            bookBorrowingContentProviderRepository: BookBorrowingContentProviderRepository
    ): BookBorrowingRepository

    @Binds
    abstract fun providesExecutor(threadExecutor: ThreadExecutor): Executor

}