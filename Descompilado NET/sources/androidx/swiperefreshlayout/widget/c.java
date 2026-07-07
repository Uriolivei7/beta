package androidx.swiperefreshlayout.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ListView;
import androidx.core.view.E;
import androidx.core.view.F;
import androidx.core.view.G;
import androidx.core.view.H;
import androidx.core.view.Z;

/* JADX INFO: loaded from: classes.dex */
public class c extends ViewGroup implements G, F {

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private static final String f5501Q = "c";

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private static final int[] f5502R = {R.attr.enabled};

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    protected int f5503A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    int f5504B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    int f5505C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    androidx.swiperefreshlayout.widget.b f5506D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private Animation f5507E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private Animation f5508F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private Animation f5509G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private Animation f5510H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private Animation f5511I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    boolean f5512J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private int f5513K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    boolean f5514L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private boolean f5515M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private Animation.AnimationListener f5516N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private final Animation f5517O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private final Animation f5518P;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private View f5519b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    j f5520c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    boolean f5521d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f5522e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private float f5523f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private float f5524g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final H f5525h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final E f5526i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final int[] f5527j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final int[] f5528k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final int[] f5529l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f5530m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f5531n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    int f5532o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private float f5533p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private float f5534q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f5535r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f5536s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    boolean f5537t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private boolean f5538u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private final DecelerateInterpolator f5539v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    androidx.swiperefreshlayout.widget.a f5540w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private int f5541x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    protected int f5542y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    float f5543z;

    class a implements Animation.AnimationListener {
        a() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            j jVar;
            c cVar = c.this;
            if (!cVar.f5521d) {
                cVar.r();
                return;
            }
            cVar.f5506D.setAlpha(255);
            c.this.f5506D.start();
            c cVar2 = c.this;
            if (cVar2.f5512J && (jVar = cVar2.f5520c) != null) {
                jVar.a();
            }
            c cVar3 = c.this;
            cVar3.f5532o = cVar3.f5540w.getTop();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    class b extends Animation {
        b() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f3, Transformation transformation) {
            c.this.setAnimationProgress(f3);
        }
    }

