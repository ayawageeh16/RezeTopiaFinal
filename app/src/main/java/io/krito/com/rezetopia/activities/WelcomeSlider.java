package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.krito.com.rezetopia.R;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class WelcomeSlider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_slider);

        FlipperLayout flipperLayout = findViewById(R.id.flipper_layout);
        int num_of_pages = 3;
        for (int i = 0; i < num_of_pages; i++) {
            FlipperView view = new FlipperView(getBaseContext());
            view.setImageDrawable(R.drawable.rezetopia)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setDescription("Description");


            flipperLayout.setScrollTimeInSec(1); //setting up scroll time, by default it's 3 seconds
            //flipperLayout.getScrollTimeInSec(); //returns the scroll time in sec
            //flipperLayout.getCurrentPagePosition(); //returns the current position of pager
            flipperLayout.addFlipperView(view);
        }

        TextView textView = findViewById(R.id.welcomeSkip);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeSlider.this, Main.class));
                finish();
            }
        });

    }
}
