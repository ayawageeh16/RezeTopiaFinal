package io.krito.com.reze.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import io.krito.com.reze.R;
import io.krito.com.reze.helper.MainPagerAdapter;

public class Main extends AppCompatActivity {

    View requestView;
    View notificationView;
    EditText searchBox;
    ImageButton searchIcon;
    ImageView backView;

    TabLayout tabLayout;
    ViewPager viewPager;
    MainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        configureHeaderLayout();
    }

    private void configureHeaderLayout(){

        requestView = LayoutInflater.from(this).inflate(R.layout.request_tab_icon, null);
        notificationView = LayoutInflater.from(this).inflate(R.layout.notification_tab_icon, null);

        tabLayout = findViewById(R.id.tablayout);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_tab));
        tabLayout.addTab(tabLayout.newTab().setCustomView(notificationView));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_store));
        tabLayout.addTab(tabLayout.newTab().setCustomView(requestView));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_side_menu));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).getIcon().
                setColorFilter(ContextCompat.getColor(Main.this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        viewPager = findViewById(R.id.pager);
        adapter = new MainPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(Main.this, R.color.colorPrimaryDark);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                viewPager.setCurrentItem(tab.getPosition());
                tab.select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(Main.this, R.color.tabs);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
