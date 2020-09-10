package com.nemosofts.library;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by thivakaran
 */

public class TextView extends AppCompatTextView {

    public TextView(Context context) {
        super(context);
        init(context);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context == null) return;
    }
}
