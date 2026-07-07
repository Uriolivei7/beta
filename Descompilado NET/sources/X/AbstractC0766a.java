package x;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.core.view.AbstractC0250g0;
import androidx.core.view.C0237a;
import androidx.core.view.Z;
import java.util.ArrayList;
import java.util.List;
import l.h;
import r.v;
import r.w;
import r.x;
import x.AbstractC0767b;

/* JADX INFO: renamed from: x.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0766a extends C0237a {

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final Rect f10896n = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final AbstractC0767b.a f10897o = new C0152a();

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static final AbstractC0767b.InterfaceC0153b f10898p = new b();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final AccessibilityManager f10903h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final View f10904i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private c f10905j;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Rect f10899d = new Rect();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Rect f10900e = new Rect();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Rect f10901f = new Rect();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final int[] f10902g = new int[2];

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    int f10906k = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    int f10907l = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f10908m = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: x.a$a, reason: collision with other inner class name */
    static class C0152a implements AbstractC0767b.a {
        C0152a() {
        }

        @Override // x.AbstractC0767b.a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(v vVar, Rect rect) {
            vVar.m(rect);
        }
    }

    /* JADX INFO: renamed from: x.a$b */
    static class b implements AbstractC0767b.InterfaceC0153b {
        b() {
        }

        @Override // x.AbstractC0767b.InterfaceC0153b
        /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
        public v a(h hVar, int i3) {
            return (v) hVar.p(i3);
        }

        @Override // x.AbstractC0767b.InterfaceC0153b
        /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
        public int b(h hVar) {
            return hVar.n();
        }
    }

    /* JADX INFO: renamed from: x.a$c */
    private class c extends w {
        c() {
        }

        @Override // r.w
        public v b(int i3) {
            return v.e0(AbstractC0766a.this.F(i3));
        }

        @Override // r.w
        public v d(int i3) {
            int i4 = i3 == 2 ? AbstractC0766a.this.f10906k : AbstractC0766a.this.f10907l;
            if (i4 == Integer.MIN_VALUE) {
                return null;
            }
            return b(i4);
        }

        @Override // r.w
        public boolean f(int i3, int i4, Bundle bundle) {
            return AbstractC0766a.this.N(i3, i4, bundle);
        }
    }

    public AbstractC0766a(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View may not be null");
        }
        this.f10904i = view;
        this.f10903h = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        view.setFocusable(true);
        if (Z.r(view) == 0) {
            Z.f0(view, 1);
        }
    }

    private static Rect B(View view, int i3, Rect rect) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (i3 == 17) {
            rect.set(width, 0, width, height);
        } else if (i3 == 33) {
            rect.set(0, height, width, height);
        } else if (i3 == 66) {
            rect.set(-1, 0, -1, height);
        } else {
            if (i3 != 130) {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            rect.set(0, -1, width, -1);
        }
        return rect;
    }

    private boolean C(Rect rect) {
        if (rect == null || rect.isEmpty() || this.f10904i.getWindowVisibility() != 0) {
            return false;
        }
        Object parent = this.f10904i.getParent();
        while (parent instanceof View) {
            View view = (View) parent;
            if (view.getAlpha() <= 0.0f || view.getVisibility() != 0) {
                return false;
            }
            parent = view.getParent();
        }
        return parent != null;
    }

    private static int D(int i3) {
        if (i3 == 19) {
            return 33;
        }
        if (i3 != 21) {
            return i3 != 22 ? 130 : 66;
        }
        return 17;
    }

    private boolean E(int i3, Rect rect) {
        v vVar;
        h hVarX = x();
        int i4 = this.f10907l;
        v vVar2 = i4 == Integer.MIN_VALUE ? null : (v) hVarX.g(i4);
        if (i3 == 1 || i3 == 2) {
            vVar = (v) AbstractC0767b.d(hVarX, f10898p, f10897o, vVar2, i3, Z.s(this.f10904i) == 1, false);
        } else {
            if (i3 != 17 && i3 != 33 && i3 != 66 && i3 != 130) {
                throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            Rect rect2 = new Rect();
            int i5 = this.f10907l;
            if (i5 != Integer.MIN_VALUE) {
                y(i5, rect2);
            } else if (rect != null) {
                rect2.set(rect);
            } else {
                B(this.f10904i, i3, rect2);
            }
            vVar = (v) AbstractC0767b.c(hVarX, f10898p, f10897o, vVar2, rect2, i3);
        }
        return R(vVar != null ? hVarX.l(hVarX.k(vVar)) : Integer.MIN_VALUE);
    }

    private boolean O(int i3, int i4, Bundle bundle) {
        return i4 != 1 ? i4 != 2 ? i4 != 64 ? i4 != 128 ? H(i3, i4, bundle) : n(i3) : Q(i3) : o(i3) : R(i3);
    }

    private boolean P(int i3, Bundle bundle) {
        return Z.P(this.f10904i, i3, bundle);
    }

    private boolean Q(int i3) {
        int i4;
        if (!this.f10903h.isEnabled() || !this.f10903h.isTouchExplorationEnabled() || (i4 = this.f10906k) == i3) {
            return false;
        }
        if (i4 != Integer.MIN_VALUE) {
            n(i4);
        }
        this.f10906k = i3;
        this.f10904i.invalidate();
        S(i3, 32768);
        return true;
    }

    private void T(int i3) {
        int i4 = this.f10908m;
        if (i4 == i3) {
            return;
        }
        this.f10908m = i3;
        S(i3, 128);
        S(i4, 256);
    }

    private boolean n(int i3) {
        if (this.f10906k != i3) {
            return false;
        }
        this.f10906k = Integer.MIN_VALUE;
        this.f10904i.invalidate();
        S(i3, 65536);
        return true;
    }

    private boolean p() {
        int i3 = this.f10907l;
        return i3 != Integer.MIN_VALUE && H(i3, 16, null);
    }

    private AccessibilityEvent q(int i3, int i4) {
        return i3 != -1 ? r(i3, i4) : s(i4);
    }

    private AccessibilityEvent r(int i3, int i4) {
        AccessibilityEvent accessibilityEventObtain = AccessibilityEvent.obtain(i4);
        v vVarF = F(i3);
        accessibilityEventObtain.getText().add(vVarF.E());
        accessibilityEventObtain.setContentDescription(vVarF.u());
        accessibilityEventObtain.setScrollable(vVarF.Y());
        accessibilityEventObtain.setPassword(vVarF.W());
        accessibilityEventObtain.setEnabled(vVarF.Q());
        accessibilityEventObtain.setChecked(vVarF.N());
        J(i3, accessibilityEventObtain);
        if (accessibilityEventObtain.getText().isEmpty() && accessibilityEventObtain.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        accessibilityEventObtain.setClassName(vVarF.q());
        x.c(accessibilityEventObtain, this.f10904i, i3);
        accessibilityEventObtain.setPackageName(this.f10904i.getContext().getPackageName());
        return accessibilityEventObtain;
    }

    private AccessibilityEvent s(int i3) {
        AccessibilityEvent accessibilityEventObtain = AccessibilityEvent.obtain(i3);
        this.f10904i.onInitializeAccessibilityEvent(accessibilityEventObtain);
        return accessibilityEventObtain;
    }

    private v t(int i3) {
        v vVarC0 = v.c0();
        vVarC0.u0(true);
        vVarC0.v0(true);
        vVarC0.p0("android.view.View");
        Rect rect = f10896n;
        vVarC0.l0(rect);
        vVarC0.m0(rect);
        vVarC0.C0(this.f10904i);
        L(i3, vVarC0);
        if (vVarC0.E() == null && vVarC0.u() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        vVarC0.m(this.f10900e);
        if (this.f10900e.equals(rect)) {
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
        }
        int iK = vVarC0.k();
        if ((iK & 64) != 0) {
            throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        }
        if ((iK & 128) != 0) {
            throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        }
        vVarC0.A0(this.f10904i.getContext().getPackageName());
        vVarC0.K0(this.f10904i, i3);
        if (this.f10906k == i3) {
            vVarC0.j0(true);
            vVarC0.a(128);
        } else {
            vVarC0.j0(false);
            vVarC0.a(64);
        }
        boolean z3 = this.f10907l == i3;
        if (z3) {
            vVarC0.a(2);
        } else if (vVarC0.R()) {
            vVarC0.a(1);
        }
        vVarC0.w0(z3);
        this.f10904i.getLocationOnScreen(this.f10902g);
        vVarC0.n(this.f10899d);
        if (this.f10899d.equals(rect)) {
            vVarC0.m(this.f10899d);
            if (vVarC0.f10435b != -1) {
                v vVarC02 = v.c0();
                for (int i4 = vVarC0.f10435b; i4 != -1; i4 = vVarC02.f10435b) {
                    vVarC02.D0(this.f10904i, -1);
                    vVarC02.l0(f10896n);
                    L(i4, vVarC02);
                    vVarC02.m(this.f10900e);
                    Rect rect2 = this.f10899d;
                    Rect rect3 = this.f10900e;
                    rect2.offset(rect3.left, rect3.top);
                }
                vVarC02.g0();
            }
            this.f10899d.offset(this.f10902g[0] - this.f10904i.getScrollX(), this.f10902g[1] - this.f10904i.getScrollY());
        }
        if (this.f10904i.getLocalVisibleRect(this.f10901f)) {
            this.f10901f.offset(this.f10902g[0] - this.f10904i.getScrollX(), this.f10902g[1] - this.f10904i.getScrollY());
            if (this.f10899d.intersect(this.f10901f)) {
                vVarC0.m0(this.f10899d);
                if (C(this.f10899d)) {
                    vVarC0.O0(true);
                }
            }
        }
        return vVarC0;
    }

    private v u() {
        v vVarD0 = v.d0(this.f10904i);
        Z.N(this.f10904i, vVarD0);
        ArrayList arrayList = new ArrayList();
        A(arrayList);
        if (vVarD0.p() > 0 && arrayList.size() > 0) {
            throw new RuntimeException("Views cannot have both real and virtual children");
        }
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            vVarD0.d(this.f10904i, ((Integer) arrayList.get(i3)).intValue());
        }
        return vVarD0;
    }

    private h x() {
        ArrayList arrayList = new ArrayList();
        A(arrayList);
        h hVar = new h();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            hVar.m(i3, t(i3));
        }
        return hVar;
    }

    private void y(int i3, Rect rect) {
        F(i3).m(rect);
    }

    protected abstract void A(List list);

    v F(int i3) {
        return i3 == -1 ? u() : t(i3);
    }

    public final void G(boolean z3, int i3, Rect rect) {
        int i4 = this.f10907l;
        if (i4 != Integer.MIN_VALUE) {
            o(i4);
        }
        if (z3) {
            E(i3, rect);
        }
    }

    protected abstract boolean H(int i3, int i4, Bundle bundle);

    protected abstract void L(int i3, v vVar);

    boolean N(int i3, int i4, Bundle bundle) {
        return i3 != -1 ? O(i3, i4, bundle) : P(i4, bundle);
    }

    public final boolean R(int i3) {
        int i4;
        if ((!this.f10904i.isFocused() && !this.f10904i.requestFocus()) || (i4 = this.f10907l) == i3) {
            return false;
        }
        if (i4 != Integer.MIN_VALUE) {
            o(i4);
        }
        this.f10907l = i3;
        M(i3, true);
        S(i3, 8);
        return true;
    }

    public final boolean S(int i3, int i4) {
        ViewParent parent;
        if (i3 == Integer.MIN_VALUE || !this.f10903h.isEnabled() || (parent = this.f10904i.getParent()) == null) {
            return false;
        }
        return AbstractC0250g0.h(parent, this.f10904i, q(i3, i4));
    }

    @Override // androidx.core.view.C0237a
    public w b(View view) {
        if (this.f10905j == null) {
            this.f10905j = new c();
        }
        return this.f10905j;
    }

    @Override // androidx.core.view.C0237a
    public void f(View view, AccessibilityEvent accessibilityEvent) {
        super.f(view, accessibilityEvent);
        I(accessibilityEvent);
    }

    @Override // androidx.core.view.C0237a
    public void g(View view, v vVar) {
        super.g(view, vVar);
        K(vVar);
    }

    public final boolean o(int i3) {
        if (this.f10907l != i3) {
            return false;
        }
        this.f10907l = Integer.MIN_VALUE;
        M(i3, false);
        S(i3, 8);
        return true;
    }

    public final boolean v(MotionEvent motionEvent) {
        if (!this.f10903h.isEnabled() || !this.f10903h.isTouchExplorationEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 7 || action == 9) {
            int iZ = z(motionEvent.getX(), motionEvent.getY());
            T(iZ);
            return iZ != Integer.MIN_VALUE;
        }
        if (action != 10 || this.f10908m == Integer.MIN_VALUE) {
            return false;
        }
        T(Integer.MIN_VALUE);
        return true;
    }

    public final boolean w(KeyEvent keyEvent) {
        int i3 = 0;
        if (keyEvent.getAction() == 1) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 61) {
            if (keyEvent.hasNoModifiers()) {
                return E(2, null);
            }
            if (keyEvent.hasModifiers(1)) {
                return E(1, null);
            }
            return false;
        }
        if (keyCode != 66) {
            switch (keyCode) {
                case 19:
                case 20:
                case 21:
                case 22:
                    if (!keyEvent.hasNoModifiers()) {
                        return false;
                    }
                    int iD = D(keyCode);
                    int repeatCount = keyEvent.getRepeatCount() + 1;
                    boolean z3 = false;
                    while (i3 < repeatCount && E(iD, null)) {
                        i3++;
                        z3 = true;
                    }
                    return z3;
                case 23:
                    break;
                default:
                    return false;
            }
        }
        if (!keyEvent.hasNoModifiers() || keyEvent.getRepeatCount() != 0) {
            return false;
        }
        p();
        return true;
    }

    protected abstract int z(float f3, float f4);

    protected void I(AccessibilityEvent accessibilityEvent) {
    }

    protected void K(v vVar) {
    }

    protected void J(int i3, AccessibilityEvent accessibilityEvent) {
    }

    protected void M(int i3, boolean z3) {
    }
}
