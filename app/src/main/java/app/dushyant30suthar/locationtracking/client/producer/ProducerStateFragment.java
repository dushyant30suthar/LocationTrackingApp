package app.dushyant30suthar.locationtracking.client.producer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.location.locationTracker.LocationTrackerController;

public class ProducerStateFragment extends Fragment {

    private Button stopTrackingButton;

    public ProducerStateFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producer_state, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    private void setUpViews(View view) {
        stopTrackingButton = view.findViewById(R.id.stopTrackingButton);
        stopTrackingButton.setOnClickListener(v -> {
            LocationTrackerController.getInstance().stopLocationTracking(getContext());
            Navigation.findNavController(getActivity(), R.id.producerNavHost).navigate(R.id.onboardingActivity);
            getActivity().finish();
        });
    }
}
