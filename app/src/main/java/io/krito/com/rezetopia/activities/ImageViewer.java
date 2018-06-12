package io.krito.com.rezetopia.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import io.krito.com.rezetopia.R;

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
}
