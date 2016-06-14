package com.equaleyes.injectordemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equaleyes.injector.Inject;

/**
 * Created by zan on 12.6.2016.
 */
public class CustomView extends RelativeLayout {

    @Inject private RelativeLayout mContentView;
    @Inject private TextView mMyTextView;
    @Inject private ImageView litteralId;
    @Inject private LinearLayout mAllLowerCase;
    @Inject private RelativeLayout mLowercaseLeadingM;
    @Inject private TextView mTypeMismatch;
    @Inject(id = R.id.special_id) private TextView mTextView;
    @Inject private Button mMyButton;
    @Inject private View mClickableView;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_demo, this, true);
    }
}
