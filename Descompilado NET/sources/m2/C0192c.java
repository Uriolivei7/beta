package M2;

import M2.B;
import M2.D;
import M2.t;
import P2.d;
import W2.j;
import b3.l;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;
import s2.L;

/* JADX INFO: renamed from: M2.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0192c implements Closeable, Flushable {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final b f971h = new b(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final P2.d f972b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f973c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f974d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f975e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f976f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f977g;

    /* JADX INFO: renamed from: M2.c$a */
    private static final class a extends E {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final b3.k f978c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final d.C0026d f979d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final String f980e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final String f981f;

        /* JADX INFO: renamed from: M2.c$a$a, reason: collision with other inner class name */
        public static final class C0017a extends b3.o {

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            final /* synthetic */ b3.F f983d;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0017a(b3.F f3, b3.F f4) {
                super(f4);
                this.f983d = f3;
            }

            @Override // b3.o, b3.F, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                a.this.D().close();
                super.close();
            }
        }

        public a(d.C0026d c0026d, String str, String str2) {
            D2.h.f(c0026d, "snapshot");
            this.f979d = c0026d;
            this.f980e = str;
            this.f981f = str2;
            b3.F fI = c0026d.i(1);
            this.f978c = b3.t.d(new C0017a(fI, fI));
        }

        public final d.C0026d D() {
            return this.f979d;
        }

        @Override // M2.E
        public long q() {
            String str = this.f981f;
            if (str != null) {
                return N2.c.T(str, -1L);
            }
            return -1L;
        }

        @Override // M2.E
        public x v() {
            String str = this.f980e;
            if (str != null) {
                return x.f1251g.c(str);
            }
            return null;
        }

        @Override // M2.E
        public b3.k z() {
            return this.f978c;
        }
    }

    /* JADX INFO: renamed from: M2.c$b */
    public static final class b {
        private b() {
        }

        private final Set d(t tVar) {
            int size = tVar.size();
            TreeSet treeSet = null;
            for (int i3 = 0; i3 < size; i3++) {
                if (K2.o.n("Vary", tVar.b(i3), true)) {
                    String strH = tVar.h(i3);
                    if (treeSet == null) {
                        treeSet = new TreeSet(K2.o.o(D2.u.f192a));
                    }
                    for (String str : K2.o.k0(strH, new char[]{','}, false, 0, 6, null)) {
                        if (str == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                        treeSet.add(K2.o.w0(str).toString());
                    }
                }
            }
            return treeSet != null ? treeSet : L.b();
        }

        private final t e(t tVar, t tVar2) {
            Set setD = d(tVar2);
            if (setD.isEmpty()) {
                return N2.c.f1403b;
            }
            t.a aVar = new t.a();
            int size = tVar.size();
            for (int i3 = 0; i3 < size; i3++) {
                String strB = tVar.b(i3);
                if (setD.contains(strB)) {
                    aVar.a(strB, tVar.h(i3));
                }
            }
            return aVar.e();
        }

        public final boolean a(D d4) {
            D2.h.f(d4, "$this$hasVaryAll");
            return d(d4.d0()).contains("*");
        }

        public final String b(u uVar) {
            D2.h.f(uVar, "url");
            return b3.l.f5639f.e(uVar.toString()).n().k();
        }

        public final int c(b3.k kVar) throws IOException {
            D2.h.f(kVar, "source");
            try {
                long jV = kVar.V();
                String strG = kVar.G();
                if (jV >= 0 && jV <= Integer.MAX_VALUE && strG.length() <= 0) {
                    return (int) jV;
                }
                throw new IOException("expected an int but was \"" + jV + strG + '\"');
            } catch (NumberFormatException e4) {
                throw new IOException(e4.getMessage());
            }
        }

        public final t f(D d4) {
            D2.h.f(d4, "$this$varyHeaders");
            D dT0 = d4.t0();
            D2.h.c(dT0);
            return e(dT0.y0().e(), d4.d0());
        }

        public final boolean g(D d4, t tVar, B b4) {
            D2.h.f(d4, "cachedResponse");
            D2.h.f(tVar, "cachedRequest");
            D2.h.f(b4, "newRequest");
            Set<String> setD = d(d4.d0());
            if (setD != null && setD.isEmpty()) {
                return true;
            }
            for (String str : setD) {
                if (!D2.h.b(tVar.i(str), b4.f(str))) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: renamed from: M2.c$d */
    private final class d implements P2.b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final b3.D f997a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final b3.D f998b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f999c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final d.b f1000d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ C0192c f1001e;

        /* JADX INFO: renamed from: M2.c$d$a */
        public static final class a extends b3.n {
            a(b3.D d4) {
                super(d4);
            }

            @Override // b3.n, b3.D, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                synchronized (d.this.f1001e) {
                    if (d.this.d()) {
                        return;
                    }
                    d.this.e(true);
                    C0192c c0192c = d.this.f1001e;
                    c0192c.A(c0192c.q() + 1);
                    super.close();
                    d.this.f1000d.b();
                }
            }
        }

        public d(C0192c c0192c, d.b bVar) {
            D2.h.f(bVar, "editor");
            this.f1001e = c0192c;
            this.f1000d = bVar;
            b3.D dF = bVar.f(1);
            this.f997a = dF;
            this.f998b = new a(dF);
        }

        @Override // P2.b
        public b3.D a() {
            return this.f998b;
        }

        @Override // P2.b
        public void b() {
            synchronized (this.f1001e) {
                if (this.f999c) {
                    return;
                }
                this.f999c = true;
                C0192c c0192c = this.f1001e;
                c0192c.z(c0192c.o() + 1);
                N2.c.j(this.f997a);
                try {
                    this.f1000d.a();
                } catch (IOException unused) {
                }
            }
        }

        public final boolean d() {
            return this.f999c;
        }

        public final void e(boolean z3) {
            this.f999c = z3;
        }
    }

    public C0192c(File file, long j3, V2.a aVar) {
        D2.h.f(file, "directory");
        D2.h.f(aVar, "fileSystem");
        this.f972b = new P2.d(aVar, file, 201105, 2, j3, Q2.e.f1870h);
    }

    private final void a(d.b bVar) {
        if (bVar != null) {
            try {
                bVar.a();
            } catch (IOException unused) {
            }
        }
    }

    public final void A(int i3) {
        this.f973c = i3;
    }

    public final synchronized void D() {
        this.f976f++;
    }

    public final synchronized void P(P2.c cVar) {
        try {
            D2.h.f(cVar, "cacheStrategy");
            this.f977g++;
            if (cVar.b() != null) {
                this.f975e++;
            } else if (cVar.a() != null) {
                this.f976f++;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public final void X(D d4, D d5) {
        d.b bVarA;
        D2.h.f(d4, "cached");
        D2.h.f(d5, "network");
        C0018c c0018c = new C0018c(d5);
        E eQ = d4.q();
        if (eQ == null) {
            throw new NullPointerException("null cannot be cast to non-null type okhttp3.Cache.CacheResponseBody");
        }
        try {
            bVarA = ((a) eQ).D().a();
            if (bVarA != null) {
                try {
                    c0018c.f(bVarA);
                    bVarA.b();
                } catch (IOException unused) {
                    a(bVarA);
                }
            }
        } catch (IOException unused2) {
            bVarA = null;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f972b.close();
    }

    @Override // java.io.Flushable
    public void flush() {
        this.f972b.flush();
    }

    public final D i(B b4) {
        D2.h.f(b4, "request");
        try {
            d.C0026d c0026dD0 = this.f972b.d0(f971h.b(b4.l()));
            if (c0026dD0 != null) {
                try {
                    C0018c c0018c = new C0018c(c0026dD0.i(0));
                    D d4 = c0018c.d(c0026dD0);
                    if (c0018c.b(b4, d4)) {
                        return d4;
                    }
                    E eQ = d4.q();
                    if (eQ != null) {
                        N2.c.j(eQ);
                    }
                    return null;
                } catch (IOException unused) {
                    N2.c.j(c0026dD0);
                }
            }
        } catch (IOException unused2) {
        }
        return null;
    }

    public final int o() {
        return this.f974d;
    }

    public final int q() {
        return this.f973c;
    }

    public final P2.b v(D d4) {
        d.b bVarC0;
        D2.h.f(d4, "response");
        String strH = d4.y0().h();
        if (S2.f.f2322a.a(d4.y0().h())) {
            try {
                y(d4.y0());
            } catch (IOException unused) {
            }
            return null;
        }
        if (!D2.h.b(strH, "GET")) {
            return null;
        }
        b bVar = f971h;
        if (bVar.a(d4)) {
            return null;
        }
        C0018c c0018c = new C0018c(d4);
        try {
            bVarC0 = P2.d.c0(this.f972b, bVar.b(d4.y0().l()), 0L, 2, null);
            if (bVarC0 == null) {
                return null;
            }
            try {
                c0018c.f(bVarC0);
                return new d(this, bVarC0);
            } catch (IOException unused2) {
                a(bVarC0);
                return null;
            }
        } catch (IOException unused3) {
            bVarC0 = null;
        }
    }

    public final void y(B b4) {
        D2.h.f(b4, "request");
        this.f972b.C0(f971h.b(b4.l()));
    }

    public final void z(int i3) {
        this.f974d = i3;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0192c(File file, long j3) {
        this(file, j3, V2.a.f2678a);
        D2.h.f(file, "directory");
    }

    /* JADX INFO: renamed from: M2.c$c, reason: collision with other inner class name */
    private static final class C0018c {

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private static final String f984k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private static final String f985l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public static final a f986m = new a(null);

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f987a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final t f988b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final String f989c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final A f990d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final int f991e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final String f992f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final t f993g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private final s f994h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private final long f995i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private final long f996j;

        /* JADX INFO: renamed from: M2.c$c$a */
        public static final class a {
            private a() {
            }

            public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        static {
            StringBuilder sb = new StringBuilder();
            j.a aVar = W2.j.f2732c;
            sb.append(aVar.g().g());
            sb.append("-Sent-Millis");
            f984k = sb.toString();
            f985l = aVar.g().g() + "-Received-Millis";
        }

        public C0018c(b3.F f3) {
            D2.h.f(f3, "rawSource");
            try {
                b3.k kVarD = b3.t.d(f3);
                this.f987a = kVarD.G();
                this.f989c = kVarD.G();
                t.a aVar = new t.a();
                int iC = C0192c.f971h.c(kVarD);
                for (int i3 = 0; i3 < iC; i3++) {
                    aVar.b(kVarD.G());
                }
                this.f988b = aVar.e();
                S2.k kVarA = S2.k.f2338d.a(kVarD.G());
                this.f990d = kVarA.f2339a;
                this.f991e = kVarA.f2340b;
                this.f992f = kVarA.f2341c;
                t.a aVar2 = new t.a();
                int iC2 = C0192c.f971h.c(kVarD);
                for (int i4 = 0; i4 < iC2; i4++) {
                    aVar2.b(kVarD.G());
                }
                String str = f984k;
                String strF = aVar2.f(str);
                String str2 = f985l;
                String strF2 = aVar2.f(str2);
                aVar2.h(str);
                aVar2.h(str2);
                this.f995i = strF != null ? Long.parseLong(strF) : 0L;
                this.f996j = strF2 != null ? Long.parseLong(strF2) : 0L;
                this.f993g = aVar2.e();
                if (a()) {
                    String strG = kVarD.G();
                    if (strG.length() > 0) {
                        throw new IOException("expected \"\" but was \"" + strG + '\"');
                    }
                    this.f994h = s.f1216e.a(!kVarD.J() ? G.f955i.a(kVarD.G()) : G.SSL_3_0, C0198i.f1147s1.b(kVarD.G()), c(kVarD), c(kVarD));
                } else {
                    this.f994h = null;
                }
                f3.close();
            } catch (Throwable th) {
                f3.close();
                throw th;
            }
        }

        private final boolean a() {
            return K2.o.z(this.f987a, "https://", false, 2, null);
        }

        private final List c(b3.k kVar) throws IOException {
            int iC = C0192c.f971h.c(kVar);
            if (iC == -1) {
                return AbstractC0717n.g();
            }
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                ArrayList arrayList = new ArrayList(iC);
                for (int i3 = 0; i3 < iC; i3++) {
                    String strG = kVar.G();
                    b3.i iVar = new b3.i();
                    b3.l lVarB = b3.l.f5639f.b(strG);
                    D2.h.c(lVarB);
                    iVar.u(lVarB);
                    arrayList.add(certificateFactory.generateCertificate(iVar.q0()));
                }
                return arrayList;
            } catch (CertificateException e4) {
                throw new IOException(e4.getMessage());
            }
        }

        private final void e(b3.j jVar, List list) throws IOException {
            try {
                jVar.i0(list.size()).L(10);
                int size = list.size();
                for (int i3 = 0; i3 < size; i3++) {
                    byte[] encoded = ((Certificate) list.get(i3)).getEncoded();
                    l.a aVar = b3.l.f5639f;
                    D2.h.e(encoded, "bytes");
                    jVar.h0(l.a.h(aVar, encoded, 0, 0, 3, null).a()).L(10);
                }
            } catch (CertificateEncodingException e4) {
                throw new IOException(e4.getMessage());
            }
        }

        public final boolean b(B b4, D d4) {
            D2.h.f(b4, "request");
            D2.h.f(d4, "response");
            return D2.h.b(this.f987a, b4.l().toString()) && D2.h.b(this.f989c, b4.h()) && C0192c.f971h.g(d4, this.f988b, b4);
        }

        public final D d(d.C0026d c0026d) {
            D2.h.f(c0026d, "snapshot");
            String strA = this.f993g.a("Content-Type");
            String strA2 = this.f993g.a("Content-Length");
            return new D.a().r(new B.a().m(this.f987a).g(this.f989c, null).f(this.f988b).b()).p(this.f990d).g(this.f991e).m(this.f992f).k(this.f993g).b(new a(c0026d, strA, strA2)).i(this.f994h).s(this.f995i).q(this.f996j).c();
        }

        public final void f(d.b bVar) throws IOException {
            D2.h.f(bVar, "editor");
            b3.j jVarC = b3.t.c(bVar.f(0));
            try {
                jVarC.h0(this.f987a).L(10);
                jVarC.h0(this.f989c).L(10);
                jVarC.i0(this.f988b.size()).L(10);
                int size = this.f988b.size();
                for (int i3 = 0; i3 < size; i3++) {
                    jVarC.h0(this.f988b.b(i3)).h0(": ").h0(this.f988b.h(i3)).L(10);
                }
                jVarC.h0(new S2.k(this.f990d, this.f991e, this.f992f).toString()).L(10);
                jVarC.i0(this.f993g.size() + 2).L(10);
                int size2 = this.f993g.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    jVarC.h0(this.f993g.b(i4)).h0(": ").h0(this.f993g.h(i4)).L(10);
                }
                jVarC.h0(f984k).h0(": ").i0(this.f995i).L(10);
                jVarC.h0(f985l).h0(": ").i0(this.f996j).L(10);
                if (a()) {
                    jVarC.L(10);
                    s sVar = this.f994h;
                    D2.h.c(sVar);
                    jVarC.h0(sVar.a().c()).L(10);
                    e(jVarC, this.f994h.d());
                    e(jVarC, this.f994h.c());
                    jVarC.h0(this.f994h.e().a()).L(10);
                }
                r2.r rVar = r2.r.f10584a;
                A2.a.a(jVarC, null);
            } finally {
            }
        }

        public C0018c(D d4) {
            D2.h.f(d4, "response");
            this.f987a = d4.y0().l().toString();
            this.f988b = C0192c.f971h.f(d4);
            this.f989c = d4.y0().h();
            this.f990d = d4.w0();
            this.f991e = d4.A();
            this.f992f = d4.n0();
            this.f993g = d4.d0();
            this.f994h = d4.P();
            this.f995i = d4.z0();
            this.f996j = d4.x0();
        }
    }
}
