package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.VilibraApplication

object VilibraInjector {

    lateinit var vilibraComponent: VilibraComponent
        private set

    fun initialize(vilibraApplication: VilibraApplication) {
        vilibraComponent = DaggerVilibraComponent.builder()
                .applicationModule(ApplicationModule(vilibraApplication))
                .build()
    }

}