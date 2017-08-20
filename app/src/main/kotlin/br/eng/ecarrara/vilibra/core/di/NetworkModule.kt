package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.service.GoogleBooksServiceInterface
import dagger.Module
import dagger.Provides
import retrofit.RestAdapter
import javax.inject.Singleton

@Module
class NetworkModule(
        private val googleBooksRestApiBaseUrl: String
) {

    @Provides
    @Singleton
    fun providesGoogleBooksRestApi(retrofitClient: RestAdapter)
            = retrofitClient.create(GoogleBooksServiceInterface::class.java)

    @Provides
    @Singleton
    fun providesRetrofitClient() =
            RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(googleBooksRestApiBaseUrl)
                    .build();

}
