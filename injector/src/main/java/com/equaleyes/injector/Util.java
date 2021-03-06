package com.equaleyes.injector;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Žan Skamljič on 4. 08. 2015.
 */
class Util {
    private static Map<Package, Class> mClasses = new HashMap<>();

    static int findDesiredId(int id, String name, Object object) {
        return getResourceId(id, name, object, "id");
    }

    static int findDrawableById(int id, String name, Object object) {
        return getResourceId(id, name, object, "drawable");
    }

    static int findStringById(int id, String name, Object object) {
        return getResourceId(id, name, object, "string");
    }

    static int findColorById(int id, String name, Object object) {
        return getResourceId(id, name, object, "color");
    }

    static int getResourceId(int id, String name, Object object, String type) {
        if (id != -1) {
            return id;
        }

        Set<String> candidates = getCandidates(name);

        Class clazz = getRClass(object.getClass().getPackage());
        Class idClass;
        try {
            idClass = Class.forName(clazz.getName() + "$" + type);
        } catch (ClassNotFoundException e) {
            Log.e("Injector",
                    "Class " + clazz.getName() + " seems to be invalid. Try setting the ID");
            return -1;
        }

        id = getIdFromClass(idClass, candidates);
        if (id != -1) {
            return id;
        }

        idClass = android.R.id.class;
        id = getIdFromClass(idClass, candidates);
        return id;
    }

    static int getAndroidRId(String name) {
        return getIdFromClass(android.R.id.class, getCandidates(name));
    }

    static int getAndroidDrawable(String name) {
        return getIdFromClass(android.R.drawable.class, getCandidates(name));
    }

    static int getAndroidString(String name) {
        return getIdFromClass(android.R.string.class, getCandidates(name));
    }

    static int getAndroidColor(String name) {
        return getIdFromClass(android.R.color.class, getCandidates(name));
    }

    private static Set<String> getCandidates(String name) {
        // Assemble a set of valid field names
        Set<String> candidates = new HashSet<>();
        candidates.add(name);
        candidates.add(name.toLowerCase());
        candidates.add(name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase());

        // Add m-prefixed candidates only if the field starts with "m"
        if (name.startsWith("m")) {
            name = name.substring(1);
            candidates.add(name);
            candidates.add(name.toLowerCase());
            candidates.add(Character.toLowerCase(name.charAt(0)) + name.substring(1));
            candidates.add(name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase());
        }

        return candidates;
    }

    private static int getIdFromClass(Class idClass, Set<String> candidates) {
        int id;
        for (String candidate : candidates) {
            try {
                id = idClass.getDeclaredField(candidate).getInt(idClass);
                return id;
            } catch (Exception ignored) {
                // The candidate was not valid
            }
        }

        return -1;
    }

    static Class getRClass(Package pack) {
        Class clazz;
        if ((clazz = mClasses.get(pack)) != null) {
            return clazz;
        }

        try {
            clazz = Class.forName(pack.getName() + ".R");
            mClasses.put(pack, clazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            if (pack.getName().contains(".")) {
                pack = Package
                        .getPackage(pack.getName().substring(0, pack.getName().lastIndexOf('.')));
                return getRClass(pack);
            } else {
                return android.R.class;
            }
        }
    }
}