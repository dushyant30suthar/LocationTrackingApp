package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

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
            context.startForegroundService(new Intent(context, TrackLocationService.class));
        } else {
            context.startService(new Intent(context, TrackLocationService.class));
        }
    }

    public void stopLocationTracking(Context context) {
        context.stopService(new Intent(context, TrackLocationService.class));
    }

    public boolean isLocationTrackingOngoing() {
        return false;
    }

}
