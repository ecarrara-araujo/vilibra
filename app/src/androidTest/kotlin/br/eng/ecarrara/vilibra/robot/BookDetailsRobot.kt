package br.eng.ecarrara.vilibra.robot

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import br.eng.ecarrara.vilibra.R
import org.hamcrest.Matchers

fun bookDetails(func: BookDetailsRobot.() -> Unit) = BookDetailsRobot().apply { func() }

class BookDetailsRobot {

    fun checkForBookTitle(expectedTitle: String): BookDetailsRobot {
        Espresso.onView(ViewMatchers.withId(R.id.book_title_text_view))
                .check(ViewAssertions.matches(ViewMatchers.withText(expectedTitle)))
        return this
    }

    fun checkForBookIsbn10(expectedIsbn: String): BookDetailsRobot {
        Espresso.onView(ViewMatchers.withId(R.id.book_isbn10_text_view))
                .check(ViewAssertions.matches(ViewMatchers.withText(expectedIsbn)))
        return this
    }

    fun checkForBookIsbn13(expectedIsbn: String): BookDetailsRobot {
        Espresso.onView(ViewMatchers.withId(R.id.book_isbn13_text_view))
                .check(ViewAssertions.matches(ViewMatchers.withText(expectedIsbn)))
        return this
    }

    fun lend() {
        Espresso.onView(ViewMatchers.withId(R.id.lend_book_button))
                .perform(ViewActions.click())
    }

}