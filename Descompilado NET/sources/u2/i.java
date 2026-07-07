package U2;

import M2.t;
import b3.C0324g;
import b3.D;
import b3.F;
import b3.G;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final a f2619o = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private long f2620a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f2621b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f2622c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f2623d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final ArrayDeque f2624e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f2625f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final c f2626g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final b f2627h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final d f2628i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final d f2629j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private U2.b f2630k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private IOException f2631l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final int f2632m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final f f2633n;

    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public final class c implements F {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final b3.i f2639b = new b3.i();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final b3.i f2640c = new b3.i();

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private t f2641d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f2642e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final long f2643f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private boolean f2644g;

        public c(long j3, boolean z3) {
            this.f2643f = j3;
            this.f2644g = z3;
        }

        private final void y(long j3) {
            i iVar = i.this;
            if (!N2.c.f1409h || !Thread.holdsLock(iVar)) {
                i.this.g().X0(j3);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append(iVar);
            throw new AssertionError(sb.toString());
        }

        public final boolean a() {
            return this.f2642e;
        }

        @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            long jF0;
            synchronized (i.this) {
                this.f2642e = true;
                jF0 = this.f2640c.F0();
                this.f2640c.v();
                i iVar = i.this;
                if (iVar == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.Object");
                }
                iVar.notifyAll();
                r rVar = r.f10584a;
            }
            if (jF0 > 0) {
                y(jF0);
            }
            i.this.b();
        }

        @Override // b3.F
        public G f() {
            return i.this.m();
        }

        public final boolean i() {
            return this.f2644g;
        }

        public final void o(b3.k kVar, long j3) throws EOFException {
            boolean z3;
            boolean z4;
            long jF0;
            D2.h.f(kVar, "source");
            i iVar = i.this;
            if (N2.c.f1409h && Thread.holdsLock(iVar)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Thread ");
                Thread threadCurrentThread = Thread.currentThread();
                D2.h.e(threadCurrentThread, "Thread.currentThread()");
                sb.append(threadCurrentThread.getName());
                sb.append(" MUST NOT hold lock on ");
                sb.append(iVar);
                throw new AssertionError(sb.toString());
            }
            while (j3 > 0) {
                synchronized (i.this) {
                    z3 = this.f2644g;
                    z4 = this.f2640c.F0() + j3 > this.f2643f;
                    r rVar = r.f10584a;
                }
                if (z4) {
                    kVar.s(j3);
                    i.this.f(U2.b.FLOW_CONTROL_ERROR);
                    return;
                }
                if (z3) {
                    kVar.s(j3);
                    return;
                }
                long jX = kVar.x(this.f2639b, j3);
                if (jX == -1) {
                    throw new EOFException();
                }
                j3 -= jX;
                synchronized (i.this) {
                    try {
                        if (this.f2642e) {
                            jF0 = this.f2639b.F0();
                            this.f2639b.v();
                        } else {
                            boolean z5 = this.f2640c.F0() == 0;
                            this.f2640c.T(this.f2639b);
                            if (z5) {
                                i iVar2 = i.this;
                                if (iVar2 == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type java.lang.Object");
                                }
                                iVar2.notifyAll();
                            }
                            jF0 = 0;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (jF0 > 0) {
                    y(jF0);
                }
            }
        }

        public final void q(boolean z3) {
            this.f2644g = z3;
        }

        public final void v(t tVar) {
            this.f2641d = tVar;
        }

        /* JADX WARN: Finally extract failed */
        @Override // b3.F
        public long x(b3.i iVar, long j3) throws IOException {
            IOException iOExceptionI;
            long jX;
            boolean z3;
            D2.h.f(iVar, "sink");
            long j4 = 0;
            if (!(j3 >= 0)) {
                throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
            }
            while (true) {
                synchronized (i.this) {
                    i.this.m().r();
                    try {
                        if (i.this.h() != null) {
                            iOExceptionI = i.this.i();
                            if (iOExceptionI == null) {
                                U2.b bVarH = i.this.h();
                                D2.h.c(bVarH);
                                iOExceptionI = new n(bVarH);
                            }
                        } else {
                            iOExceptionI = null;
                        }
                        if (this.f2642e) {
                            throw new IOException("stream closed");
                        }
                        if (this.f2640c.F0() > j4) {
                            b3.i iVar2 = this.f2640c;
                            jX = iVar2.x(iVar, Math.min(j3, iVar2.F0()));
                            i iVar3 = i.this;
                            iVar3.A(iVar3.l() + jX);
                            long jL = i.this.l() - i.this.k();
                            if (iOExceptionI == null && jL >= i.this.g().C0().c() / 2) {
                                i.this.g().d1(i.this.j(), jL);
                                i iVar4 = i.this;
                                iVar4.z(iVar4.l());
                            }
                        } else if (this.f2644g || iOExceptionI != null) {
                            jX = -1;
                        } else {
                            i.this.D();
                            jX = -1;
                            z3 = true;
                            i.this.m().y();
                            r rVar = r.f10584a;
                        }
                        z3 = false;
                        i.this.m().y();
                        r rVar2 = r.f10584a;
                    } catch (Throwable th) {
                        i.this.m().y();
                        throw th;
                    }
                }
                if (!z3) {
                    if (jX != -1) {
                        y(jX);
                        return jX;
                    }
                    if (iOExceptionI == null) {
                        return -1L;
                    }
                    D2.h.c(iOExceptionI);
                    throw iOExceptionI;
                }
                j4 = 0;
            }
        }
    }

    public final class d extends C0324g {
        public d() {
        }

        @Override // b3.C0324g
        protected IOException t(IOException iOException) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (iOException != null) {
                socketTimeoutException.initCause(iOException);
            }
            return socketTimeoutException;
        }

        @Override // b3.C0324g
        protected void x() {
            i.this.f(U2.b.CANCEL);
            i.this.g().R0();
        }

        public final void y() throws IOException {
            if (s()) {
                throw t(null);
            }
        }
    }

    public i(int i3, f fVar, boolean z3, boolean z4, t tVar) {
        D2.h.f(fVar, "connection");
        this.f2632m = i3;
        this.f2633n = fVar;
        this.f2623d = fVar.D0().c();
        ArrayDeque arrayDeque = new ArrayDeque();
        this.f2624e = arrayDeque;
        this.f2626g = new c(fVar.C0().c(), z4);
        this.f2627h = new b(z3);
        this.f2628i = new d();
        this.f2629j = new d();
        if (tVar == null) {
            if (!t()) {
                throw new IllegalStateException("remotely-initiated streams should have headers");
            }
        } else {
            if (t()) {
                throw new IllegalStateException("locally-initiated streams shouldn't have headers yet");
            }
            arrayDeque.add(tVar);
        }
    }

    private final boolean e(U2.b bVar, IOException iOException) {
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
        synchronized (this) {
            if (this.f2630k != null) {
                return false;
            }
            if (this.f2626g.i() && this.f2627h.o()) {
                return false;
            }
            this.f2630k = bVar;
            this.f2631l = iOException;
            notifyAll();
            r rVar = r.f10584a;
            this.f2633n.Q0(this.f2632m);
            return true;
        }
    }

    public final void A(long j3) {
        this.f2620a = j3;
    }

    public final void B(long j3) {
        this.f2622c = j3;
    }

    public final synchronized t C() {
        Object objRemoveFirst;
        this.f2628i.r();
        while (this.f2624e.isEmpty() && this.f2630k == null) {
            try {
                D();
            } catch (Throwable th) {
                this.f2628i.y();
                throw th;
            }
        }
        this.f2628i.y();
        if (this.f2624e.isEmpty()) {
            IOException iOException = this.f2631l;
            if (iOException != null) {
                throw iOException;
            }
            U2.b bVar = this.f2630k;
            D2.h.c(bVar);
            throw new n(bVar);
        }
        objRemoveFirst = this.f2624e.removeFirst();
        D2.h.e(objRemoveFirst, "headersQueue.removeFirst()");
        return (t) objRemoveFirst;
    }

    public final void D() throws InterruptedIOException {
        try {
            wait();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
        }
    }

    public final G E() {
        return this.f2629j;
    }

    public final void a(long j3) {
        this.f2623d += j3;
        if (j3 > 0) {
            notifyAll();
        }
    }

    public final void b() {
        boolean z3;
        boolean zU;
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
        synchronized (this) {
            try {
                z3 = !this.f2626g.i() && this.f2626g.a() && (this.f2627h.o() || this.f2627h.i());
                zU = u();
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
        if (z3) {
            d(U2.b.CANCEL, null);
        } else {
            if (zU) {
                return;
            }
            this.f2633n.Q0(this.f2632m);
        }
    }

    public final void c() throws IOException {
        if (this.f2627h.i()) {
            throw new IOException("stream closed");
        }
        if (this.f2627h.o()) {
            throw new IOException("stream finished");
        }
        if (this.f2630k != null) {
            IOException iOException = this.f2631l;
            if (iOException != null) {
                throw iOException;
            }
            U2.b bVar = this.f2630k;
            D2.h.c(bVar);
            throw new n(bVar);
        }
    }

    public final void d(U2.b bVar, IOException iOException) {
        D2.h.f(bVar, "rstStatusCode");
        if (e(bVar, iOException)) {
            this.f2633n.b1(this.f2632m, bVar);
        }
    }

    public final void f(U2.b bVar) {
        D2.h.f(bVar, "errorCode");
        if (e(bVar, null)) {
            this.f2633n.c1(this.f2632m, bVar);
        }
    }

    public final f g() {
        return this.f2633n;
    }

    public final synchronized U2.b h() {
        return this.f2630k;
    }

    public final IOException i() {
        return this.f2631l;
    }

    public final int j() {
        return this.f2632m;
    }

    public final long k() {
        return this.f2621b;
    }

    public final long l() {
        return this.f2620a;
    }

    public final d m() {
        return this.f2628i;
    }

    public final D n() {
        synchronized (this) {
            try {
                if (!(this.f2625f || t())) {
                    throw new IllegalStateException("reply before requesting the sink");
                }
                r rVar = r.f10584a;
            } finally {
            }
        }
        return this.f2627h;
    }

    public final b o() {
        return this.f2627h;
    }

    public final c p() {
        return this.f2626g;
    }

    public final long q() {
        return this.f2623d;
    }

    public final long r() {
        return this.f2622c;
    }

    public final d s() {
        return this.f2629j;
    }

    public final boolean t() {
        return this.f2633n.x0() == ((this.f2632m & 1) == 1);
    }

    public final synchronized boolean u() {
        try {
            if (this.f2630k != null) {
                return false;
            }
            if (this.f2626g.i() || this.f2626g.a()) {
                if (this.f2627h.o() || this.f2627h.i()) {
                    if (this.f2625f) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public final G v() {
        return this.f2628i;
    }

    public final void w(b3.k kVar, int i3) {
        D2.h.f(kVar, "source");
        if (!N2.c.f1409h || !Thread.holdsLock(this)) {
            this.f2626g.o(kVar, i3);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread threadCurrentThread = Thread.currentThread();
        D2.h.e(threadCurrentThread, "Thread.currentThread()");
        sb.append(threadCurrentThread.getName());
        sb.append(" MUST NOT hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    public final void x(t tVar, boolean z3) {
        boolean zU;
        D2.h.f(tVar, "headers");
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
        synchronized (this) {
            try {
                if (this.f2625f && z3) {
                    this.f2626g.v(tVar);
                } else {
                    this.f2625f = true;
                    this.f2624e.add(tVar);
                }
                if (z3) {
                    this.f2626g.q(true);
                }
                zU = u();
                notifyAll();
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
        if (zU) {
            return;
        }
        this.f2633n.Q0(this.f2632m);
    }

    public final synchronized void y(U2.b bVar) {
        D2.h.f(bVar, "errorCode");
        if (this.f2630k == null) {
            this.f2630k = bVar;
            notifyAll();
        }
    }

    public final void z(long j3) {
        this.f2621b = j3;
    }

    public final class b implements D {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final b3.i f2634b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private t f2635c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f2636d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f2637e;

        public b(boolean z3) {
            this.f2637e = z3;
            this.f2634b = new b3.i();
        }

        private final void a(boolean z3) throws IOException {
            long jMin;
            boolean z4;
            synchronized (i.this) {
                try {
                    i.this.s().r();
                    while (i.this.r() >= i.this.q() && !this.f2637e && !this.f2636d && i.this.h() == null) {
                        try {
                            i.this.D();
                        } finally {
                        }
                    }
                    i.this.s().y();
                    i.this.c();
                    jMin = Math.min(i.this.q() - i.this.r(), this.f2634b.F0());
                    i iVar = i.this;
                    iVar.B(iVar.r() + jMin);
                    z4 = z3 && jMin == this.f2634b.F0() && i.this.h() == null;
                    r rVar = r.f10584a;
                } catch (Throwable th) {
                    throw th;
                }
            }
            i.this.s().r();
            try {
                i.this.g().Y0(i.this.j(), z4, this.f2634b, jMin);
            } finally {
            }
        }

        @Override // b3.D
        public void Q(b3.i iVar, long j3) throws IOException {
            D2.h.f(iVar, "source");
            i iVar2 = i.this;
            if (!N2.c.f1409h || !Thread.holdsLock(iVar2)) {
                this.f2634b.Q(iVar, j3);
                while (this.f2634b.F0() >= 16384) {
                    a(false);
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append(iVar2);
            throw new AssertionError(sb.toString());
        }

        @Override // b3.D, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            i iVar = i.this;
            if (N2.c.f1409h && Thread.holdsLock(iVar)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Thread ");
                Thread threadCurrentThread = Thread.currentThread();
                D2.h.e(threadCurrentThread, "Thread.currentThread()");
                sb.append(threadCurrentThread.getName());
                sb.append(" MUST NOT hold lock on ");
                sb.append(iVar);
                throw new AssertionError(sb.toString());
            }
            synchronized (i.this) {
                if (this.f2636d) {
                    return;
                }
                boolean z3 = i.this.h() == null;
                r rVar = r.f10584a;
                if (!i.this.o().f2637e) {
                    boolean z4 = this.f2634b.F0() > 0;
                    if (this.f2635c != null) {
                        while (this.f2634b.F0() > 0) {
                            a(false);
                        }
                        f fVarG = i.this.g();
                        int iJ = i.this.j();
                        t tVar = this.f2635c;
                        D2.h.c(tVar);
                        fVarG.Z0(iJ, z3, N2.c.L(tVar));
                    } else if (z4) {
                        while (this.f2634b.F0() > 0) {
                            a(true);
                        }
                    } else if (z3) {
                        i.this.g().Y0(i.this.j(), true, null, 0L);
                    }
                }
                synchronized (i.this) {
                    this.f2636d = true;
                    r rVar2 = r.f10584a;
                }
                i.this.g().flush();
                i.this.b();
            }
        }

        @Override // b3.D
        public G f() {
            return i.this.s();
        }

        @Override // b3.D, java.io.Flushable
        public void flush() throws IOException {
            i iVar = i.this;
            if (N2.c.f1409h && Thread.holdsLock(iVar)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Thread ");
                Thread threadCurrentThread = Thread.currentThread();
                D2.h.e(threadCurrentThread, "Thread.currentThread()");
                sb.append(threadCurrentThread.getName());
                sb.append(" MUST NOT hold lock on ");
                sb.append(iVar);
                throw new AssertionError(sb.toString());
            }
            synchronized (i.this) {
                i.this.c();
                r rVar = r.f10584a;
            }
            while (this.f2634b.F0() > 0) {
                a(false);
                i.this.g().flush();
            }
        }

        public final boolean i() {
            return this.f2636d;
        }

        public final boolean o() {
            return this.f2637e;
        }

        public /* synthetic */ b(i iVar, boolean z3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            this((i3 & 1) != 0 ? false : z3);
        }
    }
}
