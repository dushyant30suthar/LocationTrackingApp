package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationConfiguration {

    public static final int SWIPEABLE_NOTIFICATION_ID = 1;

    public static final int PERSISTING_NOTIFICATION_ID = 2;

    public static final String LOCATION_TRACKING_NOTIFICATION_CHANNEL_ID = "Location Tracking";

    private static NotificationConfiguration notificationConfiguration;

    public static NotificationConfiguration getInstance() {
        if (notificationConfiguration == null) {
            notificationConfiguration = new NotificationConfiguration();
        }
        return notificationConfiguration;
    }

    public void createNotificationChannel(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name;
            String description;
            int importance;
            name = "Location Tracking";
            description = "Notification about location being monitored and sharing with server";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public Notification preparePersistingNotification(String notificationChannelId, String notificationTitle, String notificationText, String subText, PendingIntent pendingIntent, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new NotificationCompat.Builder(context, notificationChannelId)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setSubText(subText)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent).build();
        } else {
            return new NotificationCompat.Builder(context)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setSubText(subText)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent).build();
        }

    }


}
