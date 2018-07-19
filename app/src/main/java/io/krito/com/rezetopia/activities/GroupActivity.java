package io.krito.com.rezetopia.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.fragments.AddFriendFragment;
import io.krito.com.rezetopia.fragments.DescriptionFragment;
import io.krito.com.rezetopia.fragments.GroupHomeFragment;
import io.krito.com.rezetopia.helper.GroupPagerAdapter;

public class GroupActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    GroupPagerAdapter groupPagerAdapter ;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("groupId");
        }

        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.pager);
        groupPagerAdapter = new GroupPagerAdapter(getSupportFragmentManager());

        groupPagerAdapter = new GroupPagerAdapter(getSupportFragmentManager());
        groupPagerAdapter.addFrag(new GroupHomeFragment(), "Home");
        groupPagerAdapter.addFrag(new DescriptionFragment(), "Description");
        groupPagerAdapter.addFrag(new AddFriendFragment(), "Add member");
        viewPager.setAdapter(groupPagerAdapter);
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


    }
}
