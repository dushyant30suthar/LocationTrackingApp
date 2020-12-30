package app.dushyant30suthar.locationtracking.client.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.authentication.AuthenticationManager;

public class AuthenticationFragment extends Fragment {

    private EditText groupIdEditText;
    private Button establishButton;
    private AuthenticationManager authenticationManager;

    /*
     * Must have constructor for fragment.*/
    public AuthenticationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    private void setUpViews(View view) {
        groupIdEditText = view.findViewById(R.id.groupIdEditText);
        establishButton = view.findViewById(R.id.establishButton);
        establishButton.setOnClickListener(v -> {
            if (groupIdEditText.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please enter user Id", Toast.LENGTH_SHORT).show();
                return;
            }
            groupIdEditText.setEnabled(false);
            establishButton.setEnabled(false);
            Toast.makeText(getContext(), "Just a sec.", Toast.LENGTH_SHORT).show();
            authenticationManager = AuthenticationManager.getInstance(groupIdEditText.getText().toString());
            authenticationManager.connectToGroup(new AuthenticationManager.OnConnectionStateChangeListener() {
                @Override
                public void onConnectionSuccessful() {
                    establishButton.setEnabled(true);
                    groupIdEditText.setEnabled(true);
                    Navigation.findNavController(getActivity(), R.id.onboardingNavHost).navigate(R.id.action_authenticationFragment_to_moduleSelectionFragment);
                }

                @Override
                public void onConnectionFailed() {
                    establishButton.setEnabled(true);
                    groupIdEditText.setEnabled(true);
                    Toast.makeText(getContext(), "Error connecting server", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
