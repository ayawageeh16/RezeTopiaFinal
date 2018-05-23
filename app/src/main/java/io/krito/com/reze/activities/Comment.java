package io.krito.com.reze.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.krito.com.reze.R;
import io.krito.com.reze.application.AppConfig;
import io.krito.com.reze.helper.VolleyCustomRequest;
import io.krito.com.reze.models.operations.HomeOperations;
import io.krito.com.reze.models.pojo.post.ApiCommentResponse;
import io.krito.com.reze.views.CustomEditText;
import io.krito.com.reze.views.CustomTextView;

public class Comment extends AppCompatActivity implements View.OnClickListener{

    private static final int REPLAY_REQUEST_CODE = 1006;

    private static final int VIEW_HEADER = 1;
    private static final int VIEW_COMMENT = 2;

    private static final String COMMENTS_EXTRA = "comment_activity.comments_extra";
    private static final String POST_ID_EXTRA = "comment_activity.post_id_extra";
    private static final String TIME_NOW_EXTRA = "comment_activity.time_now_extra";
    private static final String LIKES_EXTRA = "comment_activity.likes_extra";
    private static final String POST_OWNER_EXTRA = "comment_activity.post_owner_extra";

    ImageView backView;
    ArrayList<io.krito.com.reze.models.pojo.post.Comment> comments;
    int[] likes;
    RecyclerView commentsRecyclerView;
    RecyclerView.Adapter adapter;
    ImageView sendCommentView;
    TextView postLikesView;
    CustomEditText commentEditText;
    CustomTextView loadMoreComments;
    int postId;
    io.krito.com.reze.models.pojo.post.Comment Comment;
    long now;
    String userId;
    int ownerId;
    ProgressBar commentProgressView;
    int addedComments = 0;
    ApiCommentResponse apiComment;
    static LoadMoreCallback loadMoreCallback;


    public static Intent createIntent(int[] likeItems, int postId, long now, int postOwnerId, Context context){
        Intent intent = new Intent(context, Comment.class);
        Bundle bundle = new Bundle();
        bundle.putIntArray(LIKES_EXTRA, likeItems);
        bundle.putInt(POST_ID_EXTRA, postId);
        bundle.putLong(TIME_NOW_EXTRA, now);
        bundle.putInt(POST_OWNER_EXTRA, postOwnerId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        comments = new ArrayList<>();

        backView = findViewById(R.id.commentBackView);
        loadMoreComments = findViewById(R.id.loadMoreComments);
        backView.setOnClickListener(this);

        postLikesView = findViewById(R.id.postLikesCommentView);
        commentProgressView = findViewById(R.id.commentProgressView);
        commentProgressView.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        commentProgressView.setVisibility(View.VISIBLE);


        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        ownerId = getIntent().getExtras().getInt(POST_OWNER_EXTRA, 0);

        likes = getIntent().getExtras().getIntArray(LIKES_EXTRA);

        if (likes != null && likes.length > 0){
            postLikesView.setVisibility(View.VISIBLE);
            postLikesView.setText(likes.length + "");
        } else {
            postLikesView.setVisibility(View.GONE);
        }


        postId = getIntent().getExtras().getInt(POST_ID_EXTRA);
        now = getIntent().getExtras().getLong(TIME_NOW_EXTRA);
        commentsRecyclerView = findViewById(R.id.commentRecView);

        fetchComments();
        sendCommentView = findViewById(R.id.sendCommentView);
        commentEditText = findViewById(R.id.commentEditText);

        sendCommentView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentBackView:
                if (Comment != null){
                    Intent intent = new Intent();
                    //intent.putExtra("comment", Comment);
                    intent.putExtra("added_size", comments.size());
                    intent.putExtra("post_id", postId);
                    setResult(RESULT_OK, intent);
                }
                onBackPressed();
                break;
            case R.id.sendCommentView:
                if (!commentEditText.getText().toString().contentEquals("")) {
                    io.krito.com.reze.models.pojo.post.Comment response = new io.krito.com.reze.models.pojo.post.Comment();
                    response.setCommentText(commentEditText.getText().toString());
                    response.setPending(true);
                    comments.add(response);
                    updateUi();
                    adapter.notifyItemInserted(comments.size()-1);
                    commentsRecyclerView.scrollToPosition(comments.size()-1);
                    performComment();
                }
                break;
        }
    }

