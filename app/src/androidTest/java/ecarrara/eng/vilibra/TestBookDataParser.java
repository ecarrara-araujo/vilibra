package ecarrara.eng.vilibra;

import android.content.ContentValues;
import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONException;

import ecarrara.eng.vilibra.data.GoogleBooksJsonDataParser;
import ecarrara.eng.vilibra.testutils.TestDataHelper;

/**
 * Created by ecarrara on 11/12/2014.
 */
public class TestBookDataParser extends AndroidTestCase {

    private final String LOG_TAG = TestBookDataParser.class.getSimpleName();

    public void testBookDataJsonParser() throws JSONException {

        ContentValues resultingValues = (new GoogleBooksJsonDataParser()).parse(TEST_BOOK_JSON);
        ContentValues expectedValues = TestDataHelper.createAndroidRecipesValues();

        Log.d(LOG_TAG, "ResultingValues: " + resultingValues.toString());
        Log.d(LOG_TAG, "ExpectedValues: " + expectedValues.toString());

        assertTrue("The parsed ContentValues does not match the expected values",
                expectedValues.equals(resultingValues));

    }

    private final String TEST_BOOK_JSON = "{\n" +
            " \"kind\": \"books#volumes\",\n" +
            " \"totalItems\": 1,\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"P6yDPLVEfsEC\",\n" +
            "   \"etag\": \"9uI7yYKo2EE\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/P6yDPLVEfsEC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Android Recipes\",\n" +
            "    \"subtitle\": \"A Problem-Solution Approach\",\n" +
            "    \"authors\": [\n" +
            "     \"Jeff Friesen\",\n" +
            "     \"Dave Smith\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Apress\",\n" +
            "    \"publishedDate\": \"2011-05-01\",\n" +
            "    \"description\": \"Android continues to be one of the leading mobile OS and development platforms driving today's mobile innovations and the apps ecosystem. Android appears complex, but offers a variety of organized development kits to those coming into Android with differing programming language skill sets. Android Recipes: A Problem-Solution Approach guides you step-by-step through a wide range of useful topics using complete and real-world working code examples. In this book, you'll start off with a recap of Android architecture and app fundamentals, and then get down to business and build an app with Google’s Android SDK at the command line and Eclipse. Next, you'll learn how to accomplish practical tasks pertaining to the user interface, communications with the cloud, device hardware, data persistence, communications between applications, and interacting with Android itself. Finally, you'll learn how to leverage various libraries and Scripting Layer for Android (SL4A) to help you perform tasks more quickly, how to use the Android NDK to boost app performance, and how to design apps for performance, responsiveness, seamlessness, and more. Instead of abstract descriptions of complex concepts, in Android Recipes, you'll find live code examples. When you start a new project, you can consider copying and pasting the code and configuration files from this book, then modifying them for your own customization needs. This can save you a great deal of work over creating a project from scratch! What you’ll learn Discover Android architecture and various Android-specific APIs How to develop a unit conversion app in the context of command-line/Android SDK and Eclipse/Android SDK environments How to accomplish various tasks related to the user interface and more How to use external libraries to save time and effort How to quickly develop an app using the Scripting Layer for Android (SL4A) tool How to boost app performance by using the Android NDK Guidelines for designing filtered apps, performant apps, responsive apps, and seamless apps Who this book is for Newcomers to Android, as well as more accomplished Android developers. Table of Contents Getting Started with Android User Interface Recipes Communications and Networking Interacting with Device Hardware and Media Persisting Data Interacting with the System Working with Libraries Scripting Layer for Android Android NDK App Design Guidelines\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781430234142\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1430234148\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 456,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"averageRating\": 5.0,\n" +
            "    \"ratingsCount\": 1,\n" +
            "    \"contentVersion\": \"0.3.3.0.preview.3\",\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=P6yDPLVEfsEC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=P6yDPLVEfsEC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com/books?id=P6yDPLVEfsEC&printsec=frontcover&dq=isbn:1430234148&hl=&cd=1&source=gbs_api\",\n" +
            "    \"infoLink\": \"http://books.google.com/books?id=P6yDPLVEfsEC&dq=isbn:1430234148&hl=&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"http://books.google.com/books/about/Android_Recipes.html?hl=&id=P6yDPLVEfsEC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 31.99,\n" +
            "     \"currencyCode\": \"USD\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 17.27,\n" +
            "     \"currencyCode\": \"USD\"\n" +
            "    },\n" +
            "    \"buyLink\": \"http://books.google.com/books?id=P6yDPLVEfsEC&dq=isbn:1430234148&hl=&buy=&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 3.199E7,\n" +
            "       \"currencyCode\": \"USD\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 1.727E7,\n" +
            "       \"currencyCode\": \"USD\"\n" +
            "      }\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": true\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://books.google.com/books/reader?id=P6yDPLVEfsEC&hl=&printsec=frontcover&output=reader&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"In this book, you&#39;ll start off with a recap of Android architecture and app fundamentals, and then get down to business and build an app with Google’s Android SDK at the command line and Eclipse.\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}\n";

}
