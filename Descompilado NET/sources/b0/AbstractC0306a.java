package b0;

import X.k;
import android.graphics.Bitmap;
import java.io.Closeable;
import java.io.IOException;

/* JADX INFO: renamed from: b0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0306a implements Cloneable, Closeable {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static int f5573g;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected boolean f5576b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected final C0314i f5577c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected final c f5578d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected final Throwable f5579e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static Class f5572f = AbstractC0306a.class;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final InterfaceC0313h f5574h = new C0087a();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final c f5575i = new b();

    /* JADX INFO: renamed from: b0.a$a, reason: collision with other inner class name */
    class C0087a implements InterfaceC0313h {
        C0087a() {
        }

        @Override // b0.InterfaceC0313h
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Closeable closeable) {
            try {
                X.b.a(closeable, true);
            } catch (IOException unused) {
            }
        }
    }

    /* JADX INFO: renamed from: b0.a$b */
    class b implements c {
        b() {
        }

        @Override // b0.AbstractC0306a.c
        public boolean a() {
            return false;
        }

        @Override // b0.AbstractC0306a.c
        public void b(C0314i c0314i, Throwable th) {
            Object objF = c0314i.f();
            Y.a.G(AbstractC0306a.f5572f, "Finalized without closing: %x %x (type = %s)", Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(c0314i)), objF == null ? null : objF.getClass().getName());
        }
    }

    /* JADX INFO: renamed from: b0.a$c */
    public interface c {
        boolean a();

        void b(C0314i c0314i, Throwable th);
    }

    protected AbstractC0306a(C0314i c0314i, c cVar, Throwable th) {
        this.f5577c = (C0314i) k.g(c0314i);
        c0314i.b();
        this.f5578d = cVar;
        this.f5579e = th;
    }

    public static AbstractC0306a A(AbstractC0306a abstractC0306a) {
        if (abstractC0306a != null) {
            return abstractC0306a.z();
        }
        return null;
    }

    public static void D(AbstractC0306a abstractC0306a) {
        if (abstractC0306a != null) {
            abstractC0306a.close();
        }
    }

    public static boolean c0(AbstractC0306a abstractC0306a) {
        return abstractC0306a != null && abstractC0306a.a0();
    }

    public static AbstractC0306a d0(Closeable closeable) {
        return n0(closeable, f5574h);
    }

    public static AbstractC0306a e0(Closeable closeable, c cVar) {
        if (closeable == null) {
            return null;
        }
        return u0(closeable, f5574h, cVar, cVar.a() ? new Throwable() : null);
    }

    public static AbstractC0306a n0(Object obj, InterfaceC0313h interfaceC0313h) {
        return t0(obj, interfaceC0313h, f5575i);
    }

    public static AbstractC0306a t0(Object obj, InterfaceC0313h interfaceC0313h, c cVar) {
        if (obj == null) {
            return null;
        }
        return u0(obj, interfaceC0313h, cVar, cVar.a() ? new Throwable() : null);
    }

    public static AbstractC0306a u0(Object obj, InterfaceC0313h interfaceC0313h, c cVar, Throwable th) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Bitmap) || (obj instanceof InterfaceC0309d)) {
            int i3 = f5573g;
            if (i3 == 1) {
                return new C0308c(obj, interfaceC0313h, cVar, th);
            }
            if (i3 == 2) {
                return new C0312g(obj, interfaceC0313h, cVar, th);
            }
            if (i3 == 3) {
                return new C0310e(obj);
            }
        }
        return new C0307b(obj, interfaceC0313h, cVar, th);
    }

    public synchronized Object P() {
        k.i(!this.f5576b);
        return k.g(this.f5577c.f());
    }

    public int X() {
        if (a0()) {
            return System.identityHashCode(this.f5577c.f());
        }
        return 0;
    }

    public synchronized boolean a0() {
        return !this.f5576b;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this) {
            try {
                if (this.f5576b) {
                    return;
                }
                this.f5576b = true;
                this.f5577c.d();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: y */
    public abstract AbstractC0306a clone();

    public synchronized AbstractC0306a z() {
        if (!a0()) {
            return null;
        }
        return clone();
    }

    protected AbstractC0306a(Object obj, InterfaceC0313h interfaceC0313h, c cVar, Throwable th, boolean z3) {
        this.f5577c = new C0314i(obj, interfaceC0313h, z3);
        this.f5578d = cVar;
        this.f5579e = th;
    }
}
