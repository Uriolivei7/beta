package androidx.core.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import androidx.core.view.C0237a;
import androidx.core.view.C0271s;
import androidx.core.view.D;
import androidx.core.view.E;
import androidx.core.view.G;
import androidx.core.view.H;
import androidx.core.view.InterfaceC0272t;
import androidx.core.view.Z;
import m.AbstractC0595a;
import r.v;
import r.x;

/* JADX INFO: loaded from: classes.dex */
public class NestedScrollView extends FrameLayout implements G {

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private static final float f4673E = (float) (Math.log(0.78d) / Math.log(0.9d));

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private static final a f4674F = new a();

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private static final int[] f4675G = {R.attr.fillViewport};

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private float f4676A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private d f4677B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    final c f4678C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    C0271s f4679D;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float f4680b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f4681c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Rect f4682d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private OverScroller f4683e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public EdgeEffect f4684f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public EdgeEffect f4685g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f4686h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f4687i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f4688j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private View f4689k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f4690l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private VelocityTracker f4691m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f4692n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f4693o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f4694p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f4695q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f4696r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f4697s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final int[] f4698t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final int[] f4699u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private int f4700v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private int f4701w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private e f4702x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final H f4703y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private final E f4704z;

    static class a extends C0237a {
        a() {
        }

        @Override // androidx.core.view.C0237a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            super.f(view, accessibilityEvent);
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            accessibilityEvent.setClassName(ScrollView.class.getName());
            accessibilityEvent.setScrollable(nestedScrollView.getScrollRange() > 0);
            accessibilityEvent.setScrollX(nestedScrollView.getScrollX());
            accessibilityEvent.setScrollY(nestedScrollView.getScrollY());
            x.a(accessibilityEvent, nestedScrollView.getScrollX());
            x.b(accessibilityEvent, nestedScrollView.getScrollRange());
        }

        @Override // androidx.core.view.C0237a
        public void g(View view, v vVar) {
            int scrollRange;
            super.g(view, vVar);
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            vVar.p0(ScrollView.class.getName());
            if (!nestedScrollView.isEnabled() || (scrollRange = nestedScrollView.getScrollRange()) <= 0) {
                return;
            }
            vVar.H0(true);
            if (nestedScrollView.getScrollY() > 0) {
                vVar.b(v.a.f10471q);
                vVar.b(v.a.f10438B);
            }
            if (nestedScrollView.getScrollY() < scrollRange) {
                vVar.b(v.a.f10470p);
                vVar.b(v.a.f10440D);
            }
        }

