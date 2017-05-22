
package com.example.safedrive.utils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.safedrive.utils.FontUtils;


public class GothamBoldTextView extends TextView {
    Typeface mGothamBoldTypeface;

    public GothamBoldTextView(Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GothamBoldTextView(Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GothamBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            mGothamBoldTypeface = FontUtils.loadTypeFace(getContext(), FontUtils.Gotham_Bold);
            setTypeface(mGothamBoldTypeface);
        }
    }
}
