package br.eng.ecarrara.vilibra.book.data.datasource

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import io.reactivex.Single

interface BookRemoteDataSource {

    fun searchForBookBy(isbn: String) : Single<Book>

}