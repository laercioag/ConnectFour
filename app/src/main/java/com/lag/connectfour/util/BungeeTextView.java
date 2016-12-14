package com.lag.connectfour.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BungeeTextView extends TextView {

    public BungeeTextView(Context context) {
        super(context);
        init();
    }

    public BungeeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BungeeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Bungee-Regular.ttf");
        setTypeface(typeface);

    }

}
