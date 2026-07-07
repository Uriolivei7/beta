package com.facebook.react.views.modal;

import D2.h;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStructure;
import android.view.Window;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.view.C0264n0;
import androidx.core.view.M0;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.InterfaceC0462w0;
import com.facebook.react.uimanager.Q;
import com.facebook.react.uimanager.S;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.view.g;
import com.facebook.react.views.view.p;
import d1.AbstractC0505m;
import d1.AbstractC0509q;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class c extends ViewGroup implements LifecycleEventListener {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final a f7716l = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Dialog f7717b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f7718c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private DialogInterface.OnShowListener f7719d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private InterfaceC0113c f7720e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f7721f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f7722g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private String f7723h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f7724i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final b f7725j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f7726k;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b extends g implements InterfaceC0462w0 {

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        private A0 f7727t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        private EventDispatcher f7728u;

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        private int f7729v;

        /* JADX INFO: renamed from: w, reason: collision with root package name */
        private int f7730w;

        /* JADX INFO: renamed from: x, reason: collision with root package name */
        private final S f7731x;

        /* JADX INFO: renamed from: y, reason: collision with root package name */
        private Q f7732y;

        public static final class a extends GuardedRunnable {
            a(B0 b02) {
                super(b02);
            }

            @Override // com.facebook.react.bridge.GuardedRunnable
            public void runGuarded() {
                UIManagerModule uIManagerModule = (UIManagerModule) b.this.getReactContext().b().getNativeModule(UIManagerModule.class);
                if (uIManagerModule != null) {
                    uIManagerModule.updateNodeSize(b.this.getId(), b.this.f7729v, b.this.f7730w);
                }
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(Context context) {
            super(context);
            h.f(context, "context");
            this.f7731x = new S(this);
            if (ReactFeatureFlags.dispatchPointerEvents) {
                this.f7732y = new Q(this);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final B0 getReactContext() {
            Context context = getContext();
            h.d(context, "null cannot be cast to non-null type com.facebook.react.uimanager.ThemedReactContext");
            return (B0) context;
        }

        public final void I(int i3, int i4) {
            C0429f0 c0429f0 = C0429f0.f7476a;
            float fD = c0429f0.d(i3);
            float fD2 = c0429f0.d(i4);
            A0 a02 = this.f7727t;
            if (a02 == null) {
                getReactContext().runOnNativeModulesQueueThread(new a(getReactContext()));
                return;
            }
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            writableNativeMap.putDouble("screenWidth", fD);
            writableNativeMap.putDouble("screenHeight", fD2);
            a02.b(writableNativeMap);
        }

        @Override // com.facebook.react.uimanager.InterfaceC0462w0
        public void b(View view, MotionEvent motionEvent) {
            h.f(view, "childView");
            h.f(motionEvent, "ev");
            EventDispatcher eventDispatcher = this.f7728u;
            if (eventDispatcher != null) {
                this.f7731x.e(motionEvent, eventDispatcher);
            }
            Q q3 = this.f7732y;
            if (q3 != null) {
                q3.o();
            }
        }

        @Override // com.facebook.react.uimanager.InterfaceC0462w0
        public void c(View view, MotionEvent motionEvent) {
            h.f(motionEvent, "ev");
            EventDispatcher eventDispatcher = this.f7728u;
            if (eventDispatcher != null) {
                this.f7731x.f(motionEvent, eventDispatcher);
                Q q3 = this.f7732y;
                if (q3 != null) {
                    q3.p(view, motionEvent, eventDispatcher);
                }
            }
        }

        public final EventDispatcher getEventDispatcher$ReactAndroid_release() {
            return this.f7728u;
        }

        public final A0 getStateWrapper$ReactAndroid_release() {
            return this.f7727t;
        }

        @Override // com.facebook.react.views.view.g, android.view.View
        public boolean onHoverEvent(MotionEvent motionEvent) {
            Q q3;
            h.f(motionEvent, "event");
            EventDispatcher eventDispatcher = this.f7728u;
            if (eventDispatcher != null && (q3 = this.f7732y) != null) {
                q3.k(motionEvent, eventDispatcher, false);
            }
            return super.onHoverEvent(motionEvent);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            h.f(accessibilityNodeInfo, "info");
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            String str = (String) getTag(AbstractC0505m.f9247t);
            if (str != null) {
                accessibilityNodeInfo.setViewIdResourceName(str);
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
            Q q3;
            h.f(motionEvent, "event");
            EventDispatcher eventDispatcher = this.f7728u;
            if (eventDispatcher != null && (q3 = this.f7732y) != null) {
                q3.k(motionEvent, eventDispatcher, true);
            }
            return super.onHoverEvent(motionEvent);
        }

        @Override // com.facebook.react.views.view.g, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            h.f(motionEvent, "event");
            EventDispatcher eventDispatcher = this.f7728u;
            if (eventDispatcher != null) {
                this.f7731x.c(motionEvent, eventDispatcher, getReactContext());
                Q q3 = this.f7732y;
                if (q3 != null) {
                    q3.k(motionEvent, eventDispatcher, true);
                }
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // com.facebook.react.views.view.g, android.view.View
        protected void onSizeChanged(int i3, int i4, int i5, int i6) {
            super.onSizeChanged(i3, i4, i5, i6);
            this.f7729v = i3;
            this.f7730w = i4;
            I(i3, i4);
        }

        @Override // com.facebook.react.views.view.g, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            h.f(motionEvent, "event");
            EventDispatcher eventDispatcher = this.f7728u;
            if (eventDispatcher != null) {
                this.f7731x.c(motionEvent, eventDispatcher, getReactContext());
                Q q3 = this.f7732y;
                if (q3 != null) {
                    q3.k(motionEvent, eventDispatcher, false);
                }
            }
            super.onTouchEvent(motionEvent);
            return true;
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z3) {
        }

        public final void setEventDispatcher$ReactAndroid_release(EventDispatcher eventDispatcher) {
            this.f7728u = eventDispatcher;
        }

        public final void setStateWrapper$ReactAndroid_release(A0 a02) {
            this.f7727t = a02;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.views.modal.c$c, reason: collision with other inner class name */
    public interface InterfaceC0113c {
        void a(DialogInterface dialogInterface);
    }

    public static final class d implements DialogInterface.OnKeyListener {
        d() {
        }

        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int i3, KeyEvent keyEvent) {
            h.f(dialogInterface, "dialog");
            h.f(keyEvent, "event");
            if (keyEvent.getAction() != 1) {
                return false;
            }
            if (i3 == 4 || i3 == 111) {
                InterfaceC0113c onRequestCloseListener = c.this.getOnRequestCloseListener();
                if (onRequestCloseListener == null) {
                    throw new IllegalStateException("onRequestClose callback must be set if back key is expected to close the modal");
                }
                onRequestCloseListener.a(dialogInterface);
                return true;
            }
            Context context = c.this.getContext();
            h.d(context, "null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
            Activity currentActivity = ((ReactContext) context).getCurrentActivity();
            if (currentActivity != null) {
                return currentActivity.onKeyUp(i3, keyEvent);
            }
            return false;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(B0 b02) {
        super(b02);
        h.f(b02, "context");
        this.f7725j = new b(b02);
    }

    private final void a() {
        Activity activity;
        UiThreadUtil.assertOnUiThread();
        Dialog dialog = this.f7717b;
        if (dialog != null) {
            if (dialog.isShowing() && ((activity = (Activity) V1.a.a(dialog.getContext(), Activity.class)) == null || !activity.isFinishing())) {
                dialog.dismiss();
            }
            this.f7717b = null;
            this.f7726k = true;
            ViewParent parent = this.f7725j.getParent();
            ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
            if (viewGroup != null) {
                viewGroup.removeViewAt(0);
            }
        }
    }

    private final boolean b(Activity activity) {
        return (activity == null || (activity.getWindow().getAttributes().flags & 8192) == 0) ? false : true;
    }

    private final void e(C0264n0 c0264n0, M0 m02, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Number) it.next()).intValue();
            if (c0264n0.o(iIntValue)) {
                if (m02 != null) {
                    m02.f(iIntValue);
                }
            } else if (m02 != null) {
                m02.a(iIntValue);
            }
        }
    }

    static /* synthetic */ void f(c cVar, C0264n0 c0264n0, M0 m02, List list, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            list = AbstractC0717n.j(Integer.valueOf(C0264n0.m.d()), Integer.valueOf(C0264n0.m.c()));
        }
        cVar.e(c0264n0, m02, list);
    }

    private final void g() {
        Dialog dialog = this.f7717b;
        if (dialog == null) {
            throw new IllegalStateException("dialog must exist when we call updateProperties");
        }
        Window window = dialog.getWindow();
        if (window == null) {
            throw new IllegalStateException("dialog must have window when we call updateProperties");
        }
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null || currentActivity.isFinishing() || currentActivity.isDestroyed()) {
            return;
        }
        try {
            Window window2 = currentActivity.getWindow();
            if (window2 != null) {
                if ((window2.getAttributes().flags & 1024) != 0) {
                    window.addFlags(1024);
                } else {
                    window.clearFlags(1024);
                }
            }
            p.e(window, this.f7722g);
            if (!this.f7722g) {
                p.b(window, this.f7721f);
            }
            if (this.f7718c) {
                window.clearFlags(2);
            } else {
                window.setDimAmount(0.5f);
                window.setFlags(2, 2);
            }
        } catch (IllegalArgumentException e4) {
            Y.a.o("ReactNative", "ReactModalHostView: error while setting window flags: ", e4.getMessage());
        }
    }

    private final View getContentView() {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.addView(this.f7725j);
        if (!this.f7721f) {
            frameLayout.setFitsSystemWindows(true);
        }
        return frameLayout;
    }

    private final Activity getCurrentActivity() {
        Context context = getContext();
        h.d(context, "null cannot be cast to non-null type com.facebook.react.uimanager.ThemedReactContext");
        return ((B0) context).getCurrentActivity();
    }

    private final void h() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        Dialog dialog = this.f7717b;
        if (dialog == null) {
            throw new IllegalStateException("dialog must exist when we call updateProperties");
        }
        Window window = dialog.getWindow();
        if (window == null) {
            throw new IllegalStateException("dialog must have window when we call updateProperties");
        }
        Window window2 = currentActivity.getWindow();
        if (Build.VERSION.SDK_INT <= 30) {
            window.getDecorView().setSystemUiVisibility(window2.getDecorView().getSystemUiVisibility());
            return;
        }
        M0 m02 = new M0(window2, window2.getDecorView());
        M0 m03 = new M0(window, window.getDecorView());
        m03.d(m02.b());
        WindowInsets rootWindowInsets = window2.getDecorView().getRootWindowInsets();
        if (rootWindowInsets != null) {
            C0264n0 c0264n0V = C0264n0.v(rootWindowInsets);
            h.e(c0264n0V, "toWindowInsetsCompat(...)");
            f(this, c0264n0V, m03, null, 4, null);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addChildrenForAccessibility(ArrayList arrayList) {
        h.f(arrayList, "outChildren");
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i3) {
        UiThreadUtil.assertOnUiThread();
        this.f7725j.addView(view, i3);
    }

    public final void c() {
        Context context = getContext();
        h.d(context, "null cannot be cast to non-null type com.facebook.react.uimanager.ThemedReactContext");
        ((B0) context).removeLifecycleEventListener(this);
        a();
    }

    public final void d() {
        Window window;
        Window window2;
        UiThreadUtil.assertOnUiThread();
        if (!this.f7726k) {
            g();
            return;
        }
        a();
        this.f7726k = false;
        String str = this.f7723h;
        int i3 = h.b(str, "fade") ? AbstractC0509q.f9304e : h.b(str, "slide") ? AbstractC0509q.f9305f : AbstractC0509q.f9303d;
        Activity currentActivity = getCurrentActivity();
        Dialog dialog = new Dialog(currentActivity != null ? currentActivity : getContext(), i3);
        this.f7717b = dialog;
        Window window3 = dialog.getWindow();
        Objects.requireNonNull(window3);
        window3.setFlags(8, 8);
        dialog.setContentView(getContentView());
        g();
        dialog.setOnShowListener(this.f7719d);
        dialog.setOnKeyListener(new d());
        Window window4 = dialog.getWindow();
        if (window4 != null) {
            window4.setSoftInputMode(16);
        }
        if (this.f7724i && (window2 = dialog.getWindow()) != null) {
            window2.addFlags(16777216);
        }
        if (b(currentActivity) && (window = dialog.getWindow()) != null) {
            window.setFlags(8192, 8192);
        }
        if (currentActivity == null || currentActivity.isFinishing()) {
            return;
        }
        dialog.show();
        h();
        Window window5 = dialog.getWindow();
        if (window5 != null) {
            window5.clearFlags(8);
        }
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        h.f(accessibilityEvent, "event");
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchProvideStructure(ViewStructure viewStructure) {
        h.f(viewStructure, "structure");
        this.f7725j.dispatchProvideStructure(viewStructure);
    }

    public final String getAnimationType() {
        return this.f7723h;
    }

    @Override // android.view.ViewGroup
    public View getChildAt(int i3) {
        return this.f7725j.getChildAt(i3);
    }

    @Override // android.view.ViewGroup
    public int getChildCount() {
        return this.f7725j.getChildCount();
    }

    public final Dialog getDialog() {
        return this.f7717b;
    }

    public final EventDispatcher getEventDispatcher() {
        return this.f7725j.getEventDispatcher$ReactAndroid_release();
    }

    public final boolean getHardwareAccelerated() {
        return this.f7724i;
    }

    public final boolean getNavigationBarTranslucent() {
        return this.f7722g;
    }

    public final InterfaceC0113c getOnRequestCloseListener() {
        return this.f7720e;
    }

    public final DialogInterface.OnShowListener getOnShowListener() {
        return this.f7719d;
    }

    public final A0 getStateWrapper() {
        return this.f7725j.getStateWrapper$ReactAndroid_release();
    }

    public final boolean getStatusBarTranslucent() {
        return this.f7721f;
    }

    public final boolean getTransparent() {
        return this.f7718c;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Context context = getContext();
        h.d(context, "null cannot be cast to non-null type com.facebook.react.uimanager.ThemedReactContext");
        ((B0) context).addLifecycleEventListener(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        c();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        c();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        d();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        UiThreadUtil.assertOnUiThread();
        if (view != null) {
            this.f7725j.removeView(view);
        }
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int i3) {
        UiThreadUtil.assertOnUiThread();
        this.f7725j.removeView(getChildAt(i3));
    }

    public final void setAnimationType(String str) {
        this.f7723h = str;
        this.f7726k = true;
    }

    public final void setDialogRootViewGroupTestId(String str) {
        this.f7725j.setTag(AbstractC0505m.f9247t, str);
    }

    public final void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.f7725j.setEventDispatcher$ReactAndroid_release(eventDispatcher);
    }

    public final void setHardwareAccelerated(boolean z3) {
        this.f7724i = z3;
        this.f7726k = true;
    }

    @Override // android.view.View
    public void setId(int i3) {
        super.setId(i3);
        this.f7725j.setId(i3);
    }

    public final void setNavigationBarTranslucent(boolean z3) {
        this.f7722g = z3;
        this.f7726k = true;
    }

    public final void setOnRequestCloseListener(InterfaceC0113c interfaceC0113c) {
        this.f7720e = interfaceC0113c;
    }

    public final void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.f7719d = onShowListener;
    }

    public final void setStateWrapper(A0 a02) {
        this.f7725j.setStateWrapper$ReactAndroid_release(a02);
    }

    public final void setStatusBarTranslucent(boolean z3) {
        this.f7721f = z3;
        this.f7726k = true;
    }

    public final void setTransparent(boolean z3) {
        this.f7718c = z3;
    }
}
