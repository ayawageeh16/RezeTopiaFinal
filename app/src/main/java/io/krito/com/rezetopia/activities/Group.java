package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.fragments.GroupDescriptionFragment;
import io.krito.com.rezetopia.fragments.GroupHomeFragment;
import io.krito.com.rezetopia.fragments.GroupReportFragment;
import io.krito.com.rezetopia.helper.GroupPagerAdapter;

public class Group extends AppCompatActivity {

    TextView group_name;
    TextView group_privacy;
    TextView group_members;
    TabLayout tabLayout;
    ViewPager viewPager;
    RecyclerView recyclerView ;
    private int groupId;
    private GroupPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        group_name = findViewById(R.id.group_name_tv);
        group_privacy = findViewById(R.id.group_privacy_tv);
        group_members= findViewById(R.id.group_members_tv);
        tabLayout = findViewById(R.id.tablayout);
        viewPager= findViewById(R.id.pager);
       // addTabs(viewPager);
        adapter = new GroupPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new GroupHomeFragment(), "Home");
        adapter.addFrag(new GroupDescriptionFragment(), "Description");
        adapter.addFrag(new GroupReportFragment(), "Report");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("groupId");
        }
    }

}
