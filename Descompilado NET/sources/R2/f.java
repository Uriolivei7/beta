package R2;

import K2.o;
import M2.A;
import M2.B;
import M2.C0190a;
import M2.C0196g;
import M2.D;
import M2.F;
import M2.InterfaceC0194e;
import M2.l;
import M2.r;
import M2.s;
import M2.u;
import M2.z;
import U2.f;
import U2.m;
import U2.n;
import a3.d;
import b3.G;
import b3.t;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class f extends f.d implements M2.j {

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public static final a f2158t = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Socket f2159c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Socket f2160d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private s f2161e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private A f2162f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private U2.f f2163g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private b3.k f2164h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private b3.j f2165i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f2166j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f2167k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f2168l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f2169m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f2170n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f2171o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final List f2172p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private long f2173q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final h f2174r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final F f2175s;

    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static final class b extends D2.i implements C2.a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ C0196g f2176c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ s f2177d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ C0190a f2178e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(C0196g c0196g, s sVar, C0190a c0190a) {
            super(0);
            this.f2176c = c0196g;
            this.f2177d = sVar;
            this.f2178e = c0190a;
        }

        @Override // C2.a
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final List a() {
            Z2.c cVarD = this.f2176c.d();
            D2.h.c(cVarD);
            return cVarD.a(this.f2177d.d(), this.f2178e.l().h());
        }
    }

    static final class c extends D2.i implements C2.a {
        c() {
            super(0);
        }

        @Override // C2.a
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final List a() {
            s sVar = f.this.f2161e;
            D2.h.c(sVar);
            List<Certificate> listD = sVar.d();
            ArrayList arrayList = new ArrayList(AbstractC0717n.q(listD, 10));
            for (Certificate certificate : listD) {
                if (certificate == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                }
                arrayList.add((X509Certificate) certificate);
            }
            return arrayList;
        }
    }

    public static final class d extends d.AbstractC0048d {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ R2.c f2180e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ b3.k f2181f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ b3.j f2182g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(R2.c cVar, b3.k kVar, b3.j jVar, boolean z3, b3.k kVar2, b3.j jVar2) {
            super(z3, kVar2, jVar2);
            this.f2180e = cVar;
            this.f2181f = kVar;
            this.f2182g = jVar;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.f2180e.a(-1L, true, true, null);
        }
    }

    public f(h hVar, F f3) {
        D2.h.f(hVar, "connectionPool");
        D2.h.f(f3, "route");
        this.f2174r = hVar;
        this.f2175s = f3;
        this.f2171o = 1;
        this.f2172p = new ArrayList();
        this.f2173q = Long.MAX_VALUE;
    }

    private final boolean B(List list) {
        if (list != null && list.isEmpty()) {
            return false;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            F f3 = (F) it.next();
            Proxy.Type type = f3.b().type();
            Proxy.Type type2 = Proxy.Type.DIRECT;
            if (type == type2 && this.f2175s.b().type() == type2 && D2.h.b(this.f2175s.d(), f3.d())) {
                return true;
            }
        }
        return false;
    }

    private final void F(int i3) throws SocketException {
        Socket socket = this.f2160d;
        D2.h.c(socket);
        b3.k kVar = this.f2164h;
        D2.h.c(kVar);
        b3.j jVar = this.f2165i;
        D2.h.c(jVar);
        socket.setSoTimeout(0);
        U2.f fVarA = new f.b(true, Q2.e.f1870h).m(socket, this.f2175s.a().l().h(), kVar, jVar).k(this).l(i3).a();
        this.f2163g = fVarA;
        this.f2171o = U2.f.f2491E.a().d();
        U2.f.W0(fVarA, false, null, 3, null);
    }

    private final boolean G(u uVar) {
        s sVar;
        if (N2.c.f1409h && !Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        u uVarL = this.f2175s.a().l();
        if (uVar.l() != uVarL.l()) {
            return false;
        }
        if (D2.h.b(uVar.h(), uVarL.h())) {
            return true;
        }
        if (this.f2167k || (sVar = this.f2161e) == null) {
            return false;
        }
        D2.h.c(sVar);
        return e(uVar, sVar);
    }

    private final boolean e(u uVar, s sVar) {
        List listD = sVar.d();
        if (listD.isEmpty()) {
            return false;
        }
        Z2.d dVar = Z2.d.f2847a;
        String strH = uVar.h();
        Object obj = listD.get(0);
        if (obj != null) {
            return dVar.e(strH, (X509Certificate) obj);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.security.cert.X509Certificate");
    }

    private final void h(int i3, int i4, InterfaceC0194e interfaceC0194e, r rVar) throws IOException {
        Socket socket;
        int i5;
        Proxy proxyB = this.f2175s.b();
        C0190a c0190aA = this.f2175s.a();
        Proxy.Type type = proxyB.type();
        if (type != null && ((i5 = g.f2183a[type.ordinal()]) == 1 || i5 == 2)) {
            socket = c0190aA.j().createSocket();
            D2.h.c(socket);
        } else {
            socket = new Socket(proxyB);
        }
        this.f2159c = socket;
        rVar.j(interfaceC0194e, this.f2175s.d(), proxyB);
        socket.setSoTimeout(i4);
        try {
            W2.j.f2732c.g().f(socket, this.f2175s.d(), i3);
            try {
                this.f2164h = t.d(t.m(socket));
                this.f2165i = t.c(t.i(socket));
            } catch (NullPointerException e4) {
                if (D2.h.b(e4.getMessage(), "throw with null exception")) {
                    throw new IOException(e4);
                }
            }
        } catch (ConnectException e5) {
            ConnectException connectException = new ConnectException("Failed to connect to " + this.f2175s.d());
            connectException.initCause(e5);
            throw connectException;
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private final void i(R2.b bVar) throws Throwable {
        C0190a c0190aA = this.f2175s.a();
        SSLSocketFactory sSLSocketFactoryK = c0190aA.k();
        SSLSocket sSLSocket = null;
        try {
            D2.h.c(sSLSocketFactoryK);
            Socket socketCreateSocket = sSLSocketFactoryK.createSocket(this.f2159c, c0190aA.l().h(), c0190aA.l().l(), true);
            if (socketCreateSocket == null) {
                throw new NullPointerException("null cannot be cast to non-null type javax.net.ssl.SSLSocket");
            }
            SSLSocket sSLSocket2 = (SSLSocket) socketCreateSocket;
            try {
                l lVarA = bVar.a(sSLSocket2);
                if (lVarA.h()) {
                    W2.j.f2732c.g().e(sSLSocket2, c0190aA.l().h(), c0190aA.f());
                }
                sSLSocket2.startHandshake();
                SSLSession session = sSLSocket2.getSession();
                s.a aVar = s.f1216e;
                D2.h.e(session, "sslSocketSession");
                s sVarB = aVar.b(session);
                HostnameVerifier hostnameVerifierE = c0190aA.e();
                D2.h.c(hostnameVerifierE);
                if (hostnameVerifierE.verify(c0190aA.l().h(), session)) {
                    C0196g c0196gA = c0190aA.a();
                    D2.h.c(c0196gA);
                    this.f2161e = new s(sVarB.e(), sVarB.a(), sVarB.c(), new b(c0196gA, sVarB, c0190aA));
                    c0196gA.b(c0190aA.l().h(), new c());
                    String strH = lVarA.h() ? W2.j.f2732c.g().h(sSLSocket2) : null;
                    this.f2160d = sSLSocket2;
                    this.f2164h = t.d(t.m(sSLSocket2));
                    this.f2165i = t.c(t.i(sSLSocket2));
                    this.f2162f = strH != null ? A.f895j.a(strH) : A.HTTP_1_1;
                    W2.j.f2732c.g().b(sSLSocket2);
                    return;
                }
                List listD = sVarB.d();
                if (listD.isEmpty()) {
                    throw new SSLPeerUnverifiedException("Hostname " + c0190aA.l().h() + " not verified (no certificates)");
                }
                Object obj = listD.get(0);
                if (obj == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                }
                X509Certificate x509Certificate = (X509Certificate) obj;
                StringBuilder sb = new StringBuilder();
                sb.append("\n              |Hostname ");
                sb.append(c0190aA.l().h());
                sb.append(" not verified:\n              |    certificate: ");
                sb.append(C0196g.f1028d.a(x509Certificate));
                sb.append("\n              |    DN: ");
                Principal subjectDN = x509Certificate.getSubjectDN();
                D2.h.e(subjectDN, "cert.subjectDN");
                sb.append(subjectDN.getName());
                sb.append("\n              |    subjectAltNames: ");
                sb.append(Z2.d.f2847a.a(x509Certificate));
                sb.append("\n              ");
                throw new SSLPeerUnverifiedException(o.h(sb.toString(), null, 1, null));
            } catch (Throwable th) {
                th = th;
                sSLSocket = sSLSocket2;
                if (sSLSocket != null) {
                    W2.j.f2732c.g().b(sSLSocket);
                }
                if (sSLSocket != null) {
                    N2.c.k(sSLSocket);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private final void j(int i3, int i4, int i5, InterfaceC0194e interfaceC0194e, r rVar) throws IOException {
        B bL = l();
        u uVarL = bL.l();
        for (int i6 = 0; i6 < 21; i6++) {
            h(i3, i4, interfaceC0194e, rVar);
            bL = k(i4, i5, bL, uVarL);
            if (bL == null) {
                return;
            }
            Socket socket = this.f2159c;
            if (socket != null) {
                N2.c.k(socket);
            }
            this.f2159c = null;
            this.f2165i = null;
            this.f2164h = null;
            rVar.h(interfaceC0194e, this.f2175s.d(), this.f2175s.b(), null);
        }
    }

    private final B k(int i3, int i4, B b4, u uVar) throws IOException {
        String str = "CONNECT " + N2.c.P(uVar, true) + " HTTP/1.1";
        while (true) {
            b3.k kVar = this.f2164h;
            D2.h.c(kVar);
            b3.j jVar = this.f2165i;
            D2.h.c(jVar);
            T2.b bVar = new T2.b(null, this, kVar, jVar);
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            kVar.f().g(i3, timeUnit);
            jVar.f().g(i4, timeUnit);
            bVar.A(b4.e(), str);
            bVar.a();
            D.a aVarG = bVar.g(false);
            D2.h.c(aVarG);
            D dC = aVarG.r(b4).c();
            bVar.z(dC);
            int iA = dC.A();
            if (iA == 200) {
                if (kVar.e().J() && jVar.e().J()) {
                    return null;
                }
                throw new IOException("TLS tunnel buffered too many bytes!");
            }
            if (iA != 407) {
                throw new IOException("Unexpected response code for CONNECT: " + dC.A());
            }
            B bA = this.f2175s.a().h().a(this.f2175s, dC);
            if (bA == null) {
                throw new IOException("Failed to authenticate with proxy");
            }
            if (o.n("close", D.c0(dC, "Connection", null, 2, null), true)) {
                return bA;
            }
            b4 = bA;
        }
    }

    private final B l() {
        B b4 = new B.a().l(this.f2175s.a().l()).g("CONNECT", null).e("Host", N2.c.P(this.f2175s.a().l(), true)).e("Proxy-Connection", "Keep-Alive").e("User-Agent", "okhttp/4.9.2").b();
        B bA = this.f2175s.a().h().a(this.f2175s, new D.a().r(b4).p(A.HTTP_1_1).g(407).m("Preemptive Authenticate").b(N2.c.f1404c).s(-1L).q(-1L).j("Proxy-Authenticate", "OkHttp-Preemptive").c());
        return bA != null ? bA : b4;
    }

    private final void m(R2.b bVar, int i3, InterfaceC0194e interfaceC0194e, r rVar) throws Throwable {
        if (this.f2175s.a().k() != null) {
            rVar.C(interfaceC0194e);
            i(bVar);
            rVar.B(interfaceC0194e, this.f2161e);
            if (this.f2162f == A.HTTP_2) {
                F(i3);
                return;
            }
            return;
        }
        List listF = this.f2175s.a().f();
        A a4 = A.H2_PRIOR_KNOWLEDGE;
        if (!listF.contains(a4)) {
            this.f2160d = this.f2159c;
            this.f2162f = A.HTTP_1_1;
        } else {
            this.f2160d = this.f2159c;
            this.f2162f = a4;
            F(i3);
        }
    }

    public F A() {
        return this.f2175s;
    }

    public final void C(long j3) {
        this.f2173q = j3;
    }

    public final void D(boolean z3) {
        this.f2166j = z3;
    }

    public Socket E() {
        Socket socket = this.f2160d;
        D2.h.c(socket);
        return socket;
    }

    public final synchronized void H(e eVar, IOException iOException) {
        try {
            D2.h.f(eVar, "call");
            if (iOException instanceof n) {
                if (((n) iOException).f2667b == U2.b.REFUSED_STREAM) {
                    int i3 = this.f2170n + 1;
                    this.f2170n = i3;
                    if (i3 > 1) {
                        this.f2166j = true;
                        this.f2168l++;
                    }
                } else if (((n) iOException).f2667b != U2.b.CANCEL || !eVar.q()) {
                    this.f2166j = true;
                    this.f2168l++;
                }
            } else if (!v() || (iOException instanceof U2.a)) {
                this.f2166j = true;
                if (this.f2169m == 0) {
                    if (iOException != null) {
                        g(eVar.l(), this.f2175s, iOException);
                    }
                    this.f2168l++;
                }
            }
        } finally {
        }
    }

    @Override // U2.f.d
    public synchronized void a(U2.f fVar, m mVar) {
        D2.h.f(fVar, "connection");
        D2.h.f(mVar, "settings");
        this.f2171o = mVar.d();
    }

    @Override // U2.f.d
    public void b(U2.i iVar) {
        D2.h.f(iVar, "stream");
        iVar.d(U2.b.REFUSED_STREAM, null);
    }

    public final void d() {
        Socket socket = this.f2159c;
        if (socket != null) {
            N2.c.k(socket);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x014e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void f(int r17, int r18, int r19, int r20, boolean r21, M2.InterfaceC0194e r22, M2.r r23) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 356
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: R2.f.f(int, int, int, int, boolean, M2.e, M2.r):void");
    }

    public final void g(z zVar, F f3, IOException iOException) {
        D2.h.f(zVar, "client");
        D2.h.f(f3, "failedRoute");
        D2.h.f(iOException, "failure");
        if (f3.b().type() != Proxy.Type.DIRECT) {
            C0190a c0190aA = f3.a();
            c0190aA.i().connectFailed(c0190aA.l().q(), f3.b().address(), iOException);
        }
        zVar.x().b(f3);
    }

    public final List n() {
        return this.f2172p;
    }

    public final long o() {
        return this.f2173q;
    }

    public final boolean p() {
        return this.f2166j;
    }

    public final int q() {
        return this.f2168l;
    }

    public s r() {
        return this.f2161e;
    }

    public final synchronized void s() {
        this.f2169m++;
    }

    public final boolean t(C0190a c0190a, List list) {
        D2.h.f(c0190a, "address");
        if (N2.c.f1409h && !Thread.holdsLock(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            D2.h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(this);
            throw new AssertionError(sb.toString());
        }
        if (this.f2172p.size() >= this.f2171o || this.f2166j || !this.f2175s.a().d(c0190a)) {
            return false;
        }
        if (D2.h.b(c0190a.l().h(), A().a().l().h())) {
            return true;
        }
        if (this.f2163g == null || list == null || !B(list) || c0190a.e() != Z2.d.f2847a || !G(c0190a.l())) {
            return false;
        }
        try {
            C0196g c0196gA = c0190a.a();
            D2.h.c(c0196gA);
            String strH = c0190a.l().h();
            s sVarR = r();
            D2.h.c(sVarR);
            c0196gA.a(strH, sVarR.d());
            return true;
        } catch (SSLPeerUnverifiedException unused) {
            return false;
        }
    }

    public String toString() {
        Object objA;
        StringBuilder sb = new StringBuilder();
        sb.append("Connection{");
        sb.append(this.f2175s.a().l().h());
        sb.append(':');
        sb.append(this.f2175s.a().l().l());
        sb.append(',');
        sb.append(" proxy=");
        sb.append(this.f2175s.b());
        sb.append(" hostAddress=");
        sb.append(this.f2175s.d());
        sb.append(" cipherSuite=");
        s sVar = this.f2161e;
        if (sVar == null || (objA = sVar.a()) == null) {
            objA = "none";
        }
        sb.append(objA);
        sb.append(" protocol=");
        sb.append(this.f2162f);
        sb.append('}');
        return sb.toString();
    }

    public final boolean u(boolean z3) {
        long j3;
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
        long jNanoTime = System.nanoTime();
        Socket socket = this.f2159c;
        D2.h.c(socket);
        Socket socket2 = this.f2160d;
        D2.h.c(socket2);
        b3.k kVar = this.f2164h;
        D2.h.c(kVar);
        if (socket.isClosed() || socket2.isClosed() || socket2.isInputShutdown() || socket2.isOutputShutdown()) {
            return false;
        }
        U2.f fVar = this.f2163g;
        if (fVar != null) {
            return fVar.I0(jNanoTime);
        }
        synchronized (this) {
            j3 = jNanoTime - this.f2173q;
        }
        if (j3 < 10000000000L || !z3) {
            return true;
        }
        return N2.c.D(socket2, kVar);
    }

    public final boolean v() {
        return this.f2163g != null;
    }

    public final S2.d w(z zVar, S2.g gVar) throws SocketException {
        D2.h.f(zVar, "client");
        D2.h.f(gVar, "chain");
        Socket socket = this.f2160d;
        D2.h.c(socket);
        b3.k kVar = this.f2164h;
        D2.h.c(kVar);
        b3.j jVar = this.f2165i;
        D2.h.c(jVar);
        U2.f fVar = this.f2163g;
        if (fVar != null) {
            return new U2.g(zVar, this, gVar, fVar);
        }
        socket.setSoTimeout(gVar.k());
        G gF = kVar.f();
        long jG = gVar.g();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        gF.g(jG, timeUnit);
        jVar.f().g(gVar.j(), timeUnit);
        return new T2.b(zVar, this, kVar, jVar);
    }

    public final d.AbstractC0048d x(R2.c cVar) throws SocketException {
        D2.h.f(cVar, "exchange");
        Socket socket = this.f2160d;
        D2.h.c(socket);
        b3.k kVar = this.f2164h;
        D2.h.c(kVar);
        b3.j jVar = this.f2165i;
        D2.h.c(jVar);
        socket.setSoTimeout(0);
        z();
        return new d(cVar, kVar, jVar, true, kVar, jVar);
    }

    public final synchronized void y() {
        this.f2167k = true;
    }

    public final synchronized void z() {
        this.f2166j = true;
    }
}
