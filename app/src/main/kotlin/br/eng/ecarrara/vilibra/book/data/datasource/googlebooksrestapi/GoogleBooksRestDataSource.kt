package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi

import br.eng.ecarrara.vilibra.book.data.datasource.BookRemoteDataSource
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import io.reactivex.Maybe
import javax.inject.Inject

class GoogleBooksRestDataSource
@Inject constructor(
        private val googleBooksRestApi: GoogleBooksRestApi
) : BookRemoteDataSource {

    override fun searchForBookBy(isbn: String): Maybe<Book> {
        return googleBooksRestApi.searchVolumeData("isbn:$isbn")
                .flatMapMaybe { (jsonBookVolumes) ->
                    when {
                        jsonBookVolumes.isEmpty() -> Maybe.empty<Book>()
                        else -> Maybe.just(jsonBookVolumes[0].toBook())
                    }
                }
    }

}