package J;

import android.os.Build;
import android.os.Trace;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static long f472a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Method f473b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Method f474c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Method f475d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static Method f476e;

    public static void a(String str, int i3) {
        if (Build.VERSION.SDK_INT >= 29) {
            c.a(str, i3);
        } else {
            b(str, i3);
        }
    }

    private static void b(String str, int i3) {
        try {
            if (f474c == null) {
                f474c = Trace.class.getMethod("asyncTraceBegin", Long.TYPE, String.class, Integer.TYPE);
            }
            f474c.invoke(null, Long.valueOf(f472a), str, Integer.valueOf(i3));
        } catch (Exception e4) {
            g("asyncTraceBegin", e4);
        }
    }

    public static void c(String str) {
        b.a(str);
    }

    public static void d(String str, int i3) {
        if (Build.VERSION.SDK_INT >= 29) {
            c.b(str, i3);
        } else {
            e(str, i3);
        }
    }

    private static void e(String str, int i3) {
        try {
            if (f475d == null) {
                f475d = Trace.class.getMethod("asyncTraceEnd", Long.TYPE, String.class, Integer.TYPE);
            }
            f475d.invoke(null, Long.valueOf(f472a), str, Integer.valueOf(i3));
        } catch (Exception e4) {
            g("asyncTraceEnd", e4);
        }
    }

    public static void f() {
        b.b();
    }

    private static void g(String str, Exception exc) {
        if (exc instanceof InvocationTargetException) {
            Throwable cause = exc.getCause();
            if (!(cause instanceof RuntimeException)) {
                throw new RuntimeException(cause);
            }
            throw ((RuntimeException) cause);
        }
        Log.v("Trace", "Unable to call " + str + " via reflection", exc);
    }

    public static boolean h() {
        return Build.VERSION.SDK_INT >= 29 ? c.c() : i();
    }

    private static boolean i() {
        try {
            if (f473b == null) {
                f472a = Trace.class.getField("TRACE_TAG_APP").getLong(null);
                f473b = Trace.class.getMethod("isTagEnabled", Long.TYPE);
            }
            return ((Boolean) f473b.invoke(null, Long.valueOf(f472a))).booleanValue();
        } catch (Exception e4) {
            g("isTagEnabled", e4);
            return false;
        }
    }

    public static void j(String str, int i3) {
        if (Build.VERSION.SDK_INT >= 29) {
            c.d(str, i3);
        } else {
            k(str, i3);
        }
    }

    private static void k(String str, int i3) {
        try {
            if (f476e == null) {
                f476e = Trace.class.getMethod("traceCounter", Long.TYPE, String.class, Integer.TYPE);
            }
            f476e.invoke(null, Long.valueOf(f472a), str, Integer.valueOf(i3));
        } catch (Exception e4) {
            g("traceCounter", e4);
        }
    }
}
