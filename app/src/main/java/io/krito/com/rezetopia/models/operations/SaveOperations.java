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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.helper.VolleyCasheRequest;
import io.krito.com.rezetopia.helper.VolleyCustomRequest;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.models.pojo.post.ApiResponse;
import io.krito.com.rezetopia.models.pojo.post.Post;

/**
 * Created by Ahmed Ali on 6/30/2018.
 */

public class SaveOperations  {

    private static RequestQueue requestQueue;
    private static final String baseUrl = "http://rezetopia.dev-krito.com/app/";
    private static SavedPostsCallback saveCallback;
    private static String cursor = "0";

    public static void setCursor(String c){
        cursor = c;
    }

    public static void setSavedCallback(SavedPostsCallback call) {
        saveCallback = call;
    }

    public static void setRequestQueue(RequestQueue queue) {
        requestQueue = queue;
    }

    public static class FetchSavePosts extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "reze/user_post.php";

            VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.POST, url, ApiResponse.class,
                    new Response.Listener<ApiResponse>() {
                        @Override
                        public void onResponse(ApiResponse response) {
                            if (!response.isError()) {
                                Log.i("response_", "onResponse: " + response.isError());
                                if (response.getPosts() != null && response.getPosts().length > 0) {
                                    NewsFeed newsFeed = new NewsFeed();
                                    ArrayList<NewsFeedItem> items = new ArrayList<>();

                                    for (Post post : response.getPosts()) {
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
                                        item.setType(NewsFeedItem.POST_TYPE);
                                        items.add(item);
                                    }

                                    newsFeed.setItems(items);
                                    newsFeed.setNextCursor(response.getNextCursor());
                                    newsFeed.setNow(response.getNow());
                                    saveCallback.onSuccess(newsFeed);
                                    cursor = String.valueOf(Integer.parseInt(cursor) + 11);
                                }
                            } else if (response.isError() && response.getMessage().contentEquals("there are no posts")) {
                                saveCallback.onEmptyResult();
                            } else {
                                Log.i("response_", "onResponse: " + response.isError());
                                saveCallback.onError(R.string.unknown_error);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("response_", "onResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        saveCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ServerError) {
                        saveCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        saveCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        saveCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        saveCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        saveCallback.onError(R.string.time_out);
                        return;
                    }
                    saveCallback.onError(R.string.connection_error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("method", "get_saved");
                    map.put("user_id", strings[0]);
                    map.put("cursor", cursor);
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

    public interface SavedPostsCallback {
        void onSuccess(NewsFeed newsFeed);
        void onError(int error);
        void onEmptyResult();
    }

    public static void fetchSavedPosts(String userId, String cursor) {
        new FetchSavePosts().execute(userId, cursor);
    }
}
