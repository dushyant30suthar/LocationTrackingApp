package app.dushyant30suthar.locationtracking.domain.location;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.dushyant30suthar.locationtracking.domain.authentication.AuthenticationManager;
import app.dushyant30suthar.locationtracking.domain.authentication.User;

public class LocationDao implements ValueEventListener {
    private final DatabaseReference databaseReference;
    private final static String TAG = LocationDao.class.getSimpleName();

    private final String currentUser;

    private OnLocationUpdateListener onLocationUpdateListener;

    public LocationDao() {
        AuthenticationManager authenticationManager = null;
        try {
            authenticationManager = AuthenticationManager.getInstance();
            this.currentUser = authenticationManager.getCurrentUserId();
            this.databaseReference = authenticationManager.getDatabaseReferenceToGroup();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Not a valid connection to the server");
        }
    }

    public void updateLocation(Location location) {
        databaseReference.child(currentUser).child("currentLocation").setValue(location.toString());
    }

    /*
     * Just assumed that we will be having just one part of the ui which is subscribing to this event.
     *
     * If there is any change in group such as new user is added to the group or any user has
     * updated it's location then we would deliver the lastest data for all of the users
     * which are currently being shown on the screen.*/
    public void addOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListener) {
        this.onLocationUpdateListener = onLocationUpdateListener;
        databaseReference.addValueEventListener(this);
    }

    public void removeOnLocationUpdateListener() {
        this.onLocationUpdateListener = null;
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        /*
         * Get list of all of the users which are currently present in the group.*/
        List<User> userList = new ArrayList<>();

        for (DataSnapshot d : dataSnapshot.getChildren()) {
            User user = d.getValue(User.class);
            userList.add(user);
        }

        onLocationUpdateListener.onLocationUpdate(userList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting User failed, log a message
        Log.w(TAG, "loadUser:onCancelled", databaseError.toException());

    }

    interface OnLocationUpdateListener {
        void onLocationUpdate(List<User> userList);
    }
}