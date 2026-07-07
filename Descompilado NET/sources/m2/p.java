package M2;

import R2.e;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class p {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f1205a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f1206b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Runnable f1207c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private ExecutorService f1208d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final ArrayDeque f1209e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final ArrayDeque f1210f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final ArrayDeque f1211g;

    public p() {
        this.f1205a = 64;
        this.f1206b = 5;
        this.f1209e = new ArrayDeque();
        this.f1210f = new ArrayDeque();
        this.f1211g = new ArrayDeque();
    }

    private final e.a e(String str) {
        for (e.a aVar : this.f1210f) {
            if (D2.h.b(aVar.d(), str)) {
                return aVar;
            }
        }
        for (e.a aVar2 : this.f1209e) {
            if (D2.h.b(aVar2.d(), str)) {
                return aVar2;
            }
        }
        return null;
    }

    private final void f(Deque deque, Object obj) {
        Runnable runnable;
        synchronized (this) {
            if (!deque.remove(obj)) {
                throw new AssertionError("Call wasn't in-flight!");
            }
            runnable = this.f1207c;
            r2.r rVar = r2.r.f10584a;
        }
        if (i() || runnable == null) {
            return;
        }
        runnable.run();
    }

    private final boolean i() {
        int i3;
        boolean z3;
        if (N2.c.f1409h && Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        ArrayList arrayList = new ArrayList();
        synchronized (this) {
            try {
                Iterator it = this.f1209e.iterator();
                D2.h.e(it, "readyAsyncCalls.iterator()");
                while (it.hasNext()) {
                    e.a aVar = (e.a) it.next();
                    if (this.f1210f.size() >= this.f1205a) {
                        break;
                    }
                    if (aVar.c().get() < this.f1206b) {
                        it.remove();
                        aVar.c().incrementAndGet();
                        D2.h.e(aVar, "asyncCall");
                        arrayList.add(aVar);
                        this.f1210f.add(aVar);
                    }
                }
                z3 = l() > 0;
                r2.r rVar = r2.r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
        int size = arrayList.size();
        for (i3 = 0; i3 < size; i3++) {
            ((e.a) arrayList.get(i3)).a(d());
        }
        return z3;
    }

    public final ExecutorService a() {
        return d();
    }

    public final void b(e.a aVar) {
        e.a aVarE;
        D2.h.f(aVar, "call");
        synchronized (this) {
            try {
                this.f1209e.add(aVar);
                if (!aVar.b().p() && (aVarE = e(aVar.d())) != null) {
                    aVar.e(aVarE);
                }
                r2.r rVar = r2.r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
        i();
    }

    public final synchronized void c(R2.e eVar) {
        D2.h.f(eVar, "call");
        this.f1211g.add(eVar);
    }

    public final synchronized ExecutorService d() {
        ExecutorService executorService;
        try {
            if (this.f1208d == null) {
                this.f1208d = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), N2.c.K(N2.c.f1410i + " Dispatcher", false));
            }
            executorService = this.f1208d;
            D2.h.c(executorService);
        } catch (Throwable th) {
            throw th;
        }
        return executorService;
    }

    public final void g(e.a aVar) {
        D2.h.f(aVar, "call");
        aVar.c().decrementAndGet();
        f(this.f1210f, aVar);
    }

    public final void h(R2.e eVar) {
        D2.h.f(eVar, "call");
        f(this.f1211g, eVar);
    }

    public final synchronized List j() {
        List listUnmodifiableList;
        try {
            ArrayDeque arrayDeque = this.f1209e;
            ArrayList arrayList = new ArrayList(AbstractC0717n.q(arrayDeque, 10));
            Iterator it = arrayDeque.iterator();
            while (it.hasNext()) {
                arrayList.add(((e.a) it.next()).b());
            }
            listUnmodifiableList = Collections.unmodifiableList(arrayList);
            D2.h.e(listUnmodifiableList, "Collections.unmodifiable…yncCalls.map { it.call })");
        } catch (Throwable th) {
            throw th;
        }
        return listUnmodifiableList;
    }

    public final synchronized List k() {
        List listUnmodifiableList;
        try {
            ArrayDeque arrayDeque = this.f1211g;
            ArrayDeque arrayDeque2 = this.f1210f;
            ArrayList arrayList = new ArrayList(AbstractC0717n.q(arrayDeque2, 10));
            Iterator it = arrayDeque2.iterator();
            while (it.hasNext()) {
                arrayList.add(((e.a) it.next()).b());
            }
            listUnmodifiableList = Collections.unmodifiableList(AbstractC0717n.W(arrayDeque, arrayList));
            D2.h.e(listUnmodifiableList, "Collections.unmodifiable…yncCalls.map { it.call })");
        } catch (Throwable th) {
            throw th;
        }
        return listUnmodifiableList;
    }

    public final synchronized int l() {
        return this.f1210f.size() + this.f1211g.size();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public p(ExecutorService executorService) {
        this();
        D2.h.f(executorService, "executorService");
        this.f1208d = executorService;
    }
}
