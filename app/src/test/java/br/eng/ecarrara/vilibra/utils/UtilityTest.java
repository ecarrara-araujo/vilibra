package br.eng.ecarrara.vilibra.utils;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilityTest extends TestCase {

    public void testIfGetFormattedMonthDayReturnExpectedFormat() throws Exception {

        String inputDateString = "20150403";
        String expectedDateFormat = "yyyy, MMMM dd";

        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 4 - 1, 3); //20150403

        String expectedResult = (new SimpleDateFormat(expectedDateFormat)).format(calendar.getTime());
        String result = Utility.getFormattedMonthDay(expectedDateFormat, inputDateString);

        assertEquals("Date was not correctly formatted.", expectedResult, result);

        // testing a second format to be sure
        expectedDateFormat = "dd MM yyyy";
        expectedResult = (new SimpleDateFormat(expectedDateFormat)).format(calendar.getTime());
        result = Utility.getFormattedMonthDay(expectedDateFormat, inputDateString);
        assertEquals("Date was not correctly formatted.", expectedResult, result);

    }

    public void testGetFormattedMonthDayForBadInput() throws Exception {

        String inputDateString = "";
        final String expectedDateFormat = "yyyy, MMMM dd";
        final String expectedResult = "";

        String result = Utility.getFormattedMonthDay(expectedDateFormat, inputDateString);
        assertEquals("A wrong formatted input must return an empty String", expectedResult, result);

        inputDateString = "98734";
        result = Utility.getFormattedMonthDay(expectedDateFormat, inputDateString);
        assertEquals("A wrong formatted input must return an empty String", expectedResult, result);

        inputDateString = null;
        result = Utility.getFormattedMonthDay(expectedDateFormat, inputDateString);
        assertEquals("A wrong formatted input must return an empty String", expectedResult, result);

    }
}