package com.equaleyes.injectordemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

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
public class InjectorTestView {
    private CustomView mCustomView;

    @Before
    public void setUp() {
        Activity activity = Robolectric.setupActivity(Activity.class);

        // Inflate the file containing the custom view
        mCustomView = (CustomView) LayoutInflater
                .from(activity)
                .inflate(R.layout.custom_view, null);

        // Make sure it's not null
        assertNotNull(mCustomView);
    }

    @Test
    public void testInjectView() throws Exception {
        // Inject the view
        Injector.inject(mCustomView);

        // Get the declared fields
        Field[] fields = mCustomView.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Ignore non-annotated fields
            if (!field.isAnnotationPresent(Inject.class))
                continue;

            // Make the field accessible and get its value
            field.setAccessible(true);
            Object value = field.get(mCustomView);

            // Only this field should fail to inject, resulting in null value
            if ("mTypeMismatch".equals(field.getName()))
                assertNull(value);
            else
                assertNotNull(value); // The remaining ones should be injected
        }
    }

    @Test
    public void testInjectViewWithListener() throws Exception {
        // Dummy listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };

        // Inject with OnClickListener
        Injector.inject(mCustomView, listener);

        // Get the expected clickable items
        Class clazz = mCustomView.getClass();

        Field buttonField = clazz.getDeclaredField("mMyButton");
        buttonField.setAccessible(true);
        Button theButton = (Button) buttonField.get(mCustomView);

        Field viewField = clazz.getDeclaredField("mClickableView");
        viewField.setAccessible(true);
        View theView = (View) viewField.get(mCustomView);

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
