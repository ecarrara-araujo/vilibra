package br.eng.ecarrara.vilibra.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.eng.ecarrara.vilibra.BookListActivity;
import br.eng.ecarrara.vilibra.LendedBookDetailActivity;
import br.eng.ecarrara.vilibra.R;
import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.data.VilibraContract.BookEntry;
import br.eng.ecarrara.vilibra.data.VilibraContract.LendingEntry;
import br.eng.ecarrara.vilibra.utils.Utility;

/**
 * The purpose of this service is to check the ViLibra database for current lended books and
 * warn the user according to the user configured time interval using Notifications
 * TODO: Analyze if would be interesting to replace this service for a Sync Adapter
 * Created by ecarrara on 30/12/2014.
 */
public class BookLendingNotificationService extends IntentService {

    private static final String LOG_TAG = BookLendingNotificationService.class.getSimpleName();

    private static final String[] LENDING_DETAIL_COLUMNS = {
            BookEntry.TABLE_NAME + "." + BookEntry.COLUMN_BOOK_ID,
            BookEntry.COLUMN_TITLE,
            LendingEntry.TABLE_NAME + "." + LendingEntry.COLUMN_LENDING_ID,
            LendingEntry.COLUMN_CONTACT_URI,
            LendingEntry.COLUMN_LENDING_DATE,
            LendingEntry.COLUMN_LAST_NOTIFICATION_DATE
    };

    public static final int COL_BOOK_ID = 0;
    public static final int COL_BOOK_TITLE = 1;
    public static final int COL_LENDING_ID = 2;
    public static final int COL_LENDING_CONTACT = 3;
    public static final int COL_LENDING_DATE = 4;
    public static final int COL_LENDING_LAST_NOTIFICATION_DATE = 5;

    public static final int LENDING_NOTIFICATION_INTERVAL = 7; // in days
    public static final String NOTIFICATION_GROUP_VILIBRA = "group_key_vilibra";

    private static final int SERVICE_EXECUTION_FREQUENCY_HOURS = 24;

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private ArrayList<Notification> mNotifications;
    private ArrayList<String> mSummarizedBookMessages;

    /**
     * Base constructor for service creation
     */
    public BookLendingNotificationService() {
        super(BookLendingNotificationService.class.getSimpleName());
    }

