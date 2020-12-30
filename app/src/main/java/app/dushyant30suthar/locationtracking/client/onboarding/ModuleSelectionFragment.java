package app.dushyant30suthar.locationtracking.client.onboarding;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.location.locationTracker.LocationTrackerController;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ModuleSelectionFragment extends Fragment {

    private Button startSharingButton;
    private Button startWatchingButton;

    public ModuleSelectionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module_selection, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    private void setUpViews(View view) {
        startSharingButton = view.findViewById(R.id.startSharingButton);
        startWatchingButton = view.findViewById(R.id.startWatchingButton);

        startSharingButton.setOnClickListener(v -> {
            LocationTrackerController.getInstance().startLocationTracking(getContext());
            Navigation.findNavController(getActivity(), R.id.onboardingNavHost).navigate(R.id.action_moduleSelectionFragment_to_producerActivity);
            getActivity().finish();
        });

        startWatchingButton.setOnClickListener(v -> {

        });


        handlePermissionStuff();
    }


    private void handlePermissionStuff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PERMISSION_GRANTED) {
                String[] permissions = new String[1];
                permissions = new String[1];
                permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
                requestPermissions(permissions, 2);
                /*
                 * If SDK >= Q then we got to check for background locations*/


            } else {

                /*
                 * Permission granted*/

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {

            case 2: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        permissions[0] = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
                        requestPermissions(permissions, 3);
                    }
                }
            }
            break;
            case 3:

                if (grantResults.length > 0 &&
                        grantResults[0] == PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PERMISSION_GRANTED) {
                    }
                }
        }
    }
}
