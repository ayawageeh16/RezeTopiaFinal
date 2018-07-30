package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.helper.Group.GroupPostsAdapter;
import io.krito.com.rezetopia.models.pojo.Group.GroupPost;

public class Group extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.group_name_tv)
    TextView group_name;
    @BindView(R.id.group_privacy_tv)
    TextView group_privacy;
    @BindView(R.id.group_members_tv)
    TextView group_members;
    @BindView(R.id.cover_image)
    ImageView coverImage;
    @BindView(R.id.settings_icon)
    ImageView settingsImage;
    @BindView(R.id.home_btn)
    Button home_btn;
    @BindView(R.id.description_btn)
    Button description_btn;
    @BindView(R.id.add_friend_btn)
    Button add_btn;
    @BindView(R.id.homeRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.relative)
    RelativeLayout relativeLayout;
    @BindView(R.id.error_tv)
    TextView error_tv;
    @BindView(R.id.group_img)
    ImageView ppImage;

    private String BACK_END = "https://rezetopia.com/Apis/groups/get";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Gson gson;
    private int groupId;
    private GroupPost groupPost ;
    private String id ;
    private String cursor = "0";
    private GroupPostsAdapter groupPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);


        //Setting on click listener
        settingsImage.setOnClickListener(this);
        home_btn.setOnClickListener(this);
        description_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("groupId");
        }
        id = String.valueOf(30);

        // Volley Request and Gson
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        groupPost = new GroupPost();
        getData();
    }

    public void getData() {
        stringRequest = new StringRequest(Request.Method.POST, BACK_END, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                groupPost = gson.fromJson(response, GroupPost.class);
                if (groupPost !=null){
                    setView();
                }else {
                    Toast.makeText(Group.this, "null object", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Snackbar.make(relativeLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       getData();
                                    }
                                }).show();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        Snackbar.make(relativeLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getData();
                                    }
                                }).show();
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Log.e("Error Status", status);
                        Log.e("Error Message", message);
                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                            Snackbar.make(relativeLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.retry, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            getData();
                                        }
                                    }).show();
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                            Toast.makeText(Group.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                            Toast.makeText(Group.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                            Snackbar.make(relativeLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.retry, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            getData();
                                        }
                                    }).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("cursor", cursor);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void setView() {
        group_name.setText(this.groupPost.getGroupName());
        group_members.setText(String.valueOf(this.groupPost.getMembersNumber())+ " " + getString(R.string.member));
        group_privacy.setText(this.groupPost.getPrivacy());
        if (!groupPost.getCover().isEmpty()){
            Picasso.with(Group.this).load(this.groupPost.getCover()).into(coverImage);
        }
        if (!groupPost.getPicture().isEmpty()){
            Picasso.with(Group.this).load(this.groupPost.getPicture()).into(ppImage);
        }
        settingsImage.setVisibility(View.VISIBLE);

          //Setting Posts Recyclerview
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        groupPostsAdapter = new GroupPostsAdapter(Group.this, groupPost.getPosts());
//        recyclerView.setAdapter(groupPostsAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v == settingsImage){
            Intent intent = new Intent( Group.this, GroupSettings.class);
            intent.putExtra("groupName",groupPost.getGroupName());
            intent.putExtra("membersNumber", groupPost.getMembersNumber());
            intent.putExtra("groupPrivacy", groupPost.getPrivacy());
            intent.putExtra("groupId",id);
            intent.putExtra("username", groupPost.getUsername());
            intent.putExtra("add_member", groupPost.getAddMember());
            intent.putExtra("add_post", groupPost.getAddPost());
            intent.putExtra("add_event", groupPost.getAddEvent());
            intent.putExtra("picture", groupPost.getPicture());
            intent.putExtra("cover", groupPost.getCover());
            intent.putExtra("description", groupPost.getGroupDecription());
            intent.putExtra("members",groupPost.getMembers());
            intent.putExtra("admins", groupPost.getAdmins());
            startActivity(intent);
        }else if (v == description_btn ){
            Intent intent= new Intent(this, GroupDescription.class);
            intent.putExtra("description",groupPost.getGroupDecription());
            intent.putExtra("groupName",groupPost.getGroupName());
            intent.putExtra("membersNumber", groupPost.getMembersNumber());
            intent.putExtra("groupPrivacy", groupPost.getPrivacy());
            intent.putExtra("groupId",id);
            startActivity(intent);
        }else if (v == add_btn){
            Intent intent= new Intent(this, GroupAddMember.class);
            intent.putExtra("description",groupPost.getGroupDecription());
            intent.putExtra("groupName",groupPost.getGroupName());
            intent.putExtra("membersNumber", groupPost.getMembersNumber());
            intent.putExtra("groupPrivacy", groupPost.getPrivacy());
            intent.putExtra("groupId",id);
            intent.putExtra("members", groupPost.getMembers());
            intent.putExtra("userId", groupPost.getPrivacy());
            startActivity(intent);
        }
    }
}
