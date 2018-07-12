package io.krito.com.rezetopia.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.models.operations.HomeOperations;
import io.krito.com.rezetopia.models.operations.ProfileOperations;
import io.krito.com.rezetopia.models.operations.SaveOperations;
import io.krito.com.rezetopia.models.operations.UserOperations;
import io.krito.com.rezetopia.receivers.ConnectivityReceiver;

public class RezetopiaApp extends Application {

    public static final String TAG = RezetopiaApp.class.getSimpleName();

    private static boolean sIsChatActivityOpen = false;
    private RequestQueue mRequestQueue;
    private RequestQueue mSaveRequestQueue;
    private ConnectivityReceiver receiver;
    private static RezetopiaApp mInstance;


    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        receiver = new ConnectivityReceiver();
        registerReceiver(receiver, filter);

        /*getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putString(FirebaseInstanceId.getInstance().getToken(), AppConfig.DEVICE_TOKEN_SHARED)
                .apply();
        //Toast.makeText(this, FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_LONG).show();
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            Log.i("fcm_token", FirebaseInstanceId.getInstance().getToken());
            updateToken();
        }*/

        UserOperations.setRequestQueue(getRequestQueue());
        HomeOperations.setRequestQueue(getRequestQueue());
        ProfileOperations.setRequestQueue(getRequestQueue());
        SaveOperations.setRequestQueue(getSaveRequestQueue() );
    }

    public static synchronized RezetopiaApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public RequestQueue getRequestQueue() {
        if (mSaveRequestQueue == null) {
            mSaveRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mSaveRequestQueue;
    }

    public RequestQueue getSaveRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void removeQueue(){
        mRequestQueue = null;
    }

    private void updateToken() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/push_notification.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error) {
                                Log.i("volley response", "updateToken: " + jsonObject.getString("message"));
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                String token = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                        .getString(AppConfig.DEVICE_TOKEN_SHARED, "null");

                String userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                        .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "null");

                map.put("method", "update_token");
                map.put("token", token);
                map.put("userId", userId);

                return map;
            }
        };

        getRequestQueue().add(stringRequest);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(receiver);
    }
}

