package com.example.safedrive.utils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.safedrive.utils.FontUtils;


public class GothamBlackTextView extends TextView {

    Typeface mGothamBlackTypeface;

    public GothamBlackTextView(Context context,
                              AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GothamBlackTextView(Context context,
                              AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GothamBlackTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            mGothamBlackTypeface = FontUtils.loadTypeFace(getContext(), FontUtils.Gotham_Black);
            setTypeface(mGothamBlackTypeface);
        }
    }
}
