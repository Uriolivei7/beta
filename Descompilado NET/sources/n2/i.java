package n2;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import com.swmansion.gesturehandler.react.j;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public static final a f10009m = new a(null);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final PointF f10010n = new PointF();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final float[] f10011o = new float[2];

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static final Matrix f10012p = new Matrix();

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private static final float[] f10013q = new float[2];

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static final Comparator f10014r = new Comparator() { // from class: n2.f
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return i.t((C0625d) obj, (C0625d) obj2);
        }
    };

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ViewGroup f10015a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final j f10016b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final D f10017c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f10018d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final ArrayList f10019e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final ArrayList f10020f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final ArrayList f10021g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final HashSet f10022h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f10023i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f10024j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f10025k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f10026l;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean h(C0625d c0625d, C0625d c0625d2) {
            return c0625d == c0625d2 || c0625d.J0(c0625d2) || c0625d2.J0(c0625d);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean i(int i3) {
            return i3 == 3 || i3 == 1 || i3 == 5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean j(float f3, float f4, View view) {
            return 0.0f <= f3 && f3 <= ((float) view.getWidth()) && 0.0f <= f4 && f4 <= ((float) view.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean k(C0625d c0625d, C0625d c0625d2) {
            if (!c0625d.W(c0625d2) || h(c0625d, c0625d2)) {
                return false;
            }
            if (c0625d == c0625d2 || !(c0625d.Y() || c0625d.Q() == 4)) {
                return true;
            }
            return c0625d.I0(c0625d2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean l(C0625d c0625d, C0625d c0625d2) {
            return c0625d != c0625d2 && (c0625d.L0(c0625d2) || c0625d2.K0(c0625d));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean m(View view, float[] fArr) {
            return !((view instanceof ViewGroup) && view.getBackground() == null) && j(fArr[0], fArr[1], view);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void n(float f3, float f4, ViewGroup viewGroup, View view, PointF pointF) {
            float scrollX = (f3 + viewGroup.getScrollX()) - view.getLeft();
            float scrollY = (f4 + viewGroup.getScrollY()) - view.getTop();
            Matrix matrix = view.getMatrix();
            if (!matrix.isIdentity()) {
                float[] fArr = i.f10011o;
                fArr[0] = scrollX;
                fArr[1] = scrollY;
                matrix.invert(i.f10012p);
                i.f10012p.mapPoints(fArr);
                float f5 = fArr[0];
                scrollY = fArr[1];
                scrollX = f5;
            }
            pointF.set(scrollX, scrollY);
        }

        private a() {
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f10027a;

        static {
            int[] iArr = new int[v.values().length];
            try {
                iArr[v.f10099b.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[v.f10101d.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[v.f10100c.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[v.f10102e.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f10027a = iArr;
        }
    }

    public i(ViewGroup viewGroup, j jVar, D d4) {
        D2.h.f(viewGroup, "wrapperView");
        D2.h.f(jVar, "handlerRegistry");
        D2.h.f(d4, "viewConfigHelper");
        this.f10015a = viewGroup;
        this.f10016b = jVar;
        this.f10017c = d4;
        this.f10019e = new ArrayList();
        this.f10020f = new ArrayList();
        this.f10021g = new ArrayList();
        this.f10022h = new HashSet();
    }

    private final void C(C0625d c0625d, View view) {
        if (this.f10019e.contains(c0625d)) {
            return;
        }
        this.f10019e.add(c0625d);
        c0625d.t0(false);
        c0625d.u0(false);
        c0625d.s0(Integer.MAX_VALUE);
        c0625d.m0(view, this);
    }

    private final boolean D(View view, float[] fArr, int i3, MotionEvent motionEvent) {
        boolean z3;
        ArrayList arrayListA = this.f10016b.a(view);
        if (arrayListA != null) {
            synchronized (arrayListA) {
                try {
                    Iterator it = arrayListA.iterator();
                    D2.h.e(it, "iterator(...)");
                    z3 = false;
                    while (it.hasNext()) {
                        C0625d c0625d = (C0625d) it.next();
                        if (c0625d.b0() && c0625d.d0(view, fArr[0], fArr[1]) && !I(c0625d, motionEvent.getAction())) {
                            C(c0625d, view);
                            c0625d.M0(i3);
                            z3 = true;
                        }
                    }
                    r2.r rVar = r2.r.f10584a;
                } catch (Throwable th) {
                    throw th;
                }
            }
        } else {
            z3 = false;
        }
        float width = view.getWidth();
        float f3 = fArr[0];
        if (0.0f <= f3 && f3 <= width) {
            float height = view.getHeight();
            float f4 = fArr[1];
            if (0.0f <= f4 && f4 <= height && y(view) && p(view, fArr, i3)) {
                return true;
            }
        }
        return z3;
    }

    private final void E() {
        if (this.f10023i || this.f10024j != 0) {
            this.f10025k = true;
        } else {
            l();
        }
    }

    private final boolean G(C0625d c0625d) {
        ArrayList<C0625d> arrayList = this.f10019e;
        if (arrayList != null && arrayList.isEmpty()) {
            return false;
        }
        for (C0625d c0625d2 : arrayList) {
            if (c0625d.W(c0625d2) && c0625d2.Q() == 4 && !f10009m.h(c0625d, c0625d2) && c0625d.a0(c0625d2)) {
                return true;
            }
        }
        return false;
    }

    private final boolean H(C0625d c0625d) {
        ArrayList<C0625d> arrayList = this.f10019e;
        if (arrayList != null && arrayList.isEmpty()) {
            return false;
        }
        for (C0625d c0625d2 : arrayList) {
            if (f10009m.l(c0625d, c0625d2) && c0625d2.Q() == 5) {
                return true;
            }
        }
        return false;
    }

    private final boolean I(C0625d c0625d, int i3) {
        return ((c0625d instanceof m) || (c0625d instanceof j.b) || !AbstractC0717n.j(10, 9, 7).contains(Integer.valueOf(i3))) ? false : true;
    }

    private final boolean L(View view, float[] fArr, int i3, MotionEvent motionEvent) {
        int i4 = b.f10027a[this.f10017c.a(view).ordinal()];
        if (i4 != 1) {
            if (i4 != 2) {
                if (i4 != 3) {
                    if (i4 != 4) {
                        throw new C0685h();
                    }
                    boolean zR = view instanceof ViewGroup ? r((ViewGroup) view, fArr, i3, motionEvent) : false;
                    if (D(view, fArr, i3, motionEvent) || zR || f10009m.m(view, fArr)) {
                        return true;
                    }
                } else {
                    if (view instanceof ViewGroup) {
                        boolean zR2 = r((ViewGroup) view, fArr, i3, motionEvent);
                        if (!zR2) {
                            return zR2;
                        }
                        D(view, fArr, i3, motionEvent);
                        return zR2;
                    }
                    if (view instanceof EditText) {
                        return D(view, fArr, i3, motionEvent);
                    }
                }
            } else if (D(view, fArr, i3, motionEvent) || f10009m.m(view, fArr)) {
                return true;
            }
        }
        return false;
    }

    private final void M(C0625d c0625d) {
        if (H(c0625d) || G(c0625d)) {
            c0625d.o();
        } else if (u(c0625d)) {
            h(c0625d);
        } else {
            z(c0625d);
            c0625d.u0(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final r2.r g(C0625d c0625d) {
        q qVar = (q) c0625d;
        qVar.n();
        qVar.i();
        qVar.z();
        return r2.r.f10584a;
    }

    private final void h(C0625d c0625d) {
        if (this.f10020f.contains(c0625d)) {
            return;
        }
        this.f10020f.add(c0625d);
        this.f10022h.add(Integer.valueOf(c0625d.R()));
        c0625d.u0(true);
        int i3 = this.f10026l;
        this.f10026l = i3 + 1;
        c0625d.s0(i3);
    }

    private final boolean i(View view) {
        return view.getVisibility() == 0 && view.getAlpha() >= this.f10018d;
    }

    private final void j() {
        Iterator it = AbstractC0717n.Y(this.f10020f).iterator();
        while (it.hasNext()) {
            ((C0625d) it.next()).o();
        }
        this.f10021g.clear();
        this.f10021g.addAll(this.f10019e);
        Iterator it2 = AbstractC0717n.Y(this.f10019e).iterator();
        while (it2.hasNext()) {
            ((C0625d) it2.next()).o();
        }
    }

    private final void k() {
        for (C0625d c0625d : AbstractC0717n.e0(this.f10020f)) {
            if (!c0625d.Y()) {
                this.f10020f.remove(c0625d);
                this.f10022h.remove(Integer.valueOf(c0625d.R()));
            }
        }
    }

    private final void l() {
        for (C0625d c0625d : AbstractC0717n.C(this.f10019e)) {
            if (f10009m.i(c0625d.Q()) && !c0625d.Y()) {
                c0625d.n0();
                c0625d.t0(false);
                c0625d.u0(false);
                c0625d.s0(Integer.MAX_VALUE);
            }
        }
        AbstractC0717n.x(this.f10019e, new C2.l() { // from class: n2.g
            @Override // C2.l
            public final Object d(Object obj) {
                return Boolean.valueOf(i.m((C0625d) obj));
            }
        });
        this.f10025k = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean m(C0625d c0625d) {
        D2.h.f(c0625d, "it");
        return f10009m.i(c0625d.Q()) && !c0625d.Y();
    }

    private final void n(C0625d c0625d, MotionEvent motionEvent) {
        if (!x(c0625d.U())) {
            c0625d.o();
            return;
        }
        if (c0625d.Q0()) {
            int actionMasked = motionEvent.getActionMasked();
            View viewU = c0625d.U();
            MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
            D2.h.e(motionEventObtain, "obtain(...)");
            MotionEvent motionEventJ = J(viewU, motionEventObtain);
            if (c0625d.L() && c0625d.Q() != 0) {
                c0625d.P0(motionEventJ, motionEvent);
            }
            if (!c0625d.Y() || actionMasked != 2) {
                boolean z3 = c0625d.Q() == 0;
                c0625d.V(motionEventJ, motionEvent);
                if (c0625d.X()) {
                    if (c0625d.P()) {
                        c0625d.F0(false);
                        c0625d.p0();
                    }
                    c0625d.t(motionEventJ);
                }
                if (c0625d.L() && z3) {
                    c0625d.P0(motionEventJ, motionEvent);
                }
                if (actionMasked == 1 || actionMasked == 6 || actionMasked == 10) {
                    c0625d.N0(motionEventJ.getPointerId(motionEventJ.getActionIndex()));
                }
            }
            motionEventJ.recycle();
        }
    }

    private final void o(MotionEvent motionEvent) {
        this.f10021g.clear();
        this.f10021g.addAll(this.f10019e);
        AbstractC0717n.s(this.f10021g, f10014r);
        Iterator it = this.f10021g.iterator();
        D2.h.e(it, "iterator(...)");
        while (it.hasNext()) {
            n((C0625d) it.next(), motionEvent);
        }
    }

    private final boolean p(View view, float[] fArr, int i3) {
        boolean z3 = false;
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parent;
                ArrayList arrayListA = this.f10016b.a((View) parent);
                if (arrayListA != null) {
                    synchronized (arrayListA) {
                        try {
                            Iterator it = arrayListA.iterator();
                            D2.h.e(it, "iterator(...)");
                            while (it.hasNext()) {
                                C0625d c0625d = (C0625d) it.next();
                                if (c0625d.b0() && c0625d.d0(view, fArr[0], fArr[1])) {
                                    C(c0625d, viewGroup);
                                    c0625d.M0(i3);
                                    z3 = true;
                                }
                            }
                            r2.r rVar = r2.r.f10584a;
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return z3;
    }

    private final void q(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(actionIndex);
        float[] fArr = f10013q;
        fArr[0] = motionEvent.getX(actionIndex);
        fArr[1] = motionEvent.getY(actionIndex);
        L(this.f10015a, fArr, pointerId, motionEvent);
        r(this.f10015a, fArr, pointerId, motionEvent);
    }

    private final boolean r(ViewGroup viewGroup, float[] fArr, int i3, MotionEvent motionEvent) {
        for (int childCount = viewGroup.getChildCount() - 1; -1 < childCount; childCount--) {
            View viewB = this.f10017c.b(viewGroup, childCount);
            if (i(viewB)) {
                PointF pointF = f10010n;
                a aVar = f10009m;
                aVar.n(fArr[0], fArr[1], viewGroup, viewB, pointF);
                float f3 = fArr[0];
                float f4 = fArr[1];
                fArr[0] = pointF.x;
                fArr[1] = pointF.y;
                boolean zL = (!w(viewB) || aVar.j(fArr[0], fArr[1], viewB)) ? L(viewB, fArr, i3, motionEvent) : false;
                fArr[0] = f3;
                fArr[1] = f4;
                if (zL) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int t(C0625d c0625d, C0625d c0625d2) {
        if ((c0625d.X() && c0625d2.X()) || (c0625d.Y() && c0625d2.Y())) {
            return Integer.signum(c0625d2.E() - c0625d.E());
        }
        if (!c0625d.X()) {
            if (!c0625d2.X()) {
                if (!c0625d.Y()) {
                    if (!c0625d2.Y()) {
                        return 0;
                    }
                }
            }
            return 1;
        }
        return -1;
    }

    private final boolean u(C0625d c0625d) {
        ArrayList<C0625d> arrayList = this.f10019e;
        if (arrayList != null && arrayList.isEmpty()) {
            return false;
        }
        for (C0625d c0625d2 : arrayList) {
            a aVar = f10009m;
            if (!aVar.i(c0625d2.Q()) && aVar.l(c0625d, c0625d2)) {
                return true;
            }
        }
        return false;
    }

    private final boolean w(View view) {
        return !(view instanceof ViewGroup) || this.f10017c.c((ViewGroup) view);
    }

    private final boolean x(View view) {
        if (view == null) {
            return false;
        }
        if (view == this.f10015a) {
            return true;
        }
        ViewParent parent = view.getParent();
        while (parent != null && parent != this.f10015a) {
            parent = parent.getParent();
        }
        return parent == this.f10015a;
    }

    private final boolean y(View view) {
        ViewParent parent = view.getParent();
        ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
        if (viewGroup == null) {
            return false;
        }
        Matrix matrix = view.getMatrix();
        float[] fArr = f10011o;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        matrix.mapPoints(fArr);
        float left = fArr[0] + view.getLeft();
        float top = fArr[1] + view.getTop();
        return left < 0.0f || left + ((float) view.getWidth()) > ((float) viewGroup.getWidth()) || top < 0.0f || top + ((float) view.getHeight()) > ((float) viewGroup.getHeight());
    }

    private final void z(C0625d c0625d) {
        int iQ = c0625d.Q();
        c0625d.u0(false);
        c0625d.t0(true);
        c0625d.F0(true);
        int i3 = this.f10026l;
        this.f10026l = i3 + 1;
        c0625d.s0(i3);
        for (C0625d c0625d2 : AbstractC0717n.C(this.f10019e)) {
            if (f10009m.k(c0625d2, c0625d)) {
                c0625d2.o();
            }
        }
        for (C0625d c0625d3 : AbstractC0717n.Y(this.f10020f)) {
            if (f10009m.k(c0625d3, c0625d)) {
                c0625d3.u0(false);
            }
        }
        k();
        if (iQ == 1 || iQ == 3) {
            return;
        }
        c0625d.u(4, 2);
        if (iQ != 4) {
            c0625d.u(5, 4);
            if (iQ != 5) {
                c0625d.u(0, 5);
            }
        }
    }

    public final void A(C0625d c0625d, int i3, int i4) {
        D2.h.f(c0625d, "handler");
        this.f10024j++;
        if (f10009m.i(i3)) {
            for (C0625d c0625d2 : AbstractC0717n.e0(this.f10020f)) {
                if (f10009m.l(c0625d2, c0625d) && this.f10022h.contains(Integer.valueOf(c0625d2.R()))) {
                    if (i3 == 5) {
                        c0625d2.o();
                        if (c0625d2.Q() == 5) {
                            c0625d2.u(3, 2);
                        }
                        c0625d2.u0(false);
                    } else {
                        M(c0625d2);
                    }
                }
            }
            k();
        }
        if (i3 == 4) {
            M(c0625d);
        } else if (i4 == 4 || i4 == 5) {
            if (c0625d.X()) {
                c0625d.u(i3, i4);
            } else if (i4 == 4 && (i3 == 3 || i3 == 1)) {
                c0625d.u(i3, 2);
            }
        } else if (i4 != 0 || i3 != 3) {
            c0625d.u(i3, i4);
        }
        this.f10024j--;
        E();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean B(android.view.MotionEvent r4) {
        /*
            r3 = this;
            java.lang.String r0 = "event"
            D2.h.f(r4, r0)
            r0 = 1
            r3.f10023i = r0
            int r1 = r4.getActionMasked()
            if (r1 == 0) goto L1c
            r2 = 3
            if (r1 == r2) goto L18
            r2 = 5
            if (r1 == r2) goto L1c
            r2 = 7
            if (r1 == r2) goto L1c
            goto L1f
        L18:
            r3.j()
            goto L1f
        L1c:
            r3.q(r4)
        L1f:
            r3.o(r4)
            r4 = 0
            r3.f10023i = r4
            boolean r4 = r3.f10025k
            if (r4 == 0) goto L30
            int r4 = r3.f10024j
            if (r4 != 0) goto L30
            r3.l()
        L30:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: n2.i.B(android.view.MotionEvent):boolean");
    }

    public final void F(float f3) {
        this.f10018d = f3;
    }

    public final MotionEvent J(View view, MotionEvent motionEvent) {
        D2.h.f(motionEvent, "event");
        if (view == null) {
            return motionEvent;
        }
        ViewParent parent = view.getParent();
        ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
        if (!D2.h.b(viewGroup, this.f10015a)) {
            J(viewGroup, motionEvent);
        }
        if (viewGroup != null) {
            motionEvent.setLocation((motionEvent.getX() + viewGroup.getScrollX()) - view.getLeft(), (motionEvent.getY() + viewGroup.getScrollY()) - view.getTop());
        }
        if (!view.getMatrix().isIdentity()) {
            Matrix matrix = view.getMatrix();
            Matrix matrix2 = f10012p;
            matrix.invert(matrix2);
            motionEvent.transform(matrix2);
        }
        return motionEvent;
    }

    public final PointF K(View view, PointF pointF) {
        D2.h.f(pointF, "point");
        if (view == null) {
            return pointF;
        }
        ViewParent parent = view.getParent();
        ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
        if (!D2.h.b(viewGroup, this.f10015a)) {
            K(viewGroup, pointF);
        }
        if (viewGroup != null) {
            pointF.x += viewGroup.getScrollX() - view.getLeft();
            pointF.y += viewGroup.getScrollY() - view.getTop();
        }
        if (!view.getMatrix().isIdentity()) {
            Matrix matrix = view.getMatrix();
            Matrix matrix2 = f10012p;
            matrix.invert(matrix2);
            float[] fArr = f10013q;
            fArr[0] = pointF.x;
            fArr[1] = pointF.y;
            matrix2.mapPoints(fArr);
            pointF.x = fArr[0];
            pointF.y = fArr[1];
        }
        return pointF;
    }

    public final void f(View view) {
        D2.h.f(view, "view");
        ArrayList<C0625d> arrayListA = this.f10016b.a(view);
        if (arrayListA != null) {
            for (final C0625d c0625d : arrayListA) {
                if (c0625d instanceof q) {
                    C(c0625d, view);
                    ((q) c0625d).R0(new C2.a() { // from class: n2.h
                        @Override // C2.a
                        public final Object a() {
                            return i.g(c0625d);
                        }
                    });
                }
            }
        }
    }

    public final ArrayList s(View view) {
        D2.h.f(view, "view");
        return this.f10016b.a(view);
    }

    public final boolean v() {
        ArrayList arrayList = this.f10019e;
        if (arrayList != null && arrayList.isEmpty()) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((C0625d) it.next()).Q() == 4) {
                return true;
            }
        }
        return false;
    }
}
