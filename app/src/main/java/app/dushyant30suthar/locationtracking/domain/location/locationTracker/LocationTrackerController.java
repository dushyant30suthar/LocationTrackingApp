package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

/*
 * Controller of whole tracking process.
 *
 * Any client can turn ON or OFF the tracking process by using this.
 *
 * It's just kind of api used to interact with tracking module as whole.*/
public class LocationTrackerController {
    private static LocationTrackerController locationTrackerController;

    private LocationTrackerController() {

    }

    public static LocationTrackerController getInstance() {
        if (locationTrackerController == null) {
            locationTrackerController = new LocationTrackerController();
        }
        return locationTrackerController;
    }

    public void startLocationTracking(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, LocationTrackerService.class));
        } else {
            context.startService(new Intent(context, LocationTrackerService.class));
        }
    }

    public void stopLocationTracking(Context context) {
        context.stopService(new Intent(context, LocationTrackerService.class));
    }

    public void isLocationTrackingOngoing(Context context, LocationTrackerControllerEventsListener locationTrackerControllerEventsListener) {
        final ServiceConnection connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                LocationTrackerService.TrackLocationBinder binder = (LocationTrackerService.TrackLocationBinder) service;
                LocationTrackerService locationTrackerService = binder.getService();
                if (locationTrackerService.isLocationTrackingOngoing()) {
                    locationTrackerControllerEventsListener.onLocationTrackingOngoing();
                } else {
                    locationTrackerControllerEventsListener.onLocationTrackingNotOngoing();
                }

                context.unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
            }
        };
        Intent intent = new Intent(context, LocationTrackerService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public interface LocationTrackerControllerEventsListener {
        void onLocationTrackingOngoing();

        void onLocationTrackingNotOngoing();
    }

}
