package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnimBarBuilder;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.fragments.Home;
import io.krito.com.rezetopia.helper.MainPagerAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Main extends AppCompatActivity implements Home.HomeCallback {


    View requestView;
    View notificationView;
    EditText searchBox;
    ImageButton searchIcon;
    ImageView backView;
    FloatingActionButton fab;

    TabLayout tabLayout;
    ViewPager viewPager;
    MainPagerAdapter adapter;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkListener();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        if (userId == null || userId.isEmpty()){
            startActivity(new Intent(this, Login.class));
            finish();
        }

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
                if (tab.getIcon() != null) {
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                } else {
                    View customBackground = tab.getCustomView();
                }
                viewPager.setCurrentItem(tab.getPosition());
                tab.select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(Main.this, R.color.tabs);
                if (tab.getIcon() != null) {
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                } else {
                    View customBackground = tab.getCustomView();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onScroll(boolean show) {
        if (show){
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                                .enterAnimation(new FlashAnimBarBuilder(Main.this).slideFromRight().duration(200))
                                .build().show();
                    }
                });
    }
}
