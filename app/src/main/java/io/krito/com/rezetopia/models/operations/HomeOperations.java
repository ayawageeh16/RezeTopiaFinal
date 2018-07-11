package io.krito.com.rezetopia.models.operations;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.helper.VolleyCasheRequest;
import io.krito.com.rezetopia.helper.VolleyCustomRequest;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.models.pojo.post.ApiCommentResponse;
import io.krito.com.rezetopia.models.pojo.post.ApiResponse;
import io.krito.com.rezetopia.models.pojo.post.Post;
import io.krito.com.rezetopia.models.pojo.post.Pp;

/**
 * Created by Ahmed Ali on 5/19/2018.
 */

public class HomeOperations {


    private static final String baseUrl = "http://rezetopia.dev-krito.com/app/reze/";
    private static RequestQueue requestQueue;
    private static NewsFeedCallback feedCallback;
    private LikeCallback likeCallback;
    private static FetchCommentsCallback fetchCommentsCallback;
    private static String homeCursor = "0";


    public static void setCursor(String homeCursor) {
        HomeOperations.homeCursor = homeCursor;
    }

    public static void setRequestQueue(RequestQueue queue){
        requestQueue = queue;
    }

    public static void setFeedCallback(NewsFeedCallback call) {
        feedCallback = call;
    }

    public void setLikeCallback(LikeCallback callback){
        likeCallback = callback;
    }

    public static void setFetchCommentsCallback(FetchCommentsCallback callback) {
        fetchCommentsCallback = callback;
    }

    public static void fetchNewsFeed(String userId, String cursor){
        new FetchNewsFeedTask().execute(userId, cursor);
    }

    public void postLike(String method, String userId, String ownerId, String postId){
        new PerformLikeTask().execute(method, userId, ownerId, postId);
    }

    public void postUnlike(String method, String userId, String ownerId, String postId){
        new PerformLikeTask().execute(method, userId, ownerId, postId);
    }

    public static void fetchComments(String postId, String cursor){
        new FetchCommentsTask().execute(postId, cursor);
    }

    public static class FetchNewsFeedTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "user_post.php";

            VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.POST, url, ApiResponse.class,
                    new Response.Listener<ApiResponse>() {
                @Override
                public void onResponse(ApiResponse response) {
                    NewsFeed newsFeed = new NewsFeed();

                    if (!response.isError()){
                        ArrayList<NewsFeedItem> items = new ArrayList<>();
                        if (response.getPosts() != null && response.getPosts().length > 0){
                            for (Post post:response.getPosts()) {
                                NewsFeedItem item = new NewsFeedItem();
                                item.setPostId(post.getPostId());
                                item.setCreatedAt(post.getCreatedAt());
                                item.setCommentSize(post.getCommentSize());
                                item.setItemImage(post.getImageUrl());
                                item.setLikes(post.getLikes());
                                item.setOwnerId(post.getUserId());
                                item.setOwnerName(post.getUsername());
                                item.setPostText(post.getText());
                                item.setPostAttachment(post.getAttachment());
                                item.setPrivacyId(post.getPrivacyId());
                                if (post.getMessage() != null && !post.getMessage().isEmpty()){
                                    item.setMessage(post.getMessage());
                                    if (post.getMessage().contentEquals("like_friends_of_friends")){
                                        Log.i("like_friends_of_friends", "onResponse: " + "like_friends_of_friends");
                                        item.setLikerId(post.getLikerId());
                                        item.setLikerName(post.getLikerName());
                                    } else if (post.getMessage().contentEquals("friend_share")){
                                        Log.i("friend_share", "onResponse: " + "friend_share");
                                        item.setSharerId(post.getSharerId());
                                        item.setSharerUsername(post.getSharerUsername());
                                        item.setShareTimestamp(post.getShareTimestamp());
                                    }

                                }
                                item.setType(NewsFeedItem.POST_TYPE);
                                items.add(item);
                            }

                            newsFeed.setItems(items);
                            newsFeed.setNextCursor(response.getNextCursor());
                            newsFeed.setNow(response.getNow());
                            homeCursor = String.valueOf(Integer.parseInt(homeCursor) + 11);
                            Log.i("response_cursor", "onResponse: " + homeCursor);
                        }

                        if (response.getPps() != null && response.getPps().length > 0){
                            Log.i("response_pps_size", "onResponse: " + response.getPps().length);
                            for (Pp pp:response.getPps()) {
                                NewsFeedItem item = new NewsFeedItem();
                                item.setPostId(pp.getPostId());
                                item.setCreatedAt(pp.getCreatedAt());
                                item.setCommentSize(pp.getCommentSize());
                                item.setItemImage(pp.getImageUrl());
                                item.setLikes(pp.getLikes());
                                item.setOwnerId(pp.getUserId());
                                item.setOwnerName(pp.getUsername());
                                item.setPostText(pp.getText());
                                item.setPpUrl(pp.getPpUrl());
                                item.setType(NewsFeedItem.PP_TYPE);
                                items.add(item);
                            }
                        }

                        if (newsFeed.getItems().size() > 0){
                            feedCallback.onSuccess(newsFeed);
                        }
                    } else {
                        feedCallback.onError(R.string.unknown_error);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        feedCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ServerError) {
                        feedCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        feedCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        feedCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        feedCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        feedCallback.onError(R.string.time_out);
                        return;
                    }
                    feedCallback.onError(R.string.connection_error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                        map.put("method", "get_relative_news_feed");
                    map.put("userId", strings[0]);
                    map.put("cursor", homeCursor);
                    return map;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(request);
            return null;
        }
    }

    public class PerformLikeTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "user_post.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")){
                                    likeCallback.onSuccess();
                                } else {
                                    likeCallback.onError(R.string.unknown_error);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("like_error", "onErrorResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        likeCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ServerError) {
                        likeCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        likeCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        likeCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        likeCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        likeCallback.onError(R.string.time_out);
                        return;
                    }
                    likeCallback.onError(R.string.connection_error);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("method", "post_like");
                    map.put("userId", strings[1]);
                    map.put("owner_id", strings[2]);
                    map.put("post_id", strings[3]);

                    if (strings[0].contentEquals("add_like")){
                        map.put("add_like", String.valueOf(true));
                    } else {
                        map.put("remove_like", String.valueOf(true));
                    }


                    return map;
                }
            };

            requestQueue.add(stringRequest);
            return null;
        }
    }

    public static class     FetchCommentsTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(final String... strings) {
            VolleyCustomRequest stringRequest = new VolleyCustomRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                    ApiCommentResponse.class,
                    new Response.Listener<ApiCommentResponse>() {
                        @Override
                        public void onResponse(ApiCommentResponse response) {
                            if (!response.isError()){
                                fetchCommentsCallback.onSuccess(response);
                            } else {
                                fetchCommentsCallback.onError(R.string.empty_date);
                                Log.i("commentResponse", "onResponse: " + response.isError());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        fetchCommentsCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ServerError) {
                        fetchCommentsCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        fetchCommentsCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        fetchCommentsCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        fetchCommentsCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        fetchCommentsCallback.onError(R.string.time_out);
                        return;
                    }
                    fetchCommentsCallback.onError(R.string.connection_error);

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("method", "get_comments");
                    map.put("post_id", strings[0]);
                    map.put("cursor", strings[1]);

                    return map;
                }
            };

            requestQueue.add(stringRequest);
            return null;
        }
    }

    public interface NewsFeedCallback{
        void onSuccess(NewsFeed newsFeed);
        void onError(int error);
    }

    public interface LikeCallback {
        void onSuccess();
        void onError(int error);
    }

    public interface FetchCommentsCallback{
        void onSuccess(ApiCommentResponse response);
        void onError(int error);
    }
}
