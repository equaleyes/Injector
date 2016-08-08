package com.equaleyes.injectordemo;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equaleyes.injector.Inject;
import com.equaleyes.injector.Injector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by zan on 12.6.2016.
 */
@Config(manifest = "src/main/AndroidManifest.xml", sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class InjectorTestActivity {

    private DemoActivity mActivity;

    @Before
    public void setUp() {
        // Get the activity
        mActivity = Robolectric.setupActivity(DemoActivity.class);

        // Make sure it's not null
        assertNotNull(mActivity);
    }

    @Test
    public void testInjection() throws Exception {
        // Perform regular injection
        Injector.inject(mActivity);

        // Get the declared fields
        Field[] fields = mActivity.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Ignore non-annotated fields
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            // Make the field accessible and get its value
            field.setAccessible(true);
            Object value = field.get(mActivity);

            // Only this field should fail to inject, resulting in null value
            if ("mTypeMismatch".equals(field.getName()))
                assertNull(value);
            else
                assertNotNull(value); // The remaining ones should be injected
        }
    }

    @Test
    public void testInjectionWithListener() throws Exception {
        // Dummy listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };

        // Inject with OnClickListener
        Injector.inject(mActivity, listener);

        // Get the expected clickable items
        Class clazz = mActivity.getClass();

        Field buttonField = clazz.getDeclaredField("mMyButton");
        buttonField.setAccessible(true);
        Button theButton = (Button) buttonField.get(mActivity);

        Field viewField = clazz.getDeclaredField("mClickableView");
        viewField.setAccessible(true);
        View theView = (View) viewField.get(mActivity);

        // Make sure they are not null
        assertNotNull(theButton);
        assertNotNull(theView);

        // Get the click listener fields
        Field listenerField =
                Class.forName("android.view.View").getDeclaredField("mListenerInfo");
        listenerField.setAccessible(true);

        Field clickListenerField =
                Class.forName("android.view.View$ListenerInfo").getField("mOnClickListener");
        clickListenerField.setAccessible(true);

        Object listenerInfo;
        View.OnClickListener retrievedListener;

        // Test the Button
        listenerInfo = listenerField.get(theButton);
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);

        // Test the regular view
        listenerInfo = listenerField.get(theView);
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);
    }

    @Test
    public void testInjectViewHolder() throws Exception {
        // Declare the class containing the views
        class ViewHolder {
            @Inject private RelativeLayout mContentView;
            @Inject private TextView mMyTextView;
            @Inject private ImageView litteralId;
            @Inject private LinearLayout mAllLowerCase;
            @Inject private RelativeLayout mLowercaseLeadingM;
            @Inject private TextView mTypeMismatch;
            @Inject(R.id.special_id) private TextView mTextView;
            @Inject private Button mMyButton;
            @Inject private View mClickableView;
        }

        // Create the holder instance
        ViewHolder holder = new ViewHolder();

        // Inject the view holder
        Injector.injectToFrom(holder, mActivity.findViewById(android.R.id.content));

        // Get the declared fields
        Field[] fields = holder.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Ignore non-annotated fields
            if (!field.isAnnotationPresent(Inject.class))
                continue;

            // Make the field accessible and get its value
            field.setAccessible(true);
            Object value = field.get(holder);

            // Only this field should fail to inject, resulting in null value
            if ("mTypeMismatch".equals(field.getName()))
                assertNull(value);
            else
                assertNotNull(value); // The remaining ones should be injected
        }
    }

    @Test
    public void testInjectViewHolderWithListener() throws Exception {
        // Declare the class containing the views
        class ViewHolder {
            @Inject private Button mMyButton;
            @Inject private View mClickableView;
        }

        // Dummy listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };

        // Create the holder instance
        ViewHolder holder = new ViewHolder();

        // Inject the view holder
        Injector.injectToFrom(holder, mActivity.findViewById(android.R.id.content), listener);

        // Get the expected clickable items
        Class clazz = holder.getClass();

        Field buttonField = clazz.getDeclaredField("mMyButton");
        buttonField.setAccessible(true);
        Button theButton = (Button) buttonField.get(holder);

        Field viewField = clazz.getDeclaredField("mClickableView");
        viewField.setAccessible(true);
        View theView = (View) viewField.get(holder);

        // Make sure they are not null
        assertNotNull(theButton);
        assertNotNull(theView);

        // Get the click listener fields
        Field listenerField =
                Class.forName("android.view.View").getDeclaredField("mListenerInfo");
        listenerField.setAccessible(true);

        Field clickListenerField =
                Class.forName("android.view.View$ListenerInfo").getField("mOnClickListener");
        clickListenerField.setAccessible(true);

        Object listenerInfo;
        View.OnClickListener retrievedListener;

        // Test the Button
        listenerInfo = listenerField.get(theButton);
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);

        // Test the regular view
        listenerInfo = listenerField.get(theView);
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);
    }
}
