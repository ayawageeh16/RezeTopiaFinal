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

import java.util.ArrayList;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.fragments.AlertFragment;
import io.krito.com.rezetopia.fragments.Share;
import io.krito.com.rezetopia.helper.ProfileRecyclerAdapter;
import io.krito.com.rezetopia.models.operations.ProfileOperations;
import io.krito.com.rezetopia.models.pojo.User;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.models.pojo.post.PostResponse;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private static final int COMMENT_ACTIVITY_RESULT = 1001;
    private static final int CREATE_POST_RESULT = 1002;
    private static final int CREATE_PP_RESULT = 1003;
    private static final int CREATE_COVER_RESULT = 1004;
    private static final int EDIT_POST_RESULT = 1005;

    private static final String USER_ID_EXTRA = "user_id";


    TextView addFriendBtn;

    FrameLayout profileHeader;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    ImageView backView;
    String cursor = "0";

    NewsFeed newsFeed;
    ProfileRecyclerAdapter adapter;

    int start = 0, end = 0;
    boolean loadingData = false;

    String profileOwnerUserId;
    String loggedInUserId;
    User user;

    boolean isFriend = false;
    boolean friendState = false;

    public static Intent createIntent(String id, Context context) {
        Intent intent = new Intent(context, Profile.class);
        intent.putExtra(USER_ID_EXTRA, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        profileOwnerUserId = getIntent().getExtras().getString(USER_ID_EXTRA);
        loggedInUserId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        profileHeader = findViewById(R.id.profileHeader);
        recyclerView = findViewById(R.id.profilePostsRecyclerView);
        progressBar = findViewById(R.id.profileProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        backView = findViewById(R.id.searchBackView);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);


        getInfo();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0 || dy < 0)
//                    profileCallback.onScroll(false);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
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

//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    profileCallback.onScroll(true);
//                }
//
//                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void getInfo() {
        ProfileOperations.getInfo(profileOwnerUserId);
        ProfileOperations.setInfoCallback(new ProfileOperations.UserInfoCallback() {
            @Override
            public void onSuccess(User u) {
                user = u;
                ProfileOperations.isFriend(loggedInUserId, profileOwnerUserId);
                ProfileOperations.setIsFriendCallback(new ProfileOperations.IsFriendCallback() {
                    @Override
                    public void onSuccess(boolean[] is_friend) {
                        isFriend = is_friend[0];
                        friendState = is_friend[1];
                        Log.e("IsFriendActivity", "onSuccess: " + String.valueOf(isFriend) + "-" + String.valueOf(friendState));
                        fetchNewsFeed();
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

    private void sendFriendRequest() {
        addFriendBtn.setEnabled(false);
        ProfileOperations.sendFriendRequest(loggedInUserId, profileOwnerUserId);

        ProfileOperations.setFriendRequestCallback(new ProfileOperations.SendFriendRequestCallback() {
            @Override
            public void onSuccess(boolean result) {
                addFriendBtn.setEnabled(true);
                if (result) {
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

    private void cancelDeleteFriendship() {
        addFriendBtn.setEnabled(false);
        ProfileOperations.cancelFriendRequest(loggedInUserId, profileOwnerUserId);

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
        switch (v.getId()) {
            case R.id.addFriendBtn:
                if (isFriend) {
                    cancelDeleteFriendship();
                } else {
                    sendFriendRequest();
                }
                break;
            case R.id.ppView:
                break;
            case R.id.searchBackView:
                onBackPressed();
                break;
        }
    }

    private void fetchNewsFeed() {
        Log.i("POST_CURSOR", "fetchSavedPosts: " + cursor);
        ProfileOperations.fetchNewsFeed(profileOwnerUserId, cursor);
        ProfileOperations.setFeedCallback(new ProfileOperations.NewsFeedCallback() {
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
                        }
                    });
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
                String errorString = Profile.this.getResources().getString(error);
                Log.i("news_feed_error", "onError: " + errorString);
                Snackbar.make(profileHeader, error, BaseTransientBottomBar.LENGTH_LONG).show();
                loadingData = false;
            }

            @Override
            public void onEmptyResult() {
                if (adapter != null) {
                    adapter.removeLastItem();
                } else {
                    adapter = new ProfileRecyclerAdapter(Profile.this, new ArrayList<NewsFeedItem>(), 0, loggedInUserId, profileOwnerUserId,
                            isFriend, friendState, user, false);

                    recyclerView.setAdapter(adapter);
                    layoutManager = new LinearLayoutManager(Profile.this);
                    recyclerView.setLayoutManager(layoutManager);
                    progressBar.setVisibility(View.GONE);

                    adapter.setCallback(new ProfileRecyclerAdapter.AdapterCallback() {
                        @Override
                        public void onStartComment(NewsFeedItem item, long now) {
                            Intent intent = Comment.createIntent(item.getLikes(), Integer.parseInt(item.getPostId()), now, Integer.parseInt(item.getOwnerId()),
                                    Profile.this);

                            startActivityForResult(intent, COMMENT_ACTIVITY_RESULT);
                            Profile.this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
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
                        public void showSnackBar(String message) {
                            Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onStartPickPp() {
                            CreatePp.skip = true;
                            startActivityForResult(new Intent(Profile.this, CreatePp.class), CREATE_PP_RESULT);
                            //startActivity(new Intent(Profile.this, CreatePp.class));
                        }

                        @Override
                        public void onStartPickCover() {
                            startActivityForResult(new Intent(Profile.this, CreateCover.class), CREATE_COVER_RESULT);
                            //startActivity(new Intent(Profile.this, CreateCover.class));
                        }

                        @Override
                        public void onStartEditPost(NewsFeedItem item, int index) {
                            Intent intent = new Intent(Profile.this, EditPost.class);
                            intent.putExtra("item" , item);
                            intent.putExtra("index" , index);
                            startActivityForResult(intent, EDIT_POST_RESULT);
                        }

                        @Override
                        public void onStartShare(NewsFeedItem item) {
                            Share share = Share.createShareFragment(item);
                            share.show(getFragmentManager(), null);
                        }

                        @Override
                        public void onStartCreatePost() {
                            Intent intent = new Intent(Profile.this, CreatePost.class);
                            startActivityForResult(intent, CREATE_POST_RESULT);
                        }
                    });
                }
            }
        });
    }

    private void updateUi(int s, int e) {
        if (adapter == null) {
            adapter = new ProfileRecyclerAdapter(this, newsFeed.getItems(), newsFeed.getNow(), loggedInUserId, profileOwnerUserId,
                    isFriend, friendState, user, true);
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter.setCallback(new ProfileRecyclerAdapter.AdapterCallback() {
                @Override
                public void onStartComment(NewsFeedItem item, long now) {
                    Intent intent = Comment.createIntent(item.getLikes(), Integer.parseInt(item.getPostId()), now, Integer.parseInt(item.getOwnerId()),
                            Profile.this);

                    startActivityForResult(intent, COMMENT_ACTIVITY_RESULT);
                    Profile.this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
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
                public void showSnackBar(String message) {
                    Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onStartPickPp() {
                    CreatePp.skip = true;
                    startActivityForResult(new Intent(Profile.this, CreatePp.class), CREATE_PP_RESULT);
                    //startActivity(new Intent(Profile.this, CreatePp.class));
                }

                @Override
                public void onStartPickCover() {
                    startActivityForResult(new Intent(Profile.this, CreateCover.class), CREATE_COVER_RESULT);
                    //startActivity(new Intent(Profile.this, CreateCover.class));
                }

                @Override
                public void onStartEditPost(NewsFeedItem item, int index) {
                    Intent intent = new Intent(Profile.this, EditPost.class);
                    intent.putExtra("item" , item);
                    intent.putExtra("index" , index);
                    startActivityForResult(intent, EDIT_POST_RESULT);
                }

                @Override
                public void onStartShare(NewsFeedItem item) {
                    Share share = Share.createShareFragment(item);
                    share.show(getFragmentManager(), null);
                }

                @Override
                public void onStartCreatePost() {
                    Intent intent = new Intent(Profile.this, CreatePost.class);
                    startActivityForResult(intent, CREATE_POST_RESULT);
                }
            });

        } else if (s > 0 && e > 0) {
            adapter.notifyItemRangeInserted(s, e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMMENT_ACTIVITY_RESULT) {

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
        } else if (requestCode == CREATE_PP_RESULT) {
            if (data != null) {
                NewsFeedItem item = (NewsFeedItem) data.getSerializableExtra("post");
                if (item != null) {
                    //todo add post to adapter
                    adapter.addPostToTop(item);
                    adapter.updatePp(item.getItemImage());
                    //adapter.addPostItem(item);
                }
            }
        } else if (requestCode == CREATE_COVER_RESULT) {
            if (data != null) {
                NewsFeedItem item = (NewsFeedItem) data.getSerializableExtra("post");
                if (item != null) {
                    //todo add post to adapter
                    adapter.addPostToTop(item);
                    adapter.updateCover(item.getItemImage());
                    //adapter.addPostItem(item);
                }
            }
        } else if (requestCode == EDIT_POST_RESULT){
            if (data != null) {
                if (data.getExtras().getSerializable("item") != null) {
                    NewsFeedItem item = (NewsFeedItem) data.getExtras().getSerializable("item");
                    int index = data.getExtras().getInt("index");
                    adapter.setItem(item, index);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProfileOperations.setCursor("0");
    }
}
