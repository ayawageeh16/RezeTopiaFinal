package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.fragments.AddFriendFragment;
import io.krito.com.rezetopia.fragments.DescriptionFragment;
import io.krito.com.rezetopia.fragments.GroupHomeFragment;
import io.krito.com.rezetopia.helper.Group.GroupPagerAdapter;
import io.krito.com.rezetopia.models.pojo.Group.GroupPost;

public class Group extends AppCompatActivity implements View.OnClickListener{

    TextView group_name;
    TextView group_privacy;
    TextView group_members;
    ImageView coverImage;
    ImageView settingsImage;
    TabLayout tabLayout;
    ViewPager viewPager;
    RecyclerView recyclerView;

    private GroupPagerAdapter adapter;
    private String BACK_END = "https://rezetopia.com/Apis/groups/get";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private int groupId;
    private Gson gson;
    private GroupPost groupPost ;
    private String id ;
    private String cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        group_name = findViewById(R.id.group_name_tv);
        group_privacy = findViewById(R.id.group_privacy_tv);
        group_members = findViewById(R.id.group_members_tv);
        coverImage = findViewById(R.id.cover_image);
        settingsImage = findViewById(R.id.settings_icon);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.pager);
        settingsImage.setOnClickListener(this);

        //Tablayout and ViewPagger
        adapter = new GroupPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new GroupHomeFragment(), "Home");
        adapter.addFrag(new DescriptionFragment(), "Description");
        adapter.addFrag(new AddFriendFragment(), "Add member");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Volley Request and Gson
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        groupPost = new GroupPost();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("groupId");
        }
        id = String.valueOf(85);
        cursor=String.valueOf(0);
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
                Toast.makeText(Group.this, error.toString(), Toast.LENGTH_LONG).show();
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
        group_name.setText(groupPost.getGroupName());
        group_members.setText(String.valueOf(groupPost.getMembersNumber())+ " " + getString(R.string.member));
        group_privacy.setText(groupPost.getPrivacy());
        if (!groupPost.getCover().isEmpty()){
            Picasso.with(Group.this).load(groupPost.getCover()).into(coverImage);
        }
        settingsImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == settingsImage){
            Intent i = new Intent( Group.this, GroupSettings.class);
            i.putExtra("groupName",groupPost.getGroupName());
            i.putExtra("membersNumber", groupPost.getMembersNumber());
            i.putExtra("groupPrivacy", groupPost.getPrivacy());
            startActivity(i);
        }
    }
}
