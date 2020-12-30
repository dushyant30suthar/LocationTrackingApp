package app.dushyant30suthar.locationtracking.client.onboarding;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.location.locationTracker.LocationTrackerController;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        LocationTrackerController.getInstance().isLocationTrackingOngoing(this,
                new LocationTrackerController.LocationTrackerControllerEventsListener() {
                    @Override
                    public void onLocationTrackingOngoing() {
                        Navigation.findNavController(OnboardingActivity.this, R.id.onboardingNavHost).navigate(R.id.producerActivity);
                        finish();
                    }

                    @Override
                    public void onLocationTrackingNotOngoing() {
                        /*
                         * Do nothing*/
                    }
                });
    }
}
