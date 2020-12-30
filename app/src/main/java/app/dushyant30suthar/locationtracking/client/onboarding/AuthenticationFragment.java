package app.dushyant30suthar.locationtracking.client.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.dushyant30suthar.locationtracking.R;
import app.dushyant30suthar.locationtracking.domain.authentication.AuthenticationManager;

class AuthenticationFragment extends Fragment {

    private EditText groupIdEditText;
    private Button establishButton;
    private AuthenticationManager authenticationManager;

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
            authenticationManager = AuthenticationManager.getInstance(groupIdEditText.getText().toString());

        });
    }
}
