package J;

import android.os.Trace;

/* JADX INFO: loaded from: classes.dex */
abstract class c {
    public static void a(String str, int i3) {
        Trace.beginAsyncSection(str, i3);
    }

    public static void b(String str, int i3) {
        Trace.endAsyncSection(str, i3);
    }

    public static boolean c() {
        return Trace.isEnabled();
    }

    public static void d(String str, int i3) {
        Trace.setCounter(str, i3);
    }
}
