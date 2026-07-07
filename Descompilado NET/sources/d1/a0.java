package d1;

import a1.C0210a;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.DisplayCutout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.react.modules.appregistry.AppRegistry;
import com.facebook.react.modules.deviceinfo.DeviceInfoModule;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.C0449p0;
import com.facebook.react.uimanager.C0461w;
import com.facebook.react.uimanager.C0463x;
import com.facebook.react.uimanager.C0464x0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.InterfaceC0447o0;
import com.facebook.react.uimanager.InterfaceC0462w0;
import com.facebook.react.uimanager.events.EventDispatcher;
import d2.C0518a;
import java.util.concurrent.atomic.AtomicInteger;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
public class a0 extends FrameLayout implements InterfaceC0462w0, InterfaceC0447o0 {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private J f9172b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f9173c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Bundle f9174d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private a f9175e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f9176f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f9177g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f9178h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private com.facebook.react.uimanager.S f9179i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private com.facebook.react.uimanager.Q f9180j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final C0515x f9181k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f9182l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f9183m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f9184n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f9185o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f9186p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f9187q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f9188r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f9189s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final AtomicInteger f9190t;

    private class a implements ViewTreeObserver.OnGlobalLayoutListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Rect f9191b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f9192c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f9193d = false;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f9194e = 0;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private int f9195f = 0;

        a() {
            C0463x.f(a0.this.getContext().getApplicationContext());
            this.f9191b = new Rect();
            this.f9192c = (int) C0429f0.h(60.0f);
        }

        private void a() {
            g();
        }

        private void b() {
            int rotation = ((WindowManager) a0.this.getContext().getSystemService("window")).getDefaultDisplay().getRotation();
            if (this.f9195f == rotation) {
                return;
            }
            this.f9195f = rotation;
            C0463x.e(a0.this.getContext().getApplicationContext());
            f(rotation);
        }

        private void c() {
            boolean zIsVisible;
            a0.this.getRootView().getWindowVisibleDisplayFrame(this.f9191b);
            WindowInsets rootWindowInsets = a0.this.getRootView().getRootWindowInsets();
            if (rootWindowInsets == null || (zIsVisible = rootWindowInsets.isVisible(WindowInsets.Type.ime())) == this.f9193d) {
                return;
            }
            this.f9193d = zIsVisible;
            if (!zIsVisible) {
                a0.this.r("keyboardDidHide", e(C0429f0.f(this.f9191b.height()), 0.0d, C0429f0.f(this.f9191b.width()), 0.0d));
                return;
            }
            int i3 = rootWindowInsets.getInsets(WindowInsets.Type.ime()).bottom - rootWindowInsets.getInsets(WindowInsets.Type.systemBars()).bottom;
            ViewGroup.LayoutParams layoutParams = a0.this.getRootView().getLayoutParams();
            C0210a.a(layoutParams instanceof WindowManager.LayoutParams);
            a0.this.r("keyboardDidShow", e(C0429f0.f(((WindowManager.LayoutParams) layoutParams).softInputMode == 48 ? this.f9191b.bottom - i3 : this.f9191b.bottom), C0429f0.f(this.f9191b.left), C0429f0.f(this.f9191b.width()), C0429f0.f(i3)));
        }

        private void d() {
            WindowInsets rootWindowInsets;
            DisplayCutout displayCutout;
            a0.this.getRootView().getWindowVisibleDisplayFrame(this.f9191b);
            int safeInsetTop = (Build.VERSION.SDK_INT < 28 || (rootWindowInsets = a0.this.getRootView().getRootWindowInsets()) == null || (displayCutout = rootWindowInsets.getDisplayCutout()) == null) ? 0 : displayCutout.getSafeInsetTop();
            int i3 = (C0463x.d().heightPixels - this.f9191b.bottom) + safeInsetTop;
            int i4 = this.f9194e;
            if (i4 != i3 && i3 > this.f9192c) {
                this.f9194e = i3;
                this.f9193d = true;
                a0.this.r("keyboardDidShow", e(C0429f0.f(r4), C0429f0.f(this.f9191b.left), C0429f0.f(this.f9191b.width()), C0429f0.f(this.f9194e)));
            } else {
                if (i4 == 0 || i3 > this.f9192c) {
                    return;
                }
                this.f9194e = 0;
                this.f9193d = false;
                a0.this.r("keyboardDidHide", e(C0429f0.f(r3.height()), 0.0d, C0429f0.f(this.f9191b.width()), 0.0d));
            }
        }

