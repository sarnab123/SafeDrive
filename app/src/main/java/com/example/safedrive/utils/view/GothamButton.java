package com.example.safedrive.utils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.safedrive.utils.FontUtils;


public class GothamButton extends Button {
	Typeface mGothamBoldTypeface;

	public GothamButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public GothamButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GothamButton(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (!isInEditMode()) {
			mGothamBoldTypeface = FontUtils.loadTypeFace(getContext(), FontUtils.Gotham_Book);
			setTypeface(mGothamBoldTypeface);
		}
	}
}
