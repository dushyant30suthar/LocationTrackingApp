package app.dushyant30suthar.locationtracking.domain.locationTracker;

import com.google.firebase.database.DatabaseReference;

public class LocationTracker {

    private LocationTracker() {
    }


    private static class UserLocationDao {
        private final DatabaseReference databaseReference;

        UserLocationDao(DatabaseReference databaseReference) {
            this.databaseReference = databaseReference;
        }


    }

}