        private WritableMap e(double d4, double d5, double d6, double d7) {
            WritableMap writableMapCreateMap = Arguments.createMap();
            WritableMap writableMapCreateMap2 = Arguments.createMap();
            writableMapCreateMap2.putDouble("height", d7);
            writableMapCreateMap2.putDouble("screenX", d5);
            writableMapCreateMap2.putDouble("width", d6);
            writableMapCreateMap2.putDouble("screenY", d4);
            writableMapCreateMap.putMap("endCoordinates", writableMapCreateMap2);
            writableMapCreateMap.putString("easing", "keyboard");
            writableMapCreateMap.putDouble("duration", 0.0d);
            return writableMapCreateMap;
        }

        private void f(int i3) {
            String str;
            double d4;
            boolean z3 = false;
            if (i3 != 0) {
                if (i3 == 1) {
                    str = "landscape-primary";
                    d4 = -90.0d;
                } else if (i3 == 2) {
                    str = "portrait-secondary";
                    d4 = 180.0d;
                } else {
                    if (i3 != 3) {
                        return;
                    }
                    str = "landscape-secondary";
                    d4 = 90.0d;
                }
                z3 = true;
            } else {
                str = "portrait-primary";
                d4 = 0.0d;
            }
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putString("name", str);
            writableMapCreateMap.putDouble("rotationDegrees", d4);
            writableMapCreateMap.putBoolean("isLandscape", z3);
            a0.this.r("namedOrientationDidChange", writableMapCreateMap);
        }

