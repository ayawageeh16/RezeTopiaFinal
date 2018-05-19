package io.krito.com.reze.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.krito.com.reze.R;
import io.krito.com.reze.activities.Login;
import io.krito.com.reze.application.AppConfig;
import io.krito.com.reze.application.RezetopiaApp;
import io.krito.com.reze.receivers.ConnectivityReceiver;

public class SideMenu extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener{

    TextView logoutView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_side_menu, container, false);

        logoutView = view.findViewById(R.id.logoutView);
        logoutView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }


    @Override
    public void onResume() {
        super.onResume();
        RezetopiaApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logoutView:
                if (ConnectivityReceiver.isConnected(getActivity())){
                    getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                            .edit().putString(AppConfig.LOGGED_IN_USER_ID_SHARED, null).apply();
                    startActivity(new Intent(getActivity(), Login.class));
                } else {
                    Snackbar.make(v, R.string.connection_error, Snackbar.LENGTH_INDEFINITE).show();
                }
                break;
        }
    }
}
