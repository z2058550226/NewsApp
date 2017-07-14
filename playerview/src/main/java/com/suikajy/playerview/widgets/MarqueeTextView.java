package com.suikajy.playerview.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by suikajy on 2017/1/14.
 * 跑马灯TextView
 */
public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
