package br.eng.ecarrara.vilibra.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module
class ApplicationModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun providesApplicationContext() = applicationContext

}