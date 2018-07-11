package io.krito.com.rezetopia.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.fragments.Share;
import io.krito.com.rezetopia.helper.SaveRecyclerAdapter;
import io.krito.com.rezetopia.models.operations.SaveOperations;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;

public class SavedPosts extends AppCompatActivity {

    private static final int COMMENT_ACTIVITY_RESULT = 1001;
    private static final int CREATE_POST_RESULT = 1002;

    FrameLayout homeHeader;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    String cursor = "0";
    int pastVisibleItems;
    NewsFeed newsFeed;
    String userId;
    ArrayList<NewsFeedItem> tempItems;
    SaveRecyclerAdapter adapter;
    SaveCallback saveCallback;
    ImageView backView;
    TextView dontHavePosts;
    SwipeRefreshLayout savedSwipeView;


    int start = 0, end = 0;
    boolean loadingData = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);

       
        homeHeader = findViewById(R.id.homeHeader);
        recyclerView = findViewById(R.id.homePostsRecyclerView);
        progressBar = findViewById(R.id.homeProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        dontHavePosts = findViewById(R.id.dontHavePosts);
        savedSwipeView = findViewById(R.id.savedSwipeView);
        backView = findViewById(R.id.searchBackView);


        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        //Log.i("USER_ID", "onCreateView: " + userId);

        if (savedInstanceState != null && savedInstanceState.getSerializable("feed") != null){
            Log.i("USER_ID_saved", "onCreateView: " + userId);

            newsFeed = (NewsFeed) savedInstanceState.getSerializable("feed");
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    updateUi(0, 0);
                    recyclerView.scrollToPosition(savedInstanceState.getInt("last"));
                    if (newsFeed.getItems().size() > 0){
                        progressBar.setVisibility(View.GONE);
                        dontHavePosts.setVisibility(View.VISIBLE);
                    }
                    //adapter.notifyDataSetChanged();
                }
            });
        } else {
            Log.i("USER_ID", "onCreateView: " + userId);
            SaveOperations.setCursor("0");
            fetchSavedPosts();
        }

        savedSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchSavedPosts();
            }
        });

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        registerScrollListener();
    }

    private void registerScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0)
                    //saveCallback.onScroll(false);

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
                            fetchSavedPosts();
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //saveCallback.onScroll(true);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void fetchSavedPosts() {
        Log.i("POST_CURSOR", "fetchSavedPosts: " + cursor);
        SaveOperations.fetchSavedPosts(userId, cursor);
        SaveOperations.setSavedCallback(new SaveOperations.SavedPostsCallback() {
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
                savedSwipeView.setRefreshing(false);
            }

            @Override
            public void onError(int error) {
                //String errorString = getActivity().getResources().getString(error);
                //Log.i("news_feed_error", "onError: " + errorString);
                //Snackbar.make(homeHeader, error, BaseTransientBottomBar.LENGTH_LONG).show();
                loadingData = false;
            }

            @Override
            public void onEmptyResult() {
                progressBar.setVisibility(View.GONE);
                dontHavePosts.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateUi(int s, int e) {
        if (adapter == null) {
            adapter = new SaveRecyclerAdapter(this, newsFeed.getItems(), newsFeed.getNow(), userId);
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter.setCallback(new SaveRecyclerAdapter.AdapterCallback() {
                @Override
                public void onStartComment(NewsFeedItem item, long now) {
                    Intent intent = Comment.createIntent(item.getLikes(), Integer.parseInt(item.getPostId()), now, Integer.parseInt(item.getOwnerId()),
                            SavedPosts.this);

                    startActivityForResult(intent, COMMENT_ACTIVITY_RESULT);
                    SavedPosts.this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
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
                    share.show(SavedPosts.this.getFragmentManager(), null);
                }

                @Override
                public void onStartEditPost(NewsFeedItem item, int index) {
//                    Intent intent = new Intent(SavedPosts.this, EditPost.class);
//                    intent.putExtra("item" , item);
//                    intent.putExtra("index" , index);
//                    startActivityForResult(intent, EDIT_POST_RESULT);
                }

                @Override
                public void onSetItem(int index) {
                    adapter.notifyItemChanged(index);
                }

                @Override
                public void onStartCreatePost() {
                    Intent intent = new Intent(SavedPosts.this, CreatePost.class);
                    startActivityForResult(intent, CREATE_POST_RESULT);
                }
            });

        } else if (s > 0 && e > 0) {
            adapter.notifyItemRangeInserted(s, e);
        }
    }

    public interface SaveCallback {
        void onScroll(boolean show);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (newsFeed != null) {
            outState.putSerializable("feed", newsFeed);
            outState.putInt("last", pastVisibleItems);
        }
    }
}
