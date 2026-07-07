package Q2;

import D2.h;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f1864a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private a f1865b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List f1866c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f1867d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final e f1868e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final String f1869f;

    public d(e eVar, String str) {
        h.f(eVar, "taskRunner");
        h.f(str, "name");
        this.f1868e = eVar;
        this.f1869f = str;
        this.f1866c = new ArrayList();
    }

    public static /* synthetic */ void j(d dVar, a aVar, long j3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            j3 = 0;
        }
        dVar.i(aVar, j3);
    }

    public final void a() {
        if (N2.c.f1409h && Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        synchronized (this.f1868e) {
            try {
                if (b()) {
                    this.f1868e.h(this);
                }
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final boolean b() {
        a aVar = this.f1865b;
        if (aVar != null) {
            h.c(aVar);
            if (aVar.a()) {
                this.f1867d = true;
            }
        }
        boolean z3 = false;
        for (int size = this.f1866c.size() - 1; size >= 0; size--) {
            if (((a) this.f1866c.get(size)).a()) {
                a aVar2 = (a) this.f1866c.get(size);
                if (e.f1872j.a().isLoggable(Level.FINE)) {
                    b.c(aVar2, this, "canceled");
                }
                this.f1866c.remove(size);
                z3 = true;
            }
        }
        return z3;
    }

    public final a c() {
        return this.f1865b;
    }

    public final boolean d() {
        return this.f1867d;
    }

    public final List e() {
        return this.f1866c;
    }

    public final String f() {
        return this.f1869f;
    }

    public final boolean g() {
        return this.f1864a;
    }

    public final e h() {
        return this.f1868e;
    }

    public final void i(a aVar, long j3) {
        h.f(aVar, "task");
        synchronized (this.f1868e) {
            if (!this.f1864a) {
                if (k(aVar, j3, false)) {
                    this.f1868e.h(this);
                }
                r rVar = r.f10584a;
            } else if (aVar.a()) {
                if (e.f1872j.a().isLoggable(Level.FINE)) {
                    b.c(aVar, this, "schedule canceled (queue is shutdown)");
                }
            } else {
                if (e.f1872j.a().isLoggable(Level.FINE)) {
                    b.c(aVar, this, "schedule failed (queue is shutdown)");
                }
                throw new RejectedExecutionException();
            }
        }
    }

    public final boolean k(a aVar, long j3, boolean z3) {
        String str;
        h.f(aVar, "task");
        aVar.e(this);
        long jC = this.f1868e.g().c();
        long j4 = jC + j3;
        int iIndexOf = this.f1866c.indexOf(aVar);
        if (iIndexOf != -1) {
            if (aVar.c() <= j4) {
                if (e.f1872j.a().isLoggable(Level.FINE)) {
                    b.c(aVar, this, "already scheduled");
                }
                return false;
            }
            this.f1866c.remove(iIndexOf);
        }
        aVar.g(j4);
        if (e.f1872j.a().isLoggable(Level.FINE)) {
            if (z3) {
                str = "run again after " + b.b(j4 - jC);
            } else {
                str = "scheduled after " + b.b(j4 - jC);
            }
            b.c(aVar, this, str);
        }
        Iterator it = this.f1866c.iterator();
        int size = 0;
        while (true) {
            if (!it.hasNext()) {
                size = -1;
                break;
            }
            if (((a) it.next()).c() - jC > j3) {
                break;
            }
            size++;
        }
        if (size == -1) {
            size = this.f1866c.size();
        }
        this.f1866c.add(size, aVar);
        return size == 0;
    }

    public final void l(a aVar) {
        this.f1865b = aVar;
    }

    public final void m(boolean z3) {
        this.f1867d = z3;
    }

    public final void n() {
        if (N2.c.f1409h && Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        synchronized (this.f1868e) {
            try {
                this.f1864a = true;
                if (b()) {
                    this.f1868e.h(this);
                }
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public String toString() {
        return this.f1869f;
    }
}
