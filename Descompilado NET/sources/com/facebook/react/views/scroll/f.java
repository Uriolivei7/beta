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
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.HorizontalScrollView;
import android.widget.OverScroller;
import androidx.core.view.Z;
import com.facebook.react.animated.NativeAnimatedModule;
import com.facebook.react.bridge.ReactContext;
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
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class f extends HorizontalScrollView implements InterfaceC0435i0, ViewGroup.OnHierarchyChangeListener, View.OnLayoutChangeListener, d, InterfaceC0443m0, j.c, j.e, j.a, j.b, j.d {

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private static boolean f7768K = false;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private static String f7769L = "f";

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private static int f7770M = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private static Field f7771N = null;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private static boolean f7772O = false;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private int f7773A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private A0 f7774B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private final j.g f7775C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final ValueAnimator f7776D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private EnumC0431g0 f7777E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private long f7778F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private int f7779G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private View f7780H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private com.facebook.react.views.scroll.b f7781I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private final Rect f7782J;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f7783b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final c f7784c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final OverScroller f7785d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final m f7786e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Rect f7787f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f7788g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Rect f7789h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private p f7790i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f7791j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f7792k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Runnable f7793l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f7794m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f7795n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f7796o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private String f7797p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private Drawable f7798q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f7799r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f7800s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private int f7801t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private List f7802u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f7803v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private boolean f7804w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private int f7805x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private boolean f7806y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private int f7807z;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f7808b = false;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f7809c = 0;

        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            NativeAnimatedModule nativeAnimatedModule;
            if (f.this.f7788g) {
                f.this.f7788g = false;
                this.f7809c = 0;
                Z.T(f.this, this, 20L);
                return;
            }
            j.s(f.this);
            int i3 = this.f7809c + 1;
            this.f7809c = i3;
            if (i3 < 3) {
                if (f.this.f7792k && !this.f7808b) {
                    this.f7808b = true;
                    f.this.w(0);
                }
                Z.T(f.this, this, 20L);
                return;
            }
            f.this.f7793l = null;
            if (f.this.f7796o) {
                j.j(f.this);
            }
            ReactContext reactContext = (ReactContext) f.this.getContext();
            if (reactContext != null && (nativeAnimatedModule = (NativeAnimatedModule) reactContext.getNativeModule(NativeAnimatedModule.class)) != null) {
                nativeAnimatedModule.userDrivenScrollEnded(f.this.getId());
            }
            f.this.r();
        }
    }

    static /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f7811a;

        static {
            int[] iArr = new int[p.values().length];
            f7811a = iArr;
            try {
                iArr[p.f2097d.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f7811a[p.f2098e.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f7811a[p.f2096c.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public f(Context context) {
        this(context, null);
    }

    private void B(int i3, int i4) {
        if (f7768K) {
            Y.a.u(f7769L, "handlePostTouchScrolling[%d] velocityX %d velocityY %d", Integer.valueOf(getId()), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        if (this.f7793l != null) {
            return;
        }
        if (this.f7796o) {
            j.i(this, i3, i4);
        }
        this.f7788g = false;
        a aVar = new a();
        this.f7793l = aVar;
        Z.T(this, aVar, 20L);
    }

    private boolean C() {
        View contentView = getContentView();
        return (contentView == null || contentView.getWidth() == 0 || contentView.getHeight() == 0) ? false : true;
    }

    private boolean D(View view) {
        int iZ = z(view);
        view.getDrawingRect(this.f7782J);
        return iZ != 0 && Math.abs(iZ) < this.f7782J.width() / 2;
    }

    private boolean E() {
        return false;
    }

    private boolean F(View view) {
        return z(view) == 0;
    }

    private int G(int i3) {
        int iMax = Math.max(0, computeHorizontalScrollRange() - getWidth());
        if (getFlingAnimator() == this.f7776D) {
            return j.p(this, i3, 0, iMax, 0).x;
        }
        return x(i3) + j.m(this, getScrollX(), getReactScrollViewScrollState().b().x, i3);
    }

    private void H(int i3, int i4) {
        if (getFlingAnimator().isRunning()) {
            getFlingAnimator().cancel();
        }
        OverScroller overScroller = this.f7785d;
        if (overScroller == null || overScroller.isFinished()) {
            return;
        }
        int currX = this.f7785d.getCurrX();
        boolean zComputeScrollOffset = this.f7785d.computeScrollOffset();
        this.f7785d.forceFinished(true);
        if (!zComputeScrollOffset) {
            scrollTo(i3 + (this.f7785d.getCurrX() - currX), getScrollY());
            return;
        }
        this.f7785d.fling(i3, getScrollY(), (int) (this.f7785d.getCurrVelocity() * Math.signum(this.f7785d.getFinalX() - this.f7785d.getStartX())), 0, 0, i4, 0, 0);
    }

    private void I(View view) {
        int iZ = z(view);
        if (iZ != 0) {
            scrollBy(iZ, 0);
        }
    }

    private void K(int i3, int i4) {
        if (f7768K) {
            Y.a.u(f7769L, "setPendingContentOffsets[%d] x %d y %d", Integer.valueOf(getId()), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        if (C()) {
            this.f7807z = -1;
            this.f7773A = -1;
        } else {
            this.f7807z = i3;
            this.f7773A = i4;
        }
    }

    private void L(int i3) {
        if (f7768K) {
            Y.a.t(f7769L, "smoothScrollAndSnap[%d] velocity %d", Integer.valueOf(getId()), Integer.valueOf(i3));
        }
        double snapInterval = getSnapInterval();
        double dM = j.m(this, getScrollX(), getReactScrollViewScrollState().b().x, i3);
        double dG = G(i3);
        double d4 = dM / snapInterval;
        int iFloor = (int) Math.floor(d4);
        int iCeil = (int) Math.ceil(d4);
        int iRound = (int) Math.round(d4);
        int iRound2 = (int) Math.round(dG / snapInterval);
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
            this.f7788g = true;
            f((int) d5, getScrollY());
        }
    }

    private void M(int i3) {
        if (f7768K) {
            Y.a.t(f7769L, "smoothScrollToNextPage[%d] direction %d", Integer.valueOf(getId()), Integer.valueOf(i3));
        }
        int width = getWidth();
        int scrollX = getScrollX();
        int i4 = scrollX / width;
        if (scrollX % width != 0) {
            i4++;
        }
        int i5 = i3 == 17 ? i4 - 1 : i4 + 1;
        if (i5 < 0) {
            i5 = 0;
        }
        f(i5 * width, getScrollY());
        B(0, 0);
    }

    private View getContentView() {
        return getChildAt(0);
    }

    private OverScroller getOverScrollerFromParent() {
        if (!f7772O) {
            f7772O = true;
            try {
                Field declaredField = HorizontalScrollView.class.getDeclaredField("mScroller");
                f7771N = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
                Y.a.I(f7769L, "Failed to get mScroller field for HorizontalScrollView! This app will exhibit the bounce-back scrolling bug :(");
            }
        }
        Field field = f7771N;
        OverScroller overScroller = null;
        if (field != null) {
            try {
                Object obj = field.get(this);
                if (obj instanceof OverScroller) {
                    overScroller = (OverScroller) obj;
                } else {
                    Y.a.I(f7769L, "Failed to cast mScroller field in HorizontalScrollView (probably due to OEM changes to AOSP)! This app will exhibit the bounce-back scrolling bug :(");
                }
            } catch (IllegalAccessException e4) {
                throw new RuntimeException("Failed to get mScroller from HorizontalScrollView!", e4);
            }
        }
        return overScroller;
    }

    private int getSnapInterval() {
        int i3 = this.f7801t;
        return i3 != 0 ? i3 : getWidth();
    }

    private void p(int i3, int i4, int i5, int i6) {
        if (getFlingAnimator().isRunning()) {
            getFlingAnimator().end();
        }
        int i7 = i4 - i3;
        int scrollX = i7 - (i6 - getScrollX());
        scrollTo(scrollX, getScrollY());
        H(scrollX, i7 - getWidth());
    }

    private void q() {
        Runnable runnable = this.f7793l;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.f7793l = null;
            getFlingAnimator().cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        if (E()) {
            C0210a.c(null);
            C0210a.c(this.f7797p);
            throw null;
        }
    }

    private void s() {
        if (E()) {
            C0210a.c(null);
            C0210a.c(this.f7797p);
            throw null;
        }
    }

    private static HorizontalScrollView t(View view, MotionEvent motionEvent) {
        return u(view, motionEvent, true);
    }

    private static HorizontalScrollView u(View view, MotionEvent motionEvent, boolean z3) {
        if (view == null) {
            return null;
        }
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        if (!rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
            return null;
        }
        if (!z3 && (view instanceof HorizontalScrollView) && Z.G(view) && (view instanceof f) && ((f) view).f7795n) {
            return (HorizontalScrollView) view;
        }
        if (view instanceof ViewGroup) {
            int i3 = 0;
            while (true) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (i3 >= viewGroup.getChildCount()) {
                    break;
                }
                HorizontalScrollView horizontalScrollViewU = u(viewGroup.getChildAt(i3), motionEvent, false);
                if (horizontalScrollViewU != null) {
                    return horizontalScrollViewU;
                }
                i3++;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w(int i3) {
        int i4;
        int iMin;
        int iIntValue;
        int iMax;
        int iIntValue2;
        OverScroller overScroller;
        if (f7768K) {
            Y.a.t(f7769L, "smoothScrollAndSnap[%d] velocityX %d", Integer.valueOf(getId()), Integer.valueOf(i3));
        }
        if (getChildCount() <= 0) {
            return;
        }
        if (this.f7801t == 0 && this.f7802u == null && this.f7805x == 0) {
            L(i3);
            return;
        }
        boolean z3 = getFlingAnimator() != this.f7776D;
        int iMax2 = Math.max(0, computeHorizontalScrollRange() - getWidth());
        int iG = G(i3);
        if (this.f7800s) {
            iG = getScrollX();
        }
        int width = (getWidth() - Z.w(this)) - Z.v(this);
        if (getLayoutDirection() == 1) {
            iG = iMax2 - iG;
            i4 = -i3;
        } else {
            i4 = i3;
        }
        List list = this.f7802u;
        if (list == null || list.isEmpty()) {
            int i5 = this.f7805x;
            if (i5 != 0) {
                int i6 = this.f7801t;
                if (i6 > 0) {
                    double d4 = ((double) iG) / ((double) i6);
                    double dFloor = Math.floor(d4);
                    int i7 = this.f7801t;
                    int iMax3 = Math.max(y(i5, (int) (dFloor * ((double) i7)), i7, width), 0);
                    int i8 = this.f7805x;
                    double dCeil = Math.ceil(d4);
                    int i9 = this.f7801t;
                    iMin = Math.min(y(i8, (int) (dCeil * ((double) i9)), i9, width), iMax2);
                    iIntValue = iMax2;
                    iMax = iMax3;
                    iIntValue2 = 0;
                } else {
                    ViewGroup viewGroup = (ViewGroup) getContentView();
                    int iMin2 = iMax2;
                    int i10 = iMin2;
                    int i11 = 0;
                    int iMax4 = 0;
                    for (int i12 = 0; i12 < viewGroup.getChildCount(); i12++) {
                        View childAt = viewGroup.getChildAt(i12);
                        int iY = y(this.f7805x, childAt.getLeft(), childAt.getWidth(), width);
                        if (iY <= iG && iG - iY < iG - i11) {
                            i11 = iY;
                        }
                        if (iY >= iG && iY - iG < i10 - iG) {
                            i10 = iY;
                        }
                        iMin2 = Math.min(iMin2, iY);
                        iMax4 = Math.max(iMax4, iY);
                    }
                    iMax = Math.max(i11, iMin2);
                    iMin = Math.min(i10, iMax4);
                    iIntValue = iMax2;
                }
            } else {
                double snapInterval = getSnapInterval();
                double d5 = ((double) iG) / snapInterval;
                int iFloor = (int) (Math.floor(d5) * snapInterval);
                iMin = Math.min((int) (Math.ceil(d5) * snapInterval), iMax2);
                iIntValue = iMax2;
                iMax = iFloor;
            }
            iIntValue2 = 0;
        } else {
            iIntValue2 = ((Integer) this.f7802u.get(0)).intValue();
            List list2 = this.f7802u;
            iIntValue = ((Integer) list2.get(list2.size() - 1)).intValue();
            iMin = iMax2;
            iMax = 0;
            for (int i13 = 0; i13 < this.f7802u.size(); i13++) {
                int iIntValue3 = ((Integer) this.f7802u.get(i13)).intValue();
                if (iIntValue3 <= iG && iG - iIntValue3 < iG - iMax) {
                    iMax = iIntValue3;
                }
                if (iIntValue3 >= iG && iIntValue3 - iG < iMin - iG) {
                    iMin = iIntValue3;
                }
            }
        }
        int i14 = iG - iMax;
        int i15 = iMin - iG;
        int i16 = Math.abs(i14) < Math.abs(i15) ? iMax : iMin;
        int scrollX = getScrollX();
        if (getLayoutDirection() == 1) {
            scrollX = iMax2 - scrollX;
        }
        if (this.f7804w || iG < iIntValue) {
            if (this.f7803v || iG > iIntValue2) {
                if (i4 > 0) {
                    if (!z3) {
                        i4 += (int) (((double) i15) * 10.0d);
                    }
                    iG = iMin;
                } else if (i4 < 0) {
                    if (!z3) {
                        i4 -= (int) (((double) i14) * 10.0d);
                    }
                    iG = iMax;
                } else {
                    iG = i16;
                }
            } else if (scrollX > iIntValue2) {
                iG = iIntValue2;
            }
        } else if (scrollX < iIntValue) {
            iG = iIntValue;
        }
        int iMin3 = Math.min(Math.max(0, iG), iMax2);
        if (getLayoutDirection() == 1) {
            iMin3 = iMax2 - iMin3;
            i4 = -i4;
        }
        int i17 = iMin3;
        if (z3 || (overScroller = this.f7785d) == null) {
            f(i17, getScrollY());
            return;
        }
        this.f7788g = true;
        overScroller.fling(getScrollX(), getScrollY(), i4 != 0 ? i4 : i17 - getScrollX(), 0, i17, i17, 0, 0, (i17 == 0 || i17 == iMax2) ? width / 2 : 0, 0);
        postInvalidateOnAnimation();
    }

    private int y(int i3, int i4, int i5, int i6) {
        int i7;
        if (i3 == 1) {
            return i4;
        }
        if (i3 == 2) {
            i7 = (i6 - i5) / 2;
        } else {
            if (i3 != 3) {
                throw new IllegalStateException("Invalid SnapToAlignment value: " + this.f7805x);
            }
            i7 = i6 - i5;
        }
        return i4 - i7;
    }

    private int z(View view) {
        view.getDrawingRect(this.f7782J);
        offsetDescendantRectToMyCoords(view, this.f7782J);
        return computeScrollDeltaToGetChildRectOnScreen(this.f7782J);
    }

    protected void A(MotionEvent motionEvent) {
        P1.m.b(this, motionEvent);
        j.d(this);
        this.f7791j = true;
        s();
        getFlingAnimator().cancel();
    }

    public void J(float f3, int i3) {
        C0418a.q(this, R1.d.values()[i3], Float.isNaN(f3) ? null : new W(C0429f0.f(f3), X.f7408b));
    }

    @Override // com.facebook.react.views.scroll.j.a
    public void a(int i3, int i4) {
        this.f7776D.cancel();
        int iL = j.l(getContext());
        this.f7776D.setDuration(iL).setIntValues(i3, i4);
        this.f7776D.start();
        if (this.f7796o) {
            j.i(this, iL > 0 ? (i4 - i3) / iL : 0, 0);
            j.a(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList arrayList, int i3, int i4) {
        if (!this.f7792k || this.f7806y) {
            super.addFocusables(arrayList, i3, i4);
            return;
        }
        ArrayList<View> arrayList2 = new ArrayList<>();
        super.addFocusables(arrayList2, i3, i4);
        for (View view : arrayList2) {
            if (F(view) || c(view) || view.isFocused()) {
                arrayList.add(view);
            }
        }
    }

    @Override // android.widget.HorizontalScrollView
    public boolean arrowScroll(int i3) {
        if (!this.f7792k) {
            return super.arrowScroll(i3);
        }
        boolean z3 = true;
        this.f7806y = true;
        if (getChildCount() > 0) {
            View viewFindNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus(), i3);
            View contentView = getContentView();
            if (contentView == null || viewFindNextFocus == null || viewFindNextFocus.getParent() != contentView) {
                M(i3);
            } else {
                if (!F(viewFindNextFocus) && !D(viewFindNextFocus)) {
                    M(i3);
                }
                viewFindNextFocus.requestFocus();
            }
        } else {
            z3 = false;
        }
        this.f7806y = false;
        return z3;
    }

    @Override // com.facebook.react.views.scroll.j.d
    public void b(int i3, int i4) {
        scrollTo(i3, i4);
        H(i3, Integer.MAX_VALUE);
    }

    @Override // com.facebook.react.views.scroll.d
    public boolean c(View view) {
        int iZ = z(view);
        view.getDrawingRect(this.f7782J);
        return iZ != 0 && Math.abs(iZ) < this.f7782J.width();
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i3) {
        return this.f7795n && super.canScrollHorizontally(i3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0443m0
    public void d(int i3, int i4, int i5, int i6) {
        this.f7787f.set(i3, i4, i5, i6);
    }

    @Override // android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (EnumC0431g0.c(this.f7777E)) {
            return super.dispatchGenericMotionEvent(motionEvent);
        }
        return false;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public void draw(Canvas canvas) {
        if (this.f7799r != 0) {
            View contentView = getContentView();
            if (this.f7798q != null && contentView != null && contentView.getRight() < getWidth()) {
                this.f7798q.setBounds(contentView.getRight(), 0, getWidth(), getHeight());
                this.f7798q.draw(canvas);
            }
        }
        super.draw(canvas);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public void e() {
        if (this.f7794m) {
            C0518a.c(0L, "ReactHorizontalScrollView.updateClippingRect");
            try {
                C0210a.c(this.f7789h);
                C0437j0.a(this, this.f7789h);
                KeyEvent.Callback contentView = getContentView();
                if (contentView instanceof InterfaceC0435i0) {
                    ((InterfaceC0435i0) contentView).e();
                }
            } finally {
                C0518a.i(0L);
            }
        }
    }

    @Override // android.widget.HorizontalScrollView
    public boolean executeKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (this.f7795n || !(keyCode == 21 || keyCode == 22)) {
            return super.executeKeyEvent(keyEvent);
        }
        return false;
    }

    @Override // com.facebook.react.views.scroll.j.d
    public void f(int i3, int i4) {
        j.r(this, i3, i4);
        K(i3, i4);
    }

    @Override // android.widget.HorizontalScrollView
    public void fling(int i3) {
        if (f7768K) {
            Y.a.t(f7769L, "fling[%d] velocityX %d", Integer.valueOf(getId()), Integer.valueOf(i3));
        }
        if (Build.VERSION.SDK_INT == 28) {
            i3 = (int) (Math.abs(i3) * Math.signum(this.f7784c.a()));
        }
        if (this.f7792k) {
            w(i3);
        } else if (this.f7785d != null) {
            this.f7785d.fling(getScrollX(), getScrollY(), i3, 0, 0, Integer.MAX_VALUE, 0, 0, ((getWidth() - Z.w(this)) - Z.v(this)) / 2, 0);
            Z.R(this);
        } else {
            super.fling(i3);
        }
        B(i3, 0);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public void g(Rect rect) {
        rect.set((Rect) C0210a.c(this.f7789h));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean getChildVisibleRect(View view, Rect rect, Point point) {
        return super.getChildVisibleRect(view, rect, point);
    }

    @Override // com.facebook.react.views.scroll.j.a
    public ValueAnimator getFlingAnimator() {
        return this.f7776D;
    }

    @Override // com.facebook.react.views.scroll.j.b
    public long getLastScrollDispatchTime() {
        return this.f7778F;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0441l0
    public String getOverflow() {
        int i3 = b.f7811a[this.f7790i.ordinal()];
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
        return this.f7787f;
    }

    public EnumC0431g0 getPointerEvents() {
        return this.f7777E;
    }

    @Override // com.facebook.react.views.scroll.j.c
    public j.g getReactScrollViewScrollState() {
        return this.f7775C;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public boolean getRemoveClippedSubviews() {
        return this.f7794m;
    }

    @Override // com.facebook.react.views.scroll.d
    public boolean getScrollEnabled() {
        return this.f7795n;
    }

    @Override // com.facebook.react.views.scroll.j.b
    public int getScrollEventThrottle() {
        return this.f7779G;
    }

    @Override // com.facebook.react.views.scroll.j.e
    public A0 getStateWrapper() {
        return this.f7774B;
    }

    public void o() {
        OverScroller overScroller = this.f7785d;
        if (overScroller == null || overScroller.isFinished()) {
            return;
        }
        this.f7785d.abortAnimation();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f7794m) {
            e();
        }
        com.facebook.react.views.scroll.b bVar = this.f7781I;
        if (bVar != null) {
            bVar.f();
        }
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    public void onChildViewAdded(View view, View view2) {
        this.f7780H = view2;
        view2.addOnLayoutChangeListener(this);
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    public void onChildViewRemoved(View view, View view2) {
        View view3 = this.f7780H;
        if (view3 != null) {
            view3.removeOnLayoutChangeListener(this);
        }
        this.f7780H = null;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        com.facebook.react.views.scroll.b bVar = this.f7781I;
        if (bVar != null) {
            bVar.g();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.f7790i != p.f2096c) {
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

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.f7795n) {
            return false;
        }
        if (motionEvent.getAction() == 0 && t(this, motionEvent) != null) {
            return false;
        }
        if (!EnumC0431g0.c(this.f7777E)) {
            return true;
        }
        try {
            if (super.onInterceptTouchEvent(motionEvent)) {
                A(motionEvent);
                return true;
            }
        } catch (IllegalArgumentException e4) {
            Y.a.J("ReactNative", "Error intercepting touch event.", e4);
        }
        return false;
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        OverScroller overScroller;
        if (f7768K) {
            Y.a.v(f7769L, "onLayout[%d] l %d t %d r %d b %d", Integer.valueOf(getId()), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6));
        }
        int i7 = this.f7783b;
        if (i7 != f7770M && (overScroller = this.f7785d) != null && i7 != overScroller.getFinalX() && !this.f7785d.isFinished()) {
            if (f7768K) {
                Y.a.t(f7769L, "onLayout[%d] scroll hack enabled: reset to previous scrollX position of %d", Integer.valueOf(getId()), Integer.valueOf(this.f7783b));
            }
            OverScroller overScroller2 = this.f7785d;
            overScroller2.startScroll(this.f7783b, overScroller2.getFinalY(), 0, 0);
            this.f7785d.forceFinished(true);
            this.f7783b = f7770M;
        }
        if (C()) {
            int scrollX = this.f7807z;
            if (scrollX == -1) {
                scrollX = getScrollX();
            }
            int scrollY = this.f7773A;
            if (scrollY == -1) {
                scrollY = getScrollY();
            }
            scrollTo(scrollX, scrollY);
        }
        j.c(this);
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        if (this.f7780H == null) {
            return;
        }
        if (view.getLayoutDirection() == 1) {
            p(i3, i5, i7, i9);
        } else {
            com.facebook.react.views.scroll.b bVar = this.f7781I;
            if (bVar != null) {
                bVar.h();
            }
        }
        j.b(this);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i3, int i4) {
        OverScroller overScroller;
        com.facebook.react.uimanager.Z.a(i3, i4);
        int size = View.MeasureSpec.getSize(i3);
        int size2 = View.MeasureSpec.getSize(i4);
        if (f7768K) {
            Y.a.u(f7769L, "onMeasure[%d] measured width: %d measured height: %d", Integer.valueOf(getId()), Integer.valueOf(size), Integer.valueOf(size2));
        }
        boolean z3 = getMeasuredHeight() != size2;
        setMeasuredDimension(size, size2);
        if (!z3 || (overScroller = this.f7785d) == null) {
            return;
        }
        this.f7783b = overScroller.getCurrX();
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    protected void onOverScrolled(int i3, int i4, boolean z3, boolean z4) {
        int iMax;
        if (f7768K) {
            Y.a.v(f7769L, "onOverScrolled[%d] scrollX %d scrollY %d clampedX %b clampedY %b", Integer.valueOf(getId()), Integer.valueOf(i3), Integer.valueOf(i4), Boolean.valueOf(z3), Boolean.valueOf(z4));
        }
        OverScroller overScroller = this.f7785d;
        if (overScroller != null && !overScroller.isFinished() && this.f7785d.getCurrX() != this.f7785d.getFinalX() && i3 >= (iMax = Math.max(computeHorizontalScrollRange() - getWidth(), 0))) {
            this.f7785d.abortAnimation();
            i3 = iMax;
        }
        super.onOverScrolled(i3, i4, z3, z4);
    }

    @Override // android.view.View
    protected void onScrollChanged(int i3, int i4, int i5, int i6) {
        if (f7768K) {
            Y.a.v(f7769L, "onScrollChanged[%d] x %d y %d oldx %d oldy %d", Integer.valueOf(getId()), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6));
        }
        C0518a.c(0L, "ReactHorizontalScrollView.onScrollChanged");
        try {
            super.onScrollChanged(i3, i4, i5, i6);
            this.f7788g = true;
            if (this.f7784c.c(i3, i4)) {
                if (this.f7794m) {
                    e();
                }
                j.u(this, this.f7784c.a(), this.f7784c.b());
            }
            C0518a.i(0L);
        } catch (Throwable th) {
            C0518a.i(0L);
            throw th;
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    protected void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        if (this.f7794m) {
            e();
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.f7795n || !EnumC0431g0.b(this.f7777E)) {
            return false;
        }
        this.f7786e.a(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 && this.f7791j) {
            j.s(this);
            float fB = this.f7786e.b();
            float fC = this.f7786e.c();
            j.e(this, fB, fC);
            P1.m.a(this, motionEvent);
            this.f7791j = false;
            B(Math.round(fB), Math.round(fC));
        }
        if (actionMasked == 0) {
            q();
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.widget.HorizontalScrollView
    public boolean pageScroll(int i3) {
        boolean zPageScroll = super.pageScroll(i3);
        if (this.f7792k && zPageScroll) {
            B(0, 0);
        }
        return zPageScroll;
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (view2 != null && !this.f7792k) {
            I(view2);
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public void scrollTo(int i3, int i4) {
        if (f7768K) {
            Y.a.u(f7769L, "scrollTo[%d] x %d y %d", Integer.valueOf(getId()), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        super.scrollTo(i3, i4);
        j.s(this);
        K(i3, i4);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i3) {
        C0418a.n(this, Integer.valueOf(i3));
    }

    public void setBorderRadius(float f3) {
        J(f3, R1.d.f1999b.ordinal());
    }

    public void setBorderStyle(String str) {
        C0418a.r(this, str == null ? null : R1.f.b(str));
    }

    public void setDecelerationRate(float f3) {
        getReactScrollViewScrollState().h(f3);
        OverScroller overScroller = this.f7785d;
        if (overScroller != null) {
            overScroller.setFriction(1.0f - f3);
        }
    }

    public void setDisableIntervalMomentum(boolean z3) {
        this.f7800s = z3;
    }

    public void setEndFillColor(int i3) {
        if (i3 != this.f7799r) {
            this.f7799r = i3;
            this.f7798q = new ColorDrawable(this.f7799r);
        }
    }

    @Override // com.facebook.react.views.scroll.j.b
    public void setLastScrollDispatchTime(long j3) {
        this.f7778F = j3;
    }

    public void setMaintainVisibleContentPosition(b.C0115b c0115b) {
        com.facebook.react.views.scroll.b bVar;
        if (c0115b != null && this.f7781I == null) {
            com.facebook.react.views.scroll.b bVar2 = new com.facebook.react.views.scroll.b(this, true);
            this.f7781I = bVar2;
            bVar2.f();
        } else if (c0115b == null && (bVar = this.f7781I) != null) {
            bVar.g();
            this.f7781I = null;
        }
        com.facebook.react.views.scroll.b bVar3 = this.f7781I;
        if (bVar3 != null) {
            bVar3.e(c0115b);
        }
    }

    public void setOverflow(String str) {
        if (str == null) {
            this.f7790i = p.f2098e;
        } else {
            p pVarB = p.b(str);
            if (pVarB == null) {
                pVarB = p.f2098e;
            }
            this.f7790i = pVarB;
        }
        invalidate();
    }

    public void setPagingEnabled(boolean z3) {
        this.f7792k = z3;
    }

    public void setPointerEvents(EnumC0431g0 enumC0431g0) {
        this.f7777E = enumC0431g0;
    }

    public void setRemoveClippedSubviews(boolean z3) {
        if (z3 && this.f7789h == null) {
            this.f7789h = new Rect();
        }
        this.f7794m = z3;
        e();
    }

    public void setScrollEnabled(boolean z3) {
        this.f7795n = z3;
    }

    public void setScrollEventThrottle(int i3) {
        this.f7779G = i3;
    }

    public void setScrollPerfTag(String str) {
        this.f7797p = str;
    }

    public void setSendMomentumEvents(boolean z3) {
        this.f7796o = z3;
    }

    public void setSnapInterval(int i3) {
        this.f7801t = i3;
    }

    public void setSnapOffsets(List<Integer> list) {
        this.f7802u = list;
    }

    public void setSnapToAlignment(int i3) {
        this.f7805x = i3;
    }

    public void setSnapToEnd(boolean z3) {
        this.f7804w = z3;
    }

    public void setSnapToStart(boolean z3) {
        this.f7803v = z3;
    }

    public void setStateWrapper(A0 a02) {
        this.f7774B = a02;
    }

    public void v() {
        awakenScrollBars();
    }

    public int x(int i3) {
        return j.p(this, i3, 0, Math.max(0, computeHorizontalScrollRange() - getWidth()), 0).x;
    }

    public f(Context context, com.facebook.react.views.scroll.a aVar) {
        super(context);
        this.f7783b = f7770M;
        this.f7784c = new c();
        this.f7786e = new m();
        this.f7787f = new Rect();
        this.f7790i = p.f2098e;
        this.f7792k = false;
        this.f7795n = true;
        this.f7799r = 0;
        this.f7800s = false;
        this.f7801t = 0;
        this.f7803v = true;
        this.f7804w = true;
        this.f7805x = 0;
        this.f7806y = false;
        this.f7807z = -1;
        this.f7773A = -1;
        this.f7774B = null;
        this.f7776D = ObjectAnimator.ofInt(this, "scrollX", 0, 0);
        this.f7777E = EnumC0431g0.f7482f;
        this.f7778F = 0L;
        this.f7779G = 0;
        this.f7782J = new Rect();
        Z.X(this, new h());
        this.f7785d = getOverScrollerFromParent();
        this.f7775C = new j.g();
        setOnHierarchyChangeListener(this);
        setClipChildren(false);
    }
}
