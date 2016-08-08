package com.equaleyes.injectordemo;

import android.graphics.drawable.Drawable;

import com.equaleyes.injector.InjectRes;
import com.equaleyes.injector.Injector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by zan on 8.8.2016.
 */
@Config(manifest = "src/main/AndroidManifest.xml", sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class InjectorTestResource {

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
            // Ignore fields without @InjectRes annotation
            if (!field.isAnnotationPresent(InjectRes.class)) {
                continue;
            }

            // Get the field value
            field.setAccessible(true);
            Object value = field.get(mActivity);

            // Make sure it's not null
            assertNotNull(value);

            // If the value is a primitive 0 is the default value.
            if (field.getType().isPrimitive()) {
                assertTrue((int)value != 0);
            }
        }
    }

    @Test
    public void testContextInjection() throws Exception {
        // Resource holder
        class Holder {
            @InjectRes private Drawable mDrawable;
            @InjectRes private String mAppName;
            @InjectRes private int mColorPrimary;

            @InjectRes(R.mipmap.ic_launcher) private Drawable icon;
            @InjectRes(R.string.app_name) private String name;
            @InjectRes(R.color.colorPrimary) private int color;
        }

        // Create an instance
        Holder holder = new Holder();

        // Inject the holder
        Injector.injectTo(holder, mActivity);

        // Get the declared fields
        Field[] fields = holder.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Ignore non-annotated fields
            if (!field.isAnnotationPresent(InjectRes.class))
                continue;

            // Get the field value
            field.setAccessible(true);
            Object value = field.get(holder);

            // Make sure it's not null
            assertNotNull(value);

            // If the value is a primitive 0 is the default value.
            if (field.getType().isPrimitive()) {
                assertTrue((int)value != 0);
            }
        }
    }
}
