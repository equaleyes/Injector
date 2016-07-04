package com.equaleyes.injectordemo;

import android.app.Activity;

import com.equaleyes.injector.Inject;
import com.equaleyes.injector.Injector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;

/**
 * Created by zan on 7/4/16.
 */
@Config(manifest = "src/main/AndroidManifest.xml", sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class InjectorTestRClass {
    private Activity mActivity;
    private AndroidView mView;

    @Before
    public void setUp() {
        mActivity = Robolectric.setupActivity(Activity.class);

        mView = new AndroidView(mActivity);
    }

    @Test
    public void testAndroidRClass() throws Exception {
        Injector.inject(mView);

        Field[] fields = mView.getClass().getDeclaredFields();

        for (Field field : fields) {
            // Ignore non-annotated fields
            if (!field.isAnnotationPresent(Inject.class))
                continue;

            // Make the field accessible and get its value
            field.setAccessible(true);
            Object value = field.get(mView);

            assertNotNull(value);
        }
    }
}
