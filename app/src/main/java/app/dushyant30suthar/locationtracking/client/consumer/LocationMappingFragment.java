package app.dushyant30suthar.locationtracking.client.consumer;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.authentication.User;
import app.dushyant30suthar.locationtracking.domain.location.LocationDao;
import app.dushyant30suthar.locationtracking.domain.location.locationReceiver.LocationReceiverController;

public class LocationMappingFragment extends Fragment implements LocationDao.OnUsersLocationUpdateListener {

    private LocationReceiverController locationReceiverController;
    private GoogleMap googleMap;

    public LocationMappingFragment() {
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LocationMappingFragment.this.googleMap = googleMap;
        }
    };


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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void startWatchingUsers() {

        locationReceiverController.startReceivingUsersLocationUpdates();
    }

    private void stopWatchingUsers() {
        locationReceiverController.stopReceivingUsersLocationUpdates();
    }

    @Override
    public void onUsersLocationUpdate(List<User> userList) {
        if (googleMap == null)
            return;

        Log.e("updated", userList.get(0).getLatitude() + "");
        for (User user : userList) {

            /*
             * That's a very poor logic to filter out the current user from the map.
             *
             * current user should be filtered out on the basis of id rather than this
             * kind of logic.*/
            if (user.getLatitude() == 0.0 || user.getLongitude() == 0.0) {

            } else {
                LatLng userLocation = new LatLng(user.getLatitude(), user.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(userLocation));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            }
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    marker.setVisible(!hideMarker);
                }
            }
        });
    }
}
