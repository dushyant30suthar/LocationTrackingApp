package app.dushyant30suthar.locationtracking.client.consumer;

import android.Manifest;
import android.content.pm.PackageManager;
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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.authentication.User;
import app.dushyant30suthar.locationtracking.domain.location.LocationDao;
import app.dushyant30suthar.locationtracking.domain.location.locationReceiver.LocationReceiverController;

public class LocationMappingFragment extends Fragment implements LocationDao.OnUsersLocationUpdateListener {

    private LocationReceiverController locationReceiverController;
    private GoogleMap googleMap;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LocationMappingFragment.this.googleMap = googleMap;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    };
    private Map<Integer, Marker> attachedMarkerList;

    public LocationMappingFragment() {
    }

    private Button stopWatchingButton;


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
        attachedMarkerList = new HashMap<>();
        stopWatchingButton = view.findViewById(R.id.stopWatching);
        stopWatchingButton.setOnClickListener(v -> {
            stopWatchingUsers();
            Navigation.findNavController(getActivity(), R.id.consumerNavHost).navigate(R.id.action_locationMappingFragment_to_onboardingActivity2);
            getActivity().finish();
        });
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
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);

            /*
             * That's a very poor logic to filter out the current user from the map.
             *
             * current user should be filtered out on the basis of id rather than this
             * kind of logic.*/
            if (user.getLatitude() == 0.0 || user.getLongitude() == 0.0) {

            } else {
                LatLng userLocation = new LatLng(user.getLatitude(), user.getLongitude());
                if (attachedMarkerList.get(i) == null) {
                    attachedMarkerList.put(i, googleMap.addMarker(new MarkerOptions().position(userLocation)));
                }
                animateMarker(attachedMarkerList.get(i), userLocation, false);
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
