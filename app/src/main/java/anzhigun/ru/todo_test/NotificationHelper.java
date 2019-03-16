package anzhigun.ru.todo_test;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;

public class NotificationHelper extends ContextWrapper {
    public static final String ID = "channelID";
    public static final String NAME = "channelName";
    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(){
        NotificationChannel channel = new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }


    public NotificationManager getManager(){
        if(notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return notificationManager;

    }

    public NotificationCompat.Builder getChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(), ID)
                .setContentTitle("Напоминание")
                .setContentText("Выполните задачу")
                .setSmallIcon(R.drawable.ic_save);

    }
}