    private class LoadMoreHeader extends RecyclerView.ViewHolder{

        private CustomTextView loadMore;

        public LoadMoreHeader(View itemView) {
            super(itemView);

            loadMore = itemView.findViewById(R.id.loadMoreComments);

            loadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchComments();

                    setLoadMoreCallback(new LoadMoreCallback() {
                        @Override
                        public void onSuccess() {
                            //todo
                        }

                        @Override
                        public void onEmptyResult() {
                            loadMore.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView commentTextView;
        TextView createdAtView;
        TextView commenterView;
        TextView commentReplayView;
        TextView commentLikeView;
        TextView postingView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            commentTextView = itemView.findViewById(R.id.commentTextView);
            createdAtView = itemView.findViewById(R.id.commentCreatedAtView);
            commenterView = itemView.findViewById(R.id.commenterView);
            commentReplayView = itemView.findViewById(R.id.commentReplayView);
            commentLikeView = itemView.findViewById(R.id.commentLikeView);
            postingView = itemView.findViewById(R.id.postingView);
        }

        public void bind(final io.krito.com.reze.models.pojo.post.Comment comment, boolean pending, final int position){
            if (pending){
                postingView.setVisibility(View.VISIBLE);
            } else {
                postingView.setVisibility(View.GONE);

                commentTextView.setText(comment.getCommentText());
                createdAtView.setText(comment.getCreatedAt());
                commenterView.setText(comment.getCommenterName());
                String replay = getResources().getString(R.string.replay);

                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(comment.getCreatedAt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long milliseconds = date.getTime();
                long millisecondsFromNow = milliseconds - now;
                createdAtView.setText(DateUtils.getRelativeTimeSpanString(milliseconds, now, milliseconds - now));
                if (comment.getReplaySize() > 0) {
                    commentReplayView.setText(comment.getReplaySize() + " " + replay);
                }

                if (comment.getLikes() != null && comment.getLikes().length > 0) {
                    String like = getResources().getString(R.string.like);
                    commentLikeView.setText(comment.getLikes().length + " " + like);
                    for (int i = 0; i < comment.getLikes().length; i++) {
                        if (comment.getLikes()[i] == Integer.parseInt(userId)) {

                            if (comment.getLikes().length > 0) {
                                commentLikeView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        }
                    }
                }


                commentLikeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String likeString = getResources().getString(R.string.like);

                        if (comment.getLikes() != null && comment.getLikes().length > 0) {
                            for (int i = 0; i < comment.getLikes().length; i++) {
                                if (comment.getLikes()[i] == Integer.parseInt(userId)) {

                                    commentLikeView.setText((comment.getLikes().length - 1) + " " + likeString);
                                    if (!(comment.getLikes().length > 1)) {
                                        commentLikeView.setText(likeString);
                                    }

                                    reverseLike(comment, position);
                                    return;
                                }
                            }
                        }


                        commentLikeView.setText((comment.getLikes().length + 1) + " " + likeString);
                        performLike(comment, position);
                    }
                });

                commentReplayView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ArrayList<CommentReplyResponse> replies = new ArrayList<>();
//                        int[] likes = new int[0];
//                        if (comment.getReplies() != null) {
//                            replies = new ArrayList<>(Arrays.asList(comment.getReplies()));
//                        }
//
//                        if (comment.getLikes() != null) {
//                            likes = comment.getLikes();
//                        }
//
//
//                        Intent intent = ReplayActivity.createIntent(
//                                likes,
//                                postId,
//                                comment.getCommentId(),
//                                now,
//                                CommentActivity.this
//                        );
//
//                        startActivityForResult(intent, REPLAY_REQUEST_CODE);
                        //startActivity(intent);
                    }
                });
            }
        }

