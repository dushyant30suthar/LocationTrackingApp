package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import app.dushyant30suthar.locationtracking.client.producer.ProducerActivity;

/*
 * This service will host the location tracking module i.e location tracking module will run on this application
 * component.
 *
 * This service supports binding. Through which we can find out (on the ui side) whether tracking module is
 * ON or OFF. Also we can start and stop tracking using binding only.*/


public class LocationTrackerService extends Service {

    private final IBinder binder = new TrackLocationBinder();
    private LocationTracker locationTracker;

    public static int LOCATION_TRACKING_NOTIFICATION_ID = 10;

    private Notification notification;


    @Override
    public void onCreate() {
        super.onCreate();

        NotificationConfiguration.getInstance().createNotificationChannel(this, NotificationConfiguration.LOCATION_TRACKING_NOTIFICATION_CHANNEL_ID);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, ProducerActivity.class), 0);
        notification = NotificationConfiguration.getInstance()
                .preparePersistingNotification(NotificationConfiguration.LOCATION_TRACKING_NOTIFICATION_CHANNEL_ID, "Location Tracking",
                        "Fetching location with high accuracy", "Also sharing data with server", pendingIntent, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForeground(LOCATION_TRACKING_NOTIFICATION_ID, notification);
        locationTracker = LocationTracker.getInstance(this, () -> locationTracker.startTracking());
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        if (locationTracker != null)
            locationTracker.stopTracking();
        super.onDestroy();
    }

    public boolean isLocationTrackingOngoing() {
        if (locationTracker == null) {
            return false;
        } else
            return locationTracker.getLocationTrackerCurrentState() == LocationTracker.State.ONGOING;
    }


    public class TrackLocationBinder extends Binder {
        LocationTrackerService getService() {
            return LocationTrackerService.this;
        }
    }
}
