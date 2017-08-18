package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.GoogleBooksRestApi
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule(
        private val googleBooksRestApiBaseUrl: String
) {

    @Provides
    @Singleton
    fun providesGoogleBooksRestApi(retrofitClient: Retrofit)
            = retrofitClient.create(GoogleBooksRestApi::class.java)

    @Provides
    @Singleton
    fun providesRetrofitClient() = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(googleBooksRestApiBaseUrl)
            .build()

}
