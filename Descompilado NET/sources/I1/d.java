package I1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import o1.InterfaceC0629a;

/* JADX INFO: loaded from: classes.dex */
public class d implements InterfaceC0629a {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final Executor f434i = I1.c.f433c;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final Executor f435j = I1.c.f432b;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static d f436k = new d((Object) null);

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static d f437l = new d(Boolean.TRUE);

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static d f438m = new d(Boolean.FALSE);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static d f439n = new d(true);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f441b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f442c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Object f443d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Exception f444e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f445f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private I1.f f446g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Object f440a = new Object();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private List f447h = new ArrayList();

    class a implements I1.a {
        a() {
        }

        @Override // I1.a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public d a(d dVar) {
            return dVar.q() ? d.e() : dVar.s() ? d.l(dVar.n()) : d.m(null);
        }
    }

    class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ I1.e f449b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Callable f450c;

        b(I1.e eVar, Callable callable) {
            this.f449b = eVar;
            this.f450c = callable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f449b.d(this.f450c.call());
            } catch (CancellationException unused) {
                this.f449b.b();
            } catch (Exception e4) {
                this.f449b.c(e4);
            }
        }
    }

    class c implements I1.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ I1.e f451a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ I1.a f452b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Executor f453c;

        c(I1.e eVar, I1.a aVar, Executor executor) {
            this.f451a = eVar;
            this.f452b = aVar;
            this.f453c = executor;
        }

        @Override // I1.a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void a(d dVar) {
            d.g(this.f451a, this.f452b, dVar, this.f453c);
            return null;
        }
    }

    /* JADX INFO: renamed from: I1.d$d, reason: collision with other inner class name */
    class C0007d implements I1.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ I1.e f455a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ I1.a f456b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Executor f457c;

        C0007d(I1.e eVar, I1.a aVar, Executor executor) {
            this.f455a = eVar;
            this.f456b = aVar;
            this.f457c = executor;
        }

        @Override // I1.a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void a(d dVar) {
            d.f(this.f455a, this.f456b, dVar, this.f457c);
            return null;
        }
    }

    class e implements I1.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ I1.a f459a;

        e(I1.a aVar) {
            this.f459a = aVar;
        }

        @Override // I1.a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public d a(d dVar) {
            return dVar.s() ? d.l(dVar.n()) : dVar.q() ? d.e() : dVar.h(this.f459a);
        }
    }

    class f implements I1.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ I1.a f461a;

        f(I1.a aVar) {
            this.f461a = aVar;
        }

        @Override // I1.a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public d a(d dVar) {
            return dVar.s() ? d.l(dVar.n()) : dVar.q() ? d.e() : dVar.j(this.f461a);
        }
    }

    class g implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ I1.a f463b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ d f464c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ I1.e f465d;

        g(I1.a aVar, d dVar, I1.e eVar) {
            this.f463b = aVar;
            this.f464c = dVar;
            this.f465d = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f465d.d(this.f463b.a(this.f464c));
            } catch (CancellationException unused) {
                this.f465d.b();
            } catch (Exception e4) {
                this.f465d.c(e4);
            }
        }
    }

    class h implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ I1.a f466b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ d f467c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ I1.e f468d;

        class a implements I1.a {
            a() {
            }

            @Override // I1.a
            /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
            public Void a(d dVar) {
                if (dVar.q()) {
                    h.this.f468d.b();
                    return null;
                }
                if (dVar.s()) {
                    h.this.f468d.c(dVar.n());
                    return null;
                }
                h.this.f468d.d(dVar.o());
                return null;
            }
        }

        h(I1.a aVar, d dVar, I1.e eVar) {
            this.f466b = aVar;
            this.f467c = dVar;
            this.f468d = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                d dVar = (d) this.f466b.a(this.f467c);
                if (dVar == null) {
                    this.f468d.d(null);
                } else {
                    dVar.h(new a());
                }
            } catch (CancellationException unused) {
                this.f468d.b();
            } catch (Exception e4) {
                this.f468d.c(e4);
            }
        }
    }

    public interface i {
    }

    d() {
    }

    public static d c(Callable callable) {
        return d(callable, f434i);
    }

    public static d d(Callable callable, Executor executor) {
        I1.e eVar = new I1.e();
        try {
            executor.execute(new b(eVar, callable));
        } catch (Exception e4) {
            eVar.c(new I1.b(e4));
        }
        return eVar.a();
    }

    public static d e() {
        return f439n;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void f(I1.e eVar, I1.a aVar, d dVar, Executor executor) {
        try {
            executor.execute(new h(aVar, dVar, eVar));
        } catch (Exception e4) {
            eVar.c(new I1.b(e4));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void g(I1.e eVar, I1.a aVar, d dVar, Executor executor) {
        try {
            executor.execute(new g(aVar, dVar, eVar));
        } catch (Exception e4) {
            eVar.c(new I1.b(e4));
        }
    }

    public static d l(Exception exc) {
        I1.e eVar = new I1.e();
        eVar.c(exc);
        return eVar.a();
    }

    public static d m(Object obj) {
        if (obj == null) {
            return f436k;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? f437l : f438m;
        }
        I1.e eVar = new I1.e();
        eVar.d(obj);
        return eVar.a();
    }

    public static i p() {
        return null;
    }

    private void w() {
        synchronized (this.f440a) {
            Iterator it = this.f447h.iterator();
            while (it.hasNext()) {
                try {
                    ((I1.a) it.next()).a(this);
                } catch (RuntimeException e4) {
                    throw e4;
                } catch (Exception e5) {
                    throw new RuntimeException(e5);
                }
            }
            this.f447h = null;
        }
    }

    public d h(I1.a aVar) {
        return i(aVar, f434i);
    }

    public d i(I1.a aVar, Executor executor) {
        boolean zR;
        I1.e eVar = new I1.e();
        synchronized (this.f440a) {
            try {
                zR = r();
                if (!zR) {
                    this.f447h.add(new c(eVar, aVar, executor));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (zR) {
            g(eVar, aVar, this, executor);
        }
        return eVar.a();
    }

    public d j(I1.a aVar) {
        return k(aVar, f434i);
    }

    public d k(I1.a aVar, Executor executor) {
        boolean zR;
        I1.e eVar = new I1.e();
        synchronized (this.f440a) {
            try {
                zR = r();
                if (!zR) {
                    this.f447h.add(new C0007d(eVar, aVar, executor));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (zR) {
            f(eVar, aVar, this, executor);
        }
        return eVar.a();
    }

    public Exception n() {
        Exception exc;
        synchronized (this.f440a) {
            try {
                if (this.f444e != null) {
                    this.f445f = true;
                    I1.f fVar = this.f446g;
                    if (fVar != null) {
                        fVar.a();
                        this.f446g = null;
                    }
                }
                exc = this.f444e;
            } catch (Throwable th) {
                throw th;
            }
        }
        return exc;
    }

    public Object o() {
        Object obj;
        synchronized (this.f440a) {
            obj = this.f443d;
        }
        return obj;
    }

    public boolean q() {
        boolean z3;
        synchronized (this.f440a) {
            z3 = this.f442c;
        }
        return z3;
    }

    public boolean r() {
        boolean z3;
        synchronized (this.f440a) {
            z3 = this.f441b;
        }
        return z3;
    }

    public boolean s() {
        boolean z3;
        synchronized (this.f440a) {
            z3 = n() != null;
        }
        return z3;
    }

    public d t() {
        return j(new a());
    }

    public d u(I1.a aVar, Executor executor) {
        return k(new e(aVar), executor);
    }

    public d v(I1.a aVar, Executor executor) {
        return k(new f(aVar), executor);
    }

    boolean x() {
        synchronized (this.f440a) {
            try {
                if (this.f441b) {
                    return false;
                }
                this.f441b = true;
                this.f442c = true;
                this.f440a.notifyAll();
                w();
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    boolean y(Exception exc) {
        synchronized (this.f440a) {
            try {
                if (this.f441b) {
                    return false;
                }
                this.f441b = true;
                this.f444e = exc;
                this.f445f = false;
                this.f440a.notifyAll();
                w();
                if (!this.f445f) {
                    p();
                }
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    boolean z(Object obj) {
        synchronized (this.f440a) {
            try {
                if (this.f441b) {
                    return false;
                }
                this.f441b = true;
                this.f443d = obj;
                this.f440a.notifyAll();
                w();
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private d(Object obj) {
        z(obj);
    }

    private d(boolean z3) {
        if (z3) {
            x();
        } else {
            z(null);
        }
    }
}
