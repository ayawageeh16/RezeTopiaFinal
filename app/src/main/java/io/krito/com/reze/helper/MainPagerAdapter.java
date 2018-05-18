package io.krito.com.reze.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.krito.com.reze.fragments.FriendRequest;
import io.krito.com.reze.fragments.Home;
import io.krito.com.reze.fragments.Notification;
import io.krito.com.reze.fragments.SideMenu;
import io.krito.com.reze.fragments.Store;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public MainPagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                Home home = new Home();
                return home;
            case 1:
                Notification notification = new Notification();
                return notification;
            case 2:
                Store store = new Store();
                return store;
            case 3:
                FriendRequest requests = new FriendRequest();
                return requests;
            case 4:
                SideMenu sideMenu = new SideMenu();
                return sideMenu;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}