        private void performLike(final io.krito.com.reze.models.pojo.post.Comment comment, final int position){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);
                            try {
                                Log.i("comment_like", "onResponse: " + response);
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")){
                                    int[] likes = new int[comment.getLikes().length + 1];
                                    for (int i = 0; i < comment.getLikes().length; i++) {
                                        likes[i] = comment.getLikes()[i];
                                    }

                                    likes[likes.length - 1] = Integer.parseInt(userId);
                                    comment.setLikes(likes);
                                    //adapter.notifyItemChanged(position);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("like_error", "onErrorResponse: " + error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();

                    Log.i("add_like_parameters", "getParams: " + userId + " " + postId + " " + comment.getCommentId());
                    map.put("method", "comment_like");
                    map.put("userId", userId);
                    map.put("post_id", String.valueOf(postId));
                    map.put("comment_id", String.valueOf(comment.getCommentId()));
                    map.put("add_like", String.valueOf(true));

                    return map;
                }
            };

            Volley.newRequestQueue(Comment.this).add(stringRequest);
        }

        private void reverseLike(final io.krito.com.reze.models.pojo.post.Comment comment, final int position){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);
                            try {
                                Log.i("comment_dislike", "onResponse: " + response);
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")){

                                    ArrayList<Integer> likesList = new ArrayList<>();

                                    for (int id : comment.getLikes()) {
                                        if (id != Integer.parseInt(userId)){
                                            likesList.add(id);
                                        }
                                    }

                                    int[] likes = new int[likesList.size()];

                                    for(int i = 0; i < likesList.size(); i++) {
                                        likes[i] = likesList.get(i);
                                    }

                                    comment.setLikes(likes);
                                    //adapter.notifyItemChanged(position);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("unlike_error", "onErrorResponse: " + error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();

                    Log.i("reverse_like_parameters", "getParams: " + userId + " " + postId + " " + comment.getCommentId());
                    map.put("method", "comment_like");
                    map.put("userId", userId);
                    map.put("post_id", String.valueOf(postId));
                    map.put("comment_id", String.valueOf(comment.getCommentId()));
                    map.put("remove_like", String.valueOf(true));

                    return map;
                }
            };

            Volley.newRequestQueue(Comment.this).add(stringRequest);
        }
    }

    private class CommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_HEADER){
                View view = LayoutInflater.from(Comment.this).inflate(R.layout.load_more_header, parent, false);
                return new LoadMoreHeader(view);
            } else {
                View view = LayoutInflater.from(Comment.this).inflate(R.layout.post_comment, parent, false);
                return new CommentViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CommentViewHolder) {
                CommentViewHolder pHolder = (CommentViewHolder) holder;
                pHolder.bind(comments.get(position - 1), comments.get(position - 1).isPending(), position - 1);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)){
                return VIEW_HEADER;
            } else {
                return VIEW_COMMENT;
            }
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    private void performComment(){
        if (commentEditText.getText().toString().length() > 0){
            final String commentText = commentEditText.getText().toString();
            commentEditText.setText(null);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("error")){
                                    //   Toast.makeText(CommentActivity.this, "Error submitting comment", Toast.LENGTH_SHORT).show();
                                    comments.remove(comments.size()-1);
                                } else {
                                    Comment = new io.krito.com.reze.models.pojo.post.Comment();
                                    Comment.setCommenterId(jsonObject.getInt("commenterId"));
                                    Comment.setCommentId(jsonObject.getInt("commentId"));
                                    Comment.setCommentText(jsonObject.getString("commentText"));
                                    Comment.setCreatedAt(jsonObject.getString("createdAt"));
                                    Comment.setCommenterName(jsonObject.getString("commenterName"));
                                    Comment.setPending(false);
                                    Comment.setLikes(new int[0]);
                                    comments.set(comments.size()-1, Comment);

                                    //comments.add(Comment);
                                    adapter.notifyItemChanged(comments.size()-1);
                                    commentsRecyclerView.scrollToPosition(comments.size()-1);
                                    addedComments ++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("method", "add_comment");
                    map.put("post_id", String.valueOf(postId));
                    map.put("comment", commentText);
                    map.put("owner_id",  String.valueOf(ownerId));
                    map.put("userId", userId);

                    return map;
                }
            };

            Volley.newRequestQueue(Comment.this).add(stringRequest);
        } else {
            // Toast.makeText(this, "Empty comment!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REPLAY_REQUEST_CODE){
            if (data != null){
                int commentId = data.getIntExtra("comment_id", 0);

                for (int i = 0; i < comments.size(); i++) {
                    if (comments.get(i).getCommentId() == commentId) {
                        int commentSize = data.getIntExtra("added_size", comments.get(i).getReplaySize());
                        comments.get(i).setReplaySize(commentSize);
                        adapter.notifyItemChanged(i+1);
                        break;
                    }
                }

            }
        }
    }

    private void fetchComments(){
        String cursor = "0";
        if (apiComment != null){
            cursor = apiComment.getNextCursor();
        }


        HomeOperations.fetchComments(String.valueOf(postId), cursor);
        HomeOperations.setFetchCommentsCallback(new HomeOperations.FetchCommentsCallback() {
            @Override
            public void onSuccess(ApiCommentResponse response) {
                if (apiComment != null && response.getComments() != null && response.getComments().length > 0){
                    //comments.addAll(Arrays.asList(response.getComments()));
                    comments.addAll(1, Arrays.asList(response.getComments()));
                } else {
                    comments = new ArrayList<>(Arrays.asList(response.getComments()));
                }

                apiComment = response;

                for (io.krito.com.reze.models.pojo.post.Comment Comment:comments) {
                    Comment.setPending(false);
                }

                updateUi();

                commentProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onError(int error) {
                commentProgressView.setVisibility(View.GONE);
                loadMoreCallback.onEmptyResult();
                if (apiComment != null){
                    apiComment.setError(true);
                }
            }
        });
        /*VolleyCustomRequest stringRequest = new VolleyCustomRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                ApiCommentResponse.class,
                new Response.Listener<ApiCommentResponse>() {
                    @Override
                    public void onResponse(ApiCommentResponse response) {
                        commentProgressView.setVisibility(View.GONE);
                        if (!response.isError()){
                            if (response.getComments() != null) {
                                Log.i("commentResponse", "onResponse: " + response.getComments()[0].getCommentText());
                                comments = new ArrayList<>(Arrays.asList(response.getComments()));
                                for (io.krito.com.reze.models.pojo.post.Comment Comment:comments) {
                                    Comment.setPending(false);
                                }
                                updateUi();
                            } else {
                                Log.i("commentResponse", "onResponse: " + response.isError());
                            }
                        } else {
                            Log.i("commentResponse", "onResponse: " + response.isError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley error", "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("method", "get_comments");
                map.put("post_id", String.valueOf(postId));
                map.put("cursor", "0");

                Log.i("post_id", "getParams: " + postId);

                return map;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);*/
    }

    private void updateUi(){
        if (adapter == null){
            adapter = new CommentRecyclerAdapter();
            commentsRecyclerView.setAdapter(adapter);
            commentsRecyclerView.setLayoutManager(new LinearLayoutManager(Comment.this));
        } else {
            adapter.notifyDataSetChanged();
            commentsRecyclerView.scrollToPosition(1);
        }
    }

    public interface LoadMoreCallback{
        void onSuccess();
        void onEmptyResult();
    }

    public static void setLoadMoreCallback(LoadMoreCallback callback){
        loadMoreCallback = callback;
    }
}

