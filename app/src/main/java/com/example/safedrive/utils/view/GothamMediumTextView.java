package com.example.safedrive.utils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.safedrive.utils.FontUtils;


public class GothamMediumTextView extends TextView {

    Typeface mGothamBlackTypeface;

    public GothamMediumTextView(Context context,
                                AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GothamMediumTextView(Context context,
                                AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GothamMediumTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            mGothamBlackTypeface = FontUtils.loadTypeFace(getContext(), FontUtils.Gotham_Medium);
            setTypeface(mGothamBlackTypeface);
        }
    }
}
