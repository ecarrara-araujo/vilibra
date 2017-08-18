package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi

import br.eng.ecarrara.vilibra.BuildConfig
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolumeCollection
import br.eng.ecarrara.vilibra.core.networking.BOOKS_INFORMATION_SERVICE_API_KEY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksRestApi {

    @GET("/volumes")
    fun searchVolumeData(
            @Query("q") query: String,
            @Query("key") key: String = BOOKS_INFORMATION_SERVICE_API_KEY
    ): Single<JsonBookVolumeCollection>

}
