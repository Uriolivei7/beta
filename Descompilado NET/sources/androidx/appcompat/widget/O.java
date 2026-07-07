package androidx.appcompat.widget;

import android.R;
import android.graphics.Insets;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class O {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final int[] f3802a = {R.attr.state_checked};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final int[] f3803b = new int[0];

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final Rect f3804c = new Rect();

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final boolean f3805a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private static final Method f3806b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private static final Field f3807c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private static final Field f3808d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private static final Field f3809e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private static final Field f3810f;

        /* JADX WARN: Removed duplicated region for block: B:25:0x004c  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0059  */
        static {
            /*
                r0 = 1
                r1 = 0
                r2 = 0
                java.lang.String r3 = "android.graphics.Insets"
                java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.NoSuchFieldException -> L3f java.lang.ClassNotFoundException -> L42 java.lang.NoSuchMethodException -> L45
                java.lang.Class<android.graphics.drawable.Drawable> r4 = android.graphics.drawable.Drawable.class
                java.lang.String r5 = "getOpticalInsets"
                java.lang.Class[] r6 = new java.lang.Class[r2]     // Catch: java.lang.NoSuchFieldException -> L3f java.lang.ClassNotFoundException -> L42 java.lang.NoSuchMethodException -> L45
                java.lang.reflect.Method r4 = r4.getMethod(r5, r6)     // Catch: java.lang.NoSuchFieldException -> L3f java.lang.ClassNotFoundException -> L42 java.lang.NoSuchMethodException -> L45
                java.lang.String r5 = "left"
                java.lang.reflect.Field r5 = r3.getField(r5)     // Catch: java.lang.NoSuchFieldException -> L36 java.lang.ClassNotFoundException -> L39 java.lang.NoSuchMethodException -> L3c
                java.lang.String r6 = "top"
                java.lang.reflect.Field r6 = r3.getField(r6)     // Catch: java.lang.NoSuchFieldException -> L2f java.lang.ClassNotFoundException -> L32 java.lang.NoSuchMethodException -> L34
                java.lang.String r7 = "right"
                java.lang.reflect.Field r7 = r3.getField(r7)     // Catch: java.lang.Throwable -> L2d
                java.lang.String r8 = "bottom"
                java.lang.reflect.Field r3 = r3.getField(r8)     // Catch: java.lang.Throwable -> L48
                r8 = r0
                goto L4a
            L2d:
                r7 = r1
                goto L48
            L2f:
                r6 = r1
            L30:
                r7 = r6
                goto L48
            L32:
                r6 = r1
                goto L30
            L34:
                r6 = r1
                goto L30
            L36:
                r5 = r1
            L37:
                r6 = r5
                goto L30
            L39:
                r5 = r1
            L3a:
                r6 = r5
                goto L30
            L3c:
                r5 = r1
            L3d:
                r6 = r5
                goto L30
            L3f:
                r4 = r1
                r5 = r4
                goto L37
            L42:
                r4 = r1
                r5 = r4
                goto L3a
            L45:
                r4 = r1
                r5 = r4
                goto L3d
            L48:
                r3 = r1
                r8 = r2
            L4a:
                if (r8 == 0) goto L59
                androidx.appcompat.widget.O.a.f3806b = r4
                androidx.appcompat.widget.O.a.f3807c = r5
                androidx.appcompat.widget.O.a.f3808d = r6
                androidx.appcompat.widget.O.a.f3809e = r7
                androidx.appcompat.widget.O.a.f3810f = r3
                androidx.appcompat.widget.O.a.f3805a = r0
                goto L65
            L59:
                androidx.appcompat.widget.O.a.f3806b = r1
                androidx.appcompat.widget.O.a.f3807c = r1
                androidx.appcompat.widget.O.a.f3808d = r1
                androidx.appcompat.widget.O.a.f3809e = r1
                androidx.appcompat.widget.O.a.f3810f = r1
                androidx.appcompat.widget.O.a.f3805a = r2
            L65:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.O.a.<clinit>():void");
        }

        static Rect a(Drawable drawable) {
            if (Build.VERSION.SDK_INT < 29 && f3805a) {
                try {
                    Object objInvoke = f3806b.invoke(drawable, new Object[0]);
                    if (objInvoke != null) {
                        return new Rect(f3807c.getInt(objInvoke), f3808d.getInt(objInvoke), f3809e.getInt(objInvoke), f3810f.getInt(objInvoke));
                    }
                } catch (IllegalAccessException | InvocationTargetException unused) {
                }
            }
            return O.f3804c;
        }
    }

    static class b {
        static Insets a(Drawable drawable) {
            return drawable.getOpticalInsets();
        }
    }

    static void a(Drawable drawable) {
        String name = drawable.getClass().getName();
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 29 || i3 >= 31 || !"android.graphics.drawable.ColorStateListDrawable".equals(name)) {
            return;
        }
        b(drawable);
    }

    private static void b(Drawable drawable) {
        int[] state = drawable.getState();
        if (state == null || state.length == 0) {
            drawable.setState(f3802a);
        } else {
            drawable.setState(f3803b);
        }
        drawable.setState(state);
    }

    public static Rect c(Drawable drawable) {
        if (Build.VERSION.SDK_INT < 29) {
            return a.a(androidx.core.graphics.drawable.a.i(drawable));
        }
        Insets insetsA = b.a(drawable);
        return new Rect(insetsA.left, insetsA.top, insetsA.right, insetsA.bottom);
    }

    public static PorterDuff.Mode d(int i3, PorterDuff.Mode mode) {
        if (i3 == 3) {
            return PorterDuff.Mode.SRC_OVER;
        }
        if (i3 == 5) {
            return PorterDuff.Mode.SRC_IN;
        }
        if (i3 == 9) {
            return PorterDuff.Mode.SRC_ATOP;
        }
        switch (i3) {
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return mode;
        }
    }
}
