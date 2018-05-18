package io.krito.com.reze.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Walaa on 3/2/2015.
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    public CustomEditText(Context context) {
        super(context);
        if(!this.isInEditMode()){
            Typeface face= Typeface.createFromAsset(context.getAssets(), "CoconNextArabic-Regular.otf");
            this.setTypeface(face);
        }

    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!this.isInEditMode()) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "CoconNextArabic-Regular.otf");
            this.setTypeface(face);
        }
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
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
