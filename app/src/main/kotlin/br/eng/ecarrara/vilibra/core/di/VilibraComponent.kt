package br.eng.ecarrara.vilibra.core.di

import br.eng.ecarrara.vilibra.LendedBookListFragment
import br.eng.ecarrara.vilibra.LendedBookRegistrationFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        NetworkModule::class,
        ServicesModule::class))
interface VilibraComponent {

    fun inject(lentBookListFragment: LendedBookListFragment)
    fun inject(lendedBookRegistrationFragment: LendedBookRegistrationFragment)

}