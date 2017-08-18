package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.data.repository.BookBorrowingContentProviderRepository
import br.eng.ecarrara.vilibra.domain.executor.Executor
import br.eng.ecarrara.vilibra.domain.executor.ThreadExecutor
import br.eng.ecarrara.vilibra.domain.repository.BookBorrowingRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ServicesModule() {

    @Binds
    abstract fun providesBookBorrowingRepository(
            bookBorrowingContentProviderRepository: BookBorrowingContentProviderRepository
    ): BookBorrowingRepository

    @Binds
    abstract fun providesExecutor(threadExecutor: ThreadExecutor): Executor

}