package br.eng.ecarrara.vilibra.book.data

import br.eng.ecarrara.vilibra.book.data.datasource.BookLocalCache
import br.eng.ecarrara.vilibra.book.data.datasource.BookRemoteDataSource
import br.eng.ecarrara.vilibra.book.domain.BookRepository
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.book.domain.entity.BookWithNotValidIsbnException
import br.eng.ecarrara.vilibra.core.data.RxCache
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

/**
 * Concrete implementation of [BookRepository] that combines an implementation of
 * [RxCache] with an implementation of [BookRepository]
 *
 */
class BookCachedRepository
@Inject constructor(
        private val bookRemoteDataSource: BookRemoteDataSource,
        private val bookLocalCache: BookLocalCache
) : BookRepository {

    override fun getByIsbn(isbn: String): Single<Book> {
        return Single
                .concat(bookLocalCache[isbn], getFromRemoteDataSourceAndStoreToCache(isbn))
                .doOnComplete { Timber.d("Current Thread: " + Thread.currentThread().name) }
                .firstOrError()
    }

    private fun getFromRemoteDataSourceAndStoreToCache(isbn: String) = bookRemoteDataSource
            .searchForBookBy(isbn)
            .flatMap { book -> add(book).andThen(bookLocalCache[book.isbn10]) }

    override fun add(book: Book) = bookLocalCache.put(getCacheKeyForBook(book), book)

    private fun getCacheKeyForBook(book: Book): String {
        with(book, {
            return when {
                isbn10.isNotEmpty() -> isbn10
                isbn13.isNotEmpty() -> isbn13
                else -> throw BookWithNotValidIsbnException()
            }
        })
    }
}
