package ecarrara.eng.vilibra;

import android.test.AndroidTestCase;

import ecarrara.eng.vilibra.data.BookInfoRequester;

/**
 * Created by ecarrara on 10/12/2014.
 */
public class TestBookDataRequester extends AndroidTestCase {

    public void testBookDataRequest() {

        final String validBookISBN = "8074840204";
        String returnedJson = BookInfoRequester.requestBookData(validBookISBN);
        assertTrue("Nothing was returned from books service.", !"".equals(returnedJson));

    }

}
