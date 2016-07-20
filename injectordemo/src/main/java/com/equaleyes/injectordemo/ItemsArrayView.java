package com.equaleyes.injectordemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.equaleyes.injector.InjectList;

/**
 * Created by zan on 20/07/16.
 */
public class ItemsArrayView extends RelativeLayout {

    @InjectList()
    private View[] mTextViews;

    public ItemsArrayView(Context context) {
        this(context, null);
    }

    public ItemsArrayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemsArrayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, this, true);
    }
}
