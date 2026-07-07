package R2;

import M2.B;
import M2.C0190a;
import M2.C0196g;
import M2.D;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import M2.p;
import M2.r;
import M2.u;
import M2.z;
import b3.C0324g;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import r2.AbstractC0678a;

/* JADX INFO: loaded from: classes.dex */
public final class e implements InterfaceC0194e {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final h f2135b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final r f2136c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final c f2137d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final AtomicBoolean f2138e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Object f2139f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private d f2140g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private f f2141h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f2142i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private R2.c f2143j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f2144k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f2145l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f2146m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private volatile boolean f2147n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private volatile R2.c f2148o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private volatile f f2149p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final z f2150q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final B f2151r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final boolean f2152s;

    public final class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private volatile AtomicInteger f2153b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final InterfaceC0195f f2154c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ e f2155d;

        public a(e eVar, InterfaceC0195f interfaceC0195f) {
            D2.h.f(interfaceC0195f, "responseCallback");
            this.f2155d = eVar;
            this.f2154c = interfaceC0195f;
            this.f2153b = new AtomicInteger(0);
        }

        public final void a(ExecutorService executorService) {
            D2.h.f(executorService, "executorService");
            p pVarS = this.f2155d.l().s();
            if (N2.c.f1409h && Thread.holdsLock(pVarS)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Thread ");
                Thread threadCurrentThread = Thread.currentThread();
                D2.h.e(threadCurrentThread, "Thread.currentThread()");
                sb.append(threadCurrentThread.getName());
                sb.append(" MUST NOT hold lock on ");
                sb.append(pVarS);
                throw new AssertionError(sb.toString());
            }
            try {
                try {
                    executorService.execute(this);
                } catch (RejectedExecutionException e4) {
                    InterruptedIOException interruptedIOException = new InterruptedIOException("executor rejected");
                    interruptedIOException.initCause(e4);
                    this.f2155d.w(interruptedIOException);
                    this.f2154c.a(this.f2155d, interruptedIOException);
                    this.f2155d.l().s().g(this);
                }
            } catch (Throwable th) {
                this.f2155d.l().s().g(this);
                throw th;
            }
        }

        public final e b() {
            return this.f2155d;
        }

        public final AtomicInteger c() {
            return this.f2153b;
        }

        public final String d() {
            return this.f2155d.s().l().h();
        }

        public final void e(a aVar) {
            D2.h.f(aVar, "other");
            this.f2153b = aVar.f2153b;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z3;
            Throwable th;
            IOException e4;
            p pVarS;
            String str = "OkHttp " + this.f2155d.x();
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "currentThread");
            String name = threadCurrentThread.getName();
            threadCurrentThread.setName(str);
            try {
                this.f2155d.f2137d.r();
                try {
                    try {
                        z3 = true;
                        try {
                            this.f2154c.b(this.f2155d, this.f2155d.t());
                            pVarS = this.f2155d.l().s();
                        } catch (IOException e5) {
                            e4 = e5;
                            if (z3) {
                                W2.j.f2732c.g().k("Callback failure for " + this.f2155d.D(), 4, e4);
                            } else {
                                this.f2154c.a(this.f2155d, e4);
                            }
                            pVarS = this.f2155d.l().s();
                        } catch (Throwable th2) {
                            th = th2;
                            this.f2155d.cancel();
                            if (!z3) {
                                IOException iOException = new IOException("canceled due to " + th);
                                AbstractC0678a.a(iOException, th);
                                this.f2154c.a(this.f2155d, iOException);
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        this.f2155d.l().s().g(this);
                        throw th3;
                    }
                } catch (IOException e6) {
                    z3 = false;
                    e4 = e6;
                } catch (Throwable th4) {
                    z3 = false;
                    th = th4;
                }
                pVarS.g(this);
            } finally {
                threadCurrentThread.setName(name);
            }
        }
    }

    public static final class b extends WeakReference {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Object f2156a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(e eVar, Object obj) {
            super(eVar);
            D2.h.f(eVar, "referent");
            this.f2156a = obj;
        }

        public final Object a() {
            return this.f2156a;
        }
    }

    public static final class c extends C0324g {
        c() {
        }

        @Override // b3.C0324g
        protected void x() {
            e.this.cancel();
        }
    }

