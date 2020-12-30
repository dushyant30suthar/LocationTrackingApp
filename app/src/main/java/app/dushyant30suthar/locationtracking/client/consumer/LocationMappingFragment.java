package app.dushyant30suthar.locationtracking.client.consumer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.authentication.User;
import app.dushyant30suthar.locationtracking.domain.location.LocationDao;
import app.dushyant30suthar.locationtracking.domain.location.locationReceiver.LocationReceiverController;

public class LocationMappingFragment extends Fragment implements LocationDao.OnUsersLocationUpdateListener {

    private LocationReceiverController locationReceiverController;

    public LocationMappingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_mapping, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
        startWatchingUsers();
    }

    private void setUpViews(View view) {
        locationReceiverController = LocationReceiverController.getInstance(this);
    }

    private void startWatchingUsers() {

        locationReceiverController.startReceivingUsersLocationUpdates();
    }

    private void stopWatchingUsers() {
        locationReceiverController.stopReceivingUsersLocationUpdates();
    }

    @Override
    public void onUsersLocationUpdate(List<User> userList) {
        Log.e("updated", userList.get(0).getLatitude() + "");
    }
}
