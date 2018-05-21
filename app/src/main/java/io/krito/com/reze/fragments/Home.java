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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    LinearLayoutManager layoutManager;
    String cursor = "0";

    NewsFeed newsFeed;
    String userId;
    ArrayList<NewsFeedItem> tempItems;
    PostRecyclerAdapter adapter;
    HomeCallback homeCallback;

    int start = 0, end = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeHeader = view.findViewById(R.id.homeHeader);
        recyclerView = view.findViewById(R.id.homePostsRecyclerView);

        userId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        fetchNewsFeed();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 || dy < 0)
                    homeCallback.onScroll(false);

                if(dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ( (visibleItemCount + pastVisibleItems) >= totalItemCount){
                        Log.v("SCROLL_DOWN", "Last Item Wow !");
                        Snackbar.make(homeHeader, R.string.loading, BaseTransientBottomBar.LENGTH_LONG).show();
                        //adapter.notifyItemInserted(adapter.addItem());
                        fetchNewsFeed();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    homeCallback.onScroll(true);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

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
        Log.i("POST_CURSOR", "fetchNewsFeed: " + cursor);
        HomeOperations.fetchNewsFeed(userId, cursor);
        HomeOperations.setFeedCallback(new HomeOperations.NewsFeedCallback() {
            @Override
            public void onSuccess(NewsFeed feed) {
                if (newsFeed != null && feed.getItems() != null && feed.getItems().size() > 0){
                    Log.i("newsFeed_size", "onSuccess: " + newsFeed.getItems().size() + " " + feed.getItems().size());
                    start = newsFeed.getItems().size();
                    newsFeed.addAllitems(feed.getItems());
                    newsFeed.setNow(feed.getNow());
                    newsFeed.setNextCursor(feed.getNextCursor());
                    end = newsFeed.getItems().size()-1;
                    Log.i("Array_size", "onSuccess: " + start + " : " + end);
                    cursor = String.valueOf(Integer.parseInt(cursor) + 10);
                    //adapter.notifyItemRemoved(adapter.removeLastItem());
                    adapter.notifyItemRangeInserted(Integer.parseInt(cursor) - 10, Integer.parseInt(cursor));
                    updateUi(0, 0);
                } else {
                    newsFeed = feed;
                    updateUi(0, 0);
                    cursor = String.valueOf(Integer.parseInt(cursor) + 10);
                }
            }

            @Override
            public void onError(int error) {
                Snackbar.make(homeHeader, error, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private void updateUi(int s, int e){
        if (adapter == null){
            adapter = new PostRecyclerAdapter(getActivity(), newsFeed.getItems(), newsFeed.getNow(), userId);
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        } else if (s > 0 && e > 0){
            adapter.notifyItemRangeInserted(s, e);
        }
    }

    public interface HomeCallback{
        void onScroll(boolean show);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeCallback = (HomeCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeCallback = null;
    }
}
