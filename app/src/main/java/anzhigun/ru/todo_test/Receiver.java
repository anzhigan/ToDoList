package anzhigun.ru.todo_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nBuilder = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nBuilder.build());
    }
}
