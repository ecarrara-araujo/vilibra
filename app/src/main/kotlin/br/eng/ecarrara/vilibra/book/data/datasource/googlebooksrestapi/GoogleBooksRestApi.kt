package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi

import br.eng.ecarrara.vilibra.core.networking.BOOKS_INFORMATION_SERVICE_API_KEY
import br.eng.ecarrara.vilibra.model.BookVolumeCollection
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksRestApi {

    @GET("volumes")
    fun searchVolumeData(
            @Query("q") query: String,
            @Query("key") key: String = BOOKS_INFORMATION_SERVICE_API_KEY
    ): Call<BookVolumeCollection>

}