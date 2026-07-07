package O;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final ExecutorService f1424i = O.b.a();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final Executor f1425j = O.b.b();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final Executor f1426k = O.a.c();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static f f1427l = new f((Object) null);

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static f f1428m = new f(Boolean.TRUE);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static f f1429n = new f(Boolean.FALSE);

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static f f1430o = new f(true);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f1432b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f1433c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Object f1434d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Exception f1435e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f1436f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private h f1437g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Object f1431a = new Object();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private List f1438h = new ArrayList();

    class a implements O.d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ g f1439a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ O.d f1440b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Executor f1441c;

        a(g gVar, O.d dVar, Executor executor, O.c cVar) {
            this.f1439a = gVar;
            this.f1440b = dVar;
            this.f1441c = executor;
        }

        @Override // O.d
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void a(f fVar) {
            f.d(this.f1439a, this.f1440b, fVar, this.f1441c, null);
            return null;
        }
    }

    static class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ g f1443b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ O.d f1444c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ f f1445d;

        b(O.c cVar, g gVar, O.d dVar, f fVar) {
            this.f1443b = gVar;
            this.f1444c = dVar;
            this.f1445d = fVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f1443b.d(this.f1444c.a(this.f1445d));
            } catch (CancellationException unused) {
                this.f1443b.b();
            } catch (Exception e4) {
                this.f1443b.c(e4);
            }
        }
    }

    static class c implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ g f1446b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Callable f1447c;

        c(O.c cVar, g gVar, Callable callable) {
            this.f1446b = gVar;
            this.f1447c = callable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f1446b.d(this.f1447c.call());
            } catch (CancellationException unused) {
                this.f1446b.b();
            } catch (Exception e4) {
                this.f1446b.c(e4);
            }
        }
    }

    public interface d {
    }

    f() {
    }

    public static f b(Callable callable, Executor executor) {
        return c(callable, executor, null);
    }

    public static f c(Callable callable, Executor executor, O.c cVar) {
        g gVar = new g();
        try {
            executor.execute(new c(cVar, gVar, callable));
        } catch (Exception e4) {
            gVar.c(new e(e4));
        }
        return gVar.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void d(g gVar, O.d dVar, f fVar, Executor executor, O.c cVar) {
        try {
            executor.execute(new b(cVar, gVar, dVar, fVar));
        } catch (Exception e4) {
            gVar.c(new e(e4));
        }
    }

    public static f g(Exception exc) {
        g gVar = new g();
        gVar.c(exc);
        return gVar.a();
    }

    public static f h(Object obj) {
        if (obj == null) {
            return f1427l;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? f1428m : f1429n;
        }
        g gVar = new g();
        gVar.d(obj);
        return gVar.a();
    }

    public static d k() {
        return null;
    }

    private void o() {
        synchronized (this.f1431a) {
            Iterator it = this.f1438h.iterator();
            while (it.hasNext()) {
                try {
                    ((O.d) it.next()).a(this);
                } catch (RuntimeException e4) {
                    throw e4;
                } catch (Exception e5) {
                    throw new RuntimeException(e5);
                }
            }
            this.f1438h = null;
        }
    }

    public f e(O.d dVar) {
        return f(dVar, f1425j, null);
    }

    public f f(O.d dVar, Executor executor, O.c cVar) {
        boolean zM;
        g gVar = new g();
        synchronized (this.f1431a) {
            try {
                zM = m();
                if (!zM) {
                    this.f1438h.add(new a(gVar, dVar, executor, cVar));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (zM) {
            d(gVar, dVar, this, executor, cVar);
        }
        return gVar.a();
    }

    public Exception i() {
        Exception exc;
        synchronized (this.f1431a) {
            try {
                if (this.f1435e != null) {
                    this.f1436f = true;
                    h hVar = this.f1437g;
                    if (hVar != null) {
                        hVar.a();
                        this.f1437g = null;
                    }
                }
                exc = this.f1435e;
            } catch (Throwable th) {
                throw th;
            }
        }
        return exc;
    }

    public Object j() {
        Object obj;
        synchronized (this.f1431a) {
            obj = this.f1434d;
        }
        return obj;
    }

    public boolean l() {
        boolean z3;
        synchronized (this.f1431a) {
            z3 = this.f1433c;
        }
        return z3;
    }

    public boolean m() {
        boolean z3;
        synchronized (this.f1431a) {
            z3 = this.f1432b;
        }
        return z3;
    }

    public boolean n() {
        boolean z3;
        synchronized (this.f1431a) {
            z3 = i() != null;
        }
        return z3;
    }

    boolean p() {
        synchronized (this.f1431a) {
            try {
                if (this.f1432b) {
                    return false;
                }
                this.f1432b = true;
                this.f1433c = true;
                this.f1431a.notifyAll();
                o();
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    boolean q(Exception exc) {
        synchronized (this.f1431a) {
            try {
                if (this.f1432b) {
                    return false;
                }
                this.f1432b = true;
                this.f1435e = exc;
                this.f1436f = false;
                this.f1431a.notifyAll();
                o();
                if (!this.f1436f) {
                    k();
                }
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    boolean r(Object obj) {
        synchronized (this.f1431a) {
            try {
                if (this.f1432b) {
                    return false;
                }
                this.f1432b = true;
                this.f1434d = obj;
                this.f1431a.notifyAll();
                o();
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private f(Object obj) {
        r(obj);
    }

    private f(boolean z3) {
        if (z3) {
            p();
        } else {
            r(null);
        }
    }
}
