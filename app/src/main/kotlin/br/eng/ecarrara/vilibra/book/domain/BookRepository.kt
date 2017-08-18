package br.eng.ecarrara.vilibra.book.domain

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import io.reactivex.Completable
import io.reactivex.Single

interface BookRepository {

    fun byIsbn(isbn: String): Single<Book>
    fun add(book: Book): Completable

}
