package br.eng.ecarrara.vilibra.book.data.datasource

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import io.reactivex.Maybe

interface BookRemoteDataSource {

    fun searchForBookBy(isbn: String) : Maybe<Book>

}