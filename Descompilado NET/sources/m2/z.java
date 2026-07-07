package M2;

import M2.InterfaceC0194e;
import M2.r;
import W2.j;
import Z2.c;
import java.net.Proxy;
import java.net.ProxySelector;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public class z implements Cloneable, InterfaceC0194e.a {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private final int f1279A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private final int f1280B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private final int f1281C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final long f1282D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private final R2.i f1283E;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final p f1284b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final k f1285c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f1286d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final List f1287e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final r.c f1288f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f1289g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final InterfaceC0191b f1290h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final boolean f1291i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final boolean f1292j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final n f1293k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final C0192c f1294l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final q f1295m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final Proxy f1296n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final ProxySelector f1297o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final InterfaceC0191b f1298p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final SocketFactory f1299q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final SSLSocketFactory f1300r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final X509TrustManager f1301s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final List f1302t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final List f1303u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private final HostnameVerifier f1304v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private final C0196g f1305w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private final Z2.c f1306x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final int f1307y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private final int f1308z;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    public static final b f1278H = new b(null);

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private static final List f1276F = N2.c.t(A.HTTP_2, A.HTTP_1_1);

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private static final List f1277G = N2.c.t(l.f1167h, l.f1169j);

    public static final class b {
        private b() {
        }

        public final List a() {
            return z.f1277G;
        }

        public final List b() {
            return z.f1276F;
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public z(a aVar) throws NoSuchAlgorithmException, KeyStoreException {
        ProxySelector proxySelectorD;
        D2.h.f(aVar, "builder");
        this.f1284b = aVar.q();
        this.f1285c = aVar.n();
        this.f1286d = N2.c.R(aVar.w());
        this.f1287e = N2.c.R(aVar.y());
        this.f1288f = aVar.s();
        this.f1289g = aVar.F();
        this.f1290h = aVar.h();
        this.f1291i = aVar.t();
        this.f1292j = aVar.u();
        this.f1293k = aVar.p();
        this.f1294l = aVar.i();
        this.f1295m = aVar.r();
        this.f1296n = aVar.B();
        if (aVar.B() != null) {
            proxySelectorD = Y2.a.f2795a;
        } else {
            proxySelectorD = aVar.D();
            proxySelectorD = proxySelectorD == null ? ProxySelector.getDefault() : proxySelectorD;
            if (proxySelectorD == null) {
                proxySelectorD = Y2.a.f2795a;
            }
        }
        this.f1297o = proxySelectorD;
        this.f1298p = aVar.C();
        this.f1299q = aVar.H();
        List listO = aVar.o();
        this.f1302t = listO;
        this.f1303u = aVar.A();
        this.f1304v = aVar.v();
        this.f1307y = aVar.j();
        this.f1308z = aVar.m();
        this.f1279A = aVar.E();
        this.f1280B = aVar.J();
        this.f1281C = aVar.z();
        this.f1282D = aVar.x();
        R2.i iVarG = aVar.G();
        this.f1283E = iVarG == null ? new R2.i() : iVarG;
        if (listO == null || !listO.isEmpty()) {
            Iterator it = listO.iterator();
            while (it.hasNext()) {
                if (((l) it.next()).f()) {
                    if (aVar.I() != null) {
                        this.f1300r = aVar.I();
                        Z2.c cVarK = aVar.k();
                        D2.h.c(cVarK);
                        this.f1306x = cVarK;
                        X509TrustManager x509TrustManagerK = aVar.K();
                        D2.h.c(x509TrustManagerK);
                        this.f1301s = x509TrustManagerK;
                        C0196g c0196gL = aVar.l();
                        D2.h.c(cVarK);
                        this.f1305w = c0196gL.e(cVarK);
                    } else {
                        j.a aVar2 = W2.j.f2732c;
                        X509TrustManager x509TrustManagerP = aVar2.g().p();
                        this.f1301s = x509TrustManagerP;
                        W2.j jVarG = aVar2.g();
                        D2.h.c(x509TrustManagerP);
                        this.f1300r = jVarG.o(x509TrustManagerP);
                        c.a aVar3 = Z2.c.f2846a;
                        D2.h.c(x509TrustManagerP);
                        Z2.c cVarA = aVar3.a(x509TrustManagerP);
                        this.f1306x = cVarA;
                        C0196g c0196gL2 = aVar.l();
                        D2.h.c(cVarA);
                        this.f1305w = c0196gL2.e(cVarA);
                    }
                }
            }
            this.f1300r = null;
            this.f1306x = null;
            this.f1301s = null;
            this.f1305w = C0196g.f1027c;
        } else {
            this.f1300r = null;
            this.f1306x = null;
            this.f1301s = null;
            this.f1305w = C0196g.f1027c;
        }
        N();
    }

    private final void N() {
        List list = this.f1286d;
        if (list == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<okhttp3.Interceptor?>");
        }
        if (list.contains(null)) {
            throw new IllegalStateException(("Null interceptor: " + this.f1286d).toString());
        }
        List list2 = this.f1287e;
        if (list2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<okhttp3.Interceptor?>");
        }
        if (list2.contains(null)) {
            throw new IllegalStateException(("Null network interceptor: " + this.f1287e).toString());
        }
        List list3 = this.f1302t;
        if (list3 == null || !list3.isEmpty()) {
            Iterator it = list3.iterator();
            while (it.hasNext()) {
                if (((l) it.next()).f()) {
                    if (this.f1300r == null) {
                        throw new IllegalStateException("sslSocketFactory == null");
                    }
                    if (this.f1306x == null) {
                        throw new IllegalStateException("certificateChainCleaner == null");
                    }
                    if (this.f1301s == null) {
                        throw new IllegalStateException("x509TrustManager == null");
                    }
                    return;
                }
            }
        }
        if (!(this.f1300r == null)) {
            throw new IllegalStateException("Check failed.");
        }
        if (!(this.f1306x == null)) {
            throw new IllegalStateException("Check failed.");
        }
        if (!(this.f1301s == null)) {
            throw new IllegalStateException("Check failed.");
        }
        if (!D2.h.b(this.f1305w, C0196g.f1027c)) {
            throw new IllegalStateException("Check failed.");
        }
    }

    public final long A() {
        return this.f1282D;
    }

    public final List B() {
        return this.f1287e;
    }

    public a C() {
        return new a(this);
    }

    public H D(B b4, I i3) {
        D2.h.f(b4, "request");
        D2.h.f(i3, "listener");
        a3.d dVar = new a3.d(Q2.e.f1870h, b4, i3, new Random(), this.f1281C, null, this.f1282D);
        dVar.p(this);
        return dVar;
    }

    public final int E() {
        return this.f1281C;
    }

    public final List F() {
        return this.f1303u;
    }

    public final Proxy G() {
        return this.f1296n;
    }

    public final InterfaceC0191b H() {
        return this.f1298p;
    }

    public final ProxySelector I() {
        return this.f1297o;
    }

    public final int J() {
        return this.f1279A;
    }

    public final boolean K() {
        return this.f1289g;
    }

    public final SocketFactory L() {
        return this.f1299q;
    }

    public final SSLSocketFactory M() {
        SSLSocketFactory sSLSocketFactory = this.f1300r;
        if (sSLSocketFactory != null) {
            return sSLSocketFactory;
        }
        throw new IllegalStateException("CLEARTEXT-only client");
    }

    public final int O() {
        return this.f1280B;
    }

    public final X509TrustManager P() {
        return this.f1301s;
    }

    @Override // M2.InterfaceC0194e.a
    public InterfaceC0194e b(B b4) {
        D2.h.f(b4, "request");
        return new R2.e(this, b4, false);
    }

    public final p c() {
        return this.f1284b;
    }

    public Object clone() {
        return super.clone();
    }

    public final InterfaceC0191b g() {
        return this.f1290h;
    }

    public final C0192c h() {
        return this.f1294l;
    }

    public final int j() {
        return this.f1307y;
    }

    public final Z2.c k() {
        return this.f1306x;
    }

    public final C0196g l() {
        return this.f1305w;
    }

    public final int m() {
        return this.f1308z;
    }

    public final k n() {
        return this.f1285c;
    }

    public final List p() {
        return this.f1302t;
    }

    public final n r() {
        return this.f1293k;
    }

    public final p s() {
        return this.f1284b;
    }

    public final q t() {
        return this.f1295m;
    }

    public final r.c u() {
        return this.f1288f;
    }

    public final boolean v() {
        return this.f1291i;
    }

    public final boolean w() {
        return this.f1292j;
    }

    public final R2.i x() {
        return this.f1283E;
    }

    public final HostnameVerifier y() {
        return this.f1304v;
    }

    public final List z() {
        return this.f1286d;
    }

    public static final class a {

        /* JADX INFO: renamed from: A, reason: collision with root package name */
        private int f1309A;

        /* JADX INFO: renamed from: B, reason: collision with root package name */
        private int f1310B;

        /* JADX INFO: renamed from: C, reason: collision with root package name */
        private long f1311C;

        /* JADX INFO: renamed from: D, reason: collision with root package name */
        private R2.i f1312D;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private p f1313a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private k f1314b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final List f1315c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final List f1316d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private r.c f1317e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f1318f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private InterfaceC0191b f1319g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private boolean f1320h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private boolean f1321i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private n f1322j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private C0192c f1323k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private q f1324l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        private Proxy f1325m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        private ProxySelector f1326n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        private InterfaceC0191b f1327o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        private SocketFactory f1328p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        private SSLSocketFactory f1329q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        private X509TrustManager f1330r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        private List f1331s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        private List f1332t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        private HostnameVerifier f1333u;

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        private C0196g f1334v;

        /* JADX INFO: renamed from: w, reason: collision with root package name */
        private Z2.c f1335w;

        /* JADX INFO: renamed from: x, reason: collision with root package name */
        private int f1336x;

        /* JADX INFO: renamed from: y, reason: collision with root package name */
        private int f1337y;

        /* JADX INFO: renamed from: z, reason: collision with root package name */
        private int f1338z;

        public a() {
            this.f1313a = new p();
            this.f1314b = new k();
            this.f1315c = new ArrayList();
            this.f1316d = new ArrayList();
            this.f1317e = N2.c.e(r.f1214a);
            this.f1318f = true;
            InterfaceC0191b interfaceC0191b = InterfaceC0191b.f968a;
            this.f1319g = interfaceC0191b;
            this.f1320h = true;
            this.f1321i = true;
            this.f1322j = n.f1202a;
            this.f1324l = q.f1212a;
            this.f1327o = interfaceC0191b;
            SocketFactory socketFactory = SocketFactory.getDefault();
            D2.h.e(socketFactory, "SocketFactory.getDefault()");
            this.f1328p = socketFactory;
            b bVar = z.f1278H;
            this.f1331s = bVar.a();
            this.f1332t = bVar.b();
            this.f1333u = Z2.d.f2847a;
            this.f1334v = C0196g.f1027c;
            this.f1337y = 10000;
            this.f1338z = 10000;
            this.f1309A = 10000;
            this.f1311C = 1024L;
        }

        public final List A() {
            return this.f1332t;
        }

        public final Proxy B() {
            return this.f1325m;
        }

        public final InterfaceC0191b C() {
            return this.f1327o;
        }

        public final ProxySelector D() {
            return this.f1326n;
        }

        public final int E() {
            return this.f1338z;
        }

        public final boolean F() {
            return this.f1318f;
        }

        public final R2.i G() {
            return this.f1312D;
        }

        public final SocketFactory H() {
            return this.f1328p;
        }

        public final SSLSocketFactory I() {
            return this.f1329q;
        }

        public final int J() {
            return this.f1309A;
        }

        public final X509TrustManager K() {
            return this.f1330r;
        }

        public final a L(List list) {
            D2.h.f(list, "protocols");
            List listG0 = AbstractC0717n.g0(list);
            A a4 = A.H2_PRIOR_KNOWLEDGE;
            if (!(listG0.contains(a4) || listG0.contains(A.HTTP_1_1))) {
                throw new IllegalArgumentException(("protocols must contain h2_prior_knowledge or http/1.1: " + listG0).toString());
            }
            if (!(!listG0.contains(a4) || listG0.size() <= 1)) {
                throw new IllegalArgumentException(("protocols containing h2_prior_knowledge cannot use other protocols: " + listG0).toString());
            }
            if (listG0.contains(A.HTTP_1_0)) {
                throw new IllegalArgumentException(("protocols must not contain http/1.0: " + listG0).toString());
            }
            if (listG0.contains(null)) {
                throw new IllegalArgumentException("protocols must not contain null");
            }
            listG0.remove(A.SPDY_3);
            if (!D2.h.b(listG0, this.f1332t)) {
                this.f1312D = null;
            }
            List listUnmodifiableList = Collections.unmodifiableList(listG0);
            D2.h.e(listUnmodifiableList, "Collections.unmodifiableList(protocolsCopy)");
            this.f1332t = listUnmodifiableList;
            return this;
        }

        public final a M(long j3, TimeUnit timeUnit) {
            D2.h.f(timeUnit, "unit");
            this.f1338z = N2.c.h("timeout", j3, timeUnit);
            return this;
        }

        public final a N(long j3, TimeUnit timeUnit) {
            D2.h.f(timeUnit, "unit");
            this.f1309A = N2.c.h("timeout", j3, timeUnit);
            return this;
        }

        public final a a(v vVar) {
            D2.h.f(vVar, "interceptor");
            this.f1316d.add(vVar);
            return this;
        }

        public final z b() {
            return new z(this);
        }

        public final a c(C0192c c0192c) {
            this.f1323k = c0192c;
            return this;
        }

        public final a d(long j3, TimeUnit timeUnit) {
            D2.h.f(timeUnit, "unit");
            this.f1336x = N2.c.h("timeout", j3, timeUnit);
            return this;
        }

        public final a e(long j3, TimeUnit timeUnit) {
            D2.h.f(timeUnit, "unit");
            this.f1337y = N2.c.h("timeout", j3, timeUnit);
            return this;
        }

        public final a f(n nVar) {
            D2.h.f(nVar, "cookieJar");
            this.f1322j = nVar;
            return this;
        }

        public final a g(r rVar) {
            D2.h.f(rVar, "eventListener");
            this.f1317e = N2.c.e(rVar);
            return this;
        }

        public final InterfaceC0191b h() {
            return this.f1319g;
        }

        public final C0192c i() {
            return this.f1323k;
        }

        public final int j() {
            return this.f1336x;
        }

        public final Z2.c k() {
            return this.f1335w;
        }

        public final C0196g l() {
            return this.f1334v;
        }

        public final int m() {
            return this.f1337y;
        }

        public final k n() {
            return this.f1314b;
        }

        public final List o() {
            return this.f1331s;
        }

        public final n p() {
            return this.f1322j;
        }

        public final p q() {
            return this.f1313a;
        }

        public final q r() {
            return this.f1324l;
        }

        public final r.c s() {
            return this.f1317e;
        }

        public final boolean t() {
            return this.f1320h;
        }

        public final boolean u() {
            return this.f1321i;
        }

        public final HostnameVerifier v() {
            return this.f1333u;
        }

        public final List w() {
            return this.f1315c;
        }

        public final long x() {
            return this.f1311C;
        }

        public final List y() {
            return this.f1316d;
        }

        public final int z() {
            return this.f1310B;
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public a(z zVar) {
            this();
            D2.h.f(zVar, "okHttpClient");
            this.f1313a = zVar.s();
            this.f1314b = zVar.n();
            AbstractC0717n.t(this.f1315c, zVar.z());
            AbstractC0717n.t(this.f1316d, zVar.B());
            this.f1317e = zVar.u();
            this.f1318f = zVar.K();
            this.f1319g = zVar.g();
            this.f1320h = zVar.v();
            this.f1321i = zVar.w();
            this.f1322j = zVar.r();
            this.f1323k = zVar.h();
            this.f1324l = zVar.t();
            this.f1325m = zVar.G();
            this.f1326n = zVar.I();
            this.f1327o = zVar.H();
            this.f1328p = zVar.L();
            this.f1329q = zVar.f1300r;
            this.f1330r = zVar.P();
            this.f1331s = zVar.p();
            this.f1332t = zVar.F();
            this.f1333u = zVar.y();
            this.f1334v = zVar.l();
            this.f1335w = zVar.k();
            this.f1336x = zVar.j();
            this.f1337y = zVar.m();
            this.f1338z = zVar.J();
            this.f1309A = zVar.O();
            this.f1310B = zVar.E();
            this.f1311C = zVar.A();
            this.f1312D = zVar.x();
        }
    }

    public z() {
        this(new a());
    }
}
