package P2;

import D2.h;
import K2.o;
import M2.A;
import M2.B;
import M2.C0192c;
import M2.D;
import M2.E;
import M2.InterfaceC0194e;
import M2.r;
import M2.t;
import M2.v;
import P2.c;
import S2.f;
import b3.F;
import b3.G;
import b3.i;
import b3.j;
import b3.k;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a implements v {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final C0025a f1712b = new C0025a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C0192c f1713a;

    /* JADX INFO: renamed from: P2.a$a, reason: collision with other inner class name */
    public static final class C0025a {
        private C0025a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final t c(t tVar, t tVar2) {
            t.a aVar = new t.a();
            int size = tVar.size();
            for (int i3 = 0; i3 < size; i3++) {
                String strB = tVar.b(i3);
                String strH = tVar.h(i3);
                if ((!o.n("Warning", strB, true) || !o.z(strH, "1", false, 2, null)) && (d(strB) || !e(strB) || tVar2.a(strB) == null)) {
                    aVar.c(strB, strH);
                }
            }
            int size2 = tVar2.size();
            for (int i4 = 0; i4 < size2; i4++) {
                String strB2 = tVar2.b(i4);
                if (!d(strB2) && e(strB2)) {
                    aVar.c(strB2, tVar2.h(i4));
                }
            }
            return aVar.e();
        }

        private final boolean d(String str) {
            return o.n("Content-Length", str, true) || o.n("Content-Encoding", str, true) || o.n("Content-Type", str, true);
        }

        private final boolean e(String str) {
            return (o.n("Connection", str, true) || o.n("Keep-Alive", str, true) || o.n("Proxy-Authenticate", str, true) || o.n("Proxy-Authorization", str, true) || o.n("TE", str, true) || o.n("Trailers", str, true) || o.n("Transfer-Encoding", str, true) || o.n("Upgrade", str, true)) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final D f(D d4) {
            return (d4 != null ? d4.q() : null) != null ? d4.u0().b(null).c() : d4;
        }

        public /* synthetic */ C0025a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final class b implements F {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f1714b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ k f1715c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ P2.b f1716d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ j f1717e;

        b(k kVar, P2.b bVar, j jVar) {
            this.f1715c = kVar;
            this.f1716d = bVar;
            this.f1717e = jVar;
        }

        @Override // b3.F, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (!this.f1714b && !N2.c.p(this, 100, TimeUnit.MILLISECONDS)) {
                this.f1714b = true;
                this.f1716d.b();
            }
            this.f1715c.close();
        }

        @Override // b3.F
        public G f() {
            return this.f1715c.f();
        }

        @Override // b3.F
        public long x(i iVar, long j3) throws IOException {
            h.f(iVar, "sink");
            try {
                long jX = this.f1715c.x(iVar, j3);
                if (jX != -1) {
                    iVar.D(this.f1717e.e(), iVar.F0() - jX, jX);
                    this.f1717e.U();
                    return jX;
                }
                if (!this.f1714b) {
                    this.f1714b = true;
                    this.f1717e.close();
                }
                return -1L;
            } catch (IOException e4) {
                if (!this.f1714b) {
                    this.f1714b = true;
                    this.f1716d.b();
                }
                throw e4;
            }
        }
    }

    public a(C0192c c0192c) {
        this.f1713a = c0192c;
    }

    private final D b(P2.b bVar, D d4) {
        if (bVar == null) {
            return d4;
        }
        b3.D dA = bVar.a();
        E eQ = d4.q();
        h.c(eQ);
        b bVar2 = new b(eQ.z(), bVar, b3.t.c(dA));
        return d4.u0().b(new S2.h(D.c0(d4, "Content-Type", null, 2, null), d4.q().q(), b3.t.d(bVar2))).c();
    }

    @Override // M2.v
    public D a(v.a aVar) {
        r rVarN;
        E eQ;
        E eQ2;
        h.f(aVar, "chain");
        InterfaceC0194e interfaceC0194eCall = aVar.call();
        C0192c c0192c = this.f1713a;
        D dI = c0192c != null ? c0192c.i(aVar.i()) : null;
        c cVarB = new c.b(System.currentTimeMillis(), aVar.i(), dI).b();
        B b4 = cVarB.b();
        D dA = cVarB.a();
        C0192c c0192c2 = this.f1713a;
        if (c0192c2 != null) {
            c0192c2.P(cVarB);
        }
        R2.e eVar = (R2.e) (interfaceC0194eCall instanceof R2.e ? interfaceC0194eCall : null);
        if (eVar == null || (rVarN = eVar.n()) == null) {
            rVarN = r.f1214a;
        }
        if (dI != null && dA == null && (eQ2 = dI.q()) != null) {
            N2.c.j(eQ2);
        }
        if (b4 == null && dA == null) {
            D dC = new D.a().r(aVar.i()).p(A.HTTP_1_1).g(504).m("Unsatisfiable Request (only-if-cached)").b(N2.c.f1404c).s(-1L).q(System.currentTimeMillis()).c();
            rVarN.A(interfaceC0194eCall, dC);
            return dC;
        }
        if (b4 == null) {
            h.c(dA);
            D dC2 = dA.u0().d(f1712b.f(dA)).c();
            rVarN.b(interfaceC0194eCall, dC2);
            return dC2;
        }
        if (dA != null) {
            rVarN.a(interfaceC0194eCall, dA);
        } else if (this.f1713a != null) {
            rVarN.c(interfaceC0194eCall);
        }
        try {
            D dA2 = aVar.a(b4);
            if (dA2 == null && dI != null && eQ != null) {
            }
            if (dA != null) {
                if (dA2 != null && dA2.A() == 304) {
                    D.a aVarU0 = dA.u0();
                    C0025a c0025a = f1712b;
                    D dC3 = aVarU0.k(c0025a.c(dA.d0(), dA2.d0())).s(dA2.z0()).q(dA2.x0()).d(c0025a.f(dA)).n(c0025a.f(dA2)).c();
                    E eQ3 = dA2.q();
                    h.c(eQ3);
                    eQ3.close();
                    C0192c c0192c3 = this.f1713a;
                    h.c(c0192c3);
                    c0192c3.D();
                    this.f1713a.X(dA, dC3);
                    rVarN.b(interfaceC0194eCall, dC3);
                    return dC3;
                }
                E eQ4 = dA.q();
                if (eQ4 != null) {
                    N2.c.j(eQ4);
                }
            }
            h.c(dA2);
            D.a aVarU02 = dA2.u0();
            C0025a c0025a2 = f1712b;
            D dC4 = aVarU02.d(c0025a2.f(dA)).n(c0025a2.f(dA2)).c();
            if (this.f1713a != null) {
                if (S2.e.b(dC4) && c.f1718c.a(dC4, b4)) {
                    D dB = b(this.f1713a.v(dC4), dC4);
                    if (dA != null) {
                        rVarN.c(interfaceC0194eCall);
                    }
                    return dB;
                }
                if (f.f2322a.a(b4.h())) {
                    try {
                        this.f1713a.y(b4);
                    } catch (IOException unused) {
                    }
                }
            }
            return dC4;
        } finally {
            if (dI != null && (eQ = dI.q()) != null) {
                N2.c.j(eQ);
            }
        }
    }
}
