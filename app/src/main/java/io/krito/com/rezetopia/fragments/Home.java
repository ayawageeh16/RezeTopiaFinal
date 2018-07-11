package io.krito.com.rezetopia.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.isupatches.wisefy.WiseFy;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyCache;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.activities.ConnectionBuddyActivity;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import java.util.ArrayList;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.activities.Comment;
import io.krito.com.rezetopia.activities.CreatePost;
import io.krito.com.rezetopia.activities.EditPost;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.helper.PostRecyclerAdapter;
import io.krito.com.rezetopia.models.operations.HomeOperations;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.models.pojo.post.PostResponse;
import io.krito.com.rezetopia.receivers.ConnectivityReceiver;

public class Home extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final int COMMENT_ACTIVITY_RESULT = 1001;
    private static final int CREATE_POST_RESULT = 1002;
    private static final int EDIT_POST_RESULT = 1003;

    SwipeRefreshLayout homeSwipeView;
    FrameLayout homeHeader;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    String cursor = "0";
    int pastVisibleItems;
    Merlin merlinConn;
    Merlin merlinDisConn;

    NewsFeed newsFeed;
    String userId;
    ArrayList<NewsFeedItem> tempItems;
    PostRecyclerAdapter adapter;
    HomeCallback homeCallback;

    int start = 0, end = 0;
    boolean loadingData = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeHeader = view.findViewById(R.id.homeHeader);
        recyclerView = view.findViewById(R.id.homePostsRecyclerView);
        homeSwipeView = view.findViewById(R.id.homeSwipeView);
        progressBar = view.findViewById(R.id.homeProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        userId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        //Log.i("USER_ID", "onCreateView: " + userId);

        if (savedInstanceState != null && savedInstanceState.getSerializable("feed") != null){

            newsFeed = (NewsFeed) savedInstanceState.getSerializable("feed");
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    updateUi(0, 0);
                    recyclerView.scrollToPosition(savedInstanceState.getInt("last"));
                    //adapter.notifyDataSetChanged();
                }
            });
        } else {
            fetchNewsFeed();
        }

        registerScrollListener();

        homeSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsFeed = null;
                adapter = null;
                HomeOperations.setCursor("0");
                fetchNewsFeed();
            }
        });

//        NoNet.check(getActivity())
//                .toast()
//                .callback(new Monitor.Callback() {
//                    @Override
//                    public void onConnectionEvent(int connectionStatus) {
//                        Log.i("NoNet", "onConnectionEvent: " + connectionStatus);
//                    }
//                }).start();

//        merlinConn = new Merlin.Builder().withConnectableCallbacks().build(getActivity().getApplicationContext());
//        merlinConn.registerConnectable(new Connectable() {
//            @Override
//            public void onConnect() {
//                Log.i("HazNet", "onConnectionEvent: ");
//            }
//        });
//
//        merlinDisConn = new Merlin.Builder().withDisconnectableCallbacks().build(getActivity().getApplicationContext());
//        merlinDisConn.registerDisconnectable(new Disconnectable() {
//            @Override
//            public void onDisconnect() {
//                Log.i("NoNet", "onConnectionEvent: ");
//            }
//        });

//        ConnectionBuddyConfiguration networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder(getActivity()).build();
//        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);


        return view;
    }

    private void registerScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0)
                    homeCallback.onScroll(false);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();


                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        //Snackbar.make(homeHeader, R.string.loading, BaseTransientBottomBar.LENGTH_LONG).show();
                        //adapter.notifyItemInserted(adapter.addItem());
                        if (!loadingData) {
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

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    homeCallback.onScroll(true);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    public void onResume() {
        super.onResume();
        RezetopiaApp.getInstance().setConnectivityListener(this);
//        merlinConn.bind();
    }

    @Override
    public void onPause() {
       // merlinConn.unbind();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
    }

    private void fetchNewsFeed() {
        Log.i("POST_CURSOR", "fetchSavedPosts: " + cursor);
        HomeOperations.fetchNewsFeed(userId, cursor);
        HomeOperations.setFeedCallback(new HomeOperations.NewsFeedCallback() {
            @Override
            public void onSuccess(NewsFeed feed) {
                if (newsFeed != null && feed.getItems() != null && feed.getItems().size() > 0) {
                    final int lastItem = adapter.removeLastItem();
                    Log.i("newsFeed_size", "onSuccess: " + newsFeed.getItems().size() + " " + feed.getItems().size());
                    start = newsFeed.getItems().size();
                    newsFeed.addAllItems(feed.getItems());
                    newsFeed.setNow(feed.getNow());
                    newsFeed.setNextCursor(feed.getNextCursor());
                    end = newsFeed.getItems().size() - 1;
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

                if (homeSwipeView.isRefreshing()){
                    homeSwipeView.setRefreshing(false);
                }
            }

            @Override
            public void onError(int error) {
                //String errorString = getActivity().getResources().getString(error);
                //Log.i("news_feed_error", "onError: " + errorString);
                //Snackbar.make(homeHeader, error, BaseTransientBottomBar.LENGTH_LONG).show();
                loadingData = false;
                if (homeSwipeView.isRefreshing()){
                    homeSwipeView.setRefreshing(false);
                }
            }
        });
    }

    private void updateUi(int s, int e) {
        if (adapter == null) {
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
                public void onStartShare(NewsFeedItem item) {
                    Share share = Share.createShareFragment(item);
                    share.show(getActivity().getFragmentManager(), null);
                }

                @Override
                public void onStartEditPost(NewsFeedItem item, int index) {
                    Intent intent = new Intent(getActivity(), EditPost.class);
                    intent.putExtra("item" , item);
                    intent.putExtra("index" , index);
                    startActivityForResult(intent, EDIT_POST_RESULT);
                }

                @Override
                public void onSetItem(int index) {
                    adapter.notifyItemChanged(index);
                }

                @Override
                public void onStartCreatePost() {
                    Intent intent = new Intent(getActivity(), CreatePost.class);
                    startActivityForResult(intent, CREATE_POST_RESULT);
                }
            });

        } else if (s > 0 && e > 0) {
            adapter.notifyItemRangeInserted(s, e);
        }
    }

    public interface HomeCallback {
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
        if (requestCode == COMMENT_ACTIVITY_RESULT) {
            if (data != null) {
                int postId = data.getIntExtra("post_id", 0);
                int commentSize = data.getIntExtra("added_size", 0);
                adapter.addCommentItem(postId, commentSize);
            }
        } else if (requestCode == CREATE_POST_RESULT) {
            if (data != null) {
                PostResponse returnPost = (PostResponse) data.getSerializableExtra("post");
                if (returnPost != null) {
                    NewsFeedItem item = new NewsFeedItem();
                    item.setPostId(String.valueOf(returnPost.getPostId()));
                    item.setCreatedAt(returnPost.getCreatedAt());
                    item.setOwnerId(returnPost.getUserId());
                    item.setOwnerName(returnPost.getUsername());
                    item.setPostText(returnPost.getText());
                    item.setPostAttachment(returnPost.getAttachment());
                    item.setLikes(null);
                    item.setCommentSize(0);
                    item.setType(NewsFeedItem.POST_TYPE);
                    //todo add post to adapter
                    adapter.addPostToTop(item);
                    //adapter.addPostItem(item);
                }
            }
        } else if (requestCode == EDIT_POST_RESULT){
            NewsFeedItem item = (NewsFeedItem) data.getExtras().getSerializable("item");
            int index = data.getExtras().getInt("index");
            adapter.setItem(item, index);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("feed" ,newsFeed);
        outState.putInt("last", pastVisibleItems);
    }
}
