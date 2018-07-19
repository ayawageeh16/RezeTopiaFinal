package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.fragments.AddFriendFragment;
import io.krito.com.rezetopia.fragments.DescriptionFragment;
import io.krito.com.rezetopia.fragments.GroupHomeFragment;
import io.krito.com.rezetopia.helper.GroupPagerAdapter;

public class Group extends AppCompatActivity {

    TextView group_name;
    TextView group_privacy;
    TextView group_members;
    ImageView imageView;

    TabLayout tabLayout;
    ViewPager viewPager;

    RecyclerView recyclerView;
    private int groupId;
    private GroupPagerAdapter adapter;
    private String BACK_END = "https://rezetopia.com/Apis/groups/get";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        group_name = findViewById(R.id.group_name_tv);
        group_privacy = findViewById(R.id.group_privacy_tv);
        group_members = findViewById(R.id.group_members_tv);
        imageView = findViewById(R.id.cover_image);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.pager);
        requestQueue = Volley.newRequestQueue(this);

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("groupId");
        }
        getData();
    }

    private void getData() {
        stringRequest = new StringRequest(Request.Method.POST, BACK_END, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(Group.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(CreateGroup.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.i("request response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String id = String.valueOf(groupId);

                params.put("id", id);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
