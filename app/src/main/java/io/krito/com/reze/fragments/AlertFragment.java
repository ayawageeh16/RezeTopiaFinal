package io.krito.com.reze.fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.krito.com.reze.R;

/**
 * Created by Ahmed Ali on 5/19/2018.
 */

public class AlertFragment extends DialogFragment {

    TextView alertString;

    public static AlertFragment createFragment(String alert){
        Bundle bundle = new Bundle();
        bundle.putString("alert", alert);
        AlertFragment fragment = new AlertFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertString = view.findViewById(R.id.alertString);
        alertString.setText(getArguments().getString("alert"));
        return view;
    }
}
