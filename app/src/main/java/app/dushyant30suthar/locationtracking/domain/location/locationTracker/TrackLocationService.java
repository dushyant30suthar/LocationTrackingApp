package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/*
 * This service will host the location tracking module i.e location tracking module will run on this application
 * component.
 *
 * This service supports binding. Through which we can find out (on the ui side) whether tracking module is
 * ON or OFF. Also we can start and stop tracking using binding only.*/


public class TrackLocationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
