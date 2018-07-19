package io.krito.com.rezetopia.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.models.pojo.GroupPost;

/**
 * Created by Goda on 17/07/2018.
 */

public class GroupPostsAdapter extends RecyclerView.Adapter<GroupPostsAdapter.GroupPostViewHolder> {

    private static final int VIEW_HOME_HEADER = 1;
    private static final int VIEW_POST_1 = 2;
    private static final int VIEW_PROGRESS = 3;
    private static final int VIEW_POST_2 = 4;
    private static final int VIEW_POST_3 = 5;
    private static final int VIEW_POST_4 = 6;
    private static final int VIEW_POST_5 = 7;
    private static final int VIEW_PP = 8;

    Context context;
    List<GroupPost> groupPosts = new ArrayList<>();

    public GroupPostsAdapter (Context context, List<GroupPost> groupPosts){
        this.context=context;
        this.groupPosts=groupPosts;

    }

    @NonNull
    @Override
    public GroupPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOME_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.create_post_header, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        } else if (viewType == VIEW_POST_1) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_1, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        } else if (viewType == VIEW_POST_2) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_2, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        } else if (viewType == VIEW_POST_3) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_3, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        } else if (viewType == VIEW_POST_4) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_4, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        } else if (viewType == VIEW_POST_5) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_card_5, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        } else if (viewType == VIEW_PP) {
            View view = LayoutInflater.from(context).inflate(R.layout.pp_card, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.progress, parent, false);
            return new GroupPostsAdapter.GroupPostViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GroupPostViewHolder holder, int position) {
                 holder.bind(groupPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return groupPosts.size();
    }

    public class GroupPostViewHolder extends RecyclerView.ViewHolder {
      public GroupPostViewHolder(View itemView) {
          super(itemView);
      }
      public void bind (GroupPost groupPost){

      }
  }
}
