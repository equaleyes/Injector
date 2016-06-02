package com.equaleyes.injector;

import java.util.ArrayList;

/**
 * Created by Žan Skamljič on 4. 08. 2015.
 */
class Util {
    static int findDesiredId(int id, String name, Object object) {
        if (id != -1)
            return id;

        Class classR = getRClass(object.getClass().getPackage());

        Class idCls = null;
        for (Class cls : classR.getClasses())
            if (cls.getSimpleName().equals("id")) {
                idCls = cls;
                break;
            }
        if (idCls == null)
            throw new RuntimeException(
                    "Class " + classR.getName() + " seems to be invalid. Try setting the ID");

        ArrayList<String> candidates = new ArrayList<>();
        candidates.add(name);
        candidates.add(name.toLowerCase());
        candidates.add(name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase());
        if (name.startsWith("m")) {
            name = name.substring(1);
            candidates.add(name);
            candidates.add(name.toLowerCase());
            candidates.add(name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase());
        }

        try {
            for (String candidate : candidates) {
                try {
                    id = idCls.getDeclaredField(candidate).getInt(idCls);
                    break;
                } catch (NoSuchFieldException ignored) {

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return id;
    }

    static Class getRClass(Package pack) {
        try {
            return Class.forName(pack.getName() + ".R");
        } catch (ClassNotFoundException e) {
            if (pack.getName().contains(".")) {
                pack = Package
                        .getPackage(pack.getName().substring(0, pack.getName().lastIndexOf('.')));
                return getRClass(pack);
            } else {
                try {
                    return Class.forName("android.R");
                } catch (ClassNotFoundException ignored) {
                    throw new RuntimeException("Stop messing with the dark arts, boy!");
                }
            }
        }
    }
}