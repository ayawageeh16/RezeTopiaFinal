package io.krito.com.rezetopia.activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gao.jiefly.abilitychartlibrary.AbilityChatView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.models.pojo.User;

public class Skills extends AppCompatActivity {

    String userId;

    AbilityChatView skillAbility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);

        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        getSkills();
    }

    private void getSkills(){
        StringRequest post = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/get_skills.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("getSkills", "onResponse: " + response);
                try {
                    JSONObject json = new JSONObject(response);
                    if (!json.getBoolean("error")){
                        List<Double> data = new ArrayList<>();
                        JSONObject jsonObject = json.getJSONObject("user");
                        data.add((double) jsonObject.getInt("attack"));
                        data.add((double) jsonObject.getInt("defence"));
                        data.add((double) jsonObject.getInt("stamina"));
                        data.add((double) jsonObject.getInt("speed"));
                        //data.add((double) jsonObject.getInt("ball_control"));
                        //data.add((double) jsonObject.getInt("low_pass"));
                        //data.add((double) jsonObject.getInt("lofted_pass"));
                        //data.add((double) jsonObject.getInt("shoot_accuracy"));
                        data.add((double) jsonObject.getInt("shoot_power"));
                        //data.add((double) jsonObject.getInt("free_kicks"));
                        //data.add((double) jsonObject.getInt("header"));
                        data.add((double) jsonObject.getInt("jump"));

                        skillAbility = findViewById(R.id.skillAbility);
                        skillAbility.setCount(6);
                        skillAbility.changeTitles(new String[]{
                                "Attack",
                                "Defense",
                                "Stamina",
                                "Speed",
                                "Shoot",
                                "Jump"
                        });
                        skillAbility.setData(data);

                    } else {

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
                    //infoCallback.onError(R.string.checkingNetwork);
                    return;
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    //infoCallback.onError(R.string.server_error);
                    return;
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    //infoCallback.onError(R.string.connection_error);
                    return;
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    //infoCallback.onError(R.string.parsing_error);
                    return;
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    //infoCallback.onError(R.string.connection_error);
                    return;
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    //infoCallback.onError(R.string.time_out);
                    return;
                }
                //infoCallback.onError(R.string.time_out);
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("method", "get_skills");
                params.put("id", userId);
                return params;
            }
        };
        RezetopiaApp.getInstance().getRequestQueue().add(post);
    }

    private void endorse(){
        StringRequest post = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/skills_endorse.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("getSkills", "onResponse: " + response);
                try {
                    JSONObject json = new JSONObject(response);
                    if (!json.getBoolean("error")){
                        List<Double> data = new ArrayList<>();
                        JSONObject jsonObject = json.getJSONObject("user");
                        data.add((double) jsonObject.getInt("attack"));
                        data.add((double) jsonObject.getInt("defence"));
                        data.add((double) jsonObject.getInt("stamina"));
                        data.add((double) jsonObject.getInt("speed"));
                        //data.add((double) jsonObject.getInt("ball_control"));
                        //data.add((double) jsonObject.getInt("low_pass"));
                        //data.add((double) jsonObject.getInt("lofted_pass"));
                        //data.add((double) jsonObject.getInt("shoot_accuracy"));
                        data.add((double) jsonObject.getInt("shoot_power"));
                        //data.add((double) jsonObject.getInt("free_kicks"));
                        //data.add((double) jsonObject.getInt("header"));
                        data.add((double) jsonObject.getInt("jump"));

                        skillAbility = findViewById(R.id.skillAbility);
                        skillAbility.setCount(6);
                        skillAbility.changeTitles(new String[]{
                                "Attack",
                                "Defense",
                                "Stamina",
                                "Speed",
                                "Shoot",
                                "Jump"
                        });
                        skillAbility.setData(data);

                    } else {

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
                    //infoCallback.onError(R.string.checkingNetwork);
                    return;
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    //infoCallback.onError(R.string.server_error);
                    return;
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    //infoCallback.onError(R.string.connection_error);
                    return;
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    //infoCallback.onError(R.string.parsing_error);
                    return;
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    //infoCallback.onError(R.string.connection_error);
                    return;
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    //infoCallback.onError(R.string.time_out);
                    return;
                }
                //infoCallback.onError(R.string.time_out);
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("method", "endorse");
                params.put("from_id", userId);
                params.put("to_id", userId);
                params.put("attack", userId);
                params.put("stamina", userId);
                params.put("defense", userId);
                params.put("speed", userId);
                params.put("ball_control", userId);
                params.put("low_pass", userId);
                params.put("shoot_accuracy", userId);
                params.put("shoot_power", userId);
                params.put("free_kicks", userId);
                params.put("header", userId);
                params.put("jump", userId);
                params.put("lofted_pass", userId);
                return params;
            }
        };
        RezetopiaApp.getInstance().getRequestQueue().add(post);
    }
}
