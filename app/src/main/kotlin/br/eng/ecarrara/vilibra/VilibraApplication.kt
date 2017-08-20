package br.eng.ecarrara.vilibra

import android.app.Application
import br.eng.ecarrara.vilibra.core.di.VilibraInjector
import timber.log.Timber

/**
 * Application class to handle global state objects configuration.
 */
class VilibraApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeLoggingInfrastructure()
        VilibraInjector.initialize(this)
    }

    private fun initializeLoggingInfrastructure() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}
