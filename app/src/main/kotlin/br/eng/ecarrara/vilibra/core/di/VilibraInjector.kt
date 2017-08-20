package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.VilibraApplication
import br.eng.ecarrara.vilibra.core.networking.BOOKS_INFORMATION_SERVICE_BASE_URL

object VilibraInjector {

    lateinit var vilibraComponent: VilibraComponent
        private set

    fun initialize(vilibraApplication: VilibraApplication) {
        vilibraComponent = DaggerVilibraComponent.builder()
                .applicationModule(ApplicationModule(vilibraApplication))
                .networkModule(NetworkModule(BOOKS_INFORMATION_SERVICE_BASE_URL))
                .build()
    }

}