package com.equaleyes.injector;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Field;

import com.equaleyes.injector.Util;

/**
 * Created by Žan Skamljič on 3. 08. 2015.
 */
public class Injector {
    public static void inject(Activity activity) {
        inject(activity, null);
    }

    public static void inject(Activity activity, View.OnClickListener onClickListener) {
        for (Field field : activity.getClass().getDeclaredFields())
            handleField(activity, activity, field, onClickListener);
    }

    public static void inject(View view) {
        inject(view, null);
    }

    public static void inject(View view, View.OnClickListener onClickListener) {
        for (Field field : view.getClass().getDeclaredFields())
            handleField(view, view, field, onClickListener);
    }

    public static void injectToFrom(Object viewsContainer, View view) {
        injectToFrom(viewsContainer, view, null);
    }

    public static void injectToFrom(Object viewsContainer, View view, View.OnClickListener listener) {
        for (Field field : viewsContainer.getClass().getDeclaredFields())
            handleField(viewsContainer, view, field, listener);
    }

    private static void handleField(Object owner, Object container, Field field,
                                    View.OnClickListener onClickListener) {
        if (field.isAnnotationPresent(Inject.class)) {
            field.setAccessible(true);
            Inject toInject = field.getAnnotation(Inject.class);

            int id = Util.findDesiredId(toInject.id(), field.getName(), owner);

            try {
                if (container instanceof Activity)
                    field.set(owner, ((Activity) container).findViewById(id));
                else if (container instanceof View && !(container instanceof AdapterView))
                    field.set(owner, ((View) container).findViewById(id));

                if (onClickListener != null) {
                    View view = (View) field.get(owner);
                    if (view != null && view.isClickable())
                        view.setOnClickListener(onClickListener);
                }
            } catch (Exception e) {
                Log.w("INJECTOR", "Warning: Field \"" + field.getName() + "\" was not injected.");
            }
        }
    }
}