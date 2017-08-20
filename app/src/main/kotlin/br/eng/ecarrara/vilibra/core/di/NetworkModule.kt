package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.BuildConfig
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.GoogleBooksRestApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import javax.inject.Named
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
    fun providesRetrofitClient(okHttpClient: OkHttpClient) =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(googleBooksRestApiBaseUrl)
                    .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(
            @Named("loggingInterceptor") loggingInterceptor: Interceptor
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(loggingInterceptor)
        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    @Named("loggingInterceptor")
    fun providesLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

}
