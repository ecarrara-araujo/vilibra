package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi

import br.eng.ecarrara.vilibra.book.data.datasource.BookRemoteDataSource
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.book.domain.entity.BookInformationNotFoundException
import io.reactivex.Single
import javax.inject.Inject

class GoogleBooksRestDataSource
@Inject constructor(
        private val googleBooksRestApi: GoogleBooksRestApi
) : BookRemoteDataSource {

    override fun searchForBookBy(isbn: String): Single<Book> {
        return googleBooksRestApi.searchVolumeData("isbn:$isbn")
                .map { (jsonBookVolumes) ->
                    when {
                        jsonBookVolumes.isEmpty() -> throw BookInformationNotFoundException()
                        else -> jsonBookVolumes[0].toBook()
                    }
                }
    }

}