package br.eng.ecarrara.vilibra.android.presentation;

import android.content.ContentValues;
import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.eng.ecarrara.vilibra.BookListActivity;
import br.eng.ecarrara.vilibra.R;
import br.eng.ecarrara.vilibra.data.VilibraContract;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class TestEmptyLoanedBooksList {

    private Context context;

    @Rule public ActivityTestRule<BookListActivity> activityRule =
            new ActivityTestRule<>(BookListActivity.class);

    @Before public void setUp() {
        this.context = getTargetContext();
        clearTestData();
    }

    @Test public void testEmptyStateOfLoanedBooksList() {

        onView(withId(R.id.loaned_book_list_view)).check(matches(not(isDisplayed())));

        onView(withId(R.id.empty_book_list_icon)).check(matches(isDisplayed()));

        ViewInteraction emptyBookListMessage = onView(withId(R.id.empty_book_list_message_text_view));
        emptyBookListMessage.check(matches(isDisplayed()));
        emptyBookListMessage.check(
                matches(withText(this.context.getString(R.string.empty_book_list_message))));

        ViewInteraction emptyBookListUserDirections =
                onView(withId(R.id.empty_book_list_user_directions_text_view));
        emptyBookListUserDirections.check(matches(isDisplayed()));
        emptyBookListUserDirections.check(
                matches(withText(this.context.getString(R.string.add_book_message))));
    }

    private void clearTestData() {
        this.context.getContentResolver().delete(
                VilibraContract.LendingEntry.CONTENT_URI,
                null,
                null
        );
        this.context.getContentResolver().delete(
                VilibraContract.BookEntry.CONTENT_URI,
                null,
                null
        );
    }
}
