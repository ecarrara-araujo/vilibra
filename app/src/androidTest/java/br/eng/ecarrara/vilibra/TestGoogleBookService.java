package br.eng.ecarrara.vilibra;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.eng.ecarrara.vilibra.data.BookVolumeTestDataFactory;
import br.eng.ecarrara.vilibra.model.BookVolume;
import br.eng.ecarrara.vilibra.service.GoogleBooksService;
import br.eng.ecarrara.vilibra.service.GoogleBooksServiceInterface;
import retrofit.RestAdapter;

import static org.junit.Assert.assertTrue;

@Ignore("To be refactored with GoogleBookService class")
@RunWith(AndroidJUnit4.class)
public class TestGoogleBookService {

    @Test
    public void testSuccessfulRequest() {

        BookVolume testBookVolume = BookVolumeTestDataFactory.getTestBookVolume();
        String isbn10 = testBookVolume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier();

        // TODO: create a test network module to inject these dependencies
//        GoogleBooksService gbs = new GoogleBooksService();
//        BookVolume returnedBookVolume = gbs.lookForVolumeByISBN(isbn10);
//
//        assertTrue(testBookVolume.hasSameIndustryIdentifiers(returnedBookVolume));
    }
}
