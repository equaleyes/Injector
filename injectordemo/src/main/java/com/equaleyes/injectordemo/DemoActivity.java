package com.equaleyes.injectordemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equaleyes.injector.Inject;
import com.equaleyes.injector.Injector;

public class DemoActivity extends Activity {

    @Inject private RelativeLayout mContentView;
    @Inject private TextView mMyTextView;
    @Inject private ImageView litteralId;
    @Inject private LinearLayout mAllLowerCase;
    @Inject private RelativeLayout mLowercaseLeadingM;
    @Inject private TextView mTypeMismatch;
    @Inject(id = R.id.special_id) private TextView mTextView;
    @Inject private Button mMyButton;
    @Inject private View mClickableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }
}
