package edu.upenn.cis350.cisproject.ui.safety;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import edu.upenn.cis350.cisproject.R;

import static android.Manifest.permission.CALL_PHONE;

public class SafetyFragment extends Fragment {

    private SafetyViewModel safetyViewModel;
    private Button btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        callPhone("6105267911");

        return inflater.inflate(R.layout.fragment_safety, null);


        /**safetyViewModel =
                ViewModelProviders.of(this).get(SafetyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_safety, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        safetyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
         */
    }

    public void callPhone(String phoneNum){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +phoneNum ));

        if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{CALL_PHONE}, 1);
        }
    }

/**
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }*/



}