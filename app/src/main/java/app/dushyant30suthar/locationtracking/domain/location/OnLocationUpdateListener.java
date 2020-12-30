package app.dushyant30suthar.locationtracking.domain.location;

import java.util.List;

import app.dushyant30suthar.locationtracking.domain.authentication.User;

interface OnLocationUpdateListener {
    void onLocationUpdate(List<User> userList);
}