    /* JADX INFO: renamed from: androidx.swiperefreshlayout.widget.c$c, reason: collision with other inner class name */
    class C0086c extends Animation {
        C0086c() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f3, Transformation transformation) {
            c.this.setAnimationProgress(1.0f - f3);
        }
    }

    class d extends Animation {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f5547b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f5548c;

        d(int i3, int i4) {
            this.f5547b = i3;
            this.f5548c = i4;
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f3, Transformation transformation) {
            c.this.f5506D.setAlpha((int) (this.f5547b + ((this.f5548c - r0) * f3)));
        }
    }

    class e implements Animation.AnimationListener {
        e() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            c cVar = c.this;
            if (cVar.f5537t) {
                return;
            }
            cVar.y(null);
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    class f extends Animation {
        f() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f3, Transformation transformation) {
            c cVar = c.this;
            int iAbs = !cVar.f5514L ? cVar.f5504B - Math.abs(cVar.f5503A) : cVar.f5504B;
            c cVar2 = c.this;
            c.this.setTargetOffsetTopAndBottom((cVar2.f5542y + ((int) ((iAbs - r1) * f3))) - cVar2.f5540w.getTop());
            c.this.f5506D.e(1.0f - f3);
        }
    }

    class g extends Animation {
        g() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f3, Transformation transformation) {
            c.this.p(f3);
        }
    }

    class h extends Animation {
        h() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f3, Transformation transformation) {
            c cVar = c.this;
            float f4 = cVar.f5543z;
            cVar.setAnimationProgress(f4 + ((-f4) * f3));
            c.this.p(f3);
        }
    }

    public interface i {
    }

    public interface j {
        void a();
    }

    public c(Context context) {
        this(context, null);
    }

    private void A(Animation.AnimationListener animationListener) {
        this.f5540w.setVisibility(0);
        this.f5506D.setAlpha(255);
        b bVar = new b();
        this.f5507E = bVar;
        bVar.setDuration(this.f5531n);
        if (animationListener != null) {
            this.f5540w.b(animationListener);
        }
        this.f5540w.clearAnimation();
        this.f5540w.startAnimation(this.f5507E);
    }

    private void a(int i3, Animation.AnimationListener animationListener) {
        this.f5542y = i3;
        this.f5517O.reset();
        this.f5517O.setDuration(200L);
        this.f5517O.setInterpolator(this.f5539v);
        if (animationListener != null) {
            this.f5540w.b(animationListener);
        }
        this.f5540w.clearAnimation();
        this.f5540w.startAnimation(this.f5517O);
    }

    private void b(int i3, Animation.AnimationListener animationListener) {
        if (this.f5537t) {
            z(i3, animationListener);
            return;
        }
        this.f5542y = i3;
        this.f5518P.reset();
        this.f5518P.setDuration(200L);
        this.f5518P.setInterpolator(this.f5539v);
        if (animationListener != null) {
            this.f5540w.b(animationListener);
        }
        this.f5540w.clearAnimation();
        this.f5540w.startAnimation(this.f5518P);
    }

    private void e() {
        this.f5540w = new androidx.swiperefreshlayout.widget.a(getContext());
        androidx.swiperefreshlayout.widget.b bVar = new androidx.swiperefreshlayout.widget.b(getContext());
        this.f5506D = bVar;
        bVar.l(1);
        this.f5540w.setImageDrawable(this.f5506D);
        this.f5540w.setVisibility(8);
        addView(this.f5540w);
    }

    private void g() {
        if (this.f5519b == null) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (!childAt.equals(this.f5540w)) {
                    this.f5519b = childAt;
                    return;
                }
            }
        }
    }

    private void h(float f3) {
        if (f3 > this.f5523f) {
            t(true, true);
            return;
        }
        this.f5521d = false;
        this.f5506D.j(0.0f, 0.0f);
        b(this.f5532o, !this.f5537t ? new e() : null);
        this.f5506D.d(false);
    }

    private boolean k(Animation animation) {
        return (animation == null || !animation.hasStarted() || animation.hasEnded()) ? false : true;
    }

    private void l(float f3) {
        this.f5506D.d(true);
        float fMin = Math.min(1.0f, Math.abs(f3 / this.f5523f));
        float fMax = (((float) Math.max(((double) fMin) - 0.4d, 0.0d)) * 5.0f) / 3.0f;
        float fAbs = Math.abs(f3) - this.f5523f;
        int i3 = this.f5505C;
        if (i3 <= 0) {
            i3 = this.f5514L ? this.f5504B - this.f5503A : this.f5504B;
        }
        float f4 = i3;
        double dMax = Math.max(0.0f, Math.min(fAbs, f4 * 2.0f) / f4) / 4.0f;
        float fPow = ((float) (dMax - Math.pow(dMax, 2.0d))) * 2.0f;
        int i4 = this.f5503A + ((int) ((f4 * fMin) + (f4 * fPow * 2.0f)));
        if (this.f5540w.getVisibility() != 0) {
            this.f5540w.setVisibility(0);
        }
        if (!this.f5537t) {
            this.f5540w.setScaleX(1.0f);
            this.f5540w.setScaleY(1.0f);
        }
        if (this.f5537t) {
            setAnimationProgress(Math.min(1.0f, f3 / this.f5523f));
        }
        if (f3 < this.f5523f) {
            if (this.f5506D.getAlpha() > 76 && !k(this.f5509G)) {
                x();
            }
        } else if (this.f5506D.getAlpha() < 255 && !k(this.f5510H)) {
            w();
        }
        this.f5506D.j(0.0f, Math.min(0.8f, fMax * 0.8f));
        this.f5506D.e(Math.min(1.0f, fMax));
        this.f5506D.g((((fMax * 0.4f) - 0.25f) + (fPow * 2.0f)) * 0.5f);
        setTargetOffsetTopAndBottom(i4 - this.f5532o);
    }

    private void q(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.f5536s) {
            this.f5536s = motionEvent.getPointerId(actionIndex == 0 ? 1 : 0);
        }
    }

    private void setColorViewAlpha(int i3) {
        this.f5540w.getBackground().setAlpha(i3);
        this.f5506D.setAlpha(i3);
    }

    private void t(boolean z3, boolean z4) {
        if (this.f5521d != z3) {
            this.f5512J = z4;
            g();
            this.f5521d = z3;
            if (z3) {
                a(this.f5532o, this.f5516N);
            } else {
                y(this.f5516N);
            }
        }
    }

    private Animation u(int i3, int i4) {
        d dVar = new d(i3, i4);
        dVar.setDuration(300L);
        this.f5540w.b(null);
        this.f5540w.clearAnimation();
        this.f5540w.startAnimation(dVar);
        return dVar;
    }

    private void v(float f3) {
        float f4 = this.f5534q;
        float f5 = f3 - f4;
        int i3 = this.f5522e;
        if (f5 <= i3 || this.f5535r) {
            return;
        }
        this.f5533p = f4 + i3;
        this.f5535r = true;
        this.f5506D.setAlpha(76);
    }

    private void w() {
        this.f5510H = u(this.f5506D.getAlpha(), 255);
    }

    private void x() {
        this.f5509G = u(this.f5506D.getAlpha(), 76);
    }

    private void z(int i3, Animation.AnimationListener animationListener) {
        this.f5542y = i3;
        this.f5543z = this.f5540w.getScaleX();
        h hVar = new h();
        this.f5511I = hVar;
        hVar.setDuration(150L);
        if (animationListener != null) {
            this.f5540w.b(animationListener);
        }
        this.f5540w.clearAnimation();
        this.f5540w.startAnimation(this.f5511I);
    }

    @Override // androidx.core.view.F
    public void c(View view, View view2, int i3, int i4) {
        if (i4 == 0) {
            onNestedScrollAccepted(view, view2, i3);
        }
    }

    public boolean d() {
        View view = this.f5519b;
        return view instanceof ListView ? androidx.core.widget.g.a((ListView) view, -1) : view.canScrollVertically(-1);
    }

    @Override // android.view.View
    public boolean dispatchNestedFling(float f3, float f4, boolean z3) {
        return this.f5526i.a(f3, f4, z3);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreFling(float f3, float f4) {
        return this.f5526i.b(f3, f4);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreScroll(int i3, int i4, int[] iArr, int[] iArr2) {
        return this.f5526i.c(i3, i4, iArr, iArr2);
    }

    @Override // android.view.View
    public boolean dispatchNestedScroll(int i3, int i4, int i5, int i6, int[] iArr) {
        return this.f5526i.f(i3, i4, i5, i6, iArr);
    }

    public void f(int i3, int i4, int i5, int i6, int[] iArr, int i7, int[] iArr2) {
        if (i7 == 0) {
            this.f5526i.e(i3, i4, i5, i6, iArr, i7, iArr2);
        }
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i3, int i4) {
        int i5 = this.f5541x;
        return i5 < 0 ? i4 : i4 == i3 + (-1) ? i5 : i4 >= i5 ? i4 + 1 : i4;
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.f5525h.a();
    }

    public int getProgressCircleDiameter() {
        return this.f5513K;
    }

    public int getProgressViewEndOffset() {
        return this.f5504B;
    }

    public int getProgressViewStartOffset() {
        return this.f5503A;
    }

    @Override // android.view.View
    public boolean hasNestedScrollingParent() {
        return this.f5526i.j();
    }

    @Override // androidx.core.view.F
    public void i(View view, int i3) {
        if (i3 == 0) {
            onStopNestedScroll(view);
        }
    }

    @Override // android.view.View
    public boolean isNestedScrollingEnabled() {
        return this.f5526i.l();
    }

    @Override // androidx.core.view.F
    public void j(View view, int i3, int i4, int[] iArr, int i5) {
        if (i5 == 0) {
            onNestedPreScroll(view, i3, i4, iArr);
        }
    }

    @Override // androidx.core.view.G
    public void m(View view, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        if (i7 != 0) {
            return;
        }
        int i8 = iArr[1];
        f(i3, i4, i5, i6, this.f5528k, i7, iArr);
        int i9 = i6 - (iArr[1] - i8);
        if ((i9 == 0 ? i6 + this.f5528k[1] : i9) >= 0 || d()) {
            return;
        }
        float fAbs = this.f5524g + Math.abs(r1);
        this.f5524g = fAbs;
        l(fAbs);
        iArr[1] = iArr[1] + i9;
    }

    @Override // androidx.core.view.F
    public void n(View view, int i3, int i4, int i5, int i6, int i7) {
        m(view, i3, i4, i5, i6, i7, this.f5529l);
    }

    @Override // androidx.core.view.F
    public boolean o(View view, View view2, int i3, int i4) {
        if (i4 == 0) {
            return onStartNestedScroll(view, view2, i3);
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        r();
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0058  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            r4.g()
            int r0 = r5.getActionMasked()
            boolean r1 = r4.f5538u
            r2 = 0
            if (r1 == 0) goto L10
            if (r0 != 0) goto L10
            r4.f5538u = r2
        L10:
            boolean r1 = r4.isEnabled()
            if (r1 == 0) goto L81
            boolean r1 = r4.f5538u
            if (r1 != 0) goto L81
            boolean r1 = r4.d()
            if (r1 != 0) goto L81
            boolean r1 = r4.f5521d
            if (r1 != 0) goto L81
            boolean r1 = r4.f5530m
            if (r1 == 0) goto L29
            goto L81
        L29:
            if (r0 == 0) goto L5d
            r1 = 1
            r3 = -1
            if (r0 == r1) goto L58
            r1 = 2
            if (r0 == r1) goto L3d
            r1 = 3
            if (r0 == r1) goto L58
            r1 = 6
            if (r0 == r1) goto L39
            goto L7e
        L39:
            r4.q(r5)
            goto L7e
        L3d:
            int r0 = r4.f5536s
            if (r0 != r3) goto L49
            java.lang.String r5 = androidx.swiperefreshlayout.widget.c.f5501Q
            java.lang.String r0 = "Got ACTION_MOVE event but don't have an active pointer id."
            android.util.Log.e(r5, r0)
            return r2
        L49:
            int r0 = r5.findPointerIndex(r0)
            if (r0 >= 0) goto L50
            return r2
        L50:
            float r5 = r5.getY(r0)
            r4.v(r5)
            goto L7e
        L58:
            r4.f5535r = r2
            r4.f5536s = r3
            goto L7e
        L5d:
            int r0 = r4.f5503A
            androidx.swiperefreshlayout.widget.a r1 = r4.f5540w
            int r1 = r1.getTop()
            int r0 = r0 - r1
            r4.setTargetOffsetTopAndBottom(r0)
            int r0 = r5.getPointerId(r2)
            r4.f5536s = r0
            r4.f5535r = r2
            int r0 = r5.findPointerIndex(r0)
            if (r0 >= 0) goto L78
            return r2
        L78:
            float r5 = r5.getY(r0)
            r4.f5534q = r5
        L7e:
            boolean r5 = r4.f5535r
            return r5
        L81:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.swiperefreshlayout.widget.c.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (this.f5519b == null) {
            g();
        }
        View view = this.f5519b;
        if (view == null) {
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        view.layout(paddingLeft, paddingTop, ((measuredWidth - getPaddingLeft()) - getPaddingRight()) + paddingLeft, ((measuredHeight - getPaddingTop()) - getPaddingBottom()) + paddingTop);
        int measuredWidth2 = this.f5540w.getMeasuredWidth();
        int measuredHeight2 = this.f5540w.getMeasuredHeight();
        int i7 = measuredWidth / 2;
        int i8 = measuredWidth2 / 2;
        int i9 = this.f5532o;
        this.f5540w.layout(i7 - i8, i9, i7 + i8, measuredHeight2 + i9);
    }

    @Override // android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        if (this.f5519b == null) {
            g();
        }
        View view = this.f5519b;
        if (view == null) {
            return;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), 1073741824));
        this.f5540w.measure(View.MeasureSpec.makeMeasureSpec(this.f5513K, 1073741824), View.MeasureSpec.makeMeasureSpec(this.f5513K, 1073741824));
        this.f5541x = -1;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            if (getChildAt(i5) == this.f5540w) {
                this.f5541x = i5;
                return;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View view, float f3, float f4, boolean z3) {
        return dispatchNestedFling(f3, f4, z3);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPreFling(View view, float f3, float f4) {
        return dispatchNestedPreFling(f3, f4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedPreScroll(View view, int i3, int i4, int[] iArr) {
        if (i4 > 0) {
            float f3 = this.f5524g;
            if (f3 > 0.0f) {
                float f4 = i4;
                if (f4 > f3) {
                    iArr[1] = (int) f3;
                    this.f5524g = 0.0f;
                } else {
                    this.f5524g = f3 - f4;
                    iArr[1] = i4;
                }
                l(this.f5524g);
            }
        }
        if (this.f5514L && i4 > 0 && this.f5524g == 0.0f && Math.abs(i4 - iArr[1]) > 0) {
            this.f5540w.setVisibility(8);
        }
        int[] iArr2 = this.f5527j;
        if (dispatchNestedPreScroll(i3 - iArr[0], i4 - iArr[1], iArr2, null)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScroll(View view, int i3, int i4, int i5, int i6) {
        m(view, i3, i4, i5, i6, 0, this.f5529l);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScrollAccepted(View view, View view2, int i3) {
        this.f5525h.b(view, view2, i3);
        startNestedScroll(i3 & 2);
        this.f5524g = 0.0f;
        this.f5530m = true;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        k kVar = (k) parcelable;
        super.onRestoreInstanceState(kVar.getSuperState());
        setRefreshing(kVar.f5554a);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        return new k(super.onSaveInstanceState(), this.f5521d);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onStartNestedScroll(View view, View view2, int i3) {
        return (!isEnabled() || this.f5538u || this.f5521d || (i3 & 2) == 0) ? false : true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onStopNestedScroll(View view) {
        this.f5525h.d(view);
        this.f5530m = false;
        float f3 = this.f5524g;
        if (f3 > 0.0f) {
            h(f3);
            this.f5524g = 0.0f;
        }
        stopNestedScroll();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (this.f5538u && actionMasked == 0) {
            this.f5538u = false;
        }
        if (!isEnabled() || this.f5538u || d() || this.f5521d || this.f5530m) {
            return false;
        }
        if (actionMasked == 0) {
            this.f5536s = motionEvent.getPointerId(0);
            this.f5535r = false;
        } else {
            if (actionMasked == 1) {
                int iFindPointerIndex = motionEvent.findPointerIndex(this.f5536s);
                if (iFindPointerIndex < 0) {
                    Log.e(f5501Q, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }
                if (this.f5535r) {
                    float y3 = (motionEvent.getY(iFindPointerIndex) - this.f5533p) * 0.5f;
                    this.f5535r = false;
                    h(y3);
                }
                this.f5536s = -1;
                return false;
            }
            if (actionMasked == 2) {
                int iFindPointerIndex2 = motionEvent.findPointerIndex(this.f5536s);
                if (iFindPointerIndex2 < 0) {
                    Log.e(f5501Q, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                float y4 = motionEvent.getY(iFindPointerIndex2);
                v(y4);
                if (this.f5535r) {
                    float f3 = (y4 - this.f5533p) * 0.5f;
                    if (f3 <= 0.0f) {
                        return false;
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                    l(f3);
                }
            } else {
                if (actionMasked == 3) {
                    return false;
                }
                if (actionMasked == 5) {
                    int actionIndex = motionEvent.getActionIndex();
                    if (actionIndex < 0) {
                        Log.e(f5501Q, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                        return false;
                    }
                    this.f5536s = motionEvent.getPointerId(actionIndex);
                } else if (actionMasked == 6) {
                    q(motionEvent);
                }
            }
        }
        return true;
    }

    void p(float f3) {
        setTargetOffsetTopAndBottom((this.f5542y + ((int) ((this.f5503A - r0) * f3))) - this.f5540w.getTop());
    }

    void r() {
        this.f5540w.clearAnimation();
        this.f5506D.stop();
        this.f5540w.setVisibility(8);
        setColorViewAlpha(255);
        if (this.f5537t) {
            setAnimationProgress(0.0f);
        } else {
            setTargetOffsetTopAndBottom(this.f5503A - this.f5532o);
        }
        this.f5532o = this.f5540w.getTop();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z3) {
        ViewParent parent;
        View view = this.f5519b;
        if (view == null || Z.G(view)) {
            super.requestDisallowInterceptTouchEvent(z3);
        } else {
            if (this.f5515M || (parent = getParent()) == null) {
                return;
            }
            parent.requestDisallowInterceptTouchEvent(z3);
        }
    }

    public void s(boolean z3, int i3, int i4) {
        this.f5537t = z3;
        this.f5503A = i3;
        this.f5504B = i4;
        this.f5514L = true;
        r();
        this.f5521d = false;
    }

    void setAnimationProgress(float f3) {
        this.f5540w.setScaleX(f3);
        this.f5540w.setScaleY(f3);
    }

    @Deprecated
    public void setColorScheme(int... iArr) {
        setColorSchemeResources(iArr);
    }

    public void setColorSchemeColors(int... iArr) {
        g();
        this.f5506D.f(iArr);
    }

    public void setColorSchemeResources(int... iArr) {
        Context context = getContext();
        int[] iArr2 = new int[iArr.length];
        for (int i3 = 0; i3 < iArr.length; i3++) {
            iArr2[i3] = androidx.core.content.a.b(context, iArr[i3]);
        }
        setColorSchemeColors(iArr2);
    }

    public void setDistanceToTriggerSync(int i3) {
        this.f5523f = i3;
    }

    @Override // android.view.View
    public void setEnabled(boolean z3) {
        super.setEnabled(z3);
        if (z3) {
            return;
        }
        r();
    }

    @Deprecated
    public void setLegacyRequestDisallowInterceptTouchEventEnabled(boolean z3) {
        this.f5515M = z3;
    }

    @Override // android.view.View
    public void setNestedScrollingEnabled(boolean z3) {
        this.f5526i.m(z3);
    }

    public void setOnChildScrollUpCallback(i iVar) {
    }

    public void setOnRefreshListener(j jVar) {
        this.f5520c = jVar;
    }

    @Deprecated
    public void setProgressBackgroundColor(int i3) {
        setProgressBackgroundColorSchemeResource(i3);
    }

    public void setProgressBackgroundColorSchemeColor(int i3) {
        this.f5540w.setBackgroundColor(i3);
    }

    public void setProgressBackgroundColorSchemeResource(int i3) {
        setProgressBackgroundColorSchemeColor(androidx.core.content.a.b(getContext(), i3));
    }

    public void setRefreshing(boolean z3) {
        if (!z3 || this.f5521d == z3) {
            t(z3, false);
            return;
        }
        this.f5521d = z3;
        setTargetOffsetTopAndBottom((!this.f5514L ? this.f5504B + this.f5503A : this.f5504B) - this.f5532o);
        this.f5512J = false;
        A(this.f5516N);
    }

    public void setSize(int i3) {
        if (i3 == 0 || i3 == 1) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if (i3 == 0) {
                this.f5513K = (int) (displayMetrics.density * 56.0f);
            } else {
                this.f5513K = (int) (displayMetrics.density * 40.0f);
            }
            this.f5540w.setImageDrawable(null);
            this.f5506D.l(i3);
            this.f5540w.setImageDrawable(this.f5506D);
        }
    }

    public void setSlingshotDistance(int i3) {
        this.f5505C = i3;
    }

    void setTargetOffsetTopAndBottom(int i3) {
        this.f5540w.bringToFront();
        Z.L(this.f5540w, i3);
        this.f5532o = this.f5540w.getTop();
    }

    @Override // android.view.View
    public boolean startNestedScroll(int i3) {
        return this.f5526i.o(i3);
    }

    @Override // android.view.View
    public void stopNestedScroll() {
        this.f5526i.q();
    }

    void y(Animation.AnimationListener animationListener) {
        C0086c c0086c = new C0086c();
        this.f5508F = c0086c;
        c0086c.setDuration(150L);
        this.f5540w.b(animationListener);
        this.f5540w.clearAnimation();
        this.f5540w.startAnimation(this.f5508F);
    }

    static class k extends View.BaseSavedState {
        public static final Parcelable.Creator<k> CREATOR = new a();

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final boolean f5554a;

        class a implements Parcelable.Creator {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public k createFromParcel(Parcel parcel) {
                return new k(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
            public k[] newArray(int i3) {
                return new k[i3];
            }
        }

        k(Parcelable parcelable, boolean z3) {
            super(parcelable);
            this.f5554a = z3;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeByte(this.f5554a ? (byte) 1 : (byte) 0);
        }

        k(Parcel parcel) {
            super(parcel);
            this.f5554a = parcel.readByte() != 0;
        }
    }

    public c(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5521d = false;
        this.f5523f = -1.0f;
        this.f5527j = new int[2];
        this.f5528k = new int[2];
        this.f5529l = new int[2];
        this.f5536s = -1;
        this.f5541x = -1;
        this.f5516N = new a();
        this.f5517O = new f();
        this.f5518P = new g();
        this.f5522e = ViewConfiguration.get(context).getScaledTouchSlop();
        this.f5531n = getResources().getInteger(R.integer.config_mediumAnimTime);
        setWillNotDraw(false);
        this.f5539v = new DecelerateInterpolator(2.0f);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.f5513K = (int) (displayMetrics.density * 40.0f);
        e();
        setChildrenDrawingOrderEnabled(true);
        int i3 = (int) (displayMetrics.density * 64.0f);
        this.f5504B = i3;
        this.f5523f = i3;
        this.f5525h = new H(this);
        this.f5526i = new E(this);
        setNestedScrollingEnabled(true);
        int i4 = -this.f5513K;
        this.f5532o = i4;
        this.f5503A = i4;
        p(1.0f);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f5502R);
        setEnabled(typedArrayObtainStyledAttributes.getBoolean(0, true));
        typedArrayObtainStyledAttributes.recycle();
    }
}
