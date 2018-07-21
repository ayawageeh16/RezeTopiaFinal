package io.krito.com.rezetopia.helper.Group;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.activities.Profile;
import io.krito.com.rezetopia.helper.PostRecyclerAdapter;
import io.krito.com.rezetopia.models.operations.HomeOperations;
import io.krito.com.rezetopia.models.pojo.Group.GroupPost;
import io.krito.com.rezetopia.models.pojo.Group.Posts;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.models.pojo.Group.Post;
import io.krito.com.rezetopia.models.pojo.Group.GroupPostAttachment;


/**
 * Created by Goda on 17/07/2018.
 */

public class GroupPostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HOME_HEADER = 1;
    private static final int VIEW_POST_1 = 2;
    private static final int VIEW_PROGRESS = 3;
    private static final int VIEW_POST_2 = 4;
    private static final int VIEW_POST_3 = 5;
    private static final int VIEW_POST_4 = 6;
    private static final int VIEW_POST_5 = 7;
    private static final int VIEW_PP = 8;

    private PostRecyclerAdapter.AdapterCallback callback;

    Context context;
    List<GroupPost> groupPosts = new ArrayList<>();

    public GroupPostsAdapter (Context context, List<GroupPost> groupPosts){
        this.context=context;
        this.groupPosts=groupPosts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOME_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.create_post_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == VIEW_POST_1) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_1, parent, false);
            return new PostViewHolder(view);
        } else if (viewType == VIEW_POST_2) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_2, parent, false);
            return new PostViewHolder(view);
        } else if (viewType == VIEW_POST_3) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_3, parent, false);
            return new PostViewHolder(view);
        } else if (viewType == VIEW_POST_4) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_4, parent, false);
            return new PostViewHolder(view);
        } else (viewType == VIEW_POST_5) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_5, parent, false);
            return new PostViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return groupPosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return VIEW_HOME_HEADER;
        } else if (groupPosts.get(position- 1).getType() == 1009) {
            return VIEW_PROGRESS;
        } else if (groupPosts.get(position- 1).getPosts().getPost().get(position-1).getAttachment() != null && groupPosts.get(position- 1).getPosts().getPost().get(position-1).getAttachment().getImages() != null && groupPosts.get(position- 1).getPosts().getPost().get(position-1).getAttachment().getImages().size() > 0) {
            if (groupPosts.get(position- 1).getPosts().getPost().get(position-1).getAttachment().getImages().size()== 2) {
                return VIEW_POST_2;
            } else if (groupPosts.get(position- 1).getPosts().getPost().get(position-1).getAttachment().getImages().size()== 3) {
                return VIEW_POST_3;
            } else if (groupPosts.get(position- 1).getPosts().getPost().get(position-1).getAttachment().getImages().size() == 4) {
                return VIEW_POST_4;
            } else if (groupPosts.get(position- 1).getPosts().getPost().get(position-1).getAttachment().getImages().size()== 5) {
                return VIEW_POST_5;
            }
        }
        return VIEW_POST_1;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        TextView postTextView;
        Button likeButton;
        Button commentButton;
        TextView dateView;
        TextView usernameView;
        ImageView ppView;
        ImageView postSideMenu;
        Button postShareButton;
        ImageView verifyView;
        LinearLayout likeCommentHeaderLayout;
        TextView likerCommenterNameView;
        TextView likeCommentMessageView;
        TextView postLikeCommentOwnerNameView;
        TextView shareTextPartOneView;
        TextView shareTextPartTwoView;
        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        ImageView image5;
        ProgressBar progressImage1;
        ProgressBar progressImage2;

        public PostViewHolder(final View itemView) {
            super(itemView);

            postTextView = itemView.findViewById(R.id.postTextView);
            likeButton = itemView.findViewById(R.id.postLikeButton);
            commentButton = itemView.findViewById(R.id.postCommentButton);
            dateView = itemView.findViewById(R.id.postDateView);
            usernameView = itemView.findViewById(R.id.postUserName);
            ppView = itemView.findViewById(R.id.ppView);
            postSideMenu = itemView.findViewById(R.id.postSideMenu);
            postShareButton = itemView.findViewById(R.id.postShareButton);
            likeCommentHeaderLayout = itemView.findViewById(R.id.likeCommentHeaderLayout);
            likerCommenterNameView = itemView.findViewById(R.id.likerCommenterNameView);
            likeCommentMessageView = itemView.findViewById(R.id.likeCommentMessageView);
            postLikeCommentOwnerNameView = itemView.findViewById(R.id.postLikeCommentOwnerNameView);
            shareTextPartOneView = itemView.findViewById(R.id.shareTextPartOneView);
            shareTextPartTwoView = itemView.findViewById(R.id.shareTextPartTwoView);
            verifyView = itemView.findViewById(R.id.verifyView);
            image1 = itemView.findViewById(R.id.postImage1);
            image2 = itemView.findViewById(R.id.postImage2);
            image3 = itemView.findViewById(R.id.postImage3);
            image4 = itemView.findViewById(R.id.postImage4);
            image5 = itemView.findViewById(R.id.postImage5);
            progressImage1 = itemView.findViewById(R.id.progressImage1);
            progressImage2 = itemView.findViewById(R.id.progressImage2);
        }

        public void bind(final GroupPost item, final int pos) {

            if (item.getMessage() != null && !item.getMessage().isEmpty()) {
                if (item.getMessage().contentEquals("like_friends_of_friends")) {
                    likeCommentHeaderLayout.setVisibility(View.VISIBLE);
                    likerCommenterNameView.setText(item.getLikerName());
                    likeCommentMessageView.setText(R.string.like_header_message);
                    if (item.getOwnerId().contentEquals(userId)) {
                        postLikeCommentOwnerNameView.setText(R.string.you);
                    } else {
                        postLikeCommentOwnerNameView.setText(item.getOwnerName());
                    }

                }
            } else {
                likeCommentHeaderLayout.setVisibility(View.GONE);
            }

            postShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //createShareSheetMenu();
                    callback.onStartShare(item);
                }
            });

            if (item.getItemImage() != null) {
                Picasso.with(context).load(item.getItemImage()).into(ppView);
            } else {
                ppView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_avatar));
            }

            if (item.getPostAttachment() != null && item.getPostAttachment().getImages() != null && item.getPostAttachment().getImages().length > 0) {
                if (item.getPostAttachment().getImages().length == 1) {
                    image1.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[0].getPath()).into(image1);
                } else if (item.getPostAttachment().getImages().length == 2) {
                    Log.i("IMAGE1", "bind: " + item.getPostAttachment().getImages()[0].getPath());
                    Log.i("IMAGE2", "bind: " + item.getPostAttachment().getImages()[1].getPath());
                    image1.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[0].getPath()).into(image1, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressImage1.setVisibility(View.GONE);
                            Log.i("loading_images_success", "onSuccess: " + "success loading images-1");
                        }

                        @Override
                        public void onError() {
                            Log.i("loading_images_success", "onSuccess: " + "success loading images-2");
                        }
                    });
                    image2.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[1].getPath()).into(image2, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressImage2.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            Log.i("error_loading_images", "onError: " + "error loading images-2");
                        }
                    });
                } else if (item.getPostAttachment().getImages().length == 3) {
                    image1.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[0].getPath()).into(image1);
                    image2.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[1].getPath()).into(image2);
                    image3.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[2].getPath()).into(image3);
                } else if (item.getPostAttachment().getImages().length == 4) {
                    image1.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[0].getPath()).into(image1);
                    image2.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[1].getPath()).into(image2);
                    image3.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[2].getPath()).into(image3);
                    image4.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[3].getPath()).into(image4);
                } else if (item.getPostAttachment().getImages().length >= 5) {
                    image1.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[0].getPath()).into(image1);
                    Log.i("IMAGE-1", "bind: " + item.getPostAttachment().getImages()[0].getPath());
                    image2.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[1].getPath()).into(image2);
                    Log.i("IMAGE-2", "bind: " + item.getPostAttachment().getImages()[1].getPath());
                    image3.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[2].getPath()).into(image3);
                    Log.i("IMAGE-3", "bind: " + item.getPostAttachment().getImages()[2].getPath());
                    image4.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[3].getPath()).into(image4);
                    Log.i("IMAGE-4", "bind: " + item.getPostAttachment().getImages()[3].getPath());
                    image5.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(item.getPostAttachment().getImages()[4].getPath()).into(image5);
                    Log.i("IMAGE-5", "bind: " + item.getPostAttachment().getImages()[4].getPath());
                } else {
                    image1.setVisibility(View.GONE);
                    if (image2 != null) {
                        image2.setVisibility(View.GONE);
                    }

                    if (image3 != null) {
                        image3.setVisibility(View.GONE);
                    }

                    if (image4 != null) {
                        image4.setVisibility(View.GONE);
                    }

                    if (image5 != null) {
                        image5.setVisibility(View.GONE);
                    }
                }
            } else {
                image1.setVisibility(View.GONE);
                if (image2 != null) {
                    image2.setVisibility(View.GONE);
                }

                if (image3 != null) {
                    image3.setVisibility(View.GONE);
                }

                if (image4 != null) {
                    image4.setVisibility(View.GONE);
                }

                if (image5 != null) {
                    image5.setVisibility(View.GONE);
                }
            }

            String postText = null;
            if (item.getOwnerName() != null) {
                usernameView.setText(item.getOwnerName());
            }

            Date date = null;
            SimpleDateFormat sdf;
            try {
                //last format yyyy-MM-dd hh:mm:ss
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
//                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                long time = sdf.parse(item.getCreatedAt()).getTime();
//                CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
//                dateView.setText(ago);
                String ago = TimeAgo.using(time);
                dateView.setText(ago);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            long milliseconds = date.getTime();
//            long millisecondsFromNow = milliseconds - now;
//            dateView.setText(DateUtils.getRelativeDateTimeString(context, milliseconds, millisecondsFromNow, DateUtils.DAY_IN_MILLIS, 0));


            if (!(item.getPostText() == null || item.getPostText().contentEquals("null"))){
                postTextView.setText(item.getPostText());
            } else {
                postTextView.setText("");
            }

//            if (!(item.getPostText() == null || item.getPostText().contentEquals("null"))){
//                try {
//                    postTextView.setText(URLDecoder.decode(item.getPostText(), "UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                postTextView.setText("");
//            }

            String likeString = context.getResources().getString(R.string.like);
            if (item.getLikes() != null && item.getLikes().length > 0) {

                likeButton.setText(item.getLikes().length + " " + likeString);
                Log.e("post_like ->> " + item.getPostText(), item.getLikes().length + " " + likeString);

                //Log.e("userId", userId);
                for (int id : item.getLikes()) {
                    //Log.e("likesUserId", String.valueOf(id));
                    if (String.valueOf(id).contentEquals(String.valueOf(userId))) {
                        likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_holo_star, 0, 0, 0);
                        break;
                    } else {
                        likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star, 0, 0, 0);
                    }
                }
            } else {
                likeButton.setText(likeString);
                likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star, 0, 0, 0);
            }
            String commentString = context.getResources().getString(R.string.comment);
            if (item.getCommentSize() > 0) {
                commentButton.setText(item.getCommentSize() + " " + commentString);
                Log.e("post_comment ->> " + item.getPostText(), (item.getCommentSize() + " " + commentString));
            } else {
                commentButton.setText(commentString);
            }

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onStartComment(item, now);
                }
            });

            //todo
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String likeString = context.getResources().getString(R.string.like);

                    if (item.getLikes() != null) {
                        for (int i = 0; i < item.getLikes().length; i++) {
                            if (item.getLikes()[i] == Integer.parseInt(userId)) {

                                if (item.getLikes().length > 1) {
                                    likeButton.setText((item.getLikes().length - 1) + " " + likeString);
                                } else {
                                    likeButton.setText(likeString);
                                }
                                likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star, 0, 0, 0);
                                reverseLike(item);
                                return;
                            }
                        }
                    }


                    if (item.getLikes() != null && item.getLikes().length > 0) {
                        likeButton.setText((item.getLikes().length + 1) + " " + likeString);
                    } else {
                        likeButton.setText(("1 " + likeString));
                    }

                    likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_holo_star, 0, 0, 0);
                    performLike(item);

                }
            });

            usernameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getOwnerId().contentEquals(userId)) {
                        //todo start profile activity
                        startOtherProfile(item);
                    } else {
                        startOtherProfile(item);
                    }
                }
            });

            ppView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getOwnerId().contentEquals(userId)) {
                        //todo start profile activity
                        startOtherProfile(item);
                    } else {
                        startOtherProfile(item);
                    }
                }
            });

            postSideMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createSheetMenu(item, Integer.parseInt(item.getPostId()), item.getOwnerId(), pos);
                    /*if (String.valueOf(item.getOwnerId()).contentEquals(String.valueOf(userId))) {
                        //showPostPopupWindow(postSideMenu, true, item.getId(), item.getOwnerId());

                    } else {
                        //showPostPopupWindow(postSideMenu, false, item.getId(), item.getOwnerId());
                    }*/
                }
            });

            if (item.getMessage() != null && !item.getMessage().isEmpty()) {
                if (item.getMessage().contentEquals("friend_share")) {
                    shareTextPartOneView.setVisibility(View.VISIBLE);
                    shareTextPartTwoView.setVisibility(View.VISIBLE);
                    usernameView.setText(item.getSharerUsername());
                }
            } else {
                shareTextPartOneView.setVisibility(View.GONE);
                shareTextPartTwoView.setVisibility(View.GONE);
            }
        }

        private void startOtherProfile(NewsFeedItem item) {
            Intent intent = Profile.createIntent(item.getOwnerId(), context);
            context.startActivity(intent);
        }

        private void performLike(final NewsFeedItem item) {
            HomeOperations homeOperations = new HomeOperations();
            homeOperations.postLike("add_like", userId, item.getOwnerId(), item.getPostId());
            homeOperations.setLikeCallback(new HomeOperations.LikeCallback() {
                @Override
                public void onSuccess() {
                    if (item.getLikes() == null) {
                        item.setLikes(new int[0]);
                    }
                    int[] likes = new int[item.getLikes().length + 1];
                    for (int i = 0; i < item.getLikes().length; i++) {
                        likes[i] = item.getLikes()[i];
                    }

                    likes[likes.length - 1] = Integer.parseInt(userId);
                    item.setLikes(likes);
                }

                @Override
                public void onError(int error) {

                }
            });
        }

        private void reverseLike(final NewsFeedItem item) {
            HomeOperations homeOperations = new HomeOperations();
            homeOperations.postUnlike("remove_like", userId, item.getOwnerId(), item.getPostId());
            homeOperations.setLikeCallback(new HomeOperations.LikeCallback() {
                @Override
                public void onSuccess() {
                    ArrayList<Integer> likesList = new ArrayList<>();
                    for (int id : item.getLikes()) {
                        if (id != Integer.parseInt(userId)) {
                            likesList.add(id);
                        }
                    }

                    int[] likes = new int[likesList.size()];

                    for (int i = 0; i < likesList.size(); i++) {
                        likes[i] = likesList.get(i);
                    }

                    item.setLikes(likes);
                }

                @Override
                public void onError(int error) {

                }
            });
        }
    }

}


