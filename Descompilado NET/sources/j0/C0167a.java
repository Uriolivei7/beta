package J0;

import android.util.Log;
import b0.AbstractC0306a;
import b0.C0314i;
import b0.InterfaceC0313h;
import java.io.Closeable;

/* JADX INFO: renamed from: J0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0167a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final AbstractC0306a.c f562a;

    /* JADX INFO: renamed from: J0.a$a, reason: collision with other inner class name */
    class C0008a implements AbstractC0306a.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ L0.a f563a;

        C0008a(L0.a aVar) {
            this.f563a = aVar;
        }

        @Override // b0.AbstractC0306a.c
        public boolean a() {
            return this.f563a.b();
        }

        @Override // b0.AbstractC0306a.c
        public void b(C0314i c0314i, Throwable th) {
            this.f563a.a(c0314i, th);
            Object objF = c0314i.f();
            Y.a.K("Fresco", "Finalized without closing: %x %x (type = %s).\nStack:\n%s", Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(c0314i)), objF != null ? objF.getClass().getName() : "<value is null>", C0167a.d(th));
        }
    }

    public C0167a(L0.a aVar) {
        this.f562a = new C0008a(aVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String d(Throwable th) {
        return th == null ? "" : Log.getStackTraceString(th);
    }

    public AbstractC0306a b(Closeable closeable) {
        return AbstractC0306a.e0(closeable, this.f562a);
    }

    public AbstractC0306a c(Object obj, InterfaceC0313h interfaceC0313h) {
        return AbstractC0306a.t0(obj, interfaceC0313h, this.f562a);
    }
}