    public e(z zVar, B b4, boolean z3) {
        D2.h.f(zVar, "client");
        D2.h.f(b4, "originalRequest");
        this.f2150q = zVar;
        this.f2151r = b4;
        this.f2152s = z3;
        this.f2135b = zVar.n().a();
        this.f2136c = zVar.u().a(this);
        c cVar = new c();
        cVar.g(zVar.j(), TimeUnit.MILLISECONDS);
        r2.r rVar = r2.r.f10584a;
        this.f2137d = cVar;
        this.f2138e = new AtomicBoolean();
        this.f2146m = true;
    }

    private final IOException C(IOException iOException) {
        if (this.f2142i || !this.f2137d.s()) {
            return iOException;
        }
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String D() {
        StringBuilder sb = new StringBuilder();
        sb.append(q() ? "canceled " : "");
        sb.append(this.f2152s ? "web socket" : "call");
        sb.append(" to ");
        sb.append(x());
        return sb.toString();
    }

    private final IOException e(IOException iOException) {
        Socket socketY;
        boolean z3 = N2.c.f1409h;
        if (z3 && Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST NOT hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        f fVar = this.f2141h;
        if (fVar != null) {
            if (z3 && Thread.holdsLock(fVar)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Thread ");
                Thread threadCurrentThread2 = Thread.currentThread();
                D2.h.e(threadCurrentThread2, "Thread.currentThread()");
                sb2.append(threadCurrentThread2.getName());
                sb2.append(" MUST NOT hold lock on ");
                sb2.append(fVar);
                throw new AssertionError(sb2.toString());
            }
            synchronized (fVar) {
                socketY = y();
            }
            if (this.f2141h == null) {
                if (socketY != null) {
                    N2.c.k(socketY);
                }
                this.f2136c.l(this, fVar);
            } else {
                if (!(socketY == null)) {
                    throw new IllegalStateException("Check failed.");
                }
            }
        }
        IOException iOExceptionC = C(iOException);
        if (iOException != null) {
            r rVar = this.f2136c;
            D2.h.c(iOExceptionC);
            rVar.e(this, iOExceptionC);
        } else {
            this.f2136c.d(this);
        }
        return iOExceptionC;
    }

    private final void f() {
        this.f2139f = W2.j.f2732c.g().i("response.body().close()");
        this.f2136c.f(this);
    }

    private final C0190a h(u uVar) {
        SSLSocketFactory sSLSocketFactoryM;
        HostnameVerifier hostnameVerifierY;
        C0196g c0196gL;
        if (uVar.i()) {
            sSLSocketFactoryM = this.f2150q.M();
            hostnameVerifierY = this.f2150q.y();
            c0196gL = this.f2150q.l();
        } else {
            sSLSocketFactoryM = null;
            hostnameVerifierY = null;
            c0196gL = null;
        }
        return new C0190a(uVar.h(), uVar.l(), this.f2150q.t(), this.f2150q.L(), sSLSocketFactoryM, hostnameVerifierY, c0196gL, this.f2150q.H(), this.f2150q.G(), this.f2150q.F(), this.f2150q.p(), this.f2150q.I());
    }

    public final void A(f fVar) {
        this.f2149p = fVar;
    }

    public final void B() {
        if (this.f2142i) {
            throw new IllegalStateException("Check failed.");
        }
        this.f2142i = true;
        this.f2137d.s();
    }

    @Override // M2.InterfaceC0194e
    public D a() {
        if (!this.f2138e.compareAndSet(false, true)) {
            throw new IllegalStateException("Already Executed");
        }
        this.f2137d.r();
        f();
        try {
            this.f2150q.s().c(this);
            return t();
        } finally {
            this.f2150q.s().h(this);
        }
    }

    @Override // M2.InterfaceC0194e
    public void cancel() {
        if (this.f2147n) {
            return;
        }
        this.f2147n = true;
        R2.c cVar = this.f2148o;
        if (cVar != null) {
            cVar.b();
        }
        f fVar = this.f2149p;
        if (fVar != null) {
            fVar.d();
        }
        this.f2136c.g(this);
    }

    public final void d(f fVar) {
        D2.h.f(fVar, "connection");
        if (!N2.c.f1409h || Thread.holdsLock(fVar)) {
            if (!(this.f2141h == null)) {
                throw new IllegalStateException("Check failed.");
            }
            this.f2141h = fVar;
            fVar.n().add(new b(this, this.f2139f));
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

    /* JADX INFO: renamed from: g, reason: merged with bridge method [inline-methods] */
    public e clone() {
        return new e(this.f2150q, this.f2151r, this.f2152s);
    }

    @Override // M2.InterfaceC0194e
    public B i() {
        return this.f2151r;
    }

    public final void j(B b4, boolean z3) {
        D2.h.f(b4, "request");
        if (!(this.f2143j == null)) {
            throw new IllegalStateException("Check failed.");
        }
        synchronized (this) {
            if (this.f2145l) {
                throw new IllegalStateException("cannot make a new request because the previous response is still open: please call response.close()");
            }
            if (this.f2144k) {
                throw new IllegalStateException("Check failed.");
            }
            r2.r rVar = r2.r.f10584a;
        }
        if (z3) {
            this.f2140g = new d(this.f2135b, h(b4.l()), this, this.f2136c);
        }
    }

    public final void k(boolean z3) {
        R2.c cVar;
        synchronized (this) {
            if (!this.f2146m) {
                throw new IllegalStateException("released");
            }
            r2.r rVar = r2.r.f10584a;
        }
        if (z3 && (cVar = this.f2148o) != null) {
            cVar.d();
        }
        this.f2143j = null;
    }

    public final z l() {
        return this.f2150q;
    }

    public final f m() {
        return this.f2141h;
    }

    public final r n() {
        return this.f2136c;
    }

    @Override // M2.InterfaceC0194e
    public void o(InterfaceC0195f interfaceC0195f) {
        D2.h.f(interfaceC0195f, "responseCallback");
        if (!this.f2138e.compareAndSet(false, true)) {
            throw new IllegalStateException("Already Executed");
        }
        f();
        this.f2150q.s().b(new a(this, interfaceC0195f));
    }

    public final boolean p() {
        return this.f2152s;
    }

    @Override // M2.InterfaceC0194e
    public boolean q() {
        return this.f2147n;
    }

    public final R2.c r() {
        return this.f2143j;
    }

    public final B s() {
        return this.f2151r;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final M2.D t() throws java.lang.Throwable {
        /*
            r11 = this;
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            M2.z r0 = r11.f2150q
            java.util.List r0 = r0.z()
            s2.AbstractC0717n.t(r2, r0)
            S2.j r0 = new S2.j
            M2.z r1 = r11.f2150q
            r0.<init>(r1)
            r2.add(r0)
            S2.a r0 = new S2.a
            M2.z r1 = r11.f2150q
            M2.n r1 = r1.r()
            r0.<init>(r1)
            r2.add(r0)
            P2.a r0 = new P2.a
            M2.z r1 = r11.f2150q
            M2.c r1 = r1.h()
            r0.<init>(r1)
            r2.add(r0)
            R2.a r0 = R2.a.f2103a
            r2.add(r0)
            boolean r0 = r11.f2152s
            if (r0 != 0) goto L46
            M2.z r0 = r11.f2150q
            java.util.List r0 = r0.B()
            s2.AbstractC0717n.t(r2, r0)
        L46:
            S2.b r0 = new S2.b
            boolean r1 = r11.f2152s
            r0.<init>(r1)
            r2.add(r0)
            S2.g r9 = new S2.g
            M2.B r5 = r11.f2151r
            M2.z r0 = r11.f2150q
            int r6 = r0.m()
            M2.z r0 = r11.f2150q
            int r7 = r0.J()
            M2.z r0 = r11.f2150q
            int r8 = r0.O()
            r3 = 0
            r4 = 0
            r0 = r9
            r1 = r11
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            r0 = 0
            r1 = 0
            M2.B r2 = r11.f2151r     // Catch: java.lang.Throwable -> L8a java.io.IOException -> L8c
            M2.D r2 = r9.a(r2)     // Catch: java.lang.Throwable -> L8a java.io.IOException -> L8c
            boolean r3 = r11.q()     // Catch: java.lang.Throwable -> L8a java.io.IOException -> L8c
            if (r3 != 0) goto L7f
            r11.w(r0)
            return r2
        L7f:
            N2.c.j(r2)     // Catch: java.lang.Throwable -> L8a java.io.IOException -> L8c
            java.io.IOException r2 = new java.io.IOException     // Catch: java.lang.Throwable -> L8a java.io.IOException -> L8c
            java.lang.String r3 = "Canceled"
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L8a java.io.IOException -> L8c
            throw r2     // Catch: java.lang.Throwable -> L8a java.io.IOException -> L8c
        L8a:
            r2 = move-exception
            goto La2
        L8c:
            r1 = move-exception
            r2 = 1
            java.io.IOException r1 = r11.w(r1)     // Catch: java.lang.Throwable -> L9c
            if (r1 != 0) goto La1
            java.lang.NullPointerException r1 = new java.lang.NullPointerException     // Catch: java.lang.Throwable -> L9c
            java.lang.String r3 = "null cannot be cast to non-null type kotlin.Throwable"
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L9c
            throw r1     // Catch: java.lang.Throwable -> L9c
        L9c:
            r1 = move-exception
            r10 = r2
            r2 = r1
            r1 = r10
            goto La2
        La1:
            throw r1     // Catch: java.lang.Throwable -> L9c
        La2:
            if (r1 != 0) goto La7
            r11.w(r0)
        La7:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: R2.e.t():M2.D");
    }

    public final R2.c u(S2.g gVar) throws IOException {
        D2.h.f(gVar, "chain");
        synchronized (this) {
            if (!this.f2146m) {
                throw new IllegalStateException("released");
            }
            if (this.f2145l) {
                throw new IllegalStateException("Check failed.");
            }
            if (this.f2144k) {
                throw new IllegalStateException("Check failed.");
            }
            r2.r rVar = r2.r.f10584a;
        }
        d dVar = this.f2140g;
        D2.h.c(dVar);
        R2.c cVar = new R2.c(this, this.f2136c, dVar, dVar.a(this.f2150q, gVar));
        this.f2143j = cVar;
        this.f2148o = cVar;
        synchronized (this) {
            this.f2144k = true;
            this.f2145l = true;
        }
        if (this.f2147n) {
            throw new IOException("Canceled");
        }
        return cVar;
    }

    public final IOException v(R2.c cVar, boolean z3, boolean z4, IOException iOException) {
        boolean z5;
        boolean z6;
        D2.h.f(cVar, "exchange");
        if (!D2.h.b(cVar, this.f2148o)) {
            return iOException;
        }
        synchronized (this) {
            z5 = false;
            if (z3) {
                try {
                    if (!this.f2144k) {
                        if (z4 || !this.f2145l) {
                            z6 = false;
                        }
                    }
                    if (z3) {
                        this.f2144k = false;
                    }
                    if (z4) {
                        this.f2145l = false;
                    }
                    boolean z7 = this.f2144k;
                    boolean z8 = (z7 || this.f2145l) ? false : true;
                    if (!z7 && !this.f2145l && !this.f2146m) {
                        z5 = true;
                    }
                    z6 = z5;
                    z5 = z8;
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                if (z4) {
                }
                z6 = false;
            }
            r2.r rVar = r2.r.f10584a;
        }
        if (z5) {
            this.f2148o = null;
            f fVar = this.f2141h;
            if (fVar != null) {
                fVar.s();
            }
        }
        return z6 ? e(iOException) : iOException;
    }

    public final IOException w(IOException iOException) {
        boolean z3;
        synchronized (this) {
            try {
                z3 = false;
                if (this.f2146m) {
                    this.f2146m = false;
                    if (!this.f2144k && !this.f2145l) {
                        z3 = true;
                    }
                }
                r2.r rVar = r2.r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
        return z3 ? e(iOException) : iOException;
    }

    public final String x() {
        return this.f2151r.l().n();
    }

    public final Socket y() {
        f fVar = this.f2141h;
        D2.h.c(fVar);
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
        Iterator it = listN.iterator();
        int i3 = 0;
        while (true) {
            if (!it.hasNext()) {
                i3 = -1;
                break;
            }
            if (D2.h.b((e) ((Reference) it.next()).get(), this)) {
                break;
            }
            i3++;
        }
        if (!(i3 != -1)) {
            throw new IllegalStateException("Check failed.");
        }
        listN.remove(i3);
        this.f2141h = null;
        if (listN.isEmpty()) {
            fVar.C(System.nanoTime());
            if (this.f2135b.c(fVar)) {
                return fVar.E();
            }
        }
        return null;
    }

    public final boolean z() {
        d dVar = this.f2140g;
        D2.h.c(dVar);
        return dVar.e();
    }
}
