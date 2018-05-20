package io.krito.com.reze.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import io.krito.com.reze.R;
import io.krito.com.reze.application.AppConfig;
import io.krito.com.reze.application.RezetopiaApp;
import io.krito.com.reze.helper.PostRecyclerAdapter;
import io.krito.com.reze.models.operations.HomeOperations;
import io.krito.com.reze.models.pojo.news_feed.NewsFeed;
import io.krito.com.reze.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.reze.receivers.ConnectivityReceiver;

public class Home extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    LinearLayout homeHeader;
    RecyclerView recyclerView;

    NewsFeed newsFeed;
    String userId;
    ArrayList<NewsFeedItem> tempItems;
    RecyclerView.Adapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeHeader = view.findViewById(R.id.homeHeader);
        recyclerView = view.findViewById(R.id.homePostsRecyclerView);

        userId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        fetchNewsFeed();

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


    private void fetchNewsFeed(){
        String cursor = "0";
        if (newsFeed != null){
            cursor = newsFeed.getNextCursor();
        }

        HomeOperations.fetchNewsFeed(userId, cursor);
        HomeOperations.setFeedCallback(new HomeOperations.NewsFeedCallback() {
            @Override
            public void onSuccess(NewsFeed feed) {
                if (newsFeed != null && feed.getItems() != null && feed.getItems().size() > 0){
                    newsFeed.addAllitems(feed.getItems());
                }

                updateUi();
            }

            @Override
            public void onError(int error) {
                Snackbar.make(homeHeader, error, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private void updateUi(){
        if (adapter == null){
            adapter = new PostRecyclerAdapter(getActivity(), newsFeed.getItems(), newsFeed.getNow(), userId);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {

        }
    }
}
