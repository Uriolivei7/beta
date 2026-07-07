package a3;

import D2.p;
import D2.r;
import K2.o;
import M2.A;
import M2.B;
import M2.D;
import M2.H;
import M2.I;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import M2.z;
import a3.g;
import b3.j;
import b3.k;
import b3.l;
import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class d implements H, g.a {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    public static final b f2875A = new b(null);

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private static final List f2876z = AbstractC0717n.b(A.HTTP_1_1);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f2877a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private InterfaceC0194e f2878b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Q2.a f2879c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private a3.g f2880d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private a3.h f2881e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Q2.d f2882f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private String f2883g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private AbstractC0048d f2884h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final ArrayDeque f2885i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final ArrayDeque f2886j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private long f2887k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f2888l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f2889m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private String f2890n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f2891o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f2892p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f2893q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f2894r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f2895s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final B f2896t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final I f2897u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private final Random f2898v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private final long f2899w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private a3.e f2900x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private long f2901y;

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f2902a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final l f2903b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final long f2904c;

        public a(int i3, l lVar, long j3) {
            this.f2902a = i3;
            this.f2903b = lVar;
            this.f2904c = j3;
        }

        public final long a() {
            return this.f2904c;
        }

        public final int b() {
            return this.f2902a;
        }

        public final l c() {
            return this.f2903b;
        }
    }

    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f2905a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final l f2906b;

        public c(int i3, l lVar) {
            D2.h.f(lVar, "data");
            this.f2905a = i3;
            this.f2906b = lVar;
        }

        public final l a() {
            return this.f2906b;
        }

        public final int b() {
            return this.f2905a;
        }
    }

    /* JADX INFO: renamed from: a3.d$d, reason: collision with other inner class name */
    public static abstract class AbstractC0048d implements Closeable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final boolean f2907b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final k f2908c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final j f2909d;

        public AbstractC0048d(boolean z3, k kVar, j jVar) {
            D2.h.f(kVar, "source");
            D2.h.f(jVar, "sink");
            this.f2907b = z3;
            this.f2908c = kVar;
            this.f2909d = jVar;
        }

        public final boolean a() {
            return this.f2907b;
        }

        public final j i() {
            return this.f2909d;
        }

        public final k o() {
            return this.f2908c;
        }
    }

    private final class e extends Q2.a {
        public e() {
            super(d.this.f2883g + " writer", false, 2, null);
        }

        @Override // Q2.a
        public long f() {
            try {
                return d.this.x() ? 0L : -1L;
            } catch (IOException e4) {
                d.this.q(e4, null);
                return -1L;
            }
        }
    }

    public static final class f implements InterfaceC0195f {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ B f2912b;

        f(B b4) {
            this.f2912b = b4;
        }

        @Override // M2.InterfaceC0195f
        public void a(InterfaceC0194e interfaceC0194e, IOException iOException) {
            D2.h.f(interfaceC0194e, "call");
            D2.h.f(iOException, "e");
            d.this.q(iOException, null);
        }

        @Override // M2.InterfaceC0195f
        public void b(InterfaceC0194e interfaceC0194e, D d4) {
            D2.h.f(interfaceC0194e, "call");
            D2.h.f(d4, "response");
            R2.c cVarD = d4.D();
            try {
                d.this.n(d4, cVarD);
                D2.h.c(cVarD);
                AbstractC0048d abstractC0048dM = cVarD.m();
                a3.e eVarA = a3.e.f2930g.a(d4.d0());
                d.this.f2900x = eVarA;
                if (!d.this.t(eVarA)) {
                    synchronized (d.this) {
                        d.this.f2886j.clear();
                        d.this.a(1010, "unexpected Sec-WebSocket-Extensions in response header");
                    }
                }
                try {
                    d.this.s(N2.c.f1410i + " WebSocket " + this.f2912b.l().n(), abstractC0048dM);
                    d.this.r().f(d.this, d4);
                    d.this.u();
                } catch (Exception e4) {
                    d.this.q(e4, null);
                }
            } catch (IOException e5) {
                if (cVarD != null) {
                    cVarD.u();
                }
                d.this.q(e5, d4);
                N2.c.j(d4);
            }
        }
    }

    public static final class g extends Q2.a {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f2913e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ long f2914f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ d f2915g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ String f2916h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ AbstractC0048d f2917i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        final /* synthetic */ a3.e f2918j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public g(String str, String str2, long j3, d dVar, String str3, AbstractC0048d abstractC0048d, a3.e eVar) {
            super(str2, false, 2, null);
            this.f2913e = str;
            this.f2914f = j3;
            this.f2915g = dVar;
            this.f2916h = str3;
            this.f2917i = abstractC0048d;
            this.f2918j = eVar;
        }

        @Override // Q2.a
        public long f() {
            this.f2915g.y();
            return this.f2914f;
        }
    }

    public static final class h extends Q2.a {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f2919e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ boolean f2920f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ d f2921g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ a3.h f2922h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ l f2923i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        final /* synthetic */ r f2924j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        final /* synthetic */ p f2925k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        final /* synthetic */ r f2926l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        final /* synthetic */ r f2927m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        final /* synthetic */ r f2928n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        final /* synthetic */ r f2929o;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public h(String str, boolean z3, String str2, boolean z4, d dVar, a3.h hVar, l lVar, r rVar, p pVar, r rVar2, r rVar3, r rVar4, r rVar5) {
            super(str2, z4);
            this.f2919e = str;
            this.f2920f = z3;
            this.f2921g = dVar;
            this.f2922h = hVar;
            this.f2923i = lVar;
            this.f2924j = rVar;
            this.f2925k = pVar;
            this.f2926l = rVar2;
            this.f2927m = rVar3;
            this.f2928n = rVar4;
            this.f2929o = rVar5;
        }

        @Override // Q2.a
        public long f() {
            this.f2921g.m();
            return -1L;
        }
    }

    public d(Q2.e eVar, B b4, I i3, Random random, long j3, a3.e eVar2, long j4) {
        D2.h.f(eVar, "taskRunner");
        D2.h.f(b4, "originalRequest");
        D2.h.f(i3, "listener");
        D2.h.f(random, "random");
        this.f2896t = b4;
        this.f2897u = i3;
        this.f2898v = random;
        this.f2899w = j3;
        this.f2900x = eVar2;
        this.f2901y = j4;
        this.f2882f = eVar.i();
        this.f2885i = new ArrayDeque();
        this.f2886j = new ArrayDeque();
        this.f2889m = -1;
        if (!D2.h.b("GET", b4.h())) {
            throw new IllegalArgumentException(("Request must be GET: " + b4.h()).toString());
        }
        l.a aVar = l.f5639f;
        byte[] bArr = new byte[16];
        random.nextBytes(bArr);
        r2.r rVar = r2.r.f10584a;
        this.f2877a = l.a.h(aVar, bArr, 0, 0, 3, null).a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean t(a3.e eVar) {
        if (eVar.f2936f || eVar.f2932b != null) {
            return false;
        }
        Integer num = eVar.f2934d;
        if (num == null) {
            return true;
        }
        int iIntValue = num.intValue();
        return 8 <= iIntValue && 15 >= iIntValue;
    }

    private final void v() {
        if (!N2.c.f1409h || Thread.holdsLock(this)) {
            Q2.a aVar = this.f2879c;
            if (aVar != null) {
                Q2.d.j(this.f2882f, aVar, 0L, 2, null);
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ");
        Thread threadCurrentThread = Thread.currentThread();
        D2.h.e(threadCurrentThread, "Thread.currentThread()");
        sb.append(threadCurrentThread.getName());
        sb.append(" MUST hold lock on ");
        sb.append(this);
        throw new AssertionError(sb.toString());
    }

    private final synchronized boolean w(l lVar, int i3) {
        if (!this.f2891o && !this.f2888l) {
            if (this.f2887k + ((long) lVar.v()) > 16777216) {
                a(1001, null);
                return false;
            }
            this.f2887k += (long) lVar.v();
            this.f2886j.add(new c(i3, lVar));
            v();
            return true;
        }
        return false;
    }

    @Override // M2.H
    public boolean a(int i3, String str) {
        return o(i3, str, 60000L);
    }

    @Override // M2.H
    public boolean b(String str) {
        D2.h.f(str, "text");
        return w(l.f5639f.e(str), 1);
    }

    @Override // a3.g.a
    public synchronized void c(l lVar) {
        try {
            D2.h.f(lVar, "payload");
            if (!this.f2891o && (!this.f2888l || !this.f2886j.isEmpty())) {
                this.f2885i.add(lVar);
                v();
                this.f2893q++;
            }
        } finally {
        }
    }

    @Override // a3.g.a
    public synchronized void d(l lVar) {
        D2.h.f(lVar, "payload");
        this.f2894r++;
        this.f2895s = false;
    }

    @Override // a3.g.a
    public void e(String str) {
        D2.h.f(str, "text");
        this.f2897u.e(this, str);
    }

    @Override // M2.H
    public boolean f(l lVar) {
        D2.h.f(lVar, "bytes");
        return w(lVar, 2);
    }

    @Override // a3.g.a
    public void g(l lVar) {
        D2.h.f(lVar, "bytes");
        this.f2897u.d(this, lVar);
    }

    @Override // a3.g.a
    public void h(int i3, String str) {
        AbstractC0048d abstractC0048d;
        a3.g gVar;
        a3.h hVar;
        D2.h.f(str, "reason");
        if (!(i3 != -1)) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        synchronized (this) {
            try {
                if (!(this.f2889m == -1)) {
                    throw new IllegalStateException("already closed");
                }
                this.f2889m = i3;
                this.f2890n = str;
                abstractC0048d = null;
                if (this.f2888l && this.f2886j.isEmpty()) {
                    AbstractC0048d abstractC0048d2 = this.f2884h;
                    this.f2884h = null;
                    gVar = this.f2880d;
                    this.f2880d = null;
                    hVar = this.f2881e;
                    this.f2881e = null;
                    this.f2882f.n();
                    abstractC0048d = abstractC0048d2;
                } else {
                    gVar = null;
                    hVar = null;
                }
                r2.r rVar = r2.r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
        try {
            this.f2897u.b(this, i3, str);
            if (abstractC0048d != null) {
                this.f2897u.a(this, i3, str);
            }
        } finally {
            if (abstractC0048d != null) {
                N2.c.j(abstractC0048d);
            }
            if (gVar != null) {
                N2.c.j(gVar);
            }
            if (hVar != null) {
                N2.c.j(hVar);
            }
        }
    }

    public void m() {
        InterfaceC0194e interfaceC0194e = this.f2878b;
        D2.h.c(interfaceC0194e);
        interfaceC0194e.cancel();
    }

    public final void n(D d4, R2.c cVar) throws ProtocolException {
        D2.h.f(d4, "response");
        if (d4.A() != 101) {
            throw new ProtocolException("Expected HTTP 101 response but was '" + d4.A() + ' ' + d4.n0() + '\'');
        }
        String strC0 = D.c0(d4, "Connection", null, 2, null);
        if (!o.n("Upgrade", strC0, true)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + strC0 + '\'');
        }
        String strC02 = D.c0(d4, "Upgrade", null, 2, null);
        if (!o.n("websocket", strC02, true)) {
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + strC02 + '\'');
        }
        String strC03 = D.c0(d4, "Sec-WebSocket-Accept", null, 2, null);
        String strA = l.f5639f.e(this.f2877a + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").t().a();
        if (D2.h.b(strA, strC03)) {
            if (cVar == null) {
                throw new ProtocolException("Web Socket exchange missing: bad interceptor?");
            }
            return;
        }
        throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + strA + "' but was '" + strC03 + '\'');
    }

    public final synchronized boolean o(int i3, String str, long j3) {
        l lVarE;
        try {
            a3.f.f2937a.c(i3);
            if (str != null) {
                lVarE = l.f5639f.e(str);
                if (!(((long) lVarE.v()) <= 123)) {
                    throw new IllegalArgumentException(("reason.size() > 123: " + str).toString());
                }
            } else {
                lVarE = null;
            }
            if (!this.f2891o && !this.f2888l) {
                this.f2888l = true;
                this.f2886j.add(new a(i3, lVarE, j3));
                v();
                return true;
            }
            return false;
        } finally {
        }
    }

    public final void p(z zVar) {
        D2.h.f(zVar, "client");
        if (this.f2896t.d("Sec-WebSocket-Extensions") != null) {
            q(new ProtocolException("Request header not permitted: 'Sec-WebSocket-Extensions'"), null);
            return;
        }
        z zVarB = zVar.C().g(M2.r.f1214a).L(f2876z).b();
        B b4 = this.f2896t.i().e("Upgrade", "websocket").e("Connection", "Upgrade").e("Sec-WebSocket-Key", this.f2877a).e("Sec-WebSocket-Version", "13").e("Sec-WebSocket-Extensions", "permessage-deflate").b();
        R2.e eVar = new R2.e(zVarB, b4, true);
        this.f2878b = eVar;
        D2.h.c(eVar);
        eVar.o(new f(b4));
    }

    public final void q(Exception exc, D d4) {
        D2.h.f(exc, "e");
        synchronized (this) {
            if (this.f2891o) {
                return;
            }
            this.f2891o = true;
            AbstractC0048d abstractC0048d = this.f2884h;
            this.f2884h = null;
            a3.g gVar = this.f2880d;
            this.f2880d = null;
            a3.h hVar = this.f2881e;
            this.f2881e = null;
            this.f2882f.n();
            r2.r rVar = r2.r.f10584a;
            try {
                this.f2897u.c(this, exc, d4);
            } finally {
                if (abstractC0048d != null) {
                    N2.c.j(abstractC0048d);
                }
                if (gVar != null) {
                    N2.c.j(gVar);
                }
                if (hVar != null) {
                    N2.c.j(hVar);
                }
            }
        }
    }

    public final I r() {
        return this.f2897u;
    }

    public final void s(String str, AbstractC0048d abstractC0048d) {
        D2.h.f(str, "name");
        D2.h.f(abstractC0048d, "streams");
        a3.e eVar = this.f2900x;
        D2.h.c(eVar);
        synchronized (this) {
            try {
                this.f2883g = str;
                this.f2884h = abstractC0048d;
                this.f2881e = new a3.h(abstractC0048d.a(), abstractC0048d.i(), this.f2898v, eVar.f2931a, eVar.a(abstractC0048d.a()), this.f2901y);
                this.f2879c = new e();
                long j3 = this.f2899w;
                if (j3 != 0) {
                    long nanos = TimeUnit.MILLISECONDS.toNanos(j3);
                    String str2 = str + " ping";
                    this.f2882f.i(new g(str2, str2, nanos, this, str, abstractC0048d, eVar), nanos);
                }
                if (!this.f2886j.isEmpty()) {
                    v();
                }
                r2.r rVar = r2.r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
        this.f2880d = new a3.g(abstractC0048d.a(), abstractC0048d.o(), this, eVar.f2931a, eVar.a(!abstractC0048d.a()));
    }

    public final void u() {
        while (this.f2889m == -1) {
            a3.g gVar = this.f2880d;
            D2.h.c(gVar);
            gVar.a();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:96:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v29 */
    /* JADX WARN: Type inference failed for: r1v3, types: [D2.r] */
    /* JADX WARN: Type inference failed for: r1v30 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v2, types: [D2.r] */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r3v24 */
    /* JADX WARN: Type inference failed for: r3v25 */
    /* JADX WARN: Type inference failed for: r3v26 */
    /* JADX WARN: Type inference failed for: r3v27 */
    /* JADX WARN: Type inference failed for: r3v28 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6, types: [D2.r] */
    /* JADX WARN: Type inference failed for: r3v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean x() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 475
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: a3.d.x():boolean");
    }

    public final void y() {
        synchronized (this) {
            try {
                if (this.f2891o) {
                    return;
                }
                a3.h hVar = this.f2881e;
                if (hVar != null) {
                    int i3 = this.f2895s ? this.f2892p : -1;
                    this.f2892p++;
                    this.f2895s = true;
                    r2.r rVar = r2.r.f10584a;
                    if (i3 == -1) {
                        try {
                            hVar.q(l.f5638e);
                            return;
                        } catch (IOException e4) {
                            q(e4, null);
                            return;
                        }
                    }
                    q(new SocketTimeoutException("sent ping but didn't receive pong within " + this.f2899w + "ms (after " + (i3 - 1) + " successful ping/pongs)"), null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
