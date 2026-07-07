package R2;

import M2.B;
import M2.C;
import M2.D;
import M2.E;
import M2.r;
import a3.d;
import b3.D;
import b3.F;
import b3.n;
import b3.o;
import b3.t;
import java.io.IOException;
import java.net.ProtocolException;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f2108a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final f f2109b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e f2110c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final r f2111d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final d f2112e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final S2.d f2113f;

    private final class a extends n {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f2114c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private long f2115d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f2116e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final long f2117f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ c f2118g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(c cVar, D d4, long j3) {
            super(d4);
            D2.h.f(d4, "delegate");
            this.f2118g = cVar;
            this.f2117f = j3;
        }

        private final IOException a(IOException iOException) {
            if (this.f2114c) {
                return iOException;
            }
            this.f2114c = true;
            return this.f2118g.a(this.f2115d, false, true, iOException);
        }

        @Override // b3.n, b3.D
        public void Q(b3.i iVar, long j3) throws IOException {
            D2.h.f(iVar, "source");
            if (this.f2116e) {
                throw new IllegalStateException("closed");
            }
            long j4 = this.f2117f;
            if (j4 == -1 || this.f2115d + j3 <= j4) {
                try {
                    super.Q(iVar, j3);
                    this.f2115d += j3;
                    return;
                } catch (IOException e4) {
                    throw a(e4);
                }
            }
            throw new ProtocolException("expected " + this.f2117f + " bytes but received " + (this.f2115d + j3));
        }

        @Override // b3.n, b3.D, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.f2116e) {
                return;
            }
            this.f2116e = true;
            long j3 = this.f2117f;
            if (j3 != -1 && this.f2115d != j3) {
                throw new ProtocolException("unexpected end of stream");
            }
            try {
                super.close();
                a(null);
            } catch (IOException e4) {
                throw a(e4);
            }
        }

        @Override // b3.n, b3.D, java.io.Flushable
        public void flush() throws IOException {
            try {
                super.flush();
            } catch (IOException e4) {
                throw a(e4);
            }
        }
    }

    public final class b extends o {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private long f2119c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f2120d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f2121e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f2122f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final long f2123g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ c f2124h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(c cVar, F f3, long j3) {
            super(f3);
            D2.h.f(f3, "delegate");
            this.f2124h = cVar;
            this.f2123g = j3;
            this.f2120d = true;
            if (j3 == 0) {
                i(null);
            }
        }

        @Override // b3.o, b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.f2122f) {
                return;
            }
            this.f2122f = true;
            try {
                super.close();
                i(null);
            } catch (IOException e4) {
                throw i(e4);
            }
        }

        public final IOException i(IOException iOException) {
            if (this.f2121e) {
                return iOException;
            }
            this.f2121e = true;
            if (iOException == null && this.f2120d) {
                this.f2120d = false;
                this.f2124h.i().w(this.f2124h.g());
            }
            return this.f2124h.a(this.f2119c, true, false, iOException);
        }

        @Override // b3.o, b3.F
        public long x(b3.i iVar, long j3) throws IOException {
            D2.h.f(iVar, "sink");
            if (this.f2122f) {
                throw new IllegalStateException("closed");
            }
            try {
                long jX = a().x(iVar, j3);
                if (this.f2120d) {
                    this.f2120d = false;
                    this.f2124h.i().w(this.f2124h.g());
                }
                if (jX == -1) {
                    i(null);
                    return -1L;
                }
                long j4 = this.f2119c + jX;
                long j5 = this.f2123g;
                if (j5 != -1 && j4 > j5) {
                    throw new ProtocolException("expected " + this.f2123g + " bytes but received " + j4);
                }
                this.f2119c = j4;
                if (j4 == j5) {
                    i(null);
                }
                return jX;
            } catch (IOException e4) {
                throw i(e4);
            }
        }
    }

    public c(e eVar, r rVar, d dVar, S2.d dVar2) {
        D2.h.f(eVar, "call");
        D2.h.f(rVar, "eventListener");
        D2.h.f(dVar, "finder");
        D2.h.f(dVar2, "codec");
        this.f2110c = eVar;
        this.f2111d = rVar;
        this.f2112e = dVar;
        this.f2113f = dVar2;
        this.f2109b = dVar2.h();
    }

    private final void t(IOException iOException) {
        this.f2112e.h(iOException);
        this.f2113f.h().H(this.f2110c, iOException);
    }

    public final IOException a(long j3, boolean z3, boolean z4, IOException iOException) {
        if (iOException != null) {
            t(iOException);
        }
        if (z4) {
            if (iOException != null) {
                this.f2111d.s(this.f2110c, iOException);
            } else {
                this.f2111d.q(this.f2110c, j3);
            }
        }
        if (z3) {
            if (iOException != null) {
                this.f2111d.x(this.f2110c, iOException);
            } else {
                this.f2111d.v(this.f2110c, j3);
            }
        }
        return this.f2110c.v(this, z4, z3, iOException);
    }

    public final void b() {
        this.f2113f.cancel();
    }

    public final D c(B b4, boolean z3) {
        D2.h.f(b4, "request");
        this.f2108a = z3;
        C cA = b4.a();
        D2.h.c(cA);
        long jA = cA.a();
        this.f2111d.r(this.f2110c);
        return new a(this, this.f2113f.e(b4, jA), jA);
    }

    public final void d() {
        this.f2113f.cancel();
        this.f2110c.v(this, true, true, null);
    }

    public final void e() throws IOException {
        try {
            this.f2113f.a();
        } catch (IOException e4) {
            this.f2111d.s(this.f2110c, e4);
            t(e4);
            throw e4;
        }
    }

    public final void f() throws IOException {
        try {
            this.f2113f.b();
        } catch (IOException e4) {
            this.f2111d.s(this.f2110c, e4);
            t(e4);
            throw e4;
        }
    }

    public final e g() {
        return this.f2110c;
    }

    public final f h() {
        return this.f2109b;
    }

    public final r i() {
        return this.f2111d;
    }

    public final d j() {
        return this.f2112e;
    }

    public final boolean k() {
        return !D2.h.b(this.f2112e.d().l().h(), this.f2109b.A().a().l().h());
    }

    public final boolean l() {
        return this.f2108a;
    }

    public final d.AbstractC0048d m() {
        this.f2110c.B();
        return this.f2113f.h().x(this);
    }

    public final void n() {
        this.f2113f.h().z();
    }

    public final void o() {
        this.f2110c.v(this, true, false, null);
    }

    public final E p(M2.D d4) throws IOException {
        D2.h.f(d4, "response");
        try {
            String strC0 = M2.D.c0(d4, "Content-Type", null, 2, null);
            long jD = this.f2113f.d(d4);
            return new S2.h(strC0, jD, t.d(new b(this, this.f2113f.c(d4), jD)));
        } catch (IOException e4) {
            this.f2111d.x(this.f2110c, e4);
            t(e4);
            throw e4;
        }
    }

    public final D.a q(boolean z3) throws IOException {
        try {
            D.a aVarG = this.f2113f.g(z3);
            if (aVarG != null) {
                aVarG.l(this);
            }
            return aVarG;
        } catch (IOException e4) {
            this.f2111d.x(this.f2110c, e4);
            t(e4);
            throw e4;
        }
    }

    public final void r(M2.D d4) {
        D2.h.f(d4, "response");
        this.f2111d.y(this.f2110c, d4);
    }

    public final void s() {
        this.f2111d.z(this.f2110c);
    }

    public final void u() {
        a(-1L, true, true, null);
    }

    public final void v(B b4) throws IOException {
        D2.h.f(b4, "request");
        try {
            this.f2111d.u(this.f2110c);
            this.f2113f.f(b4);
            this.f2111d.t(this.f2110c, b4);
        } catch (IOException e4) {
            this.f2111d.s(this.f2110c, e4);
            t(e4);
            throw e4;
        }
    }
}
