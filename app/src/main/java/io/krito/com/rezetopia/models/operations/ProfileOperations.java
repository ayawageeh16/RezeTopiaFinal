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
import io.krito.com.rezetopia.helper.VolleyCustomRequest;
import io.krito.com.rezetopia.models.pojo.User;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeed;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.models.pojo.post.ApiResponse;
import io.krito.com.rezetopia.models.pojo.post.Post;

/**
 * Created by Ahmed Ali on 6/2/2018.
 */

public class ProfileOperations {

    private static final String baseUrl = "http://rezetopia.dev-krito.com/app/";

    private static RequestQueue requestQueue;
    private static UserInfoCallback infoCallback;
    private static IsFriendCallback isFriendCallback;
    private static SendFriendRequestCallback friendRequestCallback;
    private static CancelDeleteFriendRequestCallback cancelDeleteFriendRequestCallback;
    private static NewsFeedCallback feedCallback;
    private static AcceptFriendRequestCallback acceptCallback;
    private static String profileCursor = "0";

    public static void setCursor(String profileCursor) {
        ProfileOperations.profileCursor = profileCursor;
    }

    public static void setFriendRequestCallback(SendFriendRequestCallback friendRequestCallback) {
        ProfileOperations.friendRequestCallback = friendRequestCallback;
    }

    public static void setCancelDeleteFriendRequestCallback(CancelDeleteFriendRequestCallback cancelDeleteFriendRequestCallback) {
        ProfileOperations.cancelDeleteFriendRequestCallback = cancelDeleteFriendRequestCallback;
    }

    public static void setFeedCallback(NewsFeedCallback call) {
        feedCallback = call;
    }

    public static void setInfoCallback(UserInfoCallback callback) {
        infoCallback = callback;
    }

    public static void setAcceptCallback(AcceptFriendRequestCallback callback){
        acceptCallback = callback;
    }

    public static void setIsFriendCallback(IsFriendCallback callback) {
        isFriendCallback = callback;
    }

    public static void setRequestQueue(RequestQueue queue) {
        requestQueue = queue;
    }

    public static void getInfo(String id) {
        new GetInfoTask().execute(id);
    }

    public static void isFriend(String from, String to) {
        new IsFriendTask().execute(from, to);
    }

    public static void sendFriendRequest(String from, String to) {
        new SendFriendRequestTask().execute(from, to);
    }

    public static void cancelFriendRequest(String from, String to) {
        new CancelDeleteFriendRequestTask().execute(from, to);
    }

    public static void fetchNewsFeed(String userId, String cursor) {
        new FetchNewsFeedTask().execute(userId, cursor);
    }

    public static void acceptFriendRequest(String from, String to){
        new AcceptFriendRequestTask().execute(from, to);
    }

    private static class GetInfoTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "getInfo.php";

            StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (!jsonResponse.getBoolean("error")) {
                            Log.e("getInfo", response);
                            User user = new User();
                            user.setId(jsonResponse.getInt("id"));
                            user.setName(jsonResponse.getString("name"));
                            user.setCity(jsonResponse.getString("city"));
                            user.setEmail(jsonResponse.getString("email"));
                            user.setPosition(jsonResponse.getString("position"));
                            user.setImageUrl(jsonResponse.getString("img"));
                            user.setCover(jsonResponse.getString("cover"));
                            infoCallback.onSuccess(user);
                        } else {
                            infoCallback.onError(R.string.wrong_login);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = null;
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        //message = String.valueOf();
                        infoCallback.onError(R.string.checkingNetwork);
                        return;
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        infoCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        infoCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        infoCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        infoCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        infoCallback.onError(R.string.time_out);
                        return;
                    }
                    infoCallback.onError(R.string.time_out);
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    params.put("getInfo", "getInfo");
                    params.put("id", strings[0]);
                    return params;
                }
            };
            requestQueue.add(post);
            return null;
        }
    }

    private static class IsFriendTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "addfriend.php";

            StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.e("addfriend", response);
                        if (!jsonResponse.getBoolean("error")) {
                            boolean state = (jsonResponse.getInt("state") == 1);
                            isFriendCallback.onSuccess(new boolean[]{true, state});
                        } else {
                            isFriendCallback.onSuccess(new boolean[]{false, false});
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = null;
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        //message = String.valueOf();
                        isFriendCallback.onError(R.string.checkingNetwork);
                        return;
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        isFriendCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        isFriendCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        isFriendCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        isFriendCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        isFriendCallback.onError(R.string.time_out);
                        return;
                    }
                    isFriendCallback.onError(R.string.time_out);
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    Log.i("isFriendParams", "getParams: " + strings[0] + "-" + strings[1]);
                    params.put("isFriend", "isFriend");
                    params.put("from", strings[0]);
                    params.put("to", strings[1]);
                    return params;
                }
            };
            requestQueue.add(post);
            return null;
        }
    }

    private static class SendFriendRequestTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "addfriend.php";

            StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.e("add", response);
                        if (!jsonResponse.getBoolean("error")) {
                            friendRequestCallback.onSuccess(true);
                        } else {
                            friendRequestCallback.onSuccess(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = null;
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        //message = String.valueOf();
                        friendRequestCallback.onError(R.string.checkingNetwork);
                        return;
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        friendRequestCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        friendRequestCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        friendRequestCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        friendRequestCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        friendRequestCallback.onError(R.string.time_out);
                        return;
                    }
                    friendRequestCallback.onError(R.string.time_out);
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    Log.i("addParams", "getParams: " + strings[0] + "-" + strings[1]);
                    params.put("add", "add");
                    params.put("from", strings[0]);
                    params.put("to", strings[1]);
                    return params;
                }
            };
            requestQueue.add(post);
            return null;
        }
    }

    private static class CancelDeleteFriendRequestTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "addfriend.php";

            StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.e("unFriend", response);
                        if (!jsonResponse.getBoolean("error")) {
                            cancelDeleteFriendRequestCallback.onSuccess(true);
                        } else {
                            cancelDeleteFriendRequestCallback.onSuccess(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = null;
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        //message = String.valueOf();
                        cancelDeleteFriendRequestCallback.onError(R.string.checkingNetwork);
                        return;
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        cancelDeleteFriendRequestCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        cancelDeleteFriendRequestCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        cancelDeleteFriendRequestCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        cancelDeleteFriendRequestCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        cancelDeleteFriendRequestCallback.onError(R.string.time_out);
                        return;
                    }
                    cancelDeleteFriendRequestCallback.onError(R.string.time_out);
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    Log.i("unFriendParams", "getParams: " + strings[0] + "-" + strings[1]);
                    params.put("unFriend", "add");
                    params.put("from", strings[0]);
                    params.put("to", strings[1]);
                    return params;
                }
            };
            requestQueue.add(post);
            return null;
        }
    }

    private static class AcceptFriendRequestTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "addfriend.php";

            StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.e("accept", response);
                        if (!jsonResponse.getBoolean("error")) {
                            acceptCallback.onSuccess(true);
                        } else {
                            acceptCallback.onSuccess(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = null;
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                    if (error instanceof NetworkError) {
                        //message = String.valueOf();
                        acceptCallback.onError(R.string.checkingNetwork);
                        return;
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        acceptCallback.onError(R.string.server_error);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        acceptCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        acceptCallback.onError(R.string.parsing_error);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        acceptCallback.onError(R.string.connection_error);
                        return;
                    } else if (error instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        acceptCallback.onError(R.string.time_out);
                        return;
                    }
                    acceptCallback.onError(R.string.time_out);
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    Log.i("acceptParams", "getParams: " + strings[0] + "-" + strings[1]);
                    params.put("accept", "accept");
                    params.put("from", strings[0]);
                    params.put("to", strings[1]);
                    return params;
                }
            };
            requestQueue.add(post);
            return null;
        }
    }

    public static class FetchNewsFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            String url = baseUrl + "reze/user_post.php";

            VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.POST, url, ApiResponse.class,
                    new Response.Listener<ApiResponse>() {
                        @Override
                        public void onResponse(ApiResponse response) {
                            if (!response.isError()) {
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
                                        item.setPrivacyId(post.getPrivacyId());
                                        item.setPostAttachment(post.getAttachment());
                                        item.setPpUrl(post.getPpUrl());
                                        item.setCoverUrl(post.getCoverUrl());
                                        item.setType(NewsFeedItem.POST_TYPE);
                                        items.add(item);
                                    }

                                    newsFeed.setItems(items);
                                    newsFeed.setNextCursor(response.getNextCursor());
                                    newsFeed.setNow(response.getNow());
                                    feedCallback.onSuccess(newsFeed);
                                    profileCursor = String.valueOf(Integer.parseInt(profileCursor) + 11);
                                    Log.i("response_cursor", "onResponse: " + profileCursor);
                                }
                            } else if (response.isError() && response.getMessage().contentEquals("there are no posts")) {
                                feedCallback.onEmptyResult();
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
                    map.put("method", "get_user_profile");
                    map.put("userId", strings[0]);
                    map.put("cursor", profileCursor);
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

    public interface UserInfoCallback {
        void onSuccess(User user);

        void onError(int error);
    }

    public interface IsFriendCallback {
        void onSuccess(boolean[] isFriend);

        void onError(int error);
    }

    public interface SendFriendRequestCallback {
        void onSuccess(boolean result);
        void onError(int error);
    }

    public interface CancelDeleteFriendRequestCallback {
        void onSuccess(boolean result);

        void onError(int error);
    }

    public interface NewsFeedCallback {
        void onSuccess(NewsFeed newsFeed);
        void onError(int error);
        void onEmptyResult();
    }

    public interface AcceptFriendRequestCallback {
        void onSuccess(boolean result);
        void onError(int error);
    }
}
