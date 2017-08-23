package br.eng.ecarrara.vilibra.book.domain

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Defines an interface to provide access to a repository of [Book]
 */
interface BookRepository {

    fun getByIsbn(isbn: String): Single<Book>
    fun add(book: Book): Completable

}
