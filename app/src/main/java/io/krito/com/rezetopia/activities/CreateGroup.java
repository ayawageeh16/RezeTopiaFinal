package io.krito.com.rezetopia.activities;

import android.app.DownloadManager;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.helper.MainPagerAdapter;

public class CreateGroup extends AppCompatActivity implements View.OnClickListener {

    EditText name;
    EditText userName;
    EditText about;
    RadioButton open;
    RadioButton closed;
    RadioButton secret;
    Button createGroupButton;
    TabLayout tabLayout;
    FloatingActionButton fab;
    ViewPager viewPager;
    View requestView;
    View notificationView;
    private MainPagerAdapter adapter;
    private StringRequest stringRequest;
    private String BACK_END = "https://rezetopia.com/Apis/groups/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        name = findViewById(R.id.group_name_et);
        userName = findViewById(R.id.userName_et);
        about = findViewById(R.id.about_et);
        open = findViewById(R.id.open_group);
        closed = findViewById(R.id.closed_group);
        secret = findViewById(R.id.secret_group);
        createGroupButton = findViewById(R.id.create_group_btn);
        tabLayout = findViewById(R.id.tablayout);
        fab = findViewById(R.id.fab);
        viewPager = findViewById(R.id.pager);

        createGroupButton.setOnClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == createGroupButton) {
            sendGroupData();
        }
    }

    private void sendGroupData() {

        stringRequest = new StringRequest(Request.Method.POST, BACK_END, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CreateGroup.this, response, Toast.LENGTH_LONG).show();
                Log.i("request response", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateGroup.this, "failed", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String group_name = name.getText().toString();
                String user_name = userName.getText().toString();
                String about_group = about.getText().toString();

                params.put("name", group_name);
                params.put("username", user_name);
                params.put("about", about_group);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
