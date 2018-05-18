package io.krito.com.reze.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Walaa on 3/2/2015.
 */
public class CustomButton extends android.support.v7.widget.AppCompatButton {
    public CustomButton(Context context) {
        super(context);

        Typeface face= Typeface.createFromAsset(context.getAssets(), "CoconNextArabic-Regular.otf");
        this.setTypeface(face);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!this.isInEditMode()) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "CoconNextArabic-Regular.otf");
            this.setTypeface(face);
        }
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!this.isInEditMode()) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "CoconNextArabic-Regular.otf");
            this.setTypeface(face);
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}
