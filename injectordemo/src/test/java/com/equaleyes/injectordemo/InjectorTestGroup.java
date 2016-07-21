package com.equaleyes.injectordemo;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.equaleyes.injector.InjectGroup;
import com.equaleyes.injector.Injector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by zan on 20/07/16.
 */
@Config(manifest = "src/main/AndroidManifest.xml", sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class InjectorTestGroup {
    private ItemsArrayView mView;

    @Before
    public void setUp() {
        Activity activity = Robolectric.setupActivity(Activity.class);

        mView = new ItemsArrayView(activity);

        assertNotNull(mView);
    }

    @Test
    public void testInjectArray() throws Exception {
        // Inject the view
        Injector.inject(mView);

        // Get the declared fields
        Field field = mView.getClass().getDeclaredField("mTextViewsArray");

        // Make it accessible
        field.setAccessible(true);
        Object value = field.get(mView);

        // Get the array of values
        View[] values = (View[]) value;

        assertNotNull(values);
        assertEquals(values.length, 3);
        assertEquals(values[0].getId(), R.id.text1);
        assertEquals(values[1].getId(), R.id.text2);
        assertEquals(values[2].getId(), R.id.text3);
    }

    @Test
    public void testInjectTypedArray() throws Exception {
        // Inject the view
        Injector.inject(mView);

        // Get the declared fields
        Field field = mView.getClass().getDeclaredField("mTypedArray");

        // Make it accessible
        field.setAccessible(true);
        Object value = field.get(mView);

        // Get the array of values
        View[] values = (View[]) value;

        assertNotNull(values);
        assertEquals(values.length, 3);
        assertEquals(values[0].getId(), R.id.text1);
        assertEquals(values[1].getId(), R.id.text2);
        assertEquals(values[2].getId(), R.id.text3);
    }

    @Test
    @SuppressWarnings("unchecked cast")
    public void testInjectList() throws Exception {
        // Inject the view
        Injector.inject(mView);

        // Get the declared fields
        Field field = mView.getClass().getDeclaredField("mTextViewsList");

        // Make it accessible
        field.setAccessible(true);
        Object value = field.get(mView);

        // Get the array of values
        View[] values = ((List<View>) value).toArray(new View[((List<View>) value).size()]);

        assertNotNull(values);
        assertEquals(values.length, 3);
        assertEquals(values[0].getId(), R.id.text1);
        assertEquals(values[1].getId(), R.id.text2);
        assertEquals(values[2].getId(), R.id.text3);
    }

    @Test
    @SuppressWarnings("unchecked cast")
    public void testInjectTypedList() throws Exception {
        // Inject the view
        Injector.inject(mView);

        // Get the declared fields
        Field field = mView.getClass().getDeclaredField("mTypedList");

        // Make it accessible
        field.setAccessible(true);
        Object value = field.get(mView);

        // Get the array of values
        List<TextView> list = (List<TextView>) value;
        TextView[] values = list.toArray(new TextView[list.size()]);

        assertNotNull(values);
        assertEquals(values.length, 3);
        assertEquals(values[0].getId(), R.id.text1);
        assertEquals(values[1].getId(), R.id.text2);
        assertEquals(values[2].getId(), R.id.text3);
    }

    @Test
    public void testInjectExternalArray() throws Exception {
        // Declare holder
        class ViewHolder {
            @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
            View[] views;
        }

        ViewHolder holder = new ViewHolder();
        Injector.injectToFrom(holder, mView);

        assertNotNull(holder.views);
        assertEquals(holder.views.length, 3);
        assertEquals(holder.views[0].getId(), R.id.text1);
        assertEquals(holder.views[1].getId(), R.id.text2);
        assertEquals(holder.views[2].getId(), R.id.text3);
    }

    @Test
    public void testInjectExternalList() throws Exception {
        // Declare holder
        class ViewHolder {
            @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
            List<View> views;
        }

        ViewHolder holder = new ViewHolder();
        Injector.injectToFrom(holder, mView);

        assertNotNull(holder.views);
        assertEquals(holder.views.size(), 3);
        assertEquals(holder.views.get(0).getId(), R.id.text1);
        assertEquals(holder.views.get(1).getId(), R.id.text2);
        assertEquals(holder.views.get(2).getId(), R.id.text3);
    }

    @Test
    public void testInjectArrayListener() throws Exception {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        // Inject the view
        Injector.inject(mView, listener);

        // Get the declared fields
        Field field = mView.getClass().getDeclaredField("mTextViewsArray");

        // Make it accessible
        field.setAccessible(true);
        Object value = field.get(mView);

        // Get the array of values
        View[] values = (View[]) value;

        // Get the click listener fields
        Field listenerField =
                Class.forName("android.view.View").getDeclaredField("mListenerInfo");
        listenerField.setAccessible(true);

        Field clickListenerField =
                Class.forName("android.view.View$ListenerInfo").getField("mOnClickListener");
        clickListenerField.setAccessible(true);

        Object listenerInfo;
        View.OnClickListener retrievedListener;

        // Test the first view
        listenerInfo = listenerField.get(values[0]);
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);

        // Make sure the other two are not affected
        for (int i = 1; i < 3; i++) {
            listenerInfo = listenerField.get(values[i]);
            assertNull(listenerInfo);
        }
    }

    @Test
    @SuppressWarnings("unchecked cast")
    public void testInjectListListener() throws Exception {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        // Inject the view
        Injector.inject(mView, listener);

        // Get the declared fields
        Field field = mView.getClass().getDeclaredField("mTextViewsList");

        // Make it accessible
        field.setAccessible(true);
        Object value = field.get(mView);

        // Get the array of values
        View[] values = ((List<View>) value).toArray(new View[((List<View>) value).size()]);

        // Get the click listener fields
        Field listenerField =
                Class.forName("android.view.View").getDeclaredField("mListenerInfo");
        listenerField.setAccessible(true);

        Field clickListenerField =
                Class.forName("android.view.View$ListenerInfo").getField("mOnClickListener");
        clickListenerField.setAccessible(true);

        Object listenerInfo;
        View.OnClickListener retrievedListener;

        // Test the first view
        listenerInfo = listenerField.get(values[0]);
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);

        // Make sure the other two are not affected
        for (int i = 1; i < 3; i++) {
            listenerInfo = listenerField.get(values[i]);
            assertNull(listenerInfo);
        }
    }

    @Test
    public void testInjectExternalArrayListener() throws Exception {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        // Declare holder
        class ViewHolder {
            @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
            View[] views;
        }

        ViewHolder holder = new ViewHolder();
        Injector.injectToFrom(holder, mView, listener);

        // Get the click listener fields
        Field listenerField =
                Class.forName("android.view.View").getDeclaredField("mListenerInfo");
        listenerField.setAccessible(true);

        Field clickListenerField =
                Class.forName("android.view.View$ListenerInfo").getField("mOnClickListener");
        clickListenerField.setAccessible(true);

        Object listenerInfo;
        View.OnClickListener retrievedListener;

        // Test the first view
        listenerInfo = listenerField.get(holder.views[0]);
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);

        // Make sure the other two are not affected
        for (int i = 1; i < 3; i++) {
            listenerInfo = listenerField.get(holder.views[i]);
            assertNull(listenerInfo);
        }
    }

    @Test
    public void testInjectExternalListListener() throws Exception {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        // Declare holder
        class ViewHolder {
            @InjectGroup({R.id.text1, R.id.text2, R.id.text3})
            List<View> views;
        }

        ViewHolder holder = new ViewHolder();
        Injector.injectToFrom(holder, mView, listener);

        // Get the click listener fields
        Field listenerField =
                Class.forName("android.view.View").getDeclaredField("mListenerInfo");
        listenerField.setAccessible(true);

        Field clickListenerField =
                Class.forName("android.view.View$ListenerInfo").getField("mOnClickListener");
        clickListenerField.setAccessible(true);

        Object listenerInfo;
        View.OnClickListener retrievedListener;

        // Test the first view
        listenerInfo = listenerField.get(holder.views.get(0));
        assertNotNull(listenerInfo);
        retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);

        assertEquals(retrievedListener, listener);

        // Make sure the other two are not affected
        for (int i = 1; i < 3; i++) {
            listenerInfo = listenerField.get(holder.views.get(i));
            assertNull(listenerInfo);
        }
    }
}
