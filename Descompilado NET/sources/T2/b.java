package T2;

import D2.h;
import K2.o;
import M2.B;
import M2.D;
import M2.n;
import M2.t;
import M2.u;
import M2.z;
import b3.D;
import b3.F;
import b3.G;
import b3.i;
import b3.j;
import b3.k;
import b3.p;
import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class b implements S2.d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final d f2352h = new d(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f2353a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final T2.a f2354b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private t f2355c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final z f2356d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final R2.f f2357e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final k f2358f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final j f2359g;

    private abstract class a implements F {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final p f2360b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f2361c;

        public a() {
            this.f2360b = new p(b.this.f2358f.f());
        }

        protected final boolean a() {
            return this.f2361c;
        }

        @Override // b3.F
        public G f() {
            return this.f2360b;
        }

        public final void i() {
            if (b.this.f2353a == 6) {
                return;
            }
            if (b.this.f2353a == 5) {
                b.this.r(this.f2360b);
                b.this.f2353a = 6;
            } else {
                throw new IllegalStateException("state: " + b.this.f2353a);
            }
        }

        protected final void o(boolean z3) {
            this.f2361c = z3;
        }

        @Override // b3.F
        public long x(i iVar, long j3) throws IOException {
            h.f(iVar, "sink");
            try {
                return b.this.f2358f.x(iVar, j3);
            } catch (IOException e4) {
                b.this.h().z();
                i();
                throw e4;
            }
        }
    }

    /* JADX INFO: renamed from: T2.b$b, reason: collision with other inner class name */
    private final class C0033b implements D {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final p f2363b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f2364c;

        public C0033b() {
            this.f2363b = new p(b.this.f2359g.f());
        }

        @Override // b3.D
        public void Q(i iVar, long j3) {
            h.f(iVar, "source");
            if (this.f2364c) {
                throw new IllegalStateException("closed");
            }
            if (j3 == 0) {
                return;
            }
            b.this.f2359g.n(j3);
            b.this.f2359g.h0("\r\n");
            b.this.f2359g.Q(iVar, j3);
            b.this.f2359g.h0("\r\n");
        }

        @Override // b3.D, java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() {
            if (this.f2364c) {
                return;
            }
            this.f2364c = true;
            b.this.f2359g.h0("0\r\n\r\n");
            b.this.r(this.f2363b);
            b.this.f2353a = 3;
        }

        @Override // b3.D
        public G f() {
            return this.f2363b;
        }

        @Override // b3.D, java.io.Flushable
        public synchronized void flush() {
            if (this.f2364c) {
                return;
            }
            b.this.f2359g.flush();
        }
    }

    private final class c extends a {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private long f2366e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f2367f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final u f2368g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ b f2369h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(b bVar, u uVar) {
            super();
            h.f(uVar, "url");
            this.f2369h = bVar;
            this.f2368g = uVar;
            this.f2366e = -1L;
            this.f2367f = true;
        }

        private final void q() throws ProtocolException {
            if (this.f2366e != -1) {
                this.f2369h.f2358f.G();
            }
            try {
                this.f2366e = this.f2369h.f2358f.o0();
                String strG = this.f2369h.f2358f.G();
                if (strG == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                String string = o.w0(strG).toString();
                if (this.f2366e < 0 || (string.length() > 0 && !o.z(string, ";", false, 2, null))) {
                    throw new ProtocolException("expected chunk size and optional extensions but was \"" + this.f2366e + string + '\"');
                }
                if (this.f2366e == 0) {
                    this.f2367f = false;
                    b bVar = this.f2369h;
                    bVar.f2355c = bVar.f2354b.a();
                    z zVar = this.f2369h.f2356d;
                    h.c(zVar);
                    n nVarR = zVar.r();
                    u uVar = this.f2368g;
                    t tVar = this.f2369h.f2355c;
                    h.c(tVar);
                    S2.e.f(nVarR, uVar, tVar);
                    i();
                }
            } catch (NumberFormatException e4) {
                throw new ProtocolException(e4.getMessage());
            }
        }

        @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (a()) {
                return;
            }
            if (this.f2367f && !N2.c.p(this, 100, TimeUnit.MILLISECONDS)) {
                this.f2369h.h().z();
                i();
            }
            o(true);
        }

        @Override // T2.b.a, b3.F
        public long x(i iVar, long j3) throws IOException {
            h.f(iVar, "sink");
            if (!(j3 >= 0)) {
                throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
            }
            if (a()) {
                throw new IllegalStateException("closed");
            }
            if (!this.f2367f) {
                return -1L;
            }
            long j4 = this.f2366e;
            if (j4 == 0 || j4 == -1) {
                q();
                if (!this.f2367f) {
                    return -1L;
                }
            }
            long jX = super.x(iVar, Math.min(j3, this.f2366e));
            if (jX != -1) {
                this.f2366e -= jX;
                return jX;
            }
            this.f2369h.h().z();
            ProtocolException protocolException = new ProtocolException("unexpected end of stream");
            i();
            throw protocolException;
        }
    }

    public static final class d {
        private d() {
        }

        public /* synthetic */ d(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private final class e extends a {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private long f2370e;

        public e(long j3) {
            super();
            this.f2370e = j3;
            if (j3 == 0) {
                i();
            }
        }

        @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (a()) {
                return;
            }
            if (this.f2370e != 0 && !N2.c.p(this, 100, TimeUnit.MILLISECONDS)) {
                b.this.h().z();
                i();
            }
            o(true);
        }

        @Override // T2.b.a, b3.F
        public long x(i iVar, long j3) throws IOException {
            h.f(iVar, "sink");
            if (!(j3 >= 0)) {
                throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
            }
            if (a()) {
                throw new IllegalStateException("closed");
            }
            long j4 = this.f2370e;
            if (j4 == 0) {
                return -1L;
            }
            long jX = super.x(iVar, Math.min(j4, j3));
            if (jX == -1) {
                b.this.h().z();
                ProtocolException protocolException = new ProtocolException("unexpected end of stream");
                i();
                throw protocolException;
            }
            long j5 = this.f2370e - jX;
            this.f2370e = j5;
            if (j5 == 0) {
                i();
            }
            return jX;
        }
    }

    private final class f implements D {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final p f2372b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f2373c;

        public f() {
            this.f2372b = new p(b.this.f2359g.f());
        }

        @Override // b3.D
        public void Q(i iVar, long j3) {
            h.f(iVar, "source");
            if (this.f2373c) {
                throw new IllegalStateException("closed");
            }
            N2.c.i(iVar.F0(), 0L, j3);
            b.this.f2359g.Q(iVar, j3);
        }

        @Override // b3.D, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.f2373c) {
                return;
            }
            this.f2373c = true;
            b.this.r(this.f2372b);
            b.this.f2353a = 3;
        }

        @Override // b3.D
        public G f() {
            return this.f2372b;
        }

        @Override // b3.D, java.io.Flushable
        public void flush() {
            if (this.f2373c) {
                return;
            }
            b.this.f2359g.flush();
        }
    }

    private final class g extends a {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f2375e;

        public g() {
            super();
        }

        @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (a()) {
                return;
            }
            if (!this.f2375e) {
                i();
            }
            o(true);
        }

        @Override // T2.b.a, b3.F
        public long x(i iVar, long j3) throws IOException {
            h.f(iVar, "sink");
            if (!(j3 >= 0)) {
                throw new IllegalArgumentException(("byteCount < 0: " + j3).toString());
            }
            if (a()) {
                throw new IllegalStateException("closed");
            }
            if (this.f2375e) {
                return -1L;
            }
            long jX = super.x(iVar, j3);
            if (jX != -1) {
                return jX;
            }
            this.f2375e = true;
            i();
            return -1L;
        }
    }

    public b(z zVar, R2.f fVar, k kVar, j jVar) {
        h.f(fVar, "connection");
        h.f(kVar, "source");
        h.f(jVar, "sink");
        this.f2356d = zVar;
        this.f2357e = fVar;
        this.f2358f = kVar;
        this.f2359g = jVar;
        this.f2354b = new T2.a(kVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void r(p pVar) {
        G gI = pVar.i();
        pVar.j(G.f5605d);
        gI.a();
        gI.b();
    }

    private final boolean s(B b4) {
        return o.n("chunked", b4.d("Transfer-Encoding"), true);
    }

    private final boolean t(M2.D d4) {
        return o.n("chunked", M2.D.c0(d4, "Transfer-Encoding", null, 2, null), true);
    }

    private final D u() {
        if (this.f2353a == 1) {
            this.f2353a = 2;
            return new C0033b();
        }
        throw new IllegalStateException(("state: " + this.f2353a).toString());
    }

    private final F v(u uVar) {
        if (this.f2353a == 4) {
            this.f2353a = 5;
            return new c(this, uVar);
        }
        throw new IllegalStateException(("state: " + this.f2353a).toString());
    }

    private final F w(long j3) {
        if (this.f2353a == 4) {
            this.f2353a = 5;
            return new e(j3);
        }
        throw new IllegalStateException(("state: " + this.f2353a).toString());
    }

    private final D x() {
        if (this.f2353a == 1) {
            this.f2353a = 2;
            return new f();
        }
        throw new IllegalStateException(("state: " + this.f2353a).toString());
    }

    private final F y() {
        if (this.f2353a == 4) {
            this.f2353a = 5;
            h().z();
            return new g();
        }
        throw new IllegalStateException(("state: " + this.f2353a).toString());
    }

    public final void A(t tVar, String str) {
        h.f(tVar, "headers");
        h.f(str, "requestLine");
        if (!(this.f2353a == 0)) {
            throw new IllegalStateException(("state: " + this.f2353a).toString());
        }
        this.f2359g.h0(str).h0("\r\n");
        int size = tVar.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.f2359g.h0(tVar.b(i3)).h0(": ").h0(tVar.h(i3)).h0("\r\n");
        }
        this.f2359g.h0("\r\n");
        this.f2353a = 1;
    }

    @Override // S2.d
    public void a() {
        this.f2359g.flush();
    }

    @Override // S2.d
    public void b() {
        this.f2359g.flush();
    }

    @Override // S2.d
    public F c(M2.D d4) {
        h.f(d4, "response");
        if (!S2.e.b(d4)) {
            return w(0L);
        }
        if (t(d4)) {
            return v(d4.y0().l());
        }
        long jS = N2.c.s(d4);
        return jS != -1 ? w(jS) : y();
    }

    @Override // S2.d
    public void cancel() {
        h().d();
    }

    @Override // S2.d
    public long d(M2.D d4) {
        h.f(d4, "response");
        if (!S2.e.b(d4)) {
            return 0L;
        }
        if (t(d4)) {
            return -1L;
        }
        return N2.c.s(d4);
    }

    @Override // S2.d
    public D e(B b4, long j3) throws ProtocolException {
        h.f(b4, "request");
        if (b4.a() != null && b4.a().f()) {
            throw new ProtocolException("Duplex connections are not supported for HTTP/1");
        }
        if (s(b4)) {
            return u();
        }
        if (j3 != -1) {
            return x();
        }
        throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    }

    @Override // S2.d
    public void f(B b4) {
        h.f(b4, "request");
        S2.i iVar = S2.i.f2335a;
        Proxy.Type type = h().A().b().type();
        h.e(type, "connection.route().proxy.type()");
        A(b4.e(), iVar.a(b4, type));
    }

    @Override // S2.d
    public D.a g(boolean z3) {
        int i3 = this.f2353a;
        boolean z4 = true;
        if (i3 != 1 && i3 != 3) {
            z4 = false;
        }
        if (!z4) {
            throw new IllegalStateException(("state: " + this.f2353a).toString());
        }
        try {
            S2.k kVarA = S2.k.f2338d.a(this.f2354b.b());
            D.a aVarK = new D.a().p(kVarA.f2339a).g(kVarA.f2340b).m(kVarA.f2341c).k(this.f2354b.a());
            if (z3 && kVarA.f2340b == 100) {
                return null;
            }
            if (kVarA.f2340b == 100) {
                this.f2353a = 3;
                return aVarK;
            }
            this.f2353a = 4;
            return aVarK;
        } catch (EOFException e4) {
            throw new IOException("unexpected end of stream on " + h().A().a().l().n(), e4);
        }
    }

    @Override // S2.d
    public R2.f h() {
        return this.f2357e;
    }

    public final void z(M2.D d4) {
        h.f(d4, "response");
        long jS = N2.c.s(d4);
        if (jS == -1) {
            return;
        }
        F fW = w(jS);
        N2.c.J(fW, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
        fW.close();
    }
}
