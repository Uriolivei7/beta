package androidx.appcompat.app;

import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
abstract class w {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Field f3300a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static boolean f3301b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Class f3302c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static boolean f3303d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static Field f3304e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static boolean f3305f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static Field f3306g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static boolean f3307h;

    static void a(Resources resources) {
        if (Build.VERSION.SDK_INT >= 28) {
            return;
        }
        b(resources);
    }

    private static void b(Resources resources) {
        Object obj;
        if (!f3307h) {
            try {
                Field declaredField = Resources.class.getDeclaredField("mResourcesImpl");
                f3306g = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e4) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", e4);
            }
            f3307h = true;
        }
        Field field = f3306g;
        if (field == null) {
            return;
        }
        Object obj2 = null;
        try {
            obj = field.get(resources);
        } catch (IllegalAccessException e5) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", e5);
            obj = null;
        }
        if (obj == null) {
            return;
        }
        if (!f3301b) {
            try {
                Field declaredField2 = obj.getClass().getDeclaredField("mDrawableCache");
                f3300a = declaredField2;
                declaredField2.setAccessible(true);
            } catch (NoSuchFieldException e6) {
                Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", e6);
            }
            f3301b = true;
        }
        Field field2 = f3300a;
        if (field2 != null) {
            try {
                obj2 = field2.get(obj);
            } catch (IllegalAccessException e7) {
                Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", e7);
            }
        }
        if (obj2 != null) {
            c(obj2);
        }
    }

    private static void c(Object obj) {
        LongSparseArray longSparseArray;
        if (!f3303d) {
            try {
                f3302c = Class.forName("android.content.res.ThemedResourceCache");
            } catch (ClassNotFoundException e4) {
                Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", e4);
            }
            f3303d = true;
        }
        Class cls = f3302c;
        if (cls == null) {
            return;
        }
        if (!f3305f) {
            try {
                Field declaredField = cls.getDeclaredField("mUnthemedEntries");
                f3304e = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e5) {
                Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", e5);
            }
            f3305f = true;
        }
        Field field = f3304e;
        if (field == null) {
            return;
        }
        try {
            longSparseArray = (LongSparseArray) field.get(obj);
        } catch (IllegalAccessException e6) {
            Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", e6);
            longSparseArray = null;
        }
        if (longSparseArray != null) {
            longSparseArray.clear();
        }
    }
}
