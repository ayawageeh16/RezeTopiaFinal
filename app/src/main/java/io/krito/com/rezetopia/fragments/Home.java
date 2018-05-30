package io.krito.com.rezetopia.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.activities.Comment;
import io.krito.com.rezetopia.activities.CreatePost;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.helper.PostRecyclerAdapter;
import io.krito.com.rezetopia.models.operations.HomeOperations;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.receivers.ConnectivityReceiver;

public class Home extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final int COMMENT_ACTIVITY_RESULT = 1001;
    private static final int CREATE_POST_RESULT = 1002;

    FrameLayout homeHeader;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    String cursor = "0";

    NewsFeed newsFeed;
    String userId;
    ArrayList<NewsFeedItem> tempItems;
    PostRecyclerAdapter adapter;
    HomeCallback homeCallback;

    int start = 0, end = 0;
    boolean loadingData = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeHeader = view.findViewById(R.id.homeHeader);
        recyclerView = view.findViewById(R.id.homePostsRecyclerView);
        progressBar = view.findViewById(R.id.homeProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

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
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();


                    if ( (visibleItemCount + pastVisibleItems) >= totalItemCount){
                        //Snackbar.make(homeHeader, R.string.loading, BaseTransientBottomBar.LENGTH_LONG).show();
                        //adapter.notifyItemInserted(adapter.addItem());
                        if (!loadingData){
                            Log.v("SCROLL_DOWN", "Last Item Wow !");
                            loadingData = true;
                            adapter.addItem();
                            fetchNewsFeed();
                        }
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
                    final int lastItem = adapter.removeLastItem();
                    Log.i("newsFeed_size", "onSuccess: " + newsFeed.getItems().size() + " " + feed.getItems().size());
                    start = newsFeed.getItems().size();
                    newsFeed.addAllitems(feed.getItems());
                    newsFeed.setNow(feed.getNow());
                    newsFeed.setNextCursor(feed.getNextCursor());
                    end = newsFeed.getItems().size()-1;
                    Log.i("Array_size", "onSuccess: " + start + " : " + end);
                    cursor = String.valueOf(Integer.parseInt(cursor) + 10);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.removeLastItem();
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(lastItem);
                            //adapter.notifyItemRangeInserted(Integer.parseInt(cursor) - 10, Integer.parseInt(cursor));
                        }
                    });
                    //updateUi(0, 0);
                } else {
                    newsFeed = feed;
                    updateUi(0, 0);
                    cursor = String.valueOf(Integer.parseInt(cursor) + 10);
                    progressBar.setVisibility(View.GONE);
                }
                loadingData = false;
            }

            @Override
            public void onError(int error) {
                String errorString = getActivity().getResources().getString(error);
                Log.i("news_feed_error", "onError: " + errorString);
                //Snackbar.make(homeHeader, error, BaseTransientBottomBar.LENGTH_LONG).show();
                loadingData = false;
            }
        });
    }

    private void updateUi(int s, int e){
        if (adapter == null){
            adapter = new PostRecyclerAdapter(getActivity(), newsFeed.getItems(), newsFeed.getNow(), userId);
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            adapter.setCallback(new PostRecyclerAdapter.AdapterCallback() {
                @Override
                public void onStartComment(NewsFeedItem item, long now) {
                    Intent intent = Comment.createIntent(item.getLikes(), Integer.parseInt(item.getPostId()), now, Integer.parseInt(item.getOwnerId()),
                            getActivity());

                    startActivityForResult(intent, COMMENT_ACTIVITY_RESULT);
                    getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                }

                @Override
                public void onItemAdded(final int position) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemInserted(position);
                        }
                    });
                }

                @Override
                public void onItemRemoved(final int position) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRemoved(position);
                        }
                    });
                }

                @Override
                public void onStartCreatePost() {
                    Intent intent = new Intent(getActivity(), CreatePost.class);
                    startActivityForResult(intent, CREATE_POST_RESULT);
                }
            });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_ACTIVITY_RESULT){
            if (data != null){
                int postId = data.getIntExtra("post_id", 0);
                int commentSize = data.getIntExtra("added_size", 0);
                adapter.addCommentItem(postId, commentSize);
            }
        }
    }
}
