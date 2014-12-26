package ecarrara.eng.vilibra.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ecarrara.eng.vilibra.R;

/**
 * Created by ecarrara on 24/12/2014.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static final String DATE_FORMAT = "yyyyMMdd";

    public static Bitmap getThumbnailForContact(Context context, Uri contactLookupUri) {
        if(contactLookupUri == null) { return null; }

        Cursor cursor = context.getContentResolver().query(contactLookupUri,
                new String[] { ContactsContract.Contacts.PHOTO_THUMBNAIL_URI }, null, null, null);
        if(cursor.moveToFirst()) {
            Uri thumbnailUri = Uri.parse(cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)));
            AssetFileDescriptor afd = null;
            try {
                afd = context.getContentResolver().openAssetFileDescriptor(thumbnailUri, "r");
                FileDescriptor fileDescriptor = afd.getFileDescriptor();
                if(fileDescriptor != null) {
                    return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
                }
            } catch (Exception ex) {
                Log.e(LOG_TAG, ex.getMessage());
            } finally {
                if(afd != null) {
                    try {
                        afd.close();
                    } catch (IOException e) {}
                }
            }
        }
        return null;
    }

    /**
     * Converts db date format to the format "Year, Month day", e.g "2014, June 24".
     * @param context Context to use for resource localization
     * @param dateStr The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "2014, December 6"
     */
    public static String getFormattedMonthDay(Context context, String dateStr) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
        try {
            Date inputDate = dbDateFormat.parse(dateStr);
            SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat(
                    context.getString(R.string.full_date_format));
            String finalDateString = yearMonthDayFormat.format(inputDate);
            return finalDateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if a connection to the network is available to retrieve book information.
     * @param context Context to use for getting the ConnectivityManager instance.
     * @return true if there is a network connection, false otherwise
     */
    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else if (!networkInfo.isConnected()) {
            return false;
        } else if (!networkInfo.isAvailable()) {
            return false;
        }
        return true;
    }

}
