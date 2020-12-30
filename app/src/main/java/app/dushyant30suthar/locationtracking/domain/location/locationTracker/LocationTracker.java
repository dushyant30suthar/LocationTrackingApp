package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import app.dushyant30suthar.locationtracking.domain.authentication.AuthenticationManager;
import app.dushyant30suthar.locationtracking.domain.location.LocationDao;

/*
 * The actual location tracker. It holds the state of the tracker.
 *
 * Just a state machine. However I didn't make the state machine in good way,
 * the state transitionings are just not up to the mark we can improve it a lot. Not thinking about
 * it right now.
 *
 * Tracker's responsibilities are fetching the location and sending it to LocationDao
 * for updation on server.*/

class LocationTracker {
    private static LocationTracker locationTracker;
    @NonNull
    private final OnLocationTrackerReadyListener onLocationTrackerReadyListener;
    @NonNull
    private State locationTrackerCurrentState;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final LocationDao locationDao;
    private DatabaseReference databaseReference;

    private LocationTracker(Context context, @NonNull OnLocationTrackerReadyListener onLocationTrackerReadyListener) {
        locationTrackerCurrentState = State.INTERMEDIATE;
        this.databaseReference = databaseReference;
        this.onLocationTrackerReadyListener = onLocationTrackerReadyListener;
        locationDao = new LocationDao();
        setUpLocationClient(context).addOnSuccessListener(command -> {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            locationTrackerCurrentState = State.CONFIGURED;
            onLocationTrackerReadyListener.onLocationTrackerReady();
        }).addOnFailureListener(command ->
        {

        });
    }

    /*
     * LocationTracker should have just one instance. There should not be any
     * two location tracker working together.*/
    public static LocationTracker getInstance(Context context,
                                              OnLocationTrackerReadyListener onLocationTrackerReadyListener) {
        if (locationTracker == null) {
            locationTracker = new LocationTracker(context, onLocationTrackerReadyListener);
        }
        return locationTracker;
    }

    /*
     * Suppressing lint because I am not handling permissions the good way. I am just assuming things regarding permissions. */

    @SuppressLint("MissingPermission")
    public void startTracking() {
        /*
         * Should return a valid message to commander but not doing in this app. As we havent' properly handled states.*/
        if (locationTrackerCurrentState != State.CONFIGURED)
            return;
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                Looper.getMainLooper());
        locationTrackerCurrentState = State.ONGOING;

    }

    public void stopTracking() {
        try {
            AuthenticationManager.getInstance().disconnectFromGroup();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (fusedLocationClient != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
        locationTrackerCurrentState = State.TERMINATED;
    }

    private void onLocationUpdate(Location location) {
        locationDao.updateLocation(location);
    }

    @NonNull
    public State getLocationTrackerCurrentState() {
        return locationTrackerCurrentState;
    }

    private Task<LocationSettingsResponse> setUpLocationClient(Context context) {
        locationRequest = LocationRequest.create();
        /*
         * For testing I made it for 6 seconds because it was very slow so wasn't able to test. */
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLocations().size() == 0)
                    return;
                Location location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                onLocationUpdate(location);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        return task;
    }

    enum State {
        INTERMEDIATE,
        CONFIGURED,
        ONGOING,
        TERMINATED
    }

    public interface OnLocationTrackerReadyListener {
        void onLocationTrackerReady();

    }

}
