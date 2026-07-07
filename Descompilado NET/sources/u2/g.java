package U2;

import M2.A;
import M2.B;
import M2.D;
import M2.t;
import M2.z;
import b3.F;
import b3.G;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class g implements S2.d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private volatile i f2601a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final A f2602b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile boolean f2603c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final R2.f f2604d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final S2.g f2605e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final f f2606f;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f2600i = new a(null);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final List f2598g = N2.c.t("connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade", ":method", ":path", ":scheme", ":authority");

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final List f2599h = N2.c.t("connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade");

    public static final class a {
        private a() {
        }

        public final List a(B b4) {
            D2.h.f(b4, "request");
            t tVarE = b4.e();
            ArrayList arrayList = new ArrayList(tVarE.size() + 4);
            arrayList.add(new c(c.f2456f, b4.h()));
            arrayList.add(new c(c.f2457g, S2.i.f2335a.c(b4.l())));
            String strD = b4.d("Host");
            if (strD != null) {
                arrayList.add(new c(c.f2459i, strD));
            }
            arrayList.add(new c(c.f2458h, b4.l().p()));
            int size = tVarE.size();
            for (int i3 = 0; i3 < size; i3++) {
                String strB = tVarE.b(i3);
                Locale locale = Locale.US;
                D2.h.e(locale, "Locale.US");
                if (strB == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                String lowerCase = strB.toLowerCase(locale);
                D2.h.e(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
                if (!g.f2598g.contains(lowerCase) || (D2.h.b(lowerCase, "te") && D2.h.b(tVarE.h(i3), "trailers"))) {
                    arrayList.add(new c(lowerCase, tVarE.h(i3)));
                }
            }
            return arrayList;
        }

        public final D.a b(t tVar, A a4) throws ProtocolException {
            D2.h.f(tVar, "headerBlock");
            D2.h.f(a4, "protocol");
            t.a aVar = new t.a();
            int size = tVar.size();
            S2.k kVarA = null;
            for (int i3 = 0; i3 < size; i3++) {
                String strB = tVar.b(i3);
                String strH = tVar.h(i3);
                if (D2.h.b(strB, ":status")) {
                    kVarA = S2.k.f2338d.a("HTTP/1.1 " + strH);
                } else if (!g.f2599h.contains(strB)) {
                    aVar.c(strB, strH);
                }
            }
            if (kVarA != null) {
                return new D.a().p(a4).g(kVarA.f2340b).m(kVarA.f2341c).k(aVar.e());
            }
            throw new ProtocolException("Expected ':status' header not present");
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public g(z zVar, R2.f fVar, S2.g gVar, f fVar2) {
        D2.h.f(zVar, "client");
        D2.h.f(fVar, "connection");
        D2.h.f(gVar, "chain");
        D2.h.f(fVar2, "http2Connection");
        this.f2604d = fVar;
        this.f2605e = gVar;
        this.f2606f = fVar2;
        List listF = zVar.F();
        A a4 = A.H2_PRIOR_KNOWLEDGE;
        this.f2602b = listF.contains(a4) ? a4 : A.HTTP_2;
    }

    @Override // S2.d
    public void a() {
        i iVar = this.f2601a;
        D2.h.c(iVar);
        iVar.n().close();
    }

    @Override // S2.d
    public void b() {
        this.f2606f.flush();
    }

    @Override // S2.d
    public F c(D d4) {
        D2.h.f(d4, "response");
        i iVar = this.f2601a;
        D2.h.c(iVar);
        return iVar.p();
    }

    @Override // S2.d
    public void cancel() {
        this.f2603c = true;
        i iVar = this.f2601a;
        if (iVar != null) {
            iVar.f(b.CANCEL);
        }
    }

    @Override // S2.d
    public long d(D d4) {
        D2.h.f(d4, "response");
        if (S2.e.b(d4)) {
            return N2.c.s(d4);
        }
        return 0L;
    }

    @Override // S2.d
    public b3.D e(B b4, long j3) {
        D2.h.f(b4, "request");
        i iVar = this.f2601a;
        D2.h.c(iVar);
        return iVar.n();
    }

    @Override // S2.d
    public void f(B b4) throws IOException {
        D2.h.f(b4, "request");
        if (this.f2601a != null) {
            return;
        }
        this.f2601a = this.f2606f.K0(f2600i.a(b4), b4.a() != null);
        if (this.f2603c) {
            i iVar = this.f2601a;
            D2.h.c(iVar);
            iVar.f(b.CANCEL);
            throw new IOException("Canceled");
        }
        i iVar2 = this.f2601a;
        D2.h.c(iVar2);
        G gV = iVar2.v();
        long jG = this.f2605e.g();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        gV.g(jG, timeUnit);
        i iVar3 = this.f2601a;
        D2.h.c(iVar3);
        iVar3.E().g(this.f2605e.j(), timeUnit);
    }

    @Override // S2.d
    public D.a g(boolean z3) throws ProtocolException {
        i iVar = this.f2601a;
        D2.h.c(iVar);
        D.a aVarB = f2600i.b(iVar.C(), this.f2602b);
        if (z3 && aVarB.h() == 100) {
            return null;
        }
        return aVarB;
    }

    @Override // S2.d
    public R2.f h() {
        return this.f2604d;
    }
}
