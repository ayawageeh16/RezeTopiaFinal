package io.krito.com.rezetopia.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnimBarBuilder;
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
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.philjay.valuebar.ValueBar;
import com.philjay.valuebar.colors.GreenToRedFormatter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class AboutProfile extends AppCompatActivity {

    private static final String USER_ID_EXTRA = "user_id";

    String userId;

    AbilityChatView skillAbility;
    ValueBar valueBar;
    ValueBar valueBar1;
    ValueBar valueBar2;
    ValueBar valueBar3;
    ValueBar valueBar4;
    ValueBar valueBar5;
    ValueBar valueBar6;
    ValueBar valueBar7;
    ValueBar valueBar8;
    ValueBar valueBar9;
    ValueBar valueBar10;
    ValueBar valueBar11;
    ExpandableRelativeLayout expandableLayout;
    Button btnToggle;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    ArrayList<Integer> skills;

    public static Intent createIntent(Context context, String id){
        Intent intent = new Intent(context, AboutProfile.class);
        intent.putExtra(id, USER_ID_EXTRA);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_profile);

        networkListener();

        userId = getIntent().getExtras().getString("id");
        Log.i(USER_ID_EXTRA, "onCreate: " + userId);

        progressBar = findViewById(R.id.about_progress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        relativeLayout = findViewById(R.id.aboutHeader);

        getSkills();
    }

    private void getSkills(){
        StringRequest post = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/get_skills.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response == null){
                    onEmptyRespond();
                }

                Log.i("getSkills", "onResponse: " + response);
                try {
                    JSONObject json = new JSONObject(response);
                    if (!json.getBoolean("error")){
                        List<Double> data = new ArrayList<>();
                        skills = new ArrayList<>();
                        JSONObject jsonObject = json.getJSONObject("user");
                        data.add((double) jsonObject.getInt("attack"));
                        skills.add((jsonObject.getInt("attack")));
                        data.add((double) jsonObject.getInt("defence"));
                        skills.add((jsonObject.getInt("defence")));
                        data.add((double) jsonObject.getInt("stamina"));
                        skills.add((jsonObject.getInt("stamina")));
                        data.add((double) jsonObject.getInt("speed"));
                        skills.add((jsonObject.getInt("speed")));
                        //data.add((double) jsonObject.getInt("ball_control"));
                        skills.add((jsonObject.getInt("ball_control")));
                        //data.add((double) jsonObject.getInt("low_pass"));
                        skills.add((jsonObject.getInt("low_pass")));
                        //data.add((double) jsonObject.getInt("lofted_pass"));
                        skills.add((jsonObject.getInt("lofted_pass")));
                        //data.add((double) jsonObject.getInt("shoot_accuracy"));
                        skills.add((jsonObject.getInt("shoot_accuracy")));
                        data.add((double) jsonObject.getInt("shoot_power"));
                        skills.add((jsonObject.getInt("shoot_power")));
                        //data.add((double) jsonObject.getInt("free_kicks"));
                        skills.add((jsonObject.getInt("free_kicks")));
                        //data.add((double) jsonObject.getInt("header"));
                        skills.add((jsonObject.getInt("header")));
                        data.add((double) jsonObject.getInt("jump"));
                        skills.add((jsonObject.getInt("jump")));

                        skillAbility = findViewById(R.id.skillAbility);
                        progressBar.setVisibility(View.GONE);
                        skillAbility.setVisibility(View.VISIBLE);
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
                        initExpandLayout();

                    } else {
                        onEmptyRespond();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onEmptyRespond();
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

    private void initExpandLayout(){
        expandableLayout = findViewById(R.id.expandableLayout);
        btnToggle = findViewById(R.id.btnToggle);
        btnToggle.setVisibility(View.VISIBLE);
        valueBar = findViewById(R.id.valueBar);
        valueBar1 = findViewById(R.id.valueBar1);
        valueBar2 = findViewById(R.id.valueBar2);
        valueBar3 = findViewById(R.id.valueBar3);
        valueBar4 = findViewById(R.id.valueBar4);
        valueBar5 = findViewById(R.id.valueBar5);
        valueBar6 = findViewById(R.id.valueBar6);
        valueBar7 = findViewById(R.id.valueBar7);
        valueBar8 = findViewById(R.id.valueBar8);
        valueBar9 = findViewById(R.id.valueBar9);
        valueBar10 = findViewById(R.id.valueBar10);
        valueBar11 = findViewById(R.id.valueBar11);

        PushDownAnim.setPushDownAnimTo(btnToggle)
                .setScale( MODE_SCALE, 0.89f  )
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.setVisibility(View.VISIBLE);
                expandableLayout.toggle();
                if (expandableLayout.isExpanded()){
                    btnToggle.setText(R.string.less);
                } else {
                    btnToggle.setText(R.string.more);
                }
                animateValueBar();
            }
        });


        initValueBar(valueBar, skills.get(0));
        initValueBar(valueBar1, skills.get(1));
        initValueBar(valueBar2,skills.get(2));
        initValueBar(valueBar3, skills.get(3));
        initValueBar(valueBar4, skills.get(4));
        initValueBar(valueBar5, skills.get(5));
        initValueBar(valueBar6, skills.get(6));
        initValueBar(valueBar7, skills.get(7));
        initValueBar(valueBar8, skills.get(8));
        initValueBar(valueBar9, skills.get(9));
        initValueBar(valueBar10, skills.get(10));
        initValueBar(valueBar11, skills.get(11));
    }

    private void initValueBar(ValueBar bar, int value){
        bar.setEnabled(false);

        bar.setMinMax(0, 100);
        bar.setInterval(1f); // interval in which can be selected
        bar.setDrawBorder(false);
        bar.setValueTextSize(14f);
        bar.setMinMaxTextSize(14f);
        bar.setValueTextTypeface(Typeface.createFromAsset(getAssets(), "CoconNextArabic-Regular.otf"));
        bar.setMinMaxTextTypeface(Typeface.createFromAsset(getAssets(), "CoconNextArabic-Regular.otf"));

        bar.setColorFormatter(new GreenToRedFormatter());
        float v = value;
        bar.setValue(v);
        bar.setTouchEnabled(false);
    }

    private void animateValueBar(){
        valueBar.animate(1f, skills.get(0), 500);
        valueBar1.animate(1f, skills.get(1), 500);
        valueBar2.animate(1f, skills.get(2), 500);
        valueBar3.animate(1f, skills.get(3), 500);
        valueBar4.animate(1f, skills.get(4), 500);
        valueBar5.animate(1f, skills.get(5), 500);
        valueBar6.animate(1f, skills.get(6), 500);
        valueBar7.animate(1f, skills.get(7), 500);
        valueBar8.animate(1f, skills.get(8), 500);
        valueBar9.animate(1f, skills.get(9), 500);
        valueBar10.animate(1f, skills.get(10), 500);
        valueBar11.animate(1f, skills.get(11), 500);
    }

    private void onEmptyRespond(){
        progressBar.setVisibility(View.GONE);
        String username = getIntent().getExtras().getString("name");
        String message = getResources().getString(R.string.empty_skills);
        Flashbar.Builder builder = new Flashbar.Builder(AboutProfile.this);
        builder.gravity(Flashbar.Gravity.BOTTOM)
                .message(username + " " + message)
                .title("Empty Skills")
                .showOverlay()
                .build().show();
        //onBackPressed();
        /*relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);*/
    }

    private void networkListener(){
        ReactiveNetwork.observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if (connectivity.getState() == NetworkInfo.State.CONNECTED){
                        Log.i("internetC", "onNext: " + "Connected");
                    } else if (connectivity.getState() == NetworkInfo.State.SUSPENDED){
                        Log.i("internetC", "onNext: " + "LowNetwork");
                    } else {
                        Log.i("internetC", "onNext: " + "NoInternet");
                        Flashbar.Builder builder = new Flashbar.Builder(this);
                        builder.gravity(Flashbar.Gravity.BOTTOM)
                                .backgroundColor(R.color.red2)
                                .enableSwipeToDismiss()
                                .message(R.string.checkingNetwork)
                                .enterAnimation(new FlashAnimBarBuilder(AboutProfile.this).slideFromRight().duration(200))
                                .build().show();
                    }
                });
    }

}