        @Override // androidx.core.view.C0237a
        public boolean j(View view, int i3, Bundle bundle) {
            if (super.j(view, i3, bundle)) {
                return true;
            }
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            if (!nestedScrollView.isEnabled()) {
                return false;
            }
            int height = nestedScrollView.getHeight();
            Rect rect = new Rect();
            if (nestedScrollView.getMatrix().isIdentity() && nestedScrollView.getGlobalVisibleRect(rect)) {
                height = rect.height();
            }
            if (i3 != 4096) {
                if (i3 == 8192 || i3 == 16908344) {
                    int iMax = Math.max(nestedScrollView.getScrollY() - ((height - nestedScrollView.getPaddingBottom()) - nestedScrollView.getPaddingTop()), 0);
                    if (iMax == nestedScrollView.getScrollY()) {
                        return false;
                    }
                    nestedScrollView.W(0, iMax, true);
                    return true;
                }
                if (i3 != 16908346) {
                    return false;
                }
            }
            int iMin = Math.min(nestedScrollView.getScrollY() + ((height - nestedScrollView.getPaddingBottom()) - nestedScrollView.getPaddingTop()), nestedScrollView.getScrollRange());
            if (iMin == nestedScrollView.getScrollY()) {
                return false;
            }
            nestedScrollView.W(0, iMin, true);
            return true;
        }
    }

    static class b {
        static boolean a(ViewGroup viewGroup) {
            return viewGroup.getClipToPadding();
        }
    }

    class c implements InterfaceC0272t {
        c() {
        }

        @Override // androidx.core.view.InterfaceC0272t
        public boolean a(float f3) {
            if (f3 == 0.0f) {
                return false;
            }
            c();
            NestedScrollView.this.v((int) f3);
            return true;
        }

        @Override // androidx.core.view.InterfaceC0272t
        public float b() {
            return -NestedScrollView.this.getVerticalScrollFactorCompat();
        }

        @Override // androidx.core.view.InterfaceC0272t
        public void c() {
            NestedScrollView.this.f4683e.abortAnimation();
        }
    }

    public interface d {
        void a(NestedScrollView nestedScrollView, int i3, int i4, int i5, int i6);
    }

    static class e extends View.BaseSavedState {
        public static final Parcelable.Creator<e> CREATOR = new a();

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f4706a;

        class a implements Parcelable.Creator {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public e createFromParcel(Parcel parcel) {
                return new e(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
            public e[] newArray(int i3) {
                return new e[i3];
            }
        }

        e(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.f4706a + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeInt(this.f4706a);
        }

        e(Parcel parcel) {
            super(parcel);
            this.f4706a = parcel.readInt();
        }
    }

    public NestedScrollView(Context context) {
        this(context, null);
    }

    private void A() {
        VelocityTracker velocityTracker = this.f4691m;
        if (velocityTracker == null) {
            this.f4691m = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void B() {
        this.f4683e = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.f4694p = viewConfiguration.getScaledTouchSlop();
        this.f4695q = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f4696r = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    private void C() {
        if (this.f4691m == null) {
            this.f4691m = VelocityTracker.obtain();
        }
    }

    private void D(int i3, int i4) {
        this.f4686h = i3;
        this.f4697s = i4;
        X(2, 0);
    }

    private boolean E(View view) {
        return !G(view, 0, getHeight());
    }

    private static boolean F(View view, View view2) {
        if (view == view2) {
            return true;
        }
        Object parent = view.getParent();
        return (parent instanceof ViewGroup) && F((View) parent, view2);
    }

    private boolean G(View view, int i3, int i4) {
        view.getDrawingRect(this.f4682d);
        offsetDescendantRectToMyCoords(view, this.f4682d);
        return this.f4682d.bottom + i3 >= getScrollY() && this.f4682d.top - i3 <= getScrollY() + i4;
    }

    private void H(int i3, int i4, int[] iArr) {
        int scrollY = getScrollY();
        scrollBy(0, i3);
        int scrollY2 = getScrollY() - scrollY;
        if (iArr != null) {
            iArr[1] = iArr[1] + scrollY2;
        }
        this.f4704z.e(0, scrollY2, 0, i3 - scrollY2, null, i4, iArr);
    }

    private void I(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.f4697s) {
            int i3 = actionIndex == 0 ? 1 : 0;
            this.f4686h = (int) motionEvent.getY(i3);
            this.f4697s = motionEvent.getPointerId(i3);
            VelocityTracker velocityTracker = this.f4691m;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void L() {
        VelocityTracker velocityTracker = this.f4691m;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f4691m = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0060  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int M(int r4, float r5) {
        /*
            r3 = this;
            int r0 = r3.getWidth()
            float r0 = (float) r0
            float r5 = r5 / r0
            float r4 = (float) r4
            int r0 = r3.getHeight()
            float r0 = (float) r0
            float r4 = r4 / r0
            android.widget.EdgeEffect r0 = r3.f4684f
            float r0 = androidx.core.widget.d.b(r0)
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L31
            android.widget.EdgeEffect r0 = r3.f4684f
            float r4 = -r4
            float r4 = androidx.core.widget.d.d(r0, r4, r5)
            float r4 = -r4
            android.widget.EdgeEffect r5 = r3.f4684f
            float r5 = androidx.core.widget.d.b(r5)
            int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r5 != 0) goto L2f
            android.widget.EdgeEffect r5 = r3.f4684f
            r5.onRelease()
        L2f:
            r1 = r4
            goto L54
        L31:
            android.widget.EdgeEffect r0 = r3.f4685g
            float r0 = androidx.core.widget.d.b(r0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L54
            android.widget.EdgeEffect r0 = r3.f4685g
            r2 = 1065353216(0x3f800000, float:1.0)
            float r2 = r2 - r5
            float r4 = androidx.core.widget.d.d(r0, r4, r2)
            android.widget.EdgeEffect r5 = r3.f4685g
            float r5 = androidx.core.widget.d.b(r5)
            int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r5 != 0) goto L2f
            android.widget.EdgeEffect r5 = r3.f4685g
            r5.onRelease()
            goto L2f
        L54:
            int r4 = r3.getHeight()
            float r4 = (float) r4
            float r1 = r1 * r4
            int r4 = java.lang.Math.round(r1)
            if (r4 == 0) goto L63
            r3.invalidate()
        L63:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.NestedScrollView.M(int, float):int");
    }

    private void N(boolean z3) {
        if (z3) {
            X(2, 1);
        } else {
            Z(1);
        }
        this.f4701w = getScrollY();
        postInvalidateOnAnimation();
    }

    private boolean O(int i3, int i4, int i5) {
        int height = getHeight();
        int scrollY = getScrollY();
        int i6 = height + scrollY;
        boolean z3 = false;
        boolean z4 = i3 == 33;
        View viewU = u(z4, i4, i5);
        if (viewU == null) {
            viewU = this;
        }
        if (i4 < scrollY || i5 > i6) {
            P(z4 ? i4 - scrollY : i5 - i6, 0, 1, true);
            z3 = true;
        }
        if (viewU != findFocus()) {
            viewU.requestFocus(i3);
        }
        return z3;
    }

    private int P(int i3, int i4, int i5, boolean z3) {
        int i6;
        int i7;
        VelocityTracker velocityTracker;
        if (i5 == 1) {
            X(2, i5);
        }
        boolean z4 = false;
        if (l(0, i3, this.f4699u, this.f4698t, i5)) {
            i6 = i3 - this.f4699u[1];
            i7 = this.f4698t[1];
        } else {
            i6 = i3;
            i7 = 0;
        }
        int scrollY = getScrollY();
        int scrollRange = getScrollRange();
        boolean z5 = e() && !z3;
        boolean z6 = J(0, i6, 0, scrollY, 0, scrollRange, 0, 0, true) && !y(i5);
        int scrollY2 = getScrollY() - scrollY;
        int[] iArr = this.f4699u;
        iArr[1] = 0;
        p(0, scrollY2, 0, i6 - scrollY2, this.f4698t, i5, iArr);
        int i8 = i7 + this.f4698t[1];
        int i9 = i6 - this.f4699u[1];
        int i10 = scrollY + i9;
        if (i10 < 0) {
            if (z5) {
                androidx.core.widget.d.d(this.f4684f, (-i9) / getHeight(), i4 / getWidth());
                if (!this.f4685g.isFinished()) {
                    this.f4685g.onRelease();
                }
            }
        } else if (i10 > scrollRange && z5) {
            androidx.core.widget.d.d(this.f4685g, i9 / getHeight(), 1.0f - (i4 / getWidth()));
            if (!this.f4684f.isFinished()) {
                this.f4684f.onRelease();
            }
        }
        if (this.f4684f.isFinished() && this.f4685g.isFinished()) {
            z4 = z6;
        } else {
            postInvalidateOnAnimation();
        }
        if (z4 && i5 == 0 && (velocityTracker = this.f4691m) != null) {
            velocityTracker.clear();
        }
        if (i5 == 1) {
            Z(i5);
            this.f4684f.onRelease();
            this.f4685g.onRelease();
        }
        return i8;
    }

    private void Q(View view) {
        view.getDrawingRect(this.f4682d);
        offsetDescendantRectToMyCoords(view, this.f4682d);
        int iH = h(this.f4682d);
        if (iH != 0) {
            scrollBy(0, iH);
        }
    }

    private boolean R(Rect rect, boolean z3) {
        int iH = h(rect);
        boolean z4 = iH != 0;
        if (z4) {
            if (z3) {
                scrollBy(0, iH);
            } else {
                T(0, iH);
            }
        }
        return z4;
    }

    private boolean S(EdgeEffect edgeEffect, int i3) {
        if (i3 > 0) {
            return true;
        }
        return x(-i3) < androidx.core.widget.d.b(edgeEffect) * ((float) getHeight());
    }

    private void U(int i3, int i4, int i5, boolean z3) {
        if (getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.f4681c > 250) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            int height = childAt.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            int height2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int scrollY = getScrollY();
            this.f4683e.startScroll(getScrollX(), scrollY, 0, Math.max(0, Math.min(i4 + scrollY, Math.max(0, height - height2))) - scrollY, i5);
            N(z3);
        } else {
            if (!this.f4683e.isFinished()) {
                a();
            }
            scrollBy(i3, i4);
        }
        this.f4681c = AnimationUtils.currentAnimationTimeMillis();
    }

    private boolean Y(MotionEvent motionEvent) {
        boolean z3;
        if (androidx.core.widget.d.b(this.f4684f) != 0.0f) {
            androidx.core.widget.d.d(this.f4684f, 0.0f, motionEvent.getX() / getWidth());
            z3 = true;
        } else {
            z3 = false;
        }
        if (androidx.core.widget.d.b(this.f4685g) == 0.0f) {
            return z3;
        }
        androidx.core.widget.d.d(this.f4685g, 0.0f, 1.0f - (motionEvent.getX() / getWidth()));
        return true;
    }

    private void a() {
        this.f4683e.abortAnimation();
        Z(1);
    }

    private boolean e() {
        int overScrollMode = getOverScrollMode();
        if (overScrollMode != 0) {
            return overScrollMode == 1 && getScrollRange() > 0;
        }
        return true;
    }

    private boolean f() {
        if (getChildCount() <= 0) {
            return false;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        return (childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin > (getHeight() - getPaddingTop()) - getPaddingBottom();
    }

    private static int g(int i3, int i4, int i5) {
        if (i4 >= i5 || i3 < 0) {
            return 0;
        }
        return i4 + i3 > i5 ? i5 - i4 : i3;
    }

    private void q(int i3) {
        if (i3 != 0) {
            if (this.f4693o) {
                T(0, i3);
            } else {
                scrollBy(0, i3);
            }
        }
    }

    private boolean r(int i3) {
        if (androidx.core.widget.d.b(this.f4684f) != 0.0f) {
            if (S(this.f4684f, i3)) {
                this.f4684f.onAbsorb(i3);
            } else {
                v(-i3);
            }
        } else {
            if (androidx.core.widget.d.b(this.f4685g) == 0.0f) {
                return false;
            }
            int i4 = -i3;
            if (S(this.f4685g, i4)) {
                this.f4685g.onAbsorb(i4);
            } else {
                v(i4);
            }
        }
        return true;
    }

    private void s() {
        this.f4697s = -1;
        this.f4690l = false;
        L();
        Z(0);
        this.f4684f.onRelease();
        this.f4685g.onRelease();
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.view.View u(boolean r13, int r14, int r15) {
        /*
            r12 = this;
            r0 = 2
            java.util.ArrayList r0 = r12.getFocusables(r0)
            int r1 = r0.size()
            r2 = 0
            r3 = 0
            r4 = r3
            r5 = r4
        Ld:
            if (r4 >= r1) goto L53
            java.lang.Object r6 = r0.get(r4)
            android.view.View r6 = (android.view.View) r6
            int r7 = r6.getTop()
            int r8 = r6.getBottom()
            if (r14 >= r8) goto L50
            if (r7 >= r15) goto L50
            r9 = 1
            if (r14 >= r7) goto L28
            if (r8 >= r15) goto L28
            r10 = r9
            goto L29
        L28:
            r10 = r3
        L29:
            if (r2 != 0) goto L2e
            r2 = r6
            r5 = r10
            goto L50
        L2e:
            if (r13 == 0) goto L36
            int r11 = r2.getTop()
            if (r7 < r11) goto L3e
        L36:
            if (r13 != 0) goto L40
            int r7 = r2.getBottom()
            if (r8 <= r7) goto L40
        L3e:
            r7 = r9
            goto L41
        L40:
            r7 = r3
        L41:
            if (r5 == 0) goto L48
            if (r10 == 0) goto L50
            if (r7 == 0) goto L50
            goto L4f
        L48:
            if (r10 == 0) goto L4d
            r2 = r6
            r5 = r9
            goto L50
        L4d:
            if (r7 == 0) goto L50
        L4f:
            r2 = r6
        L50:
            int r4 = r4 + 1
            goto Ld
        L53:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.NestedScrollView.u(boolean, int, int):android.view.View");
    }

    private float x(int i3) {
        double dLog = Math.log((Math.abs(i3) * 0.35f) / (this.f4680b * 0.015f));
        float f3 = f4673E;
        return (float) (((double) (this.f4680b * 0.015f)) * Math.exp((((double) f3) / (((double) f3) - 1.0d)) * dLog));
    }

    private boolean z(int i3, int i4) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = getScrollY();
        View childAt = getChildAt(0);
        return i4 >= childAt.getTop() - scrollY && i4 < childAt.getBottom() - scrollY && i3 >= childAt.getLeft() && i3 < childAt.getRight();
    }

    boolean J(int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z3) {
        boolean z4;
        boolean z5;
        int overScrollMode = getOverScrollMode();
        boolean z6 = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        boolean z7 = computeVerticalScrollRange() > computeVerticalScrollExtent();
        boolean z8 = overScrollMode == 0 || (overScrollMode == 1 && z6);
        boolean z9 = overScrollMode == 0 || (overScrollMode == 1 && z7);
        int i11 = i5 + i3;
        int i12 = !z8 ? 0 : i9;
        int i13 = i6 + i4;
        int i14 = !z9 ? 0 : i10;
        int i15 = -i12;
        int i16 = i12 + i7;
        int i17 = -i14;
        int i18 = i14 + i8;
        if (i11 > i16) {
            i11 = i16;
            z4 = true;
        } else if (i11 < i15) {
            z4 = true;
            i11 = i15;
        } else {
            z4 = false;
        }
        if (i13 > i18) {
            i13 = i18;
            z5 = true;
        } else if (i13 < i17) {
            z5 = true;
            i13 = i17;
        } else {
            z5 = false;
        }
        if (z5 && !y(1)) {
            this.f4683e.springBack(i11, i13, 0, 0, 0, getScrollRange());
        }
        onOverScrolled(i11, i13, z4, z5);
        return z4 || z5;
    }

    public boolean K(int i3) {
        boolean z3 = i3 == 130;
        int height = getHeight();
        if (z3) {
            this.f4682d.top = getScrollY() + height;
            int childCount = getChildCount();
            if (childCount > 0) {
                View childAt = getChildAt(childCount - 1);
                int bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin + getPaddingBottom();
                Rect rect = this.f4682d;
                if (rect.top + height > bottom) {
                    rect.top = bottom - height;
                }
            }
        } else {
            this.f4682d.top = getScrollY() - height;
            Rect rect2 = this.f4682d;
            if (rect2.top < 0) {
                rect2.top = 0;
            }
        }
        Rect rect3 = this.f4682d;
        int i4 = rect3.top;
        int i5 = height + i4;
        rect3.bottom = i5;
        return O(i3, i4, i5);
    }

    public final void T(int i3, int i4) {
        U(i3, i4, 250, false);
    }

    void V(int i3, int i4, int i5, boolean z3) {
        U(i3 - getScrollX(), i4 - getScrollY(), i5, z3);
    }

    void W(int i3, int i4, boolean z3) {
        V(i3, i4, 250, z3);
    }

    public boolean X(int i3, int i4) {
        return this.f4704z.p(i3, i4);
    }

    public void Z(int i3) {
        this.f4704z.r(i3);
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view);
    }

    @Override // androidx.core.view.F
    public void c(View view, View view2, int i3, int i4) {
        this.f4703y.c(view, view2, i3, i4);
        X(2, i4);
    }

    @Override // android.view.View
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @Override // android.view.View
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @Override // android.view.View
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.f4683e.isFinished()) {
            return;
        }
        this.f4683e.computeScrollOffset();
        int currY = this.f4683e.getCurrY();
        int iK = k(currY - this.f4701w);
        this.f4701w = currY;
        int[] iArr = this.f4699u;
        iArr[1] = 0;
        l(0, iK, iArr, null, 1);
        int i3 = iK - this.f4699u[1];
        int scrollRange = getScrollRange();
        if (i3 != 0) {
            int scrollY = getScrollY();
            J(0, i3, getScrollX(), scrollY, 0, scrollRange, 0, 0, false);
            int scrollY2 = getScrollY() - scrollY;
            int i4 = i3 - scrollY2;
            int[] iArr2 = this.f4699u;
            iArr2[1] = 0;
            p(0, scrollY2, 0, i4, this.f4698t, 1, iArr2);
            i3 = i4 - this.f4699u[1];
        }
        if (i3 != 0) {
            int overScrollMode = getOverScrollMode();
            if (overScrollMode == 0 || (overScrollMode == 1 && scrollRange > 0)) {
                if (i3 < 0) {
                    if (this.f4684f.isFinished()) {
                        this.f4684f.onAbsorb((int) this.f4683e.getCurrVelocity());
                    }
                } else if (this.f4685g.isFinished()) {
                    this.f4685g.onAbsorb((int) this.f4683e.getCurrVelocity());
                }
            }
            a();
        }
        if (this.f4683e.isFinished()) {
            Z(1);
        } else {
            postInvalidateOnAnimation();
        }
    }

    @Override // android.view.View
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override // android.view.View
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override // android.view.View
    public int computeVerticalScrollRange() {
        int childCount = getChildCount();
        int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (childCount == 0) {
            return height;
        }
        View childAt = getChildAt(0);
        int bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin;
        int scrollY = getScrollY();
        int iMax = Math.max(0, bottom - height);
        return scrollY < 0 ? bottom - scrollY : scrollY > iMax ? bottom + (scrollY - iMax) : bottom;
    }

    public boolean d(int i3) {
        View viewFindFocus = findFocus();
        if (viewFindFocus == this) {
            viewFindFocus = null;
        }
        View viewFindNextFocus = FocusFinder.getInstance().findNextFocus(this, viewFindFocus, i3);
        int maxScrollAmount = getMaxScrollAmount();
        if (viewFindNextFocus == null || !G(viewFindNextFocus, maxScrollAmount, getHeight())) {
            if (i3 == 33 && getScrollY() < maxScrollAmount) {
                maxScrollAmount = getScrollY();
            } else if (i3 == 130 && getChildCount() > 0) {
                View childAt = getChildAt(0);
                maxScrollAmount = Math.min((childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin) - ((getScrollY() + getHeight()) - getPaddingBottom()), maxScrollAmount);
            }
            if (maxScrollAmount == 0) {
                return false;
            }
            if (i3 != 130) {
                maxScrollAmount = -maxScrollAmount;
            }
            P(maxScrollAmount, 0, 1, true);
        } else {
            viewFindNextFocus.getDrawingRect(this.f4682d);
            offsetDescendantRectToMyCoords(viewFindNextFocus, this.f4682d);
            P(h(this.f4682d), 0, 1, true);
            viewFindNextFocus.requestFocus(i3);
        }
        if (viewFindFocus != null && viewFindFocus.isFocused() && E(viewFindFocus)) {
            int descendantFocusability = getDescendantFocusability();
            setDescendantFocusability(131072);
            requestFocus();
            setDescendantFocusability(descendantFocusability);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || t(keyEvent);
    }

    @Override // android.view.View
    public boolean dispatchNestedFling(float f3, float f4, boolean z3) {
        return this.f4704z.a(f3, f4, z3);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreFling(float f3, float f4) {
        return this.f4704z.b(f3, f4);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreScroll(int i3, int i4, int[] iArr, int[] iArr2) {
        return l(i3, i4, iArr, iArr2, 0);
    }

    @Override // android.view.View
    public boolean dispatchNestedScroll(int i3, int i4, int i5, int i6, int[] iArr) {
        return this.f4704z.f(i3, i4, i5, i6, iArr);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int paddingLeft;
        super.draw(canvas);
        int scrollY = getScrollY();
        int paddingLeft2 = 0;
        if (!this.f4684f.isFinished()) {
            int iSave = canvas.save();
            int width = getWidth();
            int height = getHeight();
            int iMin = Math.min(0, scrollY);
            if (b.a(this)) {
                width -= getPaddingLeft() + getPaddingRight();
                paddingLeft = getPaddingLeft();
            } else {
                paddingLeft = 0;
            }
            if (b.a(this)) {
                height -= getPaddingTop() + getPaddingBottom();
                iMin += getPaddingTop();
            }
            canvas.translate(paddingLeft, iMin);
            this.f4684f.setSize(width, height);
            if (this.f4684f.draw(canvas)) {
                postInvalidateOnAnimation();
            }
            canvas.restoreToCount(iSave);
        }
        if (this.f4685g.isFinished()) {
            return;
        }
        int iSave2 = canvas.save();
        int width2 = getWidth();
        int height2 = getHeight();
        int iMax = Math.max(getScrollRange(), scrollY) + height2;
        if (b.a(this)) {
            width2 -= getPaddingLeft() + getPaddingRight();
            paddingLeft2 = getPaddingLeft();
        }
        if (b.a(this)) {
            height2 -= getPaddingTop() + getPaddingBottom();
            iMax -= getPaddingBottom();
        }
        canvas.translate(paddingLeft2 - width2, iMax);
        canvas.rotate(180.0f, width2, 0.0f);
        this.f4685g.setSize(width2, height2);
        if (this.f4685g.draw(canvas)) {
            postInvalidateOnAnimation();
        }
        canvas.restoreToCount(iSave2);
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        int bottom = ((childAt.getBottom() + layoutParams.bottomMargin) - getScrollY()) - (getHeight() - getPaddingBottom());
        if (bottom < verticalFadingEdgeLength) {
            return bottom / verticalFadingEdgeLength;
        }
        return 1.0f;
    }

    public int getMaxScrollAmount() {
        return (int) (getHeight() * 0.5f);
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.f4703y.a();
    }

    int getScrollRange() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        return Math.max(0, ((childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin) - ((getHeight() - getPaddingTop()) - getPaddingBottom()));
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        int scrollY = getScrollY();
        if (scrollY < verticalFadingEdgeLength) {
            return scrollY / verticalFadingEdgeLength;
        }
        return 1.0f;
    }

    float getVerticalScrollFactorCompat() {
        if (this.f4676A == 0.0f) {
            TypedValue typedValue = new TypedValue();
            Context context = getContext();
            if (!context.getTheme().resolveAttribute(R.attr.listPreferredItemHeight, typedValue, true)) {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
            this.f4676A = typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return this.f4676A;
    }

    protected int h(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int scrollY = getScrollY();
        int i3 = scrollY + height;
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        if (rect.top > 0) {
            scrollY += verticalFadingEdgeLength;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        int i4 = rect.bottom < (childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin ? i3 - verticalFadingEdgeLength : i3;
        int i5 = rect.bottom;
        if (i5 > i4 && rect.top > scrollY) {
            return Math.min(rect.height() > height ? rect.top - scrollY : rect.bottom - i4, (childAt.getBottom() + layoutParams.bottomMargin) - i3);
        }
        if (rect.top >= scrollY || i5 >= i4) {
            return 0;
        }
        return Math.max(rect.height() > height ? 0 - (i4 - rect.bottom) : 0 - (scrollY - rect.top), -getScrollY());
    }

    @Override // android.view.View
    public boolean hasNestedScrollingParent() {
        return y(0);
    }

    @Override // androidx.core.view.F
    public void i(View view, int i3) {
        this.f4703y.e(view, i3);
        Z(i3);
    }

    @Override // android.view.View
    public boolean isNestedScrollingEnabled() {
        return this.f4704z.l();
    }

    @Override // androidx.core.view.F
    public void j(View view, int i3, int i4, int[] iArr, int i5) {
        l(i3, i4, iArr, null, i5);
    }

    int k(int i3) {
        int height = getHeight();
        if (i3 > 0 && androidx.core.widget.d.b(this.f4684f) != 0.0f) {
            int iRound = Math.round(((-height) / 4.0f) * androidx.core.widget.d.d(this.f4684f, ((-i3) * 4.0f) / height, 0.5f));
            if (iRound != i3) {
                this.f4684f.finish();
            }
            return i3 - iRound;
        }
        if (i3 >= 0 || androidx.core.widget.d.b(this.f4685g) == 0.0f) {
            return i3;
        }
        float f3 = height;
        int iRound2 = Math.round((f3 / 4.0f) * androidx.core.widget.d.d(this.f4685g, (i3 * 4.0f) / f3, 0.5f));
        if (iRound2 != i3) {
            this.f4685g.finish();
        }
        return i3 - iRound2;
    }

    public boolean l(int i3, int i4, int[] iArr, int[] iArr2, int i5) {
        return this.f4704z.d(i3, i4, iArr, iArr2, i5);
    }

    @Override // androidx.core.view.G
    public void m(View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        H(i6, i7, iArr);
    }

    @Override // android.view.ViewGroup
    protected void measureChild(View view, int i3, int i4) {
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight(), view.getLayoutParams().width), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View view, int i3, int i4, int i5, int i6) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i4, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, 0));
    }

    @Override // androidx.core.view.F
    public void n(View view, int i3, int i4, int i5, int i6, int i7) {
        H(i6, i7, null);
    }

    @Override // androidx.core.view.F
    public boolean o(View view, View view2, int i3, int i4) {
        return (i3 & 2) != 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f4688j = false;
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        int i3;
        int width;
        float axisValue;
        if (motionEvent.getAction() == 8 && !this.f4690l) {
            if (D.a(motionEvent, 2)) {
                i3 = 9;
                axisValue = motionEvent.getAxisValue(9);
                width = (int) motionEvent.getX();
            } else if (D.a(motionEvent, 4194304)) {
                float axisValue2 = motionEvent.getAxisValue(26);
                width = getWidth() / 2;
                i3 = 26;
                axisValue = axisValue2;
            } else {
                i3 = 0;
                width = 0;
                axisValue = 0.0f;
            }
            if (axisValue != 0.0f) {
                P(-((int) (axisValue * getVerticalScrollFactorCompat())), width, 1, D.a(motionEvent, 8194));
                if (i3 != 0) {
                    this.f4679D.g(motionEvent, i3);
                }
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x007e  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r12) {
        /*
            Method dump skipped, instruction units count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.NestedScrollView.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        super.onLayout(z3, i3, i4, i5, i6);
        int measuredHeight = 0;
        this.f4687i = false;
        View view = this.f4689k;
        if (view != null && F(view, this)) {
            Q(this.f4689k);
        }
        this.f4689k = null;
        if (!this.f4688j) {
            if (this.f4702x != null) {
                scrollTo(getScrollX(), this.f4702x.f4706a);
                this.f4702x = null;
            }
            if (getChildCount() > 0) {
                View childAt = getChildAt(0);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                measuredHeight = childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
            int paddingTop = ((i6 - i4) - getPaddingTop()) - getPaddingBottom();
            int scrollY = getScrollY();
            int iG = g(scrollY, paddingTop, measuredHeight);
            if (iG != scrollY) {
                scrollTo(getScrollX(), iG);
            }
        }
        scrollTo(getScrollX(), getScrollY());
        this.f4688j = true;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (this.f4692n && View.MeasureSpec.getMode(i4) != 0 && getChildCount() > 0) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight();
            int measuredHeight2 = (((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) - layoutParams.topMargin) - layoutParams.bottomMargin;
            if (measuredHeight < measuredHeight2) {
                childAt.measure(ViewGroup.getChildMeasureSpec(i3, getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), View.MeasureSpec.makeMeasureSpec(measuredHeight2, 1073741824));
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View view, float f3, float f4, boolean z3) {
        if (z3) {
            return false;
        }
        dispatchNestedFling(0.0f, f4, true);
        v((int) f4);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPreFling(View view, float f3, float f4) {
        return dispatchNestedPreFling(f3, f4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedPreScroll(View view, int i3, int i4, int[] iArr) {
        j(view, i3, i4, iArr, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScroll(View view, int i3, int i4, int i5, int i6) {
        H(i6, 0, null);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScrollAccepted(View view, View view2, int i3) {
        c(view, view2, i3, 0);
    }

    @Override // android.view.View
    protected void onOverScrolled(int i3, int i4, boolean z3, boolean z4) {
        super.scrollTo(i3, i4);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i3, Rect rect) {
        if (i3 == 2) {
            i3 = 130;
        } else if (i3 == 1) {
            i3 = 33;
        }
        View viewFindNextFocus = rect == null ? FocusFinder.getInstance().findNextFocus(this, null, i3) : FocusFinder.getInstance().findNextFocusFromRect(this, rect, i3);
        if (viewFindNextFocus == null || E(viewFindNextFocus)) {
            return false;
        }
        return viewFindNextFocus.requestFocus(i3, rect);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof e)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        e eVar = (e) parcelable;
        super.onRestoreInstanceState(eVar.getSuperState());
        this.f4702x = eVar;
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        e eVar = new e(super.onSaveInstanceState());
        eVar.f4706a = getScrollY();
        return eVar;
    }

    @Override // android.view.View
    protected void onScrollChanged(int i3, int i4, int i5, int i6) {
        super.onScrollChanged(i3, i4, i5, i6);
        d dVar = this.f4677B;
        if (dVar != null) {
            dVar.a(this, i3, i4, i5, i6);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        View viewFindFocus = findFocus();
        if (viewFindFocus == null || this == viewFindFocus || !G(viewFindFocus, 0, i6)) {
            return;
        }
        viewFindFocus.getDrawingRect(this.f4682d);
        offsetDescendantRectToMyCoords(viewFindFocus, this.f4682d);
        q(h(this.f4682d));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onStartNestedScroll(View view, View view2, int i3) {
        return o(view, view2, i3, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onStopNestedScroll(View view) {
        i(view, 0);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ViewParent parent;
        C();
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f4700v = 0;
        }
        MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
        motionEventObtain.offsetLocation(0.0f, this.f4700v);
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                VelocityTracker velocityTracker = this.f4691m;
                velocityTracker.computeCurrentVelocity(1000, this.f4696r);
                int yVelocity = (int) velocityTracker.getYVelocity(this.f4697s);
                if (Math.abs(yVelocity) >= this.f4695q) {
                    if (!r(yVelocity)) {
                        int i3 = -yVelocity;
                        float f3 = i3;
                        if (!dispatchNestedPreFling(0.0f, f3)) {
                            dispatchNestedFling(0.0f, f3, true);
                            v(i3);
                        }
                    }
                } else if (this.f4683e.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    postInvalidateOnAnimation();
                }
                s();
            } else if (actionMasked == 2) {
                int iFindPointerIndex = motionEvent.findPointerIndex(this.f4697s);
                if (iFindPointerIndex == -1) {
                    Log.e("NestedScrollView", "Invalid pointerId=" + this.f4697s + " in onTouchEvent");
                } else {
                    int y3 = (int) motionEvent.getY(iFindPointerIndex);
                    int i4 = this.f4686h - y3;
                    int iM = i4 - M(i4, motionEvent.getX(iFindPointerIndex));
                    if (!this.f4690l && Math.abs(iM) > this.f4694p) {
                        ViewParent parent2 = getParent();
                        if (parent2 != null) {
                            parent2.requestDisallowInterceptTouchEvent(true);
                        }
                        this.f4690l = true;
                        iM = iM > 0 ? iM - this.f4694p : iM + this.f4694p;
                    }
                    if (this.f4690l) {
                        int iP = P(iM, (int) motionEvent.getX(iFindPointerIndex), 0, false);
                        this.f4686h = y3 - iP;
                        this.f4700v += iP;
                    }
                }
            } else if (actionMasked == 3) {
                if (this.f4690l && getChildCount() > 0 && this.f4683e.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    postInvalidateOnAnimation();
                }
                s();
            } else if (actionMasked == 5) {
                int actionIndex = motionEvent.getActionIndex();
                this.f4686h = (int) motionEvent.getY(actionIndex);
                this.f4697s = motionEvent.getPointerId(actionIndex);
            } else if (actionMasked == 6) {
                I(motionEvent);
                this.f4686h = (int) motionEvent.getY(motionEvent.findPointerIndex(this.f4697s));
            }
        } else {
            if (getChildCount() == 0) {
                return false;
            }
            if (this.f4690l && (parent = getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            if (!this.f4683e.isFinished()) {
                a();
            }
            D((int) motionEvent.getY(), motionEvent.getPointerId(0));
        }
        VelocityTracker velocityTracker2 = this.f4691m;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(motionEventObtain);
        }
        motionEventObtain.recycle();
        return true;
    }

    public void p(int i3, int i4, int i5, int i6, int[] iArr, int i7, int[] iArr2) {
        this.f4704z.e(i3, i4, i5, i6, iArr, i7, iArr2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (this.f4687i) {
            this.f4689k = view2;
        } else {
            Q(view2);
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z3) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return R(rect, z3);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z3) {
        if (z3) {
            L();
        }
        super.requestDisallowInterceptTouchEvent(z3);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.f4687i = true;
        super.requestLayout();
    }

    @Override // android.view.View
    public void scrollTo(int i3, int i4) {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
            int width2 = childAt.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int height2 = childAt.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            int iG = g(i3, width, width2);
            int iG2 = g(i4, height, height2);
            if (iG == getScrollX() && iG2 == getScrollY()) {
                return;
            }
            super.scrollTo(iG, iG2);
        }
    }

    public void setFillViewport(boolean z3) {
        if (z3 != this.f4692n) {
            this.f4692n = z3;
            requestLayout();
        }
    }

    @Override // android.view.View
    public void setNestedScrollingEnabled(boolean z3) {
        this.f4704z.m(z3);
    }

    public void setOnScrollChangeListener(d dVar) {
    }

    public void setSmoothScrollingEnabled(boolean z3) {
        this.f4693o = z3;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override // android.view.View
    public boolean startNestedScroll(int i3) {
        return X(i3, 0);
    }

    @Override // android.view.View
    public void stopNestedScroll() {
        Z(0);
    }

    public boolean t(KeyEvent keyEvent) {
        this.f4682d.setEmpty();
        if (!f()) {
            if (!isFocused() || keyEvent.getKeyCode() == 4) {
                return false;
            }
            View viewFindFocus = findFocus();
            if (viewFindFocus == this) {
                viewFindFocus = null;
            }
            View viewFindNextFocus = FocusFinder.getInstance().findNextFocus(this, viewFindFocus, 130);
            return (viewFindNextFocus == null || viewFindNextFocus == this || !viewFindNextFocus.requestFocus(130)) ? false : true;
        }
        if (keyEvent.getAction() != 0) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 19) {
            return keyEvent.isAltPressed() ? w(33) : d(33);
        }
        if (keyCode == 20) {
            return keyEvent.isAltPressed() ? w(130) : d(130);
        }
        if (keyCode == 62) {
            K(keyEvent.isShiftPressed() ? 33 : 130);
            return false;
        }
        if (keyCode == 92) {
            return w(33);
        }
        if (keyCode == 93) {
            return w(130);
        }
        if (keyCode == 122) {
            K(33);
            return false;
        }
        if (keyCode != 123) {
            return false;
        }
        K(130);
        return false;
    }

    public void v(int i3) {
        if (getChildCount() > 0) {
            this.f4683e.fling(getScrollX(), getScrollY(), 0, i3, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            N(true);
        }
    }

    public boolean w(int i3) {
        int childCount;
        boolean z3 = i3 == 130;
        int height = getHeight();
        Rect rect = this.f4682d;
        rect.top = 0;
        rect.bottom = height;
        if (z3 && (childCount = getChildCount()) > 0) {
            View childAt = getChildAt(childCount - 1);
            this.f4682d.bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin + getPaddingBottom();
            Rect rect2 = this.f4682d;
            rect2.top = rect2.bottom - height;
        }
        Rect rect3 = this.f4682d;
        return O(i3, rect3.top, rect3.bottom);
    }

    public boolean y(int i3) {
        return this.f4704z.k(i3);
    }

    public NestedScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0595a.f9722c);
    }

    public NestedScrollView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f4682d = new Rect();
        this.f4687i = true;
        this.f4688j = false;
        this.f4689k = null;
        this.f4690l = false;
        this.f4693o = true;
        this.f4697s = -1;
        this.f4698t = new int[2];
        this.f4699u = new int[2];
        c cVar = new c();
        this.f4678C = cVar;
        this.f4679D = new C0271s(getContext(), cVar);
        this.f4684f = androidx.core.widget.d.a(context, attributeSet);
        this.f4685g = androidx.core.widget.d.a(context, attributeSet);
        this.f4680b = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        B();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f4675G, i3, 0);
        setFillViewport(typedArrayObtainStyledAttributes.getBoolean(0, false));
        typedArrayObtainStyledAttributes.recycle();
        this.f4703y = new H(this);
        this.f4704z = new E(this);
        setNestedScrollingEnabled(true);
        Z.X(this, f4674F);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i3) {
        if (getChildCount() <= 0) {
            super.addView(view, i3);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (getChildCount() <= 0) {
            super.addView(view, layoutParams);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i3, ViewGroup.LayoutParams layoutParams) {
        if (getChildCount() <= 0) {
            super.addView(view, i3, layoutParams);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }
}
