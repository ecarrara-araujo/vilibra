package br.eng.ecarrara.vilibra.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * After the device boots run the service once to check for lendings and schedule new user warning cycles.
 * Created by ecarrara on 30/12/2014.
 */
public class NotificationServiceStarterOnBootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, BookLendingNotificationService.class);
        context.startService(serviceIntent);
    }
}
