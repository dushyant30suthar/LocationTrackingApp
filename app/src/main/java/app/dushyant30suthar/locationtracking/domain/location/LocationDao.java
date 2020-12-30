package app.dushyant30suthar.locationtracking.domain.location;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;

class LocationDao {
    private final DatabaseReference databaseReference;

    LocationDao(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    void updateLocation(Location location) {
        databaseReference.child("currentLocation").setValue(location.toString());
    }
}