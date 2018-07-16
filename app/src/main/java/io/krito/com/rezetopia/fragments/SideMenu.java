package io.krito.com.rezetopia.fragments;

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

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.activities.CreateGroup;
import io.krito.com.rezetopia.activities.Login;
import io.krito.com.rezetopia.activities.Profile;
import io.krito.com.rezetopia.activities.SavedPosts;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.receivers.ConnectivityReceiver;

public class SideMenu extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener{

    String userId;

    TextView logoutView;
    TextView myProfileView;
    TextView savedPosts;
    TextView createGroupView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_side_menu, container, false);

        userId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        logoutView = view.findViewById(R.id.logoutView);
        myProfileView = view.findViewById(R.id.myProfile);
        savedPosts = view.findViewById(R.id.sideSavedPostsView);
        createGroupView = view.findViewById(R.id.createGroupView);
        myProfileView.setOnClickListener(this);
        logoutView.setOnClickListener(this);
        savedPosts.setOnClickListener(this);
        createGroupView.setOnClickListener(this);
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
            case R.id.myProfile:
                Intent intent = Profile.createIntent(userId, getActivity());
                startActivity(intent);
                break;
            case R.id.sideSavedPostsView:
                Intent intent1 = new Intent(getActivity(), SavedPosts.class);
                startActivity(intent1);
                break;
            case  R.id.createGroupView:
                Intent intent2 = new Intent(getActivity(), CreateGroup.class);
                startActivity(intent2);
                break;
        }
    }
}
