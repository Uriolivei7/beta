package androidx.appcompat.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.OverScroller;
import androidx.appcompat.view.menu.j;
import androidx.core.view.C0264n0;
import d.AbstractC0487a;

/* JADX INFO: loaded from: classes.dex */
public class ActionBarOverlayLayout extends ViewGroup implements I, androidx.core.view.F, androidx.core.view.G {

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    static final int[] f3682H = {AbstractC0487a.f8674b, R.attr.windowContentOverlay};

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private static final C0264n0 f3683I = new C0264n0.b().c(androidx.core.graphics.b.b(0, 1, 0, 1)).a();

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private static final Rect f3684J = new Rect();

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private OverScroller f3685A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    ViewPropertyAnimator f3686B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    final AnimatorListenerAdapter f3687C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final Runnable f3688D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private final Runnable f3689E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private final androidx.core.view.H f3690F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private final f f3691G;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f3692b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f3693c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private ContentFrameLayout f3694d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    ActionBarContainer f3695e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private J f3696f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Drawable f3697g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f3698h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f3699i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f3700j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    boolean f3701k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f3702l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f3703m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final Rect f3704n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final Rect f3705o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final Rect f3706p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final Rect f3707q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final Rect f3708r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final Rect f3709s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final Rect f3710t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final Rect f3711u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private C0264n0 f3712v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private C0264n0 f3713w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private C0264n0 f3714x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private C0264n0 f3715y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private d f3716z;

    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.f3686B = null;
            actionBarOverlayLayout.f3701k = false;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.f3686B = null;
            actionBarOverlayLayout.f3701k = false;
        }
    }

    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionBarOverlayLayout.this.v();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.f3686B = actionBarOverlayLayout.f3695e.animate().translationY(0.0f).setListener(ActionBarOverlayLayout.this.f3687C);
        }
    }

    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionBarOverlayLayout.this.v();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.f3686B = actionBarOverlayLayout.f3695e.animate().translationY(-ActionBarOverlayLayout.this.f3695e.getHeight()).setListener(ActionBarOverlayLayout.this.f3687C);
        }
    }

    public interface d {
        void a();

        void b();

        void c(int i3);

        void d();

        void e(boolean z3);

        void f();
    }

    public static class e extends ViewGroup.MarginLayoutParams {
        public e(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public e(int i3, int i4) {
            super(i3, i4);
        }

        public e(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public e(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    private static final class f extends View {
        f(Context context) {
            super(context);
            setWillNotDraw(true);
        }

        @Override // android.view.View
        public int getWindowSystemUiVisibility() {
            return 0;
        }
    }

    public ActionBarOverlayLayout(Context context) {
        this(context, null);
    }

    private void B() {
        v();
        this.f3688D.run();
    }

    private boolean C(float f3) {
        this.f3685A.fling(0, 0, 0, (int) f3, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return this.f3685A.getFinalY() > this.f3695e.getHeight();
    }

    private void p() {
        v();
        this.f3689E.run();
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean q(android.view.View r3, android.graphics.Rect r4, boolean r5, boolean r6, boolean r7, boolean r8) {
        /*
            r2 = this;
            android.view.ViewGroup$LayoutParams r3 = r3.getLayoutParams()
            androidx.appcompat.widget.ActionBarOverlayLayout$e r3 = (androidx.appcompat.widget.ActionBarOverlayLayout.e) r3
            r0 = 1
            if (r5 == 0) goto L13
            int r5 = r3.leftMargin
            int r1 = r4.left
            if (r5 == r1) goto L13
            r3.leftMargin = r1
            r5 = r0
            goto L14
        L13:
            r5 = 0
        L14:
            if (r6 == 0) goto L1f
            int r6 = r3.topMargin
            int r1 = r4.top
            if (r6 == r1) goto L1f
            r3.topMargin = r1
            r5 = r0
        L1f:
            if (r8 == 0) goto L2a
            int r6 = r3.rightMargin
            int r8 = r4.right
            if (r6 == r8) goto L2a
            r3.rightMargin = r8
            r5 = r0
        L2a:
            if (r7 == 0) goto L35
            int r6 = r3.bottomMargin
            int r4 = r4.bottom
            if (r6 == r4) goto L35
            r3.bottomMargin = r4
            goto L36
        L35:
            r0 = r5
        L36:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ActionBarOverlayLayout.q(android.view.View, android.graphics.Rect, boolean, boolean, boolean, boolean):boolean");
    }

    private boolean r() {
        androidx.core.view.Z.d(this.f3691G, f3683I, this.f3707q);
        return !this.f3707q.equals(f3684J);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private J u(View view) {
        if (view instanceof J) {
            return (J) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        throw new IllegalStateException("Can't make a decor toolbar out of " + view.getClass().getSimpleName());
    }

    private void w(Context context) {
        TypedArray typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(f3682H);
        this.f3692b = typedArrayObtainStyledAttributes.getDimensionPixelSize(0, 0);
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(1);
        this.f3697g = drawable;
        setWillNotDraw(drawable == null);
        typedArrayObtainStyledAttributes.recycle();
        this.f3685A = new OverScroller(context);
    }

    private void y() {
        v();
        postDelayed(this.f3689E, 600L);
    }

    private void z() {
        v();
        postDelayed(this.f3688D, 600L);
    }

    void A() {
        if (this.f3694d == null) {
            this.f3694d = (ContentFrameLayout) findViewById(d.f.f8785b);
            this.f3695e = (ActionBarContainer) findViewById(d.f.f8786c);
            this.f3696f = u(findViewById(d.f.f8784a));
        }
    }

    @Override // androidx.appcompat.widget.I
    public void a(Menu menu, j.a aVar) {
        A();
        this.f3696f.a(menu, aVar);
    }

    @Override // androidx.appcompat.widget.I
    public boolean b() {
        A();
        return this.f3696f.b();
    }

    @Override // androidx.core.view.F
    public void c(View view, View view2, int i3, int i4) {
        if (i4 == 0) {
            onNestedScrollAccepted(view, view2, i3);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof e;
    }

    @Override // androidx.appcompat.widget.I
    public void d() {
        A();
        this.f3696f.d();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.f3697g != null) {
            int bottom = this.f3695e.getVisibility() == 0 ? (int) (this.f3695e.getBottom() + this.f3695e.getTranslationY() + 0.5f) : 0;
            this.f3697g.setBounds(0, bottom, getWidth(), this.f3697g.getIntrinsicHeight() + bottom);
            this.f3697g.draw(canvas);
        }
    }

    @Override // androidx.appcompat.widget.I
    public boolean e() {
        A();
        return this.f3696f.e();
    }

    @Override // androidx.appcompat.widget.I
    public boolean f() {
        A();
        return this.f3696f.f();
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        return super.fitSystemWindows(rect);
    }

    @Override // androidx.appcompat.widget.I
    public boolean g() {
        A();
        return this.f3696f.g();
    }

    public int getActionBarHideOffset() {
        ActionBarContainer actionBarContainer = this.f3695e;
        if (actionBarContainer != null) {
            return -((int) actionBarContainer.getTranslationY());
        }
        return 0;
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.f3690F.a();
    }

    public CharSequence getTitle() {
        A();
        return this.f3696f.getTitle();
    }

    @Override // androidx.appcompat.widget.I
    public boolean h() {
        A();
        return this.f3696f.h();
    }

    @Override // androidx.core.view.F
    public void i(View view, int i3) {
        if (i3 == 0) {
            onStopNestedScroll(view);
        }
    }

    @Override // androidx.core.view.F
    public void j(View view, int i3, int i4, int[] iArr, int i5) {
        if (i5 == 0) {
            onNestedPreScroll(view, i3, i4, iArr);
        }
    }

    @Override // androidx.appcompat.widget.I
    public void k(int i3) {
        A();
        if (i3 == 2) {
            this.f3696f.s();
        } else if (i3 == 5) {
            this.f3696f.t();
        } else {
            if (i3 != 109) {
                return;
            }
            setOverlayMode(true);
        }
    }

    @Override // androidx.appcompat.widget.I
    public void l() {
        A();
        this.f3696f.i();
    }

    @Override // androidx.core.view.G
    public void m(View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        n(view, i3, i4, i5, i6, i7);
    }

    @Override // androidx.core.view.F
    public void n(View view, int i3, int i4, int i5, int i6, int i7) {
        if (i7 == 0) {
            onNestedScroll(view, i3, i4, i5, i6);
        }
    }

    @Override // androidx.core.view.F
    public boolean o(View view, View view2, int i3, int i4) {
        return i4 == 0 && onStartNestedScroll(view, view2, i3);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        A();
        C0264n0 c0264n0W = C0264n0.w(windowInsets, this);
        boolean zQ = q(this.f3695e, new Rect(c0264n0W.i(), c0264n0W.k(), c0264n0W.j(), c0264n0W.h()), true, true, false, true);
        androidx.core.view.Z.d(this, c0264n0W, this.f3704n);
        Rect rect = this.f3704n;
        C0264n0 c0264n0L = c0264n0W.l(rect.left, rect.top, rect.right, rect.bottom);
        this.f3712v = c0264n0L;
        boolean z3 = true;
        if (!this.f3713w.equals(c0264n0L)) {
            this.f3713w = this.f3712v;
            zQ = true;
        }
        if (this.f3705o.equals(this.f3704n)) {
            z3 = zQ;
        } else {
            this.f3705o.set(this.f3704n);
        }
        if (z3) {
            requestLayout();
        }
        return c0264n0W.a().c().b().u();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        w(getContext());
        androidx.core.view.Z.U(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        v();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i8 = ((ViewGroup.MarginLayoutParams) eVar).leftMargin + paddingLeft;
                int i9 = ((ViewGroup.MarginLayoutParams) eVar).topMargin + paddingTop;
                childAt.layout(i8, i9, measuredWidth + i8, measuredHeight + i9);
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i3, int i4) {
        int measuredHeight;
        A();
        measureChildWithMargins(this.f3695e, i3, 0, i4, 0);
        e eVar = (e) this.f3695e.getLayoutParams();
        int iMax = Math.max(0, this.f3695e.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin + ((ViewGroup.MarginLayoutParams) eVar).rightMargin);
        int iMax2 = Math.max(0, this.f3695e.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) eVar).topMargin + ((ViewGroup.MarginLayoutParams) eVar).bottomMargin);
        int iCombineMeasuredStates = View.combineMeasuredStates(0, this.f3695e.getMeasuredState());
        boolean z3 = (androidx.core.view.Z.B(this) & 256) != 0;
        if (z3) {
            measuredHeight = this.f3692b;
            if (this.f3699i && this.f3695e.getTabContainer() != null) {
                measuredHeight += this.f3692b;
            }
        } else {
            measuredHeight = this.f3695e.getVisibility() != 8 ? this.f3695e.getMeasuredHeight() : 0;
        }
        this.f3706p.set(this.f3704n);
        this.f3714x = this.f3712v;
        if (this.f3698h || z3 || !r()) {
            this.f3714x = new C0264n0.b(this.f3714x).c(androidx.core.graphics.b.b(this.f3714x.i(), this.f3714x.k() + measuredHeight, this.f3714x.j(), this.f3714x.h())).a();
        } else {
            Rect rect = this.f3706p;
            rect.top += measuredHeight;
            rect.bottom = rect.bottom;
            this.f3714x = this.f3714x.l(0, measuredHeight, 0, 0);
        }
        q(this.f3694d, this.f3706p, true, true, true, true);
        if (!this.f3715y.equals(this.f3714x)) {
            C0264n0 c0264n0 = this.f3714x;
            this.f3715y = c0264n0;
            androidx.core.view.Z.e(this.f3694d, c0264n0);
        }
        measureChildWithMargins(this.f3694d, i3, 0, i4, 0);
        e eVar2 = (e) this.f3694d.getLayoutParams();
        int iMax3 = Math.max(iMax, this.f3694d.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) eVar2).leftMargin + ((ViewGroup.MarginLayoutParams) eVar2).rightMargin);
        int iMax4 = Math.max(iMax2, this.f3694d.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) eVar2).topMargin + ((ViewGroup.MarginLayoutParams) eVar2).bottomMargin);
        int iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates, this.f3694d.getMeasuredState());
        setMeasuredDimension(View.resolveSizeAndState(Math.max(iMax3 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i3, iCombineMeasuredStates2), View.resolveSizeAndState(Math.max(iMax4 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i4, iCombineMeasuredStates2 << 16));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View view, float f3, float f4, boolean z3) {
        if (!this.f3700j || !z3) {
            return false;
        }
        if (C(f4)) {
            p();
        } else {
            B();
        }
        this.f3701k = true;
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPreFling(View view, float f3, float f4) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedPreScroll(View view, int i3, int i4, int[] iArr) {
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScroll(View view, int i3, int i4, int i5, int i6) {
        int i7 = this.f3702l + i4;
        this.f3702l = i7;
        setActionBarHideOffset(i7);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScrollAccepted(View view, View view2, int i3) {
        this.f3690F.b(view, view2, i3);
        this.f3702l = getActionBarHideOffset();
        v();
        d dVar = this.f3716z;
        if (dVar != null) {
            dVar.b();
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onStartNestedScroll(View view, View view2, int i3) {
        if ((i3 & 2) == 0 || this.f3695e.getVisibility() != 0) {
            return false;
        }
        return this.f3700j;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onStopNestedScroll(View view) {
        if (this.f3700j && !this.f3701k) {
            if (this.f3702l <= this.f3695e.getHeight()) {
                z();
            } else {
                y();
            }
        }
        d dVar = this.f3716z;
        if (dVar != null) {
            dVar.d();
        }
    }

    @Override // android.view.View
    public void onWindowSystemUiVisibilityChanged(int i3) {
        super.onWindowSystemUiVisibilityChanged(i3);
        A();
        int i4 = this.f3703m ^ i3;
        this.f3703m = i3;
        boolean z3 = (i3 & 4) == 0;
        boolean z4 = (i3 & 256) != 0;
        d dVar = this.f3716z;
        if (dVar != null) {
            dVar.e(!z4);
            if (z3 || !z4) {
                this.f3716z.a();
            } else {
                this.f3716z.f();
            }
        }
        if ((i4 & 256) == 0 || this.f3716z == null) {
            return;
        }
        androidx.core.view.Z.U(this);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i3) {
        super.onWindowVisibilityChanged(i3);
        this.f3693c = i3;
        d dVar = this.f3716z;
        if (dVar != null) {
            dVar.c(i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: s, reason: merged with bridge method [inline-methods] */
    public e generateDefaultLayoutParams() {
        return new e(-1, -1);
    }

    public void setActionBarHideOffset(int i3) {
        v();
        this.f3695e.setTranslationY(-Math.max(0, Math.min(i3, this.f3695e.getHeight())));
    }

    public void setActionBarVisibilityCallback(d dVar) {
        this.f3716z = dVar;
        if (getWindowToken() != null) {
            this.f3716z.c(this.f3693c);
            int i3 = this.f3703m;
            if (i3 != 0) {
                onWindowSystemUiVisibilityChanged(i3);
                androidx.core.view.Z.U(this);
            }
        }
    }

    public void setHasNonEmbeddedTabs(boolean z3) {
        this.f3699i = z3;
    }

    public void setHideOnContentScrollEnabled(boolean z3) {
        if (z3 != this.f3700j) {
            this.f3700j = z3;
            if (z3) {
                return;
            }
            v();
            setActionBarHideOffset(0);
        }
    }

    public void setIcon(int i3) {
        A();
        this.f3696f.setIcon(i3);
    }

    public void setLogo(int i3) {
        A();
        this.f3696f.p(i3);
    }

    public void setOverlayMode(boolean z3) {
        this.f3698h = z3;
    }

    public void setShowingForActionMode(boolean z3) {
    }

    public void setUiOptions(int i3) {
    }

    @Override // androidx.appcompat.widget.I
    public void setWindowCallback(Window.Callback callback) {
        A();
        this.f3696f.setWindowCallback(callback);
    }

    @Override // androidx.appcompat.widget.I
    public void setWindowTitle(CharSequence charSequence) {
        A();
        this.f3696f.setWindowTitle(charSequence);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override // android.view.ViewGroup
    /* JADX INFO: renamed from: t, reason: merged with bridge method [inline-methods] */
    public e generateLayoutParams(AttributeSet attributeSet) {
        return new e(getContext(), attributeSet);
    }

    void v() {
        removeCallbacks(this.f3688D);
        removeCallbacks(this.f3689E);
        ViewPropertyAnimator viewPropertyAnimator = this.f3686B;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
    }

    public boolean x() {
        return this.f3698h;
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3693c = 0;
        this.f3704n = new Rect();
        this.f3705o = new Rect();
        this.f3706p = new Rect();
        this.f3707q = new Rect();
        this.f3708r = new Rect();
        this.f3709s = new Rect();
        this.f3710t = new Rect();
        this.f3711u = new Rect();
        C0264n0 c0264n0 = C0264n0.f4625b;
        this.f3712v = c0264n0;
        this.f3713w = c0264n0;
        this.f3714x = c0264n0;
        this.f3715y = c0264n0;
        this.f3687C = new a();
        this.f3688D = new b();
        this.f3689E = new c();
        w(context);
        this.f3690F = new androidx.core.view.H(this);
        f fVar = new f(context);
        this.f3691G = fVar;
        addView(fVar);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new e(layoutParams);
    }

    public void setIcon(Drawable drawable) {
        A();
        this.f3696f.setIcon(drawable);
    }
}
