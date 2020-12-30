package app.dushyant30suthar.locationtracking.domain.authentication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/*
 * This module helps all of the modules to get connected at the valid point on the server.*/
class AuthenticationManager {
    private static final String DATABASE_ROOT = "groups";
    private static AuthenticationManager authenticationManager;
    private final DatabaseReference databaseReference;
    private final String currentGroupId;
    private String currentUserId;

    /*
     * This database is going to be real time. No older values will be stored.
     *
     * We will deal with fresh data only. User will be deleted as soon as it leaves
     * the group. */
    private AuthenticationManager(String groupId) {
        this.currentGroupId = groupId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(DATABASE_ROOT).child(currentGroupId);
    }

    /*
     * Check if getInstance is called for new groupId. If so, then create new instance and return. */
    public static AuthenticationManager getInstance(String groupId) {
        if (authenticationManager == null) {
            authenticationManager = getNewInstance(groupId);
        } else {
            if (!authenticationManager.currentGroupId.equals(groupId)) {
                authenticationManager = getNewInstance(groupId);
            }
        }
        return authenticationManager;
    }

    private static AuthenticationManager getNewInstance(String groupId) {
        return new AuthenticationManager(groupId);
    }

    /*
     * Creates new entity in the database for user. We will hold the key for updating the user location,
     * deleting the user after it stops sharing location.
     *
     * Returns the reference to the current user.
     *
     * We can pass this reference to the other modules to interact with this api. */
    public DatabaseReference connectToGroup() {
        currentUserId = databaseReference.push().getKey();
        User user = new User("");

        Map<String, Object> postValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/" + DATABASE_ROOT + "/" + currentUserId, postValues);
        databaseReference.updateChildren(childUpdates);

        return databaseReference;
    }

}
