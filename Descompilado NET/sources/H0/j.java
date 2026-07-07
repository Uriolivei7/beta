package H0;

import b0.AbstractC0306a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f289h = new a(null);

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final Class f290i = j.class;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final S.k f291a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final a0.i f292b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final a0.l f293c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Executor f294d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Executor f295e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final t f296f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final C f297g;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public j(S.k kVar, a0.i iVar, a0.l lVar, Executor executor, Executor executor2, t tVar) {
        D2.h.f(kVar, "fileCache");
        D2.h.f(iVar, "pooledByteBufferFactory");
        D2.h.f(lVar, "pooledByteStreams");
        D2.h.f(executor, "readExecutor");
        D2.h.f(executor2, "writeExecutor");
        D2.h.f(tVar, "imageCacheStatsTracker");
        this.f291a = kVar;
        this.f292b = iVar;
        this.f293c = lVar;
        this.f294d = executor;
        this.f295e = executor2;
        this.f296f = tVar;
        C cD = C.d();
        D2.h.e(cD, "getInstance(...)");
        this.f297g = cD;
    }

    private final boolean g(R.d dVar) {
        O0.j jVarC = this.f297g.c(dVar);
        if (jVarC != null) {
            jVarC.close();
            Y.a.y(f290i, "Found image for %s in staging area", dVar.c());
            this.f296f.e(dVar);
            return true;
        }
        Y.a.y(f290i, "Did not find image for %s in staging area", dVar.c());
        this.f296f.d(dVar);
        try {
            return this.f291a.f(dVar);
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Void i(Object obj, j jVar) {
        D2.h.f(jVar, "this$0");
        Object objE = P0.a.e(obj, null);
        try {
            jVar.f297g.a();
            jVar.f291a.a();
            return null;
        } finally {
        }
    }

    private final O.f l(R.d dVar, O0.j jVar) {
        Y.a.y(f290i, "Found image for %s in staging area", dVar.c());
        this.f296f.e(dVar);
        O.f fVarH = O.f.h(jVar);
        D2.h.e(fVarH, "forResult(...)");
        return fVarH;
    }

    private final O.f n(final R.d dVar, final AtomicBoolean atomicBoolean) {
        try {
            final Object objD = P0.a.d("BufferedDiskCache_getAsync");
            return O.f.b(new Callable() { // from class: H0.g
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return j.o(objD, atomicBoolean, this, dVar);
                }
            }, this.f294d);
        } catch (Exception e4) {
            Y.a.H(f290i, e4, "Failed to schedule disk-cache read for %s", dVar.c());
            return O.f.g(e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final O0.j o(Object obj, AtomicBoolean atomicBoolean, j jVar, R.d dVar) {
        D2.h.f(atomicBoolean, "$isCancelled");
        D2.h.f(jVar, "this$0");
        D2.h.f(dVar, "$key");
        Object objE = P0.a.e(obj, null);
        try {
            if (atomicBoolean.get()) {
                throw new CancellationException();
            }
            O0.j jVarC = jVar.f297g.c(dVar);
            if (jVarC != null) {
                Y.a.y(f290i, "Found image for %s in staging area", dVar.c());
                jVar.f296f.e(dVar);
            } else {
                Y.a.y(f290i, "Did not find image for %s in staging area", dVar.c());
                jVar.f296f.d(dVar);
                try {
                    a0.h hVarR = jVar.r(dVar);
                    if (hVarR == null) {
                        return null;
                    }
                    AbstractC0306a abstractC0306aD0 = AbstractC0306a.d0(hVarR);
                    D2.h.e(abstractC0306aD0, "of(...)");
                    try {
                        jVarC = new O0.j(abstractC0306aD0);
                    } finally {
                        AbstractC0306a.D(abstractC0306aD0);
                    }
                } catch (Exception unused) {
                    return null;
                }
            }
            if (!Thread.interrupted()) {
                return jVarC;
            }
            Y.a.x(f290i, "Host thread was interrupted, decreasing reference count");
            jVarC.close();
            throw new InterruptedException();
        } catch (Throwable th) {
            try {
                P0.a.c(obj, th);
                throw th;
            } finally {
                P0.a.f(objE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void q(Object obj, j jVar, R.d dVar, O0.j jVar2) {
        D2.h.f(jVar, "this$0");
        D2.h.f(dVar, "$key");
        Object objE = P0.a.e(obj, null);
        try {
            jVar.u(dVar, jVar2);
        } finally {
        }
    }

    private final a0.h r(R.d dVar) throws IOException {
        try {
            Class cls = f290i;
            Y.a.y(cls, "Disk cache read for %s", dVar.c());
            Q.a aVarD = this.f291a.d(dVar);
            if (aVarD == null) {
                Y.a.y(cls, "Disk cache miss for %s", dVar.c());
                this.f296f.m(dVar);
                return null;
            }
            Y.a.y(cls, "Found entry in disk cache for %s", dVar.c());
            this.f296f.j(dVar);
            InputStream inputStreamA = aVarD.a();
            try {
                a0.h hVarA = this.f292b.a(inputStreamA, (int) aVarD.size());
                inputStreamA.close();
                Y.a.y(cls, "Successful read from disk cache for %s", dVar.c());
                return hVarA;
            } catch (Throwable th) {
                inputStreamA.close();
                throw th;
            }
        } catch (IOException e4) {
            Y.a.H(f290i, e4, "Exception reading from cache for %s", dVar.c());
            this.f296f.g(dVar);
            throw e4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Void t(Object obj, j jVar, R.d dVar) {
        D2.h.f(jVar, "this$0");
        D2.h.f(dVar, "$key");
        Object objE = P0.a.e(obj, null);
        try {
            jVar.f297g.g(dVar);
            jVar.f291a.g(dVar);
            return null;
        } finally {
        }
    }

    private final void u(R.d dVar, final O0.j jVar) {
        Class cls = f290i;
        Y.a.y(cls, "About to write to disk-cache for key %s", dVar.c());
        try {
            this.f291a.c(dVar, new R.j() { // from class: H0.i
                @Override // R.j
                public final void a(OutputStream outputStream) {
                    j.v(jVar, this, outputStream);
                }
            });
            this.f296f.f(dVar);
            Y.a.y(cls, "Successful disk-cache write for key %s", dVar.c());
        } catch (IOException e4) {
            Y.a.H(f290i, e4, "Failed to write to disk-cache for key %s", dVar.c());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void v(O0.j jVar, j jVar2, OutputStream outputStream) {
        D2.h.f(jVar2, "this$0");
        D2.h.f(outputStream, "os");
        D2.h.c(jVar);
        InputStream inputStreamP = jVar.P();
        if (inputStreamP == null) {
            throw new IllegalStateException("Required value was null.");
        }
        jVar2.f293c.a(inputStreamP, outputStream);
    }

    public final void f(R.d dVar) {
        D2.h.f(dVar, "key");
        this.f291a.e(dVar);
    }

    public final O.f h() {
        this.f297g.a();
        final Object objD = P0.a.d("BufferedDiskCache_clearAll");
        try {
            return O.f.b(new Callable() { // from class: H0.f
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return j.i(objD, this);
                }
            }, this.f295e);
        } catch (Exception e4) {
            Y.a.H(f290i, e4, "Failed to schedule disk-cache clear", new Object[0]);
            return O.f.g(e4);
        }
    }

    public final boolean j(R.d dVar) {
        D2.h.f(dVar, "key");
        return this.f297g.b(dVar) || this.f291a.b(dVar);
    }

    public final boolean k(R.d dVar) {
        D2.h.f(dVar, "key");
        if (j(dVar)) {
            return true;
        }
        return g(dVar);
    }

    public final O.f m(R.d dVar, AtomicBoolean atomicBoolean) {
        O.f fVarN;
        O.f fVarL;
        D2.h.f(dVar, "key");
        D2.h.f(atomicBoolean, "isCancelled");
        if (!V0.b.d()) {
            O0.j jVarC = this.f297g.c(dVar);
            return (jVarC == null || (fVarL = l(dVar, jVarC)) == null) ? n(dVar, atomicBoolean) : fVarL;
        }
        V0.b.a("BufferedDiskCache#get");
        try {
            O0.j jVarC2 = this.f297g.c(dVar);
            if (jVarC2 == null || (fVarN = l(dVar, jVarC2)) == null) {
                fVarN = n(dVar, atomicBoolean);
            }
            V0.b.b();
            return fVarN;
        } catch (Throwable th) {
            V0.b.b();
            throw th;
        }
    }

    public final void p(final R.d dVar, O0.j jVar) {
        D2.h.f(dVar, "key");
        D2.h.f(jVar, "encodedImage");
        if (!V0.b.d()) {
            if (!O0.j.w0(jVar)) {
                throw new IllegalStateException("Check failed.");
            }
            this.f297g.f(dVar, jVar);
            final O0.j jVarI = O0.j.i(jVar);
            try {
                final Object objD = P0.a.d("BufferedDiskCache_putAsync");
                this.f295e.execute(new Runnable() { // from class: H0.h
                    @Override // java.lang.Runnable
                    public final void run() {
                        j.q(objD, this, dVar, jVarI);
                    }
                });
                return;
            } catch (Exception e4) {
                Y.a.H(f290i, e4, "Failed to schedule disk-cache write for %s", dVar.c());
                this.f297g.h(dVar, jVar);
                O0.j.o(jVarI);
                return;
            }
        }
        V0.b.a("BufferedDiskCache#put");
        try {
            if (!O0.j.w0(jVar)) {
                throw new IllegalStateException("Check failed.");
            }
            this.f297g.f(dVar, jVar);
            final O0.j jVarI2 = O0.j.i(jVar);
            try {
                final Object objD2 = P0.a.d("BufferedDiskCache_putAsync");
                this.f295e.execute(new Runnable() { // from class: H0.h
                    @Override // java.lang.Runnable
                    public final void run() {
                        j.q(objD2, this, dVar, jVarI2);
                    }
                });
            } catch (Exception e5) {
                Y.a.H(f290i, e5, "Failed to schedule disk-cache write for %s", dVar.c());
                this.f297g.h(dVar, jVar);
                O0.j.o(jVarI2);
            }
            r2.r rVar = r2.r.f10584a;
        } finally {
            V0.b.b();
        }
    }

    public final O.f s(final R.d dVar) {
        D2.h.f(dVar, "key");
        this.f297g.g(dVar);
        try {
            final Object objD = P0.a.d("BufferedDiskCache_remove");
            return O.f.b(new Callable() { // from class: H0.e
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return j.t(objD, this, dVar);
                }
            }, this.f295e);
        } catch (Exception e4) {
            Y.a.H(f290i, e4, "Failed to schedule disk-cache remove for %s", dVar.c());
            return O.f.g(e4);
        }
    }
}
