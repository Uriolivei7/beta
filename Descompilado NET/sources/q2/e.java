package Q2;

import D2.h;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final Logger f1871i;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f1873a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f1874b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f1875c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f1876d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final List f1877e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Runnable f1878f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final a f1879g;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final b f1872j = new b(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final e f1870h = new e(new c(N2.c.K(N2.c.f1410i + " TaskRunner", true)));

    public interface a {
        void a(e eVar, long j3);

        void b(e eVar);

        long c();

        void execute(Runnable runnable);
    }

    public static final class b {
        private b() {
        }

        public final Logger a() {
            return e.f1871i;
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final class c implements a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ThreadPoolExecutor f1880a;

        public c(ThreadFactory threadFactory) {
            h.f(threadFactory, "threadFactory");
            this.f1880a = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), threadFactory);
        }

        @Override // Q2.e.a
        public void a(e eVar, long j3) throws InterruptedException {
            h.f(eVar, "taskRunner");
            long j4 = j3 / 1000000;
            long j5 = j3 - (1000000 * j4);
            if (j4 > 0 || j3 > 0) {
                eVar.wait(j4, (int) j5);
            }
        }

        @Override // Q2.e.a
        public void b(e eVar) {
            h.f(eVar, "taskRunner");
            eVar.notify();
        }

        @Override // Q2.e.a
        public long c() {
            return System.nanoTime();
        }

        @Override // Q2.e.a
        public void execute(Runnable runnable) {
            h.f(runnable, "runnable");
            this.f1880a.execute(runnable);
        }
    }

    public static final class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Q2.a aVarD;
            long jC;
            while (true) {
                synchronized (e.this) {
                    aVarD = e.this.d();
                }
                if (aVarD == null) {
                    return;
                }
                Q2.d dVarD = aVarD.d();
                h.c(dVarD);
                boolean zIsLoggable = e.f1872j.a().isLoggable(Level.FINE);
                if (zIsLoggable) {
                    jC = dVarD.h().g().c();
                    Q2.b.c(aVarD, dVarD, "starting");
                } else {
                    jC = -1;
                }
                try {
                    try {
                        e.this.j(aVarD);
                        r rVar = r.f10584a;
                        if (zIsLoggable) {
                            Q2.b.c(aVarD, dVarD, "finished run in " + Q2.b.b(dVarD.h().g().c() - jC));
                        }
                    } finally {
                    }
                } catch (Throwable th) {
                    if (zIsLoggable) {
                        Q2.b.c(aVarD, dVarD, "failed a run in " + Q2.b.b(dVarD.h().g().c() - jC));
                    }
                    throw th;
                }
            }
        }
    }

    static {
        Logger logger = Logger.getLogger(e.class.getName());
        h.e(logger, "Logger.getLogger(TaskRunner::class.java.name)");
        f1871i = logger;
    }

    public e(a aVar) {
        h.f(aVar, "backend");
        this.f1879g = aVar;
        this.f1873a = 10000;
        this.f1876d = new ArrayList();
        this.f1877e = new ArrayList();
        this.f1878f = new d();
    }

    private final void c(Q2.a aVar, long j3) {
        if (N2.c.f1409h && !Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        Q2.d dVarD = aVar.d();
        h.c(dVarD);
        if (!(dVarD.c() == aVar)) {
            throw new IllegalStateException("Check failed.");
        }
        boolean zD = dVarD.d();
        dVarD.m(false);
        dVarD.l(null);
        this.f1876d.remove(dVarD);
        if (j3 != -1 && !zD && !dVarD.g()) {
            dVarD.k(aVar, j3, true);
        }
        if (dVarD.e().isEmpty()) {
            return;
        }
        this.f1877e.add(dVarD);
    }

    private final void e(Q2.a aVar) {
        if (!N2.c.f1409h || Thread.holdsLock(this)) {
            aVar.g(-1L);
            Q2.d dVarD = aVar.d();
            h.c(dVarD);
            dVarD.e().remove(aVar);
            this.f1877e.remove(dVarD);
            dVarD.l(aVar);
            this.f1876d.add(dVarD);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread threadCurrentThread = Thread.currentThread();
        h.e(threadCurrentThread, "Thread.currentThread()");
        sb.append(threadCurrentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void j(Q2.a aVar) {
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
        Thread threadCurrentThread2 = Thread.currentThread();
        h.e(threadCurrentThread2, "currentThread");
        String name = threadCurrentThread2.getName();
        threadCurrentThread2.setName(aVar.b());
        try {
            long jF = aVar.f();
            synchronized (this) {
                c(aVar, jF);
                r rVar = r.f10584a;
            }
            threadCurrentThread2.setName(name);
        } catch (Throwable th) {
            synchronized (this) {
                c(aVar, -1L);
                r rVar2 = r.f10584a;
                threadCurrentThread2.setName(name);
                throw th;
            }
        }
    }

    public final Q2.a d() {
        boolean z3;
        if (N2.c.f1409h && !Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        while (!this.f1877e.isEmpty()) {
            long jC = this.f1879g.c();
            Iterator it = this.f1877e.iterator();
            long jMin = Long.MAX_VALUE;
            Q2.a aVar = null;
            while (true) {
                if (!it.hasNext()) {
                    z3 = false;
                    break;
                }
                Q2.a aVar2 = (Q2.a) ((Q2.d) it.next()).e().get(0);
                long jMax = Math.max(0L, aVar2.c() - jC);
                if (jMax > 0) {
                    jMin = Math.min(jMax, jMin);
                } else {
                    if (aVar != null) {
                        z3 = true;
                        break;
                    }
                    aVar = aVar2;
                }
            }
            if (aVar != null) {
                e(aVar);
                if (z3 || (!this.f1874b && !this.f1877e.isEmpty())) {
                    this.f1879g.execute(this.f1878f);
                }
                return aVar;
            }
            if (this.f1874b) {
                if (jMin < this.f1875c - jC) {
                    this.f1879g.b(this);
                }
                return null;
            }
            this.f1874b = true;
            this.f1875c = jC + jMin;
            try {
                try {
                    this.f1879g.a(this, jMin);
                } catch (InterruptedException unused) {
                    f();
                }
            } finally {
                this.f1874b = false;
            }
        }
        return null;
    }

    public final void f() {
        for (int size = this.f1876d.size() - 1; size >= 0; size--) {
            ((Q2.d) this.f1876d.get(size)).b();
        }
        for (int size2 = this.f1877e.size() - 1; size2 >= 0; size2--) {
            Q2.d dVar = (Q2.d) this.f1877e.get(size2);
            dVar.b();
            if (dVar.e().isEmpty()) {
                this.f1877e.remove(size2);
            }
        }
    }

    public final a g() {
        return this.f1879g;
    }

    public final void h(Q2.d dVar) {
        h.f(dVar, "taskQueue");
        if (N2.c.f1409h && !Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        if (dVar.c() == null) {
            if (dVar.e().isEmpty()) {
                this.f1877e.remove(dVar);
            } else {
                N2.c.a(this.f1877e, dVar);
            }
        }
        if (this.f1874b) {
            this.f1879g.b(this);
        } else {
            this.f1879g.execute(this.f1878f);
        }
    }

    public final Q2.d i() {
        int i3;
        synchronized (this) {
            i3 = this.f1873a;
            this.f1873a = i3 + 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('Q');
        sb.append(i3);
        return new Q2.d(this, sb.toString());
    }
}
