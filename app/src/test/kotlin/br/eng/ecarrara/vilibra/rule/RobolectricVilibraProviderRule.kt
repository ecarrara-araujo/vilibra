package br.eng.ecarrara.vilibra.rule

import br.eng.ecarrara.vilibra.data.VilibraContract.CONTENT_AUTHORITY
import br.eng.ecarrara.vilibra.data.VilibraProvider
import br.eng.ecarrara.vilibra.fakedata.VilibraProviderFakeDataInitializer
import org.junit.rules.ExternalResource
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowContentResolver

class RobolectricVilibraProviderRule : ExternalResource() {

    val vilibraProvider: VilibraProvider
    val vilibraProviderFakeDataInitializer: VilibraProviderFakeDataInitializer

    init {
        vilibraProvider = Robolectric.setupContentProvider(VilibraProvider::class.java)
        vilibraProviderFakeDataInitializer = VilibraProviderFakeDataInitializer(RuntimeEnvironment.application)
    }

    override fun before() {
        ShadowContentResolver.registerProviderInternal(CONTENT_AUTHORITY, vilibraProvider)
    }

    override fun after() {
        vilibraProviderFakeDataInitializer.clearContentProviderData()
    }

}