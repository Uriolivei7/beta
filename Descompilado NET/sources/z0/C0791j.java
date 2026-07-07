package z0;

import s2.AbstractC0717n;
import z0.InterfaceC0783b;

/* JADX INFO: renamed from: z0.j, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0791j extends C0789h {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private long f11123A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private long f11124B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private long f11125C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private boolean f11126D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private int f11127E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private int f11128F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private Throwable f11129G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private EnumC0786e f11130H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private n f11131I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private long f11132J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private long f11133K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private C0784c f11134L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private InterfaceC0783b.a f11135M;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private String f11136s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private String f11137t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private Object f11138u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private Object f11139v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private Object f11140w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private long f11141x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private long f11142y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private long f11143z;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0791j(EnumC0792k enumC0792k) {
        super(enumC0792k);
        D2.h.f(enumC0792k, "infra");
        this.f11141x = -1L;
        this.f11142y = -1L;
        this.f11143z = -1L;
        this.f11123A = -1L;
        this.f11124B = -1L;
        this.f11125C = -1L;
        this.f11127E = -1;
        this.f11128F = -1;
        this.f11130H = EnumC0786e.f11054e;
        this.f11131I = n.f11153e;
        this.f11132J = -1L;
        this.f11133K = -1L;
    }

    public final void A(long j3) {
        this.f11143z = j3;
    }

    public final void B(String str) {
        this.f11136s = str;
    }

    public final void C(long j3) {
        this.f11142y = j3;
    }

    public final void D(long j3) {
        this.f11141x = j3;
    }

    public final void E(Throwable th) {
        this.f11129G = th;
    }

    public final void F(InterfaceC0783b.a aVar) {
        this.f11135M = aVar;
    }

    public final void G(Object obj) {
        this.f11140w = obj;
    }

    public final void H(EnumC0786e enumC0786e) {
        D2.h.f(enumC0786e, "<set-?>");
        this.f11130H = enumC0786e;
    }

    public final void I(Object obj) {
        this.f11138u = obj;
    }

    public final void J(long j3) {
        this.f11125C = j3;
    }

    public final void K(long j3) {
        this.f11124B = j3;
    }

    public final void L(long j3) {
        this.f11133K = j3;
    }

    public final void M(int i3) {
        this.f11128F = i3;
    }

    public final void N(int i3) {
        this.f11127E = i3;
    }

    public final void O(boolean z3) {
        this.f11126D = z3;
    }

    public final void P(String str) {
        this.f11137t = str;
    }

    public final void Q(long j3) {
        this.f11132J = j3;
    }

    public final void R(boolean z3) {
        this.f11131I = z3 ? n.f11154f : n.f11155g;
    }

    public final C0787f S() {
        return new C0787f(j(), this.f11136s, this.f11137t, this.f11138u, this.f11139v, this.f11140w, this.f11141x, this.f11142y, this.f11143z, this.f11123A, this.f11124B, this.f11125C, f(), n(), this.f11126D, this.f11127E, this.f11128F, this.f11129G, this.f11131I, this.f11132J, this.f11133K, this.f11134L, this.f11135M, a(), o(), c(), d(), b(), r(), q(), l(), p(), AbstractC0717n.e0(k()), m(), h(), i(), g(), e());
    }

    public final void w() {
        this.f11137t = null;
        this.f11138u = null;
        this.f11139v = null;
        this.f11140w = null;
        this.f11126D = false;
        this.f11127E = -1;
        this.f11128F = -1;
        this.f11129G = null;
        this.f11130H = EnumC0786e.f11054e;
        this.f11131I = n.f11153e;
        this.f11134L = null;
        this.f11135M = null;
        x();
        s();
    }

    public final void x() {
        this.f11124B = -1L;
        this.f11125C = -1L;
        this.f11141x = -1L;
        this.f11143z = -1L;
        this.f11123A = -1L;
        this.f11132J = -1L;
        this.f11133K = -1L;
        k().clear();
        u(false);
        t(null);
        v(null);
    }

    public final void y(Object obj) {
        this.f11139v = obj;
    }

    public final void z(long j3) {
        this.f11123A = j3;
    }
}
