package com.facebook.yoga;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class YogaNodeJNIBase extends r implements Cloneable {
    private float[] arr;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private YogaNodeJNIBase f8279b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private c f8280c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private List f8281d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private o f8282e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private b f8283f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected long f8284g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Object f8285h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f8286i;
    private int mLayoutDirection;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f8287a;

        static {
            int[] iArr = new int[j.values().length];
            f8287a = iArr;
            try {
                iArr[j.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f8287a[j.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f8287a[j.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f8287a[j.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f8287a[j.START.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f8287a[j.END.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private YogaNodeJNIBase(long j3) {
        this.arr = null;
        this.mLayoutDirection = 0;
        this.f8286i = true;
        if (j3 == 0) {
            throw new IllegalStateException("Failed to allocate native memory");
        }
        this.f8284g = j3;
    }

    private void n0(r rVar) {
        o0();
    }

    private static YogaValue q0(long j3) {
        return new YogaValue(Float.intBitsToFloat((int) j3), (int) (j3 >> 32));
    }

    private final long replaceChild(YogaNodeJNIBase yogaNodeJNIBase, int i3) {
        List list = this.f8281d;
        if (list == null) {
            throw new IllegalStateException("Cannot replace child. YogaNode does not have children");
        }
        list.remove(i3);
        this.f8281d.add(i3, yogaNodeJNIBase);
        yogaNodeJNIBase.f8279b = this;
        return yogaNodeJNIBase.f8284g;
    }

    @Override // com.facebook.yoga.r
    public void A(j jVar, float f3) {
        YogaNative.jni_YGNodeStyleSetBorderJNI(this.f8284g, jVar.c(), f3);
    }

    @Override // com.facebook.yoga.r
    public void B(Object obj) {
        this.f8285h = obj;
    }

    @Override // com.facebook.yoga.r
    public void C(h hVar) {
        YogaNative.jni_YGNodeStyleSetDirectionJNI(this.f8284g, hVar.c());
    }

    @Override // com.facebook.yoga.r
    public void D(i iVar) {
        YogaNative.jni_YGNodeStyleSetDisplayJNI(this.f8284g, iVar.b());
    }

    @Override // com.facebook.yoga.r
    public void E(float f3) {
        YogaNative.jni_YGNodeStyleSetFlexJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void F(float f3) {
        YogaNative.jni_YGNodeStyleSetFlexBasisJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void G() {
        YogaNative.jni_YGNodeStyleSetFlexBasisAutoJNI(this.f8284g);
    }

    @Override // com.facebook.yoga.r
    public void H(float f3) {
        YogaNative.jni_YGNodeStyleSetFlexBasisPercentJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void I(l lVar) {
        YogaNative.jni_YGNodeStyleSetFlexDirectionJNI(this.f8284g, lVar.b());
    }

    @Override // com.facebook.yoga.r
    public void J(float f3) {
        YogaNative.jni_YGNodeStyleSetFlexGrowJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void K(float f3) {
        YogaNative.jni_YGNodeStyleSetFlexShrinkJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void L(m mVar, float f3) {
        YogaNative.jni_YGNodeStyleSetGapJNI(this.f8284g, mVar.b(), f3);
    }

    @Override // com.facebook.yoga.r
    public void M(m mVar, float f3) {
        YogaNative.jni_YGNodeStyleSetGapPercentJNI(this.f8284g, mVar.b(), f3);
    }

    @Override // com.facebook.yoga.r
    public void N(float f3) {
        YogaNative.jni_YGNodeStyleSetHeightJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void O() {
        YogaNative.jni_YGNodeStyleSetHeightAutoJNI(this.f8284g);
    }

    @Override // com.facebook.yoga.r
    public void P(float f3) {
        YogaNative.jni_YGNodeStyleSetHeightPercentJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void Q(n nVar) {
        YogaNative.jni_YGNodeStyleSetJustifyContentJNI(this.f8284g, nVar.b());
    }

    @Override // com.facebook.yoga.r
    public void R(j jVar, float f3) {
        YogaNative.jni_YGNodeStyleSetMarginJNI(this.f8284g, jVar.c(), f3);
    }

    @Override // com.facebook.yoga.r
    public void S(j jVar) {
        YogaNative.jni_YGNodeStyleSetMarginAutoJNI(this.f8284g, jVar.c());
    }

    @Override // com.facebook.yoga.r
    public void T(j jVar, float f3) {
        YogaNative.jni_YGNodeStyleSetMarginPercentJNI(this.f8284g, jVar.c(), f3);
    }

    @Override // com.facebook.yoga.r
    public void U(float f3) {
        YogaNative.jni_YGNodeStyleSetMaxHeightJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void V(float f3) {
        YogaNative.jni_YGNodeStyleSetMaxHeightPercentJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void W(float f3) {
        YogaNative.jni_YGNodeStyleSetMaxWidthJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void X(float f3) {
        YogaNative.jni_YGNodeStyleSetMaxWidthPercentJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void Y(o oVar) {
        this.f8282e = oVar;
        YogaNative.jni_YGNodeSetHasMeasureFuncJNI(this.f8284g, oVar != null);
    }

    @Override // com.facebook.yoga.r
    public void Z(float f3) {
        YogaNative.jni_YGNodeStyleSetMinHeightJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void a0(float f3) {
        YogaNative.jni_YGNodeStyleSetMinHeightPercentJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void b(r rVar, int i3) {
        if (rVar instanceof YogaNodeJNIBase) {
            YogaNodeJNIBase yogaNodeJNIBase = (YogaNodeJNIBase) rVar;
            if (yogaNodeJNIBase.f8279b != null) {
                throw new IllegalStateException("Child already has a parent, it must be removed first.");
            }
            if (this.f8281d == null) {
                this.f8281d = new ArrayList(4);
            }
            this.f8281d.add(i3, yogaNodeJNIBase);
            yogaNodeJNIBase.f8279b = this;
            YogaNative.jni_YGNodeInsertChildJNI(this.f8284g, yogaNodeJNIBase.f8284g, i3);
        }
    }

    @Override // com.facebook.yoga.r
    public void b0(float f3) {
        YogaNative.jni_YGNodeStyleSetMinWidthJNI(this.f8284g, f3);
    }

    public final float baseline(float f3, float f4) {
        return this.f8283f.a(this, f3, f4);
    }

    @Override // com.facebook.yoga.r
    public void c(float f3, float f4) {
        n0(null);
        ArrayList arrayList = new ArrayList();
        arrayList.add(this);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            YogaNodeJNIBase yogaNodeJNIBase = (YogaNodeJNIBase) arrayList.get(i3);
            List<YogaNodeJNIBase> list = yogaNodeJNIBase.f8281d;
            if (list != null) {
                for (YogaNodeJNIBase yogaNodeJNIBase2 : list) {
                    yogaNodeJNIBase2.n0(yogaNodeJNIBase);
                    arrayList.add(yogaNodeJNIBase2);
                }
            }
        }
        YogaNodeJNIBase[] yogaNodeJNIBaseArr = (YogaNodeJNIBase[]) arrayList.toArray(new YogaNodeJNIBase[arrayList.size()]);
        long[] jArr = new long[yogaNodeJNIBaseArr.length];
        for (int i4 = 0; i4 < yogaNodeJNIBaseArr.length; i4++) {
            jArr[i4] = yogaNodeJNIBaseArr[i4].f8284g;
        }
        YogaNative.jni_YGNodeCalculateLayoutJNI(this.f8284g, f3, f4, jArr, yogaNodeJNIBaseArr);
    }

    @Override // com.facebook.yoga.r
    public void c0(float f3) {
        YogaNative.jni_YGNodeStyleSetMinWidthPercentJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void d() {
        YogaNative.jni_YGNodeMarkDirtyJNI(this.f8284g);
    }

    @Override // com.facebook.yoga.r
    public void d0(u uVar) {
        YogaNative.jni_YGNodeStyleSetOverflowJNI(this.f8284g, uVar.b());
    }

    @Override // com.facebook.yoga.r
    public YogaValue e() {
        return q0(YogaNative.jni_YGNodeStyleGetHeightJNI(this.f8284g));
    }

    @Override // com.facebook.yoga.r
    public void e0(j jVar, float f3) {
        YogaNative.jni_YGNodeStyleSetPaddingJNI(this.f8284g, jVar.c(), f3);
    }

    @Override // com.facebook.yoga.r
    public h f() {
        float[] fArr = this.arr;
        return h.b(fArr != null ? (int) fArr[5] : this.mLayoutDirection);
    }

    @Override // com.facebook.yoga.r
    public void f0(j jVar, float f3) {
        YogaNative.jni_YGNodeStyleSetPaddingPercentJNI(this.f8284g, jVar.c(), f3);
    }

    @Override // com.facebook.yoga.r
    public float g() {
        float[] fArr = this.arr;
        if (fArr != null) {
            return fArr[2];
        }
        return 0.0f;
    }

    @Override // com.facebook.yoga.r
    public void g0(j jVar, float f3) {
        YogaNative.jni_YGNodeStyleSetPositionJNI(this.f8284g, jVar.c(), f3);
    }

    @Override // com.facebook.yoga.r
    public float h(j jVar) {
        float[] fArr = this.arr;
        if (fArr == null) {
            return 0.0f;
        }
        float f3 = fArr[0];
        if ((((int) f3) & 2) != 2) {
            return 0.0f;
        }
        int i3 = (((int) f3) & 1) != 1 ? 4 : 0;
        int i4 = 10 - i3;
        switch (a.f8287a[jVar.ordinal()]) {
            case 1:
                return this.arr[i4];
            case 2:
                return this.arr[11 - i3];
            case 3:
                return this.arr[12 - i3];
            case 4:
                return this.arr[13 - i3];
            case 5:
                return f() == h.RTL ? this.arr[12 - i3] : this.arr[i4];
            case 6:
                return f() == h.RTL ? this.arr[i4] : this.arr[12 - i3];
            default:
                throw new IllegalArgumentException("Cannot get layout paddings of multi-edge shorthands");
        }
    }

    @Override // com.facebook.yoga.r
    public void h0(j jVar, float f3) {
        YogaNative.jni_YGNodeStyleSetPositionPercentJNI(this.f8284g, jVar.c(), f3);
    }

    @Override // com.facebook.yoga.r
    public void i0(v vVar) {
        YogaNative.jni_YGNodeStyleSetPositionTypeJNI(this.f8284g, vVar.b());
    }

    @Override // com.facebook.yoga.r
    public float j() {
        float[] fArr = this.arr;
        if (fArr != null) {
            return fArr[1];
        }
        return 0.0f;
    }

    @Override // com.facebook.yoga.r
    public void j0(float f3) {
        YogaNative.jni_YGNodeStyleSetWidthJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public float k() {
        float[] fArr = this.arr;
        if (fArr != null) {
            return fArr[3];
        }
        return 0.0f;
    }

    @Override // com.facebook.yoga.r
    public void k0() {
        YogaNative.jni_YGNodeStyleSetWidthAutoJNI(this.f8284g);
    }

    @Override // com.facebook.yoga.r
    public float l() {
        float[] fArr = this.arr;
        if (fArr != null) {
            return fArr[4];
        }
        return 0.0f;
    }

    @Override // com.facebook.yoga.r
    public void l0(float f3) {
        YogaNative.jni_YGNodeStyleSetWidthPercentJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public YogaValue m() {
        return q0(YogaNative.jni_YGNodeStyleGetWidthJNI(this.f8284g));
    }

    @Override // com.facebook.yoga.r
    public void m0(x xVar) {
        YogaNative.jni_YGNodeStyleSetFlexWrapJNI(this.f8284g, xVar.b());
    }

    public final long measure(float f3, int i3, float f4, int i4) {
        if (r()) {
            return this.f8282e.K(this, f3, p.b(i3), f4, p.b(i4));
        }
        throw new RuntimeException("Measure function isn't defined!");
    }

    @Override // com.facebook.yoga.r
    public boolean n() {
        float[] fArr = this.arr;
        return fArr != null ? (((int) fArr[0]) & 16) == 16 : this.f8286i;
    }

    public Object o0() {
        return this.f8285h;
    }

    @Override // com.facebook.yoga.r
    public boolean p() {
        return YogaNative.jni_YGNodeIsDirtyJNI(this.f8284g);
    }

    @Override // com.facebook.yoga.r
    /* JADX INFO: renamed from: p0, reason: merged with bridge method [inline-methods] */
    public YogaNodeJNIBase t(int i3) {
        List list = this.f8281d;
        if (list == null) {
            throw new IllegalStateException("Trying to remove a child of a YogaNode that does not have children");
        }
        YogaNodeJNIBase yogaNodeJNIBase = (YogaNodeJNIBase) list.remove(i3);
        yogaNodeJNIBase.f8279b = null;
        YogaNative.jni_YGNodeRemoveChildJNI(this.f8284g, yogaNodeJNIBase.f8284g);
        return yogaNodeJNIBase;
    }

    @Override // com.facebook.yoga.r
    public boolean r() {
        return this.f8282e != null;
    }

    @Override // com.facebook.yoga.r
    public void s() {
        float[] fArr = this.arr;
        if (fArr != null) {
            fArr[0] = ((int) fArr[0]) & (-17);
        }
        this.f8286i = false;
    }

    @Override // com.facebook.yoga.r
    public void u() {
        this.f8282e = null;
        this.f8283f = null;
        this.f8285h = null;
        this.arr = null;
        this.f8286i = true;
        this.mLayoutDirection = 0;
        YogaNative.jni_YGNodeResetJNI(this.f8284g);
    }

    @Override // com.facebook.yoga.r
    public void v(com.facebook.yoga.a aVar) {
        YogaNative.jni_YGNodeStyleSetAlignContentJNI(this.f8284g, aVar.b());
    }

    @Override // com.facebook.yoga.r
    public void w(com.facebook.yoga.a aVar) {
        YogaNative.jni_YGNodeStyleSetAlignItemsJNI(this.f8284g, aVar.b());
    }

    @Override // com.facebook.yoga.r
    public void x(com.facebook.yoga.a aVar) {
        YogaNative.jni_YGNodeStyleSetAlignSelfJNI(this.f8284g, aVar.b());
    }

    @Override // com.facebook.yoga.r
    public void y(float f3) {
        YogaNative.jni_YGNodeStyleSetAspectRatioJNI(this.f8284g, f3);
    }

    @Override // com.facebook.yoga.r
    public void z(b bVar) {
        this.f8283f = bVar;
        YogaNative.jni_YGNodeSetHasBaselineFuncJNI(this.f8284g, bVar != null);
    }

    YogaNodeJNIBase() {
        this(YogaNative.jni_YGNodeNewJNI());
    }

    YogaNodeJNIBase(c cVar) {
        this(YogaNative.jni_YGNodeNewWithConfigJNI(((e) cVar).f8305a));
        this.f8280c = cVar;
    }
}
