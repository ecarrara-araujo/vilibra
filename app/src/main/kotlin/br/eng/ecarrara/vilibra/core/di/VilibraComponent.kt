package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.LendedBookListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        NetworkModule::class,
        ServicesModule::class))
interface VilibraComponent {

    fun inject(lentBookListFragment: LendedBookListFragment)

}