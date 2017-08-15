package br.eng.ecarrara.vilibra;

import android.content.Context;
import android.test.AndroidTestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.eng.ecarrara.vilibra.utils.Utility;

/**
 * Created by ecarrara on 07/04/2015.
 */
public class UtilityTest extends AndroidTestCase {

    public void testGetFormattedDateForBookInfoExpectedFormat() {

        Context context = getContext();
        final String inputDateString = "20150403";
        final String expectedDateFormat = context.getString(R.string.full_date_format);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 4 - 1, 3); //20150403

        String expectedResult = (new SimpleDateFormat(expectedDateFormat)).format(calendar.getTime());
        String result = Utility.getFormattedDateForBookInfo(context, inputDateString);

        assertEquals("Date was not correctly formatted.", expectedResult, result);

    }
}
