package app.dushyant30suthar.locationtracking.domain.location.locationTracker;

import androidx.annotation.NonNull;

/*
 * The actual location tracker. It holds the state of the tracker.
 *
 * Tracker's responsibilities are fetching the location and sending it to LocationDao
 * for updation on server.*/

class LocationTracker {
    private static LocationTracker locationTracker;
    @NonNull
    private State locationTrackerState;

    private LocationTracker() {
        locationTrackerState = State.TERMINATED;
    }

    public static LocationTracker getInstance() {
        if (locationTracker == null) {
            locationTracker = new LocationTracker();
        }
        return locationTracker;
    }

    public void startTracking() {
        locationTrackerState = State.ONGOING;

    }

    public void stopTracking() {
        locationTrackerState = State.TERMINATED;
    }

    enum State {
        ONGOING,
        TERMINATED
    }


}
