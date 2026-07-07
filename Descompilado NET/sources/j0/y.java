package J0;

import H0.InterfaceC0163a;
import H0.x;
import android.content.Context;
import com.facebook.imagepipeline.producers.q0;
import com.facebook.imagepipeline.producers.r0;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class y {

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final Class f776o = y.class;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static y f777p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private static C0185t f778q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static boolean f779r;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final q0 f780a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InterfaceC0187v f781b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0167a f782c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final X.n f783d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private H0.n f784e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private H0.u f785f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private H0.n f786g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private H0.u f787h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private M0.c f788i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private W0.d f789j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private C f790k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private W f791l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private G0.b f792m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private S0.f f793n;

    public y(InterfaceC0187v interfaceC0187v) {
        if (V0.b.d()) {
            V0.b.a("ImagePipelineConfig()");
        }
        InterfaceC0187v interfaceC0187v2 = (InterfaceC0187v) X.k.g(interfaceC0187v);
        this.f781b = interfaceC0187v2;
        this.f780a = interfaceC0187v2.G().F() ? new com.facebook.imagepipeline.producers.C(interfaceC0187v.I().b()) : new r0(interfaceC0187v.I().b());
        this.f782c = new C0167a(interfaceC0187v.o());
        if (V0.b.d()) {
            V0.b.b();
        }
        this.f783d = interfaceC0187v2.k();
        if (interfaceC0187v2.G().z()) {
            D0.e.e().g(true);
        }
    }

    private C0185t a() {
        W wP = p();
        Set setU = this.f781b.u();
        Set setI = this.f781b.i();
        X.n nVarM = this.f781b.m();
        H0.u uVarE = e();
        H0.u uVarH = h();
        X.n nVar = this.f783d;
        H0.k kVarA = this.f781b.A();
        q0 q0Var = this.f780a;
        X.n nVarT = this.f781b.G().t();
        X.n nVarH = this.f781b.G().H();
        this.f781b.C();
        return new C0185t(wP, setU, setI, nVarM, uVarE, uVarH, nVar, kVarA, q0Var, nVarT, nVarH, null, this.f781b);
    }

    private E0.a c() {
        G0.b bVarM = m();
        InterfaceC0182p interfaceC0182pI = this.f781b.I();
        H0.n nVarD = d();
        boolean zJ = this.f781b.G().j();
        boolean zV = this.f781b.G().v();
        int iC = this.f781b.G().c();
        int iD = this.f781b.G().d();
        this.f781b.n();
        E0.b.a(bVarM, interfaceC0182pI, nVarD, zJ, zV, iC, iD, null);
        return null;
    }

    private M0.c i() {
        if (this.f788i == null) {
            if (this.f781b.E() != null) {
                this.f788i = this.f781b.E();
            } else {
                c();
                M0.c cVarR = r();
                this.f781b.x();
                this.f788i = new M0.b(null, null, cVarR, n());
            }
        }
        return this.f788i;
    }

    private W0.d k() {
        if (this.f789j == null) {
            if (this.f781b.v() == null && this.f781b.s() == null && this.f781b.G().I()) {
                this.f789j = new W0.h(this.f781b.G().m());
            } else {
                this.f789j = new W0.f(this.f781b.G().m(), this.f781b.G().x(), this.f781b.v(), this.f781b.s(), this.f781b.G().E());
            }
        }
        return this.f789j;
    }

    public static y l() {
        return (y) X.k.h(f777p, "ImagePipelineFactory was not initialized!");
    }

    private C o() {
        if (this.f790k == null) {
            this.f790k = this.f781b.G().p().a(this.f781b.c(), this.f781b.d().i(), i(), this.f781b.e(), this.f781b.B(), this.f781b.F(), this.f781b.G().A(), this.f781b.I(), this.f781b.d().g(this.f781b.j()), this.f781b.d().h(), e(), h(), this.f783d, this.f781b.A(), m(), this.f781b.G().g(), this.f781b.G().f(), this.f781b.G().e(), this.f781b.G().m(), f(), this.f781b.G().l(), this.f781b.G().u());
        }
        return this.f790k;
    }

    private W p() {
        boolean zW = this.f781b.G().w();
        if (this.f791l == null) {
            this.f791l = new W(this.f781b.c().getApplicationContext().getContentResolver(), o(), this.f781b.q(), this.f781b.F(), this.f781b.G().K(), this.f780a, this.f781b.B(), zW, this.f781b.G().J(), this.f781b.y(), k(), this.f781b.G().D(), this.f781b.G().B(), this.f781b.G().a(), this.f781b.K());
        }
        return this.f791l;
    }

    public static synchronized void s(InterfaceC0187v interfaceC0187v) {
        if (f777p != null) {
            Y.a.E(f776o, "ImagePipelineFactory has already been initialized! `ImagePipelineFactory.initialize(...)` should only be called once to avoid unexpected behavior.");
            if (f779r) {
                return;
            }
        }
        f777p = new y(interfaceC0187v);
    }

    public static synchronized void t(Context context) {
        try {
            if (V0.b.d()) {
                V0.b.a("ImagePipelineFactory#initialize");
            }
            s(C0186u.L(context).a());
            if (V0.b.d()) {
                V0.b.b();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public N0.a b(Context context) {
        c();
        return null;
    }

    public H0.n d() {
        if (this.f784e == null) {
            InterfaceC0163a interfaceC0163aP = this.f781b.p();
            X.n nVarD = this.f781b.D();
            a0.d dVarW = this.f781b.w();
            x.a aVarJ = this.f781b.J();
            boolean zR = this.f781b.G().r();
            boolean zQ = this.f781b.G().q();
            this.f781b.l();
            this.f784e = interfaceC0163aP.a(nVarD, dVarW, aVarJ, zR, zQ, null);
        }
        return this.f784e;
    }

    public H0.u e() {
        if (this.f785f == null) {
            this.f785f = H0.v.a(d(), this.f781b.h());
        }
        return this.f785f;
    }

    public C0167a f() {
        return this.f782c;
    }

    public H0.n g() {
        if (this.f786g == null) {
            this.f786g = H0.r.a(this.f781b.H(), this.f781b.w(), this.f781b.z());
        }
        return this.f786g;
    }

    public H0.u h() {
        if (this.f787h == null) {
            this.f787h = H0.s.a(this.f781b.r() != null ? this.f781b.r() : g(), this.f781b.h());
        }
        return this.f787h;
    }

    public C0185t j() {
        if (f778q == null) {
            f778q = a();
        }
        return f778q;
    }

    public G0.b m() {
        if (this.f792m == null) {
            this.f792m = G0.c.a(this.f781b.d(), n(), f());
        }
        return this.f792m;
    }

    public S0.f n() {
        if (this.f793n == null) {
            this.f793n = S0.g.a(this.f781b.d(), this.f781b.G().G(), this.f781b.G().s(), this.f781b.G().o());
        }
        return this.f793n;
    }

    public N0.a q() {
        if (this.f781b.G().z()) {
            return new Y0.a();
        }
        return null;
    }

    public M0.c r() {
        if (this.f781b.G().z()) {
            return new Y0.b(this.f781b.c().getApplicationContext().getResources());
        }
        return null;
    }
}
