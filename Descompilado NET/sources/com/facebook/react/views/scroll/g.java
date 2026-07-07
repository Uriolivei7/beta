package com.facebook.react.views.scroll;

import R1.p;
import a1.C0210a;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.OverScroller;
import android.widget.ScrollView;
import androidx.core.view.Z;
import com.facebook.react.animated.NativeAnimatedModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.C0418a;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.C0437j0;
import com.facebook.react.uimanager.EnumC0431g0;
import com.facebook.react.uimanager.InterfaceC0435i0;
import com.facebook.react.uimanager.InterfaceC0443m0;
import com.facebook.react.uimanager.W;
import com.facebook.react.uimanager.X;
import com.facebook.react.views.scroll.b;
import com.facebook.react.views.scroll.j;
import d1.AbstractC0505m;
import d2.C0518a;
import java.lang.reflect.Field;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class g extends ScrollView implements InterfaceC0435i0, ViewGroup.OnHierarchyChangeListener, View.OnLayoutChangeListener, d, InterfaceC0443m0, j.c, j.e, j.a, j.b, j.d {

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private static Field f7812J = null;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private static boolean f7813K = false;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private int f7814A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private int f7815B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private A0 f7816C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final j.g f7817D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private final ValueAnimator f7818E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private EnumC0431g0 f7819F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private long f7820G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private int f7821H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private com.facebook.react.views.scroll.b f7822I;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final c f7823b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final OverScroller f7824c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final m f7825d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Rect f7826e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Rect f7827f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f7828g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Rect f7829h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private p f7830i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f7831j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f7832k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Runnable f7833l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f7834m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f7835n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f7836o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private String f7837p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private Drawable f7838q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f7839r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f7840s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private int f7841t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private List f7842u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f7843v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private boolean f7844w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private int f7845x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private View f7846y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private ReadableMap f7847z;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f7848b = false;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f7849c = 0;

        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            NativeAnimatedModule nativeAnimatedModule;
            if (g.this.f7828g) {
                g.this.f7828g = false;
                this.f7849c = 0;
                Z.T(g.this, this, 20L);
                return;
            }
            j.s(g.this);
            int i3 = this.f7849c + 1;
            this.f7849c = i3;
            if (i3 < 3) {
                if (g.this.f7832k && !this.f7848b) {
                    this.f7848b = true;
                    g.this.u(0);
                }
                Z.T(g.this, this, 20L);
                return;
            }
            g.this.f7833l = null;
            if (g.this.f7836o) {
                j.j(g.this);
            }
            ReactContext reactContext = (ReactContext) g.this.getContext();
            if (reactContext != null && (nativeAnimatedModule = (NativeAnimatedModule) reactContext.getNativeModule(NativeAnimatedModule.class)) != null) {
                nativeAnimatedModule.userDrivenScrollEnded(g.this.getId());
            }
            g.this.r();
        }
    }

    static /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f7851a;

        static {
            int[] iArr = new int[p.values().length];
            f7851a = iArr;
            try {
                iArr[p.f2097d.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f7851a[p.f2098e.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f7851a[p.f2096c.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public g(Context context) {
        this(context, null);
    }

    private boolean A() {
        View contentView = getContentView();
        return (contentView == null || contentView.getWidth() == 0 || contentView.getHeight() == 0) ? false : true;
    }

    private boolean B() {
        return false;
    }

    private int C(int i3) {
        if (getFlingAnimator() == this.f7818E) {
            return j.p(this, 0, i3, 0, getMaxScrollY()).y;
        }
        return v(i3) + j.m(this, getScrollY(), getReactScrollViewScrollState().b().y, i3);
    }

    private void D(int i3) {
        if (getFlingAnimator().isRunning()) {
            getFlingAnimator().cancel();
        }
        OverScroller overScroller = this.f7824c;
        if (overScroller == null || overScroller.isFinished()) {
            return;
        }
        int currY = this.f7824c.getCurrY();
        boolean zComputeScrollOffset = this.f7824c.computeScrollOffset();
        this.f7824c.forceFinished(true);
        if (!zComputeScrollOffset) {
            scrollTo(getScrollX(), i3 + (this.f7824c.getCurrX() - currY));
            return;
        }
        this.f7824c.fling(getScrollX(), i3, 0, (int) (this.f7824c.getCurrVelocity() * Math.signum(this.f7824c.getFinalY() - this.f7824c.getStartY())), 0, 0, 0, Integer.MAX_VALUE);
    }

    private void E(View view) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        offsetDescendantRectToMyCoords(view, rect);
        int iComputeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(rect);
        if (iComputeScrollDeltaToGetChildRectOnScreen != 0) {
            scrollBy(0, iComputeScrollDeltaToGetChildRectOnScreen);
        }
    }

    private void G(int i3, int i4) {
        if (A()) {
            this.f7814A = -1;
            this.f7815B = -1;
        } else {
            this.f7814A = i3;
            this.f7815B = i4;
        }
    }

    private void H(int i3) {
        double snapInterval = getSnapInterval();
        double dM = j.m(this, getScrollY(), getReactScrollViewScrollState().b().y, i3);
        double dC = C(i3);
        double d4 = dM / snapInterval;
        int iFloor = (int) Math.floor(d4);
        int iCeil = (int) Math.ceil(d4);
        int iRound = (int) Math.round(d4);
        int iRound2 = (int) Math.round(dC / snapInterval);
        if (i3 > 0 && iCeil == iFloor) {
            iCeil++;
        } else if (i3 < 0 && iFloor == iCeil) {
            iFloor--;
        }
        if (i3 > 0 && iRound < iCeil && iRound2 > iFloor) {
            iRound = iCeil;
        } else if (i3 < 0 && iRound > iFloor && iRound2 < iCeil) {
            iRound = iFloor;
        }
        double d5 = ((double) iRound) * snapInterval;
        if (d5 != dM) {
            this.f7828g = true;
            f(getScrollX(), (int) d5);
        }
    }

    private void I(int i3) {
        getReactScrollViewScrollState().l(i3);
        j.k(this);
    }

    private View getContentView() {
        return getChildAt(0);
    }

    private int getMaxScrollY() {
        View view = this.f7846y;
        return Math.max(0, (view == null ? 0 : view.getHeight()) - ((getHeight() - getPaddingBottom()) - getPaddingTop()));
    }

    private OverScroller getOverScrollerFromParent() {
        if (!f7813K) {
            f7813K = true;
            try {
                Field declaredField = ScrollView.class.getDeclaredField("mScroller");
                f7812J = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
                Y.a.I("ReactNative", "Failed to get mScroller field for ScrollView! This app will exhibit the bounce-back scrolling bug :(");
            }
        }
        Field field = f7812J;
        OverScroller overScroller = null;
        if (field != null) {
            try {
                Object obj = field.get(this);
                if (obj instanceof OverScroller) {
                    overScroller = (OverScroller) obj;
                } else {
                    Y.a.I("ReactNative", "Failed to cast mScroller field in ScrollView (probably due to OEM changes to AOSP)! This app will exhibit the bounce-back scrolling bug :(");
                }
            } catch (IllegalAccessException e4) {
                throw new RuntimeException("Failed to get mScroller from ScrollView!", e4);
            }
        }
        return overScroller;
    }

    private int getSnapInterval() {
        int i3 = this.f7841t;
        return i3 != 0 ? i3 : getHeight();
    }

    private void p() {
        Runnable runnable = this.f7833l;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.f7833l = null;
            getFlingAnimator().cancel();
        }
    }

    private int q(int i3) {
        if (Build.VERSION.SDK_INT != 28) {
            return i3;
        }
        float fSignum = Math.signum(this.f7823b.b());
        if (fSignum == 0.0f) {
            fSignum = Math.signum(i3);
        }
        return (int) (Math.abs(i3) * fSignum);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        if (B()) {
            C0210a.c(null);
            C0210a.c(this.f7837p);
            throw null;
        }
    }

    private void s() {
        if (B()) {
            C0210a.c(null);
            C0210a.c(this.f7837p);
            throw null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0188  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void u(int r28) {
        /*
            Method dump skipped, instruction units count: 510
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.scroll.g.u(int):void");
    }

    private int w(int i3, int i4, int i5, int i6) {
        int i7;
        if (i3 == 1) {
            return i4;
        }
        if (i3 == 2) {
            i7 = (i6 - i5) / 2;
        } else {
            if (i3 != 3) {
                throw new IllegalStateException("Invalid SnapToAlignment value: " + this.f7845x);
            }
            i7 = i6 - i5;
        }
        return i4 - i7;
    }

    private int x(View view) {
        view.getDrawingRect(this.f7826e);
        offsetDescendantRectToMyCoords(view, this.f7826e);
        return computeScrollDeltaToGetChildRectOnScreen(this.f7826e);
    }

    private void z(int i3, int i4) {
        if (this.f7833l != null) {
            return;
        }
        if (this.f7836o) {
            s();
            j.i(this, i3, i4);
        }
        this.f7828g = false;
        a aVar = new a();
        this.f7833l = aVar;
        Z.T(this, aVar, 20L);
    }

    public void F(float f3, int i3) {
        C0418a.q(this, R1.d.values()[i3], Float.isNaN(f3) ? null : new W(C0429f0.f(f3), X.f7408b));
    }

    @Override // com.facebook.react.views.scroll.j.a
    public void a(int i3, int i4) {
        this.f7818E.cancel();
        int iL = j.l(getContext());
        this.f7818E.setDuration(iL).setIntValues(i3, i4);
        this.f7818E.start();
        if (this.f7836o) {
            j.i(this, 0, iL > 0 ? (i4 - i3) / iL : 0);
            j.a(this);
        }
    }

    @Override // com.facebook.react.views.scroll.j.d
    public void b(int i3, int i4) {
        scrollTo(i3, i4);
        D(i4);
    }

    @Override // com.facebook.react.views.scroll.d
    public boolean c(View view) {
        int iX = x(view);
        view.getDrawingRect(this.f7826e);
        return iX != 0 && Math.abs(iX) < this.f7826e.width();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0443m0
    public void d(int i3, int i4, int i5, int i6) {
        this.f7827f.set(i3, i4, i5, i6);
    }

    @Override // android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (EnumC0431g0.c(this.f7819F)) {
            return super.dispatchGenericMotionEvent(motionEvent);
        }
        return false;
    }

    @Override // android.widget.ScrollView, android.view.View
    public void draw(Canvas canvas) {
        if (this.f7839r != 0) {
            View contentView = getContentView();
            if (this.f7838q != null && contentView != null && contentView.getBottom() < getHeight()) {
                this.f7838q.setBounds(0, contentView.getBottom(), getWidth(), getHeight());
                this.f7838q.draw(canvas);
            }
        }
        super.draw(canvas);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public void e() {
        if (this.f7834m) {
            C0518a.c(0L, "ReactScrollView.updateClippingRect");
            try {
                C0210a.c(this.f7829h);
                C0437j0.a(this, this.f7829h);
                KeyEvent.Callback contentView = getContentView();
                if (contentView instanceof InterfaceC0435i0) {
                    ((InterfaceC0435i0) contentView).e();
                }
            } finally {
                C0518a.i(0L);
            }
        }
    }

    @Override // android.widget.ScrollView
    public boolean executeKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (this.f7835n || !(keyCode == 19 || keyCode == 20)) {
            return super.executeKeyEvent(keyEvent);
        }
        return false;
    }

    @Override // com.facebook.react.views.scroll.j.d
    public void f(int i3, int i4) {
        j.r(this, i3, i4);
        G(i3, i4);
    }

    @Override // android.widget.ScrollView
    public void fling(int i3) {
        int iQ = q(i3);
        if (this.f7832k) {
            u(iQ);
        } else if (this.f7824c != null) {
            this.f7824c.fling(getScrollX(), getScrollY(), 0, iQ, 0, 0, 0, Integer.MAX_VALUE, 0, ((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2);
            Z.R(this);
        } else {
            super.fling(iQ);
        }
        z(0, iQ);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public void g(Rect rect) {
        rect.set((Rect) C0210a.c(this.f7829h));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean getChildVisibleRect(View view, Rect rect, Point point) {
        return super.getChildVisibleRect(view, rect, point);
    }

    @Override // com.facebook.react.views.scroll.j.a
    public ValueAnimator getFlingAnimator() {
        return this.f7818E;
    }

    @Override // com.facebook.react.views.scroll.j.b
    public long getLastScrollDispatchTime() {
        return this.f7820G;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0441l0
    public String getOverflow() {
        int i3 = b.f7851a[this.f7830i.ordinal()];
        if (i3 == 1) {
            return "hidden";
        }
        if (i3 == 2) {
            return "scroll";
        }
        if (i3 != 3) {
            return null;
        }
        return "visible";
    }

    @Override // com.facebook.react.uimanager.InterfaceC0443m0
    public Rect getOverflowInset() {
        return this.f7827f;
    }

    public EnumC0431g0 getPointerEvents() {
        return this.f7819F;
    }

    @Override // com.facebook.react.views.scroll.j.c
    public j.g getReactScrollViewScrollState() {
        return this.f7817D;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public boolean getRemoveClippedSubviews() {
        return this.f7834m;
    }

    @Override // com.facebook.react.views.scroll.d
    public boolean getScrollEnabled() {
        return this.f7835n;
    }

    @Override // com.facebook.react.views.scroll.j.b
    public int getScrollEventThrottle() {
        return this.f7821H;
    }

    @Override // com.facebook.react.views.scroll.j.e
    public A0 getStateWrapper() {
        return this.f7816C;
    }

    public void o() {
        OverScroller overScroller = this.f7824c;
        if (overScroller == null || overScroller.isFinished()) {
            return;
        }
        this.f7824c.abortAnimation();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f7834m) {
            e();
        }
        com.facebook.react.views.scroll.b bVar = this.f7822I;
        if (bVar != null) {
            bVar.f();
        }
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    public void onChildViewAdded(View view, View view2) {
        this.f7846y = view2;
        view2.addOnLayoutChangeListener(this);
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    public void onChildViewRemoved(View view, View view2) {
        View view3 = this.f7846y;
        if (view3 != null) {
            view3.removeOnLayoutChangeListener(this);
            this.f7846y = null;
        }
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        com.facebook.react.views.scroll.b bVar = this.f7822I;
        if (bVar != null) {
            bVar.g();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.f7830i != p.f2096c) {
            C0418a.a(this, canvas);
        }
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        String str = (String) getTag(AbstractC0505m.f9247t);
        if (str != null) {
            accessibilityNodeInfo.setViewIdResourceName(str);
        }
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.f7835n) {
            return false;
        }
        if (!EnumC0431g0.c(this.f7819F)) {
            return true;
        }
        try {
            if (super.onInterceptTouchEvent(motionEvent)) {
                y(motionEvent);
                return true;
            }
        } catch (IllegalArgumentException e4) {
            Y.a.J("ReactNative", "Error intercepting touch event.", e4);
        }
        return false;
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        if (A()) {
            int scrollX = this.f7814A;
            if (scrollX == -1) {
                scrollX = getScrollX();
            }
            int scrollY = this.f7815B;
            if (scrollY == -1) {
                scrollY = getScrollY();
            }
            scrollTo(scrollX, scrollY);
        }
        j.c(this);
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        if (this.f7846y == null) {
            return;
        }
        com.facebook.react.views.scroll.b bVar = this.f7822I;
        if (bVar != null) {
            bVar.h();
        }
        if (isShown() && A()) {
            int scrollY = getScrollY();
            int maxScrollY = getMaxScrollY();
            if (scrollY > maxScrollY) {
                scrollTo(getScrollX(), maxScrollY);
            }
        }
        j.b(this);
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i3, int i4) {
        com.facebook.react.uimanager.Z.a(i3, i4);
        setMeasuredDimension(View.MeasureSpec.getSize(i3), View.MeasureSpec.getSize(i4));
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onOverScrolled(int i3, int i4, boolean z3, boolean z4) {
        int maxScrollY;
        OverScroller overScroller = this.f7824c;
        if (overScroller != null && this.f7846y != null && !overScroller.isFinished() && this.f7824c.getCurrY() != this.f7824c.getFinalY() && i4 >= (maxScrollY = getMaxScrollY())) {
            this.f7824c.abortAnimation();
            i4 = maxScrollY;
        }
        super.onOverScrolled(i3, i4, z3, z4);
    }

    @Override // android.view.View
    protected void onScrollChanged(int i3, int i4, int i5, int i6) {
        C0518a.c(0L, "ReactScrollView.onScrollChanged");
        try {
            super.onScrollChanged(i3, i4, i5, i6);
            this.f7828g = true;
            if (this.f7823b.c(i3, i4)) {
                if (this.f7834m) {
                    e();
                }
                j.u(this, this.f7823b.a(), this.f7823b.b());
            }
            C0518a.i(0L);
        } catch (Throwable th) {
            C0518a.i(0L);
            throw th;
        }
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        if (this.f7834m) {
            e();
        }
    }

    @Override // android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.f7835n || !EnumC0431g0.b(this.f7819F)) {
            return false;
        }
        this.f7825d.a(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 && this.f7831j) {
            j.s(this);
            float fB = this.f7825d.b();
            float fC = this.f7825d.c();
            j.e(this, fB, fC);
            P1.m.a(this, motionEvent);
            this.f7831j = false;
            z(Math.round(fB), Math.round(fC));
        }
        if (actionMasked == 0) {
            p();
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (view2 != null) {
            E(view2);
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.widget.ScrollView, android.view.View
    public void scrollTo(int i3, int i4) {
        super.scrollTo(i3, i4);
        j.s(this);
        G(i3, i4);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i3) {
        C0418a.n(this, Integer.valueOf(i3));
    }

    public void setBorderRadius(float f3) {
        F(f3, R1.d.f1999b.ordinal());
    }

    public void setBorderStyle(String str) {
        C0418a.r(this, str == null ? null : R1.f.b(str));
    }

    public void setContentOffset(ReadableMap readableMap) {
        ReadableMap readableMap2 = this.f7847z;
        if (readableMap2 == null || !readableMap2.equals(readableMap)) {
            this.f7847z = readableMap;
            if (readableMap != null) {
                scrollTo((int) C0429f0.g(readableMap.hasKey("x") ? readableMap.getDouble("x") : 0.0d), (int) C0429f0.g(readableMap.hasKey("y") ? readableMap.getDouble("y") : 0.0d));
            } else {
                scrollTo(0, 0);
            }
        }
    }

    public void setDecelerationRate(float f3) {
        getReactScrollViewScrollState().h(f3);
        OverScroller overScroller = this.f7824c;
        if (overScroller != null) {
            overScroller.setFriction(1.0f - f3);
        }
    }

    public void setDisableIntervalMomentum(boolean z3) {
        this.f7840s = z3;
    }

    public void setEndFillColor(int i3) {
        if (i3 != this.f7839r) {
            this.f7839r = i3;
            this.f7838q = new ColorDrawable(this.f7839r);
        }
    }

    @Override // com.facebook.react.views.scroll.j.b
    public void setLastScrollDispatchTime(long j3) {
        this.f7820G = j3;
    }

    public void setMaintainVisibleContentPosition(b.C0115b c0115b) {
        com.facebook.react.views.scroll.b bVar;
        if (c0115b != null && this.f7822I == null) {
            com.facebook.react.views.scroll.b bVar2 = new com.facebook.react.views.scroll.b(this, false);
            this.f7822I = bVar2;
            bVar2.f();
        } else if (c0115b == null && (bVar = this.f7822I) != null) {
            bVar.g();
            this.f7822I = null;
        }
        com.facebook.react.views.scroll.b bVar3 = this.f7822I;
        if (bVar3 != null) {
            bVar3.e(c0115b);
        }
    }

    public void setOverflow(String str) {
        if (str == null) {
            this.f7830i = p.f2098e;
        } else {
            p pVarB = p.b(str);
            if (pVarB == null) {
                pVarB = p.f2098e;
            }
            this.f7830i = pVarB;
        }
        invalidate();
    }

    public void setPagingEnabled(boolean z3) {
        this.f7832k = z3;
    }

    public void setPointerEvents(EnumC0431g0 enumC0431g0) {
        this.f7819F = enumC0431g0;
    }

    public void setRemoveClippedSubviews(boolean z3) {
        if (z3 && this.f7829h == null) {
            this.f7829h = new Rect();
        }
        this.f7834m = z3;
        e();
    }

    public void setScrollAwayTopPaddingEnabledUnstable(int i3) {
        int childCount = getChildCount();
        C0210a.b(childCount <= 1, "React Native ScrollView should not have more than one child, it should have exactly 1 child; a content View");
        if (childCount > 0) {
            for (int i4 = 0; i4 < childCount; i4++) {
                getChildAt(i4).setTranslationY(i3);
            }
            setPadding(0, 0, 0, i3);
        }
        I(i3);
        setRemoveClippedSubviews(this.f7834m);
    }

    public void setScrollEnabled(boolean z3) {
        this.f7835n = z3;
    }

    public void setScrollEventThrottle(int i3) {
        this.f7821H = i3;
    }

    public void setScrollPerfTag(String str) {
        this.f7837p = str;
    }

    public void setSendMomentumEvents(boolean z3) {
        this.f7836o = z3;
    }

    public void setSnapInterval(int i3) {
        this.f7841t = i3;
    }

    public void setSnapOffsets(List<Integer> list) {
        this.f7842u = list;
    }

    public void setSnapToAlignment(int i3) {
        this.f7845x = i3;
    }

    public void setSnapToEnd(boolean z3) {
        this.f7844w = z3;
    }

    public void setSnapToStart(boolean z3) {
        this.f7843v = z3;
    }

    public void setStateWrapper(A0 a02) {
        this.f7816C = a02;
    }

    public void t() {
        awakenScrollBars();
    }

    public int v(int i3) {
        return j.p(this, 0, i3, 0, getMaxScrollY()).y;
    }

    protected void y(MotionEvent motionEvent) {
        P1.m.b(this, motionEvent);
        j.d(this);
        this.f7831j = true;
        s();
        getFlingAnimator().cancel();
    }

    public g(Context context, com.facebook.react.views.scroll.a aVar) {
        super(context);
        this.f7823b = new c();
        this.f7825d = new m();
        this.f7826e = new Rect();
        this.f7827f = new Rect();
        this.f7830i = p.f2098e;
        this.f7832k = false;
        this.f7835n = true;
        this.f7839r = 0;
        this.f7840s = false;
        this.f7841t = 0;
        this.f7843v = true;
        this.f7844w = true;
        this.f7845x = 0;
        this.f7847z = null;
        this.f7814A = -1;
        this.f7815B = -1;
        this.f7816C = null;
        this.f7817D = new j.g();
        this.f7818E = ObjectAnimator.ofInt(this, "scrollY", 0, 0);
        this.f7819F = EnumC0431g0.f7482f;
        this.f7820G = 0L;
        this.f7821H = 0;
        this.f7822I = null;
        this.f7824c = getOverScrollerFromParent();
        setOnHierarchyChangeListener(this);
        setScrollBarStyle(33554432);
        setClipChildren(false);
        Z.X(this, new h());
    }
}
