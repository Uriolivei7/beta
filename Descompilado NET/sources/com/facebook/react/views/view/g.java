package com.facebook.react.views.view;

import a1.C0210a;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStructure;
import android.view.animation.Animation;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.react.uimanager.C0418a;
import com.facebook.react.uimanager.C0437j0;
import com.facebook.react.uimanager.C0461w;
import com.facebook.react.uimanager.EnumC0431g0;
import com.facebook.react.uimanager.InterfaceC0435i0;
import com.facebook.react.uimanager.InterfaceC0443m0;
import com.facebook.react.uimanager.InterfaceC0445n0;
import com.facebook.react.uimanager.InterfaceC0460v0;
import com.facebook.react.uimanager.P0;
import com.facebook.react.uimanager.X;
import com.facebook.react.uimanager.Z;
import d1.AbstractC0505m;
import d1.W;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class g extends ViewGroup implements K1.d, InterfaceC0435i0, InterfaceC0445n0, K1.c, InterfaceC0460v0, InterfaceC0443m0 {

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private static final ViewGroup.LayoutParams f8169s = new ViewGroup.LayoutParams(0, 0);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Rect f8170b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f8171c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f8172d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private volatile boolean f8173e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View[] f8174f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f8175g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Rect f8176h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private Rect f8177i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private R1.p f8178j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private EnumC0431g0 f8179k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private b f8180l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private K1.b f8181m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f8182n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private P0 f8183o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private float f8184p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private boolean f8185q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private Set f8186r;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f8187a;

        static {
            int[] iArr = new int[R1.p.values().length];
            f8187a = iArr;
            try {
                iArr[R1.p.f2097d.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f8187a[R1.p.f2098e.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f8187a[R1.p.f2096c.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private static final class b implements View.OnLayoutChangeListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private g f8188b;

        public void a() {
            this.f8188b = null;
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            g gVar = this.f8188b;
            if (gVar == null || !gVar.getRemoveClippedSubviews()) {
                return;
            }
            this.f8188b.E(view);
        }

        private b(g gVar) {
            this.f8188b = gVar;
        }
    }

    public g(Context context) {
        super(context);
        this.f8170b = new Rect();
        this.f8171c = 0;
        this.f8179k = EnumC0431g0.f7482f;
        p();
    }

    private void A(int i3) {
        if (this.f8186r == null) {
            this.f8186r = new HashSet();
        }
        this.f8186r.add(Integer.valueOf(i3));
    }

    private void C(Rect rect) {
        C0210a.c(this.f8174f);
        this.f8173e = true;
        int i3 = 0;
        for (int i4 = 0; i4 < this.f8175g; i4++) {
            try {
                D(rect, i4, i3);
                if (r(this.f8174f[i4], Integer.valueOf(i4))) {
                    i3++;
                }
            } catch (IndexOutOfBoundsException e4) {
                HashSet hashSet = new HashSet();
                int i5 = 0;
                for (int i6 = 0; i6 < i4; i6++) {
                    i5 += r(this.f8174f[i6], null) ? 1 : 0;
                    hashSet.add(this.f8174f[i6]);
                }
                throw new IllegalStateException("Invalid clipping state. i=" + i4 + " clippedSoFar=" + i3 + " count=" + getChildCount() + " allChildrenCount=" + this.f8175g + " recycleCount=" + this.f8171c + " realClippedSoFar=" + i5 + " uniqueViewsCount=" + hashSet.size(), e4);
            }
        }
        this.f8173e = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void D(Rect rect, int i3, int i4) {
        UiThreadUtil.assertOnUiThread();
        GLSurfaceView gLSurfaceView = ((View[]) C0210a.c(this.f8174f))[i3];
        boolean zIntersects = rect.intersects(gLSurfaceView.getLeft(), gLSurfaceView.getTop(), gLSurfaceView.getRight(), gLSurfaceView.getBottom());
        Animation animation = gLSurfaceView.getAnimation();
        boolean z3 = (animation == null || animation.hasEnded()) ? false : true;
        if (!zIntersects && !r(gLSurfaceView, Integer.valueOf(i3)) && !z3) {
            z(gLSurfaceView, true);
            removeViewInLayout(gLSurfaceView);
        } else if (zIntersects && r(gLSurfaceView, Integer.valueOf(i3))) {
            int i5 = i3 - i4;
            C0210a.a(i5 >= 0);
            z(gLSurfaceView, false);
            addViewInLayout(gLSurfaceView, i5, f8169s, true);
            invalidate();
        } else if (!zIntersects) {
            return;
        }
        if (gLSurfaceView instanceof InterfaceC0435i0) {
            InterfaceC0435i0 interfaceC0435i0 = (InterfaceC0435i0) gLSurfaceView;
            if (interfaceC0435i0.getRemoveClippedSubviews()) {
                interfaceC0435i0.e();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E(View view) {
        if (!this.f8172d || getParent() == null) {
            return;
        }
        C0210a.c(this.f8176h);
        C0210a.c(this.f8174f);
        if (this.f8176h.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) != (!r(view, null))) {
            this.f8173e = true;
            int i3 = 0;
            int i4 = 0;
            while (true) {
                if (i3 >= this.f8175g) {
                    break;
                }
                View view2 = this.f8174f[i3];
                if (view2 == view) {
                    D(this.f8176h, i3, i4);
                    break;
                } else {
                    if (r(view2, Integer.valueOf(i3))) {
                        i4++;
                    }
                    i3++;
                }
            }
            this.f8173e = false;
        }
    }

    private P0 getDrawingOrderHelper() {
        if (this.f8183o == null) {
            this.f8183o = new P0(this);
        }
        return this.f8183o;
    }

    private void i(View view, int i3) {
        View[] viewArr = (View[]) C0210a.c(this.f8174f);
        int i4 = this.f8175g;
        int length = viewArr.length;
        if (i3 == i4) {
            if (length == i4) {
                View[] viewArr2 = new View[length + 12];
                this.f8174f = viewArr2;
                System.arraycopy(viewArr, 0, viewArr2, 0, length);
                viewArr = this.f8174f;
            }
            int i5 = this.f8175g;
            this.f8175g = i5 + 1;
            viewArr[i5] = view;
            return;
        }
        if (i3 >= i4) {
            throw new IndexOutOfBoundsException("index=" + i3 + " count=" + i4);
        }
        if (length == i4) {
            View[] viewArr3 = new View[length + 12];
            this.f8174f = viewArr3;
            System.arraycopy(viewArr, 0, viewArr3, 0, i3);
            System.arraycopy(viewArr, i3, this.f8174f, i3 + 1, i4 - i3);
            viewArr = this.f8174f;
        } else {
            System.arraycopy(viewArr, i3, viewArr, i3 + 1, i4 - i3);
        }
        viewArr[i3] = view;
        this.f8175g++;
    }

    private void l(View view, Boolean bool) {
        if (this.f8173e) {
            Object tag = view.getTag(AbstractC0505m.f9226D);
            if (!bool.equals(tag)) {
                ReactSoftExceptionLogger.logSoftException(ReactSoftExceptionLogger.Categories.RVG_ON_VIEW_REMOVED, new ReactNoCrashSoftException("View clipping tag mismatch: tag=" + tag + " expected=" + bool));
            }
        }
        if (this.f8172d) {
            view.setTag(AbstractC0505m.f9226D, bool);
        }
    }

    private boolean m() {
        return getId() != -1 && M1.a.a(getId()) == 2;
    }

    private int o(View view) {
        int i3 = this.f8175g;
        View[] viewArr = (View[]) C0210a.c(this.f8174f);
        for (int i4 = 0; i4 < i3; i4++) {
            if (viewArr[i4] == view) {
                return i4;
            }
        }
        return -1;
    }

    private void p() {
        setClipChildren(false);
        this.f8172d = false;
        this.f8173e = false;
        this.f8174f = null;
        this.f8175g = 0;
        this.f8176h = null;
        this.f8177i = null;
        this.f8178j = R1.p.f2096c;
        this.f8179k = EnumC0431g0.f7482f;
        this.f8180l = null;
        this.f8181m = null;
        this.f8182n = false;
        this.f8183o = null;
        this.f8184p = 1.0f;
        this.f8185q = true;
        this.f8186r = null;
    }

    private boolean q(View view) {
        Set set = this.f8186r;
        return set != null && set.contains(Integer.valueOf(view.getId()));
    }

    private boolean r(View view, Integer num) {
        Object tag = view.getTag(AbstractC0505m.f9226D);
        if (tag != null) {
            return ((Boolean) tag).booleanValue();
        }
        ViewParent parent = view.getParent();
        boolean zQ = q(view);
        if (num != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("View missing clipping tag: index=");
            sb.append(num);
            sb.append(" parentNull=");
            sb.append(parent == null);
            sb.append(" parentThis=");
            sb.append(parent == this);
            sb.append(" transitioning=");
            sb.append(zQ);
            ReactSoftExceptionLogger.logSoftException(ReactSoftExceptionLogger.Categories.RVG_IS_VIEW_CLIPPED, new ReactNoCrashSoftException(sb.toString()));
        }
        if (parent == null || zQ) {
            return true;
        }
        C0210a.a(parent == this);
        return false;
    }

    private void u(int i3) {
        View[] viewArr = (View[]) C0210a.c(this.f8174f);
        int i4 = this.f8175g;
        if (i3 == i4 - 1) {
            int i5 = i4 - 1;
            this.f8175g = i5;
            viewArr[i5] = null;
        } else {
            if (i3 < 0 || i3 >= i4) {
                throw new IndexOutOfBoundsException();
            }
            System.arraycopy(viewArr, i3 + 1, viewArr, i3, (i4 - i3) - 1);
            int i6 = this.f8175g - 1;
            this.f8175g = i6;
            viewArr[i6] = null;
        }
    }

    private static void z(View view, boolean z3) {
        view.setTag(AbstractC0505m.f9226D, Boolean.valueOf(z3));
    }

    void B(Drawable drawable) {
        super.setBackground(drawable);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0460v0
    public int a(int i3) {
        UiThreadUtil.assertOnUiThread();
        return (m() || !getDrawingOrderHelper().d()) ? i3 : getDrawingOrderHelper().a(getChildCount(), i3);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0443m0
    public void d(int i3, int i4, int i5, int i6) {
        if (C0461w.a(this)) {
            Rect rect = this.f8170b;
            if (rect.left != i3 || rect.top != i4 || rect.right != i5 || rect.bottom != i6) {
                invalidate();
            }
        }
        this.f8170b.set(i3, i4, i5, i6);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.f8178j != R1.p.f2096c || getTag(AbstractC0505m.f9241n) != null) {
            C0418a.a(this, canvas);
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (EnumC0431g0.c(this.f8179k)) {
            return super.dispatchGenericMotionEvent(motionEvent);
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchProvideStructure(ViewStructure viewStructure) {
        try {
            super.dispatchProvideStructure(viewStructure);
        } catch (NullPointerException e4) {
            Y.a.n("ReactNative", "NullPointerException when executing dispatchProvideStructure", e4);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSetPressed(boolean z3) {
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT < 29 || M1.a.c(this) != 2 || !C0461w.a(this)) {
            super.draw(canvas);
            return;
        }
        Rect overflowInset = getOverflowInset();
        canvas.saveLayer(overflowInset.left, overflowInset.top, getWidth() + (-overflowInset.right), getHeight() + (-overflowInset.bottom), null);
        super.draw(canvas);
        canvas.restore();
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j3) {
        BlendMode blendModeA;
        boolean z3 = view.getElevation() > 0.0f;
        if (z3) {
            c.a(canvas, true);
        }
        if (Build.VERSION.SDK_INT >= 29 && M1.a.c(this) == 2 && C0461w.a(this)) {
            blendModeA = W.a(view.getTag(AbstractC0505m.f9245r));
            if (blendModeA != null) {
                Paint paint = new Paint();
                paint.setBlendMode(blendModeA);
                Rect overflowInset = getOverflowInset();
                canvas.saveLayer(overflowInset.left, overflowInset.top, getWidth() + (-overflowInset.right), getHeight() + (-overflowInset.bottom), paint);
            }
        } else {
            blendModeA = null;
        }
        boolean zDrawChild = super.drawChild(canvas, view, j3);
        if (blendModeA != null) {
            canvas.restore();
        }
        if (z3) {
            c.a(canvas, false);
        }
        return zDrawChild;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public void e() {
        if (this.f8172d) {
            C0210a.c(this.f8176h);
            C0210a.c(this.f8174f);
            C0437j0.a(this, this.f8176h);
            C(this.f8176h);
        }
    }

    @Override // android.view.ViewGroup
    public void endViewTransition(View view) {
        super.endViewTransition(view);
        Set set = this.f8186r;
        if (set != null) {
            set.remove(Integer.valueOf(view.getId()));
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0460v0
    public void f() {
        if (m()) {
            return;
        }
        getDrawingOrderHelper().e();
        setChildrenDrawingOrderEnabled(getDrawingOrderHelper().d());
        invalidate();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0435i0
    public void g(Rect rect) {
        rect.set((Rect) C0210a.f(this.f8176h, "Fix in Kotlin"));
    }

    int getAllChildrenCount() {
        return this.f8175g;
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i3, int i4) {
        UiThreadUtil.assertOnUiThread();
        return !m() ? getDrawingOrderHelper().a(i3, i4) : i4;
    }

    @Override // K1.c
    public Rect getHitSlopRect() {
        return this.f8177i;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0441l0
    public String getOverflow() {
        int i3 = a.f8187a[this.f8178j.ordinal()];
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
        return this.f8170b;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0445n0
    public EnumC0431g0 getPointerEvents() {
        return this.f8179k;
    }

    public boolean getRemoveClippedSubviews() {
        return this.f8172d;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return this.f8182n;
    }

    void j(View view, int i3) {
        k(view, i3, f8169s);
    }

    void k(View view, int i3, ViewGroup.LayoutParams layoutParams) {
        C0210a.a(this.f8172d);
        z(view, true);
        i(view, i3);
        Rect rect = (Rect) C0210a.c(this.f8176h);
        View[] viewArr = (View[]) C0210a.c(this.f8174f);
        this.f8173e = true;
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            if (r(viewArr[i5], Integer.valueOf(i5))) {
                i4++;
            }
        }
        D(rect, i3, i4);
        this.f8173e = false;
        view.addOnLayoutChangeListener(this.f8180l);
    }

    View n(int i3) {
        if (i3 < 0 || i3 >= this.f8175g) {
            return null;
        }
        return ((View[]) C0210a.c(this.f8174f))[i3];
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f8172d) {
            e();
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        return ReactFeatureFlags.dispatchPointerEvents ? EnumC0431g0.b(this.f8179k) : super.onHoverEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        K1.b bVar = this.f8181m;
        if ((bVar == null || !bVar.a(this, motionEvent)) && EnumC0431g0.c(this.f8179k)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
    }

    @Override // android.view.View
    protected void onMeasure(int i3, int i4) {
        Z.a(i3, i4);
        setMeasuredDimension(View.MeasureSpec.getSize(i3), View.MeasureSpec.getSize(i4));
    }

    @Override // android.view.View
    protected void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        if (this.f8172d) {
            e();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return EnumC0431g0.b(this.f8179k);
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        UiThreadUtil.assertOnUiThread();
        l(view, Boolean.FALSE);
        if (m()) {
            setChildrenDrawingOrderEnabled(false);
        } else {
            getDrawingOrderHelper().b(view);
            setChildrenDrawingOrderEnabled(getDrawingOrderHelper().d());
        }
        super.onViewAdded(view);
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        UiThreadUtil.assertOnUiThread();
        l(view, Boolean.TRUE);
        if (m()) {
            setChildrenDrawingOrderEnabled(false);
        } else {
            if (indexOfChild(view) == -1) {
                return;
            }
            getDrawingOrderHelper().c(view);
            setChildrenDrawingOrderEnabled(getDrawingOrderHelper().d());
        }
        if (view.getParent() != null) {
            A(view.getId());
        }
        super.onViewRemoved(view);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
    }

    void s() {
        b bVar;
        this.f8171c++;
        if (this.f8174f != null && (bVar = this.f8180l) != null) {
            bVar.a();
            for (int i3 = 0; i3 < this.f8175g; i3++) {
                this.f8174f[i3].removeOnLayoutChangeListener(this.f8180l);
            }
        }
        p();
        this.f8170b.setEmpty();
        removeAllViews();
        B(null);
        w();
    }

    public void setBackfaceVisibility(String str) {
        this.f8185q = "visible".equals(str);
        x();
    }

    @Override // android.view.View
    public void setBackgroundColor(int i3) {
        C0418a.n(this, Integer.valueOf(i3));
    }

    @Deprecated(forRemoval = true, since = "0.75.0")
    public void setBorderRadius(float f3) {
        y(f3, R1.d.f1999b.ordinal());
    }

    public void setBorderStyle(String str) {
        C0418a.r(this, str == null ? null : R1.f.b(str));
    }

    public void setHitSlopRect(Rect rect) {
        this.f8177i = rect;
    }

    public void setNeedsOffscreenAlphaCompositing(boolean z3) {
        this.f8182n = z3;
    }

    @Override // K1.d
    public void setOnInterceptTouchEventListener(K1.b bVar) {
        this.f8181m = bVar;
    }

    public void setOpacityIfPossible(float f3) {
        this.f8184p = f3;
        x();
    }

    public void setOverflow(String str) {
        if (str == null) {
            this.f8178j = R1.p.f2096c;
        } else {
            R1.p pVarB = R1.p.b(str);
            if (pVarB == null) {
                pVarB = R1.p.f2096c;
            }
            this.f8178j = pVarB;
        }
        invalidate();
    }

    public void setPointerEvents(EnumC0431g0 enumC0431g0) {
        this.f8179k = enumC0431g0;
    }

    public void setRemoveClippedSubviews(boolean z3) {
        if (z3 == this.f8172d) {
            return;
        }
        this.f8172d = z3;
        this.f8186r = null;
        if (!z3) {
            C0210a.c(this.f8176h);
            C0210a.c(this.f8174f);
            C0210a.c(this.f8180l);
            for (int i3 = 0; i3 < this.f8175g; i3++) {
                this.f8174f[i3].removeOnLayoutChangeListener(this.f8180l);
            }
            getDrawingRect(this.f8176h);
            C(this.f8176h);
            this.f8174f = null;
            this.f8176h = null;
            this.f8175g = 0;
            this.f8180l = null;
            return;
        }
        Rect rect = new Rect();
        this.f8176h = rect;
        C0437j0.a(this, rect);
        int childCount = getChildCount();
        this.f8175g = childCount;
        this.f8174f = new View[Math.max(12, childCount)];
        this.f8180l = new b();
        for (int i4 = 0; i4 < this.f8175g; i4++) {
            View childAt = getChildAt(i4);
            this.f8174f[i4] = childAt;
            childAt.addOnLayoutChangeListener(this.f8180l);
            z(childAt, false);
        }
        e();
    }

    @Deprecated(forRemoval = true, since = "0.76.0")
    public void setTranslucentBackgroundDrawable(Drawable drawable) {
        C0418a.v(this, drawable);
    }

    void t() {
        C0210a.a(this.f8172d);
        View[] viewArr = (View[]) C0210a.c(this.f8174f);
        for (int i3 = 0; i3 < this.f8175g; i3++) {
            viewArr[i3].removeOnLayoutChangeListener(this.f8180l);
        }
        removeAllViewsInLayout();
        this.f8175g = 0;
    }

    void v(View view) {
        UiThreadUtil.assertOnUiThread();
        C0210a.a(this.f8172d);
        C0210a.c(this.f8176h);
        View[] viewArr = (View[]) C0210a.c(this.f8174f);
        view.removeOnLayoutChangeListener(this.f8180l);
        int iO = o(view);
        if (!r(viewArr[iO], Integer.valueOf(iO))) {
            int i3 = 0;
            for (int i4 = 0; i4 < iO; i4++) {
                if (r(viewArr[i4], Integer.valueOf(i4))) {
                    i3++;
                }
            }
            removeViewsInLayout(iO - i3, 1);
            invalidate();
        }
        u(iO);
    }

    void w() {
        this.f8179k = EnumC0431g0.f7482f;
    }

    public void x() {
        if (this.f8185q) {
            setAlpha(this.f8184p);
            return;
        }
        float rotationX = getRotationX();
        float rotationY = getRotationY();
        if (rotationX < -90.0f || rotationX >= 90.0f || rotationY < -90.0f || rotationY >= 90.0f) {
            setAlpha(0.0f);
        } else {
            setAlpha(this.f8184p);
        }
    }

    public void y(float f3, int i3) {
        C0418a.q(this, R1.d.values()[i3], Float.isNaN(f3) ? null : new com.facebook.react.uimanager.W(f3, X.f7408b));
    }
}
