package com.equaleyes.injector;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Žan Skamljič on 3. 08. 2015.
 */
public class Injector {

    public static void inject(Activity activity) {
        inject(activity, null);
    }

    public static void inject(Activity activity, View.OnClickListener onClickListener) {
        for (Field field : activity.getClass().getDeclaredFields()) {
            handleAnnotations(activity, activity, field, onClickListener);
        }
    }

    public static void inject(View view) {
        inject(view, null);
    }

    public static void inject(View view, View.OnClickListener onClickListener) {
        for (Field field : view.getClass().getDeclaredFields()) {
            handleAnnotations(view, view, field, onClickListener);
        }
    }

    public static void injectTo(Object container, Context context) {
        for (Field field : container.getClass().getDeclaredFields()) {
            handleAnnotations(context, container, field, null);
        }
    }

    public static void injectToFrom(Object viewsContainer, View view) {
        injectToFrom(viewsContainer, view, null);
    }

    public static void injectToFrom(Object viewsContainer, View view, View.OnClickListener listener) {
        for (Field field : viewsContainer.getClass().getDeclaredFields()) {
            handleAnnotations(viewsContainer, view, field, listener);
        }
    }

    @SuppressWarnings("unchecked cast")
    private static void handleAnnotations(Object owner, Object container, Field field,
                                          View.OnClickListener onClickListener) {
        if (field.isAnnotationPresent(Inject.class)) {
            field.setAccessible(true);
            Inject toInject = field.getAnnotation(Inject.class);

            int id = Util.findDesiredId(toInject.value(), field.getName(), owner);
            if (id == -1) {
                Log.w("INJECTOR", "Warning: Field \"" + field.getName() + "\" was not injected.");
                return;
            }

            try {
                View view = getView(container, id, onClickListener);
                if (view == null) {
                    view = getView(container,
                            Util.getAndroidRId(field.getName()), onClickListener);
                }

                field.set(owner, view);
            } catch (Exception e) {
                Log.w("INJECTOR", "Warning: Field \"" + field.getName() + "\" was not injected.");
            }
        } else if (field.isAnnotationPresent(InjectGroup.class)) {
            field.setAccessible(true);

            InjectGroup toInject = field.getAnnotation(InjectGroup.class);

            int[] ids = toInject.value();
            View[] views = new View[ids.length];

            for (int i = 0; i < ids.length; i++) {
                views[i] = getView(container, ids[i], onClickListener);
                if (views[i] == null) {
                    views[i] = getView(container,
                            Util.getAndroidRId(field.getName()), onClickListener);
                }
            }

            try {
                if (field.getType().isArray()) {
                    if (field.getType() == View[].class) {
                        field.set(owner, views);
                    } else {
                        Class type = field.getType();

                        String typeName = type.getCanonicalName();
                        typeName = typeName.substring(0, typeName.length() - 2);

                        Class itemType = Class.forName(typeName);
                        Object array = Array.newInstance(itemType, views.length);

                        for (int i = 0; i < views.length; i++) {
                            Array.set(array, i, downcastItem(views[i]));
                        }

                        field.set(owner, array);
                    }
                } else if (field.getType() == List.class) {
                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    if (type.getActualTypeArguments()[0] == View.class) {
                        field.set(owner, Arrays.asList(views));
                    } else {
                        List list = createTypedList();
                        for (View view : views)
                            list.add(downcastItem(view));

                        field.set(owner, list);
                    }
                }
            } catch (Exception e) {
                Log.w("INJECTOR", "Warning: Field \"" + field.getName() + "\" was not injected.");
            }
        } else if (field.isAnnotationPresent(InjectRes.class)) {
            field.setAccessible(true);

            InjectRes toInject = field.getAnnotation(InjectRes.class);

            Class type = field.getType();

            Context context = (Context) owner;
            if (type.isAssignableFrom(Drawable.class)) {
                int id = Util.findDrawableById(toInject.value(), field.getName(), context);

                if (id == -1) {
                    Log.w("INJECTOR", "Warning: Field \"" +
                            field.getName() + "\" was not injected.");
                    return;
                }

                try {
                    Drawable drawable = getDrawable(context, id);
                    if (drawable == null) {
                        drawable = getDrawable(context, Util.getAndroidDrawable(field.getName()));
                    }

                    field.set(container, drawable);
                } catch (Exception e) {
                    Log.w("INJECTOR", "Warning: Field \"" +
                            field.getName() + "\" was not injected.");
                }
            } else if (type.isAssignableFrom(String.class)) {
                int id = Util.findStringById(toInject.value(), field.getName(), context);

                if (id == -1) {
                    Log.w("INJECTOR", "Warning: Field \"" +
                            field.getName() + "\" was not injected.");
                    return;
                }

                try {
                    String string = context.getString(id);
                    if (string == null) {
                        string = context.getString(Util.getAndroidString(field.getName()));
                    }

                    field.set(container, string);
                } catch (Exception e) {
                    Log.w("INJECTOR", "Warning: Field \"" +
                            field.getName() + "\" was not injected.");
                }
            } else if (type.isAssignableFrom(int.class)) {
                int id = Util.findColorById(toInject.value(), field.getName(), context);

                if (id == -1) {
                    Log.w("INJECTOR", "Warning: Field \"" +
                            field.getName() + "\" was not injected.");
                    return;
                }

                try {
                    int color = getColor(context, id);
                    if (color == 0) {
                        color = getColor(context, Util.getAndroidColor(field.getName()));
                    }

                    field.set(container, color);
                } catch (Exception e) {
                    Log.w("INJECTOR", "Warning: Field \"" +
                            field.getName() + "\" was not injected.");
                }
            }
        }
    }

    private static View getView(Object container, int id, View.OnClickListener listener) {
        View view = null;

        if (container instanceof Activity) {
            view = ((Activity) container).findViewById(id);
        } else if (container instanceof View && !(container instanceof AdapterView)) {
            view = ((View) container).findViewById(id);
        }

        if (listener != null) {
            if (view != null && view.isClickable() && !(view instanceof AdapterView))
                view.setOnClickListener(listener);
        }

        return view;
    }

    private static Drawable getDrawable(Context context, int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(id, context.getTheme());
        } else {
            drawable = context.getResources().getDrawable(id);
        }

        return drawable;
    }

    private static int getColor(Context context, int id) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = context.getResources().getColor(id, context.getTheme());
        } else {
            color = context.getResources().getColor(id);
        }
        return color;
    }

    private static <T> T downcastItem(Object item) {
        return (T) item;
    }

    private static <T> List<T> createTypedList() {
        return new ArrayList<>();
    }
}