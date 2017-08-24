package br.eng.ecarrara.vilibra.robot

import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import android.support.test.uiautomator.Until

fun contacts(func: ContactsRobot.() -> Unit) = ContactsRobot().apply { func() }

class ContactsRobot {

    fun pickContact(contactName: String) {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val contactView = uiDevice.findObject(
                UiSelector().className("android.widget.TextView").text(contactName))
        contactView.click()
        uiDevice.wait(Until.hasObject(By.pkg("br.eng.ecarrara.vilibra")), 500L)
    }

}