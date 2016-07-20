package com.equaleyes.injectordemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equaleyes.injector.InjectGroup;

import java.util.List;

/**
 * Created by zan on 20/07/16.
 */
public class ItemsArrayView extends RelativeLayout {

    @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
    private View[] mTextViewsArray;

    @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
    private List<View> mTextViewsList;

    @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
    private TextView[] mTypedArray;

    @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
    private List<TextView> mTypedList;

    public ItemsArrayView(Context context) {
        this(context, null);
    }

    public ItemsArrayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemsArrayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.item_array_view, this, true);
    }
}
