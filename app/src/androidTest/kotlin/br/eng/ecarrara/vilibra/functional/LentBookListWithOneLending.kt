package br.eng.ecarrara.vilibra.functional

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.AndroidJUnit4
import android.view.View
import br.eng.ecarrara.vilibra.BookListActivity
import br.eng.ecarrara.vilibra.R
import br.eng.ecarrara.vilibra.robot.lentBooks
import br.eng.ecarrara.vilibra.rule.ActivityWithFakeDataPreLoadedTestRule
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LentBookListWithOneLending {

    @get:Rule
    var testDataRule = ActivityWithFakeDataPreLoadedTestRule(BookListActivity::class.java)

    @Test
    fun whenOpened_withOneBookLending_displayBookLendingCorrectly_withRobot() {
        val fakeBook = testDataRule.fakeBook
        lentBooks {
            checkForBookTitle(fakeBook.title)
            checkForBookAuthors(fakeBook.authors.joinToString(","))
        }
    }

    @Test
    fun whenOpened_withOneBookLending_displayBookLendingCorrectly_pureEspresso() {
        val fakeBook = testDataRule.fakeBook
        Espresso.onView(
                Matchers.allOf<View>(
                        ViewMatchers.withId(R.id.book_name_text_view),
                        ViewMatchers.hasSibling(ViewMatchers.withText(fakeBook.title))
                ))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(
                Matchers.allOf<View>(
                        ViewMatchers.withId(R.id.book_author_text_view),
                        ViewMatchers.hasSibling(ViewMatchers.withText(fakeBook.authors.joinToString(",")))))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}