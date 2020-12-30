package app.dushyant30suthar.locationtracking.domain.location.locationReceiver;

import app.dushyant30suthar.locationtracking.domain.authentication.AuthenticationManager;
import app.dushyant30suthar.locationtracking.domain.location.LocationDao;

public class LocationReceiverController {
    private static LocationReceiverController locationReceiverController;
    private final LocationDao.OnUsersLocationUpdateListener onUsersLocationUpdateListener;
    private final LocationDao locationDao;

    private LocationReceiverController(LocationDao.OnUsersLocationUpdateListener onUsersLocationUpdateListener) {
        this.onUsersLocationUpdateListener = onUsersLocationUpdateListener;
        locationDao = new LocationDao();
    }

    public static LocationReceiverController getInstance(LocationDao.OnUsersLocationUpdateListener onUsersLocationUpdateListener) {
        if (locationReceiverController == null) {
            locationReceiverController = new LocationReceiverController(onUsersLocationUpdateListener);
        }
        return locationReceiverController;
    }

    public void startReceivingUsersLocationUpdates() {
        locationDao.addOnLocationUpdateListener(onUsersLocationUpdateListener);
    }

    public void stopReceivingUsersLocationUpdates() {
        locationDao.removeOnLocationUpdateListener();

        try {
            AuthenticationManager.getInstance().disconnectFromGroup();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
