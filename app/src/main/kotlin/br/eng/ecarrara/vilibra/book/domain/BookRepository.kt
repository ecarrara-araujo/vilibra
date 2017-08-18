package br.eng.ecarrara.vilibra.book.domain

import br.eng.ecarrara.vilibra.book.domain.entity.Book

interface BookRepository {

    fun byIsbn(isbn: String): Book
    fun add(book: Book)

}
