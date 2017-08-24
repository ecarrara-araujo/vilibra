package br.eng.ecarrara.vilibra.robot

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import br.eng.ecarrara.vilibra.R

fun searchBook(func: SearchBookRobot.() -> Unit) = SearchBookRobot().apply { func() }

class SearchBookRobot {

    fun fillIsbn(isbn: String): SearchBookRobot {
        Espresso.onView(ViewMatchers.withId(R.id.isbn_edit_text))
                .perform(ViewActions.typeText(isbn), ViewActions.closeSoftKeyboard())
        return this
    }

    fun confirm() {
        Espresso.onView(ViewMatchers.withId(R.id.confirm_button))
                .perform(ViewActions.click())
    }

}