        private void g() {
            DeviceInfoModule deviceInfoModule;
            ReactContext currentReactContext = a0.this.getCurrentReactContext();
            if (currentReactContext == null || (deviceInfoModule = (DeviceInfoModule) currentReactContext.getNativeModule(DeviceInfoModule.class)) == null) {
                return;
            }
            deviceInfoModule.emitUpdateDimensionsEvent();
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (a0.this.i() && a0.this.o()) {
                if (Build.VERSION.SDK_INT >= 30) {
                    c();
                } else {
                    d();
                }
                b();
                a();
            }
        }
    }

    public interface b {
    }

    public a0(Context context) {
        super(context);
        this.f9176f = 0;
        this.f9181k = new C0515x(this);
        this.f9182l = false;
        this.f9183m = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.f9184n = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.f9185o = 0;
        this.f9186p = 0;
        this.f9187q = Integer.MIN_VALUE;
        this.f9188r = Integer.MIN_VALUE;
        this.f9189s = 1;
        this.f9190t = new AtomicInteger(0);
        k();
    }

    private void e() {
        C0518a.c(0L, "attachToReactInstanceManager");
        ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_ATTACH_TO_REACT_INSTANCE_MANAGER_START);
        if (getId() != -1) {
            ReactSoftExceptionLogger.logSoftException("ReactRootView", new com.facebook.react.uimanager.P("Trying to attach a ReactRootView with an explicit id already set to [" + getId() + "]. React Native uses the id field to track react tags and will overwrite this field. If that is fine, explicitly overwrite the id field to View.NO_ID."));
        }
        try {
            if (this.f9177g) {
                return;
            }
            this.f9177g = true;
            ((J) C0210a.c(this.f9172b)).s(this);
            getViewTreeObserver().addOnGlobalLayoutListener(getCustomGlobalLayoutListener());
        } finally {
            ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_ATTACH_TO_REACT_INSTANCE_MANAGER_END);
            C0518a.i(0L);
        }
    }

    private a getCustomGlobalLayoutListener() {
        if (this.f9175e == null) {
            this.f9175e = new a();
        }
        return this.f9175e;
    }

    private void k() {
        setRootViewTag(C0449p0.a());
        setClipChildren(false);
    }

    private boolean l() {
        if (!i() || !o()) {
            Y.a.I("ReactRootView", "Unable to dispatch touch to JS as the catalyst instance has not been attached");
            return false;
        }
        if (this.f9179i == null) {
            Y.a.I("ReactRootView", "Unable to dispatch touch to JS before the dispatcher is available");
            return false;
        }
        if (!ReactFeatureFlags.dispatchPointerEvents || this.f9180j != null) {
            return true;
        }
        Y.a.I("ReactRootView", "Unable to dispatch pointer events to JS before the dispatcher is available");
        return false;
    }

    private boolean m() {
        return getUIManagerType() == 2;
    }

    private boolean n() {
        int i3 = this.f9176f;
        return (i3 == 0 || i3 == -1) ? false : true;
    }

    private void q() {
        getViewTreeObserver().removeOnGlobalLayoutListener(getCustomGlobalLayoutListener());
    }

    private void s() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        this.f9183m = View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE);
        this.f9184n = View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE);
    }

    private void w(boolean z3, int i3, int i4) {
        UIManager uIManagerG;
        int i5;
        int i6;
        ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_UPDATE_LAYOUT_SPECS_START);
        if (!j()) {
            ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_UPDATE_LAYOUT_SPECS_END);
            Y.a.I("ReactRootView", "Unable to update root layout specs for uninitialized ReactInstanceManager");
            return;
        }
        boolean zM = m();
        if (zM && !n()) {
            ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_UPDATE_LAYOUT_SPECS_END);
            Y.a.m("ReactRootView", "Unable to update root layout specs for ReactRootView: no rootViewTag set yet");
            return;
        }
        ReactContext currentReactContext = getCurrentReactContext();
        if (currentReactContext != null && (uIManagerG = H0.g(currentReactContext, getUIManagerType())) != null) {
            if (zM) {
                Point pointB = C0464x0.b(this);
                i5 = pointB.x;
                i6 = pointB.y;
            } else {
                i5 = 0;
                i6 = 0;
            }
            if (z3 || i5 != this.f9187q || i6 != this.f9188r) {
                uIManagerG.updateRootLayoutSpecs(getRootViewTag(), i3, i4, i5, i6);
            }
            this.f9187q = i5;
            this.f9188r = i6;
        }
        ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_UPDATE_LAYOUT_SPECS_END);
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public void a(int i3) {
        if (i3 != 101) {
            return;
        }
        p();
    }

    public void b(View view, MotionEvent motionEvent) {
        EventDispatcher eventDispatcherB;
        if (l() && (eventDispatcherB = H0.b(getCurrentReactContext(), getUIManagerType())) != null) {
            this.f9179i.e(motionEvent, eventDispatcherB);
            com.facebook.react.uimanager.Q q3 = this.f9180j;
            if (q3 != null) {
                q3.o();
            }
        }
    }

    public void c(View view, MotionEvent motionEvent) {
        EventDispatcher eventDispatcherB;
        com.facebook.react.uimanager.Q q3;
        if (l() && (eventDispatcherB = H0.b(getCurrentReactContext(), getUIManagerType())) != null) {
            this.f9179i.f(motionEvent, eventDispatcherB);
            if (view == null || (q3 = this.f9180j) == null) {
                return;
            }
            q3.p(view, motionEvent, eventDispatcherB);
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public void d() {
        C0518a.c(0L, "ReactRootView.runApplication");
        try {
            if (j() && o()) {
                ReactContext currentReactContext = getCurrentReactContext();
                if (currentReactContext == null) {
                    C0518a.i(0L);
                    return;
                }
                CatalystInstance catalystInstance = currentReactContext.getCatalystInstance();
                String jSModuleName = getJSModuleName();
                if (this.f9182l) {
                    w(true, this.f9183m, this.f9184n);
                }
                WritableNativeMap writableNativeMap = new WritableNativeMap();
                writableNativeMap.putDouble("rootTag", getRootViewTag());
                Bundle appProperties = getAppProperties();
                if (appProperties != null) {
                    writableNativeMap.putMap("initialProps", Arguments.fromBundle(appProperties));
                }
                this.f9178h = true;
                ((AppRegistry) catalystInstance.getJSModule(AppRegistry.class)).runApplication(jSModuleName, writableNativeMap);
                C0518a.i(0L);
            }
        } finally {
            C0518a.i(0L);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (StackOverflowError e4) {
            h(e4);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (i() && o()) {
            this.f9181k.d(keyEvent);
            return super.dispatchKeyEvent(keyEvent);
        }
        Y.a.I("ReactRootView", "Unable to handle key event as the catalyst instance has not been attached");
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j3) {
        BlendMode blendModeA;
        if (Build.VERSION.SDK_INT >= 29 && M1.a.c(this) == 2 && C0461w.a(this)) {
            blendModeA = W.a(view.getTag(AbstractC0505m.f9245r));
            if (blendModeA != null) {
                Paint paint = new Paint();
                paint.setBlendMode(blendModeA);
                canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), paint);
            }
        } else {
            blendModeA = null;
        }
        boolean zDrawChild = super.drawChild(canvas, view, j3);
        if (blendModeA != null) {
            canvas.restore();
        }
        return zDrawChild;
    }

    protected void f(MotionEvent motionEvent, boolean z3) {
        if (!i() || !o()) {
            Y.a.I("ReactRootView", "Unable to dispatch touch to JS as the catalyst instance has not been attached");
            return;
        }
        if (this.f9180j == null) {
            if (ReactFeatureFlags.dispatchPointerEvents) {
                Y.a.I("ReactRootView", "Unable to dispatch pointer events to JS before the dispatcher is available");
            }
        } else {
            EventDispatcher eventDispatcherB = H0.b(getCurrentReactContext(), getUIManagerType());
            if (eventDispatcherB != null) {
                this.f9180j.k(motionEvent, eventDispatcherB, z3);
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        C0210a.b(!this.f9177g, "The application this ReactRootView was rendering was not unmounted before the ReactRootView was garbage collected. This usually means that your application is leaking large amounts of memory. To solve this, make sure to call ReactRootView#unmountReactApplication in the onDestroy() of your hosting Activity or in the onDestroyView() of your hosting Fragment.");
    }

    protected void g(MotionEvent motionEvent) {
        if (!i() || !o()) {
            Y.a.I("ReactRootView", "Unable to dispatch touch to JS as the catalyst instance has not been attached");
            return;
        }
        if (this.f9179i == null) {
            Y.a.I("ReactRootView", "Unable to dispatch touch to JS before the dispatcher is available");
            return;
        }
        EventDispatcher eventDispatcherB = H0.b(getCurrentReactContext(), getUIManagerType());
        if (eventDispatcherB != null) {
            this.f9179i.c(motionEvent, eventDispatcherB, getCurrentReactContext());
        }
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public Bundle getAppProperties() {
        return this.f9174d;
    }

    public ReactContext getCurrentReactContext() {
        J j3 = this.f9172b;
        if (j3 == null) {
            return null;
        }
        return j3.C();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public int getHeightMeasureSpec() {
        return this.f9184n;
    }

    public String getJSModuleName() {
        return (String) C0210a.c(this.f9173c);
    }

    public J getReactInstanceManager() {
        return this.f9172b;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public int getRootViewTag() {
        return this.f9176f;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public AtomicInteger getState() {
        return this.f9190t;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public String getSurfaceID() {
        Bundle appProperties = getAppProperties();
        if (appProperties != null) {
            return appProperties.getString("surfaceID");
        }
        return null;
    }

    public int getUIManagerType() {
        return this.f9189s;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public int getWidthMeasureSpec() {
        return this.f9183m;
    }

    public void h(Throwable th) {
        if (!i()) {
            throw new RuntimeException(th);
        }
        getCurrentReactContext().handleException(new com.facebook.react.uimanager.P(th.getMessage(), this, th));
    }

    public boolean i() {
        J j3 = this.f9172b;
        return (j3 == null || j3.C() == null) ? false : true;
    }

    public boolean j() {
        return this.f9172b != null;
    }

    public boolean o() {
        return this.f9177g;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (o()) {
            q();
            getViewTreeObserver().addOnGlobalLayoutListener(getCustomGlobalLayoutListener());
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (o()) {
            q();
        }
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean z3, int i3, Rect rect) {
        if (i() && o()) {
            this.f9181k.a();
            super.onFocusChanged(z3, i3, rect);
        } else {
            Y.a.I("ReactRootView", "Unable to handle focus changed event as the catalyst instance has not been attached");
            super.onFocusChanged(z3, i3, rect);
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        f(motionEvent, false);
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
        f(motionEvent, true);
        return super.onInterceptHoverEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (t(motionEvent)) {
            g(motionEvent);
        }
        f(motionEvent, true);
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        if (this.f9182l && m()) {
            w(false, this.f9183m, this.f9184n);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i3, int i4) {
        int iMax;
        int iMax2;
        C0518a.c(0L, "ReactRootView.onMeasure");
        ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_ON_MEASURE_START);
        try {
            boolean z3 = (i3 == this.f9183m && i4 == this.f9184n) ? false : true;
            this.f9183m = i3;
            this.f9184n = i4;
            int mode = View.MeasureSpec.getMode(i3);
            if (mode == Integer.MIN_VALUE || mode == 0) {
                iMax = 0;
                for (int i5 = 0; i5 < getChildCount(); i5++) {
                    View childAt = getChildAt(i5);
                    iMax = Math.max(iMax, childAt.getLeft() + childAt.getMeasuredWidth() + childAt.getPaddingLeft() + childAt.getPaddingRight());
                }
            } else {
                iMax = View.MeasureSpec.getSize(i3);
            }
            int mode2 = View.MeasureSpec.getMode(i4);
            if (mode2 == Integer.MIN_VALUE || mode2 == 0) {
                iMax2 = 0;
                for (int i6 = 0; i6 < getChildCount(); i6++) {
                    View childAt2 = getChildAt(i6);
                    iMax2 = Math.max(iMax2, childAt2.getTop() + childAt2.getMeasuredHeight() + childAt2.getPaddingTop() + childAt2.getPaddingBottom());
                }
            } else {
                iMax2 = View.MeasureSpec.getSize(i4);
            }
            setMeasuredDimension(iMax, iMax2);
            this.f9182l = true;
            if (j() && !o()) {
                e();
            } else if (z3 || this.f9185o != iMax || this.f9186p != iMax2) {
                w(true, this.f9183m, this.f9184n);
            }
            this.f9185o = iMax;
            this.f9186p = iMax2;
            ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_ON_MEASURE_END);
            C0518a.i(0L);
        } catch (Throwable th) {
            ReactMarker.logMarker(ReactMarkerConstants.ROOT_VIEW_ON_MEASURE_END);
            C0518a.i(0L);
            throw th;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (t(motionEvent)) {
            g(motionEvent);
        }
        f(motionEvent, false);
        super.onTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (this.f9178h) {
            this.f9178h = false;
            ReactMarker.logMarker(ReactMarkerConstants.CONTENT_APPEARED, getJSModuleName(), this.f9176f);
        }
    }

    public void p() {
        this.f9179i = new com.facebook.react.uimanager.S(this);
        if (ReactFeatureFlags.dispatchPointerEvents) {
            this.f9180j = new com.facebook.react.uimanager.Q(this);
        }
    }

    void r(String str, WritableMap writableMap) {
        if (j()) {
            getCurrentReactContext().emitDeviceEvent(str, writableMap);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (i() && o()) {
            this.f9181k.e(view2);
            super.requestChildFocus(view, view2);
        } else {
            Y.a.I("ReactRootView", "Unable to handle child focus changed event as the catalyst instance has not been attached");
            super.requestChildFocus(view, view2);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z3) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(z3);
        }
    }

    public void setAppProperties(Bundle bundle) {
        UiThreadUtil.assertOnUiThread();
        this.f9174d = bundle;
        if (n()) {
            d();
        }
    }

    public void setIsFabric(boolean z3) {
        this.f9189s = z3 ? 2 : 1;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public void setRootViewTag(int i3) {
        this.f9176f = i3;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public void setShouldLogContentAppeared(boolean z3) {
        this.f9178h = z3;
    }

    public boolean t(MotionEvent motionEvent) {
        return true;
    }

    public void u(J j3, String str, Bundle bundle) {
        C0518a.c(0L, "startReactApplication");
        try {
            UiThreadUtil.assertOnUiThread();
            C0210a.b(this.f9172b == null, "This root view has already been attached to a catalyst instance manager");
            this.f9172b = j3;
            this.f9173c = str;
            this.f9174d = bundle;
            j3.y();
            if (C0670b.d()) {
                if (!this.f9182l) {
                    s();
                }
                e();
            }
            C0518a.i(0L);
        } catch (Throwable th) {
            C0518a.i(0L);
            throw th;
        }
    }

    public void v() {
        UiThreadUtil.assertOnUiThread();
        J j3 = this.f9172b;
        if (j3 != null && this.f9177g) {
            j3.A(this);
            this.f9177g = false;
        }
        this.f9172b = null;
        this.f9178h = false;
    }

    public a0(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f9176f = 0;
        this.f9181k = new C0515x(this);
        this.f9182l = false;
        this.f9183m = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.f9184n = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.f9185o = 0;
        this.f9186p = 0;
        this.f9187q = Integer.MIN_VALUE;
        this.f9188r = Integer.MIN_VALUE;
        this.f9189s = 1;
        this.f9190t = new AtomicInteger(0);
        k();
    }

    public a0(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f9176f = 0;
        this.f9181k = new C0515x(this);
        this.f9182l = false;
        this.f9183m = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.f9184n = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.f9185o = 0;
        this.f9186p = 0;
        this.f9187q = Integer.MIN_VALUE;
        this.f9188r = Integer.MIN_VALUE;
        this.f9189s = 1;
        this.f9190t = new AtomicInteger(0);
        k();
    }

    @Override // com.facebook.react.uimanager.InterfaceC0447o0
    public ViewGroup getRootViewGroup() {
        return this;
    }

    public void setEventListener(b bVar) {
    }
}
