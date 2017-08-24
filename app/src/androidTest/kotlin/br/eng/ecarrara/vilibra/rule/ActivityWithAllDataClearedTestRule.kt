package br.eng.ecarrara.vilibra.rule

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.fakedata.VilibraProviderFakeDataInitializer

class ActivityWithAllDataClearedTestRule<T : Activity>(
        activityClass: Class<T>
) : ActivityTestRule<T>(activityClass) {

    private val vilibraProviderFakeDataInitializer by lazy {
        VilibraProviderFakeDataInitializer(InstrumentationRegistry.getContext())
    }

    override fun beforeActivityLaunched() {
        vilibraProviderFakeDataInitializer.clearContentProviderData()
    }

    override fun afterActivityFinished() {
        vilibraProviderFakeDataInitializer.clearContentProviderData()
    }

}