package com.equaleyes.injectordemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equaleyes.injector.Inject;

/**
 * Created by zan on 7/4/16.
 */
public class AndroidView extends RelativeLayout {

    @Inject private TextView text1;

    public AndroidView(Context context) {
        this(context, null);
    }

    public AndroidView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AndroidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, this, true);
    }
}
