package R2;

import R2.e;
import java.lang.ref.Reference;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f2184f = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final long f2185a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Q2.d f2186b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final b f2187c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ConcurrentLinkedQueue f2188d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f2189e;

    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final class b extends Q2.a {
        b(String str) {
            super(str, false, 2, null);
        }

        @Override // Q2.a
        public long f() {
            return h.this.b(System.nanoTime());
        }
    }

    public h(Q2.e eVar, int i3, long j3, TimeUnit timeUnit) {
        D2.h.f(eVar, "taskRunner");
        D2.h.f(timeUnit, "timeUnit");
        this.f2189e = i3;
        this.f2185a = timeUnit.toNanos(j3);
        this.f2186b = eVar.i();
        this.f2187c = new b(N2.c.f1410i + " ConnectionPool");
        this.f2188d = new ConcurrentLinkedQueue();
        if (j3 > 0) {
            return;
        }
        throw new IllegalArgumentException(("keepAliveDuration <= 0: " + j3).toString());
    }

    private final int d(f fVar, long j3) {
        if (N2.c.f1409h && !Thread.holdsLock(fVar)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(fVar);
            throw new AssertionError(sb.toString());
        }
        List listN = fVar.n();
        int i3 = 0;
        while (i3 < listN.size()) {
            Reference reference = (Reference) listN.get(i3);
            if (reference.get() != null) {
                i3++;
            } else {
                W2.j.f2732c.g().m("A connection to " + fVar.A().a().l() + " was leaked. Did you forget to close a response body?", ((e.b) reference).a());
                listN.remove(i3);
                fVar.D(true);
                if (listN.isEmpty()) {
                    fVar.C(j3 - this.f2185a);
                    return 0;
                }
            }
        }
        return listN.size();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002d A[Catch: all -> 0x002b, TryCatch #0 {all -> 0x002b, blocks: (B:8:0x0024, B:15:0x0033, B:13:0x002d, B:18:0x0037), top: B:26:0x0024 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean a(M2.C0190a r4, R2.e r5, java.util.List r6, boolean r7) {
        /*
            r3 = this;
            java.lang.String r0 = "address"
            D2.h.f(r4, r0)
            java.lang.String r0 = "call"
            D2.h.f(r5, r0)
            java.util.concurrent.ConcurrentLinkedQueue r0 = r3.f2188d
            java.util.Iterator r0 = r0.iterator()
        L10:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L3f
            java.lang.Object r1 = r0.next()
            R2.f r1 = (R2.f) r1
            java.lang.String r2 = "connection"
            D2.h.e(r1, r2)
            monitor-enter(r1)
            if (r7 == 0) goto L2d
            boolean r2 = r1.v()     // Catch: java.lang.Throwable -> L2b
            if (r2 != 0) goto L2d
            goto L33
        L2b:
            r4 = move-exception
            goto L3d
        L2d:
            boolean r2 = r1.t(r4, r6)     // Catch: java.lang.Throwable -> L2b
            if (r2 != 0) goto L37
        L33:
            r2.r r2 = r2.r.f10584a     // Catch: java.lang.Throwable -> L2b
            monitor-exit(r1)
            goto L10
        L37:
            r5.d(r1)     // Catch: java.lang.Throwable -> L2b
            monitor-exit(r1)
            r4 = 1
            return r4
        L3d:
            monitor-exit(r1)
            throw r4
        L3f:
            r4 = 0
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: R2.h.a(M2.a, R2.e, java.util.List, boolean):boolean");
    }

    public final long b(long j3) {
        int i3 = 0;
        long j4 = Long.MIN_VALUE;
        f fVar = null;
        int i4 = 0;
        for (f fVar2 : this.f2188d) {
            D2.h.e(fVar2, "connection");
            synchronized (fVar2) {
                try {
                    if (d(fVar2, j3) > 0) {
                        i4++;
                    } else {
                        i3++;
                        long jO = j3 - fVar2.o();
                        if (jO > j4) {
                            r rVar = r.f10584a;
                            fVar = fVar2;
                            j4 = jO;
                        } else {
                            r rVar2 = r.f10584a;
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        long j5 = this.f2185a;
        if (j4 < j5 && i3 <= this.f2189e) {
            if (i3 > 0) {
                return j5 - j4;
            }
            if (i4 > 0) {
                return j5;
            }
            return -1L;
        }
        D2.h.c(fVar);
        synchronized (fVar) {
            if (!fVar.n().isEmpty()) {
                return 0L;
            }
            if (fVar.o() + j4 != j3) {
                return 0L;
            }
            fVar.D(true);
            this.f2188d.remove(fVar);
            N2.c.k(fVar.E());
            if (this.f2188d.isEmpty()) {
                this.f2186b.a();
            }
            return 0L;
        }
    }

    public final boolean c(f fVar) {
        D2.h.f(fVar, "connection");
        if (N2.c.f1409h && !Thread.holdsLock(fVar)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(fVar);
            throw new AssertionError(sb.toString());
        }
        if (!fVar.p() && this.f2189e != 0) {
            Q2.d.j(this.f2186b, this.f2187c, 0L, 2, null);
            return false;
        }
        fVar.D(true);
        this.f2188d.remove(fVar);
        if (this.f2188d.isEmpty()) {
            this.f2186b.a();
        }
        return true;
    }

    public final void e(f fVar) {
        D2.h.f(fVar, "connection");
        if (!N2.c.f1409h || Thread.holdsLock(fVar)) {
            this.f2188d.add(fVar);
            Q2.d.j(this.f2186b, this.f2187c, 0L, 2, null);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread threadCurrentThread = Thread.currentThread();
        D2.h.e(threadCurrentThread, "Thread.currentThread()");
        sb.append(threadCurrentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append(fVar);
        throw new AssertionError(sb.toString());
    }
}
