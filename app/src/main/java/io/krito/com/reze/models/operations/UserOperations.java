package io.krito.com.reze.models.operations;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

import io.krito.com.reze.models.pojo.User;

public class UserOperations {

    private static final String baseUrl = "https://rezetopia.div-krito.com/app/";

    private static RegisterCallback registerCallback;
    private static RequestQueue requestQueue;

    public static void setRegisterCallback(RegisterCallback callback) {
        registerCallback = callback;
    }

    public static void setRequestQueue(RequestQueue queue){
        requestQueue = queue;
    }

    public void Register(User user){
        new RegisterTask().execute(user);
    }

    interface RegisterCallback{
        void onSuccess(String userId);
        void onError(String error);
    }

    private static class RegisterTask extends AsyncTask<User, Void, Void>{

        @Override
        protected Void doInBackground(User... users) {
            String url = baseUrl + "register.php";
            final User user = users[0];
            StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (!jsonResponse.getBoolean("error")) {
                            Log.e("register", response);
                            registerCallback.onSuccess(jsonResponse.getString("id"));
                        } else {
                            registerCallback.onError(jsonResponse.getString("msg"));
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
                        message = "Cannot connect to Internet...Please check your connection!";
                        registerCallback.onError(message);
                        return;
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        registerCallback.onError(message);
                        return;
                    } else if (error instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        registerCallback.onError(message);
                        return;
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        registerCallback.onError(message);
                        return;
                    } else if (error instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        registerCallback.onError(message);
                        return;
                    } else if (error instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        registerCallback.onError(message);
                        return;
                    }
                    registerCallback.onError("Cannot connect to Internet...Please check your connection!");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String>  params = new HashMap<>();
                    // the POST parameters:
                    params.put("name", user.getName());
                    params.put("address", "empty");
                    params.put("mobile", user.getMobile());
                    params.put("mail", user.getEmail());
                    params.put("password", user.getPassword());
                    params.put("birthday", user.getBirthday());
                    params.put("weight", "0");
                    params.put("height", "0");
                    params.put("nationality", "empty");
                    params.put("city", "empty");
                    params.put("position", "empty");
                    return params;
                }
            };
            requestQueue.add(post);
            return null;
        }
    }
}
