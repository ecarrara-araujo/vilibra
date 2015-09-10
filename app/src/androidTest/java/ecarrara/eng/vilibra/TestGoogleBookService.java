package ecarrara.eng.vilibra;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ecarrara.eng.vilibra.data.BookVolumeTestDataFactory;
import ecarrara.eng.vilibra.model.BookVolume;
import ecarrara.eng.vilibra.service.GoogleBooksService;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestGoogleBookService {

    @Test
    public void testSuccessfulRequest() {

        BookVolume testBookVolume = BookVolumeTestDataFactory.getTestBookVolume();
        String isbn10 = testBookVolume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier();

        GoogleBooksService gbs = new GoogleBooksService();
        BookVolume returnedBookVolume = gbs.lookForVolumeByISBN(isbn10);

        assertTrue(testBookVolume.hasSameIndustryIdentifiers(returnedBookVolume));
    }
}
