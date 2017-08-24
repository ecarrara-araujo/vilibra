package br.eng.ecarrara.vilibra.robot

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import br.eng.ecarrara.vilibra.R
import org.hamcrest.Matchers

fun lentBooks(func: LentBooksRobot.() -> Unit) = LentBooksRobot().apply { func() }

class LentBooksRobot {

    fun checkForBookTitle(expectedTitle: String): LentBooksRobot {
        Espresso.onView(
                Matchers.allOf<View>(
                        ViewMatchers.withId(R.id.book_name_text_view),
                        ViewMatchers.hasSibling(ViewMatchers.withText(expectedTitle))
                ))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun checkForBookAuthors(expectedBookAuthors: String): LentBooksRobot {
       Espresso.onView(
               Matchers.allOf<View>(
                       ViewMatchers.withId(R.id.book_author_text_view),
                       ViewMatchers.hasSibling(ViewMatchers.withText(expectedBookAuthors))))
               .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun addBookLending() {
        Espresso.onView(ViewMatchers.withId(R.id.add_lending_action_button))
                .perform(ViewActions.click())
    }

}