    @Override
    public void onDestroy() {
        // Schedule the service to execute again 24h after this execution
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60 * 60 * SERVICE_EXECUTION_FREQUENCY_HOURS),
                PendingIntent.getService(this, 0,
                        new Intent(this, BookLendingNotificationService.class), 0)
        );
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");

        if(!isNotificationEnabled()) {
            Log.d(LOG_TAG, "Notification disabled by user.");
            return;
        }

        Cursor bookLendingCursor = getContentResolver().query(LendingEntry.buildLendingBooksUri(),
                LENDING_DETAIL_COLUMNS, null, null, null);

        if(bookLendingCursor.moveToFirst()) {
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationBuilder = new NotificationCompat.Builder(this);

            mNotifications = new ArrayList<Notification>();
            mSummarizedBookMessages = new ArrayList<String>();

            do {
                Date lastNotificationDate = VilibraContract.getDateFromDb(
                        bookLendingCursor.getString(COL_LENDING_LAST_NOTIFICATION_DATE));

                if(isNotificationNeededForDate(lastNotificationDate)) {
                    String bookName = bookLendingCursor.getString(COL_BOOK_TITLE);
                    String lendingDate = Utility.getFormattedDateForBookInfo(
                            this,
                            bookLendingCursor.getString(COL_LENDING_DATE));

                    String contactName =
                            getContactName(
                                    Uri.parse(bookLendingCursor.getString(COL_LENDING_CONTACT)));
                    Uri bookUri = BookEntry.buildBookUri(bookLendingCursor.getLong(COL_BOOK_ID));

                    Notification notification =
                            buildLendedBookNotification(bookName, contactName, lendingDate, bookUri);
                    mNotifications.add(notification);

                    updateNotificationDate(
                            LendingEntry.buildLendingUri(bookLendingCursor.getLong(COL_LENDING_ID)));
                }
            } while(bookLendingCursor.moveToNext());

            if(mNotifications.size() > 0) {
                mNotificationManager.cancelAll(); // clean up the existing notifications.
            }

            for (Notification notification : mNotifications) {
                // mId allows you to update the notification later on.
                mNotificationManager.notify(0, notification);
            }

            if(mNotifications.size() > 1) {
                mNotificationManager.notify(0, buildSummaryNotification());
            }

        } else {
            Log.d(LOG_TAG, "No lending found to warn the user");
        }

    }

    private void updateNotificationDate(Uri lendingUri) {
        ContentValues values = new ContentValues();
        values.put(LendingEntry.COLUMN_LAST_NOTIFICATION_DATE,
                VilibraContract.getDbDateString(new Date()));
        getContentResolver().update(lendingUri, values, null, null);
    }

    private boolean isNotificationEnabled() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String frequency = prefs.getString(getString(R.string.pref_frequency_key),
                getString(R.string.pref_frequency_weekly));
        if(frequency.equals(getString(R.string.pref_frequency_disabled))) {
            return false;
        }
        return true;
    }

    private int getLendingNotificationInterval() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String frequency = prefs.getString(getString(R.string.pref_frequency_key),
                getString(R.string.pref_frequency_weekly));

        int daysForInterval = LENDING_NOTIFICATION_INTERVAL;
        if(frequency.equals(getString(R.string.pref_frequency_weekly))) {
            daysForInterval = 7;
        } else if(frequency.equals(getString(R.string.pref_frequency_every_two_weeks))){
            daysForInterval = 14;
        } else if(frequency.equals(getString(R.string.pref_frequency_monthly))) {
            daysForInterval = 30;
        }

        return daysForInterval;
    }

    private boolean isNotificationNeededForDate(Date lastNotificationDate) {
        int lendingNotificationInterval = getLendingNotificationInterval();

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastNotificationDate);
        calendar.add(Calendar.DATE, lendingNotificationInterval);

        // if the last notification date plus the time interval is less than the current
        // time it is ok to notify the user about the lending
        return (calendar.getTime().compareTo(now) < 0);
    }

    private Notification buildSummaryNotification() {
        String title = String.format(getString(R.string.format_books_missing_you),
                mNotifications.size());

        NotificationCompat.InboxStyle notificationStyle = new NotificationCompat.InboxStyle();
        notificationStyle.setBigContentTitle(title);
        for(String message : mSummarizedBookMessages) {
            notificationStyle.addLine(message);
        }

        mNotificationBuilder
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_vilibra)
                .setStyle(notificationStyle)
                .setGroup(NOTIFICATION_GROUP_VILIBRA)
                .setGroupSummary(true);

        // explicit intent that will be called when the user touches the notification.
        Intent resultIntent = new Intent(this, BookListActivity.class);

        // artificial back stack to ensure that the user go back to the correct location
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(BookListActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder.setContentIntent(resultPendingIntent);

        return mNotificationBuilder.build();
    }

    private Notification buildLendedBookNotification(String bookName, String contactName,
                                                     String lendingDate, Uri bookUri) {

        String notificationMessage = String.format(
                getString(R.string.format_book_lended_to), bookName, contactName);
        mNotificationBuilder.setSmallIcon(R.drawable.ic_vilibra)
                .setContentTitle(getString(R.string.book_lending_reminder))
                .setContentText(notificationMessage)
                .setGroup(NOTIFICATION_GROUP_VILIBRA);

        // store the message to use in the summary notification
        mSummarizedBookMessages.add(notificationMessage);

        // explicit intent that will be called when the user touches the notification.
        Intent resultIntent = new Intent(this, LendedBookDetailActivity.class);
        resultIntent.putExtra(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI, bookUri);

        // artificial back stack to ensure that the user go back to the correct location
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(BookListActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder.setContentIntent(resultPendingIntent);
        return mNotificationBuilder.build();
    }

    private String getContactName(Uri contactUri) {
        String contactName = getString(R.string.unknown);

        final String[] CONTACT_COLUMNS = {
                ContactsContract.Contacts.DISPLAY_NAME
        };
        final int COL_CONTACT_NAME = 0;

        Cursor contactCursor =
                getContentResolver().query(contactUri, CONTACT_COLUMNS, null, null, null, null);

        if (contactCursor != null && contactCursor.moveToFirst()) {
            contactName = contactCursor.getString(COL_CONTACT_NAME);
            contactCursor.close();
        }

        return contactName;
    }
}
