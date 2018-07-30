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
    Posts posts ;

    public GroupPostsAdapter (Context context, Posts posts){
        this.context=context;
        this.posts=posts;
     }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOME_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.create_post_header, parent, false);
            return new PostViewHolder(view);
         //   return new HeaderViewHolder(view);
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
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_5, parent, false);
            return new PostViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            PostViewHolder pHolder = (PostViewHolder) holder;
            pHolder.bind(posts,position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return posts.getPost().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return VIEW_HOME_HEADER;
        } else if (posts.getPost().get(position- 1).getAttachment() != null && posts.getPost().get(position- 1).getAttachment().getImages() != null && posts.getPost().get(position- 1).getAttachment().getImages().size() > 0) {
            if (posts.getPost().get(position- 1).getAttachment().getImages().size()== 2) {
                return VIEW_POST_2;
            } else if (posts.getPost().get(position- 1).getAttachment().getImages().size()== 3) {
                return VIEW_POST_3;
            } else if (posts.getPost().get(position- 1).getAttachment().getImages().size() == 4) {
                return VIEW_POST_4;
            } else if (posts.getPost().get(position- 1).getAttachment().getImages().size()== 5) {
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

        public void bind(final Posts item, final int pos) {
            postTextView.setText(item.getPost().get(pos).getText());
        }
    }

    public interface AdapterCallback {
        void onStartComment(NewsFeedItem item, long now);

        void onStartCreatePost();

        void onItemAdded(int position);

        void onItemRemoved(int position);

        void onStartShare(NewsFeedItem item);

        void onStartEditPost(NewsFeedItem item, int index);

        void onSetItem(int index);
    }

}


