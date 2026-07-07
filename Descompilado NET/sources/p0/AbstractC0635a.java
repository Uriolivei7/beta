package p0;

import android.os.Looper;

/* JADX INFO: renamed from: p0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0635a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AbstractC0635a f10265a;

    /* JADX INFO: renamed from: p0.a$a, reason: collision with other inner class name */
    public interface InterfaceC0140a {
        void release();
    }

    public static synchronized AbstractC0635a b() {
        try {
            if (f10265a == null) {
                f10265a = new b();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f10265a;
    }

    static boolean c() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public abstract void a(InterfaceC0140a interfaceC0140a);

    public abstract void d(InterfaceC0140a interfaceC0140a);
}
