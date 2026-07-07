package androidx.core.os;

import android.os.Build;
import android.os.Trace;
import android.util.Log;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static long f4524a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Method f4525b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Method f4526c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Method f4527d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static Method f4528e;

    static {
        if (Build.VERSION.SDK_INT < 29) {
            try {
                f4524a = Trace.class.getField("TRACE_TAG_APP").getLong(null);
                Class cls = Long.TYPE;
                f4525b = Trace.class.getMethod("isTagEnabled", cls);
                Class cls2 = Integer.TYPE;
                f4526c = Trace.class.getMethod("asyncTraceBegin", cls, String.class, cls2);
                f4527d = Trace.class.getMethod("asyncTraceEnd", cls, String.class, cls2);
                f4528e = Trace.class.getMethod("traceCounter", cls, String.class, cls2);
            } catch (Exception e4) {
                Log.i("TraceCompat", "Unable to initialize via reflection.", e4);
            }
        }
    }

    public static void a(String str) {
        Trace.beginSection(str);
    }

    public static void b() {
        Trace.endSection();
    }
}
