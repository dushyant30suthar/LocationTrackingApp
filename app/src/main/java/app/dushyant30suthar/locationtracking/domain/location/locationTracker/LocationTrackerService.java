package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/*
 * This service will host the location tracking module i.e location tracking module will run on this application
 * component.
 *
 * This service supports binding. Through which we can find out (on the ui side) whether tracking module is
 * ON or OFF. Also we can start and stop tracking using binding only.*/


public class LocationTrackerService extends Service {

    private final IBinder binder = new TrackLocationBinder();
    private LocationTracker locationTracker;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        locationTracker = LocationTracker.getInstance(this, () -> locationTracker.startTracking());
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        locationTracker.stopTracking();
        super.onDestroy();
    }

    public boolean isLocationTrackingOngoing() {
        return locationTracker.getLocationTrackerCurrentState() == LocationTracker.State.ONGOING;
    }


    public class TrackLocationBinder extends Binder {
        LocationTrackerService getService() {
            return LocationTrackerService.this;
        }
    }
}