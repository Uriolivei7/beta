package b3;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: b3.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0324g extends G {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final long f5616i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final long f5617j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static C0324g f5618k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final a f5619l = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f5620f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private C0324g f5621g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private long f5622h;

    /* JADX INFO: renamed from: b3.g$a */
    public static final class a {
        private a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean d(C0324g c0324g) {
            synchronized (C0324g.class) {
                for (C0324g c0324g2 = C0324g.f5618k; c0324g2 != null; c0324g2 = c0324g2.f5621g) {
                    if (c0324g2.f5621g == c0324g) {
                        c0324g2.f5621g = c0324g.f5621g;
                        c0324g.f5621g = null;
                        return false;
                    }
                }
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void e(C0324g c0324g, long j3, boolean z3) {
            synchronized (C0324g.class) {
                try {
                    if (C0324g.f5618k == null) {
                        C0324g.f5618k = new C0324g();
                        new b().start();
                    }
                    long jNanoTime = System.nanoTime();
                    if (j3 != 0 && z3) {
                        c0324g.f5622h = Math.min(j3, c0324g.c() - jNanoTime) + jNanoTime;
                    } else if (j3 != 0) {
                        c0324g.f5622h = j3 + jNanoTime;
                    } else {
                        if (!z3) {
                            throw new AssertionError();
                        }
                        c0324g.f5622h = c0324g.c();
                    }
                    long jU = c0324g.u(jNanoTime);
                    C0324g c0324g2 = C0324g.f5618k;
                    D2.h.c(c0324g2);
                    while (c0324g2.f5621g != null) {
                        C0324g c0324g3 = c0324g2.f5621g;
                        D2.h.c(c0324g3);
                        if (jU < c0324g3.u(jNanoTime)) {
                            break;
                        }
                        c0324g2 = c0324g2.f5621g;
                        D2.h.c(c0324g2);
                    }
                    c0324g.f5621g = c0324g2.f5621g;
                    c0324g2.f5621g = c0324g;
                    if (c0324g2 == C0324g.f5618k) {
                        C0324g.class.notify();
                    }
                    r2.r rVar = r2.r.f10584a;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public final C0324g c() throws InterruptedException {
            C0324g c0324g = C0324g.f5618k;
            D2.h.c(c0324g);
            C0324g c0324g2 = c0324g.f5621g;
            if (c0324g2 == null) {
                long jNanoTime = System.nanoTime();
                C0324g.class.wait(C0324g.f5616i);
                C0324g c0324g3 = C0324g.f5618k;
                D2.h.c(c0324g3);
                if (c0324g3.f5621g != null || System.nanoTime() - jNanoTime < C0324g.f5617j) {
                    return null;
                }
                return C0324g.f5618k;
            }
            long jU = c0324g2.u(System.nanoTime());
            if (jU > 0) {
                long j3 = jU / 1000000;
                C0324g.class.wait(j3, (int) (jU - (1000000 * j3)));
                return null;
            }
            C0324g c0324g4 = C0324g.f5618k;
            D2.h.c(c0324g4);
            c0324g4.f5621g = c0324g2.f5621g;
            c0324g2.f5621g = null;
            return c0324g2;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: renamed from: b3.g$b */
    private static final class b extends Thread {
        public b() {
            super("Okio Watchdog");
            setDaemon(true);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            C0324g c0324gC;
            while (true) {
                try {
                    synchronized (C0324g.class) {
                        c0324gC = C0324g.f5619l.c();
                        if (c0324gC == C0324g.f5618k) {
                            C0324g.f5618k = null;
                            return;
                        }
                        r2.r rVar = r2.r.f10584a;
                    }
                    if (c0324gC != null) {
                        c0324gC.x();
                    }
                } catch (InterruptedException unused) {
                    continue;
                }
            }
        }
    }

    /* JADX INFO: renamed from: b3.g$c */
    public static final class c implements D {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ D f5624c;

        c(D d4) {
            this.f5624c = d4;
        }

        @Override // b3.D
        public void Q(i iVar, long j3) throws IOException {
            D2.h.f(iVar, "source");
            AbstractC0323f.b(iVar.F0(), 0L, j3);
            while (true) {
                long j4 = 0;
                if (j3 <= 0) {
                    return;
                }
                A a4 = iVar.f5627b;
                D2.h.c(a4);
                while (true) {
                    if (j4 >= 65536) {
                        break;
                    }
                    j4 += (long) (a4.f5592c - a4.f5591b);
                    if (j4 >= j3) {
                        j4 = j3;
                        break;
                    } else {
                        a4 = a4.f5595f;
                        D2.h.c(a4);
                    }
                }
                C0324g c0324g = C0324g.this;
                c0324g.r();
                try {
                    this.f5624c.Q(iVar, j4);
                    r2.r rVar = r2.r.f10584a;
                    if (c0324g.s()) {
                        throw c0324g.m(null);
                    }
                    j3 -= j4;
                } catch (IOException e4) {
                    if (!c0324g.s()) {
                        throw e4;
                    }
                    throw c0324g.m(e4);
                } finally {
                    c0324g.s();
                }
            }
        }

        @Override // b3.D
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public C0324g f() {
            return C0324g.this;
        }

        @Override // b3.D, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            C0324g c0324g = C0324g.this;
            c0324g.r();
            try {
                this.f5624c.close();
                r2.r rVar = r2.r.f10584a;
                if (c0324g.s()) {
                    throw c0324g.m(null);
                }
            } catch (IOException e4) {
                if (!c0324g.s()) {
                    throw e4;
                }
                throw c0324g.m(e4);
            } finally {
                c0324g.s();
            }
        }

        @Override // b3.D, java.io.Flushable
        public void flush() throws IOException {
            C0324g c0324g = C0324g.this;
            c0324g.r();
            try {
                this.f5624c.flush();
                r2.r rVar = r2.r.f10584a;
                if (c0324g.s()) {
                    throw c0324g.m(null);
                }
            } catch (IOException e4) {
                if (!c0324g.s()) {
                    throw e4;
                }
                throw c0324g.m(e4);
            } finally {
                c0324g.s();
            }
        }

        public String toString() {
            return "AsyncTimeout.sink(" + this.f5624c + ')';
        }
    }

    /* JADX INFO: renamed from: b3.g$d */
    public static final class d implements F {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ F f5626c;

        d(F f3) {
            this.f5626c = f3;
        }

        @Override // b3.F
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public C0324g f() {
            return C0324g.this;
        }

        @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            C0324g c0324g = C0324g.this;
            c0324g.r();
            try {
                this.f5626c.close();
                r2.r rVar = r2.r.f10584a;
                if (c0324g.s()) {
                    throw c0324g.m(null);
                }
            } catch (IOException e4) {
                if (!c0324g.s()) {
                    throw e4;
                }
                throw c0324g.m(e4);
            } finally {
                c0324g.s();
            }
        }

        public String toString() {
            return "AsyncTimeout.source(" + this.f5626c + ')';
        }

        @Override // b3.F
        public long x(i iVar, long j3) throws IOException {
            D2.h.f(iVar, "sink");
            C0324g c0324g = C0324g.this;
            c0324g.r();
            try {
                long jX = this.f5626c.x(iVar, j3);
                if (c0324g.s()) {
                    throw c0324g.m(null);
                }
                return jX;
            } catch (IOException e4) {
                if (c0324g.s()) {
                    throw c0324g.m(e4);
                }
                throw e4;
            } finally {
                c0324g.s();
            }
        }
    }

    static {
        long millis = TimeUnit.SECONDS.toMillis(60L);
        f5616i = millis;
        f5617j = TimeUnit.MILLISECONDS.toNanos(millis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long u(long j3) {
        return this.f5622h - j3;
    }

    public final IOException m(IOException iOException) {
        return t(iOException);
    }

    public final void r() {
        if (this.f5620f) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        long jH = h();
        boolean zE = e();
        if (jH != 0 || zE) {
            this.f5620f = true;
            f5619l.e(this, jH, zE);
        }
    }

    public final boolean s() {
        if (!this.f5620f) {
            return false;
        }
        this.f5620f = false;
        return f5619l.d(this);
    }

    protected IOException t(IOException iOException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    public final D v(D d4) {
        D2.h.f(d4, "sink");
        return new c(d4);
    }

    public final F w(F f3) {
        D2.h.f(f3, "source");
        return new d(f3);
    }

    protected void x() {
    }
}
