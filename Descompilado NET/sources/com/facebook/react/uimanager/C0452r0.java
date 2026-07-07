package com.facebook.react.uimanager;

import a1.C0210a;
import com.facebook.yoga.YogaValue;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: renamed from: com.facebook.react.uimanager.r0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0452r0 implements InterfaceC0451q0 {

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private static final com.facebook.yoga.c f7606x = C0458u0.f7631a.b();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f7607a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f7608b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f7609c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private B0 f7610d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f7611e;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private ArrayList f7613g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private C0452r0 f7614h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private C0452r0 f7615i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f7616j;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private C0452r0 f7618l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private ArrayList f7619m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f7620n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f7621o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f7622p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f7623q;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final float[] f7625s;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private com.facebook.yoga.r f7627u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private Integer f7628v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private Integer f7629w;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f7612f = true;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f7617k = 0;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final boolean[] f7626t = new boolean[9];

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final C0468z0 f7624r = new C0468z0(0.0f);

    public C0452r0() {
        float[] fArr = new float[9];
        this.f7625s = fArr;
        if (R()) {
            this.f7627u = null;
            return;
        }
        com.facebook.yoga.r rVarA = (com.facebook.yoga.r) b1.b().b();
        rVarA = rVarA == null ? com.facebook.yoga.s.a(f7606x) : rVarA;
        this.f7627u = rVarA;
        rVarA.B(this);
        Arrays.fill(fArr, Float.NaN);
    }

    private int n0() {
        EnumC0419a0 enumC0419a0M = m();
        if (enumC0419a0M == EnumC0419a0.f7443d) {
            return this.f7617k;
        }
        if (enumC0419a0M == EnumC0419a0.f7442c) {
            return this.f7617k + 1;
        }
        return 1;
    }

    private void t1(int i3) {
        if (m() != EnumC0419a0.f7441b) {
            for (C0452r0 parent = getParent(); parent != null; parent = parent.getParent()) {
                parent.f7617k += i3;
                if (parent.m() == EnumC0419a0.f7441b) {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0091  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void u1() {
        /*
            r4 = this;
            r0 = 0
        L1:
            r1 = 8
            if (r0 > r1) goto Lb6
            if (r0 == 0) goto L62
            r2 = 2
            if (r0 == r2) goto L62
            r2 = 4
            if (r0 == r2) goto L62
            r2 = 5
            if (r0 != r2) goto L11
            goto L62
        L11:
            r2 = 1
            if (r0 == r2) goto L33
            r2 = 3
            if (r0 != r2) goto L18
            goto L33
        L18:
            float[] r1 = r4.f7625s
            r1 = r1[r0]
            boolean r1 = com.facebook.yoga.g.a(r1)
            if (r1 == 0) goto L91
            com.facebook.yoga.r r1 = r4.f7627u
            com.facebook.yoga.j r2 = com.facebook.yoga.j.b(r0)
            com.facebook.react.uimanager.z0 r3 = r4.f7624r
            float r3 = r3.b(r0)
            r1.e0(r2, r3)
            goto Lb2
        L33:
            float[] r2 = r4.f7625s
            r2 = r2[r0]
            boolean r2 = com.facebook.yoga.g.a(r2)
            if (r2 == 0) goto L91
            float[] r2 = r4.f7625s
            r3 = 7
            r2 = r2[r3]
            boolean r2 = com.facebook.yoga.g.a(r2)
            if (r2 == 0) goto L91
            float[] r2 = r4.f7625s
            r1 = r2[r1]
            boolean r1 = com.facebook.yoga.g.a(r1)
            if (r1 == 0) goto L91
            com.facebook.yoga.r r1 = r4.f7627u
            com.facebook.yoga.j r2 = com.facebook.yoga.j.b(r0)
            com.facebook.react.uimanager.z0 r3 = r4.f7624r
            float r3 = r3.b(r0)
            r1.e0(r2, r3)
            goto Lb2
        L62:
            float[] r2 = r4.f7625s
            r2 = r2[r0]
            boolean r2 = com.facebook.yoga.g.a(r2)
            if (r2 == 0) goto L91
            float[] r2 = r4.f7625s
            r3 = 6
            r2 = r2[r3]
            boolean r2 = com.facebook.yoga.g.a(r2)
            if (r2 == 0) goto L91
            float[] r2 = r4.f7625s
            r1 = r2[r1]
            boolean r1 = com.facebook.yoga.g.a(r1)
            if (r1 == 0) goto L91
            com.facebook.yoga.r r1 = r4.f7627u
            com.facebook.yoga.j r2 = com.facebook.yoga.j.b(r0)
            com.facebook.react.uimanager.z0 r3 = r4.f7624r
            float r3 = r3.b(r0)
            r1.e0(r2, r3)
            goto Lb2
        L91:
            boolean[] r1 = r4.f7626t
            boolean r1 = r1[r0]
            if (r1 == 0) goto La5
            com.facebook.yoga.r r1 = r4.f7627u
            com.facebook.yoga.j r2 = com.facebook.yoga.j.b(r0)
            float[] r3 = r4.f7625s
            r3 = r3[r0]
            r1.f0(r2, r3)
            goto Lb2
        La5:
            com.facebook.yoga.r r1 = r4.f7627u
            com.facebook.yoga.j r2 = com.facebook.yoga.j.b(r0)
            float[] r3 = r4.f7625s
            r3 = r3[r0]
            r1.e0(r2, r3)
        Lb2:
            int r0 = r0 + 1
            goto L1
        Lb6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.C0452r0.u1():void");
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final float A() {
        return this.f7627u.l();
    }

    public void A0(M0 m02) {
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void B(float f3, float f4) {
        this.f7627u.c(f3, f4);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: B0, reason: merged with bridge method [inline-methods] */
    public C0452r0 e(int i3) {
        ArrayList arrayList = this.f7613g;
        if (arrayList == null) {
            throw new ArrayIndexOutOfBoundsException("Index " + i3 + " out of bounds: node has no children");
        }
        C0452r0 c0452r0 = (C0452r0) arrayList.remove(i3);
        c0452r0.f7614h = null;
        if (this.f7627u != null && !w0()) {
            this.f7627u.t(i3);
        }
        y0();
        int iN0 = c0452r0.n0();
        this.f7617k -= iN0;
        t1(-iN0);
        return c0452r0;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final int C() {
        ArrayList arrayList = this.f7613g;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: C0, reason: merged with bridge method [inline-methods] */
    public final C0452r0 I(int i3) {
        C0210a.c(this.f7619m);
        C0452r0 c0452r0 = (C0452r0) this.f7619m.remove(i3);
        c0452r0.f7618l = null;
        return c0452r0;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public int D() {
        return this.f7620n;
    }

    public void D0(com.facebook.yoga.a aVar) {
        this.f7627u.v(aVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public Iterable E() {
        if (v0()) {
            return null;
        }
        return this.f7613g;
    }

    public void E0(com.facebook.yoga.a aVar) {
        this.f7627u.w(aVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void F(float f3, float f4, M0 m02, C0423c0 c0423c0) {
        if (this.f7612f) {
            A0(m02);
        }
        if (o0()) {
            float fJ = J();
            float fA = A();
            float f5 = f3 + fJ;
            int iRound = Math.round(f5);
            float f6 = f4 + fA;
            int iRound2 = Math.round(f6);
            int iRound3 = Math.round(f5 + e0());
            int iRound4 = Math.round(f6 + u());
            int iRound5 = Math.round(fJ);
            int iRound6 = Math.round(fA);
            int i3 = iRound3 - iRound;
            int i4 = iRound4 - iRound2;
            boolean z3 = (iRound5 == this.f7620n && iRound6 == this.f7621o && i3 == this.f7622p && i4 == this.f7623q) ? false : true;
            this.f7620n = iRound5;
            this.f7621o = iRound6;
            this.f7622p = i3;
            this.f7623q = i4;
            if (z3) {
                if (c0423c0 != null) {
                    c0423c0.l(this);
                } else {
                    m02.P(getParent().H(), H(), D(), j(), a(), b(), getLayoutDirection());
                }
            }
        }
    }

    public void F0(com.facebook.yoga.a aVar) {
        this.f7627u.x(aVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void G() {
        if (C() == 0) {
            return;
        }
        int iN0 = 0;
        for (int iC = C() - 1; iC >= 0; iC--) {
            if (this.f7627u != null && !w0()) {
                this.f7627u.t(iC);
            }
            C0452r0 c0452r0N = N(iC);
            c0452r0N.f7614h = null;
            iN0 += c0452r0N.n0();
            c0452r0N.f();
        }
        ((ArrayList) C0210a.c(this.f7613g)).clear();
        y0();
        this.f7617k -= iN0;
        t1(-iN0);
    }

    public void G0(com.facebook.yoga.b bVar) {
        this.f7627u.z(bVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final int H() {
        return this.f7607a;
    }

    public void H0(int i3, float f3) {
        this.f7627u.A(com.facebook.yoga.j.b(i3), f3);
    }

    public void I0(float f3) {
        this.f7627u.L(com.facebook.yoga.m.COLUMN, f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final float J() {
        return this.f7627u.k();
    }

    public void J0(float f3) {
        this.f7627u.M(com.facebook.yoga.m.COLUMN, f3);
    }

    public void K0(int i3, float f3) {
        this.f7624r.c(i3, f3);
        u1();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final void L() {
        ArrayList arrayList = this.f7619m;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                ((C0452r0) this.f7619m.get(size)).f7618l = null;
            }
            this.f7619m.clear();
        }
    }

    public void L0(com.facebook.yoga.i iVar) {
        this.f7627u.D(iVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void M() {
        B(Float.NaN, Float.NaN);
    }

    public void M0(float f3) {
        this.f7627u.F(f3);
    }

    public void N0() {
        this.f7627u.G();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void O(C0423c0 c0423c0) {
    }

    public void O0(float f3) {
        this.f7627u.H(f3);
    }

    public void P0(com.facebook.yoga.l lVar) {
        this.f7627u.I(lVar);
    }

    public void Q0(com.facebook.yoga.x xVar) {
        this.f7627u.m0(xVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public boolean R() {
        return false;
    }

    public void R0(float f3) {
        this.f7627u.L(com.facebook.yoga.m.ALL, f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void S(int i3, float f3) {
        this.f7625s[i3] = f3;
        this.f7626t[i3] = false;
        u1();
    }

    public void S0(float f3) {
        this.f7627u.L(com.facebook.yoga.m.ALL, f3);
    }

    public void T0(com.facebook.yoga.n nVar) {
        this.f7627u.Q(nVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final int U() {
        ArrayList arrayList = this.f7619m;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: U0, reason: merged with bridge method [inline-methods] */
    public final void w(C0452r0 c0452r0) {
        this.f7615i = c0452r0;
    }

    public void V0(int i3, float f3) {
        this.f7627u.R(com.facebook.yoga.j.b(i3), f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final void W(boolean z3) {
        C0210a.b(getParent() == null, "Must remove from no opt parent first");
        C0210a.b(this.f7618l == null, "Must remove from native parent first");
        C0210a.b(U() == 0, "Must remove all native children first");
        this.f7616j = z3;
    }

    public void W0(int i3) {
        this.f7627u.S(com.facebook.yoga.j.b(i3));
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final void X(C0454s0 c0454s0) {
        R0.g(this, c0454s0);
        z0();
    }

    public void X0(int i3, float f3) {
        this.f7627u.T(com.facebook.yoga.j.b(i3), f3);
    }

    public void Y0(com.facebook.yoga.o oVar) {
        this.f7627u.Y(oVar);
    }

    public void Z0(com.facebook.yoga.u uVar) {
        this.f7627u.d0(uVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public int a() {
        return this.f7622p;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final boolean a0() {
        return this.f7616j;
    }

    public void a1(int i3, float f3) {
        this.f7625s[i3] = f3;
        this.f7626t[i3] = !com.facebook.yoga.g.a(f3);
        u1();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public int b() {
        return this.f7623q;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final void b0(int i3) {
        this.f7609c = i3;
    }

    public void b1(int i3, float f3) {
        this.f7627u.g0(com.facebook.yoga.j.b(i3), f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final YogaValue c() {
        return this.f7627u.m();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void c0(B0 b02) {
        this.f7610d = b02;
    }

    public void c1(int i3, float f3) {
        this.f7627u.h0(com.facebook.yoga.j.b(i3), f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final void d() {
        this.f7612f = false;
        if (o0()) {
            x0();
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void d0(float f3) {
        this.f7627u.j0(f3);
    }

    public void d1(com.facebook.yoga.v vVar) {
        this.f7627u.i0(vVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final float e0() {
        return this.f7627u.j();
    }

    public void e1(float f3) {
        this.f7627u.L(com.facebook.yoga.m.ROW, f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void f() {
        com.facebook.yoga.r rVar = this.f7627u;
        if (rVar != null) {
            rVar.u();
            b1.b().a(this.f7627u);
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: f0, reason: merged with bridge method [inline-methods] */
    public void o(C0452r0 c0452r0, int i3) {
        if (this.f7613g == null) {
            this.f7613g = new ArrayList(4);
        }
        this.f7613g.add(i3, c0452r0);
        c0452r0.f7614h = this;
        if (this.f7627u != null && !w0()) {
            com.facebook.yoga.r rVar = c0452r0.f7627u;
            if (rVar == null) {
                throw new RuntimeException("Cannot add a child that doesn't have a YogaNode to a parent without a measure function! (Trying to add a '" + c0452r0.toString() + "' to a '" + toString() + "')");
            }
            this.f7627u.b(rVar, i3);
        }
        y0();
        int iN0 = c0452r0.n0();
        this.f7617k += iN0;
        t1(iN0);
    }

    public void f1(float f3) {
        this.f7627u.M(com.facebook.yoga.m.ROW, f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void g(float f3) {
        this.f7627u.N(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: g0, reason: merged with bridge method [inline-methods] */
    public final void Z(C0452r0 c0452r0, int i3) {
        C0210a.a(m() == EnumC0419a0.f7441b);
        C0210a.a(c0452r0.m() != EnumC0419a0.f7443d);
        if (this.f7619m == null) {
            this.f7619m = new ArrayList(4);
        }
        this.f7619m.add(i3, c0452r0);
        c0452r0.f7618l = this;
    }

    public void g1(float f3) {
        this.f7627u.y(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public Integer getHeightMeasureSpec() {
        return this.f7629w;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final com.facebook.yoga.h getLayoutDirection() {
        return this.f7627u.f();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public Integer getWidthMeasureSpec() {
        return this.f7628v;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void h(int i3, int i4) {
        this.f7628v = Integer.valueOf(i3);
        this.f7629w = Integer.valueOf(i4);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: h0, reason: merged with bridge method [inline-methods] */
    public final C0452r0 N(int i3) {
        ArrayList arrayList = this.f7613g;
        if (arrayList != null) {
            return (C0452r0) arrayList.get(i3);
        }
        throw new ArrayIndexOutOfBoundsException("Index " + i3 + " out of bounds: node has no children");
    }

    public void h1() {
        this.f7627u.O();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void i() {
        if (!R()) {
            this.f7627u.d();
        } else if (getParent() != null) {
            getParent().i();
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: i0, reason: merged with bridge method [inline-methods] */
    public final C0452r0 P() {
        C0452r0 c0452r0 = this.f7615i;
        return c0452r0 != null ? c0452r0 : V();
    }

    public void i1(float f3) {
        this.f7627u.P(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public int j() {
        return this.f7621o;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: j0, reason: merged with bridge method [inline-methods] */
    public final int T(C0452r0 c0452r0) {
        int iN0 = 0;
        for (int i3 = 0; i3 < C(); i3++) {
            C0452r0 c0452r0N = N(i3);
            if (c0452r0 == c0452r0N) {
                return iN0;
            }
            iN0 += c0452r0N.n0();
        }
        throw new RuntimeException("Child " + c0452r0.H() + " was not a child of " + this.f7607a);
    }

    public void j1(float f3) {
        this.f7627u.U(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void k(Object obj) {
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: k0, reason: merged with bridge method [inline-methods] */
    public final C0452r0 V() {
        return this.f7618l;
    }

    public void k1(float f3) {
        this.f7627u.V(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final B0 l() {
        return (B0) C0210a.c(this.f7610d);
    }

    public final float l0(int i3) {
        return this.f7627u.h(com.facebook.yoga.j.b(i3));
    }

    public void l1(float f3) {
        this.f7627u.W(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public EnumC0419a0 m() {
        return (R() || a0()) ? EnumC0419a0.f7443d : p0() ? EnumC0419a0.f7442c : EnumC0419a0.f7441b;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: m0, reason: merged with bridge method [inline-methods] */
    public final C0452r0 getParent() {
        return this.f7614h;
    }

    public void m1(float f3) {
        this.f7627u.X(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final int n() {
        C0210a.a(this.f7609c != 0);
        return this.f7609c;
    }

    public void n1(float f3) {
        this.f7627u.Z(f3);
    }

    public final boolean o0() {
        com.facebook.yoga.r rVar = this.f7627u;
        return rVar != null && rVar.n();
    }

    public void o1(float f3) {
        this.f7627u.a0(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final void p(String str) {
        this.f7608b = str;
    }

    public boolean p0() {
        return false;
    }

    public void p1(float f3) {
        this.f7627u.b0(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public boolean q(float f3, float f4) {
        if (!o0()) {
            return false;
        }
        float fJ = J();
        float fA = A();
        float f5 = f3 + fJ;
        int iRound = Math.round(f5);
        float f6 = f4 + fA;
        int iRound2 = Math.round(f6);
        return (Math.round(fJ) == this.f7620n && Math.round(fA) == this.f7621o && Math.round(f5 + e0()) - iRound == this.f7622p && Math.round(f6 + u()) - iRound2 == this.f7623q) ? false : true;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: q0, reason: merged with bridge method [inline-methods] */
    public final int t(C0452r0 c0452r0) {
        ArrayList arrayList = this.f7613g;
        if (arrayList == null) {
            return -1;
        }
        return arrayList.indexOf(c0452r0);
    }

    public void q1(float f3) {
        this.f7627u.c0(f3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final boolean r() {
        return this.f7611e;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: r0, reason: merged with bridge method [inline-methods] */
    public final int Y(C0452r0 c0452r0) {
        C0210a.c(this.f7619m);
        return this.f7619m.indexOf(c0452r0);
    }

    public void r1() {
        this.f7627u.k0();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void s(com.facebook.yoga.h hVar) {
        this.f7627u.C(hVar);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    /* JADX INFO: renamed from: s0, reason: merged with bridge method [inline-methods] */
    public boolean Q(C0452r0 c0452r0) {
        for (C0452r0 parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent == c0452r0) {
                return true;
            }
        }
        return false;
    }

    public void s1(float f3) {
        this.f7627u.l0(f3);
    }

    public void setFlex(float f3) {
        this.f7627u.E(f3);
    }

    public void setFlexGrow(float f3) {
        this.f7627u.J(f3);
    }

    public void setFlexShrink(float f3) {
        this.f7627u.K(f3);
    }

    public void setShouldNotifyOnLayout(boolean z3) {
        this.f7611e = z3;
    }

    public final boolean t0() {
        com.facebook.yoga.r rVar = this.f7627u;
        return rVar != null && rVar.p();
    }

    public String toString() {
        return "[" + this.f7608b + " " + H() + "]";
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final float u() {
        return this.f7627u.g();
    }

    public boolean u0() {
        return this.f7627u.r();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final String v() {
        return (String) C0210a.c(this.f7608b);
    }

    public boolean v0() {
        return false;
    }

    public boolean w0() {
        return u0();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final boolean x() {
        return this.f7612f || o0() || t0();
    }

    public final void x0() {
        com.facebook.yoga.r rVar = this.f7627u;
        if (rVar != null) {
            rVar.s();
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public void y(int i3) {
        this.f7607a = i3;
    }

    public void y0() {
        if (this.f7612f) {
            return;
        }
        this.f7612f = true;
        C0452r0 parent = getParent();
        if (parent != null) {
            parent.y0();
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0451q0
    public final YogaValue z() {
        return this.f7627u.e();
    }

    public void z0() {
    }
}
