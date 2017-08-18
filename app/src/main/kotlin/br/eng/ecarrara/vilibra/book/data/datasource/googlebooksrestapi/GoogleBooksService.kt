package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolumeCollection

class GoogleBooksService {

    private lateinit var mGoogleBooksServiceInterface: GoogleBooksRestApi

    fun lookForVolumeByISBN(isbn: String): JsonBookVolume? {
        val query = formatQueryForISBNSearch(isbn)
        val bookVolumeCollection = lookForVolumesWithQuery(query)
        var jsonBookVolume: JsonBookVolume? = null
        if (null != bookVolumeCollection) {
            if (bookVolumeCollection.items.size > 0) {
                jsonBookVolume = bookVolumeCollection.items[0]
            }
        }
        return jsonBookVolume
    }

    fun lookForVolumesWithQuery(query: String): JsonBookVolumeCollection? {
        return null
    }

    private fun formatQueryForISBNSearch(isbn: String): String {
        val ISBN_QUERY = "isbn:"
        return ISBN_QUERY + isbn
    }

}
