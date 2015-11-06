package ecarrara.eng.vilibra;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ecarrara.eng.vilibra.data.VilibraContract;

import static org.junit.Assert.assertTrue;

/**
 * Created by ecarrara on 20/07/2015.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TestLendBookFlow {

    private UiDevice mUiDevice;
    private Context mContext;
    private static final String BOOK_ISBN = "8575224123";
    private static final String BOOK_TITLE = "Dominando o Android";

    @Before
    public void setUp() throws UiObjectNotFoundException {
        mContext = InstrumentationRegistry.getContext();

        clearTestData();
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Should start from the home screen
        mUiDevice.pressHome();

        UiObject allAppsButton = mUiDevice.findObject(new UiSelector()
                .description("Apps"));
        assertTrue(allAppsButton.exists());
        allAppsButton.clickAndWaitForNewWindow();

        UiObject appsTab = mUiDevice.findObject(new UiSelector()
                .text("Apps"));
        assertTrue(appsTab.exists());
        appsTab.click();

        // find the scrollable list of apps
        UiScrollable appsList = new UiScrollable(new UiSelector()
                .scrollable(true));
        appsList.setAsHorizontalList();

        UiObject vilibraApp = appsList.getChildByText(new UiSelector()
                .className("android.widget.TextView"), "ViLibra");
        vilibraApp.click();

        mUiDevice.wait(Until.hasObject(By.pkg("ecarrara.eng.vilibra")), 5000L);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void testLendBookFlow() throws UiObjectNotFoundException {

        UiObject lendBookButton = mUiDevice.findObject(new UiSelector()
                .resourceId("ecarrara.eng.vilibra:id/add_lending_action_button"));
        lendBookButton.clickAndWaitForNewWindow();

        UiObject isbnEditText = mUiDevice.findObject(new UiSelector()
                .text("ISBN")
                .className("android.widget.EditText"));
        isbnEditText.setText(BOOK_ISBN);

        UiObject confirmButton = mUiDevice.findObject(new UiSelector()
                .text("Confirm")
                .className("android.widget.Button"));
        confirmButton.clickAndWaitForNewWindow(5000);

        UiObject isbnTextView = mUiDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId("ecarrara.eng.vilibra:id/book_isbn10_text_view"));
        Assert.assertTrue(isbnTextView.getText().contains(BOOK_ISBN));

        UiObject lendButton = mUiDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .text("Lend this Book"));
        lendButton.clickAndWaitForNewWindow();

        UiObject contactView = mUiDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .text("Meu Irmao"));
        contactView.click();

        mUiDevice.wait(Until.hasObject(By.pkg("ecarrara.eng.vilibra")), 500L);

        UiObject bookTitleTextView = mUiDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId("ecarrara.eng.vilibra:id/book_name_text_view")
                .text(BOOK_TITLE));

        assertTrue(bookTitleTextView.exists());

    }

    private void clearTestData() {
        mContext.getContentResolver().delete(
                VilibraContract.LendingEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                VilibraContract.BookEntry.CONTENT_URI,
                null,
                null
        );
    }

}
