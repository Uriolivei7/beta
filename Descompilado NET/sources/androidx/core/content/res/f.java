package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final ThreadLocal f4444a = new ThreadLocal();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final WeakHashMap f4445b = new WeakHashMap(0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final Object f4446c = new Object();

    static class a {
        static Drawable a(Resources resources, int i3, Resources.Theme theme) {
            return resources.getDrawable(i3, theme);
        }

        static Drawable b(Resources resources, int i3, int i4, Resources.Theme theme) {
            return resources.getDrawableForDensity(i3, i4, theme);
        }
    }

    static class b {
        static int a(Resources resources, int i3, Resources.Theme theme) {
            return resources.getColor(i3, theme);
        }

        static ColorStateList b(Resources resources, int i3, Resources.Theme theme) {
            return resources.getColorStateList(i3, theme);
        }
    }

    private static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final ColorStateList f4447a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final Configuration f4448b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final int f4449c;

        c(ColorStateList colorStateList, Configuration configuration, Resources.Theme theme) {
            this.f4447a = colorStateList;
            this.f4448b = configuration;
            this.f4449c = theme == null ? 0 : theme.hashCode();
        }
    }

    private static final class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Resources f4450a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final Resources.Theme f4451b;

        d(Resources resources, Resources.Theme theme) {
            this.f4450a = resources;
            this.f4451b = theme;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || d.class != obj.getClass()) {
                return false;
            }
            d dVar = (d) obj;
            return this.f4450a.equals(dVar.f4450a) && q.c.a(this.f4451b, dVar.f4451b);
        }

        public int hashCode() {
            return q.c.b(this.f4450a, this.f4451b);
        }
    }

    public static abstract class e {
        public static Handler e(Handler handler) {
            return handler == null ? new Handler(Looper.getMainLooper()) : handler;
        }

        public final void c(final int i3, Handler handler) {
            e(handler).post(new Runnable() { // from class: androidx.core.content.res.h
                @Override // java.lang.Runnable
                public final void run() {
                    this.f4457b.f(i3);
                }
            });
        }

        public final void d(final Typeface typeface, Handler handler) {
            e(handler).post(new Runnable() { // from class: androidx.core.content.res.g
                @Override // java.lang.Runnable
                public final void run() {
                    this.f4455b.g(typeface);
                }
            });
        }

        /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
        public abstract void f(int i3);

        /* JADX INFO: renamed from: i, reason: merged with bridge method [inline-methods] */
        public abstract void g(Typeface typeface);
    }

    /* JADX INFO: renamed from: androidx.core.content.res.f$f, reason: collision with other inner class name */
    public static final class C0061f {

        /* JADX INFO: renamed from: androidx.core.content.res.f$f$a */
        static class a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private static final Object f4452a = new Object();

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            private static Method f4453b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            private static boolean f4454c;

            /* JADX WARN: Removed duplicated region for block: B:31:0x0029 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            static void a(android.content.res.Resources.Theme r6) {
                /*
                    java.lang.Object r0 = androidx.core.content.res.f.C0061f.a.f4452a
                    monitor-enter(r0)
                    boolean r1 = androidx.core.content.res.f.C0061f.a.f4454c     // Catch: java.lang.Throwable -> L19
                    r2 = 0
                    if (r1 != 0) goto L25
                    r1 = 1
                    java.lang.Class<android.content.res.Resources$Theme> r3 = android.content.res.Resources.Theme.class
                    java.lang.String r4 = "rebase"
                    java.lang.Class[] r5 = new java.lang.Class[r2]     // Catch: java.lang.Throwable -> L19 java.lang.NoSuchMethodException -> L1b
                    java.lang.reflect.Method r3 = r3.getDeclaredMethod(r4, r5)     // Catch: java.lang.Throwable -> L19 java.lang.NoSuchMethodException -> L1b
                    androidx.core.content.res.f.C0061f.a.f4453b = r3     // Catch: java.lang.Throwable -> L19 java.lang.NoSuchMethodException -> L1b
                    r3.setAccessible(r1)     // Catch: java.lang.Throwable -> L19 java.lang.NoSuchMethodException -> L1b
                    goto L23
                L19:
                    r6 = move-exception
                    goto L3e
                L1b:
                    r3 = move-exception
                    java.lang.String r4 = "ResourcesCompat"
                    java.lang.String r5 = "Failed to retrieve rebase() method"
                    android.util.Log.i(r4, r5, r3)     // Catch: java.lang.Throwable -> L19
                L23:
                    androidx.core.content.res.f.C0061f.a.f4454c = r1     // Catch: java.lang.Throwable -> L19
                L25:
                    java.lang.reflect.Method r1 = androidx.core.content.res.f.C0061f.a.f4453b     // Catch: java.lang.Throwable -> L19
                    if (r1 == 0) goto L3c
                    java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L19 java.lang.reflect.InvocationTargetException -> L2f java.lang.IllegalAccessException -> L31
                    r1.invoke(r6, r2)     // Catch: java.lang.Throwable -> L19 java.lang.reflect.InvocationTargetException -> L2f java.lang.IllegalAccessException -> L31
                    goto L3c
                L2f:
                    r6 = move-exception
                    goto L32
                L31:
                    r6 = move-exception
                L32:
                    java.lang.String r1 = "ResourcesCompat"
                    java.lang.String r2 = "Failed to invoke rebase() method via reflection"
                    android.util.Log.i(r1, r2, r6)     // Catch: java.lang.Throwable -> L19
                    r6 = 0
                    androidx.core.content.res.f.C0061f.a.f4453b = r6     // Catch: java.lang.Throwable -> L19
                L3c:
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L19
                    return
                L3e:
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L19
                    throw r6
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.f.C0061f.a.a(android.content.res.Resources$Theme):void");
            }
        }

        /* JADX INFO: renamed from: androidx.core.content.res.f$f$b */
        static class b {
            static void a(Resources.Theme theme) {
                theme.rebase();
            }
        }

        public static void a(Resources.Theme theme) {
            if (Build.VERSION.SDK_INT >= 29) {
                b.a(theme);
            } else {
                a.a(theme);
            }
        }
    }

    private static void a(d dVar, int i3, ColorStateList colorStateList, Resources.Theme theme) {
        synchronized (f4446c) {
            try {
                WeakHashMap weakHashMap = f4445b;
                SparseArray sparseArray = (SparseArray) weakHashMap.get(dVar);
                if (sparseArray == null) {
                    sparseArray = new SparseArray();
                    weakHashMap.put(dVar, sparseArray);
                }
                sparseArray.append(i3, new c(colorStateList, dVar.f4450a.getConfiguration(), theme));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x003c, code lost:
    
        if (r2.f4449c == r5.hashCode()) goto L22;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.content.res.ColorStateList b(androidx.core.content.res.f.d r5, int r6) {
        /*
            java.lang.Object r0 = androidx.core.content.res.f.f4446c
            monitor-enter(r0)
            java.util.WeakHashMap r1 = androidx.core.content.res.f.f4445b     // Catch: java.lang.Throwable -> L32
            java.lang.Object r1 = r1.get(r5)     // Catch: java.lang.Throwable -> L32
            android.util.SparseArray r1 = (android.util.SparseArray) r1     // Catch: java.lang.Throwable -> L32
            if (r1 == 0) goto L45
            int r2 = r1.size()     // Catch: java.lang.Throwable -> L32
            if (r2 <= 0) goto L45
            java.lang.Object r2 = r1.get(r6)     // Catch: java.lang.Throwable -> L32
            androidx.core.content.res.f$c r2 = (androidx.core.content.res.f.c) r2     // Catch: java.lang.Throwable -> L32
            if (r2 == 0) goto L45
            android.content.res.Configuration r3 = r2.f4448b     // Catch: java.lang.Throwable -> L32
            android.content.res.Resources r4 = r5.f4450a     // Catch: java.lang.Throwable -> L32
            android.content.res.Configuration r4 = r4.getConfiguration()     // Catch: java.lang.Throwable -> L32
            boolean r3 = r3.equals(r4)     // Catch: java.lang.Throwable -> L32
            if (r3 == 0) goto L42
            android.content.res.Resources$Theme r5 = r5.f4451b     // Catch: java.lang.Throwable -> L32
            if (r5 != 0) goto L34
            int r3 = r2.f4449c     // Catch: java.lang.Throwable -> L32
            if (r3 == 0) goto L3e
            goto L34
        L32:
            r5 = move-exception
            goto L48
        L34:
            if (r5 == 0) goto L42
            int r3 = r2.f4449c     // Catch: java.lang.Throwable -> L32
            int r5 = r5.hashCode()     // Catch: java.lang.Throwable -> L32
            if (r3 != r5) goto L42
        L3e:
            android.content.res.ColorStateList r5 = r2.f4447a     // Catch: java.lang.Throwable -> L32
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L32
            return r5
        L42:
            r1.remove(r6)     // Catch: java.lang.Throwable -> L32
        L45:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L32
            r5 = 0
            return r5
        L48:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L32
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.f.b(androidx.core.content.res.f$d, int):android.content.res.ColorStateList");
    }

    public static int c(Resources resources, int i3, Resources.Theme theme) {
        return b.a(resources, i3, theme);
    }

    public static ColorStateList d(Resources resources, int i3, Resources.Theme theme) {
        d dVar = new d(resources, theme);
        ColorStateList colorStateListB = b(dVar, i3);
        if (colorStateListB != null) {
            return colorStateListB;
        }
        ColorStateList colorStateListI = i(resources, i3, theme);
        if (colorStateListI == null) {
            return b.b(resources, i3, theme);
        }
        a(dVar, i3, colorStateListI, theme);
        return colorStateListI;
    }

    public static Drawable e(Resources resources, int i3, Resources.Theme theme) {
        return a.a(resources, i3, theme);
    }

    public static Drawable f(Resources resources, int i3, int i4, Resources.Theme theme) {
        return a.b(resources, i3, i4, theme);
    }

    public static Typeface g(Context context, int i3, TypedValue typedValue, int i4, e eVar) {
        if (context.isRestricted()) {
            return null;
        }
        return k(context, i3, typedValue, i4, eVar, null, true, false);
    }

    private static TypedValue h() {
        ThreadLocal threadLocal = f4444a;
        TypedValue typedValue = (TypedValue) threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    private static ColorStateList i(Resources resources, int i3, Resources.Theme theme) {
        if (j(resources, i3)) {
            return null;
        }
        try {
            return androidx.core.content.res.c.a(resources, resources.getXml(i3), theme);
        } catch (Exception e4) {
            Log.w("ResourcesCompat", "Failed to inflate ColorStateList, leaving it to the framework", e4);
            return null;
        }
    }

    private static boolean j(Resources resources, int i3) {
        TypedValue typedValueH = h();
        resources.getValue(i3, typedValueH, true);
        int i4 = typedValueH.type;
        return i4 >= 28 && i4 <= 31;
    }

    private static Typeface k(Context context, int i3, TypedValue typedValue, int i4, e eVar, Handler handler, boolean z3, boolean z4) {
        Resources resources = context.getResources();
        resources.getValue(i3, typedValue, true);
        Typeface typefaceL = l(context, resources, typedValue, i3, i4, eVar, handler, z3, z4);
        if (typefaceL != null || eVar != null || z4) {
            return typefaceL;
        }
        throw new Resources.NotFoundException("Font resource ID #0x" + Integer.toHexString(i3) + " could not be retrieved.");
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.graphics.Typeface l(android.content.Context r16, android.content.res.Resources r17, android.util.TypedValue r18, int r19, int r20, androidx.core.content.res.f.e r21, android.os.Handler r22, boolean r23, boolean r24) {
        /*
            Method dump skipped, instruction units count: 245
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.f.l(android.content.Context, android.content.res.Resources, android.util.TypedValue, int, int, androidx.core.content.res.f$e, android.os.Handler, boolean, boolean):android.graphics.Typeface");
    }
}
