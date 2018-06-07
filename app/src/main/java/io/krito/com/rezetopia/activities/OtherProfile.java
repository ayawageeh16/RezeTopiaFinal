package io.krito.com.rezetopia.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.fragments.AlertFragment;
import io.krito.com.rezetopia.helper.ProfileRecyclerAdapter;
import io.krito.com.rezetopia.models.operations.ProfileOperations;
import io.krito.com.rezetopia.models.pojo.User;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;

public class OtherProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int COMMENT_ACTIVITY_RESULT = 1001;
    private static final int CREATE_POST_RESULT = 1002;

    private static final String USER_ID_EXTRA = "user_id";

    CircleImageView ppView;
    TextView usernameView;
    ImageView coverView;
    TextView addFriendBtn;

    FrameLayout profileHeader;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    String cursor = "0";

    NewsFeed newsFeed;
    ArrayList<NewsFeedItem> tempItems;
    ProfileRecyclerAdapter adapter;
    ProfileCallback profileCallback;

    int start = 0, end = 0;
    boolean loadingData = false;

    String userId;
    String loggedInUserId;
    User user;

    boolean isFriend = false;
    boolean friendState = false;

    public static Intent createIntent(String id, Context context){
        Intent intent = new Intent(context, OtherProfile.class);
        intent.putExtra(USER_ID_EXTRA, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        userId = getIntent().getExtras().getString(USER_ID_EXTRA);
        loggedInUserId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        profileHeader = findViewById(R.id.profileHeader);
        recyclerView = findViewById(R.id.profilePostsRecyclerView);
        progressBar = findViewById(R.id.profileProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        //fetchNewsFeed();

        getInfo();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                if (dy > 0 || dy < 0)
//                    profileCallback.onScroll(false);

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

//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    profileCallback.onScroll(true);
//                }
//
//                super.onScrollStateChanged(recyclerView, newState);
            }
        });

//        ppView = findViewById(R.id.ppView);
//        usernameView = findViewById(R.id.usernameView);
//        coverView = findViewById(R.id.coverView);
//        addFriendBtn = findViewById(R.id.addFriendBtn);
//
//        addFriendBtn.setOnClickListener(this);
//
//        getInfo();
    }

    private void getInfo(){
        ProfileOperations.getInfo(userId);
        ProfileOperations.setInfoCallback(new ProfileOperations.UserInfoCallback() {
            @Override
            public void onSuccess(User u) {
                user = u;
                ProfileOperations.isFriend(loggedInUserId, userId);
                ProfileOperations.setIsFriendCallback(new ProfileOperations.IsFriendCallback() {
                    @Override
                    public void onSuccess(boolean[] is_friend) {
                        isFriend = is_friend[0];
                        friendState = is_friend[1];
                        Log.e("IsFriendActivity", "onSuccess: " + String.valueOf(isFriend) + "-" + String.valueOf(friendState));
                        fetchNewsFeed();
//                        if (isFriend && friendState){
//                            addFriendBtn.setText(getResources().getString(R.string.remove_friend));
//                        } else if (isFriend && !friendState){
//                            addFriendBtn.setText(getResources().getString(R.string.cancel_friend_request));
//                        } else {
//                            addFriendBtn.setText(getResources().getString(R.string.add));
//                        }
                    }

                    @Override
                    public void onError(int error) {
                        AlertFragment.createFragment(getResources().getString(error)).show(getFragmentManager(), null);
                    }
                });
            }

            @Override
            public void onError(int error) {
                AlertFragment.createFragment(getResources().getString(error)).show(getFragmentManager(), null);
            }
        });
    }

    private void sendFriendRequest(){
        addFriendBtn.setEnabled(false);
        ProfileOperations.sendFriendRequest(loggedInUserId, userId);

        ProfileOperations.setFriendRequestCallback(new ProfileOperations.SendFriendRequestCallback() {
            @Override
            public void onSuccess(boolean result) {
                addFriendBtn.setEnabled(true);
                if (result){
                    addFriendBtn.setText(getResources().getString(R.string.cancel_friend_request));
                    isFriend = true;
                } else {

                }
            }

            @Override
            public void onError(int error) {
                addFriendBtn.setEnabled(true);
            }
        });
    }

    private void cancelDeleteFriendship(){
        addFriendBtn.setEnabled(false);
        ProfileOperations.cancelFriendRequest(loggedInUserId, userId);

        ProfileOperations.setCancelDeleteFriendRequestCallback(new ProfileOperations.CancelDeleteFriendRequestCallback() {
            @Override
            public void onSuccess(boolean result) {
                addFriendBtn.setText(getResources().getString(R.string.add));
                addFriendBtn.setEnabled(true);
                isFriend = false;
            }

            @Override
            public void onError(int error) {
                addFriendBtn.setEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFriendBtn:
                if (isFriend){
                    cancelDeleteFriendship();
                } else {
                    sendFriendRequest();
                }
                break;
            case R.id.ppView:
                break;
        }
    }

    private void fetchNewsFeed(){
        Log.i("POST_CURSOR", "fetchNewsFeed: " + cursor);
        ProfileOperations.fetchNewsFeed(userId, cursor);
        ProfileOperations.setFeedCallback(new ProfileOperations.NewsFeedCallback() {
            @Override
            public void onSuccess(NewsFeed feed) {
                if (newsFeed != null && feed.getItems() != null && feed.getItems().size() > 0){
                    final int lastItem = adapter.removeLastItem();
                    Log.i("newsFeed_size", "onSuccess: " + newsFeed.getItems().size() + " " + feed.getItems().size());
                    start = newsFeed.getItems().size();
                    newsFeed.addAllItems(feed.getItems());
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
                String errorString = OtherProfile.this.getResources().getString(error);
                Log.i("news_feed_error", "onError: " + errorString);
                Snackbar.make(profileHeader, error, BaseTransientBottomBar.LENGTH_LONG).show();
                loadingData = false;
            }

            @Override
            public void onEmptyResult() {
                adapter.removeLastItem();
            }
        });
    }

    private void updateUi(int s, int e){
        if (adapter == null){
            adapter = new ProfileRecyclerAdapter(this, newsFeed.getItems(), newsFeed.getNow(), loggedInUserId, userId, isFriend, friendState, user);
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter.setCallback(new ProfileRecyclerAdapter.AdapterCallback() {
                @Override
                public void onStartComment(NewsFeedItem item, long now) {
                    Intent intent = Comment.createIntent(item.getLikes(), Integer.parseInt(item.getPostId()), now, Integer.parseInt(item.getOwnerId()),
                            OtherProfile.this);

                    startActivityForResult(intent, COMMENT_ACTIVITY_RESULT);
                    OtherProfile.this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
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
                    Intent intent = new Intent(OtherProfile.this, CreatePost.class);
                    startActivityForResult(intent, CREATE_POST_RESULT);
                }
            });

        } else if (s > 0 && e > 0){
            adapter.notifyItemRangeInserted(s, e);
        }
    }

    public interface ProfileCallback{
        void onScroll(boolean show);
    }
}
