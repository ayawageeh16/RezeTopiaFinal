package io.krito.com.rezetopia.activities;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnimBarBuilder;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import io.krito.com.rezetopia.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImageViewer extends AppCompatActivity {

    public static final int PP_MODE = 0;
    public static final int COVER_MODE = 1;
    public static final String URL_EXTRA = "url_extra";
    public static final String MODE_EXTRA = "mode_extra";

    ZoomageView ppView;
    ZoomageView coverView;

    public static Intent createIntent(String url, int mode, Context context){
        Intent intent = new Intent(context, ImageViewer.class);
        intent.putExtra(URL_EXTRA, url);
        intent.putExtra(MODE_EXTRA, mode);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        networkListener();

        ppView = findViewById(R.id.ppImage);
        coverView = findViewById(R.id.coverImage);

        if (getIntent().getExtras().getInt(MODE_EXTRA) == PP_MODE){
            ppView.setVisibility(View.VISIBLE);
            String pre = "https://rezetopia.dev-krito.com/images/profileImgs/";
            Picasso.with(this).load(pre + getIntent().getExtras().getString(URL_EXTRA) + ".JPG").into(ppView);
        } else {
            coverView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(getIntent().getExtras().getString(URL_EXTRA)).into(coverView);
        }
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
                                .enterAnimation(new FlashAnimBarBuilder(ImageViewer.this).slideFromRight().duration(200))
                                .build().show();
                    }
                });
    }
}
