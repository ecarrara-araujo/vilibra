package br.eng.ecarrara.vilibra;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import br.eng.ecarrara.vilibra.data.BookVolumeTestDataFactory;
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.GoogleBooksService;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestGoogleBookService {

    @Test
    public void testSuccessfulRequest() {

        JsonBookVolume testJsonBookVolume = BookVolumeTestDataFactory.getTestBookVolume();
        String isbn10 = testJsonBookVolume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier();

        GoogleBooksService gbs = new GoogleBooksService();
        JsonBookVolume returnedJsonBookVolume = gbs.lookForVolumeByISBN(isbn10);

        assertTrue(testJsonBookVolume.equals(returnedJsonBookVolume));
    }
}
